package com.snail.webgame.game.info;

import java.sql.Timestamp;
import java.util.List;

import com.snail.webgame.engine.common.to.BaseTO;
import com.snail.webgame.game.common.util.DateUtil;
import com.snail.webgame.game.xml.cache.PlayXMLInfoMap;
import com.snail.webgame.game.xml.info.PlayXMLInfo;

/**
 * 宝石活动
 * @author zenggang
 */
public class FightGemInfo extends BaseTO {

	private int roleId;// 用户编号

	private int fightNum;// 战斗次数
	private int resetNum;// 当日重置次数
	private Timestamp lastResetTime;// 最后重置时间(重置时间)

	private int buyNum;// 当日购买次数
	private int buyResetNum;// 当日购买重置次数
	private Timestamp lastBuyTime;// 最后购买可重置次数时间

	private int lastFightBattleNo;// 最后战斗关卡编号
	private int lastFightResult;// 1-胜 2-败

	private int maxFightBattleNo;// 历史通过最高关卡
	private List<Integer> prizeBattleNos;// 领取奖励的关卡

	public int getFightNum() {
		return fightNum;
	}

	public void setFightNum(int fightNum) {
		this.fightNum = fightNum;
	}

	public int getResetNum() {
		return resetNum;
	}

	public void setResetNum(int resetNum) {
		this.resetNum = resetNum;
	}

	public Timestamp getLastResetTime() {
		return lastResetTime;
	}

	public void setLastResetTime(Timestamp lastResetTime) {
		this.lastResetTime = lastResetTime;
	}

	public int getBuyNum() {
		return buyNum;
	}

	public void setBuyNum(int buyNum) {
		this.buyNum = buyNum;
	}

	public int getBuyResetNum() {
		return buyResetNum;
	}

	public void setBuyResetNum(int buyResetNum) {
		this.buyResetNum = buyResetNum;
	}

	public Timestamp getLastBuyTime() {
		return lastBuyTime;
	}

	public void setLastBuyTime(Timestamp lastBuyTime) {
		this.lastBuyTime = lastBuyTime;
	}

	public int getLastFightBattleNo() {
		return lastFightBattleNo;
	}

	public void setLastFightBattleNo(int lastFightBattleNo) {
		this.lastFightBattleNo = lastFightBattleNo;
	}

	public int getLastFightResult() {
		return lastFightResult;
	}

	public void setLastFightResult(int lastFightResult) {
		this.lastFightResult = lastFightResult;
	}

	public int getMaxFightBattleNo() {
		return maxFightBattleNo;
	}

	public void setMaxFightBattleNo(int maxFightBattleNo) {
		this.maxFightBattleNo = maxFightBattleNo;
	}

	public List<Integer> getPrizeBattleNos() {
		return prizeBattleNos;
	}

	public void setPrizeBattleNos(List<Integer> prizeBattleNos) {
		this.prizeBattleNos = prizeBattleNos;
	}

	/**
	 * 获取当日重置次数
	 * @return
	 */
	public int getCurrResetNum() {
		if (lastResetTime != null && DateUtil.isSameDay(System.currentTimeMillis(), lastResetTime.getTime())) {
			return resetNum;
		}
		return 0;
	}

	/**
	 * 获取当日购买可重置次数
	 * @return
	 */
	public int getCurrBuyNum() {
		if (lastBuyTime != null && DateUtil.isSameDay(System.currentTimeMillis(), lastBuyTime.getTime())) {
			return buyNum;
		}
		return 0;
	}

	/**
	 * 购买可重置次数
	 * @return
	 */
	public int getCurrBuyResetNum() {
		if (lastBuyTime != null && DateUtil.isSameDay(System.currentTimeMillis(), lastBuyTime.getTime())) {
			return buyResetNum;
		}
		return 0;
	}

	/**
	 * 获取当日可重置次数上限
	 * @return
	 */
	public int getCurrResetLimit() {
		PlayXMLInfo info = PlayXMLInfoMap.getPlayXMLInfo(PlayXMLInfo.PLAY_TYPE_1);
		if (info != null) {
			return getCurrBuyResetNum() + info.getResetTimes();
		}
		return getCurrBuyResetNum();
	}

	public byte getSaveMode() {
		return ONLINE;
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}
}
