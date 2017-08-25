package com.snail.webgame.game.info;

import java.sql.Timestamp;

import com.snail.webgame.engine.common.to.BaseTO;
import com.snail.webgame.game.condtion.CheckDataChg;

/**
 * 任务
 * 
 * @author tangjq
 * 
 */
public class QuestInProgressInfo extends BaseTO implements CheckDataChg {

	/**
	 * 任务状态
	 */
	public static final int STATUS_REVEIVE = 1;// 已经领取，未完成
	public static final int STATUS_FINISH = 2;// 已经完成，未领取奖励
	public static final int STATUS_CLEAR = 3;// 已经领取过奖励

	private int roleId;
	private int questProtoNo;// 任务模板id

	/**
	 * 这几个值记录的为任务中间值
	 * 若不需要记录中间值则记录最终完成标记值：1
	 */
	private long value1;// 任务实现值
	private long value2;// 任务实现值
	private long value3;// 任务实现值

	private int status;// 状态
	private Timestamp questGetTime;// 任务领取时间
	private int talkOrder;// 当前任务的对话顺序
	
	// 缓存值
	private boolean versionChg = false;// 任务中间值版本是否改变
	private boolean isEffect = false;// 任务完成是否播特效 

	public QuestInProgressInfo() {

	}

	public QuestInProgressInfo(int roleId, int questProtoNo) {
		this.questProtoNo = questProtoNo;
		this.roleId = roleId;
		this.status = STATUS_REVEIVE;
		this.questGetTime = new Timestamp(System.currentTimeMillis());
	}

	public Timestamp getQuestGetTime() {
		return questGetTime;
	}

	public void setQuestGetTime(Timestamp questGetTime) {
		this.questGetTime = questGetTime;
	}

	public int getQuestProtoNo() {
		return questProtoNo;
	}

	public void setQuestProtoNo(int questProtoNo) {
		this.questProtoNo = questProtoNo;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

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

	public long getCurrQuestGetTime() {
		if (questGetTime != null) {
			return questGetTime.getTime();
		}
		return 0;
	}

	public boolean isVersionChg() {
		return versionChg;
	}

	public void setVersionChg(boolean versionChg) {
		this.versionChg = versionChg;
	}

	public int getTalkOrder() {
		return talkOrder;
	}

	public void setTalkOrder(int talkOrder) {
		this.talkOrder = talkOrder;
	}

	public boolean isEffect() {
		return isEffect;
	}

	public void setEffect(boolean isEffect) {
		this.isEffect = isEffect;
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

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	@Override
	public void setFinishState() {
		this.setStatus(QuestInProgressInfo.STATUS_FINISH);
		this.setEffect(true);
	}

}
