package com.snail.webgame.game.protocal.rank.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.game.cache.HeroInfoMap;
import com.snail.webgame.game.cache.RoleInfoMap;
import com.snail.webgame.game.cache.WorldBossMap;
import com.snail.webgame.game.info.HeroInfo;
import com.snail.webgame.game.info.RoleInfo;
import com.snail.webgame.game.protocal.club.entity.ClubFightInfo;
import com.snail.webgame.game.protocal.club.service.ClubService;
import com.snail.webgame.game.protocal.scene.sys.SceneService1;

public class RankService {
	
	private static final Logger logger = LoggerFactory.getLogger("logs");

	private static Timestamp lastRankLevelTime = null;
	private static List<RankInfo> roleLevelRank = new ArrayList<RankInfo>(); // 玩家等级排行
	
	private static Timestamp lastRankHeroNumTime = null;
	private static List<RankInfo> heroNumRank = new ArrayList<RankInfo>(); // 玩家拥有英雄排行
	
	private static Timestamp lastRankFightValueTime = null;
	private static List<RankInfo> fightValueRank = new ArrayList<RankInfo>(); // 战斗力排行
	
	private static Timestamp lastRankClubFightTime = null;
	private static List<ClubFightInfo> clubFightRank = new ArrayList<ClubFightInfo>(); //公会排行

	public static void rank() {
		// 玩家等级排行榜
		rankLevel();
		logger.info("GameRank roleLevel successed");

		// 英雄数量排行榜
		rankHeroNum();
		logger.info("GameRank heroNum successed");
		
		// 战斗力排行榜
		rankFightValue();
		logger.info("GameRank fightValue successed");
		
		// 战斗力大R改变
		notifySuperRChange();
		logger.info("Notify SuperR successed");
		
		rankClubFight();
		logger.info("GameRank club successed");
		
	}

	/**
	 * 玩家等级排行榜
	 */
	private static void rankLevel() {
		// 排行时间
		lastRankLevelTime = new Timestamp(System.currentTimeMillis());
		
		Map<Integer, RoleInfo> roleMap = RoleInfoMap.getMap();
		// 玩家等级排行榜
		List<RankInfo> rankList1 = new ArrayList<RankInfo>();
		for (RoleInfo roleInfo : roleMap.values()) {
			if (roleInfo == null) {
				continue;
			}
			
			if (!roleInfo.isShowRank(true)) {
				continue;
			}
			
			RankInfo rankInfo;
			if (roleInfo.getLevelRankInfo() == null) {
				rankInfo = new RankInfo();
			} else {
				rankInfo = roleInfo.getLevelRankInfo();
			}
			HeroInfo heroInfo = HeroInfoMap.getMainHeroInfo(roleInfo);
			if (heroInfo == null) {
				continue;
			}
			// 赋值
			rankInfo.setRoleId(roleInfo.getId());
			rankInfo.setName(roleInfo.getRoleName());
			rankInfo.setLevel(heroInfo.getHeroLevel());
			rankInfo.setParam(heroInfo.getHeroLevel());
			rankInfo.setHeroNo(heroInfo.getHeroNo());
			rankInfo.setFightValue(roleInfo.getFightValue());
			// 加入roleInfo
			roleInfo.setLevelRankInfo(rankInfo);
			// 加入list
			rankList1.add(rankInfo);
		}
		RankComparator.rankLevel(rankList1);
		synchronized (roleLevelRank) {
			roleLevelRank.clear();
			roleLevelRank.addAll(rankList1);
		}
		for (int i = 0; i < rankList1.size(); i++) {
			RankInfo rankInfo = rankList1.get(i);
			RoleInfo roleInfo = RoleInfoMap.getRoleInfo(rankInfo.getRoleId());
			roleInfo.setRankLevel(i + 1);
			rankInfo.setRankNum(i + 1);
		}
	}

