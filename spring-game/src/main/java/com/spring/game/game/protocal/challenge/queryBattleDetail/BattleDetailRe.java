package com.snail.webgame.game.protocal.challenge.queryBattleDetail;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class BattleDetailRe extends MessageBody {

	private byte chapterType;// 副本类型编号
	private short chapterNo;// 章节编号
	private int battleNo;//关卡
	private String starNum;//星级
	
	private byte showStory; //是否触发剧情 （1-已触发，0-未触发）
	
	private int canFightNum;//该副本可攻击次数 //-1无次数限制,0-不可攻击,
	private long fightTime;//该战斗上次攻击时间 (可攻击次数为正时,才倒计时,战斗间隔时间在配置文件获取-配置为0时无间隔)
	
	private byte goldNum; //金币购买次数

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("chapterType", 0);
		ps.add("chapterNo", 0);
		ps.add("battleNo", 0);
		ps.addString("starNum", "flashCode", 0);
		ps.add("showStory", 0);
		
		ps.add("canFightNum", 0);
		ps.add("fightTime", 0);
		ps.add("goldNum", 0);
	}
	
	public BattleDetailRe(byte chapterType, short chapterNo, int battleNo, String starNum,int canFightNum,long fightTime,byte showStory, byte goldNum) {
		this.chapterType = chapterType;
		this.chapterNo = chapterNo;
		this.battleNo = battleNo;
		this.starNum = starNum;
		this.canFightNum = canFightNum;
		this.fightTime = fightTime;
		this.showStory = showStory;
		this.goldNum = goldNum;
	}

	public byte getChapterType() {
		return chapterType;
	}

	public void setChapterType(byte chapterType) {
		this.chapterType = chapterType;
	}

	public short getChapterNo() {
		return chapterNo;
	}

	public void setChapterNo(short chapterNo) {
		this.chapterNo = chapterNo;
	}

	public int getBattleNo() {
		return battleNo;
	}

	public void setBattleNo(int battleNo) {
		this.battleNo = battleNo;
	}

	public String getStarNum() {
		return starNum;
	}

	public void setStarNum(String starNum) {
		this.starNum = starNum;
	}

	public int getCanFightNum() {
		return canFightNum;
	}

	public void setCanFightNum(int canFightNum) {
		this.canFightNum = canFightNum;
	}

	public long getFightTime() {
		return fightTime;
	}

	public void setFightTime(long fightTime) {
		this.fightTime = fightTime;
	}

	public byte getShowStory() {
		return showStory;
	}

	public void setShowStory(byte showStory) {
		this.showStory = showStory;
	}

	public byte getGoldNum() {
		return goldNum;
	}

	public void setGoldNum(byte goldNum) {
		this.goldNum = goldNum;
	}
	
}
