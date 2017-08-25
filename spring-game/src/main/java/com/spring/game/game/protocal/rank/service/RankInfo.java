package com.snail.webgame.game.protocal.rank.service;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class RankInfo extends MessageBody  {

	private int roleId; //玩家ID
	private int heroNo; //主武将编号
	private int level; //玩家等级
	private int fightValue;//玩家战斗力
	private String name; //玩家名称
	private long perMax;//boss伤害排行单次最大伤害
	private long param; // 英雄等级、战斗力、跨服竞技场、boss伤害，英雄数量
	private int rankNum; //排名
	
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("roleId", 0);
		ps.add("heroNo",0);
		ps.add("level", 0);
		ps.addString("name", "flashCode", 0);
		ps.add("perMax", 0);
		ps.add("param", 0);
		ps.add("rankNum", 0);
	}
	
	public int getRoleId() {
		return roleId;
	}
	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}
	
	public int getHeroNo(){
		return heroNo;
	}
	public void setHeroNo(int heroNo){
		this.heroNo = heroNo;
	}
	
	public long getPerMax(){
		return perMax;
	}
	public void setPerMax(long perMax){
		this.perMax = perMax;
	}
	
	public long getParam() {
		return param;
	}

	public void setParam(long param) {
		this.param = param;
	}

	public int getRankNum() {
		return rankNum;
	}
	public void setRankNum(int rankNum) {
		this.rankNum = rankNum;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getFightValue() {
		return fightValue;
	}

	public void setFightValue(int fightValue) {
		this.fightValue = fightValue;
	}

}
