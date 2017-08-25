package com.snail.webgame.game.protocal.countryfight.fight.createbattle;

import java.nio.ByteOrder;

import org.apache.mina.common.ByteBuffer;

import com.snail.webgame.engine.component.room.protocol.info.impl.BaseRoomReq;

/**
 * 创建国战战斗响应
 * 
 * @author xiasd
 *
 */
public class CreateStateFightResp extends BaseRoomReq {

	private int result;
	
	@Override
	public void bytes2Req(ByteBuffer buffer, ByteOrder order) {
		this.result = getInt(buffer, order);
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}
	
}
