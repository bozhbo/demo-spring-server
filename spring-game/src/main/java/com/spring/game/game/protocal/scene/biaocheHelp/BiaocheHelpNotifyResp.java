package com.snail.webgame.game.protocal.scene.biaocheHelp;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;


/**
 * 提示好友有人请求护送押镖
 * @author hongfm
 *
 */
public class BiaocheHelpNotifyResp extends MessageBody {

	private int result;
	private int friendRoleId;//请求的好友Id;
	private String friendRoleName;
	private int getSilver;//护送可获得的银子
	
	@Override
	protected void setSequnce(ProtocolSequence ps)
	{
		ps.add("result", 0);
		ps.add("friendRoleId", 0);
		ps.addString("friendRoleName", "flashCode", 0);
		ps.add("getSilver", 0);
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public int getFriendRoleId() {
		return friendRoleId;
	}

	public void setFriendRoleId(int friendRoleId) {
		this.friendRoleId = friendRoleId;
	}

	public String getFriendRoleName() {
		return friendRoleName;
	}

	public void setFriendRoleName(String friendRoleName) {
		this.friendRoleName = friendRoleName;
	}

	public int getGetSilver() {
		return getSilver;
	}

	public void setGetSilver(int getSilver) {
		this.getSilver = getSilver;
	}
	
}
