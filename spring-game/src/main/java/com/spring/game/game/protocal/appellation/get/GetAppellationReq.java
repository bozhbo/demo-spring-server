package com.snail.webgame.game.protocal.appellation.get;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

/**
 * 获得称谓消息体
 * 
 * @author SnailGame
 * 
 */
public class GetAppellationReq extends MessageBody {
	private byte chenghaoType; // 1-获得 2-过期
	private int id; // 称谓ID

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("chenghaoType", 0);
		ps.add("id", 0);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public byte getChenghaoType() {
		return chenghaoType;
	}

	public void setChenghaoType(byte chenghaoType) {
		this.chenghaoType = chenghaoType;
	}

}
