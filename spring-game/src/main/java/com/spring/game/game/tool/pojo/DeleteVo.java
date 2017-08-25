package com.snail.webgame.game.tool.pojo;

public class DeleteVo {

	private int id;// 删除的id,道具为no
	private int num;// 删道具时为删除后剩余的数量,删装备时固定为1

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

}
