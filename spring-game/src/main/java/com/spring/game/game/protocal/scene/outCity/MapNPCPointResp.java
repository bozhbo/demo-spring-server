package com.snail.webgame.game.protocal.scene.outCity;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;


/**
 * 世界地图上看到的NPC
 * @author hongfm
 *
 */
public class MapNPCPointResp extends MessageBody {

	private String npcArtStr;//地图上玩家可见的NPC art1,art2,art3
	
	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.addString("npcArtStr", "flashCode", 0);
	}

	public String getNpcArtStr() {
		return npcArtStr;
	}

	public void setNpcArtStr(String npcArtStr) {
		this.npcArtStr = npcArtStr;
	}

}
