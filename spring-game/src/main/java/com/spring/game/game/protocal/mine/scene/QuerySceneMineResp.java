package com.snail.webgame.game.protocal.mine.scene;

import java.util.ArrayList;
import java.util.List;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class QuerySceneMineResp extends MessageBody {

	private int result;
	private String idStr;

	private int count;
	private List<SceneMineRe> list = new ArrayList<SceneMineRe>();

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("result", 0);
		ps.addString("idStr", "flashCode", 0);

		ps.add("count", 0);
		ps.addObjectArray("list", "com.snail.webgame.game.protocal.mine.scene.SceneMineRe", "count");
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public String getIdStr() {
		return idStr;
	}

	public void setIdStr(String idStr) {
		this.idStr = idStr;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public List<SceneMineRe> getList() {
		return list;
	}

	public void setList(List<SceneMineRe> list) {
		this.list = list;
	}
}
