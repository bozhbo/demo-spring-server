package com.snail.webgame.game.protocal.scene.info;

import java.util.concurrent.ConcurrentHashMap;

public class SceneInfo {
	private int id;
	private int sceneNo;
	private ConcurrentHashMap<Integer, RolePoint> rolePointMap;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getSceneNo() {
		return sceneNo;
	}

	public void setSceneNo(int sceneNo) {
		this.sceneNo = sceneNo;
	}

	public ConcurrentHashMap<Integer, RolePoint> getRolePointMap() {
		return rolePointMap;
	}

	public void setRolePointMap(ConcurrentHashMap<Integer, RolePoint> rolePointMap) {
		this.rolePointMap = rolePointMap;
	}

	public void addRolePoint(RolePoint point) {
		if (rolePointMap == null) {
			rolePointMap = new ConcurrentHashMap<Integer, RolePoint>();
		}
		rolePointMap.put(point.getRoleId(), point);
	}

}
