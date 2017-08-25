package com.snail.webgame.game.pvp.competition.end;

import java.nio.ByteOrder;

import org.apache.mina.common.ByteBuffer;

import com.snail.webgame.engine.component.room.protocol.info.impl.BaseRoomResp;

/**
 * 
 * 类介绍:竞技场PVP战斗结束传输对象
 *
 * @author zhoubo
 * @2014-12-8
 */
public class EndCompetitionVo extends BaseRoomResp {

	private String targetNickName;//对方昵称
	private int targetLevel;// 对方等级
	private byte targetStage; //对方段位
	
	@Override
	public void bytes2Req(ByteBuffer buffer, ByteOrder order) {
		this.targetNickName = getString(buffer, order);
		this.targetLevel = getInt(buffer, order);
		this.targetStage = getByte(buffer, order);
	}
	
	@Override
	public void resp2Bytes(ByteBuffer buffer, ByteOrder order) {
		setString(buffer, order, targetNickName);
		setInt(buffer, order, targetLevel);
		setByte(buffer, order, targetStage);
		
	}

	public String getTargetNickName() {
		return targetNickName;
	}

	public void setTargetNickName(String targetNickName) {
		this.targetNickName = targetNickName;
	}

	public int getTargetLevel() {
		return targetLevel;
	}

	public void setTargetLevel(int targetLevel) {
		this.targetLevel = targetLevel;
	}

	public byte getTargetStage() {
		return targetStage;
	}

	public void setTargetStage(byte targetStage) {
		this.targetStage = targetStage;
	}
	
	
}
