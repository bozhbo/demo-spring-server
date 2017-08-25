package com.snail.webgame.game.protocal.worldBoss.query;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

/**
 * 查询世界Boss个人信息
 * 
 * @author zhangyq
 * 
 */
public class QueryWorldBossResp extends MessageBody {

	private int result;
	private int rank; 		//实时排行
	private long bestAttHp;	//历史最高伤害
	private long todayAttHp;//今日伤害
	private long nextTime;	//再次挑战时间
	private long allHp;//总血量
	private long nowHp;//当前血量
	private int bossLv;//BOSS等级

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("result", 0);
		ps.add("rank", 0);
		ps.add("bestAttHp", 0);
		ps.add("todayAttHp", 0);
		ps.add("nextTime", 0);
		ps.add("allHp", 0);
		ps.add("nowHp", 0);
		ps.add("bossLv",0);
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public long getBestAttHp() {
		return bestAttHp;
	}

	public void setBestAttHp(long bestAttHp) {
		this.bestAttHp = bestAttHp;
	}

	public long getTodayAttHp() {
		return todayAttHp;
	}

	public void setTodayAttHp(long todayAttHp) {
		this.todayAttHp = todayAttHp;
	}

	public long getNextTime() {
		return nextTime;
	}

	public void setNextTime(long nextTime) {
		this.nextTime = nextTime;
	}

	public long getAllHp() {
		return allHp;
	}

	public void setAllHp(long allHp) {
		this.allHp = allHp;
	}

	public long getNowHp() {
		return nowHp;
	}

	public void setNowHp(long nowHp) {
		this.nowHp = nowHp;
	}

	public int getBossLv() {
		return bossLv;
	}

	public void setBossLv(int bossLv) {
		this.bossLv = bossLv;
	}
	
}
