package com.snail.webgame.game.protocal.fight.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.snail.webgame.game.common.FightInfo;
import com.snail.webgame.game.common.fightdata.FightArmyDataInfo;
import com.snail.webgame.game.common.fightdata.FightSideData;
import com.snail.webgame.game.common.fightdata.HeroSkillDataInfo;
import com.snail.webgame.game.common.xml.cache.HeroXMLInfoMap;
import com.snail.webgame.game.common.xml.info.HeroXMLInfo;
import com.snail.webgame.game.common.xml.info.SkillBuffInfoLoader;
import com.snail.webgame.game.common.xml.info.SkillBuffXmlInfo;
import com.snail.webgame.game.common.xml.info.SkillBuffXmlInfoWrap;
import com.snail.webgame.game.common.xml.info.SkillInfoLoader;
import com.snail.webgame.game.common.xml.info.SkillRangeType;
import com.snail.webgame.game.common.xml.info.SkillXmlInfo;
import com.snail.webgame.game.common.xml.info.SkillXmlInfo.TargetSide;
import com.snail.webgame.game.common.xml.info.SkillXmlInfo.TargetType;
import com.snail.webgame.game.common.xml.info.States;
import com.snail.webgame.game.protocal.fight.checkFight.CheckBuffInfo;
import com.snail.webgame.game.protocal.fight.checkFight.CheckFightReq;
import com.snail.webgame.game.protocal.fight.checkFight.CheckSkillInfo;
import com.snail.webgame.game.protocal.fight.startFight.IntoFightResp;

public class CheckPVEService {
	
	//private static final Logger logger = LoggerFactory.getLogger("logs");
	
