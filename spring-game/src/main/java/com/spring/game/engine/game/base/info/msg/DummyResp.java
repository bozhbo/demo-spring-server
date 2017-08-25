package com.snail.webgame.engine.game.base.info.msg;

import java.nio.ByteOrder;

import org.apache.mina.common.ByteBuffer;

import com.snail.webgame.engine.net.msg.impl.GameMessageResp;

/**
 * 
 * 类介绍:用于表示空返回,不会被写到客户端
 *
 * @author zhoubo
 * @version 1.0.0
 * @since 2016年1月21日
 */
public class DummyResp extends GameMessageResp {

	@Override
	public void resp2Bytes(ByteBuffer buffer, ByteOrder order) {
		// TODO Auto-generated method stub
		
	}

}
