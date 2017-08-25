package com.snail.webgame.game.protocal.rolemgt.detail;

import java.util.ArrayList;
import java.util.List;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

import com.snail.webgame.game.protocal.challenge.queryBattleDetail.BattleDetailRe;
import com.snail.webgame.game.protocal.challenge.queryBattleDetail.ChapterDetailRe;
import com.snail.webgame.game.protocal.equip.query.EquipDetailRe;
import com.snail.webgame.game.protocal.hero.query.HeroDetailRe;
import com.snail.webgame.game.protocal.item.query.BagItemRe;
import com.snail.webgame.game.protocal.rolemgt.info.QueryRoleInfoResp;
import com.snail.webgame.game.protocal.soldier.query.QuerySoldierInfoRe;

public class RoleDetailResp extends MessageBody {

	private int result;
	// 角色信息
	private QueryRoleInfoResp roleInfo;

	// 武将信息
	private int heroCount;
	private List<HeroDetailRe> heroList = new ArrayList<HeroDetailRe>();

	// 背包装备
	private int bagEquipCount;
	private List<EquipDetailRe> bagEquipList = new ArrayList<EquipDetailRe>();

	// 用户其他背包
	private int roleBagCount;
	private List<BagItemRe> roleBagList = new ArrayList<BagItemRe>();

	// 玩家可打的副本信息
	private int battleCount;
	private List<BattleDetailRe> battleList = new ArrayList<BattleDetailRe>();
	private String battle;// 新开启的副本信息

	// 玩家领取过的副本宝箱
	private int prizeCount;
	private List<ChapterDetailRe> chapterList = new ArrayList<ChapterDetailRe>();

	private byte guideNo; // 引导编号
	private int sp;// 当前最新的体力
	private int energy; // 当前最新的精力

	private long lastRecoverSpTime;// 上次回复体力时间
	private long lastRecoverEnergyTime;// 上次回复精力时间

	private String curFuncOpenStr;// 当前已开启的功能编号字符串 功能编号no,功能编号no

	// 士兵信息
	private int soldierCount;
	private List<QuerySoldierInfoRe> soldierList = new ArrayList<QuerySoldierInfoRe>();

	private String deployPosOpenStr;// 布阵位置购买开启(0 未开启 1开启)例 1,0

	private int tech; // 当前最新的技能点
	private long lastRecoverTechTime;// 上次回复技能点时间

	// 供充值使用
	private int accountId;// 账号id
	private int issuerID;// 运营商id
	private int serverId;// 游戏服务器id
	private String hmacStr; // 一次性串

	private String qihooUserId;// 360 userId
	private String qihooToken;// 360 token

	// 玩家锁定的时装
	private int showPlanId;// 1-显示套装  0-显示时装
	private String lockShizhuang;// 玩家锁定的时装 equipType,equipId;....
	private String haveReward;// 已领取套装奖励<次数>（1,2,3,4）

	private int title; // 当前称号xmlNo
	private String titles; // xmlNo:time;xmlNo:time;
	
	private int popUpType;// 登录弹框类型 0：不弹框 1：首充面板...
	private String clubTechXmlNo;// 公会客户个人属性XML编号
	private String systemNotice;//聊天频道内置顶系统公告内容

