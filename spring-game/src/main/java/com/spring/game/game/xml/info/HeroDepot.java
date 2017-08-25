package com.snail.webgame.game.xml.info;

import java.util.ArrayList;
import java.util.List;

public class HeroDepot {
	
	private int heroNo;
	private List<RecruitItemXMLInfo> items = new ArrayList<RecruitItemXMLInfo>();
	public int getHeroNo() {
		return heroNo;
	}
	public void setHeroNo(int heroNo) {
		this.heroNo = heroNo;
	}
	public List<RecruitItemXMLInfo> getItems() {
		return items;
	}
	public void setItems(List<RecruitItemXMLInfo> items) {
		this.items = items;
	}
	
}
