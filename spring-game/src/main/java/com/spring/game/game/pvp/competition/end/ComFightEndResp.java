package com.snail.webgame.game.pvp.competition.end;

import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

import org.apache.mina.common.ByteBuffer;

import com.snail.webgame.engine.component.room.protocol.info.impl.BaseRoomReq;

/**
 * 
 * 类介绍:竞技场PVP结束战斗Response
 *
 * @author zhoubo
 * @2014-11-24
 */
public class ComFightEndResp extends BaseRoomReq{
	private byte fightType; // 1-竞技场PVP 2-地图PVP
	private int count;
	private byte timeoutGameOver;//0.表示正常结束战斗,1.表示本场战斗因超时结束战斗
	private List<ComFightEndRe> list;

	@Override
	public void bytes2Req(ByteBuffer buffer, ByteOrder order) {
		this.fightType = getByte(buffer, order);
		this.count = getInt(buffer, order);
		this.timeoutGameOver = getByte(buffer, order);
		if (count > 0) {
			list = new ArrayList<ComFightEndRe>();
			
			for (int i = 0; i < count; i++) {
				ComFightEndRe comFightEndRe = new ComFightEndRe();
				comFightEndRe.setFightType(fightType);
				comFightEndRe.bytes2Req(buffer, order);
				list.add(comFightEndRe);
			}
		}
	}
	
	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public List<ComFightEndRe> getList() {
		return list;
	}

	public void setList(List<ComFightEndRe> list) {
		this.list = list;
	}

	public byte getFightType() {
		return fightType;
	}

	public void setFightType(byte fightType) {
		this.fightType = fightType;
	}

	public byte getTimeoutGameOver() {
		return timeoutGameOver;
	}

	public void setTimeoutGameOver(byte timeoutGameOver) {
		this.timeoutGameOver = timeoutGameOver;
	}
	
}
