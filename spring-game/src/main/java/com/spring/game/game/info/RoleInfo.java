package com.snail.webgame.game.info;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import com.snail.webgame.engine.common.to.BaseTO;
import com.snail.webgame.game.cache.OpActivityProgressMap;
import com.snail.webgame.game.cache.QuestInfoMap;
import com.snail.webgame.game.common.GameValue;
import com.snail.webgame.game.common.xml.cache.VipXMLInfoMap;
import com.snail.webgame.game.common.xml.info.VipType;
import com.snail.webgame.game.info.log.MineFightLog;
import com.snail.webgame.game.protocal.challenge.queryBattleDetail.BattleDetailRe;
import com.snail.webgame.game.protocal.fight.competition.info.CompetitionAgainstInfo;
import com.snail.webgame.game.protocal.rank.service.RankInfo;
import com.snail.webgame.game.protocal.scene.info.RolePoint;
import com.snail.webgame.game.xml.info.GoldBuyXMLInfo;

/**
 * 用户信息
 */
public class RoleInfo extends BaseTO {

	private String account;// 帐号
	private String roleName;// 角色名称
	private String uid;// 全服唯一标识符
	private String voiceUid; // 语音UID
	private byte roleRace;// 从属势力
	private int fightValue;// 战斗力
	private int fightMaxValue;// 历史最高战斗力
	
	private long money;// 银子
	private long coin;// 金子（充值）
	private long totalCoin;// 累计充值获得的金子
	private long totalCharge;// 累计的充值
	private int vipLv;// 当前的vip等级
	private int vipExp;// vip经验
	
	private byte cardType;//1-月卡,2-季卡,3-年卡
	private Timestamp cardEndTime;// 会员卡到期时间
	private Timestamp fuliCardEndTime; // 福利卡到期时间
	private String firstChargeSaleNoStr;// 记录首次购买金子时已买一送一 格式：商品id,商品id
	
	private short sp;// 体力
	private Timestamp lastRecoverSPTime;// 最后自动回复时间
	private short energy; // 精力
	private Timestamp lastRecoverEnergyTime;// 最后自动回复时间
	private short tech;// 技能点
	private Timestamp lastRecoverTechTime;// 上次技能点变化时间
	
	private Timestamp createTime; // 创建时间
	private Timestamp loginTime; // 最后登录时间
	private Timestamp logoutTime; // 最后退出时间
	private String loginIp; // 最后登录IP
	private int clientType;// 客户端类型 1-安卓 2-ios
	private String mac;// 客户端mac地址
	private String packageName;// 客户端包名
	
	private byte roleStatus = 0;// 角色状态 0-正常 1- 角色被冻结（时间限制） 2-角色永久冻结
	private Timestamp roleStatusTime; // 异常状态结束时间
	
	private byte punishStatus = 0;// 角色禁言状态: 0-正常 1-禁言 2-永久禁言
	private Timestamp punishTime;// 结束时间
	
	private byte gmRight; // GM权限
	private byte changeRoleNameTimes; // 用户修改名称次数
	private long safeModeEndTime;// 夺宝安全模式结束时间
	private byte rankShow;// 排行榜显示字段 1-不显示 其他-显示
	private byte isAdvert;// 是否代言人 1-是 其他-不是
	
	/**
	 * 活动 经验，银币字段
	 */
	private byte expLeftTimes1; // 经验活动剩余次数
	private byte expLeftTimes2; // 经验活动剩余次数
	private byte expLeftTimes3; // 经验活动剩余次数
	private byte expLeftTimes4; // 经验活动剩余次数
	private byte expLeftTimes5; // 经验活动剩余次数
	private byte expLeftTimes6; // 经验活动剩余次数
	private byte moneyLeftTimes; // 银币活动剩余次数
	
	/**
	 * 竞技场字段
	 */
	private int competitionValue;// 竞技场积分
	private byte competitionStage;// 竞技场段位
	private long competitionTime;// 竞技场积分时间 用于排行
	private byte torrentTimes;// 连续胜利/失败次数 正值为胜利次数 负值为失败次数
	private int stageAward; // 段位奖励可领取(二进制表示) 1-可领取
	
	private int scoreValue; // 对攻战积分
	private RolePoint rolePoint;// 角色在场景中的位置
	private Timestamp mapPvpFightTime;// 大地图上遭遇拦截失败时间
	
	// 排行榜缓存
	private int rankLevel; // 等级排行
	private int rankFightValue; // 战斗力排行
	private int rankHeroNum; // 英雄数量排行
	
