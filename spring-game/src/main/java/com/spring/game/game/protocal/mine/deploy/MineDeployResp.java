package com.snail.webgame.game.protocal.mine.deploy;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

import com.snail.webgame.game.protocal.mine.query.MineInfoRe;

public class MineDeployResp extends MessageBody {

	private int result;
	private MineInfoRe mineInfo = new MineInfoRe();

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("result", 0);
		ps.addObject("mineInfo");
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public MineInfoRe getMineInfo() {
		return mineInfo;
	}

	public void setMineInfo(MineInfoRe mineInfo) {
		this.mineInfo = mineInfo;
	}
}
