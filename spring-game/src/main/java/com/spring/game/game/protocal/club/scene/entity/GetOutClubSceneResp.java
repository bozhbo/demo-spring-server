package com.snail.webgame.game.protocal.club.scene.entity;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;



public class GetOutClubSceneResp extends MessageBody{
	private int result; //当角色在场景中 发生公会解散 或者被踢出公会的时候 发送此接口 将场景切换到主城

	public GetOutClubSceneResp(){
		
	}
	
	public GetOutClubSceneResp(int result){
		this.result = result;
	}
	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("result", 0);
	}
	public int getResult() {
		return result;
	}
	public void setResult(int result) {
		this.result = result;
	}

}
