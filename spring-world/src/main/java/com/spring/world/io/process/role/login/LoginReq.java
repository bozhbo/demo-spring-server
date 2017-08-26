package com.spring.world.io.process.role.login;

import java.nio.ByteOrder;

import org.apache.mina.common.ByteBuffer;

import com.snail.mina.protocol.info.impl.BaseRoomReq;

public class LoginReq extends BaseRoomReq {

	private int IP;// 角色IP
	private String account;// 玩家登陆帐号
	private String md5Pass;// 玩家登陆密码
	private String validate;// 角色验证串
	private int clientType;// 客户端类型 1:android 2:ios
	private String mac;// 客户端mac地址
	private String packageName;// 客户端包名

	@Override
	public void bytes2Req(ByteBuffer buffer, ByteOrder order) {
		this.IP = getInt(buffer, order);
		this.account = getString(buffer, order);
		this.md5Pass = getString(buffer, order);
		this.validate = getString(buffer, order);
		this.clientType = getInt(buffer, order);
		this.mac = getString(buffer, order);
		this.packageName = getString(buffer, order);

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
