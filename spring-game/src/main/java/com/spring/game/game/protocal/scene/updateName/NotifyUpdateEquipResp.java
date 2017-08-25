package com.snail.webgame.game.protocal.scene.updateName;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class NotifyUpdateEquipResp extends MessageBody {

	private long roleId;
	private int heroNo;// 主武将编号
	private String equipNos;//AVATER装备编号
	private int showPlanId;//1-显示套装  0-显示时装

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("roleId", 0);
		ps.add("heroNo", 0);
		ps.addString("equipNos", "flashCode", 0);
		ps.add("showPlanId", 0);
	}

	public long getRoleId() {
		return roleId;
	}

	public void setRoleId(long roleId) {
		this.roleId = roleId;
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
}
