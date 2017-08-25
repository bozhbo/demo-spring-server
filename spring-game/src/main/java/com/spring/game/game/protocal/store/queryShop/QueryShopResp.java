package com.snail.webgame.game.protocal.store.queryShop;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class QueryShopResp extends MessageBody {

	private int result;
	private int goldShop; //黑市商店 1-显示 0-不显示
	private int TurkShop; //异域商店 1-显示 0-不显示
	private int isBuyShop;//黑市商店是否购买 1-已买 0-未买
	private int isBuyTurkShop;//异域商城是否购买 1-已买 0-未买
	
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("result", 0);
		ps.add("goldShop", 0);
		ps.add("TurkShop", 0);
		ps.add("isBuyShop", 0);
		ps.add("isBuyTurkShop", 0);
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public int getGoldShop() {
		return goldShop;
	}

	public void setGoldShop(int goldShop) {
		this.goldShop = goldShop;
	}

	public int getTurkShop() {
		return TurkShop;
	}

	public void setTurkShop(int turkShop) {
		TurkShop = turkShop;
	}

	public int getIsBuyShop() {
		return isBuyShop;
	}

	public void setIsBuyShop(int isBuyShop) {
		this.isBuyShop = isBuyShop;
	}

	public int getIsBuyTurkShop() {
		return isBuyTurkShop;
	}

	public void setIsBuyTurkShop(int isBuyTurkShop) {
		this.isBuyTurkShop = isBuyTurkShop;
	}
	
}
