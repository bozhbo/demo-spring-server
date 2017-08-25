package com.snail.webgame.game.cache;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.snail.webgame.game.common.GameValue;
import com.snail.webgame.game.info.BossInfo;
import com.snail.webgame.game.info.RoleInfo;

public class WorldBossMap {
	//世界boss <type, BossInfo>
	private static Map<Integer,BossInfo> bossMap = new HashMap<Integer, BossInfo>();
	//世界boss排行
	private static List<RoleInfo> bossList = new ArrayList<RoleInfo>();

	public static void addWordList(BossInfo bossInfo) {
		bossMap.put(bossInfo.getBossType(), bossInfo);
	}
	
	public static BossInfo getWorldBoss(int type) {
		return bossMap.get(type);
	}

	/**
	 * 清空排行榜
	 */
	public static void clearBossList() {
		bossList.clear();
	}
	
	/**
	 * 获得排行榜
	 */
	public static List<RoleInfo> getBossList() {
		return bossList;
	}
	
	/**
	 * 添加今日伤害 获取排行榜 0-异常
	 * @param roleInfo
	 */
	public static void addbossList(RoleInfo roleInfo,Timestamp nowTime,long hp,long bestHp,long thisBest){
		synchronized (GameValue.worldBossSynObj) {
			if (roleInfo.getRoleStatus() == 2) {
				return;
			} else if (roleInfo.getRoleStatus() == 1
					&& roleInfo.getRoleStatusTime().getTime() > System.currentTimeMillis()) {
				return;
			}
			if(nowTime!=null){
				roleInfo.setLastWorldBossFightTime(nowTime);
				roleInfo.setFightWorldBossHp(hp);
				roleInfo.setBestFightBossHp(bestHp);
				roleInfo.setThisBossBest(thisBest);
			}
			if(!bossList.contains(roleInfo) && roleInfo.getFightWorldBossHp() != 0){
				if(bossList.size() < 5000){
					bossList.add(roleInfo);
				}else if(bossList.size() == 5000 && roleInfo.getFightWorldBossHp() > bossList.get(4999).getFightWorldBossHp()){
					bossList.remove(4999);
					bossList.add(roleInfo);
				}
			}
			Collections.sort(bossList, new Comparator<RoleInfo>() {
				@Override
				public int compare(RoleInfo o1, RoleInfo o2) {
					if(o1.getFightWorldBossHp() < o2.getFightWorldBossHp()){
						return 1;
					}else if(o1.getFightWorldBossHp() == o2.getFightWorldBossHp()){
						return 0;
					}else{
						return -1;
					}
				}
			});
		}
	}
}
