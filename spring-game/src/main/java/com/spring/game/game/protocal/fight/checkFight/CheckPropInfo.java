package com.snail.webgame.game.protocal.fight.checkFight;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class CheckPropInfo extends MessageBody {

	private int index;
	private float value;
	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("index", 0);
		
		ps.add("value", 0);
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public float getValue() {
		return value;
	}

	public void setValue(float value) {
		this.value = value;
	}
	
	
}
