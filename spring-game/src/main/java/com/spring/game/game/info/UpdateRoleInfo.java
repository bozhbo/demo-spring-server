package com.snail.webgame.game.info;

import java.sql.Timestamp;

import com.snail.webgame.engine.common.to.BaseTO;

/**
 * 用户信
 */
public class UpdateRoleInfo extends BaseTO {

	private String account;// 帐号
	private String roleName;// 角色名称
	private String uid;// 全服唯一标识符
	private byte roleRace;// 从属势力
	private int maxFightValue;// 历史最高战斗力

	private long money;// 银子
	private long coin;// 金子（充值）
	private long totalCoin;

	private short sp;// 行动值
	private Timestamp lastRecoverSPTime;// 最后自动回复时间

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

	/**
	 * 活动 经验，银币字段
	 */
	private byte expLeftTimes; // 经验活动剩余次数
	private byte expBuyedTimes; // 经验活动已经购买的次数
	private byte moneyLeftTimes; // 银币活动剩余次数
	private byte moneyBuyedTimes; // 银币活动已经购买的次数

	/**
	 * 竞技场字段
	 */
	private int competitionValue;// 竞技场积分
	private byte competitionStage;// 竞技场段位
	private long competitionTime;// 竞技场积分时间 用于排行
	private byte torrentTimes;// 连续胜利/失败次数 正值为胜利次数 负值为失败次数
	private int stageAward; // 段位奖励发送(二进制表示) 1-已发送
	private int winTimes;// 胜利次数
	private int loseTimes;// 失败次数
	private byte stageWinTimes;// 段位赛胜利次数
	private byte stageLoseTimes;// 段位赛失败次数
	private byte stageState;// 段位赛胜利失败

	private int guideNo; // 引导编号
	private String funcOpenStr;// 功能开启, 格式：功能编号,功能编号

	private short buySpNum;// 当日购买次数（体力）
	private Timestamp lastBuySpTime;// 最后购买时间

	private int buyMoneyNum;// 当日购买次数（点金手）
	private Timestamp lastBuyMoneyTime;// 最后购买时间（点金手）
	// ================== 体力 ====================//
	// =============== 抽卡================//
	private byte recruitMoneyNum;// 今日免费抽卡次数
	private Timestamp lastRecruitMoneyTime;// 上次银子免费抽卡时间
	private byte recruitMoneyStatus;// 是否第一次单抽（银子非免费） 0:未first 1:已first
	private byte tenRecruitMoneyStats;//固定十连抽奖励次数（银子十连抽）
	private Timestamp lastRecruitCoinTime;// 上次金子免费抽卡时间
	private byte recruitCoinStatus;// 是否第一次单抽（金子免费） 0:未first 1:已first
	private byte tenRecruitCoinStatus;// 固定十连抽奖励次数（金子十连抽）
	// =============== 抽卡================//

	private long devotePoint;// 工会币
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

	private Timestamp goldShopAutoTime;// 黑市商店自动刷新时间
	private int buyGoldShopNum;// 黑市商店当日手动刷新次数
	private Timestamp lastBuyGoldShopTime;// 黑市商店最后手动刷新时间

	private long courage;// 竞技场货币 勇气点
	private Timestamp lastCourageTime;// 竞技场商店最后更新时间
	private byte buyCourageNum;// 当日手动刷新次数
	private Timestamp lastBuyCourageTime;// 最后手动刷新时间

	private long justice;// 征战四方货币 正义点
	private Timestamp lastJusticeTime;// 征战四方商店最后更新时间
	private int buyJusticeNum;// 当日手动刷新次数
	private Timestamp lastBuyJusticeTime;// 最后手动刷新时间

	private float pointX;// 场景中坐标X
	private float pointY;// 场景中坐标X
	private float pointZ;// 场景中坐标X

	private String checkNum;// 签到记录,(格式：1,1,1,1,1,0,0,0,0,0...)每一位代表每一天，月奖励为每月最后一天+1
	private Timestamp checkInTime;// 签到时间

	private String checkIn7DayNum; // 奖励领取记录(格式：1,1,1,0,0,0,0)
	private byte checkIn7DayMaxLoginDays; // 连续登陆最大天数
	private byte checkIn7DayCurrentLoginDays;// 当前连续登陆天数
	private Timestamp checkIn7DayTime = new Timestamp(0); // 最后登陆时间

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

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public byte getRoleRace() {
		return roleRace;
	}

	public void setRoleRace(byte roleRace) {
		this.roleRace = roleRace;
	}

	public int getMaxFightValue() {
		return maxFightValue;
	}

