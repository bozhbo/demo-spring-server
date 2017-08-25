package com.snail.webgame.game.protocal.rolemgt.disconnect;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;
/**
 * 该请求消息来自：
 * 1、客户端（主动断开与服务器的连接） 
 * 2、接入服务器（检测到客户端异常则通知游戏服务器该客户端断开）
 * @author leiqiang
 *
 */
public class DisconnectReq extends MessageBody {

	private int result;//断开原因
	private int roleId;
	private byte disconnectPhase;//1-暂时断开 2-暂时断开后在规定时间内重连上 3-暂时断开后超过规定时间 4-直接断开
	private String account;
	
	protected void setSequnce(ProtocolSequence ps) {
		 
		ps.add("result", 0);
		ps.add("roleId", 0);
		ps.add("disconnectPhase", 0);
		ps.addString("account", "flashCode", 0);
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public byte getDisconnectPhase() {
		return disconnectPhase;
	}

	public void setDisconnectPhase(byte disconnectPhase) {
		this.disconnectPhase = disconnectPhase;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}
	
}
