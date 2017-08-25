package com.snail.webgame.game.protocal.fightdeploy.buy;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class BuyDeployPosResp extends MessageBody {

	private int result;
	private String deployPosOpenStr;// 布阵位置购买开启(0 未开启 1开启)例 1,0

	private byte sourceType;
	private int sourceChange;

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("result", 0);
		ps.addString("deployPosOpenStr", "flashCode", 0);
		
		ps.add("sourceType", 0);
		ps.add("sourceChange", 0);
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public String getDeployPosOpenStr() {
		return deployPosOpenStr;
	}

	public void setDeployPosOpenStr(String deployPosOpenStr) {
		this.deployPosOpenStr = deployPosOpenStr;
	}

	public byte getSourceType() {
		return sourceType;
	}

	public void setSourceType(byte sourceType) {
		this.sourceType = sourceType;
	}

	public int getSourceChange() {
		return sourceChange;
	}

	public void setSourceChange(int sourceChange) {
		this.sourceChange = sourceChange;
	}
}
