package com.snail.webgame.game.protocal.hero.propUse;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

import com.snail.webgame.game.protocal.hero.query.HeroInfoRe;

public class HeroPropUseResp extends MessageBody {

	private int result;
	private HeroInfoRe heroInfo = new HeroInfoRe();// 属性

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("result", 0);
		ps.addObject("heroInfo");
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public HeroInfoRe getHeroInfo() {
		return heroInfo;
	}

	public void setHeroInfo(HeroInfoRe heroInfo) {
		this.heroInfo = heroInfo;
	}
}
