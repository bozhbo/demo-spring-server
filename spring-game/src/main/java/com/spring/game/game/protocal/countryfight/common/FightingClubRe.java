package com.snail.webgame.game.protocal.countryfight.common;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

/**
 * 城池战斗信息
 * 
 * @author xiasd
 *
 */
public class FightingClubRe extends MessageBody{

	private String attackClubName;// 进攻工会
	private String defendClubName;// 防守工会
	private String cityName;// 城池名
	private int isAttackWin;// 1-胜利 else-失败
	
	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.addString("attackClubName", "flashCode", 0);
		ps.addString("defendClubName", "flashCode", 0);
		ps.addString("cityName", "flashCode", 0);
		ps.add("isAttackWin", 0);
	}

	public String getAttackClubName() {
		return attackClubName;
	}

	public void setAttackClubName(String attackClubName) {
		this.attackClubName = attackClubName;
	}

	public String getDefendClubName() {
		return defendClubName;
	}

	public void setDefendClubName(String defendClubName) {
		this.defendClubName = defendClubName;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public int getIsAttackWin() {
		return isAttackWin;
	}

	public void setIsAttackWin(int isAttackWin) {
		this.isAttackWin = isAttackWin;
	}
	
}
