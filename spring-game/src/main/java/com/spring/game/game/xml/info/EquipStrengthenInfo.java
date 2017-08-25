package com.snail.webgame.game.xml.info;

import com.snail.webgame.game.common.HeroProType;

public class EquipStrengthenInfo {
	private int level;
	private boolean addRate = false;// true:加百分比未除100,false : 加值
	private HeroProType refineType;
	private float effect;

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public boolean isAddRate() {
		return addRate;
	}

	public void setAddRate(boolean addRate) {
		this.addRate = addRate;
	}

	public HeroProType getRefineType() {
		return refineType;
	}

	public void setRefineType(HeroProType refineType) {
		this.refineType = refineType;
	}

	public float getEffect() {
		return effect;
	}

	public void setEffect(float effect) {
		this.effect = effect;
	}

}
