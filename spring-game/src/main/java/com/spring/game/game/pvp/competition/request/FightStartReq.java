package com.snail.webgame.game.pvp.competition.request;


import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

import org.apache.mina.common.ByteBuffer;

import com.snail.webgame.engine.component.room.protocol.info.impl.BaseRoomResp;

/**
 * 
 * 类介绍:战斗开始请求，发往战斗服务器
 *
 * @author zhoubo
 * @2014-11-26
 */
public class FightStartReq extends BaseRoomResp {
	private byte fightType; // 1-竞技场PVP 2-地图PVP 3-组队PVP 4-劫镖
	private int count;
	private List<ComFightRequestReq> list;
	
	@Override
	public void resp2Bytes(ByteBuffer buffer, ByteOrder order) {
		setByte(buffer, order, this.fightType);
		setInt(buffer, order, this.count);
		
		if (list != null && list.size() > 0) {
			for (ComFightRequestReq comFightRequestReq : list) {
				comFightRequestReq.resp2Bytes(buffer, order);
			}
		}
	}
	
	@Override
	public void bytes2Req(ByteBuffer buffer, ByteOrder order) {
		this.fightType = getByte(buffer, order);
		this.count = getInt(buffer, order);
		
		if (count > 0) {
			list = new ArrayList<ComFightRequestReq>();
			
			for (int i = 0; i < count; i++) {
				ComFightRequestReq fightRequestReq = new ComFightRequestReq();
				fightRequestReq.setMsgType(this.getMsgType());
				fightRequestReq.bytes2Req(buffer, order);
				list.add(fightRequestReq);
			}
		}
	}

	public byte getFightType() {
		return fightType;
	}

	public void setFightType(byte fightType) {
		this.fightType = fightType;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public List<ComFightRequestReq> getList() {
		return list;
	}

	public void setList(List<ComFightRequestReq> list) {
		this.list = list;
	}

	
}