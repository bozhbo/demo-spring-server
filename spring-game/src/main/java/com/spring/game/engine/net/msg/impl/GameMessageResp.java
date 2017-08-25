package com.snail.webgame.engine.net.msg.impl;

import java.nio.ByteOrder;

import org.apache.mina.common.ByteBuffer;

import com.snail.webgame.engine.game.base.util.GameStringHandle;
import com.snail.webgame.engine.net.msg.AbstractMessageBody;
import com.snail.webgame.engine.net.msg.IStringHandle;

public abstract class GameMessageResp extends AbstractMessageBody {

	@Override
	public void bytes2Req(ByteBuffer buffer, ByteOrder order) {
		return;
	}
	
	@Override
	public IStringHandle getStringHandle() {
		return GameStringHandle.getGameStringHandle();
	}
}
