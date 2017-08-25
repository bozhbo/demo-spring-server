package com.snail.webgame.game.protocal.fight.competition.history;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class FightHistoryRes extends MessageBody {

	private byte isWin; // 是否胜利 1-胜利 otherwise-失败
	private int level;// 角色等级
	private byte militaryRank;// 段位
	private String roleName;// 角色名称
	
	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("isWin", 0);
		ps.add("level", 0);
		ps.add("militaryRank", 0);
		ps.addString("roleName", "flashCode", 0);
		
	}

	public byte getIsWin() {
		return isWin;
	}

	public void setIsWin(byte isWin) {
		this.isWin = isWin;
	}

	public byte getMilitaryRank() {
		return militaryRank;
	}

	public void setMilitaryRank(byte militaryRank) {
		this.militaryRank = militaryRank;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}
	
	
}
