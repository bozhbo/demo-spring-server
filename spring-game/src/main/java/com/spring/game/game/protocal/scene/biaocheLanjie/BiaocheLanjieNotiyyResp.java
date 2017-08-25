package com.snail.webgame.game.protocal.scene.biaocheLanjie;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;



/**
 * 镖车被拦截提示
 * @author hongfm
 *
 */
public class BiaocheLanjieNotiyyResp extends MessageBody {
	
	private byte type1;//1-你的镖车被拦截了 2-你好友的镖车被拦截了 3-你拦截了谁的镖车（押镖人取attackRolename）
	private String attackRoleName;//凶手名字
	private String friendRoleName;//好友名字
	
	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("type1", 0);
		ps.addString("attackRoleName", "flashCode", 0);
		ps.addString("friendRoleName", "flashCode", 0);
	}

	public byte getType1() {
		return type1;
	}

	public void setType1(byte type1) {
		this.type1 = type1;
	}

	public String getAttackRoleName() {
		return attackRoleName;
	}

	public void setAttackRoleName(String attackRoleName) {
		this.attackRoleName = attackRoleName;
	}

	public String getFriendRoleName() {
		return friendRoleName;
	}

	public void setFriendRoleName(String friendRoleName) {
		this.friendRoleName = friendRoleName;
	}
}
