package com.snail.webgame.game.protocal.attackAnother.match;

import java.util.ArrayList;
import java.util.List;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;
import com.snail.webgame.game.protocal.fightdeploy.view.FightDeployDetailRe;

public class AttackAnotherMatchResp extends MessageBody {
	
	private int result;
	private byte remainTime;
	private int sideRoleId;
	private String sideRoleName;
	private int sildeRoleLevel;
	private int sideRoleFightValue;
	private int matchHeroListSize;
	private List<FightDeployDetailRe> matchHeroList = new ArrayList<FightDeployDetailRe>();

	@Override
	protected void setSequnce(ProtocolSequence ps) {

		ps.add("result", 0);
		ps.add("remainTime", 0);
		ps.add("sideRoleId", 0);
		ps.addString("sideRoleName", "flashCode", 0);
		ps.add("sildeRoleLevel", 0);
		ps.add("sideRoleFightValue", 0);
		ps.add("matchHeroListSize", 0);
		ps.addObjectArray("matchHeroList", "com.snail.webgame.game.protocal.fightdeploy.view.FightDeployDetailRe", "matchHeroListSize");
		
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public String getSideRoleName() {
		return sideRoleName;
	}

	public void setSideRoleName(String sideRoleName) {
		this.sideRoleName = sideRoleName;
	}

	public int getSildeRoleLevel() {
		return sildeRoleLevel;
	}

	public void setSildeRoleLevel(int sildeRoleLevel) {
		this.sildeRoleLevel = sildeRoleLevel;
	}

	public List<FightDeployDetailRe> getMatchHeroList() {
		return matchHeroList;
	}

	public void setMatchHeroList(List<FightDeployDetailRe> matchHeroList) {
		this.matchHeroList = matchHeroList;
	}

	public int getMatchHeroListSize() {
		return matchHeroListSize;
	}

	public void setMatchHeroListSize(int matchHeroListSize) {
		this.matchHeroListSize = matchHeroListSize;
	}

	public int getSideRoleId() {
		return sideRoleId;
	}

	public void setSideRoleId(int sideRoleId) {
		this.sideRoleId = sideRoleId;
	}

	public int getSideRoleFightValue() {
		return sideRoleFightValue;
	}

	public void setSideRoleFightValue(int sideRoleFightValue) {
		this.sideRoleFightValue = sideRoleFightValue;
	}

	public byte getRemainTime() {
		return remainTime;
	}

	public void setRemainTime(byte remainTime) {
		this.remainTime = remainTime;
	}

}