	/**
	 * 释放技能
	 */
	public static void pveReleaseSkill(int roleId,FightInfo fightInfo,CheckFightReq req)
	{
		 SkillXmlInfo skillXmlInfo = SkillInfoLoader.getSkillInfo(req.getSkillNo());
		 if(req.getSkillNo() == 53000222)
		 {
			 skillXmlInfo = SkillInfoLoader.getSkillInfo(530002220);
		 }
		 
		 if(skillXmlInfo == null)
		 {
			 return;
		 }
		 
		 FightArmyDataInfo armyInfo0 = null;//
		 FightArmyDataInfo armyInfo = null;//
		 //FightArmyDataInfo armyInfo1 = null;//副将给主将的天命技能,此为副将(计算时以副将属性为基础,然后加成到主将)
		 CheckSkillInfo skillInfo = null;
		 Map<Integer,CheckSkillInfo> skillMap = fightInfo.getSkillMap();
		 if(skillMap != null && skillMap.containsKey(req.getSkillNo()))
		 {
			 long armyId = 0;
			 long armyId1 = 0;
			 skillInfo = skillMap.get(req.getSkillNo());
			 if(skillInfo.getHeroId() != 0)
			 {
				 armyId = skillInfo.getArmyId();
				 armyId1 = skillInfo.getHeroId();
			 }
			 else
			 {
				 armyId = skillInfo.getArmyId();
				 armyId1 = skillInfo.getArmyId();
			 }
			 
			 if(fightInfo.getSide0ArmyMap().containsKey(armyId))
			 {
				 armyInfo = fightInfo.getSide0ArmyMap().get(armyId);
			 }
			 
			 if(fightInfo.getSide0ArmyMap().containsKey(armyId1))
			 {
				 //armyInfo1 = fightInfo.getSide0ArmyMap().get(armyId1);
			 }
		 }
		 
		 
		 
		//普通攻击或技能伤害
		 if(skillInfo == null || armyInfo == null/*|| armyInfo1 == null*/)
		 {
			 return;
		 }
		 
		 if (skillXmlInfo.getBufferList().size() > 0)
		 {
			 for (SkillBuffXmlInfoWrap buffXmlInfoWrap : skillXmlInfo.getBufferList())
			 {
				 SkillBuffXmlInfo buffInfo = buffXmlInfoWrap.getBuffXmlInfo();
				 if(buffInfo == null)
				 {
					 continue;
				 }
				 
				 if(!(buffInfo.getFunEnum().getValue() == 8 || buffInfo.getFunEnum().getValue() == 9 ||
						 buffInfo.getFunEnum().getValue() == 10 || buffInfo.getFunEnum().getValue() == 11 ||
						 buffInfo.getFunEnum().getValue() == 32))
				 {
					 continue;
				 }
				 
				// 目标检索
				 if(buffXmlInfoWrap.getTargetSide() == TargetSide.OTHER)
				 {
					 continue;
				 }
				 
				/* if(req.getSkillNo() == 53000551)
				 {
					 System.out.println("===========,target="+skillXmlInfo.getRequireTarget()+",rang="+skillXmlInfo.getRangeType().ordinal()
							 +",side="+skillXmlInfo.getTargetSide().ordinal());
				 }*/
				 // 获取受到Buff效果的部队
				 List<FightArmyDataInfo> armyList = getBuffArmyList(fightInfo,armyInfo,skillXmlInfo,buffXmlInfoWrap);
				 
				 if(armyList != null && armyList.size() > 0)
				 {
					 for(FightArmyDataInfo armyInfo1 : armyList)
					 {
						 armyInfo0 = getArmyInfo(fightInfo,armyInfo1.getId());
						 if(armyInfo0 == null)
						 {
							 continue;
						 }
						 
						 releaseBuffToArmy(fightInfo,armyInfo1,armyInfo0,buffInfo,skillInfo);
					 }
			     }
			 }
			 
		 }
	}
	
	
	/**
	 * 检查BUFF CD
	 * @param fightInfo
	 */
	public static void checkFightSkillCD(FightInfo fightInfo)
	{
		Map<Long,HashMap<Integer,CheckBuffInfo>> armyBuffMap= fightInfo.getBuffMap();
		if(armyBuffMap != null && armyBuffMap.size() > 0)
		{
			for(long armyIds : armyBuffMap.keySet())
			{
				HashMap<Integer,CheckBuffInfo> buffMap = armyBuffMap.get(armyIds);
				if(buffMap != null && buffMap.size() > 0)
				{
					Iterator<Integer> iter = buffMap.keySet().iterator();
					while(iter.hasNext())
					{
						int buffNo = iter.next();
						CheckBuffInfo buffInfo = buffMap.get(buffNo);
						if(buffInfo == null)
						{
							continue;
						}
						
						long cdTime = System.currentTimeMillis() - buffInfo.getBuffReleaseTime();
						if(buffInfo.getDuration() > cdTime)
						{
							continue;
						}
						iter.remove();
						
						FightArmyDataInfo armyInfo0 = null;//原始对像,加属性后再减属性用到
						FightArmyDataInfo armyInfo = null;//
						 
						long armyId = buffInfo.getArmyId();
						 
						 if(fightInfo.getSide0ArmyMap().containsKey(armyId))
						 {
							 armyInfo = fightInfo.getSide0ArmyMap().get(armyId);
						 }
						 
						 armyInfo0 = getArmyInfo(fightInfo,buffInfo.getArmyId());
						 
						 
						//普通攻击或技能伤害
						 if(armyInfo == null || armyInfo0 == null)
						 {
							 return;
						 }
						 
						 SkillBuffXmlInfo buffXmlInfo= SkillBuffInfoLoader.getSkillBuffer(buffInfo.getBuffNo());
						 
						 if(buffInfo.getFunEnum().getValue() == 8 || buffInfo.getFunEnum().getValue() == 9 ||
								 buffInfo.getFunEnum().getValue() == 10 || buffInfo.getFunEnum().getValue() == 11 ||
								 buffInfo.getFunEnum().getValue() == 32)
						 {
							 removeBuff(armyInfo0,armyInfo,buffXmlInfo,buffInfo.getBuffLv(),buffInfo.getFunEnum().getValue());
						 }
					}
				}
			}
		}
	}
	
