package com.snail.webgame.game.protocal.fight.mutual.refresh;

import java.util.List;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

import com.snail.webgame.game.protocal.fight.fightend.BattlePrize;

/**
 * 
 * 类介绍:对攻战结束后奖励刷新
 *
 * @author zhoubo
 * @2015年6月4日
 */
public class MutualPrizeResp extends MessageBody {

	private byte fightResult; // 战斗结果  1-失败 2-胜利 3-平局
	
	private int fpPrizeNum;
	private List<BattlePrize> fpPrize;// 翻牌获得奖励
	
	private int count;
	private List<BattlePrize> list;// 普通掉落
	
	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("fightResult", 0);
		
		ps.add("fpPrizeNum", 0);
		ps.addObjectArray("fpPrize", "com.snail.webgame.game.protocal.fight.fightend.BattlePrize", "fpPrizeNum");
		
		ps.add("count", 0);
		ps.addObjectArray("list", "com.snail.webgame.game.protocal.fight.fightend.BattlePrize", "count");
		
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public List<BattlePrize> getList() {
		return list;
	}

	public void setList(List<BattlePrize> list) {
		this.list = list;
	}

	public int getFpPrizeNum() {
		return fpPrizeNum;
	}

	public void setFpPrizeNum(int fpPrizeNum) {
		this.fpPrizeNum = fpPrizeNum;
	}

	public List<BattlePrize> getFpPrize() {
		return fpPrize;
	}

	public void setFpPrize(List<BattlePrize> fpPrize) {
		this.fpPrize = fpPrize;
	}

	public byte getFightResult() {
		return fightResult;
	}

	public void setFightResult(byte fightResult) {
		this.fightResult = fightResult;
	}
	
	
}
