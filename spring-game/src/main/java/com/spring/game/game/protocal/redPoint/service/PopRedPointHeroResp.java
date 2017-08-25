package com.snail.webgame.game.protocal.redPoint.service;

import java.util.ArrayList;
import java.util.List;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class PopRedPointHeroResp extends MessageBody {
	private int result = 1;
	private int heroInfoResSize;
	private List<PopRedPointHeroInfoRe> heroInfoRes = new ArrayList<PopRedPointHeroInfoRe>();
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("result", 0);
		ps.add("heroInfoResSize", 0);
		ps.addObjectArray("heroInfoRes", "com.snail.webgame.game.protocal.redPoint.service.PopRedPointHeroInfoRe", "heroInfoResSize");
	}
	public int getResult() {
		return result;
	}
	public void setResult(int result) {
		this.result = result;
	}
	public int getHeroInfoResSize() {
		return heroInfoResSize;
	}
	public void setHeroInfoResSize(int heroInfoResSize) {
		this.heroInfoResSize = heroInfoResSize;
	}
	public List<PopRedPointHeroInfoRe> getHeroInfoRes() {
		return heroInfoRes;
	}
	public void setHeroInfoRes(List<PopRedPointHeroInfoRe> heroInfoRes) {
		this.heroInfoRes = heroInfoRes;
	}
}
