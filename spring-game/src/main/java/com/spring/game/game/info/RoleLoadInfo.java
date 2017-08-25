package com.snail.webgame.game.info;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import com.snail.webgame.engine.common.to.BaseTO;
import com.snail.webgame.game.common.FightType;
import com.snail.webgame.game.common.GameValue;
import com.snail.webgame.game.common.util.DateUtil;
import com.snail.webgame.game.dao.typehandler.IntegerListTypeHandler;
import com.snail.webgame.game.dao.typehandler.IntegerMapTypeHandler;

public class RoleLoadInfo extends BaseTO {

	/**
	 * 竞技场字段
	 */
	private int competitionAward;// 竞技场奖励领取状态(二进制表示) 1-已领取

	/**
	 * PVP战斗状态 0-正常状态 1-竞技场报名中 2-竞技场战斗中 4-地图战斗中 5-世界地图PVP战斗准备中 6-对攻战报名中(单人)
	 * 7-对攻战战斗中 8-对攻战报名中(组队) 9-对攻战报名中(双人)
	 */
	private volatile byte inFight;
	private String fightServer;// 战斗服务器
	private String uuid;// 登录战斗服务器UUID

	private long fightStartTime; // 竞技场战斗开始时间
	private int winTimes;// 胜利次数
	private int loseTimes;// 失败次数
	private byte stageWinTimes;// 段位赛胜利次数
	private byte stageLoseTimes;// 段位赛失败次数
	private byte stageState;// 段位赛胜利失败

	private String funcOpenStr;// 功能开启, 格式：功能编号,功能编号

	// ==================对攻战====================
	private int mutualFightCount; // 每日对攻使用次数
	private long mutualFightLastTime; // 最后使用时间,精确到日期
	
	private volatile int leaderRoleId;
	private volatile int member1RoleId;
	private volatile int member2RoleId;
	private volatile int teamDuplicateId;
	private volatile AtomicBoolean lock = new AtomicBoolean(false); // 状态同步锁，false-正常状态 true-修改中
	
	private long cancelTime = 0; // 在取消匹配成功后设置此值，用于虽inFight==0,但是此时间和当前时间差值在2秒内，按照报名中处理(角色会被切入战场)
	private long sendMessageTime = 0; // 发送全局邀请时间，用于控制频繁发送全局消息
	// ==================对攻战====================

	// ================== 金币购买相关 ====================//
	private short buySpNum;// 当日购买次数（体力）
	private Timestamp lastBuySpTime;// 最后购买时间

	private int buyMoneyNum;// 当日购买次数（点金手）
	private Timestamp lastBuyMoneyTime;// 最后购买时间（点金手）

	private int buyEnergyNum;// 当日购买次数（精力）
	private Timestamp lastBuyEnergyTime;// 最后购买时间（精力）

	private int buyTechNum;// 当日购买次数（技能点）
	private Timestamp lastBuyTechTime;// 最后购买时间（技能点）
	// ================== 金币购买相关 ====================//
	// =============== 抽卡================//
	private byte recruitMoneyNum;// 今日免费抽卡次数
	private Timestamp lastRecruitMoneyTime;// 上次银子免费抽卡时间（null 第一次银子免费单抽）
	private int tenRecruitMoneyStats;// 银子十连抽次数
	private Timestamp lastRecruitCoinTime;// 上次金子免费抽卡时间（null 第一次金子免费单抽）
	private int tenRecruitCoinStatus;// 装备十连抽次数
	private int oneRecruitCoinOpTimes;// 单次必出次数记录
	
	private int oneRecruitHeroNum;// 武将单抽次数
	private int tenRecruitHeroNum;// 武将十连抽次数
	private Timestamp lastRecruitHeroFreeTime;//上一次免费武将单抽时间
	// =============== 抽卡================//

//	private long devotePoint;// 工会币
	private Timestamp lastDevoteTime;// 工会商店最后更新时间
	private int buyDevoteNum;// 当日手动刷新次数
	private Timestamp lastBuyDevoteTime;// 最后手动刷新时间

	private Timestamp lastNormalTime;// 普通商店最后更新时间
	private int autoRefreNum;// 普通商店自动刷新次数
	private int buyNormalNum;// 当日手动刷新次数
	private Timestamp lastBuyNormalTime;// 最后手动刷新时间

	private int kuafuMoney;// 跨服币
	private Timestamp kuafuAutoTime;// 跨服商店自动刷新时间
	private int buyKuafuNum;// 跨服商店当日手动刷新次数
	private Timestamp lastBuyKuafuTime;// 跨服商店最后手动刷新时间

	private int hisExploit;// 历史战功
	private int exploit;// 战功
	private Timestamp exploitAutoTime;// 战功商店自动刷新时间
	private int buyExploitShopNum;// 战功商店当日手动刷新次数
	private Timestamp lastBuyExploitTime;// 战功商店最后手动刷新时间

	// ========= 黑市商店 ===
	private int isBuyGoldShop; //是否已开启黑市商店（VIP购买）
	private int isRefreshGoldShop;//是否刷新出黑市商店 （副本开启）
	private Timestamp RefreshGoldShopTime;// 黑市商店消失时间（缓存）
	private Timestamp goldShopAutoTime;// 黑市商店自动刷新时间
	private int buyGoldShopNum;// 黑市商店当日手动刷新次数
	private Timestamp lastBuyGoldShopTime;// 黑市商店最后手动刷新时间
	private int isBuyGoldShopForVip;//是否购买黑市商城使用权 0-未买  1-已购买
	// ========= 异域商店 ===
	private int isBuyTurkShop; //是否已开启异域商店（VIP购买）
	private int isRefreshTurkShop;//是否刷新出异域商店 （副本开启）
	private Timestamp RefreshTurkShopTime;// 异域商店自动刷新消失（缓存）
	private Timestamp turkShopAutoTime;// 异域商店自动刷新时间
	private int buyTurkShopNum;// 异域商店当日手动刷新次数
	private Timestamp lastBuyTurkShopTime;// 异域商店最后手动刷新时间
	private int isBuyTurkShopForVip;//是否购买异域商城使用权 0-未买  1-已购买
	
	//====================世界boss
	private byte isJoinBoss; //今日是否参与过世界BOSS战斗  0-未参加 1-已参加
	
	

	private long courage;// 竞技场货币 勇气点
	private Timestamp lastCourageTime;// 竞技场商店最后更新时间
	private byte buyCourageNum;// 当日手动刷新次数
	private Timestamp lastBuyCourageTime;// 最后手动刷新时间

	private long justice;// 征战四方货币 正义点
	private Timestamp lastJusticeTime;// 征战四方商店最后更新时间
	private int buyJusticeNum;// 当日手动刷新次数
	private Timestamp lastBuyJusticeTime;// 最后手动刷新时间

	private int sceneNo;//场景编号
	private float pointX;// 场景中坐标X
	private float pointY;// 场景中坐标X
	private float pointZ;// 场景中坐标X

	private String checkInStr;// 已签到记录 格式：第几天已签过,第几天已签过
	private Timestamp lastCheckInTime;// 上次同步签到时间
	private int currCheckDay;// 当前第几天可签到
	private String vipCheckInStr;// vip已签到记录 格式：第几天已签过,第几天已签过

	private String checkIn7DayNum; // 奖励领取记录(格式：1,1,1,0,0,0,0)
	private byte checkIn7DayMaxLoginDays; // 连续登陆最大天数
	private byte checkIn7DayCurrentLoginDays;// 当前连续登陆天数
	private Timestamp checkIn7DayTime = new Timestamp(0); // 最后登陆时间

	private String drawLevelGiftStr;// 记录已领取等级礼包字符串,(格式:编号,编号)

	private boolean isMapPvpAttack;// 世界地图PVP进攻方

	// ==副本
	// 副本领奖信息(prizeNo,prizeNo)存库
	private String challengePrize;
	private String challengePrize2;
	private int challengeOpen; // 是否开启全部关卡

	// 副本通关关卡(缓存)
	private List<Integer> battleList = new ArrayList<Integer>();
	// 副本信息 <副本类型no,Map<章节, Map<副本no, ChallengeInfo>
	private Map<Byte, Map<Integer, Map<Integer, ChallengeBattleInfo>>> challengeMap = new HashMap<Byte, Map<Integer, Map<Integer, ChallengeBattleInfo>>>();

