package com.snail.webgame.game.protocal.soldier.upgrade;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class UpgradeSoldierResp extends MessageBody {
	private int result;
	private byte soldierType;	// 兵种类型
    private int level; 		// 等级
    
    private byte sourceType;//扣除的资源类型
    private int soruceNum;//正为加，负为减
    private int costCoin; //提升概率消耗的金子
    
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("result", 0);
		ps.add("soldierType", 0);
		ps.add("level", 0);
		ps.add("sourceType", 0);
		ps.add("soruceNum", 0);
		ps.add("costCoin", 0);
		
	}
	public int getResult() {
		return result;
	}
	public void setResult(int result) {
		this.result = result;
	}
	public byte getSoldierType() {
		return soldierType;
	}
	public void setSoldierType(byte soldierType) {
		this.soldierType = soldierType;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
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
	public int getCostCoin() {
		return costCoin;
	}
	public void setCostCoin(int costCoin) {
		this.costCoin = costCoin;
	}
	
}
