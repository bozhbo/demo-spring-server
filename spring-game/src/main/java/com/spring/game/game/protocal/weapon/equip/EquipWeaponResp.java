package com.snail.webgame.game.protocal.weapon.equip;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

/**
 * 装备神兵响应
 * 
 * @author xiasd
 *
 */
public class EquipWeaponResp extends MessageBody {
	private int result;
	private int weaponId;// 神兵主键
	private int equipPosition;// 镶嵌的位置  // 0-卸载  else 部位 1-武器、2-盔甲、3-头盔、4-护腕、5-披风、6-鞋子
	
	private int heroId;//武将id
	private int fightValue;//武将战斗力

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("result", 0);
		ps.add("weaponId", 0);
		ps.add("equipPosition", 0);
		
		ps.add("heroId", 0);
		ps.add("fightValue", 0);
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public int getWeaponId() {
		return weaponId;
	}

	public void setWeaponId(int weaponId) {
		this.weaponId = weaponId;
	}

	public int getEquipPosition() {
		return equipPosition;
	}

	public void setEquipPosition(int equipPosition) {
		this.equipPosition = equipPosition;
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
