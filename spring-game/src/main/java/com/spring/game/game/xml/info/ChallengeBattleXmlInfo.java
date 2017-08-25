package com.snail.webgame.game.xml.info;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.snail.webgame.game.condtion.AbstractConditionCheck;

/**
 * 主线战场配置
 * 
 * @author tangjq
 * 
 */

public class ChallengeBattleXmlInfo {

	/**
	 * 主线副本类型
	 */
	public static final int TYPE_NO_1 = 1;
	public static final int TYPE_NO_2 = 2;
	public static final int TYPE_NO_3 = 3;

	private byte no;// 副本类型编号

	// 章节信息 <no,ChapterInfo>
	private Map<Integer, ChapterInfo> challengeBattlesInfoMap;

	public ChallengeBattleXmlInfo(byte no) {
		this.no = no;
	}

	public byte getNo() {
		return no;
	}

	public void setNo(byte no) {
		this.no = no;
	}

	public Map<Integer, ChapterInfo> getChallengeBattlesInfoMap() {
		return challengeBattlesInfoMap;
	}

	public void setChallengeBattlesInfoMap(Map<Integer, ChapterInfo> challengeBattlesInfoMap) {
		this.challengeBattlesInfoMap = challengeBattlesInfoMap;
	}

	public void addChallengeBattlesInfo(ChapterInfo info) {
		if (challengeBattlesInfoMap == null) {
			challengeBattlesInfoMap = new HashMap<Integer, ChapterInfo>();
		}
		challengeBattlesInfoMap.put(info.getChapterNo(), info);
	}

	public ChapterInfo getChapterInfoByNo(int no) {
		if (challengeBattlesInfoMap == null) {
			return null;
		}

		return challengeBattlesInfoMap.get(no);
	}

	/**
	 * 章节信息
	 * 
	 * @author tangjq
	 * 
	 */
	public static class ChapterInfo {

		private int chapterType;// 副本编号
		private int chapterNo;// 编号
		private int guildId;// 剧情id

		private List<AbstractConditionCheck> conds;// 条件

		private Map<Integer, BattleDetail> battleDetailMap;// <id,BattleDetail>

		private String chest1;	//第一个宝箱
		private String chest2; 	//第二个宝箱
		private String chest3;	//第三个宝箱
		
		private int star1;	//第一个宝箱
		private int star2; 	//第二个宝箱
		private int star3;	//第三个宝箱

		public ChapterInfo(int chapterType, int chapterNo, int guildId) {
			this.chapterType = chapterType;
			this.chapterNo = chapterNo;
			this.guildId = guildId;
		}
		

		public int getChapterType() {
			return chapterType;
		}

		public void setChapterType(int chapterType) {
			this.chapterType = chapterType;
		}


		public int getGuildId() {
			return guildId;
		}

		public void setGuildId(int guildId) {
			this.guildId = guildId;
		}

		public int getChapterNo() {
			return chapterNo;
		}

		public void setChapterNo(int chapterNo) {
			this.chapterNo = chapterNo;
		}

		public Map<Integer, BattleDetail> getBattleDetailMap() {
			return battleDetailMap;
		}

		public void setBattleDetailMap(Map<Integer, BattleDetail> battleDetailMap) {
			this.battleDetailMap = battleDetailMap;
		}

		public List<AbstractConditionCheck> getConds() {
			return conds;
		}

		public void setConds(List<AbstractConditionCheck> conds) {
			this.conds = conds;
		}

		public String getChest1() {
			return chest1;
		}


		public void setChest1(String chest1) {
			this.chest1 = chest1;
		}


		public String getChest2() {
			return chest2;
		}


		public void setChest2(String chest2) {
			this.chest2 = chest2;
		}


		public String getChest3() {
			return chest3;
		}


		public void setChest3(String chest3) {
			this.chest3 = chest3;
		}


		public int getStar1() {
			return star1;
		}


		public void setStar1(int star1) {
			this.star1 = star1;
		}


		public int getStar2() {
			return star2;
		}


		public void setStar2(int star2) {
			this.star2 = star2;
		}


		public int getStar3() {
			return star3;
		}


		public void setStar3(int star3) {
			this.star3 = star3;
		}


