package com.snail.webgame.game.pvp.competition.check;

import java.nio.ByteOrder;

import org.apache.mina.common.ByteBuffer;

import com.snail.webgame.engine.component.room.protocol.info.impl.BaseRoomReq;

public class CheckFightServerResp extends BaseRoomReq {

	private byte result; // 1-服务器正常 2-服务器异常 3-服务器刚启动
	private long roleId;
	
	@Override
	public void bytes2Req(ByteBuffer buffer, ByteOrder order) {
		this.result = getByte(buffer, order);
		this.roleId = getLong(buffer, order);
		
	}

	public byte getResult() {
		return result;
	}

	public void setResult(byte result) {
		this.result = result;
	}

	public long getRoleId() {
		return roleId;
	}

	public void setRoleId(long roleId) {
		this.roleId = roleId;
	}
	
	

}
