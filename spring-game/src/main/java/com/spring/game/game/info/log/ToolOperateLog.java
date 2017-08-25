package com.snail.webgame.game.info.log;

import java.sql.Timestamp;

import com.snail.webgame.engine.common.to.BaseTO;

/**
 * 记录极效操作日志
 * @author caowl
 */
public class ToolOperateLog extends BaseTO {

	private String msgType; // 执行方法命令
	private String guid; // GUID
	private String targetRoleName; // 目标角色名
	private String targetRoleAcc; // 目标角色账号
	private String xmlIdStr; // gameConfig sid 修改XML用到
	private String msgContent; // 操作内容
	private String operator; // 操作者账号
	private Timestamp operateTime; // 操作时间
	private String reserve; // 保留字段
	private boolean success;


	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public String getTargetRoleName() {
		return targetRoleName;
	}

	public void setTargetRoleName(String targetRoleName) {
		this.targetRoleName = targetRoleName;
	}

	public String getTargetRoleAcc() {
		return targetRoleAcc;
	}

	public void setTargetRoleAcc(String targetRoleAcc) {
		this.targetRoleAcc = targetRoleAcc;
	}

	public String getXmlIdStr() {
		return xmlIdStr;
	}

	public void setXmlIdStr(String xmlIdStr) {
		this.xmlIdStr = xmlIdStr;
	}

	public String getMsgContent() {
		return msgContent;
	}

	public void setMsgContent(String msgContent) {
		this.msgContent = msgContent;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getReserve() {
		return reserve;
	}

	public void setReserve(String reserve) {
		this.reserve = reserve;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	@Override
	public byte getSaveMode() {
		return ONLINE;
	}

	public Timestamp getOperateTime() {
		return operateTime;
	}

	public void setOperateTime(Timestamp operateTime) {
		this.operateTime = operateTime;
	}
}
