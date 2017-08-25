package com.snail.webgame.game.protocal.club.hire.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.snail.webgame.game.cache.HeroInfoMap;
import com.snail.webgame.game.cache.RoleClubInfoMap;
import com.snail.webgame.game.cache.RoleClubMemberInfoMap;
import com.snail.webgame.game.cache.RoleInfoMap;
import com.snail.webgame.game.common.ActionType;
import com.snail.webgame.game.common.ErrorCode;
import com.snail.webgame.game.common.FightType;
import com.snail.webgame.game.common.GameValue;
import com.snail.webgame.game.common.util.Command;
import com.snail.webgame.game.common.util.CommonUtil;
import com.snail.webgame.game.common.util.DateUtil;
import com.snail.webgame.game.common.xml.cache.HeroXMLInfoMap;
import com.snail.webgame.game.common.xml.info.HeroXMLInfo;
import com.snail.webgame.game.condtion.AbstractConditionCheck;
import com.snail.webgame.game.condtion.conds.MoneyCond;
import com.snail.webgame.game.core.SceneService;
import com.snail.webgame.game.dao.ClubHireHeroDao;
import com.snail.webgame.game.info.EquipInfo;
import com.snail.webgame.game.info.HeroImageInfo;
import com.snail.webgame.game.info.HeroInfo;
import com.snail.webgame.game.info.HireHeroInfo;
import com.snail.webgame.game.info.RoleClubInfo;
import com.snail.webgame.game.info.RoleClubMemberInfo;
import com.snail.webgame.game.info.RoleInfo;
import com.snail.webgame.game.info.RoleLoadInfo;
import com.snail.webgame.game.protocal.club.hire.entity.HireHeroInfoRe;
import com.snail.webgame.game.protocal.club.hire.info.GetClubHireHeroInfoResp;
import com.snail.webgame.game.protocal.hero.service.HeroService;
import com.snail.webgame.game.protocal.rolemgt.service.RoleService;
import com.snail.webgame.game.protocal.soldier.service.SoldierService;

public class HireHeroService {
	/**
	 * 检查上次雇佣是否在 同一天
	 * @param roleId
	 * @param info
	 * @return
	 */
	public static int checkCanHire(RoleInfo roleInfo, int heroId){
		if(roleInfo == null || roleInfo.getRoleLoadInfo() == null){
			return 1;
		}
		
		if(roleInfo.getRoleLoadInfo().getHireInfos() == null || "".equals(roleInfo.getRoleLoadInfo().getHireInfos())){
			return 1;
		}
		
		Map<Integer, Long> map = CommonUtil.String2MapByValueLong(roleInfo.getRoleLoadInfo().getHireInfos());
		
		if(!map.containsKey(heroId)){
			return 1;
		}
		
		Long time = map.get(heroId);
		if(time == null){
			return 1;
		}
		
		if(DateUtil.isSameDay(time, System.currentTimeMillis())){
			return ErrorCode.CLUB_HIRE_HERO_ERROR_12;
		}
		
		return 1;
		
	}
	
	/**
	 * 获取佣兵收益
	 * @param roleInfo
	 * @param hireHeroInfo
	 * @param isCallBack 是否自己召回 自己召回 则判断时间是否到期，其他公会行为 无视时间
	 * @return
	 */
	public static int getHireHeroIncome(RoleInfo roleInfo, HeroInfo heroInfo, HireHeroInfo hireHeroInfo, boolean isCallBack){
		if(hireHeroInfo == null){
			return 0;
		}
		
		long pass = System.currentTimeMillis() - hireHeroInfo.getTime().getTime();
		
		if(isCallBack){
			if(pass < GameValue.HIRE_HERO_CALL_BACK_MIN_TIME * 60 * 1000){
				//没有到召回时间
				return 0;
			}
		}
		
		
		int timeUnit = (int) (pass / 1000 / 60 / GameValue.SEND_HIRE_HERO_TIME_UNIT);
		
		double income = GameValue.SEND_HIRE_HERO_BASE_VALUE * heroInfo.getHeroLevel() * GameValue.SEND_HIRE_HERO_RATE * timeUnit;

		return (int) income;
	}
	
	/**
	 * 清理过期的雇佣兵数据
	 * @param roleInfo
	 * @return
	 */
	public static String getCleanupHireInfos(RoleInfo roleInfo){
		if(roleInfo == null || roleInfo.getRoleLoadInfo() == null){
			return "";
		}
		
		
		if(roleInfo.getRoleLoadInfo().getHireInfos() == null || "".equals(roleInfo.getRoleLoadInfo().getHireInfos())){
			return "";
		}
		
		Map<Integer, Long> map = CommonUtil.String2MapByValueLong(roleInfo.getRoleLoadInfo().getHireInfos());
		
		if(map == null || map.size() <= 0){
			return "";
		}
		
		Iterator<Integer> ite = map.keySet().iterator();
		
		while(ite.hasNext()){
			Long time = map.get(ite.next());
			if(time == null){
				ite.remove();
				continue;
			}
			
			if(DateUtil.isSameDay(time, System.currentTimeMillis())){
				ite.remove();
			}
		}
		
		return CommonUtil.Map2String(map);
	}
	