	/**
	 * 英雄数量排行榜
	 */
	private static void rankHeroNum() {
		// 排行时间
		lastRankHeroNumTime = new Timestamp(System.currentTimeMillis());
		
		Map<Integer, RoleInfo> roleMap = RoleInfoMap.getMap();
		// 玩家英雄数量排行榜
		List<RankInfo> rankList2 = new ArrayList<RankInfo>();

		for (RoleInfo roleInfo : roleMap.values()) {
			if (roleInfo == null) {
				continue;
			}
			if (!roleInfo.isShowRank(true)) {
				continue;
			}
			
			RankInfo rankInfo;
			if (roleInfo.getHeroNumRankInfo() == null) {
				rankInfo = new RankInfo();
			} else {
				rankInfo = roleInfo.getHeroNumRankInfo();
			}
			HeroInfo heroInfo = HeroInfoMap.getMainHeroInfo(roleInfo);
			if (heroInfo == null) {
				continue;
			}
			int heroNum = roleInfo.getCommHeroNum();
			// 赋值
			rankInfo.setRoleId(roleInfo.getId());
			rankInfo.setLevel(heroInfo.getHeroLevel());
			rankInfo.setName(roleInfo.getRoleName());
			rankInfo.setParam(heroNum);
			rankInfo.setHeroNo(heroInfo.getHeroNo());
			rankInfo.setFightValue(roleInfo.getFightValue());
			// 加入roleInfo
			roleInfo.setHeroNumRankInfo(rankInfo);
			// 加入list
			rankList2.add(rankInfo);
		}

		RankComparator.rankHeroNum(rankList2);
		synchronized (heroNumRank) {
			heroNumRank.clear();
			heroNumRank.addAll(rankList2);
		}
		Iterator<RankInfo> iter = rankList2.iterator();
		int i = 0;
		while (iter.hasNext()) {
			i++;
			RankInfo rankInfo = iter.next();
			int roleId = rankInfo.getRoleId();
			RoleInfo roleInfo = roleMap.get(roleId);
			roleInfo.setRankHeroNum(i);
			rankInfo.setRankNum(i);
		}

	}

	/**
	 * 战斗力排行
	 */
	private static void rankFightValue() {
		// 排行时间
		lastRankFightValueTime = new Timestamp(System.currentTimeMillis());
		
		Map<Integer, RoleInfo> roleMap = RoleInfoMap.getMap();
		// 玩家英雄数量排行榜
		List<RankInfo> rankList2 = new ArrayList<RankInfo>();

		for (RoleInfo roleInfo : roleMap.values()) {
			if (roleInfo == null) {
				continue;
			}
			if (!roleInfo.isShowRank(true)) {
				continue;
			}
			RankInfo rankInfo;
			if (roleInfo.getFightValueRankInfo() == null) {
				rankInfo = new RankInfo();
			} else {
				rankInfo = roleInfo.getFightValueRankInfo();
			}
			HeroInfo heroInfo = HeroInfoMap.getMainHeroInfo(roleInfo);
			if (heroInfo == null) {
				continue;
			}
			// 赋值
			rankInfo.setRoleId(roleInfo.getId());
			rankInfo.setLevel(heroInfo.getHeroLevel());
			rankInfo.setName(roleInfo.getRoleName());
			rankInfo.setParam(roleInfo.getFightValue());
			rankInfo.setHeroNo(heroInfo.getHeroNo());
			rankInfo.setFightValue(roleInfo.getFightValue());
			// 加入roleInfo
			roleInfo.setFightValueRankInfo(rankInfo);
			// 加入list
			rankList2.add(rankInfo);
		}

		RankComparator.rankFightValue(rankList2);
		synchronized (fightValueRank) {
			fightValueRank.clear();
			fightValueRank.addAll(rankList2);
		}
		Iterator<RankInfo> iter = rankList2.iterator();
		int i = 0;
		while (iter.hasNext()) {
			i++;
			RankInfo rankInfo = iter.next();
			int roleId = rankInfo.getRoleId();
			RoleInfo roleInfo = roleMap.get(roleId);
			roleInfo.setRankFightValue(i);
			rankInfo.setRankNum(i);
		}
	}
	
