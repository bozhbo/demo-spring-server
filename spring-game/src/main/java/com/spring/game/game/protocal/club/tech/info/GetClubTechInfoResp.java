package com.snail.webgame.game.protocal.club.tech.info;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class GetClubTechInfoResp extends MessageBody{
	private int result;
	private String xmlNo; //xmlNo:xmlNo
	private int gold; //公会被捐赠的金子

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("result", 0);
		ps.addString("xmlNo", "flashCode", 0);
		ps.add("gold", 0);
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public String getXmlNo() {
		return xmlNo;
	}

	public void setXmlNo(String xmlNo) {
		this.xmlNo = xmlNo;
	}

	public int getGold() {
		return gold;
	}

	public void setGold(int gold) {
		this.gold = gold;
	}
	
}
