package com.snail.webgame.game.common;

public class InnerSort {

	private int id;
	private byte stage;
	private int value;
	private long time;
	
	public InnerSort(int id, byte stage, int value, long time) {
		this.id = id;
		this.stage = stage;
		this.value = value;
		this.time = time;
	}
	
	public byte getStage() {
		return stage;
	}
	public void setStage(byte stage) {
		this.stage = stage;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
