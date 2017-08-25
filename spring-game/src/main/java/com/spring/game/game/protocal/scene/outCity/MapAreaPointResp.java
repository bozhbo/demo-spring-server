package com.snail.webgame.game.protocal.scene.outCity;

import java.util.ArrayList;
import java.util.List;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;


/**
 * 世界场景中看到单位
 * @author hongfm
 *
 */
public class MapAreaPointResp extends MessageBody {

	private int mapRoleCount;
	private List<MapRolePointRe> roleList = new ArrayList<MapRolePointRe>();//看到的玩家
	
	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("mapRoleCount", 0);
		ps.addObjectArray("roleList", "com.snail.webgame.game.protocal.scene.outCity.MapRolePointInfo", "mapRoleCount");
	}

	public int getMapRoleCount() {
		return mapRoleCount;
	}

	public void setMapRoleCount(int mapRoleCount) {
		this.mapRoleCount = mapRoleCount;
	}

	public List<MapRolePointRe> getRoleList() {
		return roleList;
	}

	public void setRoleList(List<MapRolePointRe> roleList) {
		this.roleList = roleList;
	}
	
}
