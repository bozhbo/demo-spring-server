package com.snail.webgame.game.protocal.worship.query;

import java.util.List;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;


import com.snail.webgame.game.protocal.scene.info.SuperRInfo;

public class NotifyRefreshSuperResp extends MessageBody{
	private int sceneNo;
	private int superRsCount;
	private List<SuperRInfo> superRs;
	
	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("sceneNo", 0);
		ps.add("superRsCount",0);
		ps.addObjectArray("superRs", "com.snail.webgame.game.protocal.scene.info.SuperRInfo", "superRsCount");
	}
	
	
	public int getSceneNo() {
		return sceneNo;
	}
	public void setSceneNo(int sceneNo) {
		this.sceneNo = sceneNo;
	}
	
	public int getSuperRsCount() {
		return superRsCount;
	}
	public void setSuperRsCount(int superRsCount) {
		this.superRsCount = superRsCount;
	}
	public List<SuperRInfo> getSuperRs() {
		return superRs;
	}
	public void setSuperRs(List<SuperRInfo> superRs) {
		this.superRs = superRs;
	}
	
	
	
}