		/**
		 * 添加战场信息
		 * 
		 * @param detail
		 */
		public void addBattleDetail(BattleDetail detail) {

			if (battleDetailMap == null) {
				battleDetailMap = new HashMap<Integer, BattleDetail>();
			}

			int battleNo = detail.getBattleNo();
			battleDetailMap.put(battleNo, detail);
		}

		/**
		 * 获取战场信息
		 * 
		 * @param detail
		 */
		public BattleDetail getBattleDetail(int detailId) {

			if (battleDetailMap == null) {
				return null;
			}
			return battleDetailMap.get(detailId);
		}
		
	}

	/**
	 * 战役具体战场
	 * 
	 * @author tangjq
	 * 
	 */
	public static class BattleDetail {
		private int battleNo;// 编号
		private String battleName;//副本名称
		private byte chapterType;//副本类型
		private int chapterNo;// 章节编号
		private int guildId;// 剧情id
		private String bossName; //boss名字
		private int endTime; //结束时间
		
		private int battleNum=-1;//该副本规定时间内可刷次数 -1无次数限制,0-不可攻击
		private int battleInterTime=-1;//副本战斗间隔时间,秒(-1,无间隔时间)
		private String battleWeekDay="-1";//副本周几可刷 Date="1,2,4" -1-每天
		private int battleRefreshTime;//副本次数重置时间,整点 
		
		private int battleType; //副本类型 1 小兵,2 BOSS战,3 攻城战,4 精英副本,5 护送关卡
		
		private int unLockLv;//解锁等级
		
		private String itemNo; 	//必掉物品
		
		private String cardBag; //翻牌奖励
		
		/**
		 * 用于任务检测
		 */
		private int sweepNum;// 扫荡次数

		private List<AbstractConditionCheck> conds = new ArrayList<AbstractConditionCheck>();//攻打副本条件
		private List<AbstractConditionCheck> conds2 = new ArrayList<AbstractConditionCheck>();;// 条件
		private String bag;	//奖励
		private String bagView;//必掉检测
		
		private int endTimeType; //结束类型
		
		private int starType1; //副本评星类型 第1星
		private int starType2; //副本评星类型 第2星
		private int starType3; //副本评星类型 第3星
		
		private int minTime;//当前战力最小通关时间
		private int killNpc;//大BOSS
		
		//怪点位
		private HashMap<Integer,BattleDetailPoint> pointsMap = new HashMap<Integer,BattleDetailPoint>();

		public BattleDetail(int battleNo, int guildId, String bossName) {
			this.battleNo = battleNo;
			this.guildId = guildId;
			this.bossName = bossName;
		}
		
		
		public String getBag() {
			return bag;
		}


		public void setBag(String bag) {
			this.bag = bag;
		}


		public int getBattleNum() {
			return battleNum;
		}

		public void setBattleNum(int battleNum) {
			this.battleNum = battleNum;
		}

		public int getBattleInterTime() {
			return battleInterTime;
		}

		public void setBattleInterTime(int battleInterTime) {
			this.battleInterTime = battleInterTime;
		}

		public String getBattleWeekDay() {
			return battleWeekDay;
		}

		public void setBattleWeekDay(String battleWeekDay) {
			this.battleWeekDay = battleWeekDay;
		}

		public int getBattleRefreshTime() {
			return battleRefreshTime;
		}
		
		public void setBattleRefreshTime(int battleRefreshTime) {
			this.battleRefreshTime = battleRefreshTime;
		}
		
		public String getBossName() {
			return bossName;
		}

		public void setBossName(String bossName) {
			this.bossName = bossName;
		}

		public byte getChapterType() {
			return chapterType;
		}


		public void setChapterType(byte chapterType) {
			this.chapterType = chapterType;
		}

		public int getChapterNo() {
			return chapterNo;
		}

		public void setChapterNo(int chapterNo) {
			this.chapterNo = chapterNo;
		}

		public int getGuildId() {
			return guildId;
		}

		public void setGuildId(int guildId) {
			this.guildId = guildId;
		}
		

		public int getBattleNo() {
			return battleNo;
		}


		public void setBattleNo(int battleNo) {
			this.battleNo = battleNo;
		}
		
		public String getBattleName() {
			return battleName;
		}

		public void setBattleName(String battleName) {
			this.battleName = battleName;
		}

		public List<AbstractConditionCheck> getConds() {
			return conds;
		}

		public void setConds(List<AbstractConditionCheck> conds) {
			this.conds = conds;
		}
		
