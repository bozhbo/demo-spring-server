package com.snail.webgame.game.protocal.opactivity.timeaction.querytype;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class TimeActionTypeRe  extends MessageBody {

	private int actId;// 活动id
	private String actName;// 活动name
	private byte actState;// 活动状态 0-关闭  1-开启
	
	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("actId", 0);
		ps.addString("actName", "flashCode", 0);
		ps.add("actState", 0);
	}

	public String getActName() {
		return actName;
	}

	public void setActName(String actName) {
		this.actName = actName;
	}

	public int getActId() {
		return actId;
	}

	public void setActId(int actId) {
		this.actId = actId;
	}

	public byte getActState() {
		return actState;
	}

	public void setActState(byte actState) {
		this.actState = actState;
	}

}
