package com.snail.webgame.game.protocal.countryfight.office;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

/**
 * 升级官职
 * 
 * @author xiasd
 *
 */
public class OfficeUpgradeResp extends MessageBody{

	private int result;
	private int afterOfficeNo; // 升级后的官职ID 
	private int officeMoney; // 征伐值
	
	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("result", 0);
		ps.add("afterOfficeNo", 0);
		ps.add("officeMoney", 0);
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public int getAfterOfficeNo() {
		return afterOfficeNo;
	}

	public void setAfterOfficeNo(int afterOfficeNo) {
		this.afterOfficeNo = afterOfficeNo;
	}

	public int getOfficeMoney() {
		return officeMoney;
	}

	public void setOfficeMoney(int officeMoney) {
		this.officeMoney = officeMoney;
	}

}
