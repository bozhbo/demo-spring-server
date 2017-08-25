package com.snail.webgame.game.protocal.recruit.recruit;

import java.util.ArrayList;
import java.util.List;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class ChestItemResp extends MessageBody {

	private int result;
	private byte action;// RecruitKind.xml No (1-有抽卡次数上限)

	// 抽到的奖励
	private int count;
	private List<ChestItemRe> list = new ArrayList<ChestItemRe>();

	private byte sourceType;// 1:银子 2:金子 3:体力 7:玩家经验 8:竞技场货币-勇气点 9:征战四方货币 正义点
							// 10:工会币 15:玩家等级 28:跨服币
	// 32:战功 34:历史战功 49:体力值购买次数 50:银子购买次数 51:经验活动剩余次数 52:金币活动剩余次数 53:用户名修改次数
	// 54:历史最高战斗力 55：精力
	private int sourceChange;// 资源变动数,正值为增加,负值为减少

	private byte recruitLimit;// 今日免费抽卡上限
	private byte recruitMoneyNum;// 今日免费抽卡次数
	private long lastRecruitMoneyTime;// 上次银子免费抽卡时间
	private long lastRecruitCoinTime;// 上次装备免费抽卡时间
	private long lastRecruitHeroTime;// 上次武将免费抽卡时间

	private byte recruitMoneySpaceTime;// 银子免费间隔时间（分钟）
	private byte recruitCoinSpaceTime;// 装备免费间隔时间（小时）
	private byte recruitHeroSpaceTime;// 武将抽卡间隔时间(小时)
	private byte oneRecruitCoinOpTimes; // 金子单抽次数
	private byte oneRecrutiHeroNum;//武将单抽次数

	protected void setSequnce(ProtocolSequence ps) {
		ps.add("result", 0);
		ps.add("action", 0);

		ps.add("count", 0);
		ps.addObjectArray("list",
				"com.snail.webgame.game.protocal.recruit.recruit.ChestItemRe",
				"count");

		ps.add("sourceType", 0);
		ps.add("sourceChange", 0);

		ps.add("recruitLimit", 0);
		ps.add("recruitMoneyNum", 0);
		ps.add("lastRecruitMoneyTime", 0);
		ps.add("lastRecruitCoinTime", 0);
		ps.add("lastRecruitHeroTime", 0);

		ps.add("recruitMoneySpaceTime", 0);
		ps.add("recruitCoinSpaceTime", 0);
		ps.add("recruitHeroSpaceTime", 0);
		
		ps.add("oneRecruitCoinOpTimes", 0);
		ps.add("oneRecrutiHeroNum", 0);
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public byte getAction() {
		return action;
	}

	public void setAction(byte action) {
		this.action = action;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public List<ChestItemRe> getList() {
		return list;
	}

	public void setList(List<ChestItemRe> list) {
		this.list = list;
	}

	public byte getSourceType() {
		return sourceType;
	}

	public void setSourceType(byte sourceType) {
		this.sourceType = sourceType;
	}

	public int getSourceChange() {
		return sourceChange;
	}

	public void setSourceChange(int sourceChange) {
		this.sourceChange = sourceChange;
	}

	public byte getRecruitLimit() {
		return recruitLimit;
	}

	public void setRecruitLimit(byte recruitLimit) {
		this.recruitLimit = recruitLimit;
	}

	public byte getRecruitMoneyNum() {
		return recruitMoneyNum;
	}

	public void setRecruitMoneyNum(byte recruitMoneyNum) {
		this.recruitMoneyNum = recruitMoneyNum;
	}

	public long getLastRecruitMoneyTime() {
		return lastRecruitMoneyTime;
	}

	public void setLastRecruitMoneyTime(long lastRecruitMoneyTime) {
		this.lastRecruitMoneyTime = lastRecruitMoneyTime;
	}

	public long getLastRecruitCoinTime() {
		return lastRecruitCoinTime;
	}

	public void setLastRecruitCoinTime(long lastRecruitCoinTime) {
		this.lastRecruitCoinTime = lastRecruitCoinTime;
	}

	public byte getRecruitMoneySpaceTime() {
		return recruitMoneySpaceTime;
	}

	public void setRecruitMoneySpaceTime(byte recruitMoneySpaceTime) {
		this.recruitMoneySpaceTime = recruitMoneySpaceTime;
	}

	public byte getRecruitCoinSpaceTime() {
		return recruitCoinSpaceTime;
	}

	public void setRecruitCoinSpaceTime(byte recruitCoinSpaceTime) {
		this.recruitCoinSpaceTime = recruitCoinSpaceTime;
	}

	public byte getOneRecruitCoinOpTimes() {
		return oneRecruitCoinOpTimes;
	}

	public void setOneRecruitCoinOpTimes(byte oneRecruitCoinOpTimes) {
		this.oneRecruitCoinOpTimes = oneRecruitCoinOpTimes;
	}

	public long getLastRecruitHeroTime() {
		return lastRecruitHeroTime;
	}

	public void setLastRecruitHeroTime(long lastRecruitHeroTime) {
		this.lastRecruitHeroTime = lastRecruitHeroTime;
	}

	public byte getRecruitHeroSpaceTime() {
		return recruitHeroSpaceTime;
	}

	public void setRecruitHeroSpaceTime(byte recruitHeroSpaceTime) {
		this.recruitHeroSpaceTime = recruitHeroSpaceTime;
	}

	public byte getOneRecrutiHeroNum() {
		return oneRecrutiHeroNum;
	}

	public void setOneRecrutiHeroNum(byte oneRecrutiHeroNum) {
		this.oneRecrutiHeroNum = oneRecrutiHeroNum;
	}

}