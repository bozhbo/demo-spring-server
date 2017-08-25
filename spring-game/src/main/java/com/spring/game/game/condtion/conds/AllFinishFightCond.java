package com.snail.webgame.game.condtion.conds;

import com.snail.webgame.game.common.ActionType;
import com.snail.webgame.game.common.ErrorCode;
import com.snail.webgame.game.condtion.AbstractConditionCheck;
import com.snail.webgame.game.condtion.CheckDataChg;
import com.snail.webgame.game.info.QuestInProgressInfo;
import com.snail.webgame.game.info.RoleInfo;
import com.snail.webgame.game.info.RoleLoadInfo;
import com.snail.webgame.game.xml.info.ChallengeBattleXmlInfo.BattleDetail;

public class AllFinishFightCond extends AbstractConditionCheck {

	private int typeNo;// 副本类型编号(0表示任意)
	private int chapterNo;// 章节编号(0表示任意)
	private int battleNo;// 战场编号(0表示任意)

	private int num;
	
	private int isOneKey;// 扫荡是否有效 1-有效

	public AllFinishFightCond() {
	}

	public AllFinishFightCond(int typeNo, int chapterNo, int battleNo, int num, int isOneKey) {
		this.typeNo = typeNo;
		this.chapterNo = chapterNo;
		this.battleNo = battleNo;
		this.num = num;
		this.isOneKey = isOneKey;
	}

	@Override
	public AbstractConditionCheck generate(String[] agrs) {
		if (agrs.length < 6) {
			return null;
		}
		int isOneKey = Integer.parseInt(agrs[1]);
		int typeNo = Integer.parseInt(agrs[2]);
		int chapterNo = Integer.parseInt(agrs[3]);
		int battleNo = Integer.parseInt(agrs[4]);
		int num = Integer.parseInt(agrs[5]);
		return new AllFinishFightCond(typeNo, chapterNo, battleNo, num, isOneKey);
	}
	
	@Override
	public int checkCond(RoleInfo roleInfo, int action,
			CheckDataChg checkDataChg, int index, Object obj) {
		RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
		if (roleLoadInfo != null 
				&& roleLoadInfo.getBattleList() != null && roleLoadInfo.getBattleList().contains(battleNo)) {
			updateValue(index, checkDataChg, 1);
			return 1;
		}
		
		if (action != ActionType.action103.getType() && action != ActionType.action104.getType()) {
			return 0;
		}

		if (obj == null || !(obj instanceof BattleDetail)) {
			return 0;
		}
		
		if (checkDataChg == null) {
			return 0;
		}
		
		QuestInProgressInfo questInProgress = (QuestInProgressInfo) checkDataChg;
		
		// 扫荡无效
		if (action == ActionType.action104.getType() && isOneKey != 1) {
			return 0;
		}

		// 非任意副本
		BattleDetail battleDetail = (BattleDetail) obj;
		int addVal = 1;
		if (action == ActionType.action104.getType()) {
			addVal = battleDetail.getSweepNum();
		}
		
		int value = (int) questInProgress.getSpecValue(index);
		value += addVal;
		if (typeNo <= 0) {// 任意副本类型
			if (value >= num) {
				updateValue(index, questInProgress, num);
				return 1;
			}

			updateValue(index, questInProgress, value);
			return ErrorCode.ROLE_TASK_ERROR_2;
		}

		if (battleDetail.getChapterType() != typeNo) {
			return ErrorCode.ROLE_TASK_ERROR_2;
		}

		if (chapterNo <= 0) {// 任意章节
			if (value >= num) {
				updateValue(index, questInProgress, num);
				return 1;
			}
			updateValue(index, questInProgress, value);
			return ErrorCode.ROLE_TASK_ERROR_2;
		}

		// 非任意章节
		if (battleDetail.getChapterNo() != chapterNo) {
			return ErrorCode.ROLE_TASK_ERROR_2;
		}

		if (battleNo <= 0) {// 任意战场
			if (value >= num) {
				updateValue(index, questInProgress, num);
				return 1;
			}
			updateValue(index, questInProgress, value);
			return ErrorCode.ROLE_TASK_ERROR_2;
		}

		// 非任意战场
		if (battleDetail.getBattleNo() != battleNo) {
			return ErrorCode.ROLE_TASK_ERROR_2;
		}

		if (value >= num) {
			updateValue(index, questInProgress, num);
			return 1;
		}
		updateValue(index, questInProgress, value);
		return ErrorCode.ROLE_TASK_ERROR_2;
	}

}
