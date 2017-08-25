package com.snail.webgame.game.cache;

import java.util.HashMap;
import java.util.Map;

import com.snail.webgame.game.info.ChallengeBattleInfo;
import com.snail.webgame.game.info.RoleInfo;
import com.snail.webgame.game.info.RoleLoadInfo;

public class ChallengeBattleInfoMap {

	/**
	 * 添加记录
	 * @param info
	 */
	public static void addInfo(ChallengeBattleInfo info) {
		int roleId = info.getRoleId();
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			return;
		}
		RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
		if (roleLoadInfo == null) {
			roleLoadInfo = new RoleLoadInfo();
		}
		Map<Byte, Map<Integer, Map<Integer, ChallengeBattleInfo>>> map = roleLoadInfo.getChallengeMap();
		byte chapterType = info.getChallengeType();
		Map<Integer, Map<Integer, ChallengeBattleInfo>> challengeMap = map.get(chapterType);
		if(challengeMap == null){
			challengeMap = new HashMap<Integer, Map<Integer, ChallengeBattleInfo>>();
			map.put(chapterType, challengeMap);
		}
		Map<Integer, ChallengeBattleInfo> battleMap = challengeMap.get(info.getChapterNo());
		int chapterNo = info.getChapterNo();
		if (battleMap == null) {
			battleMap = new HashMap<Integer, ChallengeBattleInfo>();
			challengeMap.put(chapterNo, battleMap);
		}
		if (!battleMap.containsKey(info.getBattleId())) {
			battleMap.put(info.getBattleId(), info);
		}


	}

	/**
	 * 获取玩家所有副本战斗信息
	 * @param roleId
	 * @return
	 */
	public static Map<Integer, Map<Integer, ChallengeBattleInfo>> getByRoleId(int roleId, byte chapterType) {
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			return null;
		}
		RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
		if (roleLoadInfo == null) {
			roleLoadInfo = new RoleLoadInfo();
		}
		Map<Integer, Map<Integer, ChallengeBattleInfo>> map = roleLoadInfo.getChallengeMap(chapterType);
		
		return map;
	}

	/**
	 * 获取玩家某章节战斗信息
	 * @param roleId
	 * @param battleTypeNo
	 * @param chapterNo
	 * @return
	 */
	public static Map<Integer, ChallengeBattleInfo> getByRoleIdAndTypeNoAndChapter(int roleId, byte chapterType, int chapterNo) {

		Map<Integer, Map<Integer, ChallengeBattleInfo>> mapByBattleNo = getByRoleId(roleId, chapterType);
		if (mapByBattleNo == null) {
			return null;
		}
		return mapByBattleNo.get(chapterNo);
	}

	/**
	 * 获取玩家副本、某战役、战场信息
	 * @param roleId
	 * @param battleTypeNo
	 * @param chapterNo
	 * @param battleId
	 * @return
	 */
	public static ChallengeBattleInfo getBattleInfo(int roleId, byte chapterType, int chapterNo, int battleNo) {

		Map<Integer, ChallengeBattleInfo> battles = getByRoleIdAndTypeNoAndChapter(roleId, chapterType, chapterNo);
		if (battles == null) {
			return null;
		}
		ChallengeBattleInfo info = battles.get(battleNo);
		return info;
	}

	/**
	 * 获取玩家副本
	 * @param roleId
	 * @return
	 */
	public static Map<Integer, ChallengeBattleInfo> getListByRoleId(int roleId, byte chapterType) {
		Map<Integer, Map<Integer, ChallengeBattleInfo>> roleMap = getByRoleId(roleId, chapterType);
		Map<Integer, ChallengeBattleInfo> result = null;
		if (roleMap != null) {
			result = new HashMap<Integer, ChallengeBattleInfo>();
			for (Integer chapterNo : roleMap.keySet()) {
				Map<Integer, ChallengeBattleInfo> map = roleMap.get(chapterNo);
				if (map != null) {
					for (ChallengeBattleInfo info : map.values()) {
						result.put(info.getBattleId(), info);
					}
				}
			}
		}
		return result;
	}

	/**
	 * 删除玩家副本信息
	 * @param roleId
	 */
	public static void removeChallenge(int roleId) {
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			return;
		}
		RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
		if (roleLoadInfo != null) {
			roleLoadInfo.getChallengeMap().clear();
		}
	}
}
