package com.snail.webgame.game.protocal.mine.query;

import java.util.ArrayList;
import java.util.List;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class MineInfoRe extends MessageBody {

	private int mineId;// id（唯一）
	private int position;// 矿位置编号
	private int mineNo; // 矿类型编号
	private long createTime;// 矿生成时间

	// 角色在该矿的防守阵形
	private int count;
	private List<MineHeroRe> heros = new ArrayList<MineHeroRe>();

	// 矿占领(协防)信息
	private MineRoleRe zlDetail = new MineRoleRe();

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("mineId", 0);
		ps.add("position", 0);
		ps.add("mineNo", 0);
		ps.add("createTime", 0);
		
		ps.add("count", 0);
		ps.addObjectArray("heros", "com.snail.webgame.game.protocal.mine.query.MineHeroRe", "count");
		
		ps.addObject("zlDetail");
	}

	public int getMineId() {
		return mineId;
	}

	public void setMineId(int mineId) {
		this.mineId = mineId;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public int getMineNo() {
		return mineNo;
	}

	public void setMineNo(int mineNo) {
		this.mineNo = mineNo;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public List<MineHeroRe> getHeros() {
		return heros;
	}

	public void setHeros(List<MineHeroRe> heros) {
		this.heros = heros;
	}

	public MineRoleRe getZlDetail() {
		return zlDetail;
	}

	public void setZlDetail(MineRoleRe zlDetail) {
		this.zlDetail = zlDetail;
	}
}
