package com.snail.webgame.game.protocal.challenge.sweep;

import java.util.List;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

import com.snail.webgame.game.protocal.fight.fightend.BattlePrize;

public class SweepResp extends MessageBody {

	private int result;
	//获得的奖励
	private int prizeNum;
	private List<BattlePrize> prize;

	private byte chapterType;//副本类型
	private int chapterNo;//章节
	private int battleNo;//	 关卡
	
	private int canFightNum;//该副本可攻击次数 //-1无次数限制,0-不可攻击
	
	private int sweep;//扫荡次数
	
	private int refreshShop; //0-未刷新 1-刷新黑市商店 2-刷新异域商店 3-两个商店都刷新

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("result", 0);
		ps.add("prizeNum", 0);
		ps.addObjectArray("prize", "com.snail.webgame.game.protocal.fight.fightend.BattlePrize", "prizeNum");
		ps.add("chapterType", 0);
		ps.add("chapterNo", 0);
		ps.add("battleNo", 0);
		ps.add("canFightNum", 0);
		ps.add("sweep", 0);
		
		ps.add("refreshShop", 0);
	}

	
	public byte getChapterType() {
		return chapterType;
	}


	public void setChapterType(byte chapterType) {
		this.chapterType = chapterType;
	}


	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public int getPrizeNum() {
		return prizeNum;
	}

	public void setPrizeNum(int prizeNum) {
		this.prizeNum = prizeNum;
	}

	public List<BattlePrize> getPrize() {
		return prize;
	}

	public void setPrize(List<BattlePrize> prize) {
		this.prize = prize;
	}

	public int getChapterNo() {
		return chapterNo;
	}

	public void setChapterNo(int chapterNo) {
		this.chapterNo = chapterNo;
	}

	public int getBattleNo() {
		return battleNo;
	}

	public void setBattleNo(int battleNo) {
		this.battleNo = battleNo;
	}

	public int getCanFightNum() {
		return canFightNum;
	}

	public void setCanFightNum(int canFightNum) {
		this.canFightNum = canFightNum;
	}


	public int getSweep() {
		return sweep;
	}


	public void setSweep(int sweep) {
		this.sweep = sweep;
	}


	public int getRefreshShop() {
		return refreshShop;
	}


	public void setRefreshShop(int refreshShop) {
		this.refreshShop = refreshShop;
	}
	

}
