package com.snail.webgame.game.xml.info;


public class StoneComeXML {

	private int no;
	private int level;// 宝石等级
	private int material;// 所需材料
	private int silver;//镶嵌消耗
	private int removeSilver;//摘除消耗
	private int num;// 所需材料数量
	public int getNo() {
		return no;
	}
	public void setNo(int no) {
		this.no = no;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public int getMaterial() {
		return material;
	}
	public void setMaterial(int material) {
		this.material = material;
	}
	public int getSilver() {
		return silver;
	}
	public void setSilver(int silver) {
		this.silver = silver;
	}
	public int getRemoveSilver() {
		return removeSilver;
	}
	public void setRemoveSilver(int removeSilver) {
		this.removeSilver = removeSilver;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	
}
