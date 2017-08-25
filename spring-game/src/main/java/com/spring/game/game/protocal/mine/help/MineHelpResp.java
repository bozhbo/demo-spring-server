package com.snail.webgame.game.protocal.mine.help;

import java.util.ArrayList;
import java.util.List;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

import com.snail.webgame.game.protocal.mine.query.MineInfoRe;

public class MineHelpResp extends MessageBody {

	private int result;
	private int action;// 0-检测请求 1-同意

	// 查询自己占领协助矿信息
	private int count;
	private List<MineInfoRe> list = new ArrayList<MineInfoRe>();

	private MineInfoRe mineInfo = new MineInfoRe();

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("result", 0);
		ps.add("action", 0);

		ps.add("count", 0);
		ps.addObjectArray("list", "com.snail.webgame.game.protocal.mine.query.MineInfoRe", "count");

		ps.addObject("mineInfo");
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public int getAction() {
		return action;
	}

	public void setAction(int action) {
		this.action = action;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public List<MineInfoRe> getList() {
		return list;
	}

	public void setList(List<MineInfoRe> list) {
		this.list = list;
	}

	public MineInfoRe getMineInfo() {
		return mineInfo;
	}

	public void setMineInfo(MineInfoRe mineInfo) {
		this.mineInfo = mineInfo;
	}
}
