package com.snail.webgame.game.protocal.club.tech.upgrade;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class ClubTechUpgradeResp extends MessageBody {
	private int result;
	private String sourceChange;// sourceType:changeNumber;sourceType:changeNumber
	private int fightValue; //公会科技升级后 个人属性增加 提升战斗力
	private int buildType;
	private String extendInfo; //公会扩容XMLNo:gold

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("result", 0);
		ps.addString("sourceChange", "flashCode", 0);
		ps.add("fightValue", 0);
		ps.add("buildType", 0);
		ps.addString("extendInfo", "flashCode", 0);
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public String getSourceChange() {
		return sourceChange;
	}

	public void setSourceChange(String sourceChange) {
		this.sourceChange = sourceChange;
	}

	public int getFightValue() {
		return fightValue;
	}

	public void setFightValue(int fightValue) {
		this.fightValue = fightValue;
	}

	public int getBuildType() {
		return buildType;
	}

	public void setBuildType(int buildType) {
		this.buildType = buildType;
	}

	public String getExtendInfo() {
		return extendInfo;
	}

	public void setExtendInfo(String extendInfo) {
		this.extendInfo = extendInfo;
	}

}