	/**
	 * 战斗力排行后通知所有玩家改变大R显示
	 *
	 */
	public static void notifySuperRChange() {
		Map<Integer, RoleInfo> roleMap = RoleInfoMap.getMap();
		for (RoleInfo roleInfo : roleMap.values()) {
			if (roleInfo == null) {
				continue;
			}
			//通知刷新大R雕像
			SceneService1.superRUpdate(roleInfo);
		}
	}
	
	private static void rankClubFight(){
		// 排行时间
		lastRankClubFightTime = new Timestamp(System.currentTimeMillis());
		clubFightRank = ClubService.calcClubFight();
		/*int rankNum = 1;
		for(ClubFightInfo info : list){
			
			RankClubInfo rankInfo = new RankClubInfo();
			rankInfo.setClubId(info.getClubId());
			rankInfo.setClubName(info.getClubName());
			rankInfo.setImageId(info.getImageId());
			rankInfo.setLevel(info.getLevel());
			rankInfo.setParam(info.getTotalFight());
			rankInfo.setRankNum(rankNum);
			rankNum++;
			clubFightRank.add(rankInfo);
		}*/
	}
	
	/**
	 * 查询等级排名
	 * @param minPlace从1开始
	 * @param maxPlace
	 * @return
	 */
	public static List<RankInfo> getRoleLevelRank(int minPlace, int maxPlace) {
		List<RankInfo> list = new ArrayList<RankInfo>();
		if (roleLevelRank != null && roleLevelRank.size() > 0) {
			synchronized (roleLevelRank) {
				for (int i = minPlace - 1; i < (maxPlace <= roleLevelRank.size() ? maxPlace : roleLevelRank.size()); i++) {
					RankInfo rankInfo = roleLevelRank.get(i);
					if (rankInfo == null) {
						break;
					} else {
						rankInfo.setRankNum(i + 1);
						list.add(rankInfo);
					}
				}
			}
		}
		return list;
	}

	/**
	 * 查询英雄数量排名
	 * @param minPlace从1开始
	 * @param maxPlace
	 * @return
	 */
	public static List<RankInfo> getHeroNumRank(int minPlace, int maxPlace) {
		List<RankInfo> list = new ArrayList<RankInfo>();
		if (heroNumRank != null && heroNumRank.size() > 0) {
			synchronized (heroNumRank) {
				for (int i = minPlace - 1; i < (maxPlace <= heroNumRank.size() ? maxPlace : heroNumRank.size()); i++) {
					RankInfo rankInfo = heroNumRank.get(i);
					if (rankInfo == null) {
						break;
					} else {
						rankInfo.setRankNum(i + 1);
						list.add(rankInfo);
					}
				}
			}
		}
		return list;
	}

	/**
	 * 查询战斗力排行
	 * @param minPlace从1开始
	 * @param maxPlace
	 * @return
	 */
	public static List<RankInfo> getFightValueRank(int minPlace, int maxPlace) {
		List<RankInfo> list = new ArrayList<RankInfo>();
		if (fightValueRank != null && fightValueRank.size() > 0) {
			synchronized (fightValueRank) {
				for (int i = minPlace - 1; i < (maxPlace <= fightValueRank.size() ? maxPlace : fightValueRank.size()); i++) {
					RankInfo rankInfo = fightValueRank.get(i);
					if (rankInfo == null) {
						break;
					} else {
						rankInfo.setRankNum(i + 1);
						list.add(rankInfo);
					}
				}
			}
		}
		return list;
	}

