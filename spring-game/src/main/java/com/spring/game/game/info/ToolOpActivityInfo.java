package com.snail.webgame.game.info;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.snail.webgame.engine.common.to.BaseTO;
import com.snail.webgame.game.xml.info.RecruitItemXMLInfo;

public class ToolOpActivityInfo extends BaseTO {

	/**
	 * 运营活动大类型 0:玩家领取奖励类型；1:双倍竞技场类型；2:双倍金币生存副本类型；3:限时武神类型
	 */
	public static final int OP_ACT_TYPE_0 = 0;
	public static final int OP_ACT_TYPE_1 = 1;
	public static final int OP_ACT_TYPE_2 = 2;
	public static final int OP_ACT_TYPE_3 = 3;
	
	private int showId;// 运营的显示id 这个值每个活动都是唯一的,不可重复 (用于极效全服更新时使用)
	private String actNo;// 活动唯一guid(版本修改： guid已废弃, 现这个字段的值就是showId, 用于保证活动在游戏内的唯一性) 
	private int actType;// 活动类型 0:玩家领取奖励类型；1:双倍竞技场类型；2:双倍金币生存副本类型；3:限时武神类型
	private String actName;// 活动name
	private String actIntroduce;// 活动说明
	private int actState;// 活动开关 1-开启
	private int actVersion;// 活动版本号,版本号修改对应活动玩家数据清零
	private Timestamp startTime;// 活动开启时间
	private Timestamp endTime;// 活动结束时间
	
	private int lotPrice;// 抽奖价格
	private int lotHitNum;// 必送的抽奖次数
	private String lotHeroStr;// 抽奖主武将字符串  heroId:概率
	private String lotRewardStr;// 抽奖奖励字符串 ID:数量:概率;ID:数量:概率
	
	// 抽卡用 缓存
	private int actHeroNo;// 本期活动武将编号
	private List<RecruitItemXMLInfo> list = new ArrayList<RecruitItemXMLInfo>();
	
	// 解析时使用
	private Map<Integer, ToolOpActivityRewardInfo> rewardMap;
	
	public String getActNo() {
		return actNo;
	}

	public void setActNo(String actNo) {
		this.actNo = actNo;
	}

	public int getActType() {
		return actType;
	}

	public void setActType(int actType) {
		this.actType = actType;
	}

	public String getActName() {
		return actName;
	}

	public void setActName(String actName) {
		this.actName = actName;
	}

	public String getActIntroduce() {
		return actIntroduce;
	}

	public void setActIntroduce(String actIntroduce) {
		this.actIntroduce = actIntroduce;
	}

	public int getActState() {
		return actState;
	}

	public void setActState(int actState) {
		this.actState = actState;
	}

	public Timestamp getStartTime() {
		return startTime;
	}

	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}

	public Timestamp getEndTime() {
		return endTime;
	}

	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}

	public int getLotPrice() {
		return lotPrice;
	}

	public void setLotPrice(int lotPrice) {
		this.lotPrice = lotPrice;
	}

	public int getLotHitNum() {
		return lotHitNum;
	}

	public void setLotHitNum(int lotHitNum) {
		this.lotHitNum = lotHitNum;
	}

	public String getLotHeroStr() {
		return lotHeroStr;
	}

	public void setLotHeroStr(String lotHeroStr) {
		this.lotHeroStr = lotHeroStr;
	}

	public String getLotRewardStr() {
		return lotRewardStr;
	}

	public void setLotRewardStr(String lotRewardStr) {
		this.lotRewardStr = lotRewardStr;
	}

	public int getActHeroNo() {
		return actHeroNo;
	}

	public void setActHeroNo(int actHeroNo) {
		this.actHeroNo = actHeroNo;
	}

	public List<RecruitItemXMLInfo> getList() {
		return list;
	}

	public void setList(List<RecruitItemXMLInfo> list) {
		this.list = list;
	}

	public Map<Integer, ToolOpActivityRewardInfo> getRewardMap() {
		return rewardMap;
	}

	public void setRewardMap(Map<Integer, ToolOpActivityRewardInfo> rewardMap) {
		this.rewardMap = rewardMap;
	}

	public int getShowId() {
		return showId;
	}

	public void setShowId(int showId) {
		this.showId = showId;
	}

	public int getActVersion() {
		return actVersion;
	}

	public void setActVersion(int actVersion) {
		this.actVersion = actVersion;
	}

	@Override
	public byte getSaveMode() {
		return 0;
	}

}
