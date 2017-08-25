package com.snail.webgame.game.info;

import java.sql.Timestamp;
import java.util.Map;

import com.snail.webgame.engine.common.to.BaseTO;
import com.snail.webgame.game.cache.RoleInfoMap;
import com.snail.webgame.game.common.util.DateUtil;
import com.snail.webgame.game.common.xml.cache.VipXMLInfoMap;
import com.snail.webgame.game.common.xml.info.VipType;
import com.snail.webgame.game.xml.cache.CampaignXMLInfoMap;
import com.snail.webgame.game.xml.info.CampaignXMLInfo;

/**
 * 宝物活动信息
 * @author zenggang
 */
public class FightCampaignInfo extends BaseTO {

	private int roleId;// 用户编号

	private int reviceNum;// 复活次数

	private int resetNum;// 当日重置次数
	private Timestamp lastResetTime;// 最后重置时间(重置时间)

	private int buyNum;// 当日购买次数
	private int buyResetNum;// 当日购买重置次数
	private Timestamp lastBuyTime;// 最后购买可重置次数时间

	private int lastFightBattleNo;// 最后战斗关卡编号
	private int lastFightResult;// 1-胜 2-败

	// 战死残血和上阵的英雄信息<heroId , info>
	private Map<Integer, FightCampaignHero> heroMap;

	// 关卡镜像信息
	// <battleNo,FightCampaignBattle>
	private Map<Integer, FightCampaignBattle> battleMap;

	private int hisFightBattleNo;// 最后战斗关卡编号(扫荡用)
	private int hisFightResult;// 1-胜 2-败(扫荡用)

	public int getReviceNum() {
		return reviceNum;
	}

	public void setReviceNum(int reviceNum) {
		this.reviceNum = reviceNum;
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

	public Map<Integer, FightCampaignBattle> getBattleMap() {
		return battleMap;
	}

	public void setBattleMap(Map<Integer, FightCampaignBattle> battleMap) {
		this.battleMap = battleMap;
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
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo != null) {
			int resetLimit = VipXMLInfoMap.getVipVal(roleInfo.getVipLv(), VipType.GCLD_NUM);
			return getCurrBuyResetNum() + resetLimit;
		}
		return getCurrBuyResetNum();
	}

	public int getCurrLastFightBattleNo() {
		CampaignXMLInfo info = CampaignXMLInfoMap.getCampaignXMLInfo(CampaignXMLInfo.CAMPAIGN_TYPE_1);
		if (lastFightResult == 0) {
			if (info != null) {
				return info.getFristBattleNo();
			}
		} else if (lastFightResult == 1) {
			if (info != null && lastFightBattleNo != info.getLastBattleNo()
					&& info.getBattles().containsKey(lastFightBattleNo + 1)) {
				return lastFightBattleNo + 1;
			}

			if (info != null && lastFightBattleNo == info.getLastBattleNo()
					&& info.getBattles().containsKey(lastFightBattleNo)) {
				return -1;// -1:全部通关
			}
		}
		return lastFightBattleNo;
	}
	
	public int getCurrHisFightBattleNo() {
		CampaignXMLInfo info = CampaignXMLInfoMap.getCampaignXMLInfo(CampaignXMLInfo.CAMPAIGN_TYPE_1);
		if (hisFightResult == 0) {
			if (info != null) {
				return 0;// 一关未通
			}
		} else if (hisFightResult == 2) {
			if (info != null && hisFightBattleNo == info.getFristBattleNo()) {
				return 0;// 一关未通
			}
			if (info != null && hisFightBattleNo != info.getLastBattleNo()
					&& info.getBattles().containsKey(hisFightBattleNo-1)) {
				return hisFightBattleNo -1;
			}
		}
		return hisFightBattleNo;
	}
	
	@Override
	public byte getSaveMode() {
		return ONLINE;
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public Map<Integer, FightCampaignHero> getHeroMap() {
		return heroMap;
	}

	public void setHeroMap(Map<Integer, FightCampaignHero> heroMap) {
		this.heroMap = heroMap;
	}

	public int getHisFightBattleNo() {
		return hisFightBattleNo;
	}

	public void setHisFightBattleNo(int hisFightBattleNo) {
		this.hisFightBattleNo = hisFightBattleNo;
	}

	public int getHisFightResult() {
		return hisFightResult;
	}

	public void setHisFightResult(int hisFightResult) {
		this.hisFightResult = hisFightResult;
	}
}
