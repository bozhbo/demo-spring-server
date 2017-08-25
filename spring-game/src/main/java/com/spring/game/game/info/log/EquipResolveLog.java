package com.snail.webgame.game.info.log;

import java.sql.Timestamp;

import com.snail.webgame.engine.common.to.BaseTO;

public class EquipResolveLog extends BaseTO {
	private int roleId;
	private String account;
	private String roleName;
	private String resolveEquips; // id:equipNo;id:equipNo;(id为数据库主键Id equipNo为 // equip.xml的No)
	private String addItems;// itemNo:num;itemNo:num
	private int type; // 0 - 熔炼 1 - 重铸 2 - 星石分解
	private Timestamp time;

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getResolveEquips() {
		return resolveEquips;
	}

	public void setResolveEquips(String resolveEquips) {
		this.resolveEquips = resolveEquips;
	}

	public String getAddItems() {
		return addItems;
	}

	public void setAddItems(String addItems) {
		this.addItems = addItems;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Timestamp getTime() {
		return time;
	}

	public void setTime(Timestamp time) {
		this.time = time;
	}

	@Override
	public byte getSaveMode() {
		return 0;
	}

}