	private RankInfo levelRankInfo;	//等级用RankInfo
	private RankInfo heroNumRankInfo;	//英雄数量用RankInfo
	private RankInfo fightValueRankInfo;	//战斗力用RankInfo
	
	// 对战历史记录集合
	private List<CompetitionAgainstInfo> competitionAgainstInfoList = null;
	
	// 神兵<seqId, RoleWeaponInfo>（只包含已上阵的）（战斗数据异步操作）
	private Map<Integer, RoleWeaponInfo> roleWeaponInfoPositionMap;
	private int WeaponFlag;//是否加载神兵
	
	// 英雄Map（战斗数据异步操作）
	private Map<Integer, HeroInfo> heroMap;
	
	// 所有羁绊武将No（战斗数据异步操作）
	private Map<Byte, Integer> jbHeroNoMap;
	
	// 当前上阵坐骑
	private RideInfo rideInfo;
	
	// 所有已开启副本（缓存）
	private List<BattleDetailRe> battleList;
	
	// 新开启的副本(缓存)
	private List<String> newBattleList;
	
	// 运营活动相关
	private OpActivityProgressMap opActProMap = new OpActivityProgressMap();// 运营活动缓存
	
	// ==任务
	private QuestInfoMap questInfoMap = new QuestInfoMap();// 角色任务缓存
	
	// 玩家礼包购买记录(包含充值宝箱及运营金币礼包)
	private Map<Integer, RoleBoxRecordInfo> roleBoxMap;
	
	// ==兵种等级
	private String soldierInfo; // 士兵等级信息（1，1，1，1，1）
	
	// 上线加载对象
	private RoleLoadInfo roleLoadInfo;
	
	/**
	 * 缓存字段
	 */
	private short gateServerId; // 接入服务器ID
	private String loginLogId = "";// 登录日志Id
	private byte loginStatus = 0;// 登录状态，0-不在线 1-在线
	private byte serverStatus = 0;// 服务器状态，0-未登录数据未初始化 1-登录数据初始化过
	private long arenaLogCheckTime = 0;// 上次查看竞技场日志时间
	private long friendRecommendTime;
	
	/**
	 * 计费服务器所需缓存字段
	 */
	private String md5Pass;// 玩家登陆密码
	private String validate;// 角色验证串
	private int accountId;// 角色账号Id
	private int serverId; // 服务器ID
	private String loginId;// 角色登陆Id
	private String accInfo;// 角色防沉迷信息
	private int issuerID;// 运营商id
	private String hmacStr;// 一次性串
	private String qihooUserId;// 360 userId
	private String qihooToken;// 360、酷派 token
	
	private byte disconnectPhase; // 1-临时断开,2-临时断开重连成功 ,3-临时断开超时,4-断开
	
	private volatile AtomicBoolean clubLock = new AtomicBoolean(false); //原子锁
	private int clubContribution; // 公会贡献值
	private long clubContributionSum; // 角色累积的公会贡献值
	
	private int commHeroNum; //普通武将数量
	private long fightWorldBossHp;	//对世界boss造成的总伤害。每日清0 -世界boss开始时
	private Timestamp lastWorldBossFightTime;	//最后一次攻打世界BOSS时间 
	private long bestFightBossHp;	//对世界boss造成的最高伤害。 结算时更新
	private long thisBossBest; //本场BOSS最高伤害值
	private int clubId; //公会Id, 大于0的说明有加入
	private Timestamp clubBuildTime; //公会建设时间
	private long totalBuild; //总的公会建设度
	
	// 矿防守记录(缓存)
	private Timestamp lastLookLogTime;//最后查看防守记录时间（缓存）
	private List<MineFightLog> mineLogs = new ArrayList<MineFightLog>();
	
	private String offlinePushStr;// 记录的是离线不推送字符串
	
	//膜拜模块字段
	private int worshipCount;//今天膜拜次数
	private int beWorshipNum;//被膜拜的人数
	private volatile AtomicBoolean worShipLock = new AtomicBoolean(false); //膜拜原子锁
	
	private boolean fightEndClearBiaoche;// 如果登陆后，清除镖车关系，是要放在战斗结束那边调用的 
	
	private String clubTechPlusInfo; //公会科技关联角色属性升级buildType:lv;buildType:lv
	private int isCanAddFlag; //角色是否允许被添加
	private Map<Integer, HireHeroInfo> hireHeroMap; //公会上阵的兵，不可参加个人战斗 key - heroId 同heroMap
	
