package com.snail.webgame.engine.game.module.trade.msg;

import java.nio.ByteOrder;

import org.apache.mina.common.ByteBuffer;

import com.snail.webgame.engine.net.msg.impl.GameMessageResp;

public class TempBuyRoleItemResp extends GameMessageResp {
	
	private int result;
	private int reduceMoney;
	private int itemId;

	@Override
	public void resp2Bytes(ByteBuffer buffer, ByteOrder order) {
		setInt(buffer, order, result);
		
		if (result == 1) {
			setInt(buffer, order, reduceMoney);
			setInt(buffer, order, itemId);
		}
		
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public int getReduceMoney() {
		return reduceMoney;
	}

	public void setReduceMoney(int reduceMoney) {
		this.reduceMoney = reduceMoney;
	}

	public int getItemId() {
		return itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	
}
