package com.snail.webgame.game.info;

import java.sql.Timestamp;

import com.snail.webgame.engine.common.to.BaseTO;
import com.snail.webgame.game.condtion.CheckDataChg;

public class OpActivityProgressInfo extends BaseTO implements CheckDataChg {

	/**
	 * 活动状态
	 */
	public static final int STATUS_REVEIVE = 0;// 已经领取，未完成
	public static final int STATUS_FINISH = 1;// 已经完成，未领取奖励
	public static final int STATUS_CLEAR = 2;// 已经领取过奖励
	
	private int actId;// 运营活动唯一id
	private int rewardNo;// 当前活动的奖励编号
	private int actVersion;// 活动版本号,版本号修改重置玩家活动信息
	
	/**
	 * 这几个值记录的为活动中间值
	 * 若不需要记录中间值则记录最终完成标记值：1
	 */
	private long value1;// 任务实现值
	private long value2;// 任务实现值
	private long value3;// 任务实现值
	
	private int roleId;// 
	
	private int rewardState;// 奖励标记 1-可领取 2-已领取
	private Timestamp checkTime;// 
	
	// 缓存值
	private boolean versionChg = false;// 运营活动中间值版本是否改变
	
	public long getValue1() {
		return value1;
	}

	public void setValue1(long value1) {
		this.value1 = value1;
	}

	public long getValue2() {
		return value2;
	}

	public void setValue2(long value2) {
		this.value2 = value2;
	}

	public long getValue3() {
		return value3;
	}

	public void setValue3(long value3) {
		this.value3 = value3;
	}

	public boolean isVersionChg() {
		return versionChg;
	}

	public void setVersionChg(boolean versionChg) {
		this.versionChg = versionChg;
	}

	public int getActId() {
		return actId;
	}

	public void setActId(int actId) {
		this.actId = actId;
	}

	public int getRewardNo() {
		return rewardNo;
	}

	public void setRewardNo(int rewardNo) {
		this.rewardNo = rewardNo;
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public int getRewardState() {
		return rewardState;
	}

	public void setRewardState(int rewardState) {
		this.rewardState = rewardState;
	}

	public Timestamp getCheckTime() {
		return checkTime;
	}

	public void setCheckTime(Timestamp checkTime) {
		this.checkTime = checkTime;
	}

	public int getActVersion() {
		return actVersion;
	}

	public void setActVersion(int actVersion) {
		this.actVersion = actVersion;
	}

	@Override
	public long getSpecValue(int index) {
		if (index == 0) {
			return value1;
		} else if (index == 1) {
			return value2;
		} else if (index == 2) {
			return value3;
		}
		return 0;
	}

	@Override
	public void updateInterValue(int index, long value) {
		if (index == 0) {
			value1 = value;
		} else if (index == 1) {
			value2 = value;
		} else if (index == 2) {
			value3 = value;
		}
	}

	@Override
	public byte getSaveMode() {
		return 0;
	}

	@Override
	public void setFinishState() {
		this.setRewardState(STATUS_FINISH);
	}

}
