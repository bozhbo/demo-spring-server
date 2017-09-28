package com.snail.client.web.msg.login;

import java.nio.ByteOrder;

import org.apache.mina.common.ByteBuffer;

import com.snail.mina.protocol.info.impl.BaseRoomResp;

public class LoginReq extends BaseRoomResp {

	private int IP;// 角色IP
	private String account;// 玩家登陆帐号
	private String md5Pass;// 玩家登陆密码
	private String validate;// 角色验证串
	private int clientType;// 客户端类型 1:android 2:ios
	private String mac;//客户端mac地址
	private String packageName;//客户端包名
	
	@Override
	public void resp2Bytes(ByteBuffer buffer, ByteOrder order) {
		setInt(buffer, order, IP);
		setString(buffer, order, account);
		setString(buffer, order, md5Pass);
		setString(buffer, order, validate);
		setInt(buffer, order, clientType);
		setString(buffer, order, mac);
		setString(buffer, order, packageName);
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
