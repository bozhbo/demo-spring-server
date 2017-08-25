package com.snail.webgame.engine.game.module.login.msg;

import java.nio.ByteOrder;

import org.apache.mina.common.ByteBuffer;

import com.snail.webgame.engine.net.msg.impl.GameMessageReq;

public class TempLoginReq extends GameMessageReq {
	
	private String account;
	private String pass;

	@Override
	public void bytes2Req(ByteBuffer buffer, ByteOrder order) {
		this.account = getString(buffer, order);
		this.pass = getString(buffer, order);
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	@Override
	public boolean validate() {
		if (account == null || "".equals(account)) {
			return false;
		}
		
		if (pass == null || "".equals(pass)) {
			return false;
		}
		
		if (account.length() > 500) {
			return false;
		}
		
		return true;
	}
}
