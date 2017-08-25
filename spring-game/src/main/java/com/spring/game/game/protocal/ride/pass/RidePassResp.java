package com.snail.webgame.game.protocal.ride.pass;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class RidePassResp extends MessageBody {
	private int result;
	
	private int leftRideId;// 唯一id
	private int leftRideLv;// 等级
	private int leftRideQua;// 品阶
	private int leftFightVal;// 坐骑战斗力
	
	private int rightRideId;// 唯一id
	private int rightRideLv;// 等级
	private int rightRideQua;// 品阶
	private int rightFightVal;// 坐骑战斗力
	
	private int heroId;
	private int fightVal;// 当前战斗力
	
	private byte sourceType;//扣除的资源类型
	private int soruceNum;//正为加，负为减

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("result", 0);
		ps.add("leftRideId", 0);
		ps.add("leftRideLv", 0);
		ps.add("leftRideQua", 0);
		ps.add("leftFightVal", 0);
		ps.add("rightRideId", 0);
		ps.add("rightRideLv", 0);
		ps.add("rightRideQua", 0);
		ps.add("rightFightVal", 0);
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

	public int getLeftRideId() {
		return leftRideId;
	}

	public void setLeftRideId(int leftRideId) {
		this.leftRideId = leftRideId;
	}

	public int getLeftRideLv() {
		return leftRideLv;
	}

	public void setLeftRideLv(int leftRideLv) {
		this.leftRideLv = leftRideLv;
	}

	public int getLeftFightVal() {
		return leftFightVal;
	}

	public void setLeftFightVal(int leftFightVal) {
		this.leftFightVal = leftFightVal;
	}

	public int getRightRideId() {
		return rightRideId;
	}

	public void setRightRideId(int rightRideId) {
		this.rightRideId = rightRideId;
	}

	public int getRightRideLv() {
		return rightRideLv;
	}

	public void setRightRideLv(int rightRideLv) {
		this.rightRideLv = rightRideLv;
	}

	public int getRightFightVal() {
		return rightFightVal;
	}

	public void setRightFightVal(int rightFightVal) {
		this.rightFightVal = rightFightVal;
	}

	public int getHeroId() {
		return heroId;
	}

	public void setHeroId(int heroId) {
		this.heroId = heroId;
	}

	public int getFightVal() {
		return fightVal;
	}

	public void setFightVal(int fightVal) {
		this.fightVal = fightVal;
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

	public int getLeftRideQua() {
		return leftRideQua;
	}

	public void setLeftRideQua(int leftRideQua) {
		this.leftRideQua = leftRideQua;
	}

	public int getRightRideQua() {
		return rightRideQua;
	}

	public void setRightRideQua(int rightRideQua) {
		this.rightRideQua = rightRideQua;
	}

}
