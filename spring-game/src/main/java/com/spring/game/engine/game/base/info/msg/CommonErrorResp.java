package com.snail.webgame.engine.game.base.info.msg;

import java.nio.ByteOrder;

import org.apache.mina.common.ByteBuffer;

import com.snail.webgame.engine.net.msg.impl.GameMessageResp;

/**
 * 
 * 类介绍:通用错误码
 * 
 * @author zhoubo
 * @version 1.0.0
 * @since 2016年1月21日
 */
public class CommonErrorResp extends GameMessageResp {
	
	private int result;

	@Override
	public void resp2Bytes(ByteBuffer buffer, ByteOrder order) {
		setInt(buffer, order, this.result);
		
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	
}
