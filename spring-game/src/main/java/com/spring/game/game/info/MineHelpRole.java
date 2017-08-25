package com.snail.webgame.game.info;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

/**
 * 协防信息
 * @author zenggang
 */
public class MineHelpRole {
	private byte helpPos;// 协防位置
	private int roleId;// 协防人
	private Timestamp createTime;// 协防开始时间
	private int status = 0;// 0-未领取奖励 1-已领取奖励

	// 防守阵形<pos,heroId>
	private Map<Integer, Integer> heroMap = new HashMap<Integer, Integer>();

	public byte getHelpPos() {
		return helpPos;
	}

	public void setHelpPos(byte helpPos) {
		this.helpPos = helpPos;
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Map<Integer, Integer> getHeroMap() {
		return heroMap;
	}

	public void setHeroMap(Map<Integer, Integer> heroMap) {
		this.heroMap = heroMap;
	}
}
