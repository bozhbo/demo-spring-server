package com.snail.webgame.game.protocal.item.query;

import java.util.ArrayList;
import java.util.List;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

import com.snail.webgame.game.protocal.equip.query.EquipDetailRe;

public class QueryEquipResp extends MessageBody {

	private int result;
	//装备信息
	private int count;
	private List<EquipDetailRe> list = new ArrayList<EquipDetailRe>();

	protected void setSequnce(ProtocolSequence ps) {
		ps.add("result", 0);
		ps.add("count", 0);
		ps.addObjectArray("list", "com.snail.webgame.game.protocal.equip.query.EquipDetailRe", "count");
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

	public List<EquipDetailRe> getList() {
		return list;
	}

	public void setList(List<EquipDetailRe> list) {
		this.list = list;
	}
	
}