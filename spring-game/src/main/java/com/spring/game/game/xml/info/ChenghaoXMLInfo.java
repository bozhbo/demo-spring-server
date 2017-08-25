package com.snail.webgame.game.xml.info;

import java.util.ArrayList;
import java.util.List;

/**
 * 称号基础信息
 * @author SnailGame
 *
 */
public class ChenghaoXMLInfo {
	private int no;
	private String type; // 类型
	private int num; // 名次
	private int keepTime; // 持续时间 单位秒
	private List<TitleValue> titleValueList = new ArrayList<TitleValue>(); // 属性

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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getKeepTime() {
		return keepTime;
	}

	public void setKeepTime(int keepTime) {
		this.keepTime = keepTime;
	}

	public List<TitleValue> getTitleValueList() {
		return titleValueList;
	}

	public void setTitleValueList(List<TitleValue> titleValueList) {
		this.titleValueList = titleValueList;
	}

}
