package com.snail.webgame.game.protocal.soldier.upgrade;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class UpgradeSoldierReq extends MessageBody {
	private byte soldierType; // 士兵类型
	private int coin; // 金子 提升升级概率

	protected void setSequnce(ProtocolSequence ps) {
		ps.add("soldierType", 0);
		ps.add("coin", 0);
	}

	public byte getSoldierType() {
		return soldierType;
	}

	public void setSoldierType(byte soldierType) {
		this.soldierType = soldierType;
	}

	public int getCoin() {
		return coin;
	}

	public void setCoin(int coin) {
		this.coin = coin;
	}

}
