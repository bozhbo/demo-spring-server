package com.snail.webgame.game.protocal.rolemgt.logout;

import java.util.ArrayList;
import java.util.List;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

import com.snail.webgame.game.protocal.rolemgt.verify.UserRoleRe;

public class UserLogoutResp extends MessageBody {

	private int result;
	private int roleId;
	private int userRoleResSize;	//list数量
	private List<UserRoleRe> userRoleRes = new ArrayList<UserRoleRe>();//角色列表
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("result", 0);
		ps.add("roleId", 0);
		ps.add("userRoleResSize", 0);
		ps.addObjectArray("userRoleRes", "com.snail.webgame.game.protocal.rolemgt.verify.UserRoleRe", "userRoleResSize");
	}
	public int getRoleId() {
		return roleId;
	}
	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}
	public int getResult() {
		return result;
	}
	public void setResult(int result) {
		this.result = result;
	}
	public int getUserRoleResSize() {
		return userRoleResSize;
	}
	public void setUserRoleResSize(int userRoleResSize) {
		this.userRoleResSize = userRoleResSize;
	}
	public List<UserRoleRe> getUserRoleRes() {
		return userRoleRes;
	}
	public void setUserRoleRes(List<UserRoleRe> userRoleRes) {
		this.userRoleRes = userRoleRes;
	}

}
