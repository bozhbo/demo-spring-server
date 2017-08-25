package com.snail.webgame.game.protocal.challenge.queryBattleDetail;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

/**
 * 已领取的宝箱
 * @author zhangyq
 *
 */
public class ChapterDetailRe extends MessageBody {

	private byte chapterType;//副本类型
	private short chapterNo;// 章节编号
	private String reserve;// 宝箱编号
	
	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("chapterType", 0);
		ps.add("chapterNo", 0);
		ps.addString("reserve", "flashCode", 0);

	}

	public byte getChapterType() {
		return chapterType;
	}

	public void setChapterType(byte chapterType) {
		this.chapterType = chapterType;
	}

	public short getChapterNo() {
		return chapterNo;
	}

	public void setChapterNo(short chapterNo) {
		this.chapterNo = chapterNo;
	}

	public String getReserve() {
		return reserve;
	}

	public void setReserve(String reserve) {
		this.reserve = reserve;
	}
	
}
