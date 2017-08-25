package com.snail.webgame.game.protocal.scene.queryOtherAI;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class OtherHeroInfo extends MessageBody{

	private int heroNo; //武将编号
	private byte star;  //星级
	private byte quality; // 品质
	private byte position;
	
	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("heroNo", 0);
		ps.add("star", 0);
		ps.add("quality", 0);
		ps.add("position", 0);
	}

	public int getHeroNo() {
		return heroNo;
	}

	public void setHeroNo(int heroNo) {
		this.heroNo = heroNo;
	}

	public byte getStar() {
		return star;
	}

	public void setStar(byte star) {
		this.star = star;
	}

	public byte getQuality() {
		return quality;
	}

	public void setQuality(byte quality) {
		this.quality = quality;
	}

	public byte getPosition() {
		return position;
	}

	public void setPosition(byte position) {
		this.position = position;
	}

	
}

