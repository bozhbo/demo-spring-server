package com.snail.webgame.game.protocal.challenge.refresh;

import java.util.List;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

import com.snail.webgame.game.protocal.challenge.queryBattleDetail.BattleDetailRe;

public class RefreshBattlesResp extends MessageBody {

	private int result;

	// 玩家可打的副本信息
	private int count;
	private List<BattleDetailRe> list;
	
	private String battle;

	protected void setSequnce(ProtocolSequence ps) {
		ps.add("result", 0);
		ps.add("count", 0);
		ps.addObjectArray("list", "com.snail.webgame.game.protocal.challenge.queryBattleDetail.BattleDetailRe", "count");
		ps.addString("battle", "flashCode", 0);
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

	public List<BattleDetailRe> getList() {
		return list;
	}

	public void setList(List<BattleDetailRe> list) {
		this.list = list;
	}

	public String getBattle() {
		return battle;
	}

	public void setBattle(String battle) {
		this.battle = battle;
	}
	
}
