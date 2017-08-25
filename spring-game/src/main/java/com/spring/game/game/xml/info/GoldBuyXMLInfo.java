package com.snail.webgame.game.xml.info;

import java.util.Map;

public class GoldBuyXMLInfo {

	public static final byte TYPE_MONEY_BUY = 1;// 购买银子
	public static final byte TYPE_SP_BUY = 2;// 购买体力
	public static final byte TYPE_TECH_BUY = 3;// 购买科技点
	public static final byte TYPE_ENERGY_BUY = 4;// 购买精力
	public static final byte TYPE_MINE_BUY = 5;// 购买资源开采抢夺次数

	private int no;// 编号
	private int type;// 1:暴击概率受等级影响 2:暴击概率不受等级影响
	private int fixed;// 0:固定次数 1:无限次数，超过最后一次后价格不递增，按照最后一次的价格来

	private int maxBuyNum = 0;// 最大购买次数
	private Map<Integer, GoldBuyXMLPro> pros;

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getFixed() {
		return fixed;
	}

	public void setFixed(int fixed) {
		this.fixed = fixed;
	}

	public int getMaxBuyNum() {
		return maxBuyNum;
	}

	public void setMaxBuyNum(int maxBuyNum) {
		this.maxBuyNum = maxBuyNum;
	}

	public Map<Integer, GoldBuyXMLPro> getPros() {
		return pros;
	}
	
	public GoldBuyXMLPro getGoldBuyXMLPro(int times){
		return pros.get(times);
	}

	public void setPros(Map<Integer, GoldBuyXMLPro> pros) {
		this.pros = pros;
	}

	public GoldBuyXMLPro getMaxBuyXMLPro() {
		return pros.get(maxBuyNum);
	}
}