	protected void setSequnce(ProtocolSequence ps) {
		ps.add("result", 0);
		ps.addObject("roleInfo");

		ps.add("heroCount", 0);
		ps.addObjectArray("heroList",
				"com.snail.webgame.game.protocal.hero.query.HeroDetailRe",
				"heroCount");

		ps.add("bagEquipCount", 0);
		ps.addObjectArray("bagEquipList",
				"com.snail.webgame.game.protocal.equip.query.EquipDetailRe",
				"bagEquipCount");

		ps.add("roleBagCount", 0);
		ps.addObjectArray("roleBagList",
				"com.snail.webgame.game.protocal.equip.query.BagItemRe",
				"roleBagCount");

		ps.add("battleCount", 0);
		ps.addObjectArray(
				"battleList",
				"com.snail.webgame.game.protocal.challenge.queryBattleDetail.BattleDetailRe",
				"battleCount");
		ps.addString("battle", "flashCode", 0);

		ps.add("prizeCount", 0);
		ps.addObjectArray(
				"chapterList",
				"com.snail.webgame.game.protocal.challenge.queryBattleDetail.ChapterDetailRe",
				"prizeCount");

		ps.add("guideNo", 0);
		ps.add("sp", 0);
		ps.add("lastRecoverSpTime", 0);
		ps.add("energy", 0);
		ps.add("lastRecoverEnergyTime", 0);
		ps.addString("curFuncOpenStr", "flashCode", 0);

		ps.add("soldierCount", 0);
		ps.addObjectArray(
				"soldierList",
				"com.snail.webgame.game.protocal.soldier.query.QuerySoldierInfoRe",
				"soldierCount");

		ps.addString("deployPosOpenStr", "flashCode", 0);

		ps.add("tech", 0);
		ps.add("lastRecoverTechTime", 0);

		ps.add("accountId", 0);
		ps.add("issuerID", 0);
		ps.add("serverId", 0);
		ps.addString("hmacStr", "flashCode", 0);

		ps.addString("qihooUserId", "flashCode", 0);
		ps.addString("qihooToken", "flashCode", 0);

		ps.add("showPlanId", 0);
		ps.addString("lockShizhuang", "flashCode", 0);
		ps.addString("haveReward", "flashCode", 0);

		ps.add("title", 0);
		ps.addString("titles", "flashCode", 0);
		
		ps.add("popUpType", 0);
		ps.addString("clubTechXmlNo", "flashCode", 0);

		ps.addString("systemNotice", "flashCode", 0);
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public QueryRoleInfoResp getRoleInfo() {
		return roleInfo;
	}

	public void setRoleInfo(QueryRoleInfoResp roleInfo) {
		this.roleInfo = roleInfo;
	}

	public int getHeroCount() {
		return heroCount;
	}

	public void setHeroCount(int heroCount) {
		this.heroCount = heroCount;
	}

	public List<HeroDetailRe> getHeroList() {
		return heroList;
	}

	public void setHeroList(List<HeroDetailRe> heroList) {
		this.heroList = heroList;
	}

	public int getBagEquipCount() {
		return bagEquipCount;
	}

	public void setBagEquipCount(int bagEquipCount) {
		this.bagEquipCount = bagEquipCount;
	}

	public List<EquipDetailRe> getBagEquipList() {
		return bagEquipList;
	}

	public void setBagEquipList(List<EquipDetailRe> bagEquipList) {
		this.bagEquipList = bagEquipList;
	}

	public int getRoleBagCount() {
		return roleBagCount;
	}

	public void setRoleBagCount(int roleBagCount) {
		this.roleBagCount = roleBagCount;
	}

	public List<BagItemRe> getRoleBagList() {
		return roleBagList;
	}

	public void setRoleBagList(List<BagItemRe> roleBagList) {
		this.roleBagList = roleBagList;
	}

	public int getBattleCount() {
		return battleCount;
	}

	public void setBattleCount(int battleCount) {
		this.battleCount = battleCount;
	}

	public List<BattleDetailRe> getBattleList() {
		return battleList;
	}

	public void setBattleList(List<BattleDetailRe> battleList) {
		this.battleList = battleList;
	}

	public int getPrizeCount() {
		return prizeCount;
	}

	public void setPrizeCount(int prizeCount) {
		this.prizeCount = prizeCount;
	}

	public List<ChapterDetailRe> getChapterList() {
		return chapterList;
	}

	public void setChapterList(List<ChapterDetailRe> chapterList) {
		this.chapterList = chapterList;
	}

	public byte getGuideNo() {
		return guideNo;
	}

	public void setGuideNo(byte guideNo) {
		this.guideNo = guideNo;
	}

	public int getSp() {
		return sp;
	}

	public void setSp(int sp) {
		this.sp = sp;
	}

	public int getEnergy() {
		return energy;
	}

	public void setEnergy(int energy) {
		this.energy = energy;
	}

	public long getLastRecoverSpTime() {
		return lastRecoverSpTime;
	}

	public void setLastRecoverSpTime(long lastRecoverSpTime) {
		this.lastRecoverSpTime = lastRecoverSpTime;
	}

	public String getCurFuncOpenStr() {
		return curFuncOpenStr;
	}

	public void setCurFuncOpenStr(String curFuncOpenStr) {
		this.curFuncOpenStr = curFuncOpenStr;
	}

	public int getSoldierCount() {
		return soldierCount;
	}

	public void setSoldierCount(int soldierCount) {
		this.soldierCount = soldierCount;
	}

	public List<QuerySoldierInfoRe> getSoldierList() {
		return soldierList;
	}

	public void setSoldierList(List<QuerySoldierInfoRe> soldierList) {
		this.soldierList = soldierList;
	}

	public long getLastRecoverEnergyTime() {
		return lastRecoverEnergyTime;
	}

	public void setLastRecoverEnergyTime(long lastRecoverEnergyTime) {
		this.lastRecoverEnergyTime = lastRecoverEnergyTime;
	}

	public String getBattle() {
		return battle;
	}

	public void setBattle(String battle) {
		this.battle = battle;
	}

	public String getDeployPosOpenStr() {
		return deployPosOpenStr;
	}

	public void setDeployPosOpenStr(String deployPosOpenStr) {
		this.deployPosOpenStr = deployPosOpenStr;
	}

	public int getTech() {
		return tech;
	}

	public void setTech(int tech) {
		this.tech = tech;
	}

	public long getLastRecoverTechTime() {
		return lastRecoverTechTime;
	}

	public void setLastRecoverTechTime(long lastRecoverTechTime) {
		this.lastRecoverTechTime = lastRecoverTechTime;
	}

	public int getAccountId() {
		return accountId;
	}

	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}

