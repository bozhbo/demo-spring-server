package com.snail.webgame.game.protocal.app.common;

public enum EChargeState {
	
	/**
	 * 准备发往中转服
	 */
	PREPARE_SEND_TO_TRANSIT_SERVER((byte)1),
	/**
	 * 发往中转服异常
	 */
	EXCEPTION_SEND_TO_TRANSIT_SERVER((byte)2),
	/**
	 * 发往中转服失败
	 */
	FAILED_SEND_TO_TRANSIT_SERVER((byte)3),
	/**
	 * 已发中转服 
	 */
	HAS_SEND_TO_TRANSIT_SERVER((byte)4),
	/**
	 * 收到中转服ERRORCODE消息
	 */
	RECEIVE_FROM_TRANSIT_SERVER_ERRORCODE((byte)5),
	/**
	 * 订单处理成功，等待计费发金币
	 */
	RECEIVE_FROM_TRANSIT_SERVER_RESULT_IS_1((byte)6),
	/**
	 * 补单成功
	 */
	ORDER_SUCCESSED((byte)7);
	
	private byte value;

	private EChargeState(byte value) {
		this.value = value;
	}

	public byte getValue() {
		return value;
	}
}