	// ==背包
	// 背包道具 <itemId,BagItemInfo>
	private Map<Integer, BagItemInfo> bagItemMap = new ConcurrentHashMap<Integer, BagItemInfo>();
	// 背包装备<equipId,EquipInfo>
	private Map<Integer, EquipInfo> bagEquipMap = new HashMap<Integer, EquipInfo>();

	// ==商店
	// 商店数据 <storeItemId,StoreItemInfo>
	private Map<Integer, StoreItemInfo> storeItemInfoMap = new HashMap<Integer, StoreItemInfo>();

	// ==神兵
	// 神兵<SeqId, RoleWeaponInfo>（总的roleWeapon集合，包含上阵的）
	private Map<Integer, RoleWeaponInfo> roleWeaponInfoMap = new HashMap<Integer, RoleWeaponInfo>();

	private int todayRunNum;// 今日已跑环次数
	private Timestamp lastRunTime;// 最后跑环时间
	
	private int todayActive;// 当前活跃度
	private Timestamp lastActiveChgTime;// 上次活跃度变化时间

	// == 宝物宝石活动
	// 宝石活动信息
	private FightGemInfo fightGemInfo = null;
	// 宝物活动信息
	private FightCampaignInfo fightCampaignInfo = null;

	// == 夺宝
	// 夺宝标示
	private byte snatchFlag;// 是否夺宝过 0-未夺宝 1-已夺宝(用于判断是不是第一次夺宝，第一次夺宝必成功)
	// 夺宝次数<宝石碎片编号，未夺到累计次数>
	private Map<Integer, Integer> snatchTimes;

	private byte defendTime;// 防守玩法已玩次数
	private Timestamp lastDefendTime;// 防守玩法最后玩的时间
	private byte attackAnotherTime;// 对攻玩法已玩次数
	private byte attackAnotherType;//狭路相逢难度
	private Timestamp lastAttackAnotherTime;// 对攻玩法最后玩的时间

	private String guideInfo;// 新手引导信息

	private long equip;// 装备商城货币
	private long pvp3Money;//组队PVP战斗奖励

	private Timestamp equipShopAutoTime;// 装备商店自动刷新时间
	private int buyEquipShopNum;// 装备商店当日手动刷新次数
	private Timestamp lastBuyEquipTime;// 装备商店最后手动刷新时间
	private int autoRefreEquipShopNum; // 装备自动刷新次数
	
	private Timestamp pvp3ShopAutoTime;// 组队商队自动刷新时间
	private int pvp3ShopAutoRefreNum; // 组队商队自动刷新次数
	private int pvp3ShopBuyNum;// 组队商队当日手动刷新次数
	private Timestamp pvp3ShopLastBuyTime;// 组队商队最后手动刷新时间
	
	private Timestamp teamShopAutoTime;// 组队商队自动刷新时间
	private int teamShopAutoRefreNum; // 组队商队自动刷新次数
	private int teamShopBuyNum;// 组队商队当日手动刷新次数
	private Timestamp teamShopLastBuyTime;// 组队商队最后手动刷新时间
	
	private Timestamp starShopAutoTime;// 星石商店自动刷新时间
	private int buyStarShopNum;// 星石商店当日手动刷新次数
	private Timestamp lastBuyStarTime;// 星石商店最后手动刷新时间
	private int autoRefreStarShopNum; // 星石自动刷新次数
	

	// === 红点
	private byte[] redPoint = new byte[GameValue.REDPOINT_NO];

	// soldierType : 升级次数;soldierType :升级次数 ; 1:1;2:5;
	private String soldierUpCounterInfo;
	
	private Timestamp lastOnlineTime;// 上一次在线时间
	private long totalTodayOnline;// 今日总在线时间
	private String drawOnlineAwardStr;// 记录当日已领取在线礼包字符串,(格式:编号,编号)
	
	private int isGetFirstCharge;// 是否领取首冲奖励 1-表示已领取
	private int linkPhoneState;// 手机绑定奖励 1-表示已领奖
	private int commentGameState;//评论奖励 1-已领取
	
	private Timestamp lastWeiXinTime;// 上次领取每日微信分享时间
	private int firstWeiXinGiftflag;// 首次分享奖励标记 0-还未分享过 1-可领取 2-已领取
	private int weiXinGiftflag;// 微信每日分享奖励标记 0-今日还未分享 1-每日分享奖励可领 2-每日分享奖励已领取
	
	private int curSevenLoginDay;// 运营活动：7日登陆 记录当前登录到第几天
	private Timestamp lastSevenDayTime;// 上一次7日活动登录时间
	private String sevenDayAwardStr;// 记录7日活动中奖励领取 格式：day-no,day-no
	
	private String wonderAwardStr;// 记录精彩活动中奖励领取 格式：no,no
	private int isBuyPlan;// 是否购买了投资计划 1-已购买
	
	private String drawVipGiftStr;// 记录已领取vip礼包字符串,(格式:编号,编号)
	
	private String drawVipLvBuyStr;// 记录vip特权礼包购买记录 格式：no,buyNum;no,buyNum
	private Timestamp lastBuyVipLvTime;// 上一次购买vip等级特权礼包时间
	
//	private Set<Integer> friendSetId = new HashSet<Integer>(); //roleId
//	private Set<Integer> friendRequestSetId = new HashSet<Integer>(); //roleId
//	private Set<Integer> blackFriendSetId = new HashSet<Integer>(); //roleId
	
	private Map<Integer, PresentEnergyInfo> presentEnergyMap = new HashMap<Integer, PresentEnergyInfo>(); //key-id 数据库主键Id 
	private Map<Integer, Long> recordPresentTimeMap = new HashMap<Integer, Long>(); //key roleId value 精力赠送时间

	// 布阵位置开启标识
	private List<Integer> deployPosOpen = new ArrayList<Integer>(2);
	private Set<Integer> roleClubMemberInfoSet = new HashSet<Integer>(); //公会Id 只存放申请的公会ID
	
	private short soldierFreeUpNum;//免费兵法升级次数
	private Timestamp soldierFreeUpTime;//免费兵法升级时间
	
	private byte refBiaoCheNum;// 镖车刷新次数 第二天重置
	private byte biaocheType;// 镖车类型,压镖成功后默认白色
	private byte thisbiaocheTypeJieBiaoNum;//本次镖车被劫次数
	private boolean isNotice = false;// 是否已经通告（橙色镖车第一次押镖的时候，通知一次）
	private byte todayYabiaoNum;// 当天押镖次数
	private byte todayJiebiaoNum;// 当天截镖次数
	private byte hubiaoNum;// 护送押镖次数
	private int jiebiaoRoleId;//劫镖人ID
	private int hubiaoRoleId;//护镖人ID
	private int yabiaoFriendRoleId;// 押镖人ID
	private HashMap<Integer, Long> rejectHelpMap;// 拒绝好友护送要求
	private Timestamp biaocheQueryTime;//当天第一次查询镖车时间
	
	private Set<Integer> clubDisplaySet = new HashSet<Integer>(); //保存玩家显示的公会信息 每次更换刷新
	
	private byte getPresentEnergyTimes; // 每日领取赠送精力次数
	private Timestamp queryEnergyInfoTime; //上次查询赠送精力的时间
	
	
	//====================世界矿
	private int mineNum;// 当日矿抢夺次数
	private Timestamp lastMineTime;// 最后矿抢夺时间
	private int buyMine;// 当日抢夺购买次数
	private Timestamp lastBuyMineTime;// 最后购买次数时间
	private int starMoney;// 星石分解后的星石币
	
	
	//============称号模块
	private String nowAppellation; //当前佩戴称号 xmlId:time
	private String allAppellation;//玩家所有的称号  ID,到点秒数  -1表示永久称号或者自动刷新称号 xmlId:time;xmlId:time
	
	//时装模块
	// 玩家已领取套装奖励<次数>
	private List<Integer> haveReward = new ArrayList<Integer>();
	
	//练兵场扫荡用-类型扫荡最大通关数                   类型阶级_最大关数,类型阶级_最大关数
	private String allExpActionMax;
	//玩家练兵场扫荡情况String转成map方便使用
	public Map<Integer,Integer> allExpActionMaxMap = new ConcurrentHashMap<Integer,Integer>();
	
	// ================通关的世界地图npcno
	private List<Integer> passMapNpcNos = new ArrayList<Integer>();
	