	public int getIssuerID() {
		return issuerID;
	}

	public void setIssuerID(int issuerID) {
		this.issuerID = issuerID;
	}

	public int getServerId() {
		return serverId;
	}

	public void setServerId(int serverId) {
		this.serverId = serverId;
	}

	public String getHmacStr() {
		return hmacStr;
	}

	public void setHmacStr(String hmacStr) {
		this.hmacStr = hmacStr;
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

	public int getShowPlanId() {
		return showPlanId;
	}

	public void setShowPlanId(int showPlanId) {
		this.showPlanId = showPlanId;
	}

	public String getLockShizhuang() {
		return lockShizhuang;
	}

	public void setLockShizhuang(String lockShizhuang) {
		this.lockShizhuang = lockShizhuang;
	}

	public int getTitle() {
		return title;
	}

	public void setTitle(int title) {
		this.title = title;
	}

	public String getTitles() {
		return titles;
	}

	public void setTitles(String titles) {
		this.titles = titles;
	}

	public String getHaveReward() {
		return haveReward;
	}

	public void setHaveReward(String haveReward) {
		this.haveReward = haveReward;
	}

	public int getPopUpType() {
		return popUpType;
	}

	public void setPopUpType(int popUpType) {
		this.popUpType = popUpType;
	}

	public String getClubTechXmlNo() {
		return clubTechXmlNo;
	}

	public void setClubTechXmlNo(String clubTechXmlNo) {
		this.clubTechXmlNo = clubTechXmlNo;
	}
	
	public String getSystemNotice() {
		return systemNotice;
	}

	public void setSystemNotice(String systemNotice) {
		this.systemNotice = systemNotice;
	}
}