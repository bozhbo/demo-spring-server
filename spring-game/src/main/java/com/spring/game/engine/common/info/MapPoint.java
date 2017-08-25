package com.snail.webgame.engine.common.info;

import java.util.ArrayList;
import java.util.List;

/**
 * 地图坐标点上的玩家
 * @author zenggy
 *
 */
public class MapPoint {
	private List<Long> roleIds =  new ArrayList<Long>();
	// 地图坐标点上的npc
	private List<Long> npcIds = new ArrayList<Long>();
	// 地图坐标点的掉落物品
	//private List<Long> dropIds = new ArrayList<Long>();
	// 地图坐标点的掉落包
	private List<Long> dropBagIds = new ArrayList<Long>();

	public List<Long> getRoleIds() {
		return roleIds;
	}

	public void setRoleIds(List<Long> roleIds) {
		this.roleIds = roleIds;
	}

	public List<Long> getNpcIds() {
		return npcIds;
	}

	public void setNpcIds(List<Long> npcIds) {
		this.npcIds = npcIds;
	}

	public List<Long> getDropBagIds() {
		return dropBagIds;
	}

	public void setDropBagIds(List<Long> dropBagIds) {
		this.dropBagIds = dropBagIds;
	}

	/*public List<Long> getDropIds() {
		return dropIds;
	}

	public void setDropIds(List<Long> dropIds) {
		this.dropIds = dropIds;
	}*/
	
}
