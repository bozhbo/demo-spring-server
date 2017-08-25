package com.snail.webgame.game.protocal.scene.joinScene;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;


/**
 * 玩家进入场景
 * @author hongfm
 *
 */
public class RoleJoinSceneReq extends MessageBody {

	private int sceneNo;//场景编号为0时进入当前场景,否则切换场景
	private byte disConnectIntoScene;// 1-断线重连
	
	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("sceneNo", 0);
		ps.add("disConnectIntoScene", 0);
	}
	
	public int getSceneNo() {
		return sceneNo;
	}

	public void setSceneNo(int sceneNo) {
		this.sceneNo = sceneNo;
	}

	public byte getDisConnectIntoScene() {
		return disConnectIntoScene;
	}

	public void setDisConnectIntoScene(byte disConnectIntoScene) {
		this.disConnectIntoScene = disConnectIntoScene;
	}
	
}
