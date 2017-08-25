package com.snail.webgame.game.xml.info;

public class GuildConstructionXmlInfo {
	private int no;
	private int constructionPoint; //建设度
	private int costType;  // 1-Money（银币）2-Gold（金币）
	private int CostNum; // 消耗的货币数量
	private int clubContribution; //公会贡献值
	private int vipLv;
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
	public int getCostType() {
		return costType;
	}
	public void setCostType(int costType) {
		this.costType = costType;
	}
	public int getCostNum() {
		return CostNum;
	}
	public void setCostNum(int costNum) {
		CostNum = costNum;
	}
	public int getClubContribution() {
		return clubContribution;
	}
	public void setClubContribution(int clubContribution) {
		this.clubContribution = clubContribution;
	}
	public int getVipLv() {
		return vipLv;
	}
	public void setVipLv(int vipLv) {
		this.vipLv = vipLv;
	}
	
	
}
