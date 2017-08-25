package com.snail.webgame.game.protocal.appellation.manage;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class AllAppellationRe extends MessageBody {
	private int id; // 称号ID
	private int sec; // 称号到点秒数

	protected void setSequnce(ProtocolSequence ps) {
		ps.add("id", 0);
		ps.add("sec", 0);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getSec() {
		return sec;
	}

	public void setSec(int sec) {
		this.sec = sec;
	}
}
