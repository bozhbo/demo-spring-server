package com.snail.webgame.game.protocal.opactivity.sevenday.getaward;

import java.util.ArrayList;
import java.util.List;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

import com.snail.webgame.game.protocal.recruit.recruit.ChestItemRe;

public class GetSevenDayAwardResp extends MessageBody {

	private int result;
	private byte getDay;// 领取天数
	private int subNo;// 领取编号
	
	// 用于客户端播特效奖励武将是否转化星石
	private int count;
	private List<ChestItemRe> list = new ArrayList<ChestItemRe>();

	protected void setSequnce(ProtocolSequence ps) {
		ps.add("result", 0);
		ps.add("getDay", 0);
		ps.add("subNo", 0);
		ps.add("count", 0);
		ps.addObjectArray("list",
				"com.snail.webgame.game.protocal.recruit.recruit.ChestItemRe",
				"count");
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public byte getGetDay() {
		return getDay;
	}

	public void setGetDay(byte getDay) {
		this.getDay = getDay;
	}

	public int getSubNo() {
		return subNo;
	}

	public void setSubNo(int subNo) {
		this.subNo = subNo;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public List<ChestItemRe> getList() {
		return list;
	}

	public void setList(List<ChestItemRe> list) {
		this.list = list;
	}

}
