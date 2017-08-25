package com.snail.webgame.engine.net.msg.impl;

import java.nio.ByteOrder;

import org.apache.mina.common.ByteBuffer;

import com.snail.webgame.engine.game.base.util.ChargeStringHandle;
import com.snail.webgame.engine.net.msg.AbstractMessageBody;
import com.snail.webgame.engine.net.msg.IStringHandle;

public abstract class ChargeMessageResp extends AbstractMessageBody {

	@Override
	public void bytes2Req(ByteBuffer buffer, ByteOrder order) {
		return;
	}
	
	@Override
	public IStringHandle getStringHandle() {
		return ChargeStringHandle.getChargeStringHandle();
	}
}
