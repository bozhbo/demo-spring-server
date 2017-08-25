package com.snail.webgame.game.protocal.challenge.getprize;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class GetPrizeReq extends MessageBody {

	private byte chapterType; //副本类型
	private int chapterNo;	//副本章节
	private byte prizeNo; //宝箱编号

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("chapterType", 0);
		ps.add("chapterNo", 0);
		ps.add("prizeNo", 0);
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

	public byte getPrizeNo() {
		return prizeNo;
	}

	public void setPrizeNo(byte prizeNo) {
		this.prizeNo = prizeNo;
	}
}
