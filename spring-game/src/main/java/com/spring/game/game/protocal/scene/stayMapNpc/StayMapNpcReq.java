package com.snail.webgame.game.protocal.scene.stayMapNpc;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;


/**
 * 在大地图上某个NPC驻足
 * @author hongfm
 *
 */
public class StayMapNpcReq extends MessageBody {

	private byte type1;//1-开始,2-取消,3-结束
	private int no;//大地图某NPC编号
	
	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("type1", 0);
		ps.add("no", 0);
		}

	public byte getType1() {
		return type1;
	}

	public void setType1(byte type1) {
		this.type1 = type1;
	}

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}
	
}