	// 今天奖励的组队副本次数
	private Map<Integer, Integer> teamChallengeTimes;
	private long teamChallengeFightLastTime; // 最后使用时间,精确到日期
	private int teamMoney;// 斩将令（组队副本货币）
	private String teamChallengeStr;//通关的组队副本

	// 今天获得的3V3奖励次数
	private byte team3V3Times;
	private long team3V3FightLastTime;
		
	// 玩家坐骑信息
	private Map<Integer, RideInfo> roleRideMap = new HashMap<Integer, RideInfo>();
	
	private long serverCheckTime;//检测时间
	private int serverCheckTimeNum;//连续加速
	
	private String hireInfos; // 雇佣的heroId:雇佣的时间 heroId:time;heroId:time //雇佣成功的兵
	// 雇佣的武将镜像 <HireType,<heroId,info>>
	private Map<Integer,Map<Integer, HeroImageInfo>> heroImageMap = new HashMap<Integer, Map<Integer,HeroImageInfo>>(); //
		
	public int getCompetitionAward() {
		return competitionAward;
	}

	public void setCompetitionAward(int competitionAward) {
		this.competitionAward = competitionAward;
	}

	/**
	 * PVP战斗状态 0-正常状态 1-竞技场报名中 2-竞技场战斗中 4-地图战斗中 5-世界地图PVP战斗准备中 6-对攻战报名中(单人)
	 * 7-对攻战战斗中 8-对攻战报名中(3人) 9-对攻战报名中(双人) 
	 * 10-组队副本战斗中  11-组队副本报名中  12-3V3战斗中   13-3V3报名中 (单人) 14-3V3报名中 (双人) 15-3V3报名中 (3人)
	 * 
	 * @return
	 */
	public byte getInFight() {
		return inFight;
	}

	/**
	 * PVP战斗状态 0-正常状态 1-竞技场报名中 2-竞技场战斗中 4-地图战斗中 5-世界地图PVP战斗准备中 6-对攻战报名中(单人)
	 * 7-对攻战战斗中 8-对攻战报名中(3人) 9-对攻战报名中(双人)
	 * 10-组队副本战斗中  11-组队副本报名中  12-3V3战斗中   13-3V3报名中 (单人) 14-3V3报名中 (双人) 15-3V3报名中 (3人)
	 * @param inFight
	 */
	public void setInFight(byte inFight) {
		this.inFight = inFight;
	}

	public String getFightServer() {
		return fightServer;
	}

