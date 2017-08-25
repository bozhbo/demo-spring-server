package com.snail.webgame.game.info;

import com.snail.webgame.engine.common.to.BaseTO;

public class BossInfo extends BaseTO {
	
	//BOSS全局变量
	public int bossType;		//boss类型 1-张角 1，3，5刷新、 2-左慈 2，4，6刷新 、3-仙人 7
	public int bosslevel = 0;	//等级
	public int bossNo;		//bossNo
	public long allHP = 0; 	//总血量
	private long currHP = 0;	//血量
	private float rate = 0; //系数
	private int mapNo;
	
	
	public long getAllHP() {
		return allHP;
	}
	public void setAllHP(long allHP) {
		this.allHP = allHP;
	}
	public long getCurrHP() {
		return currHP;
	}
	public void setCurrHP(long currHP) {
		this.currHP = currHP;
	}
	public float getRate() {
		return rate;
	}
	public void setRate(float rate) {
		this.rate = rate;
	}
	public int getBosslevel() {
		return bosslevel;
	}
	public void setBosslevel(int bosslevel) {
		this.bosslevel = bosslevel;
	}
	public int getBossType() {
		return bossType;
	}
	public void setBossType(int bossType) {
		this.bossType = bossType;
	}
	public int getBossNo() {
		return bossNo;
	}
	public void setBossNo(int bossNo) {
		this.bossNo = bossNo;
	}
	
	public int getMapNo() {
		return mapNo;
	}
	public void setMapNo(int mapNo) {
		this.mapNo = mapNo;
	}
	@Override
	public byte getSaveMode() {
		// TODO Auto-generated method stub
		return 0;
	}
	
}
