package com.snail.webgame.game.info.log;

import java.sql.Timestamp;

import com.snail.webgame.engine.common.to.BaseTO;

/**
 * 记录注册用户日,开卡用户日志,在线用户日志
 * @author zenggang
 */
public class StatLog extends BaseTO {

	private int nAmount;
	private Timestamp timestamp;

	public int getnAmount() {
		return nAmount;
	}

	public void setnAmount(int nAmount) {
		this.nAmount = nAmount;
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public byte getSaveMode() {
		return ONLINE;
	}

}
