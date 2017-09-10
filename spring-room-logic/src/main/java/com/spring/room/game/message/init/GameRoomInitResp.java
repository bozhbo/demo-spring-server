package com.spring.room.game.message.init;

import java.nio.ByteOrder;
import java.util.List;

import org.apache.mina.common.ByteBuffer;

import com.spring.logic.message.request.room.RoomInitResp;

public class GameRoomInitResp extends RoomInitResp {
	
	private int count;
	private List<GameRoomRoleInfoRes> list;
	
	@Override
	public void resp2Bytes(ByteBuffer buffer, ByteOrder order) {
		super.resp2Bytes(buffer, order);
		
		for (int i = 0; i < count; i++) {
			list.get(i).resp2Bytes(buffer, order);
		}
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public List<GameRoomRoleInfoRes> getList() {
		return list;
	}

	public void setList(List<GameRoomRoleInfoRes> list) {
		this.list = list;
	}
	
	
}
