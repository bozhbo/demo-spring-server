package com.snail.webgame.game.pvp.competition.cancel;

import java.nio.ByteOrder;

import org.apache.mina.common.ByteBuffer;

import com.snail.webgame.engine.component.room.protocol.info.impl.BaseRoomReq;

public class FightCancelResp extends BaseRoomReq {
	private int result; // 取消匹配结果 1-取消匹配成功 2-取消匹配失败
	private int roleId;// 角色 roleId

	@Override
	public void bytes2Req(ByteBuffer buffer, ByteOrder order) {
		result = getInt(buffer, order);
		roleId = getInt(buffer, order);
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	
}
