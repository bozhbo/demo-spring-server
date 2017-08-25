package com.snail.webgame.game.protocal.mail.chat;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class PersonalChatResp extends MessageBody {

	private int result;
	private long roleId;// 发送人
	private String roleName;// 发送人
	private byte sendRace;// 发送人的国家
	private int sendMainHeroNo;// 发送人的主武将编号
	private String msgContent;// 聊天内容
	private long chatTime;// 聊天时间
	private long recRoleId;// 接收人
	private String recRoleName;// 接收人
	private int vipLv; // 对方VIP等级
	private String defendStr;// 参数（聊天操作）

	protected void setSequnce(ProtocolSequence ps) {
		ps.add("result", 0);
		ps.add("roleId", 0);
		ps.addString("roleName", "flashCode", 0);
		ps.add("sendRace", 0);
		ps.add("sendMainHeroNo", 0);
		ps.addString("msgContent", "flashCode", 0);
		ps.add("chatTime", 0);
		ps.add("recRoleId", 0);
		ps.addString("recRoleName", "flashCode", 0);
		ps.add("vipLv", 0);
		ps.addString("defendStr", "flashCode", 0);
	}

	public int getVipLv() {
		return vipLv;
	}

	public void setVipLv(int vipLv) {
		this.vipLv = vipLv;
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public long getRoleId() {
		return roleId;
	}

	public void setRoleId(long roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getMsgContent() {
		return msgContent;
	}

	public void setMsgContent(String msgContent) {
		this.msgContent = msgContent;
	}

	public long getChatTime() {
		return chatTime;
	}

	public void setChatTime(long chatTime) {
		this.chatTime = chatTime;
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
