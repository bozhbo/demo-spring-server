package com.snail.webgame.game.cache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.snail.webgame.game.dao.ChallengeBattleDAO;
import com.snail.webgame.game.info.TeamChallengeRecordSub;

/**
 * 组队副本排名记录
 * 
 * @author xiasd
 *
 */
public class TeamChallengeRecord {
	public final static Object lock = new Object();
	
	private static Map<Integer, List<TeamChallengeRecordSub>> firstKill;// 首殺
	private static Map<Integer, List<TeamChallengeRecordSub>> quickKill;// 速殺
	
	public static Map<Integer, List<TeamChallengeRecordSub>> getFirstKill() {
		return firstKill;
	}
	public static Map<Integer, List<TeamChallengeRecordSub>> getQuickKill() {
		return quickKill;
	}
	public static void setFirstKill(
			Map<Integer, List<TeamChallengeRecordSub>> firstKill1) {
		TeamChallengeRecord.firstKill = firstKill1;
	}
	public static void setQuickKill(
			Map<Integer, List<TeamChallengeRecordSub>> quickKill1) {
		TeamChallengeRecord.quickKill = quickKill1;
	}
	
	public static void addDuplicateFirstRecord(int duplicateId, List<TeamChallengeRecordSub> list, long firstTime, long costTime){
		if(list == null || list.size() != 3){
			return;
		}
		
		if(firstKill == null){
			firstKill = new HashMap<Integer, List<TeamChallengeRecordSub>>();
			quickKill = new HashMap<Integer, List<TeamChallengeRecordSub>>();
		}
		
		if(firstKill.get(duplicateId) == null){
			if(ChallengeBattleDAO.getInstance().insertTeamChallengeRecord(duplicateId, list.get(0).getRoleId(), list.get(1).getRoleId(), list.get(2).getRoleId(), 
					 firstTime, costTime, list.get(0).getFightValue(), list.get(1).getFightValue(), list.get(2).getFightValue(),
					 list.get(0).getHeroLevel(), list.get(1).getHeroLevel(), list.get(2).getHeroLevel())){
				firstKill.put(duplicateId, list);
				quickKill.put(duplicateId, list);
			}
		}
	}

	public static void updateDuplicateQuickRecord(int duplicateId, List<TeamChallengeRecordSub> list, long costTime){
		if(list == null || list.size() != 3){
			return;
		}
		
		if(quickKill == null){
			quickKill = new HashMap<Integer, List<TeamChallengeRecordSub>>();
		}
		
		if(ChallengeBattleDAO.getInstance().updateTeamChallengeRecord(duplicateId, list.get(0).getRoleId(), list.get(1).getRoleId(), list.get(2).getRoleId(), 
				costTime, list.get(0).getFightValue(), list.get(1).getFightValue(), list.get(2).getFightValue(),
				list.get(0).getHeroLevel(), list.get(1).getHeroLevel(), list.get(2).getHeroLevel())){
			quickKill.put(duplicateId, list);
		}
	}
}
