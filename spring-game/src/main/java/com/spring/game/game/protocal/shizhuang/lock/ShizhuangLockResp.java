package com.snail.webgame.game.protocal.shizhuang.lock;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class ShizhuangLockResp extends MessageBody {
	private int result;
	private String lockShizhuang;// 玩家锁定的时装 部位_ID,部位_id
	private int fightValue; // 主武将战斗力

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("result", 0);
		ps.addString("lockShizhuang", "flashCode", 0);
		ps.add("fightValue", 0);
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public String getLockShizhuang() {
		return lockShizhuang;
	}

	public void setLockShizhuang(String lockShizhuang) {
		this.lockShizhuang = lockShizhuang;
	}

	public int getFightValue() {
		return fightValue;
	}

	public void setFightValue(int fightValue) {
		this.fightValue = fightValue;
	}

}
