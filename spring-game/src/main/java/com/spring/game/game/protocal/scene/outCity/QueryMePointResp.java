package com.snail.webgame.game.protocal.scene.outCity;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;


/**
 * 自己在大地图上坐标状态
 * @author hongfm
 *
 */
public class QueryMePointResp extends MessageBody {

	private int result;
	private MapRolePointRe pointRe = new MapRolePointRe();//自己在地图上情况
	
	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("result", 0);
		ps.addObject("pointRe");
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public MapRolePointRe getPointRe() {
		return pointRe;
	}

	public void setPointRe(MapRolePointRe pointRe) {
		this.pointRe = pointRe;
	}
	

}
