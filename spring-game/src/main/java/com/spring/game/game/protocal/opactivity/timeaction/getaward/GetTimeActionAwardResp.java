package com.snail.webgame.game.protocal.opactivity.timeaction.getaward;

import java.util.List;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

import com.snail.webgame.game.protocal.recruit.recruit.ChestItemRe;

public class GetTimeActionAwardResp extends MessageBody {

	private int result;
	private int actId;// 已开启的活动id
	private byte rewardNo;//

	private int remainNum;// 当前抽奖剩余次数必中

	// 抽到的奖励
	private int count;
	private List<ChestItemRe> itemList;

	private int index;// 多选一时选择的物品位置(0开始)

	protected void setSequnce(ProtocolSequence ps) {
		ps.add("result", 0);
		ps.add("actId", 0);
		ps.add("rewardNo", 0);
		ps.add("remainNum", 0);
		ps.add("count", 0);
		ps.addObjectArray("itemList", "com.snail.webgame.game.protocal.recruit.recruit.ChestItemRe", "count");

		ps.add("index", 0);
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public int getActId() {
		return actId;
	}

	public void setActId(int actId) {
		this.actId = actId;
	}

	public byte getRewardNo() {
		return rewardNo;
	}

	public void setRewardNo(byte rewardNo) {
		this.rewardNo = rewardNo;
	}

	public int getRemainNum() {
		return remainNum;
	}

	public void setRemainNum(int remainNum) {
		this.remainNum = remainNum;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public List<ChestItemRe> getItemList() {
		return itemList;
	}

	public void setItemList(List<ChestItemRe> itemList) {
		this.itemList = itemList;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

}
