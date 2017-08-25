package com.snail.webgame.game.protocal.countryfight.fight.createbattle;

import java.nio.ByteOrder;
import java.util.List;

import org.apache.mina.common.ByteBuffer;

import com.snail.webgame.engine.component.room.protocol.info.impl.BaseRoomResp;
import com.snail.webgame.game.protocal.countryfight.common.ClubInfoVo;

/**
 * 创建国战战场
 * 
 * @author xiasd
 *
 */
public class CreateStateFightReq extends BaseRoomResp {
	
	private int cityId;//城市Id
	private String cityName;//城市名称
	private int clubCount;//公会列表大小
	private List<ClubInfoVo> clubList;//公会信息
	
	@Override
	public void resp2Bytes(ByteBuffer buffer, ByteOrder order) {
		setInt(buffer, order, cityId);
		setString(buffer, order, cityName);
		setInt(buffer, order, clubCount);

		if (clubList != null && clubList.size() > 0) {
			for (ClubInfoVo clubInfo : clubList) {
				clubInfo.resp2Bytes(buffer, order);
			}
		}
	}

	public int getCityId() {
		return cityId;
	}

	public void setCityId(int cityId) {
		this.cityId = cityId;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public int getClubCount() {
		return clubCount;
	}

	public void setClubCount(int clubCount) {
		this.clubCount = clubCount;
	}

	public List<ClubInfoVo> getClubList() {
		return clubList;
	}

	public void setClubList(List<ClubInfoVo> clubList) {
		this.clubList = clubList;
	}
}
