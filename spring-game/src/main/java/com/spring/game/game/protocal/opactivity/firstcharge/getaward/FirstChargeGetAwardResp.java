package com.snail.webgame.game.protocal.opactivity.firstcharge.getaward;

import java.util.ArrayList;
import java.util.List;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

import com.snail.webgame.game.protocal.recruit.recruit.ChestItemRe;

public class FirstChargeGetAwardResp extends MessageBody {

	private int result;
	private byte actType;// 活动类型 1-首充 2-手机绑定
	
	// 用于客户端播特效奖励武将是否转化星石
	private int count;
	private List<ChestItemRe> list = new ArrayList<ChestItemRe>();

	protected void setSequnce(ProtocolSequence ps) {
		ps.add("result", 0);
		ps.add("actType", 0);
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

	public byte getActType() {
		return actType;
	}

	public void setActType(byte actType) {
		this.actType = actType;
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
