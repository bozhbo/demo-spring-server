package com.snail.webgame.game.cache;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.snail.webgame.game.common.FightInfo;
import com.snail.webgame.game.info.RoleInfo;
import com.snail.webgame.game.protocal.fight.fightend.FightEndResp;
import com.snail.webgame.game.protocal.fight.service.FightIdGen;
import com.snail.webgame.game.protocal.fight.service.FightService;

public class FightInfoMap {

	// <roleId,FightInfo>
	private static ConcurrentHashMap<Integer, FightInfo> fightMap = new ConcurrentHashMap<Integer, FightInfo>();
	
	// <roleId,FightEndResp>
	private static ConcurrentHashMap<Integer, ArrayList<FightEndResp>> fightRespMap = new ConcurrentHashMap<Integer, ArrayList<FightEndResp>>();

	/**
	 * 记录战斗信息
	 * @param info
	 */
	public static void addFightInfo(RoleInfo roleInfo, FightInfo info) {
		if (info == null) {
			return;
		}
		// 处理老战斗结果
		FightService.dealFightOut(roleInfo);

		if (info.getFightId() == 0) {
			info.setFightId(FightIdGen.getSequenceId());
		}
		info.setFightServerId(1);
		info.setFightTime(System.currentTimeMillis());// 缓存添加时间
		fightMap.put(info.getRoleId(), info);
	}

	/**
	 * 通过角色Id获取战斗信息
	 * @param roleId
	 * @return
	 */
	public static FightInfo getFightInfoByRoleId(int roleId) {
		return fightMap.get(roleId);
	}

	/**
	 * 移除战斗信息
	 * @param roleId
	 * @param fightId
	 */
	public static void removeFightInfoByRoleId(int roleId) {
		fightMap.remove(roleId);
	}

	/**
	 * 获取战斗列表
	 * @return
	 */
	public static Map<Integer, FightInfo> getFightMap() {
		return fightMap;
	}
	
	/**
	 * 添加战斗结算缓存
	 * @param info
	 */
	public static void addFightResult(RoleInfo roleInfo, FightEndResp resp) {
		if (resp == null) {
			return;
		}
		if (resp.getFightId() == 0) {
			return;
		}
		ArrayList<FightEndResp> resultList = fightRespMap.get(roleInfo.getId());
		if(resultList == null){
			resultList = new ArrayList<FightEndResp>();
		}
		resultList.add(resp);
		fightRespMap.put(roleInfo.getId(), resultList);
	}
	
	/**
	 * 通过战斗Id获取战斗结算信息
	 * @param fightId
	 * @return
	 */
	public static FightEndResp getFightResultByFightId(int roleId, long fightId) {
		ArrayList<FightEndResp> resultList = fightRespMap.get(roleId);
		if(resultList == null || resultList.size() == 0){
			return null;
		}
		for (FightEndResp resp : resultList) {
			if(resp != null && resp.getFightId() == fightId){
				return resp;
			}
		}
		return null;
	}
	
	/**
	 * 移除战斗结算信息
	 * @param roleId
	 * @param fightId
	 */
	public static void removeFightResultByRoleId(int roleId) {
		fightRespMap.remove(roleId);
	}
}
