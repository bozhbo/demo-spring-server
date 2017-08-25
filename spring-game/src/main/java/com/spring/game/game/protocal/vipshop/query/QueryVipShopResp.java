package com.snail.webgame.game.protocal.vipshop.query;

import java.util.List;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class QueryVipShopResp extends MessageBody {

	private int result;
	private byte cardType;// 1-月卡 2-季卡 3-年卡
	private byte isBuyExtra;// 是否购买额外福利卡 1-已购买
	private byte vipLv;// 当前vip等级
	private int totalCharge;// 总的充值
	private int totalVipExp;// 总的vipExp
	private String getVipGiftStr;// 已领取vip礼包字符串
	private String firstChargeSaleNoStr;// 记录已首次购买金子时买一送一 格式：商品id,商品id
	private int count;
	List<ChargeBoxRe> boxList;

	protected void setSequnce(ProtocolSequence ps) {
		ps.add("result", 0);
		ps.add("cardType", 0);
		ps.add("isBuyExtra", 0);
		ps.add("vipLv", 0);
		ps.add("totalCharge", 0);
		ps.add("totalVipExp", 0);
		ps.addString("getVipGiftStr", "flashCode", 0);
		ps.addString("firstChargeSaleNoStr", "flashCode", 0);
		ps.add("count", 0);
		ps.addObjectArray("boxList", "com.snail.webgame.game.protocal.vipshop.query.ChargeBoxRe", "count");
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public byte getCardType() {
		return cardType;
	}

	public void setCardType(byte cardType) {
		this.cardType = cardType;
	}

	public byte getIsBuyExtra() {
		return isBuyExtra;
	}

	public void setIsBuyExtra(byte isBuyExtra) {
		this.isBuyExtra = isBuyExtra;
	}

	public byte getVipLv() {
		return vipLv;
	}

	public void setVipLv(byte vipLv) {
		this.vipLv = vipLv;
	}

	public int getTotalVipExp() {
		return totalVipExp;
	}

	public void setTotalVipExp(int totalVipExp) {
		this.totalVipExp = totalVipExp;
	}

	public String getGetVipGiftStr() {
		return getVipGiftStr;
	}

	public void setGetVipGiftStr(String getVipGiftStr) {
		this.getVipGiftStr = getVipGiftStr;
	}

	public String getFirstChargeSaleNoStr() {
		return firstChargeSaleNoStr;
	}

	public void setFirstChargeSaleNoStr(String firstChargeSaleNoStr) {
		this.firstChargeSaleNoStr = firstChargeSaleNoStr;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public List<ChargeBoxRe> getBoxList() {
		return boxList;
	}

	public void setBoxList(List<ChargeBoxRe> boxList) {
		this.boxList = boxList;
	}

	public int getTotalCharge() {
		return totalCharge;
	}

	public void setTotalCharge(int totalCharge) {
		this.totalCharge = totalCharge;
	}

}
