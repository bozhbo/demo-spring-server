package com.snail.webgame.game.protocal.shizhuang.lock;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class ShizhuangLockReq extends MessageBody {
	
	private int lockType; // 类型 1-上锁 2-解锁
	private String equipIds;	// 时装ID(,)

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("lockType", 0);
		ps.addString("equipIds", "flashCode", 0);
	}

	public int getLockType() {
		return lockType;
	}

	public void setLockType(int lockType) {
		this.lockType = lockType;
	}

	public String getEquipIds() {
		return equipIds;
	}

	public void setEquipIds(String equipIds) {
		this.equipIds = equipIds;
	}
}
