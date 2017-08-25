package com.snail.webgame.game.protocal.club.entity;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class RoomIdMsgResp extends MessageBody {
	private long roomId;

	public RoomIdMsgResp(){
		
	}
	
	public RoomIdMsgResp(long roomId){
		this.roomId = roomId;
	}
	
	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("roomId", 0);
	}

	public long getRoomId() {
		return roomId;
	}

	public void setRoomId(long roomId) {
		this.roomId = roomId;
	}

}
