package com.snail.webgame.game.protocal.countryfight.queryFightingClub;

import java.util.List;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

import com.snail.webgame.game.protocal.countryfight.common.FightingClubRe;

/**
 * 城池战斗信息
 * 
 * @author xiasd
 *
 */
public class QueryFightingClubResp extends MessageBody{
	private List<FightingClubRe> list;
	private int count;

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.addObjectArray("list", "com.snail.webgame.game.protocal.countryfight.common.FightingClubRe", "count");
		ps.add("count", 0);
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public List<FightingClubRe> getList() {
		return list;
	}

	public void setList(List<FightingClubRe> list) {
		this.list = list;
	}
	
}
