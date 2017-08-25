package com.snail.webgame.game.protocal.item.service;

public class GetDropData {

	/**
	 * 0 获得新道具 1增加道具 2减少道具 3删除道具
	 */
	public static final int TYPE_NEW = 0;
	public static final int TYPE_ADD = 1;
	public static final int TYPE_SUB = 2;
	public static final int TYPE_DEL = 3;
	public static final int OTHER_TYPE = 4;

	private int no;// 编号
	private int num;// 数量

	private int type;// 0:新获得 1增加数量 2减少道具 3删除道具
	private int itemType;//道具类型
	private String param = "0";
	private int id;

	public GetDropData(int id, int type, int itemType) {
		this.id = id;
		this.type = type;
		this.itemType = itemType;
	}

	public GetDropData(int no, int num) {
		this.no = no;
		this.num = num;
	}

	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

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

	public int getItemType() {
		return itemType;
	}

	public void setItemType(int itemType) {
		this.itemType = itemType;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
