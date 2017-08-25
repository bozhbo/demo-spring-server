package com.snail.webgame.game.protocal.club.scene.inorout;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class InOrOutClubSceneReq extends MessageBody {
	private int flag; // 0 - 进入 1 - 退出
	

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("flag", 0);

	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

}
