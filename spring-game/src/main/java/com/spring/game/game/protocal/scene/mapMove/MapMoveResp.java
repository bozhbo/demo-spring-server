package com.snail.webgame.game.protocal.scene.mapMove;

import java.util.ArrayList;
import java.util.List;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

import com.snail.webgame.game.protocal.scene.outCity.MapRolePointRe;

public class MapMoveResp extends MessageBody {
	private String delRoleId; // 移除的角色ID
	private int otherRolecount;
	private List<MapRolePointRe> otherRoleList = new ArrayList<MapRolePointRe>();

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.addString("delRoleId", "flashCode", 0);
		ps.add("otherRolecount",0);
		ps.addObjectArray("otherRoleList", "com.snail.webgame.game.protocal.scene.outCity.MapRolePointRe", "otherRolecount");

	}

	public String getDelRoleId() {
		return delRoleId;
	}

	public void setDelRoleId(String delRoleId) {
		this.delRoleId = delRoleId;
	}

	public int getOtherRolecount() {
		return otherRolecount;
	}

	public void setOtherRolecount(int otherRolecount) {
		this.otherRolecount = otherRolecount;
	}

	public List<MapRolePointRe> getOtherRoleList() {
		return otherRoleList;
	}

	public void setOtherRoleList(List<MapRolePointRe> otherRoleList) {
		this.otherRoleList = otherRoleList;
	}
	
}
