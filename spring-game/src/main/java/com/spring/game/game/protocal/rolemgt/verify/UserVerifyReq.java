package com.snail.webgame.game.protocal.rolemgt.verify;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class UserVerifyReq extends MessageBody {

	private int IP;// 角色IP
	private String account;// 玩家登陆帐号
	private String md5Pass;// 玩家登陆密码
	private String validate;// 角色验证串
	private int clientType;// 客户端类型 1-安卓 2-ios
	private int flag;//验证表示 0-一般验证 1-创建角色验证

	protected void setSequnce(ProtocolSequence ps) {
		ps.add("IP", 0);
		ps.addString("account", "flashCode", 0);
		ps.addString("md5Pass", "flashCode", 0);
		ps.addString("validate", "flashCode", 0);
		ps.add("clientType", 0);
		ps.add("flag", 0);
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

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}
}
