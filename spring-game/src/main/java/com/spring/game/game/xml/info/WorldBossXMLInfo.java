package com.snail.webgame.game.xml.info;


public class WorldBossXMLInfo {

	private int no;
	private int mapCityNo;	//地图刷新no
	private String beginTime;	//开始时间
	private String endTime;	//结束时间
	private String week;		//刷新日期（星期几）
	private int NPCNo;			//NPC编号
	private long hp;				//血量
	private float rate;			//系数
	private String beginTime2;//第二只开始时间
	private String endTime2;//第二只结束时间
	
	
	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public int getMapCityNo() {
		return mapCityNo;
	}

	public void setMapCityNo(int mapCityNo) {
		this.mapCityNo = mapCityNo;
	}

	public String getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getWeek() {
		return week;
	}

	public void setWeek(String week) {
		this.week = week;
	}

	public int getNPCNo() {
		return NPCNo;
	}

	public void setNPCNo(int nPCNo) {
		NPCNo = nPCNo;
	}

	public long getHp() {
		return hp;
	}

	public void setHp(long hp) {
		this.hp = hp;
	}

	public float getRate() {
		return rate;
	}

	public void setRate(float rate) {
		this.rate = rate;
	}

	public String getBeginTime2() {
		return beginTime2;
	}

	public void setBeginTime2(String beginTime2) {
		this.beginTime2 = beginTime2;
	}

	public String getEndTime2() {
		return endTime2;
	}

	public void setEndTime2(String endTime2) {
		this.endTime2 = endTime2;
	}
	
	
}
