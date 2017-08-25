package com.snail.webgame.game.xml.info;

import java.util.ArrayList;
import java.util.List;

public class RecruitDepotXMLInfo {

	private int no;//编号
	private String name;//名字
	//物品
	private List<RecruitItemXMLInfo> items = new ArrayList<RecruitItemXMLInfo>();

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<RecruitItemXMLInfo> getItems() {
		return items;
	}

	public void setItems(List<RecruitItemXMLInfo> items) {
		this.items = items;
	}
}
