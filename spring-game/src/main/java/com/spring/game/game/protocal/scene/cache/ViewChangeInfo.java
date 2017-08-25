package com.snail.webgame.game.protocal.scene.cache;

import java.util.ArrayList;
import java.util.List;

/**
 * 附近可视、消失物体
 * 
 * @author hongfm
 * 
 */
public class ViewChangeInfo {
	/**
	 * 附近新出现的玩家
	 */
	private List<Integer> addRoleIds = new ArrayList<Integer>();
	/**
	 * 附近 消失的玩家
	 */
	private List<Integer> delRoleIds = new ArrayList<Integer>();
	
	/**
	 * 新位置原来老玩家
	 */
	List<Integer> curPonsiOldRoleIds = new ArrayList<Integer>();
	
	
	public List<Integer> getCurPonsiOldRoleIds() {
		return curPonsiOldRoleIds;
	}
	public void setCurPonsiOldRoleIds(List<Integer> curPonsiOldRoleIds) {
		this.curPonsiOldRoleIds = curPonsiOldRoleIds;
	}
	public List<Integer> getAddRoleIds() {
		return addRoleIds;
	}
	public void setAddRoleIds(List<Integer> addRoleIds) {
		this.addRoleIds = addRoleIds;
	}
	public List<Integer> getDelRoleIds() {
		return delRoleIds;
	}
	public void setDelRoleIds(List<Integer> delRoleIds) {
		this.delRoleIds = delRoleIds;
	}

}
