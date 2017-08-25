package com.snail.webgame.game.protocal.club.entity;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class ClubFightInfo extends MessageBody {
	private int clubId;
	private String clubName;
	private int imageId; // 公会图标
	private long totalFight;
	private int level; // 公会等级
	private int rankNum; //排名
	
	
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("clubId", 0);
		ps.addString("clubName","flashCode",0);
		ps.add("imageId", 0);
		ps.add("totalFight", 0);
		ps.add("level", 0);
		ps.add("rankNum", 0);
	}
	
	public int getClubId() {
		return clubId;
	}
	public void setClubId(int clubId) {
		this.clubId = clubId;
	}
	public String getClubName() {
		return clubName;
	}
	public void setClubName(String clubName) {
		this.clubName = clubName;
	}
	public int getImageId() {
		return imageId;
	}
	public void setImageId(int imageId) {
		this.imageId = imageId;
	}

	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public int getRankNum() {
		return rankNum;
	}
	public void setRankNum(int rankNum) {
		this.rankNum = rankNum;
	}
	
	public long getTotalFight() {
		return totalFight;
	}
	public void setTotalFight(long totalFight) {
		this.totalFight = totalFight;
	}
	
	

}
