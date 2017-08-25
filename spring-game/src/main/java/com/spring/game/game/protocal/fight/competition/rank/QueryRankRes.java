package com.snail.webgame.game.protocal.fight.competition.rank;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class QueryRankRes extends MessageBody {

    private int rankNum;// 排名
    private byte militaryRank;// 段位
    private String name;// 名字
    private int level;// 等级
    private int roleId;// 角色Id
    private int heroId;// 英雄编号
    
	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("rankNum", 0);
		ps.add("militaryRank", 0);
		ps.addString("name", "flashCode", 0);
		ps.add("level", 0);
		ps.add("roleId", 0);
		ps.add("heroId", 0);
		
	}

	public int getRankNum() {
		return rankNum;
	}

	public void setRankNum(int rankNum) {
		this.rankNum = rankNum;
	}

	public byte getMilitaryRank() {
		return militaryRank;
	}

	public void setMilitaryRank(byte militaryRank) {
		this.militaryRank = militaryRank;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public int getHeroId() {
		return heroId;
	}

	public void setHeroId(int heroId) {
		this.heroId = heroId;
	}
	
	
}