		public void addConds(List<AbstractConditionCheck> conds) {
			this.conds.addAll(conds);
		}

		public List<AbstractConditionCheck> getConds2() {
			return conds2;
		}

		public void setConds2(List<AbstractConditionCheck> conds2) {
			this.conds2 = conds2;
		}

		public HashMap<Integer, BattleDetailPoint> getPointsMap() {
			return pointsMap;
		}

		public void setPointsMap(HashMap<Integer, BattleDetailPoint> pointsMap) {
			this.pointsMap = pointsMap;
		}

		public int getEndTimeType() {
			return endTimeType;
		}


		public void setEndTimeType(int endTimeType) {
			this.endTimeType = endTimeType;
		}


		public int getEndTime() {
			return endTime;
		}


		public void setEndTime(int endTime) {
			this.endTime = endTime;
		}


		public int getBattleType() {
			return battleType;
		}


		public void setBattleType(int battleType) {
			this.battleType = battleType;
		}
		

		public int getStarType1() {
			return starType1;
		}


		public void setStarType1(int starType1) {
			this.starType1 = starType1;
		}


		public int getStarType2() {
			return starType2;
		}


		public void setStarType2(int starType2) {
			this.starType2 = starType2;
		}


		public int getStarType3() {
			return starType3;
		}


		public void setStarType3(int starType3) {
			this.starType3 = starType3;
		}


		public int getSweepNum() {
			return sweepNum;
		}

		public void setSweepNum(int sweepNum) {
			this.sweepNum = sweepNum;
		}
		

		public int getUnLockLv() {
			return unLockLv;
		}


		public void setUnLockLv(int unLockLv) {
			this.unLockLv = unLockLv;
		}

		public String getItemNo() {
			return itemNo;
		}


		public void setItemNo(String itemNo) {
			this.itemNo = itemNo;
		}


		public String getBagView() {
			return bagView;
		}


		public void setBagView(String bagView) {
			this.bagView = bagView;
		}

		
		public int getMinTime() {
			return minTime;
		}


		public void setMinTime(int minTime) {
			this.minTime = minTime;
		}

		

		public int getKillNpc() {
			return killNpc;
		}


		public void setKillNpc(int killNpc) {
			this.killNpc = killNpc;
		}


		/**
		 * 检查id规则
		 * 
		 * @return
		 */
//		public boolean checkId() {
//			// 63010201 章节战场id = 63 * 1000000 + 副本编号 * 10000+ 章节编号 * 100 + x
//
//			int value = this.battleNo % 1000000;
//
//			int chapterType = value / 10000;// 检测副本编号
//			int checkChapterNo = value % 10000 / 100;// 检测章节编号
//
//			if (checkChapterNo == this.chapterNo && chapterType == this.chapterType) {
//				return true;
//			}
//			return false;
//		}

		public String getCardBag() {
			return cardBag;
		}


		public void setCardBag(String cardBag) {
			this.cardBag = cardBag;
		}


		/**
		 * 根据战场id获取副本编号
		 * 
		 * @param chapterBattleId
		 * @return
		 */
		public static int getChallengeNoByBattleId(int chapterBattleId) {
			// 63010201 章节战场id = 63 * 1000000 + 副本编号 * 10000+ 章节编号 * 100 + x
			int value = chapterBattleId % 1000000;

			int checkChallengeNo = value / 10000;// 检测副本编号
			return checkChallengeNo;

		}

		/**
		 * 根据战场id获取章节编号
		 * 
		 * @param chapterBattleId
		 * @return
		 */
		public static int getChapterNoByBattleId(int chapterBattleId) {
			// 63010201 章节战场id = 63 * 1000000 + 副本编号 * 10000+ 章节编号 * 100 + x
			int value = chapterBattleId % 1000000;

			int checkChapterNo = value % 10000 / 100;// 检测章节编号
			return checkChapterNo;
		}

	}
	
	public static class BattleDetailPoint{
		private int pointNo; //点位
		private String gw;	//NPC
		private int position; //副本中怪点位置
		public int getPointNo() {
			return pointNo;
		}
		public void setPointNo(int pointNo) {
			this.pointNo = pointNo;
		}
		
		public String getGw() {
			return gw;
		}
		public void setGw(String gw) {
			this.gw = gw;
		}
		public int getPosition() {
			return position;
		}
		public void setPosition(int position) {
			this.position = position;
		}
		
		
		
	}

}
