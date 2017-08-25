package com.snail.webgame.game.xml.info;

public class PushXMLInfo {

	public static final int PUSH_MINE_FINISH = 12;// 矿开采完成
	public static final int PUSH_NO_ARENA = 13;// 竞技场推送
	public static final int PUSH_NO_MINE = 14;// 矿坑被抢夺推送
	
	private int no;
	private int pushType;// 3-服务器推送
	private String title;
	private String content;

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getPushType() {
		return pushType;
	}

	public void setPushType(int pushType) {
		this.pushType = pushType;
	}

}