	/**
	 * 获取英雄镜像
	 * @param position 佣兵布阵位置
	 * @param fightType 使用佣兵的战斗类型 0 - 攻城略地
	 * @param roleInfo 角色自己
	 * @param heroInfo 被雇佣的英雄(其他玩家的英雄)
	 * @return
	 */
	public static HeroImageInfo getHeroImageInfo(byte position, FightType fightType, RoleInfo roleInfo, HeroInfo heroInfo){
		int check = copyHeroPropCheck(fightType, roleInfo, heroInfo);
		if(check != 1){
			return null;
		}
		
		RoleInfo otherRoleInfo = RoleInfoMap.getRoleInfo(heroInfo.getRoleId());
		if(otherRoleInfo == null){
			return null;
		}
		
		HeroImageInfo imageInfo = getHeroImageInfo(roleInfo.getId(), heroInfo, position, fightType);
		if(imageInfo == null){
			return null;
		}
		roleInfo.getRoleLoadInfo().addHeroImageInfo(imageInfo);
			
		//完成镜像再扣钱
		HireHeroInfo hireHeroInfo = otherRoleInfo.getHireHeroMap().get(heroInfo.getId());
		if(hireHeroInfo == null){
			return null;
		}
		
		long price = getHireHeroPrize(heroInfo);
		List<AbstractConditionCheck> conds = new ArrayList <AbstractConditionCheck>();
		
		conds.add(new MoneyCond((long) price));

		check = AbstractConditionCheck.checkCondition(roleInfo, conds);
		if (check != 1){
			return null;
		}
		
		
		if (!RoleService.subRoleResource(ActionType.action502.getType(), roleInfo, conds , null)){
			return null;
		}
		
		
		synchronized(hireHeroInfo){
			// 更新佣兵获得总的银子
			int sum = (int) (hireHeroInfo.getHireMoneySum() + price);
			if(ClubHireHeroDao.getInstance().updateHireHeroInfoByRoleId(hireHeroInfo.getRoleId(), hireHeroInfo.getHeroId(), sum)){
				hireHeroInfo.setHireMoneySum(sum);
			}
		}
		
		
		return imageInfo;

	}
	
	/**
	 * 获取英雄镜像
	 * @param roleId
	 * @param hireHero
	 * @param position
	 * @param fightType
	 * @return
	 */
	public static HeroImageInfo getHeroImageInfo(int roleId, HeroInfo hireHero, byte position, FightType fightType) {
		RoleInfo hireRole = RoleInfoMap.getRoleInfo(hireHero.getRoleId());
		if (hireRole == null) {
			return null;
		}
		HeroXMLInfo heroXMLInfo = HeroXMLInfoMap.getHeroXMLInfo(hireHero.getHeroNo());
		if (heroXMLInfo == null) {
			return null;
		}
		HeroImageInfo imageInfo = new HeroImageInfo();
		imageInfo.setRoleId(roleId);
		imageInfo.setHireType(fightType.getValue());

		imageInfo.setImageRoleId(hireHero.getRoleId());
		imageInfo.setHeroId(hireHero.getId());
		imageInfo.setDeployStatus(position);

		imageInfo.setHeroNo(hireHero.getHeroNo());
		imageInfo.setLevel(hireHero.getHeroLevel());
		imageInfo.setIntimacyLevel(hireHero.getIntimacyLevel());
		imageInfo.setSoldierLevel(SoldierService.getSoldierLevel(hireRole, (byte) heroXMLInfo.getHeroType()));
		imageInfo.setQuality(hireHero.getQuality());
		imageInfo.setStar(hireHero.getStar());

		if (hireHero.getEquipMap() != null) {
			for (EquipInfo equipInfo : hireHero.getEquipMap().values()) {
				imageInfo.getEquipMap().put(equipInfo.getEquipType(), equipInfo.getEquipNo());
			}
		}
		imageInfo.setSkillMap(HeroService.getSkillMap(hireHero));

		imageInfo.setFightValue(hireHero.getFightValue());
		imageInfo.setTime(new Timestamp(System.currentTimeMillis()));
		return imageInfo;
	}
	
	/**
	 * 雇佣武将所消耗的银子
	 * @return
	 */
	public static long getHireHeroPrize(HeroInfo hireHero){
		return (long) (GameValue.HIRE_HERO_BASE_VALUE * hireHero.getHeroLevel() * GameValue.HIRE_HERO_RATE);
	}
	
