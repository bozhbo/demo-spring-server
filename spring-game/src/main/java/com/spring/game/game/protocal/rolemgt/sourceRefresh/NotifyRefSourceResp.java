package com.snail.webgame.game.protocal.rolemgt.sourceRefresh;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class NotifyRefSourceResp extends MessageBody {
	
	private int gold; //金子
	private long money; //银子
	private int courage; //勇气
	private int justice; //征服
	
	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("gold", 0);
		ps.add("money", 0);
		ps.add("courage", 0);
		ps.add("justice", 0);
	}

	public int getGold() {
		return gold;
	}

	public void setGold(int gold) {
		this.gold = gold;
	}

	public long getMoney() {
		return money;
	}

	public void setMoney(long money) {
		this.money = money;
	}

	public int getCourage() {
		return courage;
	}

	public void setCourage(int courage) {
		this.courage = courage;
	}

	public int getJustice() {
		return justice;
	}

	public void setJustice(int justice) {
		this.justice = justice;
	}
	
}
