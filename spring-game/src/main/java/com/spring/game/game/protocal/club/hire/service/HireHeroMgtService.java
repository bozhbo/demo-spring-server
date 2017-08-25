package com.snail.webgame.game.protocal.club.hire.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.snail.webgame.game.cache.HeroInfoMap;
import com.snail.webgame.game.cache.RoleClubInfoMap;
import com.snail.webgame.game.cache.RoleClubMemberInfoMap;
import com.snail.webgame.game.cache.RoleInfoMap;
import com.snail.webgame.game.common.ActionType;
import com.snail.webgame.game.common.ErrorCode;
import com.snail.webgame.game.common.GameFlag;
import com.snail.webgame.game.common.GameValue;
import com.snail.webgame.game.condtion.ConditionType;
import com.snail.webgame.game.core.GameLogService;
import com.snail.webgame.game.dao.ClubHireHeroDao;
import com.snail.webgame.game.info.HeroImageInfo;
import com.snail.webgame.game.info.HeroInfo;
import com.snail.webgame.game.info.HireHeroInfo;
import com.snail.webgame.game.info.RoleClubInfo;
import com.snail.webgame.game.info.RoleClubMemberInfo;
import com.snail.webgame.game.info.RoleInfo;
import com.snail.webgame.game.info.RoleLoadInfo;
import com.snail.webgame.game.protocal.club.hire.entity.HireHeroInfoRe;
import com.snail.webgame.game.protocal.club.hire.info.GetClubHireHeroInfoReq;
import com.snail.webgame.game.protocal.club.hire.info.GetClubHireHeroInfoResp;
import com.snail.webgame.game.protocal.club.hire.operation.HireHeroOperationReq;
import com.snail.webgame.game.protocal.club.hire.operation.HireHeroOperationResp;
import com.snail.webgame.game.protocal.equip.query.EquipDetailRe;
import com.snail.webgame.game.protocal.equip.query.EquipInfoRe;
import com.snail.webgame.game.protocal.equip.service.EquipService;
import com.snail.webgame.game.protocal.fight.fightend.BattlePrize;
import com.snail.webgame.game.protocal.hero.query.HeroSkillRe;
import com.snail.webgame.game.protocal.hero.service.HeroService;
import com.snail.webgame.game.protocal.rolemgt.service.RoleService;

public class HireHeroMgtService {

	/**
	 * 获取公会的佣兵
	 * @param roleId
	 * @param req
	 * @return
	 */
	public GetClubHireHeroInfoResp getHireHeroInfo(int roleId, GetClubHireHeroInfoReq req) {
		GetClubHireHeroInfoResp resp = new GetClubHireHeroInfoResp();
		resp.setFlag(req.getFlag());
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			resp.setResult(ErrorCode.ROLE_CLUB_ERROR_1);
			return resp;
		}

		synchronized (roleInfo) {
			RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
			if (roleLoadInfo == null) {
				resp.setResult(ErrorCode.ROLE_CLUB_ERROR_2);
				return resp;
			}
			
			List<HireHeroInfoRe> list = new ArrayList<HireHeroInfoRe>();
			HireHeroInfoRe re = null;
			Set<Integer> set = null;

			if (req.getFlag() != 0) {
				set = new HashSet<Integer>();
				// 已雇佣的镜像
				Map<Integer, HeroImageInfo> val = roleLoadInfo.getHeroImageMapbyFightType(req.getFlag());
				if (val != null) {
					for (HeroImageInfo imageInfo : val.values()) {
						if (set != null && set.contains(imageInfo.getHeroId())) {
							continue;
						}
						if (set != null){
							set.add(imageInfo.getHeroId());
						}
						re = getHireHeroInfoRe(imageInfo);
						if (re == null) {
							continue;
						}
						list.add(re);
					}
				}
			}
			
			
			// 工会佣兵
			List<HireHeroInfoRe> list1 = new ArrayList<HireHeroInfoRe>();
			RoleClubInfo clubInfo = RoleClubInfoMap.getRoleClubInfoByClubId(roleInfo.getClubId());
			RoleClubMemberInfo memberInfo = RoleClubMemberInfoMap.getRoleClubMemberInfo(roleInfo.getClubId(), roleId);
			if (clubInfo != null && memberInfo != null) {
				Map<Integer, RoleClubMemberInfo> memberMap = RoleClubMemberInfoMap.getRoleClubMemberMap(roleInfo
						.getClubId());
				RoleInfo memberRole = null;
				for (Integer rId : memberMap.keySet()) {
					memberRole = RoleInfoMap.getRoleInfo(rId);
					if (memberRole == null) {
						continue;
					}
					for (HireHeroInfo hireHeroInfo : memberRole.getHireHeroMap().values()) {
						if (set != null && set.contains(hireHeroInfo.getHeroId())) {
							continue;
						}
						if (set != null){
							set.add(hireHeroInfo.getHeroId());
						}
						re = getHireHeroInfoRe(roleInfo, hireHeroInfo);
						if (re == null) {
							continue;
						}
						list1.add(re);
					}
				}
				Collections.sort(list1, new Comparator<HireHeroInfoRe>() {
					// 根据雇佣价格排序
					@Override
					public int compare(HireHeroInfoRe o1, HireHeroInfoRe o2) {
						if (o1.getPrice() < o2.getPrice()) {
							return 1;
						} else if (o1.getPrice() > o2.getPrice()) {
							return -1;
						}
						return 0;
					}
				});
				list.addAll(list1);
			}
			resp.setList(list);
			resp.setCount(list.size());
			resp.setResult(1);
		}

