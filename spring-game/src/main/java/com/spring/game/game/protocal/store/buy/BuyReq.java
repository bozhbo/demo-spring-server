package com.snail.webgame.game.protocal.store.buy;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class BuyReq extends MessageBody {

	private byte storeType;// 1-竞技场商店  2-征战四方商店 3-公会商店 4-普通商店 5-跨服商店
	private int storeItemId;// 商品Id

	protected void setSequnce(ProtocolSequence ps) {
		ps.add("storeType", 0);
		ps.add("storeItemId", 0);
	}

	public byte getStoreType() {
		return storeType;
	}

	public void setStoreType(byte storeType) {
		this.storeType = storeType;
	}

	public int getStoreItemId() {
		return storeItemId;
	}

	public void setStoreItemId(int storeItemId) {
		this.storeItemId = storeItemId;
	}
}
