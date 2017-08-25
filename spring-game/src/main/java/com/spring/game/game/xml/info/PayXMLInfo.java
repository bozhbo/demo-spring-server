package com.snail.webgame.game.xml.info;

import com.snail.webgame.game.common.ActionType;

public class PayXMLInfo {
	
	/**
	 * 充值大类型 1：金子，2：会员卡 3：礼包
	 */
	public static final int PAY_TYPE_COIN = 1;
	public static final int PAY_TYPE_CARD = 2;
	public static final int PAY_TYPE_BOX = 3;
	
	/**
	 * 会员卡小类型 对应配置中的No字段
	 */
	public static final int PAY_SMALL_TYPE_1 = 1;// 月卡
	public static final int PAY_SMALL_TYPE_2 = 2;// 季卡
	public static final int PAY_SMALL_TYPE_3 = 3;// 年卡
	public static final int PAY_SMALL_TYPE_4 = 4;// 月卡升级到季卡
	public static final int PAY_SMALL_TYPE_5 = 5;// 月卡升级到年卡
	public static final int PAY_SMALL_TYPE_6 = 6;// 季卡升级到年卡
	public static final int PAY_SMALL_TYPE_7 = 7;// 福利卡
	
	private int no;
	private String pid;
	private String name;
	private int payType;// 购买类型 1：金子，2：会员卡 3：礼包
	private int moneyCost;// 需要支付的价格
	private int giftReward;// 每次赠送
	private int firstReward;// 首次赠送奖励
	
	// 该字段购买会员卡时使用
	private int effectDay;// 有效天数 

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public int getPayType() {
		return payType;
	}

	public void setPayType(int payType) {
		this.payType = payType;
	}

	public int getMoneyCost() {
		return moneyCost;
	}

	public void setMoneyCost(int moneyCost) {
		this.moneyCost = moneyCost;
	}

	public int getGiftReward() {
		return giftReward;
	}

	public void setGiftReward(int giftReward) {
		this.giftReward = giftReward;
	}

	public int getFirstReward() {
		return firstReward;
	}

	public void setFirstReward(int firstReward) {
		this.firstReward = firstReward;
	}

	public int getEffectDay() {
		return effectDay;
	}

	public void setEffectDay(int effectDay) {
		this.effectDay = effectDay;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 根据编号获取购买会员卡action
	 * 
	 * @param payNo
	 * @return
	 */
	public static int fetchCardAction(int payNo) {
		int action = 0;
		switch (payNo) {
		case PAY_SMALL_TYPE_1:
			action = ActionType.action439.getType();
			break;
		case PAY_SMALL_TYPE_2:
			action = ActionType.action442.getType();
			break;
		case PAY_SMALL_TYPE_3:
			action = ActionType.action444.getType();
			break;
		case PAY_SMALL_TYPE_4:
			action = ActionType.action440.getType();
			break;
		case PAY_SMALL_TYPE_5:
			action = ActionType.action441.getType();
			break;
		case PAY_SMALL_TYPE_6:
			action = ActionType.action443.getType();
			break;
		case PAY_SMALL_TYPE_7:
			action = ActionType.action445.getType();
			break;

		default:
			break;
		}
		
		return action;
	}
}
