package com.snail.webgame.game.protocal.ride.upqua;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class RideUpQuaResp extends MessageBody {
	private int result;
	private int rideId;// 唯一id
	private int rideQua;// 品阶
	private int rideFightVal;// 当前坐骑战斗力
	
	private int heroId;
	private int fightVal;// 当前战斗力
	
	private byte sourceType;//扣除的资源类型
	private int soruceNum;//正为加，负为减

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("result", 0);
		ps.add("rideId", 0);
		ps.add("rideQua", 0);
		ps.add("rideFightVal", 0);
		ps.add("heroId", 0);
		ps.add("fightVal", 0);
		ps.add("sourceType", 0);
		ps.add("soruceNum", 0);
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public int getRideId() {
		return rideId;
	}

	public void setRideId(int rideId) {
		this.rideId = rideId;
	}

	public int getRideQua() {
		return rideQua;
	}

	public void setRideQua(int rideQua) {
		this.rideQua = rideQua;
	}

	public int getFightVal() {
		return fightVal;
	}

	public void setFightVal(int fightVal) {
		this.fightVal = fightVal;
	}

	public int getRideFightVal() {
		return rideFightVal;
	}

	public void setRideFightVal(int rideFightVal) {
		this.rideFightVal = rideFightVal;
	}

	public int getHeroId() {
		return heroId;
	}

	public void setHeroId(int heroId) {
		this.heroId = heroId;
	}

	public byte getSourceType() {
		return sourceType;
	}

	public void setSourceType(byte sourceType) {
		this.sourceType = sourceType;
	}

	public int getSoruceNum() {
		return soruceNum;
	}

	public void setSoruceNum(int soruceNum) {
		this.soruceNum = soruceNum;
	}

}
