package com.snail.webgame.game.protocal.equip.resolve;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class EquipResolveResp extends MessageBody {
	private int result;
	private int resolveType; // 0 - 普通 1 - 重铸
	private int returnItemNum; // 重铸后产生的碎片
	private byte sourceType;// 1:银子 2:金子
	private int sourceChange;// 资源变动数,正值为增加,负值为减少
	private int equipMoney; // 装备商城专属货币
	private String resolveItems; // itemNo:num;itemNo:num
	private String resetEquipInfo; //装备重铸的信息id:id
	private int starMoney; //角色所有的星石币

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("result", 0);
		ps.add("resolveType", 0);
		ps.add("returnItemNum", 0);

		ps.add("sourceType", 0);
		ps.add("sourceChange", 0);
		ps.add("equipMoney", 0);
		ps.addString("resolveItems","flashCode", 0);
		ps.addString("resetEquipInfo","flashCode", 0);
		ps.add("starMoney", 0);
	}

	public void setResult(int result) {
		this.result = result;
	}

	public int getResult() {
		return result;
	}

	public int getResolveType() {
		return resolveType;
	}

	public void setResolveType(int resolveType) {
		this.resolveType = resolveType;
	}

	public int getReturnItemNum() {
		return returnItemNum;
	}

	public void setReturnItemNum(int returnItemNum) {
		this.returnItemNum = returnItemNum;
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

	public int getEquipMoney() {
		return equipMoney;
	}

	public void setEquipMoney(int equipMoney) {
		this.equipMoney = equipMoney;
	}

	public String getResolveItems() {
		return resolveItems;
	}

	public void setResolveItems(String resolveItems) {
		this.resolveItems = resolveItems;
	}

	public String getResetEquipInfo() {
		return resetEquipInfo;
	}

	public void setResetEquipInfo(String resetEquipInfo) {
		this.resetEquipInfo = resetEquipInfo;
	}

	public int getStarMoney() {
		return starMoney;
	}

	public void setStarMoney(int starMoney) {
		this.starMoney = starMoney;
	}

}