	/**
	 * 添加BUff
	 * @param armyInfo
	 * @param buffXml
	 */
	public static void addBuff(FightArmyDataInfo armyInfo,FightArmyDataInfo addArmyInfo,SkillBuffXmlInfo buffXml,int level,int type)
	{
		float cBuffPrecent = buffXml.getActualPercent(level);
		float cBuffValue = buffXml.getActualValue(level);
		
		if(cBuffPrecent < 0 || cBuffValue < 0)
		{
			//System.out.println("111#####,buffNo="+buffXml.getNo()+",level="+level+",cBuffPrecent="+cBuffPrecent);
			return;
		}
		
		int changeValue = 0;
		switch(type)
		{
			case 8://物理攻击
				changeValue = Math.round((armyInfo.getAd() * cBuffPrecent) + cBuffValue);
				addArmyInfo.setAd(addArmyInfo.getAd()+changeValue);
				
				//logger.info("1---add buff,armyId="+armyInfo.getId()+",buffNo="+buffXml.getNo()+",cBuffPrecent="+cBuffPrecent+",cBuffValue="+cBuffValue);
				break;
			case 9://法术攻击
				changeValue = Math.round((armyInfo.getMagicAttack() * cBuffPrecent) + cBuffValue);
				addArmyInfo.setMagicAttack(addArmyInfo.getMagicAttack()+changeValue);
				
				//logger.info("2---add buff,armyId="+armyInfo.getId()+",buffNo="+buffXml.getNo()+",cBuffPrecent="+cBuffPrecent+",cBuffValue="+cBuffValue);
				break;
			case 10: //物理防御
				changeValue = Math.round((armyInfo.getAttackDef() * cBuffPrecent) + cBuffValue);
				addArmyInfo.setAttackDef(addArmyInfo.getAttackDef()+changeValue);
				
				//logger.info("3---add buff,armyId="+armyInfo.getId()+",buffNo="+buffXml.getNo()+",cBuffPrecent="+cBuffPrecent+",cBuffValue="+cBuffValue);
				break;
			case 11:
				changeValue = Math.round((armyInfo.getMagicDef() * cBuffPrecent) + cBuffValue);
				addArmyInfo.setMagicDef(addArmyInfo.getMagicDef()+changeValue);
				
				//logger.info("4---add buff,armyId="+armyInfo.getId()+",buffNo="+buffXml.getNo()+",cBuffPrecent="+cBuffPrecent+",cBuffValue="+cBuffValue);
				break;
			case 32:
				changeValue = Math.round((armyInfo.getAd() * cBuffPrecent) + cBuffValue);
				addArmyInfo.setAd(addArmyInfo.getAd()+changeValue);
				
				//logger.info("5---add buff armyId="+armyInfo.getId()+",buffNo="+buffXml.getNo()+",cBuffPrecent="+cBuffPrecent+",cBuffValue="+cBuffValue);
				break;
		}
		
	}
	
