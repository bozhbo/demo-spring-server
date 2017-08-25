package com.snail.webgame.game.protocal.scene.queryOtherAI;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;


/**
 * 查看场景其它玩家信息
 * @author hongfm
 *
 */
public class QueryOtherAIReq extends MessageBody {

	private int otherRoleId;
	private int arenaId;// 竞技场Id
	
	private int isWorship; //点击的模型是玩家还是膜拜对象,默认为0为玩家
	
	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("otherRoleId", 0);
		ps.add("arenaId", 0);
		ps.add("isWorship", 0);
	}

	public int getOtherRoleId() {
		return otherRoleId;
	}

	public void setOtherRoleId(int otherRoleId) {
		this.otherRoleId = otherRoleId;
	}

	public int getArenaId() {
		return arenaId;
	}

	public void setArenaId(int arenaId) {
		this.arenaId = arenaId;
	}

	public int getIsWorship() {
		return isWorship;
	}

	public void setIsWorship(int isWorship) {
		this.isWorship = isWorship;
	}
	
	
	
}
