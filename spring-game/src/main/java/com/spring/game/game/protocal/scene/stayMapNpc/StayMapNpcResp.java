package com.snail.webgame.game.protocal.scene.stayMapNpc;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;


/**
 * 在大地图上某个NPC驻足
 * @author hongfm
 *
 */
public class StayMapNpcResp extends MessageBody {

	private int no;//大地图某NPC编号
	
	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("no", 0);
		}

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}
	
}
