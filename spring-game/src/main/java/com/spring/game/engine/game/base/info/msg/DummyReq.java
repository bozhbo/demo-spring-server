package com.snail.webgame.engine.game.base.info.msg;

import java.nio.ByteOrder;

import org.apache.mina.common.ByteBuffer;

import com.snail.webgame.engine.net.msg.impl.GameMessageReq;

/**
 * 
 * 类介绍:用于表示空请求
 *
 * @author zhoubo
 * @version 1.0.0
 * @since 2016年1月21日
 */
public class DummyReq extends GameMessageReq {

	@Override
	public void bytes2Req(ByteBuffer buffer, ByteOrder order) {
	}

	@Override
	public boolean validate() {
		return true;
	}

}
