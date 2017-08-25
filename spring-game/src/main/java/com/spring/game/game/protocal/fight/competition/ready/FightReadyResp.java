package com.snail.webgame.game.protocal.fight.competition.ready;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

/**
 * 
 * 类介绍:战斗准备开始通知Response
 *
 * @author zhoubo
 * @2014-11-25
 */
public class FightReadyResp extends MessageBody {

	private int result;
	private byte fightType; // 1-竞技场PVP 2-地图PVP 3-对攻战 5-劫镖 6-组队副本  7-3V3
	private String server; // ip:port
	private String uuid;

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("result", 0);
		ps.add("fightType", 0);
		ps.addString("server", "flashCode", 0);
		ps.addString("uuid","flashCode", 0);
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
	public byte getFightType() {
		return fightType;
	}

	public void setFightType(byte fightType) {
		this.fightType = fightType;
	}

	@Override
	public String toString() {
		return "FightReadyResp [result=" + result + ", server=" + server
				+ ", uuid=" + uuid + "]";
	}
}
