package com.snail.webgame.game.protocal.recruit.query;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class QueryChestResp extends MessageBody {

	private int result;

	private byte recruitLimit;// 今日免费抽卡上限
	private byte recruitMoneyNum;// 今日免费抽卡次数
	private long lastRecruitMoneyTime;// 上次银子免费抽卡时间
	private long lastRecruitCoinTime;// 上次装备免费抽卡时间
	private long lastRecruitheroTime;//上次武将免费抽卡时间
	
	private byte recruitMoneySpaceTime;// 银子免费间隔时间（分钟）
	private byte recruitCoinSpaceTime;// 装备免费间隔时间（小时）
	private byte recruitHeroSpaceTime;// 武将抽卡间隔时间(小时)
	
	private byte oneRecruitCoinOpTimes; //金子单抽次数
	private byte oneRecrutiHeroNum;//武将单抽次数(提示多少次必出**)

	protected void setSequnce(ProtocolSequence ps) {
		ps.add("result", 0);

		ps.add("recruitLimit", 0);
		ps.add("recruitMoneyNum", 0);
		ps.add("lastRecruitMoneyTime", 0);
		ps.add("lastRecruitCoinTime", 0);
		ps.add("lastRecruitheroTime", 0);

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

	public long getLastRecruitheroTime() {
		return lastRecruitheroTime;
	}

	public void setLastRecruitheroTime(long lastRecruitheroTime) {
		this.lastRecruitheroTime = lastRecruitheroTime;
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