	/**
	 * 移除BUFF
	 * @param armyInfo
	 * @param buffXml
	 */
	public static void removeBuff(FightArmyDataInfo armyInfo,FightArmyDataInfo removeArmyInfo,SkillBuffXmlInfo buffXml,int level,int type)
	{
		float cBuffPrecent = buffXml.getActualPercent(level);
		float cBuffValue = buffXml.getActualValue(level);
		
		if(cBuffPrecent < 0 || cBuffValue < 0)
		{
			//System.out.println("222#####,buffNo="+buffXml.getNo()+",level="+level+",cBuffPrecent="+cBuffPrecent);
			return;
		}
		
		int changeValue = 0;
		switch(type)
		{
			case 8://物理攻击
				changeValue = Math.round((armyInfo.getAd() * cBuffPrecent) + cBuffValue);
				removeArmyInfo.setAd(removeArmyInfo.getAd()-changeValue);
				
				//logger.info("11---remove buff,armyId="+armyInfo.getId()+",buffNo="+buffXml.getNo()+",cBuffPrecent="+cBuffPrecent+",cBuffValue="+cBuffValue);
				break;
			case 9://法术攻击
				changeValue = Math.round((armyInfo.getMagicAttack() * cBuffPrecent) + cBuffValue);
				removeArmyInfo.setMagicAttack(removeArmyInfo.getMagicAttack()-changeValue);
				
				//logger.info("22---remove buff,armyId="+armyInfo.getId()+",buffNo="+buffXml.getNo()+",cBuffPrecent="+cBuffPrecent+",cBuffValue="+cBuffValue);
				break;
			case 10: //物理防御
				changeValue = Math.round((armyInfo.getAttackDef() * cBuffPrecent) + cBuffValue);
				removeArmyInfo.setAttackDef(removeArmyInfo.getAttackDef()-changeValue);
				
				//logger.info("33---remove buff,armyId="+armyInfo.getId()+",buffNo="+buffXml.getNo()+",cBuffPrecent="+cBuffPrecent+",cBuffValue="+cBuffValue);
				break;
			case 11:
				changeValue = Math.round((armyInfo.getMagicDef() * cBuffPrecent) + cBuffValue);
				removeArmyInfo.setMagicDef(removeArmyInfo.getMagicDef()-changeValue);
				
				//logger.info("44---remove buff,armyId="+armyInfo.getId()+",buffNo="+buffXml.getNo()+",cBuffPrecent="+cBuffPrecent+",cBuffValue="+cBuffValue);
				break;
			case 32://物理攻击
				changeValue = Math.round((armyInfo.getAd() * cBuffPrecent) + cBuffValue);
				removeArmyInfo.setAd(removeArmyInfo.getAd()-changeValue);
				
				//logger.info("55---remove buff,armyId="+armyInfo.getId()+",buffNo="+buffXml.getNo()+",cBuffPrecent="+cBuffPrecent+",cBuffValue="+cBuffValue);
				break;
		}
	}
	
	
	/**
	 * 将我方技能，部队归类，方便后期检测
	 * @param fightReq
	 * @param fightInfo
	 */
	public static void setSide0DataAllSkill(IntoFightResp fightReq,FightInfo fightInfo)
	{
		List<FightSideData> sizeList = fightReq.getSizeList();
		if(sizeList == null || sizeList.size() < 1)
		{
			return;
		}
		
		for(FightSideData sideData : sizeList)
		{
			if(sideData == null)
			{
				continue;
			}
			
			if(sideData.getSideId() == 0)
			{
				List<FightArmyDataInfo> armyInfos = sideData.getArmyInfos();
				for(FightArmyDataInfo armyInfo : armyInfos)
				{
					List<HeroSkillDataInfo> heroSkills = armyInfo.getHeroSkills();
					for(HeroSkillDataInfo skillData : heroSkills)
					{
						CheckSkillInfo skillInfo = new CheckSkillInfo(skillData.getSkillNo(),0);
						skillInfo.setSkillLv(skillData.getSkillLv());
						skillInfo.setArmyId(armyInfo.getId());
						fightInfo.getSkillMap().put(skillInfo.getSkillNo(),skillInfo);
						skillInfo.setHeroId(skillData.getHeroId());
					}
					
					fightInfo.getSide0ArmyMap().put(armyInfo.getId(), (FightArmyDataInfo) armyInfo.clone());
				}
			}
			
			if(sideData.getSideId() == 1)
			{
				List<FightArmyDataInfo> armyInfos = sideData.getArmyInfos();
				for(FightArmyDataInfo armyInfo : armyInfos)
				{
					fightInfo.getSide1ArmyMap().put(armyInfo.getId(), (FightArmyDataInfo) armyInfo.clone());
				}
			}
			
		}
		
	}
	