	/**
	 * 检测是否是公会成员
	 * @param roleInfo
	 * @return
	 */
	public static int hireHeroCheck(RoleInfo roleInfo){
		RoleClubInfo roleClubInfo = RoleClubInfoMap.getRoleClubInfoByClubId(roleInfo.getClubId());
		if(roleClubInfo == null){
			return ErrorCode.ROLE_CLUB_ERROR_7;
		}
		
		RoleClubMemberInfo memberInfo = RoleClubMemberInfoMap.getRoleClubMemberInfo(roleInfo.getClubId(), roleInfo.getId());
		if(memberInfo == null || memberInfo.getStatus() == RoleClubMemberInfo.CLUB_REQUEST_MEMBER){
			return ErrorCode.ROLE_CLUB_ERROR_12;
		}
		
		return 1;
	}
	
	/**
	 * 雇佣英雄前检查
	 * @param roleInfo 角色自己
	 * @param heroInfo 被雇佣的英雄
	 * @return
	 */
	public static int copyHeroPropCheck(FightType fightType, RoleInfo roleInfo, HeroInfo heroInfo){
		if(roleInfo == null){
			return ErrorCode.ROLE_CLUB_ERROR_1;
		}		
		RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
		if(roleLoadInfo == null){
			return ErrorCode.ROLE_CLUB_ERROR_2;
		}
		
		int check = HireHeroService.hireHeroCheck(roleInfo);
		if(check != 1){
			return check;
		}
		
		if(heroInfo == null){
			return ErrorCode.CLUB_HIRE_HERO_ERROR_8;
		}
		RoleInfo otherRoleInfo = RoleInfoMap.getRoleInfo(heroInfo.getRoleId());
		if(otherRoleInfo == null){
			return ErrorCode.CLUB_HIRE_HERO_ERROR_7;
		}		
		if(otherRoleInfo.getClubId() != roleInfo.getClubId()){
			return ErrorCode.CLUB_HIRE_HERO_ERROR_20;
		}
		
		HireHeroInfo hireHeroInfo = otherRoleInfo.getHireHeroMap().get(heroInfo.getId());
		if(hireHeroInfo == null){
			return ErrorCode.CLUB_HIRE_HERO_ERROR_9;
		}
		
		if(HeroInfoMap.getMainHeroLv(roleInfo.getId()) < heroInfo.getHeroLevel() - GameValue.HIRE_HERO_LEVEL_LIMIT){
			return ErrorCode.CLUB_HIRE_HERO_ERROR_10;
		}
		
		Map<Integer, HeroImageInfo> imageMap = roleLoadInfo.getHeroImageMapbyFightType(fightType);
		if(imageMap != null){
			if(imageMap.containsKey(heroInfo.getId())){
				// 已雇佣该武将
				return ErrorCode.CLUB_HIRE_HERO_ERROR_18;
			}
			if(imageMap.size() > GameValue.MAX_HIRE_HERO_NUM){
				// 可以从帮派雇佣的上限
				return ErrorCode.CLUB_HIRE_HERO_ERROR_14;
			}
			// 不能雇佣同一个玩家的2个武将
			for(HeroImageInfo imageInfo : imageMap.values()){
				if(imageInfo.getImageRoleId() == heroInfo.getRoleId()){
					// 不能雇佣同一个玩家的2个武将
					return ErrorCode.CLUB_HIRE_HERO_ERROR_19;
				}
			}
		}
		
		List<AbstractConditionCheck> conds = new ArrayList <AbstractConditionCheck>();		
		conds.add(new MoneyCond(getHireHeroPrize(heroInfo)));
		check = AbstractConditionCheck.checkCondition(roleInfo, conds);
		if (check != 1){
			return check;
		}	
		return 1;
	}
	
	/**
	 * 推送佣兵金钱变化消息
	 * @param roleId
	 * @param fightType
	 * @param hireHeroInfo
	 */
	public static void notifyClient4AddHireMoney(FightType fightType, HireHeroInfo hireHeroInfo){
		GetClubHireHeroInfoResp resp = new GetClubHireHeroInfoResp();
		resp.setResult(2);
		resp.setFlag(fightType.getValue());
		
		List<HireHeroInfoRe> list = new ArrayList<HireHeroInfoRe>();
		HireHeroInfoRe re = new HireHeroInfoRe();
		re.setHeroId(hireHeroInfo.getHeroId());
		re.setSum(hireHeroInfo.getHireMoneySum());
		
		list.add(re);
		
		resp.setList(list);
		resp.setCount(list.size());		
		
		
		SceneService.sendRoleRefreshMsg(resp, hireHeroInfo.getRoleId(), Command.GET_CLUB_HIRE_HERO_INFO_RESP);
	}
	
}
