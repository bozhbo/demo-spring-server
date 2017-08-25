package com.snail.webgame.game.protocal.ride.query;

import java.util.List;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class QueryRideInfoResp extends MessageBody {
	private int result;
	private String openRideNoStr;// 已开坐骑编号 no,no
	private int count;
	private List<RideDetailRe> rideList;
	
	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("result", 0);
		ps.addString("openRideNoStr", "flashCode", 0);
		ps.add("count", 0);
		ps.addObjectArray("rideList", "com.snail.webgame.game.protocal.ride.query.RideDetailRe", "count");
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

	public List<RideDetailRe> getRideList() {
		return rideList;
	}

	public void setRideList(List<RideDetailRe> rideList) {
		this.rideList = rideList;
	}

	public String getOpenRideNoStr() {
		return openRideNoStr;
	}

	public void setOpenRideNoStr(String openRideNoStr) {
		this.openRideNoStr = openRideNoStr;
	}

}
