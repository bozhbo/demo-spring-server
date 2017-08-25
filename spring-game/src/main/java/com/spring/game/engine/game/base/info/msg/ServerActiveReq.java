package com.snail.webgame.engine.game.base.info.msg;

import java.nio.ByteOrder;

import org.apache.mina.common.ByteBuffer;

import com.snail.webgame.engine.net.msg.impl.GameMessageReq;


/**
 * 类介绍:服务器激活消息请求Request
 *
 * @author zhoubo
 * @version 1.0.0
 * @since 2016年1月21日
 */
public class ServerActiveReq extends GameMessageReq {

	private String serverName;
	private int flag;
	private String reserve;
	
	@Override
	public void bytes2Req(ByteBuffer buffer, ByteOrder order) {
		this.serverName = getString(buffer, order);
		this.flag = getInt(buffer, order);
		this.reserve = getString(buffer, order);
	}

	public String getServerName() {
		return serverName;
	}

	public int getFlag() {
		return flag;
	}

	public String getReserve() {
		return reserve;
	}

	@Override
	public boolean validate() {
		return true;
	}

	
}
