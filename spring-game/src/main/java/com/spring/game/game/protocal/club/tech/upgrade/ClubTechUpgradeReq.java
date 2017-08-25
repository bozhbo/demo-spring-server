package com.snail.webgame.game.protocal.club.tech.upgrade;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class ClubTechUpgradeReq extends MessageBody {
	private int buildType; // 升级类型
	private int donate; // 捐赠的金子（仅公会扩容的时候使用）
	
	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("buildType", 0);
		ps.add("donate", 0);
	}

	public int getBuildType() {
		return buildType;
	}

	public void setBuildType(int buildType) {
		this.buildType = buildType;
	}

	public int getDonate() {
		return donate;
	}

	public void setDonate(int donate) {
		this.donate = donate;
	}

}
