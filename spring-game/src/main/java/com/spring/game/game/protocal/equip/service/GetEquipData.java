package com.snail.webgame.game.protocal.equip.service;

/**
 * 获取到的掉落物信息
 * 
 * @author zhangyq
 * 
 */
public class GetEquipData {

	/**
	 * 1增加道具 2删除道具
	 */
	public static final int EQUIP_ADD = 1;
	public static final int EQUIP_DEL = 2;

	private String equipNo;	// 装备No
	private byte equipUp;	//强化等级

	private byte type;	//1增加道具 2删除道具
	private long id;	//装备ID

	public GetEquipData(){
		
	}
	
	public GetEquipData(long id, byte type, String equipNo) {
		this.id = id;
		this.equipNo = equipNo;
		this.type = type;
	}

	public byte getType() {
		return type;
	}

	public void setType(byte type) {
		this.type = type;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getEquipNo() {
		return equipNo;
	}

	public void setEquipNo(String equipNo) {
		this.equipNo = equipNo;
	}

	public byte getEquipUp() {
		return equipUp;
	}

	public void setEquipUp(byte equipUp) {
		this.equipUp = equipUp;
	}

}
