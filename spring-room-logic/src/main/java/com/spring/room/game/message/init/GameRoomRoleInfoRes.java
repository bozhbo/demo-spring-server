package com.spring.room.game.message.init;

import java.nio.ByteOrder;

import org.apache.mina.common.ByteBuffer;

import com.spring.logic.message.request.room.RoomJoinResp;

public class GameRoomRoleInfoRes extends RoomJoinResp {

	private int gold;
	private int vipLevel;
	private String header;
	
	@Override
	public void resp2Bytes(ByteBuffer buffer, ByteOrder order) {
		super.resp2Bytes(buffer, order);
		setInt(buffer, order, gold);
		setInt(buffer, order, vipLevel);
		setString(buffer, order, header);
		
	}

	public int getGold() {
		return gold;
	}

	public void setGold(int gold) {
		this.gold = gold;
	}

	public int getVipLevel() {
		return vipLevel;
	}

	public void setVipLevel(int vipLevel) {
		this.vipLevel = vipLevel;
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}
	
	
	
	
}
