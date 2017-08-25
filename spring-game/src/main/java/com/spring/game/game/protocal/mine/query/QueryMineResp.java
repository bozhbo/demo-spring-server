package com.snail.webgame.game.protocal.mine.query;

import java.util.ArrayList;
import java.util.List;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class QueryMineResp extends MessageBody {

	private int result;
	private int mineNum;// 当日矿抢夺次数
	private int mineLimit;// 当日矿抢夺次数上限
	private int buyMine;// 当日抢夺购买次数

	// 查询自己占领协助矿信息
	private String idStr;// 删除的矿点 (mineid,mineid,...)
	private int count;
	private List<MineInfoRe> list = new ArrayList<MineInfoRe>();

	// 放弃，被占领，采集完成 可以领取收益的矿点
	private int size;
	private List<MineInfoRe> endlist = new ArrayList<MineInfoRe>();

	// 新防守记录
	private int defendCount;
	private List<MineDefendLogRe> defendList = new ArrayList<MineDefendLogRe>();

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("result", 0);
		
		ps.add("mineNum", 0);
		ps.add("mineLimit", 0);
		ps.add("buyMine", 0);	
		
		ps.addString("idStr", "flashCode", 0);
		ps.add("count", 0);
		ps.addObjectArray("list", "com.snail.webgame.game.protocal.mine.query.MineInfoRe", "count");

		ps.add("size", 0);
		ps.addObjectArray("endlist", "com.snail.webgame.game.protocal.mine.query.MineInfoRe", "size");

		ps.add("defendCount", 0);
		ps.addObjectArray("defendList", "com.snail.webgame.game.protocal.mine.query.MineDefendLogRe", "defendCount");
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public int getMineNum() {
		return mineNum;
	}

	public void setMineNum(int mineNum) {
		this.mineNum = mineNum;
	}

	public int getMineLimit() {
		return mineLimit;
	}

	public void setMineLimit(int mineLimit) {
		this.mineLimit = mineLimit;
	}

	public int getBuyMine() {
		return buyMine;
	}

	public void setBuyMine(int buyMine) {
		this.buyMine = buyMine;
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

	public List<MineInfoRe> getList() {
		return list;
	}

	public void setList(List<MineInfoRe> list) {
		this.list = list;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public List<MineInfoRe> getEndlist() {
		return endlist;
	}

	public void setEndlist(List<MineInfoRe> endlist) {
		this.endlist = endlist;
	}

	public int getDefendCount() {
		return defendCount;
	}

	public void setDefendCount(int defendCount) {
		this.defendCount = defendCount;
	}

	public List<MineDefendLogRe> getDefendList() {
		return defendList;
	}

	public void setDefendList(List<MineDefendLogRe> defendList) {
		this.defendList = defendList;
	}
}