	private int isShowShizhuang;//1-显示套装  0-显示时装
	// 玩家锁定的时装 <equipType,equipId>
	private Map<Integer,Integer> lockShizhuang = new HashMap<Integer, Integer>();	
	// 玩家锁定的背包时装<equipType,EquipInfo>（缓存）
	private Map<Integer, EquipInfo> lockShizhuangMap = new HashMap<Integer, EquipInfo>();
	private boolean titleRepair; //补称号刷新的 用于判断是否补过，补过就无需再次刷新，不记库
	
	private int todayPlusWorshipNum;//今天已增加的膜拜数量
	
	private boolean sendGmccFlag; //是否发送GMCC false-未发送 true-发送

	

	public boolean isSendGmccFlag() {
		return sendGmccFlag;
	}

	public void setSendGmccFlag(boolean sendGmccFlag) {
		this.sendGmccFlag = sendGmccFlag;
	}

	public int getIsShowShizhuang() {
		return isShowShizhuang;
	}

	public void setIsShowShizhuang(int isShowShizhuang) {
		this.isShowShizhuang = isShowShizhuang;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public byte getRoleRace() {
		return roleRace;
	}

	public void setRoleRace(byte roleRace) {
		this.roleRace = roleRace;
	}

	public int getFightValue() {
		return fightValue;
	}

	public void setFightValue(int fightValue) {
		this.fightValue = fightValue;
		
		if(fightMaxValue < fightValue){
			fightMaxValue = fightValue;
		}
	}

	public int getFightMaxValue() {
		return fightMaxValue;
	}

	public void setFightMaxValue(int fightMaxValue) {
		this.fightMaxValue = fightMaxValue;
	}

	public long getMoney() {
		return money;
	}

	public void setMoney(long money) {
		this.money = money;
	}

	public long getCoin() {
		return coin;
	}

	public void setCoin(long coin) {
		this.coin = coin;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public Timestamp getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(Timestamp loginTime) {
		this.loginTime = loginTime;
	}

	public Timestamp getLogoutTime() {
		return logoutTime;
	}

	public void setLogoutTime(Timestamp logoutTime) {
		this.logoutTime = logoutTime;
	}

	public String getLoginIp() {
		return loginIp;
	}

	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}

	public byte getRoleStatus() {
		return roleStatus;
	}

	public void setRoleStatus(byte roleStatus) {
		this.roleStatus = roleStatus;
	}

	public Timestamp getRoleStatusTime() {
		return roleStatusTime;
	}

	public void setRoleStatusTime(Timestamp roleStatusTime) {
		this.roleStatusTime = roleStatusTime;
	}

	public int getCurrRoleStatus() {
		if (roleStatus == 1 && roleStatusTime != null && roleStatusTime.getTime() <= System.currentTimeMillis()) {
			return 0;
		}
		return roleStatus;
	}

	public long getCurrRoleStatusTime() {
		if (getCurrRoleStatus() == 1) {
			return roleStatusTime.getTime();
		}
		return 0;
	}
	
	/**
	 * 屏蔽排行榜冻结账号不显示
	 * 
	 * @param isRef 是否刷新冻结账号排名
	 * @return
	 */
	public boolean isShowRank(boolean isRef) {
		if (getCurrRoleStatus() != 0 || this.rankShow == 1) {
			if (isRef) {
				this.rankLevel = 0;
				this.rankFightValue = 0;
				this.rankHeroNum = 0;
			}
			return false;
		}
		
		return true;
	}

	public byte getPunishStatus() {
		return punishStatus;
	}

	public void setPunishStatus(byte punishStatus) {
		this.punishStatus = punishStatus;
	}

	public Timestamp getPunishTime() {
		if (punishTime == null) {
			return new Timestamp(0);
		}
		return punishTime;
	}

	public void setPunishTime(Timestamp punishTime) {
		this.punishTime = punishTime;
	}

	public long getCurrPunishTime() {
		if (punishTime != null) {
			return punishTime.getTime();
		}
		return 0;
	}

	public short getGateServerId() {
		return gateServerId;
	}

	public void setGateServerId(short gateServerId) {
		this.gateServerId = gateServerId;
	}

	public String getLoginLogId() {
		return loginLogId;
	}

	public void setLoginLogId(String loginLogId) {
		this.loginLogId = loginLogId;
	}

	public byte getLoginStatus() {
		return loginStatus;
	}

	public void setLoginStatus(byte loginStatus) {
		this.loginStatus = loginStatus;
	}

	public byte getServerStatus() {
		return serverStatus;
	}

	public void setServerStatus(byte serverStatus) {
		this.serverStatus = serverStatus;
	}

	public RolePoint getRolePoint() {
		return rolePoint;
	}

	public void setRolePoint(RolePoint rolePoint) {
		this.rolePoint = rolePoint;
	}

	public int getCompetitionValue() {
		return competitionValue;
	}

	public void setCompetitionValue(int competitionValue) {
		this.competitionValue = competitionValue;
	}

	public byte getCompetitionStage() {
		return competitionStage;
	}

	public void setCompetitionStage(byte competitionStage) {
		this.competitionStage = competitionStage;
	}

	public long getCompetitionTime() {
		return competitionTime;
	}

	public void setCompetitionTime(long competitionTime) {
		this.competitionTime = competitionTime;
	}

	public List<CompetitionAgainstInfo> getCompetitionAgainstInfoList() {
		return competitionAgainstInfoList;
	}

	public void setCompetitionAgainstInfoList(List<CompetitionAgainstInfo> competitionAgainstInfoList) {
		this.competitionAgainstInfoList = competitionAgainstInfoList;
	}

	public byte getTorrentTimes() {
		return torrentTimes;
	}

	public void setTorrentTimes(byte torrentTimes) {
		this.torrentTimes = torrentTimes;
	}

	public int getStageAward() {
		return stageAward;
	}

	public void setStageAward(int stageAward) {
		this.stageAward = stageAward;
	}

	public Timestamp getMapPvpFightTime() {
		if (mapPvpFightTime == null) {
			return new Timestamp(0);
		}
		return mapPvpFightTime;
	}

	public void setMapPvpFightTime(Timestamp mapPvpFightTime) {
		this.mapPvpFightTime = mapPvpFightTime;
	}

	public byte getGmRight() {
		return gmRight;
	}

	public void setGmRight(byte gmRight) {
		this.gmRight = gmRight;
	}

	@Override
	public byte getSaveMode() {
		return ONLINE;
	}
	
	public byte getExpLeftTimes1() {
		return expLeftTimes1;
	}

	public void setExpLeftTimes1(byte expLeftTimes1) {
		this.expLeftTimes1 = expLeftTimes1;
	}

	public byte getExpLeftTimes2() {
		return expLeftTimes2;
	}

	public void setExpLeftTimes2(byte expLeftTimes2) {
		this.expLeftTimes2 = expLeftTimes2;
	}

	public byte getExpLeftTimes3() {
		return expLeftTimes3;
	}

	public void setExpLeftTimes3(byte expLeftTimes3) {
		this.expLeftTimes3 = expLeftTimes3;
	}

	public byte getExpLeftTimes4() {
		return expLeftTimes4;
	}

	public void setExpLeftTimes4(byte expLeftTimes4) {
		this.expLeftTimes4 = expLeftTimes4;
	}

	public byte getExpLeftTimes5() {
		return expLeftTimes5;
	}

	public void setExpLeftTimes5(byte expLeftTimes5) {
		this.expLeftTimes5 = expLeftTimes5;
	}

	public byte getExpLeftTimes6() {
		return expLeftTimes6;
	}

	public void setExpLeftTimes6(byte expLeftTimes6) {
		this.expLeftTimes6 = expLeftTimes6;
	}

	public byte getMoneyLeftTimes() {
		return moneyLeftTimes;
	}

	public void setMoneyLeftTimes(byte moneyLeftTimes) {
		this.moneyLeftTimes = moneyLeftTimes;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getVoiceUid() {
		return voiceUid;
	}

	public void setVoiceUid(String voiceUid) {
		this.voiceUid = voiceUid;
	}

	public int getRankLevel() {
		return rankLevel;
	}

	public void setRankLevel(int rankLevel) {
		this.rankLevel = rankLevel;
	}

	public int getRankFightValue() {
		return rankFightValue;
	}

	public void setRankFightValue(int rankFightValue) {
		this.rankFightValue = rankFightValue;
	}

	public int getRankHeroNum() {
		return rankHeroNum;
	}

	public void setRankHeroNum(int rankHeroNum) {
		this.rankHeroNum = rankHeroNum;
	}

	public long getArenaLogCheckTime() {
		return arenaLogCheckTime;
	}

	public void setArenaLogCheckTime(long arenaLogCheckTime) {
		this.arenaLogCheckTime = arenaLogCheckTime;
	}

	public byte getChangeRoleNameTimes() {
		return changeRoleNameTimes;
	}

	public void setChangeRoleNameTimes(byte changeRoleNameTimes) {
		this.changeRoleNameTimes = changeRoleNameTimes;
	}

	public Map<Integer, HeroInfo> getHeroMap() {
		if (heroMap == null) {
			heroMap = new ConcurrentHashMap<Integer, HeroInfo>();
		}
		return heroMap;
	}

	public void setHeroMap(Map<Integer, HeroInfo> heroMap) {
		this.heroMap = heroMap;
	}

	public RoleLoadInfo getRoleLoadInfo() {
		return roleLoadInfo;
	}

	public void setRoleLoadInfo(RoleLoadInfo roleLoadInfo) {
		this.roleLoadInfo = roleLoadInfo;
	}

	public Timestamp getLastRecoverSPTime() {
		if (lastRecoverSPTime == null) {
			return new Timestamp(0);
		}
		return lastRecoverSPTime;
	}

	public void setLastRecoverSPTime(Timestamp lastRecoverSPTime) {
		this.lastRecoverSPTime = lastRecoverSPTime;
	}

	public short getSp() {
		return sp;
	}

	public void setSp(short sp) {
		this.sp = sp;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getMd5Pass() {
		return md5Pass;
	}

	public void setMd5Pass(String md5Pass) {
		this.md5Pass = md5Pass;
	}

	public String getValidate() {
		return validate;
	}

	public void setValidate(String validate) {
		this.validate = validate;
	}

	public int getClientType() {
		return clientType;
	}

	public void setClientType(int clientType) {
		this.clientType = clientType;
	}

	public int getAccountId() {
		return accountId;
	}

	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}

	public int getServerId() {
		return serverId;
	}

	public void setServerId(int serverId) {
		this.serverId = serverId;
	}

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public String getAccInfo() {
		return accInfo;
	}

	public void setAccInfo(String accInfo) {
		this.accInfo = accInfo;
	}

	public int getIssuerID() {
		return issuerID;
	}

	public void setIssuerID(int issuerID) {
		this.issuerID = issuerID;
	}

	public String getHmacStr() {
		return hmacStr;
	}

	public void setHmacStr(String hmacStr) {
		this.hmacStr = hmacStr;
	}

	public Map<Integer, RoleWeaponInfo> getRoleWeaponInfoPositionMap() {
		if (roleWeaponInfoPositionMap == null) {
			roleWeaponInfoPositionMap = new HashMap<Integer, RoleWeaponInfo>();
		}
		return roleWeaponInfoPositionMap;
	}

	public void setRoleWeaponInfoPositionMap(Map<Integer, RoleWeaponInfo> roleWeaponInfoPositionMap) {
		this.roleWeaponInfoPositionMap = roleWeaponInfoPositionMap;
	}

	public short getEnergy() {
		return energy;
	}

	public void setEnergy(short energy) {
		this.energy = energy;
	}

	public Timestamp getLastRecoverEnergyTime() {
		if (lastRecoverEnergyTime == null) {
			return new Timestamp(0);
		}
		return lastRecoverEnergyTime;
	}

	public void setLastRecoverEnergyTime(Timestamp lastRecoverEnergyTime) {
		this.lastRecoverEnergyTime = lastRecoverEnergyTime;
	}

	public long getSafeModeEndTime() {
		return safeModeEndTime;
	}

	public void setSafeModeEndTime(long safeModeEndTime) {
		this.safeModeEndTime = safeModeEndTime;
	}

	public int getScoreValue() {
		return scoreValue;
	}

	public void setScoreValue(int scoreValue) {
		this.scoreValue = scoreValue;
	}

	public Map<Byte, Integer> getJbHeroNoMap() {
		if (jbHeroNoMap == null) {
			jbHeroNoMap = new HashMap<Byte, Integer>();
		}
		return jbHeroNoMap;
	}

	public void setJbHeroNoMap(Map<Byte, Integer> jbHeroNoMap) {
		this.jbHeroNoMap = jbHeroNoMap;
	}

	public byte getDisconnectPhase() {
		return disconnectPhase;
	}

	public void setDisconnectPhase(byte disconnectPhase) {
		this.disconnectPhase = disconnectPhase;
	}

	public List<BattleDetailRe> getBattleList() {
		if (battleList == null) {
			battleList = new ArrayList<BattleDetailRe>();
		}
		return battleList;
	}

	public void setBattleList(List<BattleDetailRe> battleList) {
		this.battleList = battleList;
	}

	public List<String> getNewBattleList() {
		if (newBattleList == null) {
			newBattleList = new ArrayList<String>();
		}
		return newBattleList;
	}

	public void setNewBattleList(List<String> newBattleList) {
		this.newBattleList = newBattleList;
	}

	public long getFriendRecommendTime() {
		return friendRecommendTime;
	}

	public void setFriendRecommendTime(long friendRecommendTime) {
		this.friendRecommendTime = friendRecommendTime;
	}

	public short getTech() {
		return tech;
	}

	public void setTech(short tech) {
		this.tech = tech;
	}

	public Timestamp getLastRecoverTechTime() {
		if (lastRecoverTechTime == null) {
			return new Timestamp(0);
		}
		return lastRecoverTechTime;
	}

	public void setLastRecoverTechTime(Timestamp lastRecoverTechTime) {
		this.lastRecoverTechTime = lastRecoverTechTime;
	}

	public long getTotalCharge() {
		return totalCharge;
	}

	public void setTotalCharge(long totalCharge) {
		this.totalCharge = totalCharge;
	}

	public int getVipLv() {
		return vipLv;
	}

	public void setVipLv(int vipLv) {
		this.vipLv = vipLv;
	}

	public byte getCardType() {
		return cardType;
	}

	public void setCardType(byte cardType) {
		this.cardType = cardType;
	}

	public Timestamp getCardEndTime() {
		return cardEndTime;
	}

	public void setCardEndTime(Timestamp cardEndTime) {
		this.cardEndTime = cardEndTime;
	}

	public Timestamp getFuliCardEndTime() {
		return fuliCardEndTime;
	}

	public void setFuliCardEndTime(Timestamp fuliCardEndTime) {
		this.fuliCardEndTime = fuliCardEndTime;
	}

	public String getFirstChargeSaleNoStr() {
		return firstChargeSaleNoStr;
	}

	public void setFirstChargeSaleNoStr(String firstChargeSaleNoStr) {
		this.firstChargeSaleNoStr = firstChargeSaleNoStr;
	}

	public long getTotalCoin() {
		return totalCoin;
	}

	public void setTotalCoin(long totalCoin) {
		this.totalCoin = totalCoin;
	}

	public int getClubContribution() {
		return clubContribution;
	}

	public void setClubContribution(int clubContribution) {
		this.clubContribution = clubContribution;
	}

	public AtomicBoolean getClubLock() {
		return clubLock;
	}

	public void setClubLock(AtomicBoolean clubLock) {
		this.clubLock = clubLock;
	}

	public int getCommHeroNum() {
		return commHeroNum;
	}

	public void setCommHeroNum(int commHeroNum) {
		this.commHeroNum = commHeroNum;
	}

	/**
	 * 获取背包上限
	 * @return
	 */
	public int getBagLimit() {
		Integer val = VipXMLInfoMap.getVipVal(vipLv, VipType.BAG_LIMIT);
		if (val != null) {
			return val;
		}
		return 0;
	}

	/**
	 * 获取体力回复上限
	 * @return
	 */
	public int getSpRecoverLimit() {
		Integer val = VipXMLInfoMap.getVipVal(vipLv, VipType.SP_LIMIT);
		if (val != null) {
			return val;
		}
		return 0;
	}

	/**
	 * 获取精力回复上限
	 * @return
	 */
	public int getEnergyRecoverLimit() {
		Integer val = VipXMLInfoMap.getVipVal(vipLv, VipType.ENERGY_LIMIT);
		if (val != null) {
			return val;
		}
		return 0;
	}

	/**
	 * 获取技能点回复上限
	 * @return
	 */
	public int getTechRecoverLimit() {
		Integer val = VipXMLInfoMap.getVipVal(vipLv, VipType.TECH_NUM);
		if (val != null) {
			return val;
		}
		return 0;
	}
	
	/**
	 * 获取矿占领(协助)上限
	 * @return
	 */
	public int getMineLimit() {
		Integer val = VipXMLInfoMap.getVipVal(vipLv, VipType.MINE_NUM);
		if (val != null) {
			return val;
		}
		return 0;
	}
	
	/**
	 * 获取攻城略地复活次数上限
	 * @return
	 */
	public int getCampaignReviceLimit() {
		Integer val = VipXMLInfoMap.getVipVal(vipLv, VipType.GCLDFH_NUM);
		if (val != null) {
			return val;
		}
		return 0;
	}

	public OpActivityProgressMap getOpActProMap() {
		return opActProMap;
	}

	public void setOpActProMap(OpActivityProgressMap opActProMap) {
		this.opActProMap = opActProMap;
	}

	public String getQihooUserId() {
		return qihooUserId;
	}

	public void setQihooUserId(String qihooUserId) {
		this.qihooUserId = qihooUserId;
	}

	public String getQihooToken() {
		return qihooToken;
	}

	public void setQihooToken(String qihooToken) {
		this.qihooToken = qihooToken;
	}
	
	public long getFightWorldBossHp() {
		return fightWorldBossHp;
	}

	public void setFightWorldBossHp(long fightWorldBossHp) {
		this.fightWorldBossHp = fightWorldBossHp;
	}
	public Timestamp getLastWorldBossFightTime() {
		return lastWorldBossFightTime;
	}

	public void setLastWorldBossFightTime(Timestamp lastWorldBossFightTime) {
		this.lastWorldBossFightTime = lastWorldBossFightTime;
	}

	public long getBestFightBossHp() {
		return bestFightBossHp;
	}

	public void setBestFightBossHp(long bestFightBossHp) {
		this.bestFightBossHp = bestFightBossHp;
	}
	
	public long getThisBossBest() {
		return thisBossBest;
	}

	public void setThisBossBest(long thisBossBest) {
		this.thisBossBest = thisBossBest;
	}

	public Map<Integer, RoleBoxRecordInfo> getRoleBoxMap() {
		return roleBoxMap;
	}

	public void setRoleBoxMap(Map<Integer, RoleBoxRecordInfo> roleBoxMap) {
		this.roleBoxMap = roleBoxMap;
	}

	public int getVipExp() {
		return vipExp;
	}

	public void setVipExp(int vipExp) {
		this.vipExp = vipExp;
	}
	
	public QuestInfoMap getQuestInfoMap() {
		return questInfoMap;
	}

	public int getWeaponFlag() {
		return WeaponFlag;
	}

	public void setWeaponFlag(int weaponFlag) {
		WeaponFlag = weaponFlag;
	}
	
	public int getClubId() {
		return clubId;
	}

	public void setClubId(int clubId) {
		this.clubId = clubId;
	}

	public void setQuestInfoMap(QuestInfoMap questInfoMap) {
		this.questInfoMap = questInfoMap;
	}
	
	public RankInfo getLevelRankInfo() {
		return levelRankInfo;
	}

	public void setLevelRankInfo(RankInfo levelRankInfo) {
		this.levelRankInfo = levelRankInfo;
	}

	public RankInfo getHeroNumRankInfo() {
		return heroNumRankInfo;
	}

	public void setHeroNumRankInfo(RankInfo heroNumRankInfo) {
		this.heroNumRankInfo = heroNumRankInfo;
	}
	
	public RankInfo getFightValueRankInfo() {
		return fightValueRankInfo;
	}

	public void setFightValueRankInfo(RankInfo fightValueRankInfo) {
		this.fightValueRankInfo = fightValueRankInfo;
	}

	public long getClubContributionSum() {
		return clubContributionSum;
	}

	public void setClubContributionSum(long clubContributionSum) {
		this.clubContributionSum = clubContributionSum;
	}

	public String getOfflinePushStr() {
		return offlinePushStr;
	}

	public void setOfflinePushStr(String offlinePushStr) {
		this.offlinePushStr = offlinePushStr;
	}

	public byte getRankShow() {
		return rankShow;
	}

	public void setRankShow(byte rankShow) {
		this.rankShow = rankShow;
	}

	public int getGoldBuyMaxNum(int buyType)
	{
		if(buyType == GoldBuyXMLInfo.TYPE_MONEY_BUY)
		{
			 return VipXMLInfoMap.getVipVal(vipLv, VipType.SILVER_BUY);
		}
		else if(buyType == GoldBuyXMLInfo.TYPE_SP_BUY)
		{
			return VipXMLInfoMap.getVipVal(vipLv, VipType.SP_BUY);
		}
		else if(buyType == GoldBuyXMLInfo.TYPE_TECH_BUY)
		{
			return VipXMLInfoMap.getVipVal(vipLv, VipType.TECH_BUY);
		}
		else if(buyType == GoldBuyXMLInfo.TYPE_ENERGY_BUY)
		{
			return VipXMLInfoMap.getVipVal(vipLv, VipType.ENERGY_BUY);
		}
		return 1;
	}
	
	public Timestamp getLastLookLogTime() {
		return lastLookLogTime;
	}

	public void setLastLookLogTime(Timestamp lastLookLogTime) {
		this.lastLookLogTime = lastLookLogTime;
	}

	public List<MineFightLog> getMineLogs() {
		return mineLogs;
	}
	
	public byte getIsAdvert() {
		return isAdvert;
	}

	public void setIsAdvert(byte isAdvert) {
		this.isAdvert = isAdvert;
	}

	public void addMineFightLog(MineFightLog log) {
		mineLogs.add(log);
		if (mineLogs.size() > GameValue.QUERY_MINE_LOG_SHOW_LIMEIT) {
			mineLogs.remove(0);
		}
	}

	public Timestamp getClubBuildTime() {
		 if(clubBuildTime == null){
			 clubBuildTime = new Timestamp(0);
		 }
		
		return clubBuildTime;
	}

	public void setClubBuildTime(Timestamp clubBuildTime) {
		this.clubBuildTime = clubBuildTime;
	}

	public long getTotalBuild() {
		return totalBuild;
	}

	public void setTotalBuild(long totalBuild) {
		this.totalBuild = totalBuild;
	}
	
	public int getWorshipCount() {
		return worshipCount;
	}

	public void setWorshipCount(int worshipCount) {
		this.worshipCount = worshipCount;
	}

	public int getBeWorshipNum() {
		return beWorshipNum;
	}

	public void setBeWorshipNum(int beWorshipNum) {
		this.beWorshipNum = beWorshipNum;
	}

	public void setMineLogs(List<MineFightLog> mineLogs) {
		this.mineLogs = mineLogs;
	}

	public AtomicBoolean getWorShipLock() {
		return worShipLock;
	}

	public void setWorShipLock(AtomicBoolean worShipLock) {
		this.worShipLock = worShipLock;
	}

	public String getSoldierInfo() {
		return soldierInfo;
	}

	public void setSoldierInfo(String soldierInfo) {
		this.soldierInfo = soldierInfo;
	}

	public boolean isFightEndClearBiaoche() {
		return fightEndClearBiaoche;
	}

	public void setFightEndClearBiaoche(boolean fightEndClearBiaoche) {
		this.fightEndClearBiaoche = fightEndClearBiaoche;
	}
	
	public Map<Integer, Integer> getLockShizhuang() {
		return lockShizhuang;
	}

	public void setLockShizhuang(Map<Integer, Integer> lockShizhuang) {
		this.lockShizhuang = lockShizhuang;
	}

	public Map<Integer, EquipInfo> getLockShizhuangMap() {
		return lockShizhuangMap;
	}

	public void setLockShizhuangMap(Map<Integer, EquipInfo> lockShizhuangMap) {
		this.lockShizhuangMap = lockShizhuangMap;
	}

	public String getClubTechPlusInfo() {
		return clubTechPlusInfo;
	}

	public void setClubTechPlusInfo(String clubTechPlusInfo) {
		this.clubTechPlusInfo = clubTechPlusInfo;
	}

	public int getIsCanAddFlag() {
		return isCanAddFlag;
	}

	public void setIsCanAddFlag(int isCanAddFlag) {
		this.isCanAddFlag = isCanAddFlag;
	}

	public Map<Integer, HireHeroInfo> getHireHeroMap() {
		if(hireHeroMap == null){
			hireHeroMap = new ConcurrentHashMap<Integer, HireHeroInfo>();
		}
		return hireHeroMap;
	}
	
	public HireHeroInfo getHireHeroInfo(int heroId){
		if(hireHeroMap != null){
			return hireHeroMap.get(heroId);
		}
		return null;
	}

	public void setHireHeroMap(Map<Integer, HireHeroInfo> hireHeroMap) {
		this.hireHeroMap = hireHeroMap;
	}

	public RideInfo getRideInfo() {
		return rideInfo;
	}

	public void setRideInfo(RideInfo rideInfo) {
		this.rideInfo = rideInfo;
	}
	
	public int getHireHeroVipPlus(){
		Integer val = VipXMLInfoMap.getVipVal(vipLv, VipType.HIER_HERO_NUM);
		if (val != null) {
			return val;
		}
		return 0;
	}

	public boolean isTitleRepair() {
		return titleRepair;
	}

	public void setTitleRepair(boolean titleRepair) {
		this.titleRepair = titleRepair;
	}

	public int getTodayPlusWorshipNum() {
		return todayPlusWorshipNum;
	}

	public void setTodayPlusWorshipNum(int todayPlusWorshipNum) {
		this.todayPlusWorshipNum = todayPlusWorshipNum;
	}
	
	
}
