package com.snail.webgame.game.protocal.scene.sweepMapNpc;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class SweepMapNpcReq extends MessageBody {

	private int mapNpcNo;// 大地图某NPC编号

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("mapNpcNo", 0);
	}

	public int getMapNpcNo() {
		return mapNpcNo;
	}

	public void setMapNpcNo(int mapNpcNo) {
		this.mapNpcNo = mapNpcNo;
	}
}
