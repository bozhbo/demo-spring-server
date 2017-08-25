package com.snail.webgame.game.protocal.opactivity.timeaction.querysingle;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class TimeActionRewardRe extends MessageBody {

	private byte rewardNo;// 奖励编号
	private String rewardName;
	private String rewardItems;// 奖励道具
	private int curVal;// 当前进度
	private int actTargetVal;// 活动目标值
	private byte isGet;// 0-不可领取 1-可领取 2-已领取
	private byte isShowPro;// 是否显示进度 0-显示 1-不显示

	private byte multiple;// 0-不够选 1-勾选多选一

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("rewardNo", 0);
		ps.addString("rewardName", "flashCode", 0);
		ps.addString("rewardItems", "flashCode", 0);
		ps.add("curVal", 0);
		ps.add("actTargetVal", 0);
		ps.add("isGet", 0);
		ps.add("isShowPro", 0);
		ps.add("multiple", 0);
	}

	public byte getRewardNo() {
		return rewardNo;
	}

	public void setRewardNo(byte rewardNo) {
		this.rewardNo = rewardNo;
	}

	public String getRewardName() {
		return rewardName;
	}

	public void setRewardName(String rewardName) {
		this.rewardName = rewardName;
	}

	public String getRewardItems() {
		return rewardItems;
	}

	public void setRewardItems(String rewardItems) {
		this.rewardItems = rewardItems;
	}

	public byte getIsGet() {
		return isGet;
	}

	public void setIsGet(byte isGet) {
		this.isGet = isGet;
	}

	public int getCurVal() {
		return curVal;
	}

	public void setCurVal(int curVal) {
		this.curVal = curVal;
	}

	public int getActTargetVal() {
		return actTargetVal;
	}

	public void setActTargetVal(int actTargetVal) {
		this.actTargetVal = actTargetVal;
	}

	public byte getIsShowPro() {
		return isShowPro;
	}

	public void setIsShowPro(byte isShowPro) {
		this.isShowPro = isShowPro;
	}

	public byte getMultiple() {
		return multiple;
	}

	public void setMultiple(byte multiple) {
		this.multiple = multiple;
	}

}
