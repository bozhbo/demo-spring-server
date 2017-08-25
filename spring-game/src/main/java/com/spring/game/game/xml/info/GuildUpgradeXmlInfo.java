package com.snail.webgame.game.xml.info;

public class GuildUpgradeXmlInfo {
	private int no; // 等级
	private int constructionPoint; // 升级所需建设点数
	private int members; // 数量上限
	private int vicePresident; //
	private int offical; //

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public int getConstructionPoint() {
		return constructionPoint;
	}

	public void setConstructionPoint(int constructionPoint) {
		this.constructionPoint = constructionPoint;
	}

	public int getMembers() {
		return members;
	}

	public void setMembers(int members) {
		this.members = members;
	}

	public int getVicePresident() {
		return vicePresident;
	}

	public void setVicePresident(int vicePresident) {
		this.vicePresident = vicePresident;
	}

	public int getOffical() {
		return offical;
	}

	public void setOffical(int offical) {
		this.offical = offical;
	}

}
