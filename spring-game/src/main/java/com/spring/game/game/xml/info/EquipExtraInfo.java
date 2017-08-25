package com.snail.webgame.game.xml.info;

import com.snail.webgame.game.common.HeroProType;

public class EquipExtraInfo {
	private boolean addRate = false;// true:加百分比未除100,false : 加值
	private HeroProType refineType;
	private double effect;

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

	public double getEffect() {
		return effect;
	}

	public void setEffect(double effect) {
		this.effect = effect;
	}

}
