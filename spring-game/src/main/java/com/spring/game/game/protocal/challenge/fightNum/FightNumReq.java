package com.snail.webgame.game.protocal.challenge.fightNum;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class FightNumReq extends MessageBody {

	private byte chapterType; //副本类型
	private int chapterNo;	//副本章节
	private int battleNo; //关卡编号

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("chapterType", 0);
		ps.add("chapterNo", 0);
		ps.add("battleNo", 0);
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

	public int getBattleNo() {
		return battleNo;
	}

	public void setBattleNo(int battleNo) {
		this.battleNo = battleNo;
	}
	
}
