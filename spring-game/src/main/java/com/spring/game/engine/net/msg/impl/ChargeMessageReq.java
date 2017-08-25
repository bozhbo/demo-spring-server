package com.snail.webgame.engine.net.msg.impl;

import java.nio.ByteOrder;

import org.apache.mina.common.ByteBuffer;

import com.snail.webgame.engine.game.base.util.ChargeStringHandle;
import com.snail.webgame.engine.net.msg.AbstractMessageBody;
import com.snail.webgame.engine.net.msg.IStringHandle;

public abstract class ChargeMessageReq extends AbstractMessageBody {

	@Override
	public void resp2Bytes(ByteBuffer buffer, ByteOrder order) {
		return;
	}
	
	@Override
	public IStringHandle getStringHandle() {
		return ChargeStringHandle.getChargeStringHandle();
	}
}