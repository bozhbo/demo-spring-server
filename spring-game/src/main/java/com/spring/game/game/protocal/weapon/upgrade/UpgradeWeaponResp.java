package com.snail.webgame.game.protocal.weapon.upgrade;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

/**
 * 升级神兵响应
 * 
 * @author xiasd
 * 
 */
public class UpgradeWeaponResp extends MessageBody {

	private int weaponId;// 被强化的神兵
	private int result;
	private String delWeaponIds;// 强化消耗掉的神兵id
	private short level;// 强化后的等级
	private int exp;// 强化后的经验
	
	private int heroId;//武将id
	private int fightValue;//武将战斗力

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("weaponId", 0);
		ps.add("result", 0);
		ps.addString("delWeaponIds", "flashCode", 0);
		ps.add("level", 0);
		ps.add("exp", 0);
		
		ps.add("heroId", 0);
		ps.add("fightValue", 0);
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public String getDelWeaponIds() {
		return delWeaponIds;
	}

	public void setDelWeaponIds(String delWeaponIds) {
		this.delWeaponIds = delWeaponIds;
	}

	public int getWeaponId() {
		return weaponId;
	}

	public void setWeaponId(int weaponId) {
		this.weaponId = weaponId;
	}

	public short getLevel() {
		return level;
	}

	public void setLevel(short level) {
		this.level = level;
	}

	public int getExp() {
		return exp;
	}

	public void setExp(int exp) {
		this.exp = exp;
	}

	public int getHeroId() {
		return heroId;
	}

	public void setHeroId(int heroId) {
		this.heroId = heroId;
	}

	public int getFightValue() {
		return fightValue;
	}

	public void setFightValue(int fightValue) {
		this.fightValue = fightValue;
	}
}
