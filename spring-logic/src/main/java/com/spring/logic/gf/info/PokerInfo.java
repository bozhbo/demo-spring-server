package com.spring.logic.gf.info;

public class PokerInfo {

	/**
	 * 1-A 11-J 12-Q 13-K
	 */
	private byte value;
	
	/**
	 * 4-黑桃 3-红桃 2-梅花 1-方块
	 */
	private byte type;
	
	/**
	 * 原始值
	 */
	private byte src;
	
	public PokerInfo(int src) {
		int index = (src - 1) / 13;
		
		if (index == 0) {
			type = 1;
			value = (byte)src;
		} else if (index == 1) {
			type = 2;
			value = (byte)(src - 13);
		} else if (index == 2) {
			type = 3;
			value = (byte)(src - 26);
		} else {
			type = 4;
			value = (byte)(src - 39);
		}
		
		this.src = (byte)src;
	}

	public byte getValue() {
		return value;
	}

	public void setValue(byte value) {
		this.value = value;
	}

	public byte getType() {
		return type;
	}

	public void setType(byte type) {
		this.type = type;
	}

	public byte getSrc() {
		return src;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		
		if (obj instanceof PokerInfo) {
			return ((PokerInfo)obj).getValue() == this.value;
		} else {
			return false;
		}
	}
	
	@Override
	public String toString() {
		if (value == 1) {
			return "A";
		} else if (value < 11) {
			return value + "";
		} else if (value == 11) {
			return "J";
		} else if (value == 12) {
			return "Q";
		} else if (value == 13) {
			return "K";
		}
		
		return "X";
		
	}
}
