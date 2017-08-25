package com.snail.webgame.game.protocal.equip.heroQuery;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

/**
 * 英雄装备查询
 * @author zhangyq
 *
 */
public class QueryHeroEquipReq extends MessageBody {

	private int heroId;
	

	protected void setSequnce(ProtocolSequence ps) {
		ps.add("heroId", 0);
	}


	public int getHeroId() {
		return heroId;
	}


	public void setHeroId(int heroId) {
		this.heroId = heroId;
	}

	
}