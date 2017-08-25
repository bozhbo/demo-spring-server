package com.snail.webgame.game.protocal.fight.mutual.rank;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class MutualQueryRankRes extends MessageBody {
	
	private int scroeValue;// 积分
    private String name;// 名字
    private int roleId;// 角色Id
    private int heroId;// 英雄编号
    private int heroLevel; // 英雄等级
    
	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("scroeValue", 0);
		ps.addString("name", "flashCode", 0);
		ps.add("roleId", 0);
		ps.add("heroId", 0);
		ps.add("heroLevel", 0);
	}

	public int getScroeValue() {
		return scroeValue;
	}

	public void setScroeValue(int scroeValue) {
		this.scroeValue = scroeValue;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public int getHeroLevel() {
		return heroLevel;
	}

	public void setHeroLevel(int heroLevel) {
		this.heroLevel = heroLevel;
	}

	
}
