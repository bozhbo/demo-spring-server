package com.snail.webgame.game.protocal.scene.mapReachNPC;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;



/**
 * 大地图国家任务
 * @author hongfm
 *
 */
public class MapReachNPCReq extends MessageBody {
	
	private int mapCityNPCNo;
	private byte reachType;// 1-npcNo 2-cityNo
	
	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("mapCityNPCNo", 0);
		ps.add("reachType", 0);
	}

	public int getMapCityNPCNo() {
		return mapCityNPCNo;
	}

	public void setMapCityNPCNo(int mapCityNPCNo) {
		this.mapCityNPCNo = mapCityNPCNo;
	}

	public byte getReachType() {
		return reachType;
	}

	public void setReachType(byte reachType) {
		this.reachType = reachType;
	}

}
