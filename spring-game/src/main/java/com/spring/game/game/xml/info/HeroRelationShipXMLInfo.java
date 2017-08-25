package com.snail.webgame.game.xml.info;

import java.util.List;

import com.snail.webgame.game.common.HeroProType;

/**
 * 武将羁绊关系
 * @author wanglinhui
 */
public class HeroRelationShipXMLInfo {

	private int heroNo;
	private List<RelationShipXMLInfo> relationShipList;

	public class RelationShipXMLInfo {

		private int relationNo;
		private boolean addRate = false;// true:加百分比,false : 加值
		private HeroProType buffType;
		private double buffAdd;// 羁绊提升属性百分比
		private List<Integer> heroNo;// 羁绊具体武将的ID

		public int getRelationNo() {
			return relationNo;
		}

		public void setRelationNo(int relationNo) {
			this.relationNo = relationNo;
		}
		
		public boolean isAddRate() {
			return addRate;
		}

		public void setAddRate(boolean addRate) {
			this.addRate = addRate;
		}

		public HeroProType getBuffType() {
			return buffType;
		}

		public void setBuffType(HeroProType buffType) {
			this.buffType = buffType;
		}

		public double getBuffAdd() {
			return buffAdd;
		}

		public void setBuffAdd(double buffAdd) {
			this.buffAdd = buffAdd;
		}

		public List<Integer> getHeroNo() {
			return heroNo;
		}

		public void setHeroNo(List<Integer> heroNo) {
			this.heroNo = heroNo;
		}
	}

	public int getHeroNo() {
		return heroNo;
	}

	public void setHeroNo(int heroNo) {
		this.heroNo = heroNo;
	}

	public List<RelationShipXMLInfo> getRelationShipList() {
		return relationShipList;
	}

	public void setRelationShipList(List<RelationShipXMLInfo> relationShipList) {
		this.relationShipList = relationShipList;
	}

}
