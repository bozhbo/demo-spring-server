package com.snail.webgame.game.protocal.club.scene.check;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class CheckIsInSceneResp extends MessageBody {
	private int result; //1在场景中，发送InOrOutClubSceneReq 进入场景  2 - 不在场景中 发送先前进入其他场景的查询接口

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("result", result);
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

}
