package com.snail.webgame.game.xml.info;

public class LoginActiveXMLItemInfo implements Comparable<LoginActiveXMLItemInfo> {
	private int no;
	private int needNum;	//此阶段活动完成需求天数(单位：天)(ActiveType=1时使用)
	private int loginTime;	//签到指定登陆时间(ActiveType=2时使用)
	private String[] items;	//发放物品（格式：编号,数量）
	public int getNo() {
		return no;
	}
	public void setNo(int no) {
		this.no = no;
	}
	public int getNeedNum() {
		return needNum;
	}
	public void setNeedNum(int needNum) {
		this.needNum = needNum;
	}
	public int getLoginTime() {
		return loginTime;
	}
	public void setLoginTime(int loginTime) {
		this.loginTime = loginTime;
	}
	public String[] getItems() {
		return items;
	}
	public void setItems(String[] items) {
		this.items = items;
	}
	@Override
	public int compareTo(LoginActiveXMLItemInfo o) {
		//按照No升序排列
		if(this.getNo() > o.getNo()){
			return 1;
		}else if(this.getNo() < o.getNo()){
			return -1;
		}else{
			return 0;
		}
	}
}
