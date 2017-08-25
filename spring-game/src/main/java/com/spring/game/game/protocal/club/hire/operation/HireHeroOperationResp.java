package com.snail.webgame.game.protocal.club.hire.operation;

import java.util.List;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

import com.snail.webgame.game.protocal.fight.fightend.BattlePrize;

public class HireHeroOperationResp extends MessageBody{
	private int result;
	private String sourceChange;// sourceType:changeNumber;sourceType:changeNumber(changeNumber存在负数情况)
	private int count;
	private List<BattlePrize> list; //召回后获得的物品
	private int flag; // 0 - 召回 ，1 - 雇佣  2 - 派出
	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("result", 0);
		ps.addString("sourceChange", "flashCode", 0);
		ps.add("count", 0);
		ps.addObjectArray("list", "com.snail.webgame.game.protocal.fight.fightend.BattlePrize", "count");
		ps.add("flag", 0);
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public String getSourceChange() {
		return sourceChange;
	}

	public void setSourceChange(String sourceChange) {
		this.sourceChange = sourceChange;
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

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

}
