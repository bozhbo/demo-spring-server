package com.snail.webgame.game.protocal.ride.service;

import java.util.ArrayList;
import java.util.List;

import com.snail.webgame.game.common.HeroProType;
import com.snail.webgame.game.common.HeroPropertyInfo;
import com.snail.webgame.game.info.RideInfo;
import com.snail.webgame.game.info.RoleInfo;
import com.snail.webgame.game.protocal.hero.service.HeroProService;
import com.snail.webgame.game.protocal.hero.service.HeroService;
import com.snail.webgame.game.protocal.ride.query.RideDetailRe;
import com.snail.webgame.game.xml.cache.RideXMLInfoMap;
import com.snail.webgame.game.xml.info.RideQuaXMLInfo;
import com.snail.webgame.game.xml.info.RideQuaXMLInfo.QualityInfo;
import com.snail.webgame.game.xml.info.RideXMLInfo;
import com.snail.webgame.game.xml.info.RideXMLInfo.LvUpInfo;

public class RideService {

	/**
	 * 构建坐骑
	 * 
	 * @param roleInfo
	 * @param rideXMLInfo
	 * @return
	 */
	public static RideInfo initNewRideInfo(RoleInfo roleInfo, RideXMLInfo rideXMLInfo) {
		RideInfo rideInfo = new RideInfo();
		rideInfo.setRideNo(rideXMLInfo.getNo());
		rideInfo.setRoleId(roleInfo.getId());
		rideInfo.setQuality(rideXMLInfo.getInitQua());
		rideInfo.setRideLv(1);
		rideInfo.setRideState((byte) 0);
		
		// 战斗力计算
		recalRideFightVal(rideInfo);
		return rideInfo;
	}

	/**
	 * 构建坐骑详细消息对象
	 * 
	 * @param rideInfo
	 * @return
	 */
	public static RideDetailRe getRideDetailRe(RideInfo rideInfo) {
		RideDetailRe re = new RideDetailRe();
		re.setRideId(rideInfo.getId());
		re.setRideNo(rideInfo.getRideNo());
		re.setRideLv(rideInfo.getRideLv());
		re.setRideQua(rideInfo.getQuality());
		re.setRideState(rideInfo.getRideState());
		re.setFightVal(rideInfo.getFightVal());
		return re;
	}
	
	/**
	 * 计算坐骑战斗力
	 * 
	 * @param rideInfo
	 */
	public static void recalRideFightVal(RideInfo rideInfo) {
		RideXMLInfo rideXMLInfo = RideXMLInfoMap.fetchRideXMLInfo(rideInfo.getRideNo());
		RideQuaXMLInfo rideQuaXMLInfo = RideXMLInfoMap.fetchRideQuaXMLInfo(rideInfo.getRideNo());
		if (rideXMLInfo == null || rideQuaXMLInfo == null) {
			return;
		}

		// 计算总属性
		List<HeroPropertyInfo> list = new ArrayList<HeroPropertyInfo>();
		list.add(getRideBaseProperty(rideXMLInfo, rideQuaXMLInfo, rideInfo.getRideLv(), rideInfo.getQuality()));

		// 刷新坐骑战斗力
		int fightValue = HeroService.recalHeroFightValue(HeroProService.generateHeroTotalProtperty(list), null);
		if (fightValue != rideInfo.getFightVal()) {
			rideInfo.setFightVal(fightValue);
		}
		
		// 计算坐骑的速度，血量
		LvUpInfo lvUpInfo = rideXMLInfo.getRideLvUpMap().get(rideInfo.getRideLv());
		QualityInfo qualityInfo = rideQuaXMLInfo.getRideQuaMap().get(rideInfo.getQuality());
		rideInfo.setLvUpInfo(lvUpInfo);
		rideInfo.setRideRate(qualityInfo.getAddAttrRate());
		rideInfo.setCurrHP((int) (lvUpInfo.getRideHp() * (1 + qualityInfo.getAddAttrRate())));
	}
	
	/**
	 * 坐骑 加成属性计算
	 * @param rideXMLInfo
	 * @param rideQuaXMLInfo
	 * @param rideLv
	 * @param rideQua
	 * @return
	 */
	public static HeroPropertyInfo getRideBaseProperty(RideXMLInfo rideXMLInfo, RideQuaXMLInfo rideQuaXMLInfo, int rideLv, int rideQua) {
		HeroPropertyInfo pro = null;
		LvUpInfo lvUpInfo = rideXMLInfo.getRideLvUpMap().get(rideLv);
		if (lvUpInfo == null) {
			return pro;
		}
		
		// 计算等级加成
		pro = (LvUpInfo)lvUpInfo.clone();
		
		QualityInfo qualityInfo = rideQuaXMLInfo.getRideQuaMap().get(rideQua);
		if (qualityInfo == null || qualityInfo.getAddAttrRate() <= 0) {
			return pro;
		}
		
		// 计算品阶加成 speed不加成，其他都加成
		for (HeroProType proType : HeroProType.values()) {
			if (proType == HeroProType.F_moveSpeed) {
				continue;
			}
			pro.addRate(proType, qualityInfo.getAddAttrRate());
		}	
		return pro;
	}
}
