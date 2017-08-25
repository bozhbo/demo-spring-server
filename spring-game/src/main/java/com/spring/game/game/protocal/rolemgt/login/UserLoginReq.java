package com.snail.webgame.game.protocal.rolemgt.login;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class UserLoginReq extends MessageBody {

	private int IP;// 角色IP
	private String account;// 玩家登陆帐号
	private String md5Pass;// 玩家登陆密码
	private String validate;// 角色验证串
	private int clientType;// 客户端类型 1:android 2:ios
	private String mac;//客户端mac地址
	private String packageName;//客户端包名
	private int roleId;

	protected void setSequnce(ProtocolSequence ps) {
		ps.add("IP", 0);
		ps.addString("account", "flashCode", 0);
		ps.addString("md5Pass", "flashCode", 0);
		ps.addString("validate", "flashCode", 0);
		ps.add("clientType", 0);
		ps.addString("mac", "flashCode", 0);
		ps.addString("packageName", "flashCode", 0);
		ps.add("roleId", 0);
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

	public String getMd5Pass() {
		return md5Pass;
	}

	public void setMd5Pass(String md5Pass) {
		this.md5Pass = md5Pass;
	}

	public String getValidate() {
		return validate;
	}

	public void setValidate(String validate) {
		this.validate = validate;
	}

	public int getClientType() {
		return clientType;
	}

	public void setClientType(int clientType) {
		this.clientType = clientType;
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
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
