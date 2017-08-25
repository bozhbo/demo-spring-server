package com.snail.webgame.game.xml.info;

public class PropXMLInfo {

	private int no;
	private String name;
	private int type;//1-武将碎片 2-消耗品 
	private int sell;// 卖价
	private int subType;//1-经验丹	2-银子包   3-金子包 4-粮草包   5-宝箱包 6-体力包  7-科技点包
	private int colour; //颜色(橙-紫-蓝-绿-白)
	private String useParam;//使用效果
	private int resolmoney;
	
	private int level;//道具使用等级
	
	private String key;//使用道具消耗
	
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

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getSell() {
		return sell;
	}

	public void setSell(int sell) {
		this.sell = sell;
	}

	public int getSubType() {
		return subType;
	}

	public void setSubType(int subType) {
		this.subType = subType;
	}

	public String getUseParam() {
		return useParam;
	}

	public void setUseParam(String useParam) {
		this.useParam = useParam;
	}

	public int getColour() {
		return colour;
	}

	public void setColour(int colour) {
		this.colour = colour;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public int getResolmoney() {
		return resolmoney;
	}

	public void setResolmoney(int resolmoney) {
		this.resolmoney = resolmoney;
	}
	
}
