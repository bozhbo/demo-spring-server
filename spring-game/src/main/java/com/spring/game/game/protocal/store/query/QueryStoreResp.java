package com.snail.webgame.game.protocal.store.query;

import java.util.ArrayList;
import java.util.List;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class QueryStoreResp extends MessageBody {

	private int result;
	private byte storeType;	//1-积分排名赛（新竞技场） 商店 2-征战四方 商店
										//3-工会商店 4-普通商店 5-跨服商店
										//6-战功商店 7-黑市商店 8-装备商店
										//9-异域商店 10-组队PVP商店
	private byte currencyType;// 商店货币类型  1:money银子 2:gold金子 8:courage 竞技场货币 勇气点  
							  //9:justice 征战四方货币 正义点 65:clubContribution 工会币  28:kuafumoney-跨服币 32:exploit-战功
											//
	private long currencyNum;// 商店货币总量
	private long refreshTime;// 刷新时间
	private int buyNum;// 当日手动刷新次数
	
	private long leaveTime;//商人离开时间
	
	private int count;
	private List<StoreItemRe> list = new ArrayList<StoreItemRe>();

	protected void setSequnce(ProtocolSequence ps) {
		ps.add("result", 0);
		ps.add("storeType", 0);
		ps.add("currencyType", 0);
		ps.add("currencyNum", 0);
		ps.add("refreshTime", 0);
		ps.add("buyNum", 0);
		ps.add("count", 0);
		ps.addObjectArray("list", "com.snail.webgame.game.protocal.store.query.StoreItemRe", "count");
		ps.add("leaveTime",0);
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public byte getStoreType() {
		return storeType;
	}

	public void setStoreType(byte storeType) {
		this.storeType = storeType;
	}

	public byte getCurrencyType() {
		return currencyType;
	}

	public void setCurrencyType(byte currencyType) {
		this.currencyType = currencyType;
	}

	public long getCurrencyNum() {
		return currencyNum;
	}

	public void setCurrencyNum(long currencyNum) {
		this.currencyNum = currencyNum;
	}

	public long getRefreshTime() {
		return refreshTime;
	}

	public void setRefreshTime(long refreshTime) {
		this.refreshTime = refreshTime;
	}

	public int getBuyNum() {
		return buyNum;
	}

	public void setBuyNum(int buyNum) {
		this.buyNum = buyNum;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public List<StoreItemRe> getList() {
		return list;
	}

	public void setList(List<StoreItemRe> list) {
		this.list = list;
	}

	public long getLeaveTime() {
		return leaveTime;
	}

	public void setLeaveTime(long leaveTime) {
		this.leaveTime = leaveTime;
	}
	
}
