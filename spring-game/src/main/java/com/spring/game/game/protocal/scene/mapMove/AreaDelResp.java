package com.snail.webgame.game.protocal.scene.mapMove;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class AreaDelResp extends MessageBody {
	private String delRoleId; // 移除的角色ID  id1,id2,id3

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.addString("delRoleId", "flashCode", 0);

	}

	public String getDelRoleId() {
		return delRoleId;
	}

	public void setDelRoleId(String delRoleId) {
		this.delRoleId = delRoleId;
	}
}
