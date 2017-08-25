package com.snail.webgame.game.protocal.redPoint.service;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class PopRedPointHeroInfoRe extends MessageBody {
	private long heroId;	//武将ID
	private byte canUpgradeStar; //可升星
	private byte canUpgradeSkill;	//可升技能
	private byte canUpgradeColor;	//可升品质
	private byte canUpgradeEquip;	//可强化装备
	private byte canMakeStoneOnEquip;	//可镶嵌宝石
	private byte canComposeEquip;	//可合成装备
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("heroId", 0);
		ps.add("canUpgradeStar", 0);
		ps.add("canUpgradeSkill", 0);
		ps.add("canUpgradeColor", 0);
		ps.add("canUpgradeEquip", 0);
		ps.add("canMakeStoneOnEquip", 0);
		ps.add("canComposeEquip", 0);
	}
	public long getHeroId() {
		return heroId;
	}
	public void setHeroId(long heroId) {
		this.heroId = heroId;
	}
	public byte getCanUpgradeStar() {
		return canUpgradeStar;
	}
	public void setCanUpgradeStar(byte canUpgradeStar) {
		this.canUpgradeStar = canUpgradeStar;
	}
	public byte getCanUpgradeSkill() {
		return canUpgradeSkill;
	}
	public void setCanUpgradeSkill(byte canUpgradeSkill) {
		this.canUpgradeSkill = canUpgradeSkill;
	}
	public byte getCanUpgradeColor() {
		return canUpgradeColor;
	}
	public void setCanUpgradeColor(byte canUpgradeColor) {
		this.canUpgradeColor = canUpgradeColor;
	}
	public byte getCanUpgradeEquip() {
		return canUpgradeEquip;
	}
	public void setCanUpgradeEquip(byte canUpgradeEquip) {
		this.canUpgradeEquip = canUpgradeEquip;
	}
	public byte getCanMakeStoneOnEquip() {
		return canMakeStoneOnEquip;
	}
	public void setCanMakeStoneOnEquip(byte canMakeStoneOnEquip) {
		this.canMakeStoneOnEquip = canMakeStoneOnEquip;
	}
	public byte getCanComposeEquip() {
		return canComposeEquip;
	}
	public void setCanComposeEquip(byte canComposeEquip) {
		this.canComposeEquip = canComposeEquip;
	}
}
