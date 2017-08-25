package com.snail.webgame.game.protocal.countryfight.common;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

/**
 * 客户端城市响应消息体
 * 
 * @author xiasd
 *
 */
public class CityRe extends MessageBody{

	private int clubId;
	private String clubName;
	private int countryId;// 1-魏 2-蜀 3-吴
	private int defend;// 耐久
	
	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("clubId", 0);
		ps.addString("clubName", "flashCode", 0);
		ps.add("countryId", 0);
		ps.add("defend", 0);
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

	public int getCountryId() {
		return countryId;
	}

	public void setCountryId(int countryId) {
		this.countryId = countryId;
	}

	public int getDefend() {
		return defend;
	}

	public void setDefend(int defend) {
		this.defend = defend;
	}
//s
}
