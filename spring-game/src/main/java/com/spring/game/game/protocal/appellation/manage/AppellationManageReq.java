package com.snail.webgame.game.protocal.appellation.manage;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

/**
 * 称谓管理消息体
 * 
 * @author SnailGame
 * 
 */
public class AppellationManageReq extends MessageBody {
	private byte chenghaoType;// 类型 0-卸 1-穿 
	private int id;// 称谓ID xmlNo

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("chenghaoType", 0);
		ps.add("id", 0);
	}

	public byte getChenghaoType() {
		return chenghaoType;
	}

	public void setChenghaoType(byte chenghaoType) {
		this.chenghaoType = chenghaoType;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
