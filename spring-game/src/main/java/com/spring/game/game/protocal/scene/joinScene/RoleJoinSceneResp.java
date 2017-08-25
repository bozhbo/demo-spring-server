package com.snail.webgame.game.protocal.scene.joinScene;

import java.util.List;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;


/**
 * 玩家进入场景
 * @author hongfm
 *
 */
public class RoleJoinSceneResp extends MessageBody {

	private long roleId;
	private int sceneNo;//场景编号
	private int sceneId;//场景Id
	
	private int roleNum;//场景中角色数量
	private List<SceneRolePointInfo> rolePointList;
	
	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("roleId", 0);
		ps.add("sceneNo", 0);
		ps.add("sceneId", 0);
		
		ps.add("roleNum", 0);
		ps.addObjectArray("rolePointList", "com.snail.webgame.game.protocal.scene.updatePoint.SceneRolePointInfo","roleNum");
	}

	public long getRoleId() {
		return roleId;
	}

	public void setRoleId(long roleId) {
		this.roleId = roleId;
	}

	public int getSceneNo() {
		return sceneNo;
	}

	public void setSceneNo(int sceneNo) {
		this.sceneNo = sceneNo;
	}

	public int getSceneId() {
		return sceneId;
	}

	public void setSceneId(int sceneId) {
		this.sceneId = sceneId;
	}

	public int getRoleNum() {
		return roleNum;
	}

	public void setRoleNum(int roleNum) {
		this.roleNum = roleNum;
	}

	public List<SceneRolePointInfo> getRolePointList() {
		return rolePointList;
	}

	public void setRolePointList(List<SceneRolePointInfo> rolePointList) {
		this.rolePointList = rolePointList;
	}
}
