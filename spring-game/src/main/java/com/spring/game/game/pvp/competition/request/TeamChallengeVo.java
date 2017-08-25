package com.snail.webgame.game.pvp.competition.request;

import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

import org.apache.mina.common.ByteBuffer;

import com.snail.webgame.engine.component.room.protocol.info.impl.BaseRoomResp;

/**
 * 
 * 类介绍:组队副本匹配VO
 *
 * @author zhoubo
 * @2015年10月8日
 */
public class TeamChallengeVo extends BaseRoomResp {

	private int duplicatId; // 副本ID	
	private int count;
	private List<ComFightRequestReq> list;	// 包含队友信息
	private byte isSameServer; // 1-是同服
	private long costTime;// 战斗花费多久时间
	
	@Override
	public void bytes2Req(ByteBuffer buffer, ByteOrder order) {
		this.duplicatId = getInt(buffer, order);
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
		this.isSameServer = getByte(buffer, order);
		this.costTime = getLong(buffer, order);
	}
	
	@Override
	public void resp2Bytes(ByteBuffer buffer, ByteOrder order) {
		setInt(buffer, order, this.duplicatId);
		setInt(buffer, order, this.count);
		
		if (this.count > 0) {
			for (ComFightRequestReq fightRequestReq : list) {
				fightRequestReq.resp2Bytes(buffer, order);
			}
		}
		setByte(buffer, order, this.isSameServer);
		setLong(buffer, order, this.costTime);
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

	public int getDuplicatId() {
		return duplicatId;
	}

	public void setDuplicatId(int duplicatId) {
		this.duplicatId = duplicatId;
	}

	public byte getIsSameServer() {
		return isSameServer;
	}

	public void setIsSameServer(byte isSameServer) {
		this.isSameServer = isSameServer;
	}

	public long getCostTime() {
		return costTime;
	}

	public void setCostTime(long costTime) {
		this.costTime = costTime;
	}
	
}
