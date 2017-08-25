package com.snail.webgame.game.protocal.challenge.sweep;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class SweepReq extends MessageBody {
	
	private byte chapterType; //副本类型
	private int chapterNo; //章节编号
	private int battleId;	//关卡
	private byte times;//扫荡次数

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("chapterType", 0);
		ps.add("chapterNo", 0);
		ps.add("battleId", 0);
		ps.add("times", 0);
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

	public int getBattleId() {
		return battleId;
	}

	public void setBattleId(int battleId) {
		this.battleId = battleId;
	}

	public byte getTimes() {
		return times;
	}

	public void setTimes(byte times) {
		this.times = times;
	}

}
