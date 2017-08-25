package com.snail.webgame.game.protocal.rolemgt.info;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class QueryRoleInfoResp extends MessageBody {

	private int result;
	private long roleId;// 角色Id
	private String uid;// 角色名称
	private String roleName;// 角色名称
	private int roleRace;// 从属势力
	private int maxFightValue;// 历史最高战斗力
	private int fightValueLv;// 战斗力评级【字段应取消】

	private int money;// 银子
	private int coin;// 金子（充值）
	private int kuafuMoney;// 征服令
	private int exploit;// 战功
	private int courage; // 勇气令
	private int justice; // 正义令

	private byte buySpTime;// 体力值购买次数
	private byte buyMoneyTime;// 银子购买次数
	private byte buyEnergyTime;// 精力购买次数
	private byte roleCompetitionState;// 竞技场状态 1-竞技场报名中 2-竞技场战斗中
	private byte expLeftTimes1;// 经验活动剩余次数
	private byte expLeftTimes2;// 经验活动剩余次数
	private byte expLeftTimes3;// 经验活动剩余次数
	private byte expLeftTimes4;// 经验活动剩余次数
	private byte expLeftTimes5;// 经验活动剩余次数
	private byte expLeftTimes6;// 经验活动剩余次数
	private byte moneyLeftTimes;// 金币活动剩余次数(攻城略地)
	private byte mutualFightTimes;// 长坂坡剩余次数
	private byte attackFightTimes;//狭路相逢剩余次数
	private byte biaoCheTimes; //押镖剩余次数

	private byte changeRoleNameTimes;// 用户名修改次数
	private int clubContribution;// 公会贡献值
	private int pvp3Money; // 荣誉点
	private short buySkillPointTime;// 技能点购买次数
	
	private long worldRoomId; // 世界聊天房间Id
	private long clubRoomId; // 公会聊天房间Id
	
	private String teamChallengeTimes;// 组队副本各个副本对应的已奖励次数
	private byte team3V3Times;// 已奖励的竞技场3V3次数
	
	private int activeVal;// 活跃度
	private int teamMoney;// 斩将令

	protected void setSequnce(ProtocolSequence ps) {
		ps.add("result", 0);
		ps.add("roleId", 0);
		ps.addString("uid", "flashCode", 0);
		ps.addString("roleName", "flashCode", 0);
		ps.add("roleRace", 0);
		ps.add("maxFightValue", 0);
		ps.add("fightValueLv", 0);

		ps.add("money", 0);
		ps.add("coin", 0);
		ps.add("kuafuMoney", 0);
		ps.add("exploit", 0);
		ps.add("courage", 0);
		ps.add("justice", 0);

		ps.add("buySpTime", 0);
		ps.add("buyMoneyTime", 0);
		ps.add("buyEnergyTime", 0);
		ps.add("roleCompetitionState", 0);
		ps.add("expLeftTimes1", 0);
		ps.add("expLeftTimes2", 0);
		ps.add("expLeftTimes3", 0);
		ps.add("expLeftTimes4", 0);
		ps.add("expLeftTimes5", 0);
		ps.add("expLeftTimes6", 0);
		ps.add("moneyLeftTimes", 0);
		ps.add("mutualFightTimes", 0);
		ps.add("attackFightTimes", 0);
		ps.add("biaoCheTimes", 0);

		ps.add("changeRoleNameTimes", 0);
		ps.add("clubContribution", 0);
		ps.add("pvp3Money", 0);
		ps.add("buySkillPointTime", 0);
		
		ps.add("worldRoomId", 0);
		ps.add("clubRoomId", 0);
		ps.addString("teamChallengeTimes", "flashCode", 0);
		ps.add("team3V3Times", 0);
		ps.add("activeVal", 0);
		ps.add("teamMoney", 0);
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public long getRoleId() {
		return roleId;
	}

	public void setRoleId(long roleId) {
		this.roleId = roleId;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public int getRoleRace() {
		return roleRace;
	}

	public void setRoleRace(int roleRace) {
		this.roleRace = roleRace;
	}

	public int getMaxFightValue() {
		return maxFightValue;
	}

	public void setMaxFightValue(int maxFightValue) {
		this.maxFightValue = maxFightValue;
	}

	public int getFightValueLv() {
		return fightValueLv;
	}

	public void setFightValueLv(int fightValueLv) {
		this.fightValueLv = fightValueLv;
	}

	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
	}

	public int getCoin() {
		return coin;
	}

	public void setCoin(int coin) {
		this.coin = coin;
	}

	public byte getBuySpTime() {
		return buySpTime;
	}

	public void setBuySpTime(byte buySpTime) {
		this.buySpTime = buySpTime;
	}

	public byte getBuyMoneyTime() {
		return buyMoneyTime;
	}

	public void setBuyMoneyTime(byte buyMoneyTime) {
		this.buyMoneyTime = buyMoneyTime;
	}

	public byte getRoleCompetitionState() {
		return roleCompetitionState;
	}

	public void setRoleCompetitionState(byte roleCompetitionState) {
		this.roleCompetitionState = roleCompetitionState;
	}

	public int getKuafuMoney() {
		return kuafuMoney;
	}

	public void setKuafuMoney(int kuafuMoney) {
		this.kuafuMoney = kuafuMoney;
	}

	public int getExploit() {
		return exploit;
	}

	public void setExploit(int exploit) {
		this.exploit = exploit;
	}

	public int getCourage() {
		return courage;
	}

	public void setCourage(int courage) {
		this.courage = courage;
	}

	public int getJustice() {
		return justice;
	}

	public void setJustice(int justice) {
		this.justice = justice;
	}

	public byte getMoneyLeftTimes() {
		return moneyLeftTimes;
	}

	public void setMoneyLeftTimes(byte moneyLeftTimes) {
		this.moneyLeftTimes = moneyLeftTimes;
	}

	public byte getMutualFightTimes() {
		return mutualFightTimes;
	}

	public void setMutualFightTimes(byte mutualFightTimes) {
		this.mutualFightTimes = mutualFightTimes;
	}

	public byte getAttackFightTimes() {
		return attackFightTimes;
	}

	public void setAttackFightTimes(byte attackFightTimes) {
		this.attackFightTimes = attackFightTimes;
	}

	public byte getChangeRoleNameTimes() {
		return changeRoleNameTimes;
	}

	public void setChangeRoleNameTimes(byte changeRoleNameTimes) {
		this.changeRoleNameTimes = changeRoleNameTimes;
	}

	public byte getBuyEnergyTime() {
		return buyEnergyTime;
	}

	public void setBuyEnergyTime(byte buyEnergyTime) {
		this.buyEnergyTime = buyEnergyTime;
	}

	public int getClubContribution() {
		return clubContribution;
	}

	public void setClubContribution(int clubContribution) {
		this.clubContribution = clubContribution;
	}

	public int getPvp3Money() {
		return pvp3Money;
	}

	public void setPvp3Money(int pvp3Money) {
		this.pvp3Money = pvp3Money;
	}

	public short getBuySkillPointTime() {
		return buySkillPointTime;
	}

	public void setBuySkillPointTime(short buySkillPointTime) {
		this.buySkillPointTime = buySkillPointTime;
	}

	public byte getBiaoCheTimes() {
		return biaoCheTimes;
	}

	public void setBiaoCheTimes(byte biaoCheTimes) {
		this.biaoCheTimes = biaoCheTimes;
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

	public long getWorldRoomId() {
		return worldRoomId;
	}

	public void setWorldRoomId(long worldRoomId) {
		this.worldRoomId = worldRoomId;
	}

	public long getClubRoomId() {
		return clubRoomId;
	}

	public void setClubRoomId(long clubRoomId) {
		this.clubRoomId = clubRoomId;
	}

	public String getTeamChallengeTimes() {
		return teamChallengeTimes;
	}

	public void setTeamChallengeTimes(String teamChallengeTimes) {
		this.teamChallengeTimes = teamChallengeTimes;
	}

	public byte getTeam3V3Times() {
		return team3V3Times;
	}

	public void setTeam3V3Times(byte team3v3Times) {
		team3V3Times = team3v3Times;
	}

	public int getActiveVal() {
		return activeVal;
	}

	public void setActiveVal(int activeVal) {
		this.activeVal = activeVal;
	}

	public int getTeamMoney() {
		return teamMoney;
	}

	public void setTeamMoney(int teamMoney) {
		this.teamMoney = teamMoney;
	}
}
