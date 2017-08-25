package com.snail.webgame.game.protocal.rank.rank;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class RankReq extends MessageBody {

	private byte rankType; //排行榜类型  1-等级 2-战斗力  3-世界boss伤害 4-武将数量//5-公会排行
	private short page;//查看排行榜第几页

	protected void setSequnce(ProtocolSequence ps) {
		ps.add("rankType", 0);
		ps.add("page", 0);
	}

	public byte getRankType() {
		return rankType;
	}

	public void setRankType(byte rankType) {
		this.rankType = rankType;
	}

	public short getPage() {
		return page;
	}

	public void setPage(short page) {
		this.page = page;
	}
	
}