		return resp;
	}

	private HireHeroInfoRe getHireHeroInfoRe(RoleInfo roleInfo, HireHeroInfo hireHeroInfo) {
		RoleInfo role = RoleInfoMap.getRoleInfo(hireHeroInfo.getRoleId());
		if (role == null) {
			return null;
		}
		HeroInfo hero = HeroInfoMap.getHeroInfo(hireHeroInfo.getRoleId(), hireHeroInfo.getHeroId());
		if (hero == null) {
			return null;
		}

		HireHeroInfoRe re = new HireHeroInfoRe();
		re.setRoleId(role.getId());
		re.setRoleName(role.getRoleName());

		re.setLevel((short) hero.getHeroLevel());
		re.setPrice((int) HireHeroService.getHireHeroPrize(hero));
		re.setHeroXmlNo(hero.getHeroNo());
		if (roleInfo.getId() == role.getId()) {
			// 自己则计算时间和派出佣兵的收益
			re.setTime(System.currentTimeMillis() - hireHeroInfo.getTime().getTime());
			re.setIncome(HireHeroService.getHireHeroIncome(role, hero, hireHeroInfo, true));
		}

		re.setHeroStar((byte) hero.getStar());
		re.setQuality((byte) hero.getQuality());
		re.setHeroId(hero.getId());
		re.setFightValue(hero.getFightValue());
		re.setSum(hireHeroInfo.getHireMoneySum());

		List<HeroSkillRe> skillList = HeroService.getHeroSkillList(hero, "");
		re.setSkillCount(skillList.size());
		re.setSkillList(skillList);

		List<EquipDetailRe> equipList = EquipService.getHeroEquipList(hero, null);
		re.setEquipCount(equipList.size());
		re.setEquipList(equipList);
		return re;
	}

	private HireHeroInfoRe getHireHeroInfoRe(HeroImageInfo imageInfo) {
		RoleInfo role = RoleInfoMap.getRoleInfo(imageInfo.getImageRoleId());
		if (role == null) {
			return null;
		}
		HireHeroInfoRe re = new HireHeroInfoRe();
		re.setRoleId(role.getId());
		re.setRoleName(role.getRoleName());

		re.setLevel((short) imageInfo.getLevel());
		re.setHeroXmlNo(imageInfo.getHeroNo());
		re.setHeroId(imageInfo.getHeroId());
		re.setHeroStar((byte) imageInfo.getStar());
		re.setQuality((byte) imageInfo.getQuality());
		re.setFightValue(imageInfo.getFightValue());

		List<HeroSkillRe> skillList = new ArrayList<HeroSkillRe>();
		Map<Integer, Integer> skillMap = imageInfo.getSkillMap();
		if (skillMap != null) {
			for (int skillNo : skillMap.keySet()) {
				skillList.add(HeroService.getHeroSkillRe(skillNo, skillMap.get(skillNo)));
			}
		}
		re.setSkillCount(skillList.size());
		re.setSkillList(skillList);

		List<EquipDetailRe> equipList = new ArrayList<EquipDetailRe>();
		Map<Integer, Integer> equipMap = imageInfo.getEquipMap();
		if (equipMap != null) {
			int index = 0; // 模拟数据库主键
			EquipDetailRe detailRe = null;
			EquipInfoRe eInfo = null;
			for (int equipType : equipMap.keySet()) {
				detailRe = new EquipDetailRe();
				detailRe.setEquipId(index);
				detailRe.setEquipNum((short) 1);

				eInfo = new EquipInfoRe();
				eInfo.setEquipId(index);
				eInfo.setHeroId(imageInfo.getHeroId());
				eInfo.setEquipNo(equipMap.get(equipType));
				eInfo.setEquipType((byte) equipType);
				detailRe.setEquipInfo(eInfo);
				equipList.add(detailRe);
				index++; // 假装自增主键
			}
		}
		re.setEquipCount(equipList.size());
		re.setEquipList(equipList);
		return re;
	}

	/**
	 * 操作佣兵
	 * @param roleId
	 * @param req
	 * @return
	 */
	public HireHeroOperationResp operateHireHero(int roleId, HireHeroOperationReq req) {
		HireHeroOperationResp resp = new HireHeroOperationResp();
		
		if(req.getFlag() == 0){
			//召回 
			callBackHero(roleId, req, resp);
			
		}else if(req.getFlag() == 1){
			//雇佣
//			hireHero(roleId, req, resp);
			
		}else if(req.getFlag() == 2){
			//派出
			sendHero(roleId, req, resp);
			
		}else{
			resp.setResult(ErrorCode.ROLE_CLUB_ERROR_35);
			return resp;
		}
		
		return resp;
	}

	/**
	 * 派出佣兵
	 * @param roleId
	 * @param resp
	 */
	private void sendHero(int roleId, HireHeroOperationReq req, HireHeroOperationResp resp) {
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		
		if(roleInfo == null){
			resp.setResult(ErrorCode.ROLE_CLUB_ERROR_1);
			return;
		}
		
		synchronized(roleInfo){
			RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
			if(roleLoadInfo == null){
				resp.setResult(ErrorCode.ROLE_CLUB_ERROR_2);
				return;
			}
			
			int check = HireHeroService.hireHeroCheck(roleInfo);
			if(check != 1){
				resp.setResult(check);
				return;
			}
			
			String[] heroIds = req.getHeroIds().split(":");
			if(heroIds == null || heroIds.length <= 0){
				resp.setResult(ErrorCode.CLUB_HIRE_HERO_ERROR_16);
				return;
			}
			
			int heroId = 0;
			for(String hId : heroIds){
				try{
					if(hId == null || "".equals(hId))
					{
						continue;
					}
					heroId = Integer.parseInt(hId);
				}catch (Exception e) {
					e.printStackTrace();
				}
				
				HeroInfo heroInfo = roleInfo.getHeroMap().get(heroId);
				if(heroInfo == null){
					resp.setResult(ErrorCode.CLUB_HIRE_HERO_ERROR_2);
					return;
				}
				
				if(heroInfo.getDeployStatus() == HeroInfo.DEPLOY_TYPE_MAIN){
					resp.setResult(ErrorCode.CLUB_HIRE_HERO_ERROR_17);
					return;
				}
				
				if(roleInfo.getHireHeroMap().containsKey(heroId)){
					resp.setResult(ErrorCode.CLUB_HIRE_HERO_ERROR_13);
					return;
				}
				
				if(roleInfo.getHireHeroMap().size() >= GameValue.SEND_HIRE_HERO_BASE_NUM + roleInfo.getHireHeroVipPlus()){
					resp.setResult(ErrorCode.CLUB_HIRE_HERO_ERROR_1);
					return;
				}
				
				HireHeroInfo info = new HireHeroInfo();
				info.setHeroId(heroId);
				info.setRoleId(roleId);
				info.setTime(new Timestamp(System.currentTimeMillis()));
				
				if(!ClubHireHeroDao.getInstance().insertHireHeroInfo(info)){
					resp.setResult(ErrorCode.CLUB_HIRE_HERO_ERROR_3);
					return;
				}
				
				roleInfo.getHireHeroMap().put(heroId, info);
				
				GameLogService.insertPlayActionLog(roleInfo, ActionType.action505.getType(), 0, String.valueOf(heroInfo.getHeroNo()));
			}
			
			resp.setFlag(req.getFlag());
			resp.setResult(1);
		}
		
	}

	/**
	 * 雇佣佣兵
	 * @param roleId
	 * @param resp
	 */
	public void hireHero(int roleId, HireHeroOperationReq req, HireHeroOperationResp resp) {
		synchronized(GameFlag.HIRE_HERO){
			RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
			
			if(roleInfo == null){
				resp.setResult(ErrorCode.ROLE_CLUB_ERROR_1);
				return;
			}
			
			RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
			if(roleLoadInfo == null){
				resp.setResult(ErrorCode.ROLE_CLUB_ERROR_2);
				return;
			}
			
			int check = HireHeroService.hireHeroCheck(roleInfo);
			if(check != 1){
				resp.setResult(check);
				return;
			}
			
			RoleInfo otherRoleInfo = RoleInfoMap.getRoleInfo(req.getRoleId());
			if(otherRoleInfo == null){
				resp.setResult(ErrorCode.CLUB_HIRE_HERO_ERROR_7);
				return;
			}
			
			HeroInfo heroInfo = otherRoleInfo.getHeroMap().get(req.getHeroId());
			if(heroInfo == null){
				resp.setResult(ErrorCode.CLUB_HIRE_HERO_ERROR_8);
				return;
			}
			
			HireHeroInfo hireHeroInfo = otherRoleInfo.getHireHeroMap().get(req.getHeroId());
			if(hireHeroInfo == null){
				resp.setResult(ErrorCode.CLUB_HIRE_HERO_ERROR_9);
				return;
			}
			
			if(HeroInfoMap.getMainHeroLv(roleId) < heroInfo.getHeroLevel() - GameValue.HIRE_HERO_LEVEL_LIMIT){
				resp.setResult(ErrorCode.CLUB_HIRE_HERO_ERROR_10);
				return;
			}
			
			if(roleLoadInfo.getHeroImageMap().size() > GameValue.MAX_HIRE_HERO_NUM){
				//TODO
			}
			
//			double price = GameValue.HIRE_HERO_BASE_VALUE * heroInfo.getHeroLevel() * GameValue.HIRE_HERO_RATE;
			
//			List<AbstractConditionCheck> conds = new ArrayList <AbstractConditionCheck>();
//			
//			conds.add(new MoneyCond((long) price));
//
//			check = AbstractConditionCheck.checkCondition(roleInfo, conds);
//			if (check != 1){
//				resp.setResult(check);
//				return;
//			}
			
			
			synchronized(roleInfo){
				
				//TODO
			}
			
			resp.setResult(1);
			
		}
	}

	/**
	 * 召回佣兵
	 * @param roleId
	 * @param resp
	 */
	private void callBackHero(int roleId, HireHeroOperationReq req, HireHeroOperationResp resp) {
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		
		if(roleInfo == null){
			resp.setResult(ErrorCode.ROLE_CLUB_ERROR_1);
			return;
		}
		
		synchronized(roleInfo){
			RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
			if(roleLoadInfo == null){
				resp.setResult(ErrorCode.ROLE_CLUB_ERROR_2);
				return;
			}
			
			int check = HireHeroService.hireHeroCheck(roleInfo);
			if(check != 1){
				resp.setResult(check);
				return;
			}
			
			int heroId = req.getHeroId();
			HeroInfo heroInfo = roleInfo.getHeroMap().get(heroId);
			if(heroInfo == null){
				resp.setResult(ErrorCode.CLUB_HIRE_HERO_ERROR_4);
				return;
			}
			
			HireHeroInfo hireHeroInfo = roleInfo.getHireHeroMap().get(heroId);
			if(hireHeroInfo == null){
				resp.setResult(ErrorCode.CLUB_HIRE_HERO_ERROR_4);
				return;
			}
			
			int income = HireHeroService.getHireHeroIncome(roleInfo, heroInfo, hireHeroInfo, true);
			if(income <= 0){
				//没有到召回时间
				resp.setResult(ErrorCode.CLUB_HIRE_HERO_ERROR_5);
				return;
			}
			
			income += hireHeroInfo.getHireMoneySum();
			
			
			if(!RoleService.addRoleRoleResource(ActionType.action485.getType(), roleInfo, ConditionType.TYPE_MONEY, (long) income,null)){
				resp.setResult(ErrorCode.CLUB_HIRE_HERO_ERROR_6);
				return;
			}
			
			resp.setSourceChange(ConditionType.TYPE_MONEY.getType() + ":" + (int)income);
			
			if(!ClubHireHeroDao.getInstance().deleteHireHeroInfo(hireHeroInfo)){
				resp.setResult(ErrorCode.CLUB_HIRE_HERO_ERROR_6);
				return;
			}
			
			roleInfo.getHireHeroMap().remove(heroId);
			
			List<BattlePrize> prizeList = new ArrayList<BattlePrize>();
			prizeList.add(new BattlePrize(ConditionType.TYPE_MONEY.getName(), income, (byte) 1, (byte) 0));
			
			resp.setCount(prizeList.size());
			resp.setList(prizeList);
			
			resp.setFlag(req.getFlag());
			resp.setResult(1);
			
			GameLogService.insertPlayActionLog(roleInfo, ActionType.action485.getType(), 0, heroInfo.getHeroNo()+":"+income);
		}
		
	}
}
