package com.snail.webgame.game.info;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;

import com.snail.webgame.engine.common.to.BaseTO;
import com.snail.webgame.game.common.fightdata.DropInfo;
import com.snail.webgame.game.condtion.AbstractConditionCheck;
import com.snail.webgame.game.condtion.conds.OpActFinishFbCond;
import com.snail.webgame.game.condtion.conds.OpActLoginCond;
import com.snail.webgame.game.condtion.conds.OpActPayDaysCond;

public class ToolOpActivityRewardInfo extends BaseTO {

	private int opActId;// 运营活动id 对应ToolOpActivityInfo对象中的id
	private int rewardNo;
	private String rewardName;
	private String rewardItems;// 奖励道具
	private int goalVal;// 当前目标值
	private String rewardCond;// 奖励条件 login pay sdpay paydays

	private String dateStr;// 检测日期天数字符串 sdpay 格式：2015-06-17 ,paydays 格式：2
	private byte multiple;// 0-不够选 1-勾选多选一

	/**
	 * 当前需要达成的条件检测
	 */
	private List<AbstractConditionCheck> rewardConds = new ArrayList<AbstractConditionCheck>();

	public int getOpActId() {
		return opActId;
	}

	public void setOpActId(int opActId) {
		this.opActId = opActId;
	}

	public int getRewardNo() {
		return rewardNo;
	}

	public void setRewardNo(int rewardNo) {
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

	public Map<String, Integer> getRewardItemMap() {
		Map<String, Integer> result = new HashMap<String, Integer>();
		String[] tempArr = rewardItems.split(";");
		for (String itemStr : tempArr) {
			String[] subArr = itemStr.split(":");
			if (subArr.length >= 2) {
				String itemNo = subArr[1].trim();
				int itemNum = NumberUtils.toInt(subArr[0]);
				if (result.containsKey(itemNo)) {
					result.put(itemNo, itemNum + result.get(itemNo));
				} else {
					result.put(itemNo, itemNum);
				}
			}
		}
		return result;
	}

	public List<DropInfo> getDropInfoList() {
		List<DropInfo> itemList = new ArrayList<DropInfo>();
		String[] tempArr = rewardItems.split(";");
		for (String itemStr : tempArr) {
			String[] subArr = itemStr.split(":");
			if (subArr.length >= 2) {
				String itemNo = subArr[1].trim();
				int itemNum = NumberUtils.toInt(subArr[0]);
				itemList.add(new DropInfo(itemNo, itemNum));
			}
		}
		return itemList;
	}

	public List<DropInfo> getDropInfoList(int index) {		
		String[] tempArr = rewardItems.split(";");
		if (tempArr.length > index && index >= 0) {
			String[] subArr = tempArr[index].split(":");
			if (subArr.length >= 2) {
				String itemNo = subArr[1].trim();
				int itemNum = NumberUtils.toInt(subArr[0]);
				List<DropInfo> itemList = new ArrayList<DropInfo>();
				itemList.add(new DropInfo(itemNo, itemNum));
				return itemList;
			}
		}
		return null;
	}

	public String getRewardCond() {
		return rewardCond;
	}

	public void setRewardCond(String rewardCond) {
		this.rewardCond = rewardCond;
	}

	public List<AbstractConditionCheck> getRewardConds() {
		return rewardConds;
	}

	public void setRewardConds(List<AbstractConditionCheck> rewardConds) {
		this.rewardConds = rewardConds;
	}

	public int getGoalVal() {
		return goalVal;
	}

	public void setGoalVal(int goalVal) {
		this.goalVal = goalVal;
	}

	public String getDateStr() {
		return dateStr;
	}

	public void setDateStr(String dateStr) {
		this.dateStr = dateStr;
	}

	public boolean isLoginCond() {
		if (!rewardConds.isEmpty()) {
			for (AbstractConditionCheck check : rewardConds) {
				if (check instanceof OpActLoginCond) {
					return true;
				}
			}
		}

		return false;
	}

	public boolean isShowCond() {
		if (!rewardConds.isEmpty()) {
			for (AbstractConditionCheck check : rewardConds) {
				if (check instanceof OpActFinishFbCond || check instanceof OpActPayDaysCond) {
					return true;
				}
			}
		}

		return false;
	}

	public boolean isSpecCond() {
		if (rewardCond != null) {
			if (rewardCond.equalsIgnoreCase("sdpay") || rewardCond.equalsIgnoreCase("paydays")) {
				return true;
			}
		}

		return false;
	}

	public byte getMultiple() {
		return multiple;
	}

	public void setMultiple(byte multiple) {
		this.multiple = multiple;
	}

	@Override
	public byte getSaveMode() {
		return 0;
	}

}
