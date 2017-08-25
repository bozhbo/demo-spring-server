package com.snail.webgame.game.info;

import java.sql.Timestamp;

import com.snail.webgame.engine.common.to.BaseTO;
import com.snail.webgame.game.protocal.app.common.EChargeState;

/**
 * 用于保存玩家充值时的订单号及商品id
 * 
 * @author nijp
 *
 */
public class RoleChargeInfo extends BaseTO {

	private long accId;// 
	private String orderStr;// 订单号
	private int roleId;
	private int itemId;// 商品id
	private Timestamp chargeTime;// 
	private long seqId;// 极效补单表里的主键
	private boolean isNeedDel = true;// 是否需要删除
	private EChargeState state;
	private int errorCode;
	private int reTryTimes;// 重试次数
	
	// 购买充值宝箱时使用
	private String itemStr;// 道具奖励

	public String getOrderStr() {
		return orderStr;
	}

	public void setOrderStr(String orderStr) {
		this.orderStr = orderStr;
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public int getItemId() {
		return itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	public Timestamp getChargeTime() {
		return chargeTime;
	}

	public void setChargeTime(Timestamp chargeTime) {
		this.chargeTime = chargeTime;
	}

	public String getItemStr() {
		return itemStr;
	}

	public void setItemStr(String itemStr) {
		this.itemStr = itemStr;
	}

	public long getAccId() {
		return accId;
	}

	public void setAccId(long accId) {
		this.accId = accId;
	}

	public long getSeqId() {
		return seqId;
	}

	public void setSeqId(long seqId) {
		this.seqId = seqId;
	}

	public boolean isNeedDel() {
		return isNeedDel;
	}

	public void setNeedDel(boolean isNeedDel) {
		this.isNeedDel = isNeedDel;
	}

	public EChargeState getState() {
		return state;
	}

	public void setState(EChargeState state) {
		this.state = state;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public int getReTryTimes() {
		return reTryTimes;
	}

	public void setReTryTimes(int reTryTimes) {
		this.reTryTimes = reTryTimes;
	}

	@Override
	public byte getSaveMode() {
		return 0;
	}

}
