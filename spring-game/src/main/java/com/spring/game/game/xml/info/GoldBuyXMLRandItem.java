package com.snail.webgame.game.xml.info;

public class GoldBuyXMLRandItem {

	private float mul;
	private int minRand;//最小概率
	private int maxRand;//最大概率

	public float getMul() {
		return mul;
	}

	public void setMul(float mul) {
		this.mul = mul;
	}

	public int getMinRand() {
		return minRand;
	}

	public void setMinRand(int minRand) {
		this.minRand = minRand;
	}

	public int getMaxRand() {
		return maxRand;
	}

	public void setMaxRand(int maxRand) {
		this.maxRand = maxRand;
	}
}