	/**
	 * 查询今日世界BOSS伤害
	 * @param minPlace从1开始
	 * @param maxPlace
	 * @return
	 */
	public static List<RankInfo> getAttWorldBossRank(int minPlace, int maxPlace) {
		List<RoleInfo> roleLoadList = WorldBossMap.getBossList();
		List<RankInfo> list = new ArrayList<RankInfo>();
		if (roleLoadList != null && roleLoadList.size() > 0) {
			for (int i = minPlace - 1; i < (maxPlace <= roleLoadList.size() ? maxPlace : roleLoadList.size()); i++) {
				RoleInfo roleLoadInfo = roleLoadList.get(i);
				if (roleLoadInfo == null) {
					break;
				} else {
					RankInfo rankInfo = new RankInfo();
					RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleLoadInfo.getId());
					rankInfo.setRoleId(roleInfo.getId());
					HeroInfo heroInfo = HeroInfoMap.getMainHeroInfo(roleInfo);
					if (heroInfo != null) {
						rankInfo.setHeroNo(heroInfo.getHeroNo());
						rankInfo.setLevel(heroInfo.getHeroLevel());
					}
					rankInfo.setName(roleInfo.getRoleName());

					rankInfo.setRankNum(WorldBossMap.getBossList().contains(roleInfo) ? WorldBossMap.getBossList().indexOf(roleInfo)+1 : 0);
					rankInfo.setParam(roleLoadInfo.getFightWorldBossHp());
					rankInfo.setPerMax(roleLoadInfo.getThisBossBest());
					if (rankInfo.getParam() > 0) {
						list.add(rankInfo);
					}
				}
			}
		}
		return list;
	}
	
	/**
	 * 查询公会排行
	 * @param minPlace从1开始
	 * @param maxPlace
	 * @return
	 */
	public static List<ClubFightInfo> getClubFightRank(int minPlace, int maxPlace) {
		List<ClubFightInfo> list = new ArrayList<ClubFightInfo>();
		if (clubFightRank != null && clubFightRank.size() > 0) {
			synchronized (clubFightRank) {
				for (int i = minPlace - 1; i < (maxPlace <= clubFightRank.size() ? maxPlace : clubFightRank.size()); i++) {
					ClubFightInfo rankInfo = clubFightRank.get(i);
					if (rankInfo == null) {
						break;
					} else {
						rankInfo.setRankNum(i + 1);
						list.add(rankInfo);
					}
				}
			}
		}
		return list;
	}

	public static Timestamp getLastRankLevelTime() {
		return lastRankLevelTime;
	}

	public static Timestamp getLastRankHeroNumTime() {
		return lastRankHeroNumTime;
	}

	public static Timestamp getLastRankFightValueTime() {
		return lastRankFightValueTime;
	}
	
	public static Timestamp getLastRankClubFightTime() {
		return lastRankClubFightTime;
	}

	public static List<RankInfo> getFightValueRank() {
		return fightValueRank;
	}
		
	/**
	 * 获取位置（最小增量排序 相同战斗力多的有误差）
	 * @param fightValue
	 * @return
	 */
	public static int getRoleIndexbyShellSort(double fightValue) {
		return getRoleIndexbyShellSort(fightValueRank, fightValue);
	}
	
	/**
	 * 获取位置（最小增量排序 相同战斗力多的有误差）
	 * @param fightValue
	 * @return
	 */
	public static int getRoleIndexbyShellSort(List<RankInfo> fightValueRank, double fightValue) {
		int size = fightValueRank.size();
		int low = 0;
		int high = size - 1;
		int mid = 0;
		int roleFightValue = 0;
		int nextRoleFightValue = 0;
		while (low <= high) {
			if (high - low <= 20) {
				break;
			}
			mid = (low + high) / 2;
			roleFightValue = fightValueRank.get(mid).getFightValue();
			if (mid == size - 1 && fightValue == roleFightValue) {
				return mid;
			}
			if (mid + 1 < size) {
				nextRoleFightValue = fightValueRank.get(mid + 1).getFightValue();
				if (fightValue <= roleFightValue && fightValue >= nextRoleFightValue) {
					return mid;
				} else if (fightValue < nextRoleFightValue) {
					low = mid + 1;
				} else if (fightValue > roleFightValue) {
					high = mid - 1;
				}
			}
		}
		for (int i = low; i < high + 1; i++) {
			roleFightValue = fightValueRank.get(i).getFightValue();
			if (i == size - 1 && fightValue == roleFightValue) {
				return i;
			}
			if (i + 1 < size) {
				nextRoleFightValue = fightValueRank.get(i + 1).getFightValue();
				if (fightValue <= roleFightValue && fightValue >= nextRoleFightValue) {
					return i;
				}
			}
		}

		return -1;
	}
}
