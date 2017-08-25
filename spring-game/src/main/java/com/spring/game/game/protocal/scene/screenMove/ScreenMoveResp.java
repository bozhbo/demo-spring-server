package com.snail.webgame.game.protocal.scene.screenMove;

import java.util.ArrayList;
import java.util.List;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

import com.snail.webgame.game.protocal.scene.outCity.MapRolePointRe;

public class ScreenMoveResp extends MessageBody {
	
	private int result;
	private String delRoleId; // 移除的角色ID
	
	//新增玩家
	private int addRoleCount;
	private List<MapRolePointRe> addRoleList = new ArrayList<MapRolePointRe>();

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		
		ps.add("result",0);
		ps.addString("delRoleId", "flashCode", 0);
		
		ps.add("addRoleCount",0);
		ps.addObjectArray("addRoleList", "com.snail.webgame.game.protocal.scene.outCity.MapRolePointRe", "addRoleCount");

	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public String getDelRoleId() {
		return delRoleId;
	}

	public void setDelRoleId(String delRoleId) {
		this.delRoleId = delRoleId;
	}

	public int getAddRoleCount() {
		return addRoleCount;
	}

	public void setAddRoleCount(int addRoleCount) {
		this.addRoleCount = addRoleCount;
	}

	public List<MapRolePointRe> getAddRoleList() {
		return addRoleList;
	}

	public void setAddRoleList(List<MapRolePointRe> addRoleList) {
		this.addRoleList = addRoleList;
	}
}