	/**
	 * 获取我方某单位
	 * @param fightInfo
	 * @param armyId
	 * @return
	 */
	public static FightArmyDataInfo getArmyInfo(FightInfo fightInfo,long armyId)
	{
		List<FightSideData> fightDataList = fightInfo.getFightDataList();
		for(FightSideData sideData: fightDataList )
		{
			if(sideData != null && sideData.getSideId() == 0)
			{
				List<FightArmyDataInfo> armyInfos = sideData.getArmyInfos();
				for(FightArmyDataInfo armyData : armyInfos)
				{
					if(armyData != null && armyData.getId() == armyId)
					{
						return armyData;
					}
				}
			}
		}
		
		return null;
	}
	
	/**
	 * 添加BUFF
	 * @param fightInfo
	 * @param buffInfo
	 * @param armyid
	 * @param skillLv
	 */
	public static void addBuffToArmy(FightInfo fightInfo,SkillBuffXmlInfo buffInfo,Long armyId,int skillLv)
	{
		//添加部队BUFF
		CheckBuffInfo checkBuff = new CheckBuffInfo();
		
		checkBuff.setBuffNo(buffInfo.getNo());
		checkBuff.setArmyId(armyId);
		checkBuff.setBuffReleaseTime(System.currentTimeMillis());
		checkBuff.setDuration(buffInfo.getDuration());
		checkBuff.setFunEnum(buffInfo.getFunEnum());
		checkBuff.setBuffLv(skillLv);
		
		if(fightInfo.getBuffMap().containsKey(checkBuff.getArmyId()))
		{
			HashMap<Integer,CheckBuffInfo> buffMap = fightInfo.getBuffMap().get(checkBuff.getArmyId());
			buffMap.put(checkBuff.getBuffNo(), checkBuff);
		}
		else
		{
			HashMap<Integer,CheckBuffInfo> buffMap = new HashMap<Integer,CheckBuffInfo>();
			buffMap.put(checkBuff.getBuffNo(), checkBuff);
			
			fightInfo.getBuffMap().put(checkBuff.getArmyId(), buffMap);
		}
	}
	
	/**
	 * 给单位添加Buff
	 * @param fightInfo
	 * @param addAarmyInfo
	 * @param armyInfo0
	 * @param buffInfo
	 * @param skillInfo
	 */
	public static void releaseBuffToArmy(FightInfo fightInfo, FightArmyDataInfo addArmyInfo,FightArmyDataInfo armyInfo0,SkillBuffXmlInfo buffInfo,CheckSkillInfo skillInfo)
	{
		// 相同BUFF不叠加,只会延长时间
		if(fightInfo.getBuffMap().containsKey(addArmyInfo.getId()))
		{
			HashMap<Integer,CheckBuffInfo> buffMap = fightInfo.getBuffMap().get(addArmyInfo.getId());
			if(buffMap != null && buffMap.containsKey(buffInfo.getNo()))
			{
				CheckBuffInfo checkBuff = buffMap.get(buffInfo.getNo());
				if(checkBuff != null)
				{
					checkBuff.setBuffReleaseTime(System.currentTimeMillis());
					return;
				}
			}
			
			//同种效果的BUFF只看加效果大的
			Iterator<Integer> iter = buffMap.keySet().iterator();
			while(iter.hasNext())
			{
				int oldBuffNo = iter.next();
				CheckBuffInfo oldSkill = buffMap.get(oldBuffNo);
				SkillBuffXmlInfo oldBuffXml = SkillBuffInfoLoader.getSkillBuffer(oldBuffNo);
				if(oldBuffXml.getFunEnum() == buffInfo.getFunEnum() && oldSkill != null)
				{
					//logger.info("#### will add,armyId="+addArmyInfo.getId()+",buffNo="+buffInfo.getNo());
					float oldCBuffPrecent = oldBuffXml.getActualPercent(oldSkill.getBuffLv());
					float oldCBuffValue = oldBuffXml.getActualValue(oldSkill.getBuffLv());
					
					//int oldChangeValue = Math.round((armyInfo0.getMagicAttack() * oldCBuffPrecent) + oldCBuffValue);
					
					float newCBuffPrecent = buffInfo.getActualPercent(skillInfo.getSkillLv());
					float newCBuffValue = buffInfo.getActualValue(skillInfo.getSkillLv());
					
					//int newChangeValue = Math.round((armyInfo0.getMagicAttack() * newCBuffPrecent) + newCBuffValue);
					
					/*if(newChangeValue > oldChangeValue)
					{
						removeBuff(armyInfo0,addArmyInfo,oldBuffXml,oldSkill.getBuffLv(),oldSkill.getFunEnum().ordinal());
						iter.remove();
						
						break;
					}*/
					if(newCBuffPrecent > oldCBuffPrecent && newCBuffValue > oldCBuffValue)
					{
						//改成和客户端同步
						removeBuff(armyInfo0,addArmyInfo,oldBuffXml,oldSkill.getBuffLv(),oldSkill.getFunEnum().ordinal());
						iter.remove();
						
						break;
					}
					else
					{
						return;
					}
				}
			}
		}
		
		
		addBuff(armyInfo0,addArmyInfo,buffInfo,skillInfo.getSkillLv(),buffInfo.getFunEnum().getValue());
		
		//添加部队BUFF
		addBuffToArmy(fightInfo,buffInfo,addArmyInfo.getId(),skillInfo.getSkillLv());
	
	}
	
