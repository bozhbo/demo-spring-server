package com.snail.webgame.game.protocal.rolemgt.verify;

import java.util.ArrayList;
import java.util.List;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class UserVerifyResp extends MessageBody {

	private int result;// 1-成功
	private String account;
	private String acccode;//验证串-账号加密
	private int count;
	private List<UserRoleRe> list = new ArrayList<UserRoleRe>();

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("result", 0);
		ps.addString("account", "flashCode", 0);
		ps.addString("acccode", "flashCode", 0);
		ps.add("count", 0);
		ps.addObjectArray("list", "com.snail.webgame.game.protocal.rolemgt.verify.UserRoleRe", "count");
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getAcccode() {
		return acccode;
	}

	public void setAcccode(String acccode) {
		this.acccode = acccode;
	}

	public List<UserRoleRe> getList() {
		return list;
	}

	public void setList(List<UserRoleRe> list) {
		this.list = list;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}
	
}