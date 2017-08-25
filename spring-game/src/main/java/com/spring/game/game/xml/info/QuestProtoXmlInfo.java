package com.snail.webgame.game.xml.info;

import java.util.List;

import com.snail.webgame.game.condtion.AbstractConditionCheck;
import com.snail.webgame.game.condtion.conds.FinishTaskCond;
import com.snail.webgame.game.condtion.conds.RoleLvRangCond;
import com.snail.webgame.game.condtion.conds.ShowFuliCardCond;
import com.snail.webgame.game.condtion.conds.ShowTimeCardCond;
import com.snail.webgame.game.condtion.conds.TimeCond;

/**
 * 任务xml
 * @author tangjq
 */
public class QuestProtoXmlInfo {

	/**
	 * 任务类型
	 */
	public static final int QUEST_TYPE_ACHI = 1;// 成就
	public static final int QUEST_TYPE_DAILY = 2;// 日常
	public static final int QUEST_TYPE_NORMAL = 3;// 主线
	public static final int QUEST_TYPE_RACE = 4;// 国家任务
	public static final int QUEST_TYPE_BRANCH = 5;// 支线任务
	public static final int QUEST_TYPE_RUN = 6;// 跑环任务
	public static final int QUEST_TYPE_CARD = 7;// 会员卡、福利卡任务
	public static final int QUEST_TYPE_ACTIVE = 8;// 活跃度任务

	private int questProtoNo;// 任务编号
	private String questName;// 任务名称
	private int questType;// 任务类型 1-成就 2-日常 3-主线 4-国家任务 5-支线任务 6-跑环任务
	private List<AbstractConditionCheck> questConds;// 任务领取条件
	private List<AbstractConditionCheck> finishConds;// 任务完成条件
	private String finishCondStr;
	private String prizeNo;// 奖励 对应PropBag.xml
	private int showNpc;
	
	private int activeVal;// 完成任务获得活跃度
	private int needCost;// 一键秒任务金币消耗
	
	private int oneKeyItemNo;// 一键秒任务消耗道具
	private int oneKeyItemNum;// 一键秒任务道具数量

	public int getQuestProtoNo() {
		return questProtoNo;
	}

	public void setQuestProtoNo(int questProtoNo) {
		this.questProtoNo = questProtoNo;
	}

	public String getQuestName() {
		return questName;
	}

	public void setQuestName(String questName) {
		this.questName = questName;
	}

	public int getQuestType() {
		return questType;
	}

	public void setQuestType(int questType) {
		this.questType = questType;
	}

	public List<AbstractConditionCheck> getQuestConds() {
		return questConds;
	}

	public void setQuestConds(List<AbstractConditionCheck> questConds) {
		this.questConds = questConds;
	}

	public List<AbstractConditionCheck> getFinishConds() {
		return finishConds;
	}

	public void setFinishConds(List<AbstractConditionCheck> finishConds) {
		this.finishConds = finishConds;
	}

	public String getPrizeNo() {
		return prizeNo;
	}

	public void setPrizeNo(String prizeNo) {
		this.prizeNo = prizeNo;
	}

	public int getShowNpc() {
		return showNpc;
	}

	public void setShowNpc(int showNpc) {
		this.showNpc = showNpc;
	}

	public boolean isShowCheckCond() {
		if (questConds != null) {
			for (AbstractConditionCheck condCheck : questConds) {
				if (condCheck instanceof TimeCond 
						|| condCheck instanceof ShowFuliCardCond 
						|| condCheck instanceof ShowTimeCardCond
						|| condCheck instanceof RoleLvRangCond) {
					return true;
				}
			}
		}

		return false;
	}

	public boolean isFinishTaskCond() {
		if (questConds != null) {
			for (AbstractConditionCheck condCheck : questConds) {
				if (condCheck instanceof FinishTaskCond) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	public boolean isRoleLvRangCond() {
		if (questConds != null) {
			for (AbstractConditionCheck condCheck : questConds) {
				if (condCheck instanceof RoleLvRangCond) {
					return true;
				}
			}
		}
		
		return false;
	}

	public String getFinishCondStr() {
		return finishCondStr;
	}

	public void setFinishCondStr(String finishCondStr) {
		this.finishCondStr = finishCondStr;
	}

	public int getActiveVal() {
		return activeVal;
	}

	public void setActiveVal(int activeVal) {
		this.activeVal = activeVal;
	}

	public int getNeedCost() {
		return needCost;
	}

	public void setNeedCost(int needCost) {
		this.needCost = needCost;
	}

	public int getOneKeyItemNo() {
		return oneKeyItemNo;
	}

	public void setOneKeyItemNo(int oneKeyItemNo) {
		this.oneKeyItemNo = oneKeyItemNo;
	}

	public int getOneKeyItemNum() {
		return oneKeyItemNum;
	}

	public void setOneKeyItemNum(int oneKeyItemNum) {
		this.oneKeyItemNum = oneKeyItemNum;
	}
	
}
