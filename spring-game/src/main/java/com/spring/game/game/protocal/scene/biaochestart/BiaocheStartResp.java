package com.snail.webgame.game.protocal.scene.biaochestart;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

/**
 * 开始压镖
 * 
 * @author hongfm
 * 
 */
public class BiaocheStartResp extends MessageBody {

	private int result;
	private byte biaocheType;// 镖车类型
	private byte isHubiaoRole;// 0-押镖人 1-护镖人
	private int otherRoleId;// 押镖人ID或护镖人ID

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("result", 0);
		ps.add("biaocheType", 0);
		ps.add("isHubiaoRole", 0);
		ps.add("otherRoleId", 0);
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public byte getBiaocheType() {
		return biaocheType;
	}

	public void setBiaocheType(byte biaocheType) {
		this.biaocheType = biaocheType;
	}

	public byte getIsHubiaoRole() {
		return isHubiaoRole;
	}

	public void setIsHubiaoRole(byte isHubiaoRole) {
		this.isHubiaoRole = isHubiaoRole;
	}

	public int getOtherRoleId() {
		return otherRoleId;
	}

	public void setOtherRoleId(int otherRoleId) {
		this.otherRoleId = otherRoleId;
	}

}
