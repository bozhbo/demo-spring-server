package com.snail.mina.protocol.info.impl;

import java.nio.ByteOrder;

import org.apache.mina.common.ByteBuffer;

import com.snail.mina.protocol.util.FlashHandleStr;

/**
 * 
 * 类介绍:房间服务器消息头
 *
 * @author zhoubo
 * @since 2014-11-19
 */
public class RoomMessageHead extends BaseMessageHead {
	
	/**
	 * 消息版本号
	 */
	private int versionId;
	
	/**
	 * 验证后的角色ID
	 */
	private int roleId;
	
	/**
	 * 接入服务器ID
	 */
	private int gateId;
	
	/**
	 * 序列号
	 */
	private int sequenceId;
	
	/**
	 * 场景ID
	 */
	private int sceneId;
	
	/**
	 * 消息号
	 */
	private int msgType;
	
	/**
	 * 附带数据
	 */
	private String userState;
	
	public void bytes2Head(ByteBuffer buffer, ByteOrder order) {
		this.versionId = buffer.order(order).getInt();
		this.roleId = buffer.order(order).getInt();
		this.gateId = buffer.order(order).getInt();
		this.sequenceId = buffer.order(order).getInt();
		this.sceneId = buffer.order(order).getInt();
		this.msgType = buffer.order(order).getInt();
		
		this.userState = FlashHandleStr.decodeStringB(buffer);
	}
	
	public void head2Bytes(ByteBuffer buffer, ByteOrder order) {
		buffer.order(order).putInt(versionId);
		buffer.order(order).putInt(roleId);
		buffer.order(order).putInt(gateId);
		buffer.order(order).putInt(sequenceId);
		buffer.order(order).putInt(sceneId);
		buffer.order(order).putInt(msgType);
		
		FlashHandleStr.encodeStringB(userState);
	}
	
	@Override
	public int getMsgType() {
		return msgType;
	}
	
	@Override
	public void setMsgType(int msgType) {
		this.msgType = msgType;
	}
	
	public int getVersionId() {
		return versionId;
	}

	public void setVersionId(int versionId) {
		this.versionId = versionId;
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public int getGateId() {
		return gateId;
	}

	public void setGateId(int gateId) {
		this.gateId = gateId;
	}

	public int getSequenceId() {
		return sequenceId;
	}

	public void setSequenceId(int sequenceId) {
		this.sequenceId = sequenceId;
	}

	public int getSceneId() {
		return sceneId;
	}

	public void setSceneId(int sceneId) {
		this.sceneId = sceneId;
	}

	public String getUserState() {
		return userState;
	}

	public void setUserState(String userState) {
		this.userState = userState;
	}

	
	
}
