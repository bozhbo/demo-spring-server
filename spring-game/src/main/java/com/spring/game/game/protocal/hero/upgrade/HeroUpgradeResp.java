package com.snail.webgame.game.protocal.hero.upgrade;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

import com.snail.webgame.game.protocal.hero.query.HeroInfoRe;

public class HeroUpgradeResp extends MessageBody {

	private int result;
	private HeroInfoRe heroInfo = new HeroInfoRe();
	
	private byte sourceType;//1:银子	2:金子
	private int sourceChange;//资源变动数,正值为增加,负值为减少

	protected void setSequnce(ProtocolSequence ps) {
		ps.add("result", 0);
		ps.addObject("heroInfo");
		
		ps.add("sourceType", 0);
		ps.add("sourceChange", 0);
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

	public byte getSourceType() {
		return sourceType;
	}

	public void setSourceType(byte sourceType) {
		this.sourceType = sourceType;
	}

	public int getSourceChange() {
		return sourceChange;
	}

	public void setSourceChange(int sourceChange) {
		this.sourceChange = sourceChange;
	}
	
	
}
