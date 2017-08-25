package com.snail.webgame.game.protocal.scene.info;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

/**
 * 大R信息
 * @author luwd
 */
public class SuperRInfo extends MessageBody {
	private int superRoleId;
	private int rankNo;// 排名
	private String heroName;
	private int heroNo;
	private String equipNos;
	private int showPlanId;// 1-显示套装  0-显示时装
	private int rideNo;

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("superRoleId", 0);
		ps.add("rankNo", 0);
		ps.addString("heroName", "flashCode", 0);
		ps.add("heroNo", 0);
		ps.addString("equipNos", "flashCode", 0);
		ps.add("showPlanId", 0);
		ps.add("rideNo", 0);
	}

	public int getSuperRoleId() {
		return superRoleId;
	}

	public void setSuperRoleId(int superRoleId) {
		this.superRoleId = superRoleId;
	}

	public int getRankNo() {
		return rankNo;
	}

	public void setRankNo(int rankNo) {
		this.rankNo = rankNo;
	}

	public String getHeroName() {
		return heroName;
	}

	public void setHeroName(String heroName) {
		this.heroName = heroName;
	}

	public int getHeroNo() {
		return heroNo;
	}

	public void setHeroNo(int heroNo) {
		this.heroNo = heroNo;
	}

	public String getEquipNos() {
		return equipNos;
	}

	public void setEquipNos(String equipNos) {
		this.equipNos = equipNos;
	}

	public int getShowPlanId() {
		return showPlanId;
	}

	public void setShowPlanId(int showPlanId) {
		this.showPlanId = showPlanId;
	}

	public int getRideNo() {
		return rideNo;
	}

	public void setRideNo(int rideNo) {
		this.rideNo = rideNo;
	}
	
	
}
