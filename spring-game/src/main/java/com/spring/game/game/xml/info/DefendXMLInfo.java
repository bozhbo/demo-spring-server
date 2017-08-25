package com.snail.webgame.game.xml.info;

import java.util.List;
import java.util.Map;

/**
 * 防守玩法配置信息
 * @author wanglinhui
 *
 */
public class DefendXMLInfo {

	private int no;
	private String name;
	private int needLevel;// 开启等级
	private List<DefendFightXMLInfo> defendFightList;

	public class DefendFightXMLInfo {

		private int no;
		private Map<Integer, String> dropBagNo;// 掉落物品

		public int getNo() {
			return no;
		}

		public void setNo(int no) {
			this.no = no;
		}

		public Map<Integer, String> getDropBagNo() {
			return dropBagNo;
		}

		public void setDropBagNo(Map<Integer, String> dropBagNo) {
			this.dropBagNo = dropBagNo;
		}
	}

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

	public int getNeedLevel() {
		return needLevel;
	}

	public void setNeedLevel(int needLevel) {
		this.needLevel = needLevel;
	}

	public List<DefendFightXMLInfo> getDefendFightList() {
		return defendFightList;
	}

	public void setDefendFightList(List<DefendFightXMLInfo> defendFightList) {
		this.defendFightList = defendFightList;
	}

}
