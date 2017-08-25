package com.snail.webgame.game.protocal.campaign.query;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class CampaignHeroRe extends MessageBody {

	private int heroId;// 武将编号id
	private byte deployPos;// 布阵位置 0-普通 1-5上阵
	private byte heroStatus;// 0-战死 1-残血
	private int cutHp;// 减少的血量
	private int totalHp;//总血量

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("heroId", 0);
		ps.add("deployPos", 0);
		ps.add("heroStatus", 0);
		ps.add("cutHp", 0);
		ps.add("totalHp", 0);
	}

	public int getHeroId() {
		return heroId;
	}

	public void setHeroId(int heroId) {
		this.heroId = heroId;
	}

	public byte getDeployPos() {
		return deployPos;
	}

	public void setDeployPos(byte deployPos) {
		this.deployPos = deployPos;
	}

	public byte getHeroStatus() {
		return heroStatus;
	}

	public void setHeroStatus(byte heroStatus) {
		this.heroStatus = heroStatus;
	}

	public int getCutHp() {
		return cutHp;
	}

	public void setCutHp(int cutHp) {
		this.cutHp = cutHp;
	}

	public int getTotalHp() {
		return totalHp;
	}

	public void setTotalHp(int totalHp) {
		this.totalHp = totalHp;
	}
}
