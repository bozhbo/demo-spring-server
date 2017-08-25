package com.snail.webgame.game.protocal.scene.mapPvpFight;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;



/**
 * 大地图攻击提示
 * @author hongfm
 *
 */
public class MapPvpFightNofityResp extends MessageBody {
	
	private byte type1;//1-你攻击了***,2-你被***攻击了
	private String roleName;
	
	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("type1", 0);
		ps.addString("roleName", "flashCode", 0);
	}

	public byte getType1() {
		return type1;
	}

	public void setType1(byte type1) {
		this.type1 = type1;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	
	
}