	/**
	 * 获取受到Buff效果的部队
	 */
	public static List<FightArmyDataInfo> getBuffArmyList(FightInfo fightInfo,FightArmyDataInfo armyInfo,SkillXmlInfo skillXmlInfo,SkillBuffXmlInfoWrap buffXmlInfoWrap)
	{
		List<FightArmyDataInfo> armyList = new ArrayList<FightArmyDataInfo>();
		 
		 /*
		  * Object:0-不需要目标（默认目标为自己） 1-需要目标（指向类技能） 
		 	RangeType:目标检索范围类型  1-单体（对单个目标有效） 4-圆形  5-全屏
	     	ObjectSide:目标阵营
	        	0-不分阵营 1-已方 2-敌方
	     	ObjectState:目标国家
	        	0-不分国家 1-魏 2-蜀 3-吴 4-群雄
	     	ObjectSex:目标性别
	        	-1-不分性别 0-男 1-女
	     	ObjectType:目标类型  0-所有 1-主将 2-除主将外的所有 
		 
		 */
	     // 目标检索
		 
		 // 如果isUseSkillTarget 为true看技能的配置，为false看自身配置
		 if(buffXmlInfoWrap.isUseSkillTarget())
		 {
			 if(skillXmlInfo.getRangeType()== SkillRangeType.target)
			 {
				 //System.out.println("111,buffNo="+buffInfo.getNo());
				 armyList.add(armyInfo);
			 }
			 else if(skillXmlInfo.getRangeType() == SkillRangeType.full_screen
					 && skillXmlInfo.getTargetType() == TargetType.ALL)
			 {
				 // 不分国家
				 if(skillXmlInfo.getTargetState() == States.NULL)
				 {
					 for(long armyId1 : fightInfo.getSide0ArmyMap().keySet())
					 {
						 FightArmyDataInfo armyInfo3 = fightInfo.getSide0ArmyMap().get(armyId1);
						 if(armyInfo3 == null)
						 {
							 continue;
						 }
						 //System.out.println("222,buffNo="+buffInfo.getNo());
						 armyList.add(armyInfo3);
					 } 
				 }
				 else
				 {
					// 分国家
					 for(long armyId1 : fightInfo.getSide0ArmyMap().keySet())
					 {
						 //System.out.println("333,buffNo="+buffInfo.getNo());
						 FightArmyDataInfo armyInfo3 = fightInfo.getSide0ArmyMap().get(armyId1);
						 if(armyInfo3 == null)
						 {
							 continue;
						 }
						 
						 HeroXMLInfo heroXmlInfo = HeroXMLInfoMap.getHeroXMLInfo(armyInfo3.getHeroNo());
						 
						 if(heroXmlInfo == null || heroXmlInfo.getRace() != skillXmlInfo.getTargetState().ordinal())
						 {
							 continue;
						 }
						 
						 armyList.add(armyInfo3);
					 }
				 }
			 }
			 else if(skillXmlInfo.getRangeType() == SkillRangeType.full_screen
					 && skillXmlInfo.getTargetType() == TargetType.ALL_EXCEPT_MAIN)
			 {
				 // 不分国家
				 if(skillXmlInfo.getTargetState() == States.NULL)
				 {
					 for(long armyId1 : fightInfo.getSide0ArmyMap().keySet())
					 {
						 FightArmyDataInfo armyInfo3 = fightInfo.getSide0ArmyMap().get(armyId1);
						 if(armyInfo3 == null)
						 {
							 continue;
						 }
						 //System.out.println("222,buffNo="+buffInfo.getNo());
						 armyList.add(armyInfo3);
					 } 
				 }
				 else
				 {
					// 分国家
					 for(long armyId1 : fightInfo.getSide0ArmyMap().keySet())
					 {
						 //System.out.println("333,buffNo="+buffInfo.getNo());
						 FightArmyDataInfo armyInfo3 = fightInfo.getSide0ArmyMap().get(armyId1);
						 if(armyInfo3 == null)
						 {
							 continue;
						 }
						 
						 HeroXMLInfo heroXmlInfo = HeroXMLInfoMap.getHeroXMLInfo(armyInfo3.getHeroNo());
						 
						 if(heroXmlInfo == null || heroXmlInfo.getRace() != skillXmlInfo.getTargetState().ordinal())
						 {
							 continue;
						 }
						 
						 armyList.add(armyInfo3);
					 }
				 }
			 
			 }
			 else if(skillXmlInfo.getRangeType() == SkillRangeType.full_screen
					 && skillXmlInfo.getTargetType() == TargetType.MAIN_HERO)
			 {
				 //System.out.println("444,buffNo="+buffInfo.getNo());
				 armyList.add(armyInfo);
			 }
		 }
		 else
		 {
			 if(buffXmlInfoWrap.getRangeType()== SkillRangeType.target)
			 {
				 //System.out.println("555,buffNo="+buffInfo.getNo());
				 armyList.add(armyInfo);
			 }
			 else if(buffXmlInfoWrap.getRangeType() == SkillRangeType.full_screen
					 && buffXmlInfoWrap.getTargetType() == TargetType.ALL)
			 {
				 // 不分国家
				 if(buffXmlInfoWrap.getTargetState() == States.NULL)
				 {
					 //System.out.println("666,buffNo="+buffInfo.getNo());
					 for(long armyId1 : fightInfo.getSide0ArmyMap().keySet())
					 {
						 FightArmyDataInfo armyInfo3 = fightInfo.getSide0ArmyMap().get(armyId1);
						 if(armyInfo3 == null)
						 {
							 continue;
						 }
						 
						 armyList.add(armyInfo3);
					 } 
				 }
				 else
				 {
					// 分国家
					 for(long armyId1 : fightInfo.getSide0ArmyMap().keySet())
					 {
						 //System.out.println("777,buffNo="+buffInfo.getNo());
						 FightArmyDataInfo armyInfo3 = fightInfo.getSide0ArmyMap().get(armyId1);
						 if(armyInfo3 == null)
						 {
							 continue;
						 }
						 
						 HeroXMLInfo heroXmlInfo = HeroXMLInfoMap.getHeroXMLInfo(armyInfo3.getHeroNo());
						 
						 if(heroXmlInfo == null || heroXmlInfo.getRace() != skillXmlInfo.getTargetState().ordinal())
						 {
							 continue;
						 }
						 
						 armyList.add(armyInfo3);
					 }
				 }
			 }
			 else if(buffXmlInfoWrap.getRangeType() == SkillRangeType.full_screen
					 && buffXmlInfoWrap.getTargetType() == TargetType.MAIN_HERO)
			 {
				 //System.out.println("888,buffNo="+buffInfo.getNo());
				 // 全屏主武将
				 armyList.add(armyInfo);
			 }
		 }
		 
		 return armyList;
	}

}
