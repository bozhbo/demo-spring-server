package com.snail.webgame.game.info.log;

import java.sql.Timestamp;

import com.snail.webgame.engine.common.to.BaseTO;

public class RoleChargeLog extends BaseTO {
	
	public static final int ROLE_CHARGE_EVENT_1 = 18401;// 订单成功创建
	public static final int ROLE_CHARGE_EVENT_2 = 18403;// 订单完成交易（表示交易已经成功，金币到账）
	public static final int ROLE_CHARGE_EVENT_3 = 18409;// 交易完成的订单价格和实际的不符合 （不需要补偿，游戏已根据充值金额给予对应金币）
	public static final int ROLE_CHARGE_EVENT_4 = 18501;// 收到客户端票据信息
	public static final int ROLE_CHARGE_EVENT_5 = 18502;// 订单充值成功（此消息只证明充值成功，还没到账）（此时证明EAI系统中应该有充值记录）
	public static final int ROLE_CHARGE_EVENT_6 = 18503;// 订单充值失败 （充值异常的提示）
	public static final int ROLE_CHARGE_EVENT_7 = 18507;// 极效请求补单消息  （使用绩效平台补单的消息）
			
	private String roleAcc;
	private String roleName;
	private String orderId;// 订单号
	private int chargeType;// 充值类型 pay.xml no
	private int chargeEvent;// 充值事件
	private Timestamp createTime;
	
	public String getRoleAcc() {
		return roleAcc;
	}

	public void setRoleAcc(String roleAcc) {
		this.roleAcc = roleAcc;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public int getChargeType() {
		return chargeType;
	}

	public void setChargeType(int chargeType) {
		this.chargeType = chargeType;
	}

	public int getChargeEvent() {
		return chargeEvent;
	}

	public void setChargeEvent(int chargeEvent) {
		this.chargeEvent = chargeEvent;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	@Override
	public byte getSaveMode() {
		return 0;
	}

}
