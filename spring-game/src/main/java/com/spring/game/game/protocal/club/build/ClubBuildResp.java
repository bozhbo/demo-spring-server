package com.snail.webgame.game.protocal.club.build;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class ClubBuildResp extends MessageBody {
	private int result;
	private int build; // 建设度
	private byte sourceType;// 1:银子 2:金子
	private int sourceChange;// 资源变动数,正值为增加,负值为减少
	private int clubContribution; //本次增加的公会贡献值

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("result", 0);
		ps.add("build", 0);
		ps.add("sourceType", 0);
		ps.add("sourceChange", 0);
		ps.add("clubContribution", 0);
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public int getBuild() {
		return build;
	}

	public void setBuild(int build) {
		this.build = build;
	}

	public byte getSourceType() {
		return sourceType;
	}

	public void setSourceType(byte sourceType) {
		this.sourceType = sourceType;
	}

	public int getSourceChange() {
		return sourceChange;
	}

	public void setSourceChange(int sourceChange) {
		this.sourceChange = sourceChange;
	}

	public int getClubContribution() {
		return clubContribution;
	}

	public void setClubContribution(int clubContribution) {
		this.clubContribution = clubContribution;
	}

}
