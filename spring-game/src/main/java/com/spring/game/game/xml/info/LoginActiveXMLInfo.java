package com.snail.webgame.game.xml.info;

import java.util.ArrayList;
import java.util.List;

public class LoginActiveXMLInfo {
	private int no;		//编号
	private int type;	//活动类型 1-连续登录 2-累计登录 3-春节登陆活动
	private long startTime;	//活动开始时间
	private long endTime;	//活动结束时间
	private int actionType;	//活动方式 1-累计天数方式 2-连续签到方式
	private List<LoginActiveXMLItemInfo> activeXMLItemInfos = new ArrayList<LoginActiveXMLItemInfo>();	//各活动阶段
	public int getNo() {
		return no;
	}
	public void setNo(int no) {
		this.no = no;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public long getStartTime() {
		return startTime;
	}
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}
	public long getEndTime() {
		return endTime;
	}
	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}
	public int getActionType() {
		return actionType;
	}
	public void setActionType(int actionType) {
		this.actionType = actionType;
	}
	public List<LoginActiveXMLItemInfo> getActiveXMLItemInfos() {
		return activeXMLItemInfos;
	}
	public void setActiveXMLItemInfos(
			List<LoginActiveXMLItemInfo> activeXMLItemInfos) {
		this.activeXMLItemInfos = activeXMLItemInfos;
	}
	
}