	public void setFightServer(String fightServer) {
		this.fightServer = fightServer;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public long getFightStartTime() {
		return fightStartTime;
	}

	public void setFightStartTime(long fightStartTime) {
		this.fightStartTime = fightStartTime;
	}

	public int getWinTimes() {
		return winTimes;
	}

	public void setWinTimes(int winTimes) {
		this.winTimes = winTimes;
	}

	public int getLoseTimes() {
		return loseTimes;
	}

	public void setLoseTimes(int loseTimes) {
		this.loseTimes = loseTimes;
	}

	public byte getStageState() {
		return stageState;
	}

	public void setStageState(byte stageState) {
		this.stageState = stageState;
	}

	public byte getStageWinTimes() {
		return stageWinTimes;
	}

	public void setStageWinTimes(byte stageWinTimes) {
		this.stageWinTimes = stageWinTimes;
	}

	public byte getStageLoseTimes() {
		return stageLoseTimes;
	}

	public void setStageLoseTimes(byte stageLoseTimes) {
		this.stageLoseTimes = stageLoseTimes;
	}

	public byte getRecruitMoneyNum() {
		return recruitMoneyNum;
	}
	
	public byte getTodayRecruitMoneyNum() {
		if (lastRecruitMoneyTime != null && DateUtil.isSameDay(System.currentTimeMillis(), lastRecruitMoneyTime.getTime())) {
			return recruitMoneyNum;
		}
		return 0;
	}

	public void setRecruitMoneyNum(byte recruitMoneyNum) {
		this.recruitMoneyNum = recruitMoneyNum;
	}

	public Timestamp getLastRecruitMoneyTime() {
		return lastRecruitMoneyTime;
	}

	public void setLastRecruitMoneyTime(Timestamp lastRecruitMoneyTime) {
		this.lastRecruitMoneyTime = lastRecruitMoneyTime;
	}

	public Timestamp getLastRecruitCoinTime() {
		return lastRecruitCoinTime;
	}

	public void setLastRecruitCoinTime(Timestamp lastRecruitCoinTime) {
		this.lastRecruitCoinTime = lastRecruitCoinTime;
	}

	public short getBuySpNum() {
		return buySpNum;
	}

	public void setBuySpNum(short buySpNum) {
		this.buySpNum = buySpNum;
	}

	public int getTodayBuySpNum() {
		if (lastBuySpTime != null && DateUtil.isSameDay(System.currentTimeMillis(), lastBuySpTime.getTime())) {
			return buySpNum;
		}
		return 0;
	}

	public Timestamp getLastBuySpTime() {
		if (lastBuySpTime == null) {
			return new Timestamp(0);
		}
		return lastBuySpTime;
	}

	public void setLastBuySpTime(Timestamp lastBuySpTime) {
		this.lastBuySpTime = lastBuySpTime;
	}

	public int getBuyMoneyNum() {
		return buyMoneyNum;
	}

	public void setBuyMoneyNum(int buyMoneyNum) {
		this.buyMoneyNum = buyMoneyNum;
	}

	public int getTodayBuyMoneyNum() {
		if (lastBuyMoneyTime != null && DateUtil.isSameDay(System.currentTimeMillis(), lastBuyMoneyTime.getTime())) {
			return buyMoneyNum;
		}
		return 0;
	}

	public Timestamp getLastBuyMoneyTime() {
		if (lastBuyMoneyTime == null) {
			return new Timestamp(0);
		}
		return lastBuyMoneyTime;
	}

	public void setLastBuyMoneyTime(Timestamp lastBuyMoneyTime) {
		this.lastBuyMoneyTime = lastBuyMoneyTime;
	}

	public int getBuyTechNum() {
		return buyTechNum;
	}

	public void setBuyTechNum(int buyTechNum) {
		this.buyTechNum = buyTechNum;
	}

	public Timestamp getLastBuyTechTime() {
		return lastBuyTechTime;
	}

	public void setLastBuyTechTime(Timestamp lastBuyTechTime) {
		this.lastBuyTechTime = lastBuyTechTime;
	}
	
	public int getTodayBuyTechNum() {
		if (lastBuyTechTime != null && DateUtil.isSameDay(System.currentTimeMillis(), lastBuyTechTime.getTime())) {
			return buyTechNum;
		}
		return 0;
	}

	public String getFuncOpenStr() {
		return funcOpenStr;
	}

	public void setFuncOpenStr(String funcOpenStr) {
		this.funcOpenStr = funcOpenStr;
	}

	public int getMutualFightCount() {
		return mutualFightCount;
	}

	public void setMutualFightCount(int mutualFightCount) {
		this.mutualFightCount = mutualFightCount;
	}

	public long getMutualFightLastTime() {
		return mutualFightLastTime;
	}

	public byte getIsJoinBoss() {
		return isJoinBoss;
	}

	public void setIsJoinBoss(byte isJoinBoss) {
		this.isJoinBoss = isJoinBoss;
	}

	public void setMutualFightLastTime(long mutualFightLastTime) {
		this.mutualFightLastTime = mutualFightLastTime;
	}

	public int getLeaderRoleId() {
		return leaderRoleId;
	}

	public void setLeaderRoleId(int leaderRoleId) {
		this.leaderRoleId = leaderRoleId;
	}

	public int getMember1RoleId() {
		return member1RoleId;
	}

	public void setMember1RoleId(int member1RoleId) {
		this.member1RoleId = member1RoleId;
	}

	public int getMember2RoleId() {
		return member2RoleId;
	}

	public void setMember2RoleId(int member2RoleId) {
		this.member2RoleId = member2RoleId;
	}

	public AtomicBoolean getLock() {
		return lock;
	}

	public void setLock(AtomicBoolean lock) {
		this.lock = lock;
	}

	public long getCancelTime() {
		return cancelTime;
	}

	public void setCancelTime(long cancelTime) {
		this.cancelTime = cancelTime;
	}

	public long getSendMessageTime() {
		return sendMessageTime;
	}

	public void setSendMessageTime(long sendMessageTime) {
		this.sendMessageTime = sendMessageTime;
	}

	public long getJustice() {
		return justice;
	}

	public void setJustice(long justice) {
		this.justice = justice;
	}

	public Timestamp getLastJusticeTime() {
		return lastJusticeTime;
	}

	public void setLastJusticeTime(Timestamp lastJusticeTime) {
		this.lastJusticeTime = lastJusticeTime;
	}

	public int getBuyJusticeNum() {
		return buyJusticeNum;
	}

	public void setBuyJusticeNum(int buyJusticeNum) {
		this.buyJusticeNum = buyJusticeNum;
	}

	public Timestamp getLastBuyJusticeTime() {
		return lastBuyJusticeTime;
	}

	public void setLastBuyJusticeTime(Timestamp lastBuyJusticeTime) {
		this.lastBuyJusticeTime = lastBuyJusticeTime;
	}

	public Timestamp getLastDevoteTime() {
		return lastDevoteTime;
	}

	public void setLastDevoteTime(Timestamp lastDevoteTime) {
		this.lastDevoteTime = lastDevoteTime;
	}

	public int getBuyDevoteNum() {
		return buyDevoteNum;
	}

	public void setBuyDevoteNum(int buyDevoteNum) {
		this.buyDevoteNum = buyDevoteNum;
	}

	public Timestamp getLastBuyDevoteTime() {
		return lastBuyDevoteTime;
	}

	public void setLastBuyDevoteTime(Timestamp lastBuyDevoteTime) {
		this.lastBuyDevoteTime = lastBuyDevoteTime;
	}

	public Timestamp getLastNormalTime() {
		return lastNormalTime;
	}

	public void setLastNormalTime(Timestamp lastNormalTime) {
		this.lastNormalTime = lastNormalTime;
	}

	public int getAutoRefreNum() {
		return autoRefreNum;
	}

	public void setAutoRefreNum(int autoRefreNum) {
		this.autoRefreNum = autoRefreNum;
	}

	public int getBuyNormalNum() {
		return buyNormalNum;
	}

	public void setBuyNormalNum(int buyNormalNum) {
		this.buyNormalNum = buyNormalNum;
	}

	public Timestamp getLastBuyNormalTime() {
		return lastBuyNormalTime;
	}

	public void setLastBuyNormalTime(Timestamp lastBuyNormalTime) {
		this.lastBuyNormalTime = lastBuyNormalTime;
	}

	public int getKuafuMoney() {
		return kuafuMoney;
	}

	public void setKuafuMoney(int kuafuMoney) {
		this.kuafuMoney = kuafuMoney;
	}

	public Timestamp getKuafuAutoTime() {
		return kuafuAutoTime;
	}

	public void setKuafuAutoTime(Timestamp kuafuAutoTime) {
		this.kuafuAutoTime = kuafuAutoTime;
	}

	public int getBuyKuafuNum() {
		return buyKuafuNum;
	}

	public void setBuyKuafuNum(int buyKuafuNum) {
		this.buyKuafuNum = buyKuafuNum;
	}

	public Timestamp getLastBuyKuafuTime() {
		return lastBuyKuafuTime;
	}

	public void setLastBuyKuafuTime(Timestamp lastBuyKuafuTime) {
		this.lastBuyKuafuTime = lastBuyKuafuTime;
	}

	public int getHisExploit() {
		return hisExploit;
	}

	public void setHisExploit(int hisExploit) {
		this.hisExploit = hisExploit;
	}

	public int getExploit() {
		return exploit;
	}

	public void setExploit(int exploit) {
		this.exploit = exploit;
	}

	public Timestamp getExploitAutoTime() {
		return exploitAutoTime;
	}

	public void setExploitAutoTime(Timestamp exploitAutoTime) {
		this.exploitAutoTime = exploitAutoTime;
	}

	public int getBuyExploitShopNum() {
		return buyExploitShopNum;
	}

	public void setBuyExploitShopNum(int buyExploitShopNum) {
		this.buyExploitShopNum = buyExploitShopNum;
	}

	public Timestamp getLastBuyExploitTime() {
		return lastBuyExploitTime;
	}

	public void setLastBuyExploitTime(Timestamp lastBuyExploitTime) {
		this.lastBuyExploitTime = lastBuyExploitTime;
	}

	public Timestamp getGoldShopAutoTime() {
		return goldShopAutoTime;
	}

	public void setGoldShopAutoTime(Timestamp goldShopAutoTime) {
		this.goldShopAutoTime = goldShopAutoTime;
	}

	public int getBuyGoldShopNum() {
		return buyGoldShopNum;
	}

	public void setBuyGoldShopNum(int buyGoldShopNum) {
		this.buyGoldShopNum = buyGoldShopNum;
	}

	public Timestamp getLastBuyGoldShopTime() {
		return lastBuyGoldShopTime;
	}

	public void setLastBuyGoldShopTime(Timestamp lastBuyGoldShopTime) {
		this.lastBuyGoldShopTime = lastBuyGoldShopTime;
	}

	public long getCourage() {
		return courage;
	}

	public void setCourage(long courage) {
		this.courage = courage;
	}

	public Timestamp getLastCourageTime() {
		return lastCourageTime;
	}

	public void setLastCourageTime(Timestamp lastCourageTime) {
		this.lastCourageTime = lastCourageTime;
	}

	public byte getBuyCourageNum() {
		return buyCourageNum;
	}

	public void setBuyCourageNum(byte buyCourageNum) {
		this.buyCourageNum = buyCourageNum;
	}

	public Timestamp getLastBuyCourageTime() {
		return lastBuyCourageTime;
	}

	public void setLastBuyCourageTime(Timestamp lastBuyCourageTime) {
		this.lastBuyCourageTime = lastBuyCourageTime;
	}

	public int getSceneNo() {
		return sceneNo;
	}

	public void setSceneNo(int sceneNo) {
		this.sceneNo = sceneNo;
	}

	public float getPointX() {
		return pointX;
	}

	public void setPointX(float pointX) {
		this.pointX = pointX;
	}

	public float getPointY() {
		return pointY;
	}

	public void setPointY(float pointY) {
		this.pointY = pointY;
	}

	public float getPointZ() {
		return pointZ;
	}

	public void setPointZ(float pointZ) {
		this.pointZ = pointZ;
	}

	public String getCheckInStr() {
		return checkInStr;
	}

	public void setCheckInStr(String checkInStr) {
		this.checkInStr = checkInStr;
	}

	public Timestamp getLastCheckInTime() {
		return lastCheckInTime;
	}

	public void setLastCheckInTime(Timestamp lastCheckInTime) {
		this.lastCheckInTime = lastCheckInTime;
	}

	public int getCurrCheckDay() {
		return currCheckDay;
	}

	public void setCurrCheckDay(int currCheckDay) {
		this.currCheckDay = currCheckDay;
	}

	public String getCheckIn7DayNum() {
		return checkIn7DayNum;
	}

	public void setCheckIn7DayNum(String checkIn7DayNum) {
		this.checkIn7DayNum = checkIn7DayNum;
	}

	public byte getCheckIn7DayMaxLoginDays() {
		return checkIn7DayMaxLoginDays;
	}

	public void setCheckIn7DayMaxLoginDays(byte checkIn7DayMaxLoginDays) {
		this.checkIn7DayMaxLoginDays = checkIn7DayMaxLoginDays;
	}

	public byte getCheckIn7DayCurrentLoginDays() {
		return checkIn7DayCurrentLoginDays;
	}

	public void setCheckIn7DayCurrentLoginDays(byte checkIn7DayCurrentLoginDays) {
		this.checkIn7DayCurrentLoginDays = checkIn7DayCurrentLoginDays;
	}

	public Timestamp getCheckIn7DayTime() {
		return checkIn7DayTime;
	}

	public void setCheckIn7DayTime(Timestamp checkIn7DayTime) {
		this.checkIn7DayTime = checkIn7DayTime;
	}

	@Override
	public byte getSaveMode() {
		return ONLINE;
	}

	public Map<Integer, BagItemInfo> getBagItemMap() {
		return bagItemMap;
	}

	public void setBagItemMap(Map<Integer, BagItemInfo> bagItemMap) {
		this.bagItemMap = bagItemMap;
	}

	public Map<Integer, StoreItemInfo> getStoreItemInfoMap() {
		return storeItemInfoMap;
	}

	public void setStoreItemInfoMap(Map<Integer, StoreItemInfo> storeItemInfoMap) {
		this.storeItemInfoMap = storeItemInfoMap;
	}

	public Map<Integer, RoleWeaponInfo> getRoleWeaponInfoMap() {
		return roleWeaponInfoMap;
	}

	public void setRoleWeaponInfoMap(Map<Integer, RoleWeaponInfo> roleWeaponInfoMap) {
		this.roleWeaponInfoMap = roleWeaponInfoMap;
	}

	public List<Integer> getBattleList() {
		return battleList;
	}

	public void setBattleList(List<Integer> battleList) {
		this.battleList = battleList;
	}

	public void addBattle(int battleNo) {
		if (!battleList.contains(battleNo)) {
			battleList.add(battleNo);
		}
	}

	public String getChallengePrize() {
		return challengePrize;
	}

	public void setChallengePrize(String challengePrize) {
		this.challengePrize = challengePrize;
	}

	public String getChallengePrize2() {
		return challengePrize2;
	}

	public void setChallengePrize2(String challengePrize2) {
		this.challengePrize2 = challengePrize2;
	}

	public Map<Byte, Map<Integer, Map<Integer, ChallengeBattleInfo>>> getChallengeMap() {
		return challengeMap;
	}

	public Map<Integer, Map<Integer, ChallengeBattleInfo>> getChallengeMap(byte chapterType) {
		return challengeMap.get(chapterType);
	}

	public void setChallengeMap(Map<Byte, Map<Integer, Map<Integer, ChallengeBattleInfo>>> challengeMap) {
		this.challengeMap = challengeMap;
	}

	public Map<Integer, EquipInfo> getBagEquipMap() {
		return bagEquipMap;
	}

	public void setBagEquipMap(Map<Integer, EquipInfo> bagEquipMap) {
		this.bagEquipMap = bagEquipMap;
	}

	public FightGemInfo getFightGemInfo() {
		return fightGemInfo;
	}

	public void setFightGemInfo(FightGemInfo fightGemInfo) {
		this.fightGemInfo = fightGemInfo;
	}

	public FightCampaignInfo getFightCampaignInfo() {
		return fightCampaignInfo;
	}

	public void setFightCampaignInfo(FightCampaignInfo fightCampaignInfo) {
		this.fightCampaignInfo = fightCampaignInfo;
	}

	public boolean isMapPvpAttack() {
		return isMapPvpAttack;
	}

	public void setMapPvpAttack(boolean isMapPvpAttack) {
		this.isMapPvpAttack = isMapPvpAttack;
	}

	public int getBuyEnergyNum() {
		return buyEnergyNum;
	}

	public void setBuyEnergyNum(int buyEnergyNum) {
		this.buyEnergyNum = buyEnergyNum;
	}

	public int getTodayBuyEnergyNum() {
		if (lastBuyEnergyTime != null && DateUtil.isSameDay(System.currentTimeMillis(), lastBuyEnergyTime.getTime())) {
			return buyEnergyNum;
		}
		return 0;
	}

	public Timestamp getLastBuyEnergyTime() {
		return lastBuyEnergyTime;
	}

	public void setLastBuyEnergyTime(Timestamp lastBuyEnergyTime) {
		this.lastBuyEnergyTime = lastBuyEnergyTime;
	}

	public String getDrawLevelGiftStr() {
		return drawLevelGiftStr;
	}

	public void setDrawLevelGiftStr(String drawLevelGiftStr) {
		this.drawLevelGiftStr = drawLevelGiftStr;
	}

	public long getEquip() {
		return equip;
	}

	public void setEquip(long equip) {
		this.equip = equip;
	}

	public Timestamp getEquipShopAutoTime() {
		return equipShopAutoTime;
	}

	public void setEquipShopAutoTime(Timestamp equipShopAutoTime) {
		this.equipShopAutoTime = equipShopAutoTime;
	}

	public int getBuyEquipShopNum() {
		return buyEquipShopNum;
	}

	public void setBuyEquipShopNum(int buyEquipShopNum) {
		this.buyEquipShopNum = buyEquipShopNum;
	}

	public Timestamp getLastBuyEquipTime() {
		return lastBuyEquipTime;
	}

	public void setLastBuyEquipTime(Timestamp lastBuyEquipTime) {
		this.lastBuyEquipTime = lastBuyEquipTime;
	}

	public byte getDefendTime() {
		return defendTime;
	}

	public void setDefendTime(byte defendTime) {
		this.defendTime = defendTime;
	}

	public Timestamp getLastDefendTime() {
		return lastDefendTime;
	}

	public void setLastDefendTime(Timestamp lastDefendTime) {
		this.lastDefendTime = lastDefendTime;
	}

	public byte getAttackAnotherTime() {
		return attackAnotherTime;
	}

	public void setAttackAnotherTime(byte attackAnotherTime) {
		this.attackAnotherTime = attackAnotherTime;
	}

	public Timestamp getLastAttackAnotherTime() {
		return lastAttackAnotherTime;
	}

	public void setLastAttackAnotherTime(Timestamp lastAttackAnotherTime) {
		this.lastAttackAnotherTime = lastAttackAnotherTime;
	}

	public String getGuideInfo() {
		return guideInfo;
	}

	public void setGuideInfo(String guideInfo) {
		this.guideInfo = guideInfo;
	}

	public int getTenRecruitMoneyStats() {
		return tenRecruitMoneyStats;
	}

	public void setTenRecruitMoneyStats(int tenRecruitMoneyStats) {
		this.tenRecruitMoneyStats = tenRecruitMoneyStats;
	}

	public int getAutoRefreEquipShopNum() {
		return autoRefreEquipShopNum;
	}

	public void setAutoRefreEquipShopNum(int autoRefreEquipShopNum) {
		this.autoRefreEquipShopNum = autoRefreEquipShopNum;
	}

	public byte getSnatchFlag() {
		return snatchFlag;
	}

	public void setSnatchFlag(byte snatchFlag) {
		this.snatchFlag = snatchFlag;
	}

	public int getSnatchTimes(int stoneNo) {
		if (snatchTimes != null && snatchTimes.containsKey(stoneNo)) {
			return snatchTimes.get(stoneNo);
		}
		return 0;
	}

	public Map<Integer, Integer> getSnatchTimes() {
		if (snatchTimes == null) {
			return new HashMap<Integer, Integer>();
		}
		return snatchTimes;
	}

	public void setSnatchTimes(Map<Integer, Integer> snatchTimes) {
		this.snatchTimes = snatchTimes;
	}

	public int getChallengeOpen() {
		return challengeOpen;
	}

	public void setChallengeOpen(int challengeOpen) {
		this.challengeOpen = challengeOpen;
	}

	public int getTenRecruitCoinStatus() {
		return tenRecruitCoinStatus;
	}

	public void setTenRecruitCoinStatus(int tenRecruitCoinStatus) {
		this.tenRecruitCoinStatus = tenRecruitCoinStatus;
	}

	public byte[] getRedPoint() {
		return redPoint;
	}

	public void setRedPoint(byte[] redPoint) {
		this.redPoint = redPoint;
	}

	public void putRedPoint(byte type, byte status) {
		redPoint[type] = status;
	}

	public String getSoldierUpCounterInfo() {
		return soldierUpCounterInfo;
	}

	public void setSoldierUpCounterInfo(String soldierUpCounterInfo) {
		this.soldierUpCounterInfo = soldierUpCounterInfo;
	}

	public int getTodayRunNum() {
		return todayRunNum;
	}

	public void setTodayRunNum(int todayRunNum) {
		this.todayRunNum = todayRunNum;
	}

	public Timestamp getLastRunTime() {
		return lastRunTime;
	}

	public void setLastRunTime(Timestamp lastRunTime) {
		this.lastRunTime = lastRunTime;
	}

	public Map<Integer, PresentEnergyInfo> getPresentEnergyMap() {
		return presentEnergyMap;
	}

	public void setPresentEnergyMap(Map<Integer, PresentEnergyInfo> presentEnergyMap) {
		this.presentEnergyMap = presentEnergyMap;
	}

	public Map<Integer, Long> getRecordPresentTimeMap() {
		return recordPresentTimeMap;
	}

	public void setRecordPresentTimeMap(Map<Integer, Long> recordPresentTimeMap) {
		this.recordPresentTimeMap = recordPresentTimeMap;
	}

	public List<Integer> getDeployPosOpen() {
		if (deployPosOpen == null) {
			deployPosOpen = new ArrayList<Integer>(2);
		}
		if (deployPosOpen.size() <= 0) {
			deployPosOpen.add(0);
		}
		if (deployPosOpen.size() <= 1) {
			deployPosOpen.add(0);
		}
		return deployPosOpen;
	}

	public String getDeployPosOpenStr() {
		return IntegerListTypeHandler.getString(getDeployPosOpen());
	}

	public void setDeployPosOpen(List<Integer> deployPosOpen) {
		this.deployPosOpen = deployPosOpen;
	}

	public Timestamp getLastOnlineTime() {
		if(lastOnlineTime == null)
		{
			return new Timestamp(System.currentTimeMillis());
		}
		return lastOnlineTime;
	}

	public void setLastOnlineTime(Timestamp lastOnlineTime) {
		this.lastOnlineTime = lastOnlineTime;
	}

	public long getTotalTodayOnline() {
		return totalTodayOnline;
	}

	public void setTotalTodayOnline(long totalTodayOnline) {
		this.totalTodayOnline = totalTodayOnline;
	}

	public int getIsGetFirstCharge() {
		return isGetFirstCharge;
	}

	public void setIsGetFirstCharge(int isGetFirstCharge) {
		this.isGetFirstCharge = isGetFirstCharge;
	}

	public String getDrawOnlineAwardStr() {
		return drawOnlineAwardStr;
	}

	public void setDrawOnlineAwardStr(String drawOnlineAwardStr) {
		this.drawOnlineAwardStr = drawOnlineAwardStr;
	}

	public int getLinkPhoneState() {
		return linkPhoneState;
	}

	public void setLinkPhoneState(int linkPhoneState) {
		this.linkPhoneState = linkPhoneState;
	}

	public Timestamp getLastWeiXinTime() {
		return lastWeiXinTime;
	}

	public void setLastWeiXinTime(Timestamp lastWeiXinTime) {
		this.lastWeiXinTime = lastWeiXinTime;
	}

	public int getFirstWeiXinGiftflag() {
		return firstWeiXinGiftflag;
	}

	public void setFirstWeiXinGiftflag(int firstWeiXinGiftflag) {
		this.firstWeiXinGiftflag = firstWeiXinGiftflag;
	}

	public int getWeiXinGiftflag() {
		return weiXinGiftflag;
	}

	public void setWeiXinGiftflag(int weiXinGiftflag) {
		this.weiXinGiftflag = weiXinGiftflag;
	}

	public String getDrawVipGiftStr() {
		return drawVipGiftStr;
	}

	public void setDrawVipGiftStr(String drawVipGiftStr) {
		this.drawVipGiftStr = drawVipGiftStr;
	}

	public int getCurSevenLoginDay() {
		return curSevenLoginDay;
	}

	public void setCurSevenLoginDay(int curSevenLoginDay) {
		this.curSevenLoginDay = curSevenLoginDay;
	}

	public Timestamp getLastSevenDayTime() {
		return lastSevenDayTime;
	}

	public void setLastSevenDayTime(Timestamp lastSevenDayTime) {
		this.lastSevenDayTime = lastSevenDayTime;
	}

	public String getSevenDayAwardStr() {
		return sevenDayAwardStr;
	}

	public void setSevenDayAwardStr(String sevenDayAwardStr) {
		this.sevenDayAwardStr = sevenDayAwardStr;
	}

	public Set<Integer> getRoleClubMemberInfoSet() {
		return roleClubMemberInfoSet;
	}

	public void setRoleClubMemberInfoSet(Set<Integer> roleClubMemberInfoSet) {
		this.roleClubMemberInfoSet = roleClubMemberInfoSet;
	}

	public int getIsBuyGoldShop() {
		return isBuyGoldShop;
	}

	public void setIsBuyGoldShop(int isBuyGoldShop) {
		this.isBuyGoldShop = isBuyGoldShop;
	}

	public int getIsRefreshGoldShop() {
		return isRefreshGoldShop;
	}

	public void setIsRefreshGoldShop(int isRefreshGoldShop) {
		this.isRefreshGoldShop = isRefreshGoldShop;
	}

	public int getIsBuyTurkShop() {
		return isBuyTurkShop;
	}

	public void setIsBuyTurkShop(int isBuyTurkShop) {
		this.isBuyTurkShop = isBuyTurkShop;
	}

	public int getIsRefreshTurkShop() {
		return isRefreshTurkShop;
	}

	public void setIsRefreshTurkShop(int isRefreshTurkShop) {
		this.isRefreshTurkShop = isRefreshTurkShop;
	}

	public Timestamp getTurkShopAutoTime() {
		return turkShopAutoTime;
	}

	public void setTurkShopAutoTime(Timestamp turkShopAutoTime) {
		this.turkShopAutoTime = turkShopAutoTime;
	}

	public int getBuyTurkShopNum() {
		return buyTurkShopNum;
	}

	public void setBuyTurkShopNum(int buyTurkShopNum) {
		this.buyTurkShopNum = buyTurkShopNum;
	}

	public Timestamp getLastBuyTurkShopTime() {
		return lastBuyTurkShopTime;
	}

	public void setLastBuyTurkShopTime(Timestamp lastBuyTurkShopTime) {
		this.lastBuyTurkShopTime = lastBuyTurkShopTime;
	}
	
	public Timestamp getPvp3ShopAutoTime() {
		return pvp3ShopAutoTime;
	}

	public void setPvp3ShopAutoTime(Timestamp pvp3ShopAutoTime) {
		this.pvp3ShopAutoTime = pvp3ShopAutoTime;
	}

	public int getPvp3ShopAutoRefreNum() {
		return pvp3ShopAutoRefreNum;
	}

	public void setPvp3ShopAutoRefreNum(int pvp3ShopAutoRefreNum) {
		this.pvp3ShopAutoRefreNum = pvp3ShopAutoRefreNum;
	}

	public int getPvp3ShopBuyNum() {
		return pvp3ShopBuyNum;
	}

	public void setPvp3ShopBuyNum(int pvp3ShopBuyNum) {
		this.pvp3ShopBuyNum = pvp3ShopBuyNum;
	}

	public Timestamp getPvp3ShopLastBuyTime() {
		return pvp3ShopLastBuyTime;
	}

	public void setPvp3ShopLastBuyTime(Timestamp pvp3ShopLastBuyTime) {
		this.pvp3ShopLastBuyTime = pvp3ShopLastBuyTime;
	}

	public long getPvp3Money() {
		return pvp3Money;
	}

	public void setPvp3Money(long pvp3Money) {
		this.pvp3Money = pvp3Money;
	}

	public Timestamp getRefreshGoldShopTime() {
		return RefreshGoldShopTime;
	}

	public void setRefreshGoldShopTime(Timestamp refreshGoldShopTime) {
		RefreshGoldShopTime = refreshGoldShopTime;
	}

	public Timestamp getRefreshTurkShopTime() {
		return RefreshTurkShopTime;
	}

	public void setRefreshTurkShopTime(Timestamp refreshTurkShopTime) {
		RefreshTurkShopTime = refreshTurkShopTime;
	}
	public int getIsBuyGoldShopForVip() {
		return isBuyGoldShopForVip;
	}

	public void setIsBuyGoldShopForVip(int isBuyGoldShopForVip) {
		this.isBuyGoldShopForVip = isBuyGoldShopForVip;
	}

	public int getIsBuyTurkShopForVip() {
		return isBuyTurkShopForVip;
	}

	public void setIsBuyTurkShopForVip(int isBuyTurkShopForVip) {
		this.isBuyTurkShopForVip = isBuyTurkShopForVip;
	}

	public short getSoldierFreeUpNum() {
		return soldierFreeUpNum;
	}

	public void setSoldierFreeUpNum(short soldierFreeUpNum) {
		this.soldierFreeUpNum = soldierFreeUpNum;
	}

	public Timestamp getSoldierFreeUpTime() {
		if(soldierFreeUpTime == null)
		{
			return new Timestamp(0);
		}
		return soldierFreeUpTime;
	}

	public void setSoldierFreeUpTime(Timestamp soldierFreeUpTime) {
		this.soldierFreeUpTime = soldierFreeUpTime;
	}

	public String getWonderAwardStr() {
		return wonderAwardStr;
	}

	public void setWonderAwardStr(String wonderAwardStr) {
		this.wonderAwardStr = wonderAwardStr;
	}

	public int getIsBuyPlan() {
		return isBuyPlan;
	}

	public void setIsBuyPlan(int isBuyPlan) {
		this.isBuyPlan = isBuyPlan;
	}
	
	public byte getRefBiaoCheNum() {
		return refBiaoCheNum;
	}

	public void setRefBiaoCheNum(byte refBiaoCheNum) {
		this.refBiaoCheNum = refBiaoCheNum;
	}

	/**
	 * 押镖人ID
	 * @return
	 */
	public int getYabiaoFriendRoleId() {
		return yabiaoFriendRoleId;
	}

	/**
	 * 押镖人ID
	 * @param yabiaoFriendRoleId
	 */
	public void setYabiaoFriendRoleId(int yabiaoFriendRoleId) {
		this.yabiaoFriendRoleId = yabiaoFriendRoleId;
	}

	public byte getTodayYabiaoNum() {
		return todayYabiaoNum;
	}

	public void setTodayYabiaoNum(byte todayYabiaoNum) {
		this.todayYabiaoNum = todayYabiaoNum;
	}

	public byte getTodayJiebiaoNum() {
		return todayJiebiaoNum;
	}

	public void setTodayJiebiaoNum(byte todayJiebiaoNum) {
		this.todayJiebiaoNum = todayJiebiaoNum;
	}

	public byte getBiaocheType() {
		if (biaocheType == 0) {
			// 初始镖车类型为1
			biaocheType = 1;
		}
		return biaocheType;
	}

	public void setBiaocheType(byte biaocheType) {
		this.biaocheType = biaocheType;
	}

	public byte getHubiaoNum() {
		return hubiaoNum;
	}

	public void setHubiaoNum(byte hubiaoNum) {
		this.hubiaoNum = hubiaoNum;
	}

	public HashMap<Integer, Long> getRejectHelpMap() {
		return rejectHelpMap;
	}

	public HashMap<Integer, Long> getRejectHelpMap(boolean clear) {
		if (rejectHelpMap == null) {
			rejectHelpMap = new HashMap<Integer, Long>();
		}

		// 过期的去除
		if (clear && rejectHelpMap.size() > 0) {
			Iterator<Long> iter = rejectHelpMap.values().iterator();
			while (iter.hasNext()) {
				long time = iter.next();
				if (System.currentTimeMillis() - time > GameValue.PER_HELP_HU_BIAO_TIME * 1000) {
					iter.remove();
				}
			}
		}
		return rejectHelpMap;
	}

	public void setRejectHelpMap(HashMap<Integer, Long> rejectHelpMap) {
		this.rejectHelpMap = rejectHelpMap;
	}
	public byte getThisbiaocheTypeJieBiaoNum() {
		return thisbiaocheTypeJieBiaoNum;
	}

	public void setThisbiaocheTypeJieBiaoNum(byte thisbiaocheTypeJieBiaoNum) {
		this.thisbiaocheTypeJieBiaoNum = thisbiaocheTypeJieBiaoNum;
	}

	public boolean isNotice() {
		return isNotice;
	}

	public void setNotice(boolean isNotice) {
		this.isNotice = isNotice;
	}

	public String getDrawVipLvBuyStr() {
		return drawVipLvBuyStr;
	}

	public void setDrawVipLvBuyStr(String drawVipLvBuyStr) {
		this.drawVipLvBuyStr = drawVipLvBuyStr;
	}

	public Timestamp getLastBuyVipLvTime() {
		return lastBuyVipLvTime;
	}

	public void setLastBuyVipLvTime(Timestamp lastBuyVipLvTime) {
		this.lastBuyVipLvTime = lastBuyVipLvTime;
	}

	public int getOneRecruitCoinOpTimes() {
		return oneRecruitCoinOpTimes;
	}

	public void setOneRecruitCoinOpTimes(int oneRecruitCoinOpTimes) {
		this.oneRecruitCoinOpTimes = oneRecruitCoinOpTimes;
	}

	public Set<Integer> getClubDisplaySet() {
		return clubDisplaySet;
	}

	public void setClubDisplaySet(Set<Integer> clubDisplaySet) {
		this.clubDisplaySet = clubDisplaySet;
	}

	public int getJiebiaoRoleId() {
		return jiebiaoRoleId;
	}

	public void setJiebiaoRoleId(int jiebiaoRoleId) {
		this.jiebiaoRoleId = jiebiaoRoleId;
	}

	public int getHubiaoRoleId() {
		return hubiaoRoleId;
	}

	public void setHubiaoRoleId(int hubiaoRoleId) {
		this.hubiaoRoleId = hubiaoRoleId;
	}
	

	public int getOneRecruitHeroNum() {
		return oneRecruitHeroNum;
	}

	public void setOneRecruitHeroNum(int oneRecruitHeroNum) {
		this.oneRecruitHeroNum = oneRecruitHeroNum;
	}

	public Timestamp getLastRecruitHeroFreeTime() {
		return lastRecruitHeroFreeTime;
	}

	public void setLastRecruitHeroFreeTime(Timestamp lastRecruitHeroFreeTime) {
		this.lastRecruitHeroFreeTime = lastRecruitHeroFreeTime;
	}

	public int getTenRecruitHeroNum() {
		return tenRecruitHeroNum;
	}

	public void setTenRecruitHeroNum(int tenRecruitHeroNum) {
		this.tenRecruitHeroNum = tenRecruitHeroNum;
	}

	public byte getGetPresentEnergyTimes() {
		return getPresentEnergyTimes;
	}

	public void setGetPresentEnergyTimes(byte getPresentEnergyTimes) {
		this.getPresentEnergyTimes = getPresentEnergyTimes;
	}

	public Timestamp getQueryEnergyInfoTime() {
		return queryEnergyInfoTime;
	}

	public void setQueryEnergyInfoTime(Timestamp queryEnergyInfoTime) {
		this.queryEnergyInfoTime = queryEnergyInfoTime;
	}

	public Timestamp getBiaocheQueryTime() {
		return biaocheQueryTime;
	}

	public void setBiaocheQueryTime(Timestamp biaocheQueryTime) {
		this.biaocheQueryTime = biaocheQueryTime;
	}

	public String getVipCheckInStr() {
		return vipCheckInStr;
	}

	public void setVipCheckInStr(String vipCheckInStr) {
		this.vipCheckInStr = vipCheckInStr;
	}

	public byte getAttackAnotherType() {
		return attackAnotherType;
	}

	public void setAttackAnotherType(byte attackAnotherType) {
		this.attackAnotherType = attackAnotherType;
	}

	public int getMineNum() {
		return mineNum;
	}
	
	public int getTodayMineNum() {
		if (lastMineTime != null && DateUtil.isSameDay(System.currentTimeMillis(), lastMineTime.getTime())) {
			return mineNum;
		}
		return 0;
	}

	public void setMineNum(int mineNum) {
		this.mineNum = mineNum;
	}

	public Timestamp getLastMineTime() {
		return lastMineTime;
	}

	public void setLastMineTime(Timestamp lastMineTime) {
		this.lastMineTime = lastMineTime;
	}

	public int getBuyMine() {
		return buyMine;
	}
	
	public int getTodayBuyMine() {
		if (lastBuyMineTime != null && DateUtil.isSameDay(System.currentTimeMillis(), lastBuyMineTime.getTime())) {
			return buyMine;
		}
		return 0;
	}

	public void setBuyMine(int buyMine) {
		this.buyMine = buyMine;
	}

	public Timestamp getLastBuyMineTime() {
		return lastBuyMineTime;
	}

	public void setLastBuyMineTime(Timestamp lastBuyMineTime) {
		this.lastBuyMineTime = lastBuyMineTime;
	}

	public int getStarMoney() {
		return starMoney;
	}

	public void setStarMoney(int starMoney) {
		this.starMoney = starMoney;
	}

	public Timestamp getStarShopAutoTime() {
		return starShopAutoTime;
	}

	public void setStarShopAutoTime(Timestamp starShopAutoTime) {
		this.starShopAutoTime = starShopAutoTime;
	}

	public int getBuyStarShopNum() {
		return buyStarShopNum;
	}

	public void setBuyStarShopNum(int buyStarShopNum) {
		this.buyStarShopNum = buyStarShopNum;
	}

	public Timestamp getLastBuyStarTime() {
		return lastBuyStarTime;
	}

	public void setLastBuyStarTime(Timestamp lastBuyStarTime) {
		this.lastBuyStarTime = lastBuyStarTime;
	}

	public int getAutoRefreStarShopNum() {
		return autoRefreStarShopNum;
	}

	public void setAutoRefreStarShopNum(int autoRefreStarShopNum) {
		this.autoRefreStarShopNum = autoRefreStarShopNum;
	}

	public String getNowAppellation() {
		return nowAppellation;
	}

	public void setNowAppellation(String nowAppellation) {
		this.nowAppellation = nowAppellation;
	}

	public String getAllAppellation() {
		return allAppellation;
	}

	public void setAllAppellation(String allAppellation) {
		this.allAppellation = allAppellation;
	}

	public List<Integer> getHaveReward() {
		return haveReward;
	}

	public void setHaveReward(List<Integer> haveReward) {
		this.haveReward = haveReward;
	}

	public String getAllExpActionMax() {
		return allExpActionMax;
	}

	public void setAllExpActionMax(String allExpActionMax) {
		this.allExpActionMax = allExpActionMax;
	}

	public List<Integer> getPassMapNpcNos() {
		return passMapNpcNos;
	}

	public void setPassMapNpcNos(List<Integer> passMapNpcNos) {
		this.passMapNpcNos = passMapNpcNos;
	}

	public int getCommentGameState() {
		return commentGameState;
	}

	public void setCommentGameState(int commentGameState) {
		this.commentGameState = commentGameState;
	}
	
	public byte getTeam3V3Times() {
		return team3V3Times;
	}

	public void setTeam3V3Times(byte team3v3Times) {
		team3V3Times = team3v3Times;
	}

	public Map<Integer, Integer> getTeamChallengeTimes() {
		return teamChallengeTimes;
	}

	public int getTeamChallengeTimesByDupId(int duplicateId){
		if(teamChallengeTimes == null){
			return 0;
		}
		Integer times = teamChallengeTimes.get(duplicateId);
		
		if(times == null){
			return 0;
		} else {
			return times;
		}
	}

	public String getTeamChallengeAllTimesStr(){
		String times = "";
		if(teamChallengeTimes != null){
			times = IntegerMapTypeHandler.getString(teamChallengeTimes);
		}
		return times;
	}
	
	public int getTeamChallengeAllTimes(){
		int times = 0;
		if(teamChallengeTimes != null){
			for(Integer time : teamChallengeTimes.values()){
				if(time == null){
					time = 0;
				}
				times += time;
			}
		}
		return times;
	}
	
	public void setTeamChallengeTimes(Map<Integer, Integer> teamChallengeTimes) {
		this.teamChallengeTimes = teamChallengeTimes;
	}

	public int getTeamMoney() {
		return teamMoney;
	}

	public void setTeamMoney(int teamMoney) {
		this.teamMoney = teamMoney;
	}

	public Timestamp getTeamShopAutoTime() {
		return teamShopAutoTime;
	}

	public void setTeamShopAutoTime(Timestamp teamShopAutoTime) {
		this.teamShopAutoTime = teamShopAutoTime;
	}

	public int getTeamShopAutoRefreNum() {
		return teamShopAutoRefreNum;
	}

	public void setTeamShopAutoRefreNum(int teamShopAutoRefreNum) {
		this.teamShopAutoRefreNum = teamShopAutoRefreNum;
	}

	public int getTeamShopBuyNum() {
		return teamShopBuyNum;
	}

	public void setTeamShopBuyNum(int teamShopBuyNum) {
		this.teamShopBuyNum = teamShopBuyNum;
	}

	public Timestamp getTeamShopLastBuyTime() {
		return teamShopLastBuyTime;
	}

	public void setTeamShopLastBuyTime(Timestamp teamShopLastBuyTime) {
		this.teamShopLastBuyTime = teamShopLastBuyTime;
	}

	public int getTodayActive() {
		return todayActive;
	}

	public void setTodayActive(int todayActive) {
		this.todayActive = todayActive;
	}

	public Timestamp getLastActiveChgTime() {
		return lastActiveChgTime;
	}

	public void setLastActiveChgTime(Timestamp lastActiveChgTime) {
		this.lastActiveChgTime = lastActiveChgTime;
	}

	public Map<Integer, RideInfo> getRoleRideMap() {
		return roleRideMap;
	}

	public void setRoleRideMap(Map<Integer, RideInfo> roleRideMap) {
		this.roleRideMap = roleRideMap;
	}
	
	
	public long getServerCheckTime() {
		return serverCheckTime;
	}

	public void setServerCheckTime(long serverCheckTime) {
		this.serverCheckTime = serverCheckTime;
	}

	public int getServerCheckTimeNum() {
		return serverCheckTimeNum;
	}

	public void setServerCheckTimeNum(int serverCheckTimeNum) {
		this.serverCheckTimeNum = serverCheckTimeNum;
	}

	/**
	 * 检测是否已有该坐骑
	 * 
	 * @param rideNo
	 * @return
	 */
	public boolean checkIsHasSameRideByNo(int rideNo) {
		if (roleRideMap.isEmpty()) {
			return false;
		}
		
		for (RideInfo rideInfo : roleRideMap.values()) {
			if (rideInfo.getRideNo() == rideNo) {
				return true;
			}
		}
		
		return false;
	}

	public long getTeamChallengeFightLastTime() {
		return teamChallengeFightLastTime;
	}

	public void setTeamChallengeFightLastTime(long teamChallengeFightLastTime) {
		this.teamChallengeFightLastTime = teamChallengeFightLastTime;
	}

	public long getTeam3V3FightLastTime() {
		return team3V3FightLastTime;
	}

	public void setTeam3V3FightLastTime(long team3v3FightLastTime) {
		team3V3FightLastTime = team3v3FightLastTime;
	}
	
	public Map<Integer, Map<Integer, HeroImageInfo>> getHeroImageMap() {
		return heroImageMap;
	}
	
	public Map<Integer, HeroImageInfo> getHeroImageMapbyFightType(int fightType) {
		return heroImageMap.get(fightType);
	}
	
	public Map<Integer, HeroImageInfo> getHeroImageMapbyFightType(FightType fightType) {
		return heroImageMap.get(fightType.getValue());
	}
	
	public HeroImageInfo getHeroImageInfo(int fightType,int heroId){
		Map<Integer, HeroImageInfo> val = getHeroImageMapbyFightType(fightType);
		if(val != null){
			return val.get(heroId);
		}
		return null;
	}
	
	public void removeHeroImageInfo(FightType fightType,int heroId){
		Map<Integer, HeroImageInfo> val = getHeroImageMapbyFightType(fightType);
		if(val != null){
			val.remove(heroId);
		}
	}
	
	public HeroImageInfo getHeroImageInfo(FightType fightType,int heroId){
		Map<Integer, HeroImageInfo> val = getHeroImageMapbyFightType(fightType);
		if(val != null){
			return val.get(heroId);
		}
		return null;
	}
	
	public void addHeroImageInfo(HeroImageInfo heroImage){
		Map<Integer, HeroImageInfo> val = heroImageMap.get(heroImage.getHireType());
		if(val == null){
			val = new HashMap<Integer, HeroImageInfo>();
			heroImageMap.put(heroImage.getHireType(), val);
		}
		val.put(heroImage.getHeroId(), heroImage);
	}

	public void setHeroImageMap(Map<Integer, Map<Integer, HeroImageInfo>> heroImageMap) {
		this.heroImageMap = heroImageMap;
	}

	public String getHireInfos() {
		return hireInfos;
	}

	public void setHireInfos(String hireInfos) {
		this.hireInfos = hireInfos;
	}

	public String getTeamChallengeStr() {
		return teamChallengeStr;
	}

	public void setTeamChallengeStr(String teamChallengeStr) {
		this.teamChallengeStr = teamChallengeStr;
	}

	public int getTeamDuplicateId() {
		return teamDuplicateId;
	}

	public void setTeamDuplicateId(int teamDuplicateId) {
		this.teamDuplicateId = teamDuplicateId;
	}
	
}
