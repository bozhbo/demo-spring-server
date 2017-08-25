package com.snail.webgame.game.protocal.club.hire.info;

import java.util.List;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

import com.snail.webgame.game.protocal.club.hire.entity.HireHeroInfoRe;

public class GetClubHireHeroInfoResp extends MessageBody {
	private int result;
	private int flag; //fightType
	private int count;
	private List<HireHeroInfoRe> list;

	

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("result", 0);
		ps.add("flag", 0);
		ps.add("count", 0);
		ps.addObjectArray("list", "com.snail.webgame.game.protocal.club.hire.entity.HireHeroInfoRe", "count");
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

	public List<HireHeroInfoRe> getList() {
		return list;
	}

	public void setList(List<HireHeroInfoRe> list) {
		this.list = list;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}
}
