package com.snail.webgame.game.protocal.quest.query;

import java.util.ArrayList;
import java.util.List;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class QueryQuestResp extends MessageBody {

	private int result;
	private String noStr;
	private int count;
	private List<QuestInfoRe> list = new ArrayList<QuestInfoRe>();

	protected void setSequnce(ProtocolSequence ps) {
		ps.add("result", 0);
		ps.addString("noStr", "flashCode", 0);
		ps.add("count", 0);
		ps.addObjectArray("list", "com.snail.webgame.game.protocal.quest.query.QuestInfoRe", "count");
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public String getNoStr() {
		return noStr;
	}

	public void setNoStr(String noStr) {
		this.noStr = noStr;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public List<QuestInfoRe> getList() {
		return list;
	}

	public void setList(List<QuestInfoRe> list) {
		this.list = list;
	}
}
