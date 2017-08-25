package com.snail.webgame.game.protocal.fight.mutual.refresh;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

/**
 * 
 * 类介绍:刷新离开接口
 *
 * @author zhoubo
 * @2015年5月27日
 */
public class MutualLeftRefreshResp extends MessageBody {
	
	private byte changeType; // 变动类型 1-解散队伍 2-离开队伍 3-踢出队伍 4-匹配中 5-取消匹配
	private int leaderRoleId; // 队长角色Id
	private int roleId;	// 角色Id

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("changeType", 0);
		ps.add("leaderRoleId", 0);
		ps.add("roleId", 0);
	}
	
	public MutualLeftRefreshResp() {
		
	}
	
	/**
	 * @param changeType 变动类型 1-解散队伍 2-离开队伍 3-踢出队伍 4-匹配中 5-取消匹配
	 * @param leaderRoleId
	 * @param roleId
	 */
	public MutualLeftRefreshResp(byte changeType, int leaderRoleId, int roleId) {
		this.changeType = changeType;
		this.roleId = roleId;
		this.leaderRoleId = leaderRoleId;
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public int getLeaderRoleId() {
		return leaderRoleId;
	}

	public void setLeaderRoleId(int leaderRoleId) {
		this.leaderRoleId = leaderRoleId;
	}

	public byte getChangeType() {
		return changeType;
	}

	public void setChangeType(byte changeType) {
		this.changeType = changeType;
	}

}
