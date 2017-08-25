package com.snail.webgame.game.protocal.ride.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.snail.webgame.game.cache.BagItemMap;
import com.snail.webgame.game.cache.HeroInfoMap;
import com.snail.webgame.game.cache.RoleInfoMap;
import com.snail.webgame.game.common.ActionType;
import com.snail.webgame.game.common.ErrorCode;
import com.snail.webgame.game.common.GameValue;
import com.snail.webgame.game.condtion.ConditionType;
import com.snail.webgame.game.core.GameLogService;
import com.snail.webgame.game.dao.RideDAO;
import com.snail.webgame.game.info.HeroInfo;
import com.snail.webgame.game.info.RideInfo;
import com.snail.webgame.game.info.RoleInfo;
import com.snail.webgame.game.info.RoleLoadInfo;
import com.snail.webgame.game.protocal.hero.service.HeroService;
import com.snail.webgame.game.protocal.item.service.ItemService;
import com.snail.webgame.game.protocal.ride.call.RideCallReq;
import com.snail.webgame.game.protocal.ride.call.RideCallResp;
import com.snail.webgame.game.protocal.ride.pass.RidePassReq;
import com.snail.webgame.game.protocal.ride.pass.RidePassResp;
import com.snail.webgame.game.protocal.ride.query.QueryRideInfoResp;
import com.snail.webgame.game.protocal.ride.query.RideDetailRe;
import com.snail.webgame.game.protocal.ride.updown.RideUpDownReq;
import com.snail.webgame.game.protocal.ride.updown.RideUpDownResp;
import com.snail.webgame.game.protocal.ride.uplv.RideUpLvReq;
import com.snail.webgame.game.protocal.ride.uplv.RideUpLvResp;
import com.snail.webgame.game.protocal.ride.upqua.RideUpQuaReq;
import com.snail.webgame.game.protocal.ride.upqua.RideUpQuaResp;
import com.snail.webgame.game.protocal.rolemgt.service.RoleService;
import com.snail.webgame.game.xml.cache.PropXMLInfoMap;
import com.snail.webgame.game.xml.cache.RideXMLInfoMap;
import com.snail.webgame.game.xml.info.PropXMLInfo;
import com.snail.webgame.game.xml.info.RideQuaXMLInfo;
import com.snail.webgame.game.xml.info.RideXMLInfo;
import com.snail.webgame.game.xml.info.RideXMLInfo.UpCostInfo;

/**
 * 坐骑业务逻辑类
 * 
 * @author nijp
 *
 */
public class RideMgrService {

	/**
	 * 查询坐骑信息
	 * 
	 * @param roleId
	 * @return
	 */
	public QueryRideInfoResp queryRideInfo(int roleId) {
		QueryRideInfoResp resp = new QueryRideInfoResp();
		
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			resp.setResult(ErrorCode.ROLE_NOT_EXIST_ERROR_15);
			return resp;
		}
		synchronized (roleInfo) {
			RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
			if (roleLoadInfo == null) {
				resp.setResult(ErrorCode.ROLE_NOT_EXIST_ERROR_9);
				return resp;
			}
			
			List<RideDetailRe> rideList = new ArrayList<RideDetailRe>();
			if (roleLoadInfo.getRoleRideMap().size() > 0) {
				for (RideInfo rideInfo : roleLoadInfo.getRoleRideMap().values()) {
					rideList.add(RideService.getRideDetailRe(rideInfo));
				}
			}
			
			if (rideList.size() > 0) {
				resp.setCount(rideList.size());
				resp.setRideList(rideList);
			}
			
			resp.setOpenRideNoStr(RideXMLInfo.fetchOpenRideNo());
		}
		
