package com.snail.webgame.game.protocal.rolemgt.create;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class CreateRoleReq extends MessageBody {

	private int IP;// 角色IP
	private String account;// 玩家登陆帐号
	private String roleName;// 角色名称
	private int roleSex;// 用户性别 0-男 1-女
	private int roleRace;// 国家势力
	private int heroNo;// 英雄编号
	private int clientType;// 客户端类型 1:android 2:ios
	private String mac;//客户端mac地址
	private String packageName;//客户端包名

	protected void setSequnce(ProtocolSequence ps) {
		ps.add("IP", 0);
		ps.addString("account", "flashCode", 0);
		ps.addString("roleName", "flashCode", 0);
		ps.add("roleSex", 0);
		ps.add("roleRace", 0);
		ps.add("heroNo", 0);
		ps.add("clientType", 0);
		ps.addString("mac", "flashCode", 0);
		ps.addString("packageName", "flashCode", 0);
	}

	public int getIP() {
		return IP;
	}

	public void setIP(int iP) {
		IP = iP;
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

	public int getRoleSex() {
		return roleSex;
	}

	public void setRoleSex(int roleSex) {
		this.roleSex = roleSex;
	}

	public int getRoleRace() {
		return roleRace;
	}

	public void setRoleRace(int roleRace) {
		this.roleRace = roleRace;
	}

	public int getHeroNo() {
		return heroNo;
	}

	public void setHeroNo(int heroNo) {
		this.heroNo = heroNo;
	}

	public int getClientType() {
		return clientType;
	}

	public void setClientType(int clientType) {
		this.clientType = clientType;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
}
