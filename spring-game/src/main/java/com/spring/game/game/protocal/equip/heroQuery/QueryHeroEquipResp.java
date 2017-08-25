package com.snail.webgame.game.protocal.equip.heroQuery;

import java.util.ArrayList;
import java.util.List;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class QueryHeroEquipResp extends MessageBody {

	private int result;
	private int heroId;

	private int count;
	private List<HeroEquipDetailRe> list = new ArrayList<HeroEquipDetailRe>();

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("result", 0);
		ps.add("heroId", 0);

		ps.add("count", 0);
		ps.addObjectArray("list", "com.snail.webgame.game.protocal.equip.heroQuery.HeroEquipDetailRe", "count");
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public int getHeroId() {
		return heroId;
	}

	public void setHeroId(int heroId) {
		this.heroId = heroId;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public List<HeroEquipDetailRe> getList() {
		return list;
	}

	public void setList(List<HeroEquipDetailRe> list) {
		this.list = list;
	}
}
