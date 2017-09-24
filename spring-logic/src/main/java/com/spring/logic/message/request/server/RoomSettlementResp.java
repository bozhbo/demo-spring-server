package com.spring.logic.message.request.server;

import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

import org.apache.mina.common.ByteBuffer;

import com.snail.mina.protocol.info.impl.BaseRoomResp;

/**
 * 房间每局结算
 * 
 * @author Administrator
 *
 */
public class RoomSettlementResp extends BaseRoomResp {

	private int roomId;
	private int count;
	private List<RoomSettlementRes> list = new ArrayList<>();
	
	@Override
	public void bytes2Req(ByteBuffer buffer, ByteOrder order) {
		this.roomId = getInt(buffer, order);
		this.count = getInt(buffer, order);
		
		for (int i = 0; i < count; i++) {
			RoomSettlementRes roomSettlementRes = new RoomSettlementRes();
			roomSettlementRes.bytes2Req(buffer, order);
			list.add(roomSettlementRes);
		}
	}
	
	@Override
	public void resp2Bytes(ByteBuffer buffer, ByteOrder order) {
		setInt(buffer, order, this.roomId);
		setInt(buffer, order, this.count);
		
		for(int i = 0; i < count; i++) {
			RoomSettlementRes roomSettlementRes = list.get(i);
			roomSettlementRes.resp2Bytes(buffer, order);
		}
	}

	public int getRoomId() {
		return roomId;
	}

	public void setRoomId(int roomId) {
		this.roomId = roomId;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public List<RoomSettlementRes> getList() {
		return list;
	}

	public void setList(List<RoomSettlementRes> list) {
		this.list = list;
	}

	
}
