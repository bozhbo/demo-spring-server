package com.snail.webgame.game.protocal.gmcc.wordlist;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class SysWordListReq extends MessageBody {

	private byte type;
	private String word;

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("type", 0);
		ps.addString("word", "flashCode", 0);
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public byte getType() {
		return type;
	}

	public void setType(byte type) {
		this.type = type;
	}

}