		resp.setResult(1);
		return resp;
	}
	
	/**
	 * 坐骑升级
	 * 
	 * @param roleId
	 * @param req
	 * @return
	 */
	public RideUpLvResp rideUpLv(int roleId, RideUpLvReq req) {
		RideUpLvResp resp = new RideUpLvResp();
		
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			resp.setResult(ErrorCode.ROLE_NOT_EXIST_ERROR_15);
			return resp;
		}
		synchronized (roleInfo) {
			RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
			if (roleLoadInfo == null) {
				resp.setResult(ErrorCode.ROLE_NOT_EXIST_ERROR_9);
				return resp;
			}
			
			RideInfo rideInfo = roleLoadInfo.getRoleRideMap().get(req.getRideId());
			if (rideInfo == null) {
				resp.setResult(ErrorCode.ROLE_RIDE_ERROR_11);
				return resp;
			}
			
			RideXMLInfo rideXMLInfo = RideXMLInfoMap.fetchRideXMLInfo(rideInfo.getRideNo());
			// 是否已是该坐骑的最高级
			if (rideXMLInfo == null || rideInfo.getRideLv() >= rideXMLInfo.getRideLvUpMap().size()) {
				resp.setResult(ErrorCode.ROLE_RIDE_ERROR_12);
				return resp;
			}
			
			// 验证消耗
			UpCostInfo upCostInfo = RideXMLInfoMap.fetchLvUpCost(rideInfo.getRideLv() + 1);
			if (upCostInfo == null) {
				resp.setResult(ErrorCode.ROLE_RIDE_ERROR_3);
				return resp;
			}
			
			// 处理消耗
			int rt = dealUpCost(roleInfo, upCostInfo, ActionType.action483.getType());
			if (rt != 1) {
				resp.setResult(rt);
				return resp;
			}
			
			// 更新缓存数据库
			rideInfo.setRideLv(rideInfo.getRideLv() + 1);
			if(!RideDAO.getInstance().updateRideInfo(rideInfo))
			{
				resp.setResult(ErrorCode.APP_COMMENT_ERROR_ERROR_1);
				return resp;
			}
			
			// 更新坐骑战斗力
			RideService.recalRideFightVal(rideInfo);
			
			// 如果上阵,计算角色战斗力
			if (rideInfo.getRideState() == 1) {
				// 更新上阵坐骑
				roleInfo.setRideInfo(rideInfo);
				
				HeroInfo heroInfo = HeroInfoMap.getMainHeroInfo(roleInfo);
				HeroService.refeshHeroProperty(roleInfo, heroInfo);
				
				resp.setHeroId(heroInfo.getId());
				resp.setFightVal(heroInfo.getFightValue());
			}
			
			resp.setResult(1);
			resp.setRideId(rideInfo.getId());
			resp.setRideLv(rideInfo.getRideLv());
			resp.setRideFightVal(rideInfo.getFightVal());
			
			resp.setSourceType((byte) ConditionType.TYPE_MONEY.getType());
			resp.setSoruceNum(-upCostInfo.getMoneyCost());
			
			GameLogService.insertEquipUpLog(roleInfo, ActionType.action483.getType(), 0, rideInfo.getId(),
					rideInfo.getRideNo(), rideInfo.getRideLv()-1, rideInfo.getRideLv());
		}
		
		return resp;
	}
	
	private int dealUpCost(RoleInfo roleInfo, UpCostInfo upCostInfo, int gameAction) {
		// 道具是否足够
		int itemNo = upCostInfo.getItemNo();
		int itemNum = upCostInfo.getItemNum();
		PropXMLInfo propXml = PropXMLInfoMap.getPropXMLInfo(itemNo);
		if (propXml == null) {
			return ErrorCode.ROLE_RIDE_ERROR_2;
		}
		if (!BagItemMap.checkBagItemNum(roleInfo, itemNo, itemNum)) {
			return ErrorCode.ROLE_RIDE_ERROR_2;
		}
		
		// 银币是否足够
		if (roleInfo.getMoney() < upCostInfo.getMoneyCost()) {
			return ErrorCode.ROLE_RIDE_ERROR_1;
		}
		
		// 扣除银币
		if (!RoleService.subRoleRoleResource(gameAction, roleInfo, ConditionType.TYPE_MONEY, upCostInfo.getMoneyCost(), null)) {
			return ErrorCode.ROLE_MONEY_ERROR_1;
		}
		
		Map<Integer, Integer> itemMap = new HashMap<Integer, Integer>();
		itemMap.put(itemNo, itemNum);
		int result = ItemService.bagItemDel(gameAction, roleInfo, itemMap);
		if (result != 1) {
			return result;
		}
		
		return 1;
	}
	
	/**
	 * 坐骑进阶
	 * 
	 * @param roleId
	 * @param req
	 * @return
	 */
	public RideUpQuaResp rideUpQua(int roleId, RideUpQuaReq req) {
		RideUpQuaResp resp = new RideUpQuaResp();
		
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			resp.setResult(ErrorCode.ROLE_NOT_EXIST_ERROR_15);
			return resp;
		}
		synchronized (roleInfo) {
			RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
			if (roleLoadInfo == null) {
				resp.setResult(ErrorCode.ROLE_NOT_EXIST_ERROR_9);
				return resp;
			}
			
			RideInfo rideInfo = roleLoadInfo.getRoleRideMap().get(req.getRideId());
			if (rideInfo == null) {
				resp.setResult(ErrorCode.ROLE_NOT_EXIST_ERROR_9);
				return resp;
			}
			
			RideQuaXMLInfo rideQuaXMLInfo = RideXMLInfoMap.fetchRideQuaXMLInfo(rideInfo.getRideNo());
			// 是否已是该坐骑的最高品阶
			if (rideQuaXMLInfo == null || rideInfo.getQuality() >= rideQuaXMLInfo.getRideQuaMap().size()) {
				resp.setResult(ErrorCode.ROLE_RIDE_ERROR_4);
				return resp;
			}
			
			// 验证消耗
			UpCostInfo upCostInfo = RideXMLInfoMap.fetchQuaUpCost(rideInfo.getQuality() + 1);
			if (upCostInfo == null) {
				resp.setResult(ErrorCode.ROLE_RIDE_ERROR_4);
				return resp;
			}
			
			// 处理消耗
			int rt = dealUpCost(roleInfo, upCostInfo, ActionType.action484.getType());
			if (rt != 1) {
				resp.setResult(rt);
				return resp;
			}
			
			// 更新缓存数据库
			rideInfo.setQuality(rideInfo.getQuality() + 1);
			if(!RideDAO.getInstance().updateRideInfo(rideInfo))
			{
				resp.setResult(ErrorCode.APP_COMMENT_ERROR_ERROR_1);
				return resp;
			}
			
			// 更新坐骑战斗力
			RideService.recalRideFightVal(rideInfo);
			
			// 如果上阵,计算角色战斗力
			if (rideInfo.getRideState() == 1) {
				// 更新上阵坐骑
				roleInfo.setRideInfo(rideInfo);
				
				HeroInfo heroInfo = HeroInfoMap.getMainHeroInfo(roleInfo);
				HeroService.refeshHeroProperty(roleInfo, heroInfo);
				
				resp.setHeroId(heroInfo.getId());
				resp.setFightVal(heroInfo.getFightValue());
			}
						
			resp.setResult(1);
			resp.setRideId(rideInfo.getId());
			resp.setRideQua(rideInfo.getQuality());
			resp.setRideFightVal(rideInfo.getFightVal());
			
			resp.setSourceType((byte) ConditionType.TYPE_MONEY.getType());
			resp.setSoruceNum(-upCostInfo.getMoneyCost());
			
			GameLogService.insertEquipUpLog(roleInfo, ActionType.action484.getType(), 0, rideInfo.getId(),
					rideInfo.getRideNo(), rideInfo.getQuality()-1, rideInfo.getQuality());
		}
		
		return resp;
	}

	/**
	 * 坐骑传承
	 * 
	 * @param roleId
	 * @param req
	 * @return
	 */
	public RidePassResp ridePass(int roleId, RidePassReq req) {
		RidePassResp resp = new RidePassResp();
		
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			resp.setResult(ErrorCode.ROLE_NOT_EXIST_ERROR_15);
			return resp;
		}
		synchronized (roleInfo) {
			RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
			if (roleLoadInfo == null) {
				resp.setResult(ErrorCode.ROLE_NOT_EXIST_ERROR_9);
				return resp;
			}
			
			RideInfo lRideInfo = roleLoadInfo.getRoleRideMap().get(req.getLeftRideId());
			RideInfo rRideInfo = roleLoadInfo.getRoleRideMap().get(req.getRightRideId());
			if (lRideInfo == null || rRideInfo == null) {
				resp.setResult(ErrorCode.ROLE_RIDE_ERROR_4);
				return resp;
			}
			
			RideXMLInfo lRideXMLInfo = RideXMLInfoMap.fetchRideXMLInfo(lRideInfo.getRideNo());
			RideXMLInfo rRideXMLInfo = RideXMLInfoMap.fetchRideXMLInfo(rRideInfo.getRideNo());
			RideQuaXMLInfo rRideQuaXMLInfo = RideXMLInfoMap.fetchRideQuaXMLInfo(rRideInfo.getRideNo());
			if (lRideXMLInfo == null || rRideXMLInfo == null || rRideQuaXMLInfo == null) {
				resp.setResult(ErrorCode.ROLE_RIDE_ERROR_4);
				return resp;
			}
			
			// 接收者最高等级
			int rMaxLv = rRideXMLInfo.getRideLvUpMap().size();
			int rMaxQua = rRideQuaXMLInfo.getRideQuaMap().size();
			
			boolean isCanPass = false;
			
			// 传承者等级必须 >接受者等级 and 接受者还未满级
			if (lRideInfo.getRideLv() > rRideInfo.getRideLv() && rRideInfo.getRideLv() < rMaxLv) {
				isCanPass = true;
			}
			
			if (!isCanPass) {
				// 传承者品质必须 >接受者品质 and 接受者还未满品质
				if (lRideInfo.getQuality() > rRideInfo.getQuality() && rRideInfo.getQuality() < rMaxQua) {
					isCanPass = true;
				}
			}
			
			if (!isCanPass) {
				resp.setResult(ErrorCode.ROLE_RIDE_PASS_LV_LESS_ERROR);
				return resp;
			}
			
			// 金币消耗检测
			if (roleInfo.getCoin() < GameValue.RIDE_PASS_COIN_COST) {
				resp.setResult(ErrorCode.ROLE_COIN_ERROR_1);
				return resp;
			}
			
			// 扣除金币
			if (!RoleService.subRoleRoleResource(ActionType.action491.getType(), roleInfo, ConditionType.TYPE_COIN, GameValue.RIDE_PASS_COIN_COST , null)) {
				resp.setResult(ErrorCode.ROLE_COIN_ERROR_1);
				return resp;
			}
			
			// 开始传承
			// 传承者等级降为1
			if (lRideInfo.getRideLv() > rRideInfo.getRideLv()) {
				rRideInfo.setRideLv(lRideInfo.getRideLv() > rMaxLv ? rMaxLv : lRideInfo.getRideLv());
			}
			
			// 品质传承
			if (lRideInfo.getQuality() > rRideInfo.getQuality()) {
				rRideInfo.setQuality(lRideInfo.getQuality() > rMaxQua ? rMaxQua : lRideInfo.getQuality());
			}
			
			lRideInfo.setRideLv(1);
			lRideInfo.setQuality(lRideXMLInfo.getInitQua());
			
			if(!RideDAO.getInstance().updateRideInfo(lRideInfo))
			{
				resp.setResult(ErrorCode.APP_COMMENT_ERROR_ERROR_1);
				return resp;
			}
			if(!RideDAO.getInstance().updateRideInfo(rRideInfo))
			{
				resp.setResult(ErrorCode.APP_COMMENT_ERROR_ERROR_1);
				return resp;
			}
			
			// 重新计算两坐骑战斗力
			RideService.recalRideFightVal(lRideInfo);
			RideService.recalRideFightVal(rRideInfo);
			
			// 如果两坐骑有上阵坐骑,计算角色战斗力
			RideInfo onRideInfo = null;
			if (lRideInfo.getRideState() == 1) {
				onRideInfo = lRideInfo;
			} else if (rRideInfo.getRideState() == 1) {
				onRideInfo = rRideInfo;
			}
			
			if (onRideInfo != null) {
				// 更新上阵坐骑
				roleInfo.setRideInfo(onRideInfo);
				
				HeroInfo heroInfo = HeroInfoMap.getMainHeroInfo(roleInfo);
				HeroService.refeshHeroProperty(roleInfo, heroInfo);
				
				resp.setHeroId(heroInfo.getId());
				resp.setFightVal(heroInfo.getFightValue());
			}
			
			resp.setResult(1);
			resp.setLeftRideId(req.getLeftRideId());
			resp.setLeftRideLv(lRideInfo.getRideLv());
			resp.setLeftRideQua(lRideInfo.getQuality());
			resp.setLeftFightVal(lRideInfo.getFightVal());
			
			resp.setRightRideId(req.getRightRideId());
			resp.setRightRideLv(rRideInfo.getRideLv());
			resp.setRightRideQua(rRideInfo.getQuality());
			resp.setRightFightVal(rRideInfo.getFightVal());
			
			resp.setSourceType((byte) ConditionType.TYPE_COIN.getType());
			resp.setSoruceNum(-GameValue.RIDE_PASS_COIN_COST);
			
			GameLogService.insertEquipUpLog(roleInfo, ActionType.action491.getType(), 0, 0,
					0, lRideInfo.getRideNo(), rRideInfo.getRideNo());
		}
		
		return resp;
	}

	/**
	 * 坐骑召唤
	 * 
	 * @param roleId
	 * @param req
	 * @return
	 */
	public RideCallResp rideCall(int roleId, RideCallReq req) {
		RideCallResp resp = new RideCallResp();
		
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			resp.setResult(ErrorCode.ROLE_NOT_EXIST_ERROR_15);
			return resp;
		}
		synchronized (roleInfo) {
			RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
			if (roleLoadInfo == null) {
				resp.setResult(ErrorCode.ROLE_NOT_EXIST_ERROR_9);
				return resp;
			}
			
			int rideNo = req.getRideNo();
			RideXMLInfo rideXMLInfo = RideXMLInfoMap.fetchRideXMLInfo(rideNo);
			if (rideXMLInfo == null) {
				resp.setResult(ErrorCode.ROLE_RIDE_ERROR_4);
				return resp;
			}
			
			// 是否已召唤过
			if (roleLoadInfo.checkIsHasSameRideByNo(rideNo)) {
				resp.setResult(ErrorCode.ROLE_RIDE_ERROR_6);
				return resp;
			}
			
			// 坐骑开关是否打开
			if (!RideXMLInfo.checkRideIsOpen(rideNo)) {
				resp.setResult(ErrorCode.ROLE_RIDE_ERROR_6);
				return resp;
			}
			
			// 碎片是否足够
			int chipNo = rideXMLInfo.getRideChipNo();
			int chipNum = rideXMLInfo.getCallChipCost();
			PropXMLInfo propXml = PropXMLInfoMap.getPropXMLInfo(chipNo);
			if (propXml == null) {
				resp.setResult(ErrorCode.ROLE_RIDE_ERROR_4);
				return resp;
			}
			if (!BagItemMap.checkBagItemNum(roleInfo, chipNo, chipNum)) {
				resp.setResult(ErrorCode.ROLE_RIDE_ERROR_7);
				return resp;
			}

			Map<Integer, Integer> itemMap = new HashMap<Integer, Integer>();
			itemMap.put(chipNo, chipNum);
			int result = ItemService.bagItemDel(ActionType.action482.getType(), roleInfo, itemMap);
			if (result != 1) {
				resp.setResult(result);
				return resp;
			}
			
			// 构建坐骑
			RideInfo rideInfo = RideService.initNewRideInfo(roleInfo, rideXMLInfo);
			
			// 更新缓存数据库
			if (!RideDAO.getInstance().insertRideInfo(rideInfo)) {
				resp.setResult(ErrorCode.ROLE_RIDE_ERROR_8);
				return resp;
			}
			
			roleLoadInfo.getRoleRideMap().put(rideInfo.getId(), rideInfo);

			resp.setResult(1);
			resp.setRe(RideService.getRideDetailRe(rideInfo));
			
			GameLogService.insertEquipUpLog(roleInfo, ActionType.action482.getType(), 0, rideInfo.getId(),
					rideInfo.getRideNo(), 0, 1);
		}
		
		return resp;
	}

	/**
	 * 坐骑上下阵
	 * 
	 * @param roleId
	 * @param req
	 * @return
	 */
	public RideUpDownResp rideUpDown(int roleId, RideUpDownReq req) {
		RideUpDownResp resp = new RideUpDownResp();
		if (req.getState() != 0 && req.getState() != 1) {
			resp.setResult(ErrorCode.ROLE_RIDE_ERROR_9);
			return resp;
		}
		
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			resp.setResult(ErrorCode.ROLE_RIDE_ERROR_9);
			return resp;
		}
		synchronized (roleInfo) {
			RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
			if (roleLoadInfo == null) {
				resp.setResult(ErrorCode.ROLE_RIDE_ERROR_9);
				return resp;
			}
			
			// 是否有这个坐骑
			RideInfo rideInfo = roleLoadInfo.getRoleRideMap().get(req.getRideId());
			if (rideInfo == null) {
				resp.setResult(ErrorCode.ROLE_RIDE_ERROR_10);
				return resp;
			}
			
			// 已是该状态，不需要改变
			if (rideInfo.getRideState() == req.getState()) {
				resp.setResult(1);
				resp.setRideId(req.getRideId());
				resp.setState(req.getState());
				return resp;
			}
			
			// 坐骑上下阵切换
			// 下阵
			if (req.getState() == 0) {
				rideInfo.setRideState((byte) 0);
				roleInfo.setRideInfo(null);
			} else {
				// 当前已上阵坐骑
				RideInfo oldRideInfo = roleInfo.getRideInfo();
				if (oldRideInfo != null && oldRideInfo.getRoleId() != rideInfo.getId()) {
					// 旧的已上阵坐骑需要下阵
					oldRideInfo.setRideState((byte) 0);
					
					// 同步修正之前上阵坐骑缓存状态
					roleLoadInfo.getRoleRideMap().get(oldRideInfo.getId()).setRideState((byte) 0) ;
					
					// 更新数据库
					if(!RideDAO.getInstance().updateRideInfo(oldRideInfo))
					{
						resp.setResult(ErrorCode.APP_COMMENT_ERROR_ERROR_1);
						return resp;
					}
					
					resp.setOldRideId(oldRideInfo.getId());
					resp.setOldRideState((byte) 0);
				}
				
				rideInfo.setRideState((byte) 1);
				roleInfo.setRideInfo(rideInfo);
			}
			
			// 更新数据库
			if(!RideDAO.getInstance().updateRideInfo(rideInfo))
			{
				resp.setResult(ErrorCode.APP_COMMENT_ERROR_ERROR_1);
				return resp;
			}
			
			// 计算角色战斗力
			HeroInfo heroInfo = HeroInfoMap.getMainHeroInfo(roleInfo);
			HeroService.refeshHeroProperty(roleInfo, heroInfo);
			
			resp.setHeroId(heroInfo.getId());
			resp.setFightVal(heroInfo.getFightValue());
		}
		
		resp.setResult(1);
		resp.setRideId(req.getRideId());
		resp.setState(req.getState());
		return resp;
	}

}
