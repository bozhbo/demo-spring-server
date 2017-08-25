package com.snail.webgame.game.protocal.rank.rank;

import java.util.ArrayList;
import java.util.List;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

import com.snail.webgame.game.protocal.club.entity.ClubFightInfo;
import com.snail.webgame.game.protocal.rank.service.RankInfo;

public class RankResp extends MessageBody {

	private int result;
	private int rankType; //排行榜类型 1-等级 2-战斗力  3-世界boss伤害 4-武将数量
	private int count;
	private List<RankInfo> list = new ArrayList<RankInfo>();
	
	private int myRank; //个人排名
	private long myParam; //个人排名参数
	private long myPerMax;//世界BOSS排行中个人单次最大伤害
	
	private int clubCount;
	private List<ClubFightInfo> clubList = new ArrayList<ClubFightInfo>();

	protected void setSequnce(ProtocolSequence ps) {
		ps.add("result", 0);
		ps.add("rankType", 0);
		ps.add("count", 0);
		ps.addObjectArray("list", "com.snail.webgame.game.protocal.rank.service.RankInfo", "count");
		
		ps.add("myRank", 0);
		ps.add("myParam", 0);
		ps.add("myPerMax",0);
		
		ps.add("clubCount", 0);
		ps.addObjectArray("clubList", "com.snail.webgame.game.protocal.club.entity.ClubFightInfo", "count");
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getRankType() {
		return rankType;
	}

	public void setRankType(int rankType) {
		this.rankType = rankType;
	}

	public List<RankInfo> getList() {
		return list;
	}

	public void setList(List<RankInfo> list) {
		this.list = list;
	}

	public int getMyRank() {
		return myRank;
	}

	public void setMyRank(int myRank) {
		this.myRank = myRank;
	}

	public long getMyPerMax(){
		return myPerMax;
	}
	
	public void setMyPerMax(long myPerMax){
		this.myPerMax = myPerMax;
	}

	public long getMyParam() {
		return myParam;
	}

	public void setMyParam(long myParam) {
		this.myParam = myParam;
	}

	public int getClubCount() {
		return clubCount;
	}

	public void setClubCount(int clubCount) {
		this.clubCount = clubCount;
	}

	public List<ClubFightInfo> getClubList() {
		return clubList;
	}

	public void setClubList(List<ClubFightInfo> clubList) {
		this.clubList = clubList;
	}
	
	
}
