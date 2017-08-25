package com.snail.webgame.game.info;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.snail.webgame.engine.common.to.BaseTO;
import com.snail.webgame.game.xml.cache.MineXMLInfoMap;
import com.snail.webgame.game.xml.info.MineXMLInfo;

public class MinePrize extends BaseTO {

	private int mineId;// id（唯一）
	private int position;// 矿位置编号
	private int mineNo; // 矿类型编号
	private Timestamp mineCreateTime;// 矿生成时间
	private Timestamp mineEndTime;// 矿结束时间

	private int minePointId; // 矿点编号
	private int roleId; // 占领人
	private Timestamp createTime;// 占领开采时间
	private Timestamp endTime = null;// 被抢夺(放弃)时间(空：未被抢夺或放弃)
	private byte endStatus = 0;// 1-被抢夺 2-被放弃
	private byte status = 0;// 0-未领取奖励 1-已领取奖励

	// 协防人<roleId，MineHelpRole>
	private Map<Integer, MineHelpRole> helpRoles = new HashMap<Integer, MineHelpRole>();

	public int getMineId() {
		return mineId;
	}

	public void setMineId(int mineId) {
		this.mineId = mineId;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public int getMineNo() {
		return mineNo;
	}

	public void setMineNo(int mineNo) {
		this.mineNo = mineNo;
	}

	public Timestamp getMineCreateTime() {
		return mineCreateTime;
	}

	public void setMineCreateTime(Timestamp mineCreateTime) {
		this.mineCreateTime = mineCreateTime;
	}

	public Timestamp getMineEndTime() {
		return mineEndTime;
	}

	public void setMineEndTime(Timestamp mineEndTime) {
		this.mineEndTime = mineEndTime;
	}

	public int getMinePointId() {
		return minePointId;
	}

	public void setMinePointId(int minePointId) {
		this.minePointId = minePointId;
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

	public Timestamp getEndTime() {
		return endTime;
	}

	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}
	
	public long getCurrEndTime() {
		if (endTime != null) {
			return endTime.getTime();
		}
		MineXMLInfo xmlInfo = MineXMLInfoMap.getMineXMLInfo(mineNo);
		if (xmlInfo == null) {
			return 0;
		}
		long startTime = createTime.getTime();
		long endTime = mineCreateTime.getTime() + xmlInfo.getMineTime() * 60 * 1000;
		List<Long> helpTimes = new ArrayList<Long>();
		for (MineHelpRole helpRole : helpRoles.values()) {
			long time = helpRole.getCreateTime().getTime();
			if (time >= startTime && time <= endTime) {
				helpTimes.add(time);
			}
		}
		Collections.sort(helpTimes);
		for (int i = 0; i < helpTimes.size(); i++) {
			startTime = helpTimes.get(i);
			if (startTime > endTime) {
				return endTime;
			}
			endTime = startTime + ((endTime - startTime) * (i + 1)) / (i + 2);
		}
		return endTime;
	}

	public byte getEndStatus() {
		return endStatus;
	}

	public void setEndStatus(byte endStatus) {
		this.endStatus = endStatus;
	}

	public byte getStatus() {
		return status;
	}

	public void setStatus(byte status) {
		this.status = status;
	}

	public Map<Integer, MineHelpRole> getHelpRoles() {
		return helpRoles;
	}
	
	public MineHelpRole getHelpRoles(int roleId) {
		return helpRoles.get(roleId);
	}

	public void setHelpRoles(Map<Integer, MineHelpRole> helpRoles) {
		this.helpRoles = helpRoles;
	}

	@Override
	public byte getSaveMode() {
		return ONLINE;
	}
}
