package com.snail.webgame.game.protocal.fightdeploy.change;

import java.util.ArrayList;
import java.util.List;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class DeployChangeResp extends MessageBody {

	private int result;

	private int heroId;// 英雄ID
	private byte position;// 位置
	private int fightValue;// 战斗力

	private int changeHeroId;// 被交换的英雄ID 0-未交换
	private byte changeHeroPosition;// 被交换的英雄位置
	private int changeHeroFightValue;// 战斗力

	// 羁绊引起的武将战斗力变化
	private int count;
	private List<DeployChangeJBRe> list = new ArrayList<DeployChangeJBRe>();

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("result", 0);

		ps.add("heroId", 0);
		ps.add("position", 0);
		ps.add("fightValue", 0);

		ps.add("changeHeroId", 0);
		ps.add("changeHeroPosition", 0);
		ps.add("changeHeroFightValue", 0);

		ps.add("count", 0);
		ps.addObjectArray("list", "com.snail.webgame.game.protocal.fightdeploy.change.DeployChangeJBRe", "count");
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public int getHeroId() {
		return heroId;
	}

	public void setHeroId(int heroId) {
		this.heroId = heroId;
	}

	public byte getPosition() {
		return position;
	}

	public void setPosition(byte position) {
		this.position = position;
	}

	public int getFightValue() {
		return fightValue;
	}

	public void setFightValue(int fightValue) {
		this.fightValue = fightValue;
	}

	public int getChangeHeroFightValue() {
		return changeHeroFightValue;
	}

	public void setChangeHeroFightValue(int changeHeroFightValue) {
		this.changeHeroFightValue = changeHeroFightValue;
	}

	public int getChangeHeroId() {
		return changeHeroId;
	}

	public void setChangeHeroId(int changeHeroId) {
		this.changeHeroId = changeHeroId;
	}

	public byte getChangeHeroPosition() {
		return changeHeroPosition;
	}

	public void setChangeHeroPosition(byte changeHeroPosition) {
		this.changeHeroPosition = changeHeroPosition;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public List<DeployChangeJBRe> getList() {
		return list;
	}

	public void setList(List<DeployChangeJBRe> list) {
		this.list = list;
	}
}
