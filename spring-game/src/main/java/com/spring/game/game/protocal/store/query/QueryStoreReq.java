package com.snail.webgame.game.protocal.store.query;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class QueryStoreReq extends MessageBody {

	private byte storeType;// 积分排名赛（新竞技场） 商店  2-征战四方 商店 3-工会商店 4普通商店 5-跨服商店 6-战功商店 7-黑市商店 8-装备商城

	protected void setSequnce(ProtocolSequence ps) {
		ps.add("storeType", 0);
	}

	public byte getStoreType() {
		return storeType;
	}

	public void setStoreType(byte storeType) {
		this.storeType = storeType;
	}
}
