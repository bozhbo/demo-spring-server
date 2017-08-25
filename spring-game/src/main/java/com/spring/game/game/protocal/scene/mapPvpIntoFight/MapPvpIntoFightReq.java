package com.snail.webgame.game.protocal.scene.mapPvpIntoFight;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;



/**
 * 大地图攻击,2秒后开始战斗
 * @author hongfm
 *
 */
public class MapPvpIntoFightReq extends MessageBody {
	
	private String beRoleName;//被攻击人姓名
	
	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.addString("beRoleName","flashCode", 0);
	}

	public String getBeRoleName() {
		return beRoleName;
	}

	public void setBeRoleName(String beRoleName) {
		this.beRoleName = beRoleName;
	}
	
	
}
