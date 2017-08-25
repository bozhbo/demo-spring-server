package com.snail.webgame.game.protocal.rolemgt.sp;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class NotifyRefEnergyResp extends MessageBody {

	private int energy;// 最新的精力
	private long lastRefEnergyTime;// 最新的精力回复时间

	private byte refFlag;// 更新标记 0-表示都更新 1-表示只更新精力

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("energy", 0);
		ps.add("lastRefEnergyTime", 0);
		ps.add("refFlag", 0);
	}

	public int getEnergy() {
		return energy;
	}

	public void setEnergy(int energy) {
		this.energy = energy;
	}

	public long getLastRefEnergyTime() {
		return lastRefEnergyTime;
	}

	public void setLastRefEnergyTime(long lastRefEnergyTime) {
		this.lastRefEnergyTime = lastRefEnergyTime;
	}

	public byte getRefFlag() {
		return refFlag;
	}

	public void setRefFlag(byte refFlag) {
		this.refFlag = refFlag;
	}
}
