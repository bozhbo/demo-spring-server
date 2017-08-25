package com.snail.webgame.game.protocal.hero.merge;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

import com.snail.webgame.game.protocal.hero.query.HeroDetailRe;

public class MergeHeroResp extends MessageBody {

	private int result;
	private HeroDetailRe heroDetail = new HeroDetailRe();

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("result", 0);
		ps.addObject("heroDetail");
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public HeroDetailRe getHeroDetail() {
		return heroDetail;
	}

	public void setHeroDetail(HeroDetailRe heroDetail) {
		this.heroDetail = heroDetail;
	}
}
