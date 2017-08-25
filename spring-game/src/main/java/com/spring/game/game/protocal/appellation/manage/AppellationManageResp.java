package com.snail.webgame.game.protocal.appellation.manage;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

/**
 * 称谓管理消息体
 * 
 * @author SnailGame
 * 
 */
public class AppellationManageResp extends MessageBody {
	private int result;
	private byte chenghaoType; // 类型 0-卸 1-穿
	private int id; // 称谓ID xmlNo
	private int fightValue; // 刷新的战斗力

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("result", 0);
		ps.add("chenghaoType", 0);
		ps.add("id", 0);
		ps.add("fightValue", 0);
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public byte getChenghaoType() {
		return chenghaoType;
	}

	public void setChenghaoType(byte chenghaoType) {
		this.chenghaoType = chenghaoType;
	}

	public int getFightValue() {
		return fightValue;
	}

	public void setFightValue(int fightValue) {
		this.fightValue = fightValue;
	}

}
