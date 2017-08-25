package com.snail.webgame.game.xml.info;

import java.util.HashMap;

public class GWXMLInfo {

	private String no;
	
	private HashMap<Integer, String> dropMap = new HashMap<Integer, String>();

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public HashMap<Integer, String> getDropMap() {
		return dropMap;
	}

	public void setDropMap(HashMap<Integer, String> dropMap) {
		this.dropMap = dropMap;
	}
	
}
