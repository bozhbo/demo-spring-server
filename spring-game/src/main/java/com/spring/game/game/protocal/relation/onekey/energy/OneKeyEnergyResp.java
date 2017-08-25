package com.snail.webgame.game.protocal.relation.onekey.energy;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class OneKeyEnergyResp extends MessageBody {
	private int result;
	private int flag; // 0 - 一键领取 1 - 一键赠送
	private String ids;// id:id:id 领取的Id

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("result", 0);
		ps.add("flag", 0);
		ps.addString("ids", "flashCode", 0);
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

}
