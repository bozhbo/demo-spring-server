package com.snail.webgame.game.protocal.countryfight.common;

import java.nio.ByteOrder;

import org.apache.mina.common.ByteBuffer;

import com.snail.webgame.engine.component.room.protocol.info.impl.BaseRoomResp;

/**
 * 
 * @author qiuhd
 * @since  2015年9月17日
 * @version V1.0.0
 */
public class ClubInfoVo extends BaseRoomResp{
	
	private int clubId;//公会id
	private String clubName;//公会名称
	private byte flag;// 1.表示经过方 2.表示防守方
	
	@Override
	public void resp2Bytes(ByteBuffer buffer, ByteOrder order) {
		setInt(buffer, order, clubId);
		setString(buffer, order, clubName);
		setByte(buffer, order, flag);
	}
	
	public int getClubId() {
		return clubId;
	}

	public void setClubId(int clubId) {
		this.clubId = clubId;
	}

	public String getClubName() {
		return clubName;
	}

	public void setClubName(String clubName) {
		this.clubName = clubName;
	}

	public byte getFlag() {
		return flag;
	}

	public void setFlag(byte flag) {
		this.flag = flag;
	}
}
