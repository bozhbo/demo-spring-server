package com.snail.webgame.game.protocal.scene.biaocheFightEnd;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;


/**
 * 压镖战斗结束
 * @author hongfm
 *
 */
public class BiaocheFightEndResp extends MessageBody {

	private int result;
	private byte type;//1-你的镖车被***拦截了,损失了***q银子  2-你好友***的镖车被***拦截,你护送奖励损失了***银子, 3-你成功拦截了***镖车获得多少钱**银子(名字取attackRoleName)
	private byte fightResult;//1-战斗失败,2-战斗成功
	private String attackRoleName;//凶手名字
	private String friendRoleName;//好友名字
	private int silverNum;//银子数量
	
	@Override
	protected void setSequnce(ProtocolSequence ps)
	{
		ps.add("result", 0);
		ps.add("type", 0);
		ps.add("fightResult", 0);
		ps.addString("attackRoleName", "flashCode", 0);
		ps.addString("friendRoleName", "flashCode", 0);
		ps.add("silverNum", 0);
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}
	

	public byte getFightResult() {
		return fightResult;
	}

	public void setFightResult(byte fightResult) {
		this.fightResult = fightResult;
	}

	public byte getType() {
		return type;
	}

	public void setType(byte type) {
		this.type = type;
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

	public int getSilverNum() {
		return silverNum;
	}

	public void setSilverNum(int silverNum) {
		this.silverNum = silverNum;
	}
	
}
