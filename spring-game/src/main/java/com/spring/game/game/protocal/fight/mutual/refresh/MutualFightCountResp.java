package com.snail.webgame.game.protocal.fight.mutual.refresh;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

/**
 * 
 * 类介绍:战斗次数刷新
 *
 * @author zhoubo
 * @2015年6月4日
 */
public class MutualFightCountResp extends MessageBody {
	
	private int fightCount; // 今天战斗次数

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("fightCount", 0);
	}

	public int getFightCount() {
		return fightCount;
	}

	public void setFightCount(int fightCount) {
		this.fightCount = fightCount;
	}
	
	
}
