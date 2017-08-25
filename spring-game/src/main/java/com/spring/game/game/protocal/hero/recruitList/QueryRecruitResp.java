package com.snail.webgame.game.protocal.hero.recruitList;

import java.util.List;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class QueryRecruitResp extends MessageBody {
	
	private int result;
	private int heroListCount;
	private List<QueryRecuitHeroRe> recruitHeroList;//招募英雄列表

	@Override
	protected void setSequnce(ProtocolSequence ps) {

		ps.add("result", 0);
		ps.add("heroListCount", 0);
		ps.addObjectArray("recruitHeroList", "com.snail.webgame.game.protocal.hero.recruitList.QueryRecuitHeroRe", "heroListCount");
		
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public List<QueryRecuitHeroRe> getRecruitHeroList() {
		return recruitHeroList;
	}

	public void setRecruitHeroList(List<QueryRecuitHeroRe> recruitHeroList) {
		this.recruitHeroList = recruitHeroList;
	}

	public int getHeroListCount() {
		return heroListCount;
	}

	public void setHeroListCount(int heroListCount) {
		this.heroListCount = heroListCount;
	}

}
