package com.snail.webgame.game.info;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.snail.webgame.engine.common.to.BaseTO;

public class ChallengeBattleInfo extends BaseTO {
	
	private byte challengeType; //副本类型 （1-剧情副本 2-精英副本）
	private int chapterNo;// 章节编号
	private int battleId;// 战场id

	private String star;// 星级 1,2,3
	private int stars; //获得星星的数量

	private int roleId;// 角色Id
	
	private int canFightNum;//该副本可攻击次数 //-1没有次数限制,0-不可攻击,前台理论上该副本不可点击
	private Timestamp fightTime;//该副本上次攻击时间
	
	private List<String> itemByNo = new ArrayList<String>(); //已掉落的必掉物品List 支持多个 （结构 itemNo+",”）
	private int itemByNum;//必掉物品轮回计数
	
	private int goldNum;//已购买次数

	public ChallengeBattleInfo() {

	}

	public ChallengeBattleInfo(int roleId,byte challengeType, int chapterNo, int battleId) {
		this.roleId = roleId;
		this.challengeType = challengeType;
		this.chapterNo = chapterNo;
		this.battleId = battleId;
	}
	
	public byte getChallengeType() {
		return challengeType;
	}

	public void setChallengeType(byte challengeType) {
		this.challengeType = challengeType;
	}

	public int getChapterNo() {
		return chapterNo;
	}

	public void setChapterNo(int chapterNo) {
		this.chapterNo = chapterNo;
	}

	public int getBattleId() {
		return battleId;
	}

	public void setBattleId(int battleId) {
		this.battleId = battleId;
	}

	public String getStar() {
		return star;
	}

	public void setStar(String star) {
		this.star = star;
	}

	public int getStars() {
		return stars;
	}

	public void setStars(int stars) {
		this.stars = stars;
	}

	public int getCanFightNum() {
		return canFightNum;
	}

	public void setCanFightNum(int canFightNum) {
		this.canFightNum = canFightNum;
	}

	public Timestamp getFightTime() {
		if(fightTime == null)
		{
			return new Timestamp(0);
		}
		return fightTime;
	}

	public void setFightTime(Timestamp fightTime) {
		this.fightTime = fightTime;
	}
	
	public List<String> getItemByNo() {
		return itemByNo;
	}

	public void setItemByNo(List<String> itemByNo) {
		this.itemByNo = itemByNo;
	}

	public int getItemByNum() {
		return itemByNum;
	}

	public void setItemByNum(int itemByNum) {
		this.itemByNum = itemByNum;
	}
	

	public int getGoldNum() {
		return goldNum;
	}

	public void setGoldNum(int goldNum) {
		this.goldNum = goldNum;
	}

	@Override
	public byte getSaveMode() {
		return 0;
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

}
