package com.snail.webgame.game.info;

import com.snail.webgame.engine.common.to.BaseTO;

public class PhoneRecordInfo extends BaseTO {

	private String account;
	
	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	@Override
	public byte getSaveMode() {
		return 0;
	}

}
