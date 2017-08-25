package com.snail.webgame.game.protocal.countryfight.xuanzhan;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

/**
 * 宣战
 * 
 * @author xiasd
 *
 */
public class XuanzhanReq extends MessageBody{

	private int myClubId;
	private int targetClubId;
	private int food;
	
	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("myClubId", 0);
		ps.add("targetClubId", 0);
		ps.add("food", 0);
	}

	public int getMyClubId() {
		return myClubId;
	}

	public void setMyClubId(int myClubId) {
		this.myClubId = myClubId;
	}

	public int getTargetClubId() {
		return targetClubId;
	}

	public void setTargetClubId(int targetClubId) {
		this.targetClubId = targetClubId;
	}

	public int getFood() {
		return food;
	}

	public void setFood(int food) {
		this.food = food;
	}
//
}
