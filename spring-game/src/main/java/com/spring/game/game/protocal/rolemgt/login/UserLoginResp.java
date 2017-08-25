package com.snail.webgame.game.protocal.rolemgt.login;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class UserLoginResp extends MessageBody {

	private int result;// 1-成功 2-成功（本地没有用户，需创建）3-成功(新创建的用的，需要引导)
	private int roleId;// 角色Id
	private String account;// 玩家登陆帐号
	private String roleName;// 角色名称
	private byte roleRace;// 国家势力
	private short gateServerId;// gate服务器Id
	private byte punishStatus;// 角色禁言状态: 0-正常 1-禁言 2-冻结
	private long punishTime;// 禁言结束时间
	private short guildId;// 工会Id
	private byte vipLevel;// vip等级
	private byte isAdvert;// 是否代言人 1-是

	protected void setSequnce(ProtocolSequence ps) {
		ps.add("result", 0);
		ps.add("roleId", 0);
		ps.addString("account", "flashCode", 0);
		ps.addString("roleName", "flashCode", 0);
		ps.add("roleRace", 0);
		ps.add("gateServerId", 0);
		ps.add("punishStatus", 0);
		ps.add("punishTime", 0);
		ps.add("guildId", 0);
		ps.add("vipLevel", 0);
		ps.add("isAdvert", 0);
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

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public byte getRoleRace() {
		return roleRace;
	}

	public void setRoleRace(byte roleRace) {
		this.roleRace = roleRace;
	}

	public short getGateServerId() {
		return gateServerId;
	}

	public void setGateServerId(short gateServerId) {
		this.gateServerId = gateServerId;
	}

	public byte getPunishStatus() {
		return punishStatus;
	}

	public void setPunishStatus(byte punishStatus) {
		this.punishStatus = punishStatus;
	}

	public long getPunishTime() {
		return punishTime;
	}

	public void setPunishTime(long punishTime) {
		this.punishTime = punishTime;
	}

	public short getGuildId() {
		return guildId;
	}

	public void setGuildId(short guildId) {
		this.guildId = guildId;
	}

	public byte getVipLevel() {
		return vipLevel;
	}

	public void setVipLevel(byte vipLevel) {
		this.vipLevel = vipLevel;
	}

	public byte getIsAdvert() {
		return isAdvert;
	}

	public void setIsAdvert(byte isAdvert) {
		this.isAdvert = isAdvert;
	}
}
