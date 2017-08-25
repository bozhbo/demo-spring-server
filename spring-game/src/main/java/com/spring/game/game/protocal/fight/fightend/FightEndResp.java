package com.snail.webgame.game.protocal.fight.fightend;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

import com.snail.webgame.game.protocal.item.service.GetDropData;

public class FightEndResp extends MessageBody {

	private int result;
	private long fightId;// 战斗编号(添加缓存时自动分配赋值)
	private int fightType;// 战斗类型
	private String param;// 副本保留参数 challengeTypeNo,chapterNo,battleId
	// 评星
	private String starStr;
	private String starNo;
	// 获得奖励
	private int prizeNum;
	private List<BattlePrize> prize;

	// 翻牌获得奖励（第一个为获得奖励）
	private int fpPrizeNum;
	private List<BattlePrize> fpPrize;
	
	private int refreshShop; //0-未刷新 1-刷新黑市商店 2-刷新异域商店 3-两个商店都刷新
	
	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("result", 0);
		ps.add("fightType", 0);
		ps.addString("param", "flashCode", 0);
		ps.addString("starStr", "flashCode", 0);
		ps.addString("starNo", "flashCode", 0);
		ps.add("prizeNum", 0);
		ps.addObjectArray("prize", "com.snail.webgame.game.protocal.fight.fightend.BattlePrize", "prizeNum");
		ps.add("fpPrizeNum", 0);
		ps.addObjectArray("fpPrize", "com.snail.webgame.game.protocal.fight.fightend.BattlePrize", "fpPrizeNum");
		ps.add("refreshShop", 0);
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public long getFightId() {
		return fightId;
	}

	public void setFightId(long fightId) {
		this.fightId = fightId;
	}

	public int getFightType() {
		return fightType;
	}

	public void setFightType(int fightType) {
		this.fightType = fightType;
	}

	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}

	public String getStarStr() {
		return starStr;
	}

	public void setStarStr(String starStr) {
		this.starStr = starStr;
	}

	public int getPrizeNum() {
		return prizeNum;
	}

	public void setPrizeNum(int prizeNum) {
		this.prizeNum = prizeNum;
	}

	public List<BattlePrize> getPrize() {
		return prize;
	}

	public void setPrize(List<BattlePrize> prize) {
		this.prize = prize;
	}

	public String getStarNo() {
		return starNo;
	}

	public void setStarNo(String starNo) {
		this.starNo = starNo;
	}

	public int getFpPrizeNum() {
		return fpPrizeNum;
	}

	public void setFpPrizeNum(int fpPrizeNum) {
		this.fpPrizeNum = fpPrizeNum;
	}

	public List<BattlePrize> getFpPrize() {
		return fpPrize;
	}
	

	public int getRefreshShop() {
		return refreshShop;
	}

	public void setRefreshShop(int refreshShop) {
		this.refreshShop = refreshShop;
	}

	public void setFpPrize(List<BattlePrize> fpPrize) {
		this.fpPrize = fpPrize;
	}

	/**
	 * 评星
	 * 
	 * @param stars
	 */
	public String addStar(List<Integer> stars) {

		if (stars == null || stars.size() <= 0) {
			return "";
		}
		int starno1 = stars.get(0);
		int starno2 = stars.get(1);
		int starno3 = stars.get(2);

		starNo = starno1 + "," + starno2 + "," + starno3;
		stars.remove(0);
		stars.remove(0);
		stars.remove(0);

		starStr = "";
		if (stars != null && stars.size() > 0) {
			for (int star : stars) {
				starStr += star + ",";
			}
			starStr = starStr.substring(0, starStr.length() - 1);
		}
		return starStr;
	}

	/**
	 * 获取奖励
	 * 
	 * @param getPropMap
	 */
	public void addPrize(Map<String, GetDropData> getPropMap) {
		if (getPropMap == null || getPropMap.size() <= 0) {
			return;
		}

		prize = new ArrayList<BattlePrize>();

		for (GetDropData prizeData : getPropMap.values()) {

			BattlePrize prizeInfo = new BattlePrize();
			prizeInfo.setNo(prizeData.getNo() + "");
			prizeInfo.setNum(prizeData.getNum());
			prize.add(prizeInfo);
		}

		prizeNum = prize.size();
	}
}
