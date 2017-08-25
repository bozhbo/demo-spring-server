package com.snail.webgame.game.common;

public class RideRecord {
	private int rideNo;// 编号
	private int rideLv;// 等级
	private int quality;// 品阶 1:白 2：绿 3：蓝 4：紫

	public int getRideNo() {
		return rideNo;
	}

	public void setRideNo(int rideNo) {
		this.rideNo = rideNo;
	}

	public int getRideLv() {
		return rideLv;
	}

	public void setRideLv(int rideLv) {
		this.rideLv = rideLv;
	}

	public int getQuality() {
		return quality;
	}

	public void setQuality(int quality) {
		this.quality = quality;
	}
}
