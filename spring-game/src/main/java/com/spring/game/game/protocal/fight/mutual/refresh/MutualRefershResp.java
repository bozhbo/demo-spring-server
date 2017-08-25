package com.snail.webgame.game.protocal.fight.mutual.refresh;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

/**
 * 
 * 类介绍:此对象被用于请求返回接口，不用于刷新接口
 *
 * @author zhoubo
 * @2015年5月26日
 */
public class MutualRefershResp extends MessageBody {
	
	private int roleId;	// 角色Id
	private String name;// 角色名称
	private int heroId;// 主将英雄Id
	private int level;// 等级
	private String tempHeroId; // 副将ID(heroId1,heroId2..)
	private int fightValue;// 战斗力

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("roleId", 0);
		ps.addString("name", "flashCode", 0);
		ps.add("heroId", 0);
		ps.add("level", 0);
		ps.addString("tempHeroId", "flashCode", 0);
		ps.add("fightValue", 0);
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getHeroId() {
		return heroId;
	}

	public void setHeroId(int heroId) {
		this.heroId = heroId;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getTempHeroId() {
		return tempHeroId;
	}

	public void setTempHeroId(String tempHeroId) {
		this.tempHeroId = tempHeroId;
	}

	public int getFightValue() {
		return fightValue;
	}

	public void setFightValue(int fightValue) {
		this.fightValue = fightValue;
	}
	
	

}
