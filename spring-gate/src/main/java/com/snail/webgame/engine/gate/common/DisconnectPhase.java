package com.snail.webgame.engine.gate.common;

/**
 * 客户端连接断开阶段类型值 <b> 1-临时断开 2-临时断开重连成功 3-临时断开超时 4-断开 </b>
 * 
 * @author leiqiang
 *
 */
public class DisconnectPhase {

	private byte phaseValue;
	/**
	 * 临时断开
	 */
	public static DisconnectPhase DISCONNECT_TEMP = new DisconnectPhase((byte) 1);
	/**
	 * 临时断开重连成功
	 */
	public static DisconnectPhase DISCONNECT_RECONNECT_OK = new DisconnectPhase((byte) 2);
	/**
	 * 临时断开超时
	 */
	public static DisconnectPhase DISCONNECT_TIMEOUT = new DisconnectPhase((byte) 3);
	/**
	 * 临时断开未超时
	 */
	public static DisconnectPhase DISCONNECT_NOT_TIMEOUT = new DisconnectPhase((byte) 31);
	/**
	 * 断开
	 */
	public static DisconnectPhase DISCONNECT = new DisconnectPhase((byte) 4);

	public DisconnectPhase(byte phaseValue) {
		this.phaseValue = phaseValue;
	}

	public byte getPhaseValue() {
		return phaseValue;
	}
}
