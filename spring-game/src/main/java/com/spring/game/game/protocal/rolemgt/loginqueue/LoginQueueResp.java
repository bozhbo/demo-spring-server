package com.snail.webgame.game.protocal.rolemgt.loginqueue;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class LoginQueueResp extends MessageBody {
	
	private int result;
	private String account;
	private int index;
	private int num;
	private int flag;//0-等待 1-登录

	@Override
	protected void setSequnce(ProtocolSequence ps) 
	{
		ps.add("result", 0);
		ps.addString("account", "flashCode", 0);
		ps.add("index", 0);
		ps.add("num", 0);
		ps.add("flag", 0);
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

}
