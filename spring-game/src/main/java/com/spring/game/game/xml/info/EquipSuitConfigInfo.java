package com.snail.webgame.game.xml.info;

import com.snail.webgame.game.common.HeroProType;

public class EquipSuitConfigInfo {
	private int no;
	private int num;
	private boolean addRate = false;// true:加百分比未除100,false : 加值
	private HeroProType type;
	private int effect;

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public boolean isAddRate() {
		return addRate;
	}

	public void setAddRate(boolean addRate) {
		this.addRate = addRate;
	}

	public HeroProType getType() {
		return type;
	}

	public void setType(HeroProType type) {
		this.type = type;
	}

	public int getEffect() {
		return effect;
	}

	public void setEffect(int effect) {
		this.effect = effect;
	}

}
