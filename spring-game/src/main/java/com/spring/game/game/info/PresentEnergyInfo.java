package com.snail.webgame.game.info;

import java.sql.Timestamp;

import com.snail.webgame.engine.common.to.BaseTO;

public class PresentEnergyInfo extends BaseTO {
	private int roleId;
	private int relRoleId; // 赠送者Id
	private Timestamp presentDate; // 赠送时间

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public int getRelRoleId() {
		return relRoleId;
	}

	public void setRelRoleId(int relRoleId) {
		this.relRoleId = relRoleId;
	}

	public Timestamp getPresentDate() {
		return presentDate;
	}

	public void setPresentDate(Timestamp presentDate) {
		this.presentDate = presentDate;
	}

	@Override
	public byte getSaveMode() {
		return 0;
	}

}
