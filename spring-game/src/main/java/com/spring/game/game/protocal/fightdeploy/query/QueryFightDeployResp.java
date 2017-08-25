package com.snail.webgame.game.protocal.fightdeploy.query;

import java.util.List;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

/**
 * 查询布阵结果
 * @author zenggang
 */
public class QueryFightDeployResp extends MessageBody {

	private int result;
	private byte deployType;// 1-普通布阵 -2-竞技场防守阵营
	private int count;
	private List<FightDeployRe> list;

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("result", 0);
		ps.add("deployType", 0);
		ps.add("count", 0);
		ps.addObjectArray("list", "com.snail.webgame.game.protocal.fightdeploy.query.FightDeployRe", "count");
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public byte getDeployType() {
		return deployType;
	}

	public void setDeployType(byte deployType) {
		this.deployType = deployType;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public List<FightDeployRe> getList() {
		return list;
	}

	public void setList(List<FightDeployRe> list) {
		this.list = list;
	}
}
