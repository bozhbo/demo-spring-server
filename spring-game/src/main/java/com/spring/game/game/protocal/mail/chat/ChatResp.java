package com.snail.webgame.game.protocal.mail.chat;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class ChatResp extends MessageBody {
	private int result;
	private int msgType;// 1-世界 2-公会 3-滚动栏 4-国家
	private long sendRoleId;// 发送人
	private String sendRoleName;// 发送人名称
	private byte sendRace;// 发送人的国家
	private int sendMainHeroNo;// 发送人的主武将编号
	private long recRoleId;// 接收人
	private String recRoleName;// 接收人
	private String msgContent;// 聊天内容
	private long sendTime;// 聊天时间
	private byte vipLevel;// 0-无1-白银2-黄金3钻石
	private String defendStr;// 参数（聊天操作）

	protected void setSequnce(ProtocolSequence ps) {
		ps.add("result", 0);
		ps.add("msgType", 0);
		ps.add("sendRoleId", 0);
		ps.addString("sendRoleName", "flashCode", 0);
		ps.add("sendRace", 0);
		ps.add("sendMainHeroNo", 0);
		ps.add("recRoleId", 0);
		ps.addString("recRoleName", "flashCode", 0);
		ps.addString("msgContent", "flashCode", 0);
		ps.add("sendTime", 0);
		ps.add("vipLevel", 0);
		ps.addString("defendStr", "flashCode", 0);
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public int getMsgType() {
		return msgType;
	}

	public void setMsgType(int msgType) {
		this.msgType = msgType;
	}

	public String getSendRoleName() {
		return sendRoleName;
	}

	public void setSendRoleName(String sendRoleName) {
		this.sendRoleName = sendRoleName;
	}

	public long getSendRoleId() {
		return sendRoleId;
	}

	public void setSendRoleId(long sendRoleId) {
		this.sendRoleId = sendRoleId;
	}

	public long getRecRoleId() {
		return recRoleId;
	}

	public void setRecRoleId(long recRoleId) {
		this.recRoleId = recRoleId;
	}

	public String getRecRoleName() {
		return recRoleName;
	}

	public void setRecRoleName(String recRoleName) {
		this.recRoleName = recRoleName;
	}

	public String getMsgContent() {
		return msgContent;
	}

	public void setMsgContent(String msgContent) {
		this.msgContent = msgContent;
	}

	public long getSendTime() {
		return sendTime;
	}

	public void setSendTime(long sendTime) {
		this.sendTime = sendTime;
	}

	public byte getVipLevel() {
		return vipLevel;
	}

	public void setVipLevel(byte vipLevel) {
		this.vipLevel = vipLevel;
	}

	public byte getSendRace() {
		return sendRace;
	}

	public void setSendRace(byte sendRace) {
		this.sendRace = sendRace;
	}

	public int getSendMainHeroNo() {
		return sendMainHeroNo;
	}

	public void setSendMainHeroNo(int sendMainHeroNo) {
		this.sendMainHeroNo = sendMainHeroNo;
	}

	public String getDefendStr() {
		return defendStr;
	}

	public void setDefendStr(String defendStr) {
		this.defendStr = defendStr;
	}
}