	public void setMaxFightValue(int maxFightValue) {
		this.maxFightValue = maxFightValue;
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

	public short getSp() {
		return sp;
	}

	public void setSp(short sp) {
		this.sp = sp;
	}

	public Timestamp getLastRecoverSPTime() {
		return lastRecoverSPTime;
	}

	public void setLastRecoverSPTime(Timestamp lastRecoverSPTime) {
		this.lastRecoverSPTime = lastRecoverSPTime;
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

	public int getClientType() {
		return clientType;
	}

	public void setClientType(int clientType) {
		this.clientType = clientType;
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

	public byte getPunishStatus() {
		return punishStatus;
	}

	public void setPunishStatus(byte punishStatus) {
		this.punishStatus = punishStatus;
	}

	public Timestamp getPunishTime() {
		return punishTime;
	}

	public void setPunishTime(Timestamp punishTime) {
		this.punishTime = punishTime;
	}

	public byte getGmRight() {
		return gmRight;
	}

	public void setGmRight(byte gmRight) {
		this.gmRight = gmRight;
	}

	public byte getChangeRoleNameTimes() {
		return changeRoleNameTimes;
	}

	public void setChangeRoleNameTimes(byte changeRoleNameTimes) {
		this.changeRoleNameTimes = changeRoleNameTimes;
	}

	public byte getExpLeftTimes() {
		return expLeftTimes;
	}

	public void setExpLeftTimes(byte expLeftTimes) {
		this.expLeftTimes = expLeftTimes;
	}

	public byte getExpBuyedTimes() {
		return expBuyedTimes;
	}

	public void setExpBuyedTimes(byte expBuyedTimes) {
		this.expBuyedTimes = expBuyedTimes;
	}

	public byte getMoneyLeftTimes() {
		return moneyLeftTimes;
	}

	public void setMoneyLeftTimes(byte moneyLeftTimes) {
		this.moneyLeftTimes = moneyLeftTimes;
	}

	public byte getMoneyBuyedTimes() {
		return moneyBuyedTimes;
	}

	public void setMoneyBuyedTimes(byte moneyBuyedTimes) {
		this.moneyBuyedTimes = moneyBuyedTimes;
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

	public byte getStageState() {
		return stageState;
	}

	public void setStageState(byte stageState) {
		this.stageState = stageState;
	}

	public int getGuideNo() {
		return guideNo;
	}

	public void setGuideNo(int guideNo) {
		this.guideNo = guideNo;
	}

	public String getFuncOpenStr() {
		return funcOpenStr;
	}

	public void setFuncOpenStr(String funcOpenStr) {
		this.funcOpenStr = funcOpenStr;
	}

	public short getBuySpNum() {
		return buySpNum;
	}

	public void setBuySpNum(short buySpNum) {
		this.buySpNum = buySpNum;
	}

	public Timestamp getLastBuySpTime() {
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

	public Timestamp getLastBuyMoneyTime() {
		return lastBuyMoneyTime;
	}

	public void setLastBuyMoneyTime(Timestamp lastBuyMoneyTime) {
		this.lastBuyMoneyTime = lastBuyMoneyTime;
	}

	public byte getRecruitMoneyNum() {
		return recruitMoneyNum;
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

	public byte getRecruitMoneyStatus() {
		return recruitMoneyStatus;
	}

	public void setRecruitMoneyStatus(byte recruitMoneyStatus) {
		this.recruitMoneyStatus = recruitMoneyStatus;
	}

	public Timestamp getLastRecruitCoinTime() {
		return lastRecruitCoinTime;
	}

	public void setLastRecruitCoinTime(Timestamp lastRecruitCoinTime) {
		this.lastRecruitCoinTime = lastRecruitCoinTime;
	}

	public byte getRecruitCoinStatus() {
		return recruitCoinStatus;
	}

	public void setRecruitCoinStatus(byte recruitCoinStatus) {
		this.recruitCoinStatus = recruitCoinStatus;
	}

	public long getDevotePoint() {
		return devotePoint;
	}

	public void setDevotePoint(long devotePoint) {
		this.devotePoint = devotePoint;
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

	public String getCheckNum() {
		return checkNum;
	}

	public void setCheckNum(String checkNum) {
		this.checkNum = checkNum;
	}

	public Timestamp getCheckInTime() {
		return checkInTime;
	}

	public void setCheckInTime(Timestamp checkInTime) {
		this.checkInTime = checkInTime;
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

	public byte getTenRecruitMoneyStats() {
		return tenRecruitMoneyStats;
	}

	public void setTenRecruitMoneyStats(byte tenRecruitMoneyStats) {
		this.tenRecruitMoneyStats = tenRecruitMoneyStats;
	}

	public byte getTenRecruitCoinStatus() {
		return tenRecruitCoinStatus;
	}

	public void setTenRecruitCoinStatus(byte tenRecruitCoinStatus) {
		this.tenRecruitCoinStatus = tenRecruitCoinStatus;
	}

	public long getTotalCoin() {
		return totalCoin;
	}

	public void setTotalCoin(long totalCoin) {
		this.totalCoin = totalCoin;
	}

	@Override
	public byte getSaveMode() {
		return ONLINE;
	}
}