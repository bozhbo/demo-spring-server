package com.snail.webgame.game.protocal.hero.recruitList;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class QueryRecuitHeroRe extends MessageBody {
	
	private int no;//英雄编号
	private int costNum;//消耗星石数量
	private int costSilver;//消耗银子
    private int remainNum;//拥有的星石数量
    private int star;//英雄星级，用于排序
    private int lackNum;//缺少的星石数量，用于排序

	public int getStar() {
		return star;
	}

	public void setStar(int star) {
		this.star = star;
	}

	public int getLackNum() {
		return lackNum;
	}

	public void setLackNum(int lackNum) {
		this.lackNum = lackNum;
	}

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public int getCostNum() {
		return costNum;
	}

	public void setCostNum(int costNum) {
		this.costNum = costNum;
	}

	public int getCostSilver() {
		return costSilver;
	}

	public void setCostSilver(int costSilver) {
		this.costSilver = costSilver;
	}

	public int getRemainNum() {
		return remainNum;
	}

	public void setRemainNum(int remainNum) {
		this.remainNum = remainNum;
	}

	@Override
	protected void setSequnce(ProtocolSequence ps) {

		ps.add("no", 0);
		ps.add("costNum", 0);
		ps.add("costSilver", 0);
		ps.add("remainNum", 0);
		
	}

}
