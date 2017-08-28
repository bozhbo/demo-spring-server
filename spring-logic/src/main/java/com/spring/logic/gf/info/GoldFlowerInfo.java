package com.spring.logic.gf.info;

import java.util.Arrays;

import com.spring.logic.gf.compare.PokerCompare;
import com.spring.logic.gf.enums.PokerTypeEnum;

public class GoldFlowerInfo {

	private PokerInfo[] pokerInfo = new PokerInfo[3];
	private int level;
	private PokerTypeEnum type;
	
	private String key;
	
	public GoldFlowerInfo(PokerTypeEnum type, PokerInfo p1, PokerInfo p2, PokerInfo p3) {
		this.type = type;
		
		if (type == PokerTypeEnum.POKER_TYPE_PAIRS) {
			if (p1.equals(p2)) {
				pokerInfo[0] = p1;
				pokerInfo[1] = p2;
				pokerInfo[2] = p3;
			} else if (p1.equals(p3)) {
				pokerInfo[0] = p1;
				pokerInfo[1] = p3;
				pokerInfo[2] = p2;
			} else if (p2.equals(p3)) {
				pokerInfo[0] = p2;
				pokerInfo[1] = p3;
				pokerInfo[2] = p1;
			}
		} else if (type == PokerTypeEnum.POKER_TYPE_GREAT_GF) {
			int max1 = Math.max(p1.getValue(), p2.getValue());
			int max = Math.max(max1, p3.getValue());
			
			int min1 = Math.min(p1.getValue(), p2.getValue());
			int min = Math.min(min1, p3.getValue());
			
			if (max == 13 && min == 1) {
				pokerInfo[0] = p1.getValue() == 1 ? p1 : (p2.getValue() == 1 ? p2 : p3);
				pokerInfo[1] = p1.getValue() == 13 ? p1 : (p2.getValue() == 13 ? p2 : p3);
				pokerInfo[2] = p1.getValue() == 12 ? p1 : (p2.getValue() == 12 ? p2 : p3);
			} else if (max == 3 && min == 1) {
				pokerInfo[0] = p1.getValue() == 3 ? p1 : (p2.getValue() == 3 ? p2 : p3);
				pokerInfo[1] = p1.getValue() == 2 ? p1 : (p2.getValue() == 2 ? p2 : p3);
				pokerInfo[2] = p1.getValue() == 1 ? p1 : (p2.getValue() == 1 ? p2 : p3);
			} else {
				pokerInfo[0] = p1;
				pokerInfo[1] = p2;
				pokerInfo[2] = p3;
				
				Arrays.sort(pokerInfo, 0, 3, new PokerCompare());
			}
		} else {
			pokerInfo[0] = p1;
			pokerInfo[1] = p2;
			pokerInfo[2] = p3;
			
			Arrays.sort(pokerInfo, 0, 3, new PokerCompare());
		}
		
		key = pokerInfo[0].getSrc() + "," + pokerInfo[1].getSrc() + "," + pokerInfo[2].getSrc();
	}
	
	public PokerInfo[] getPokerInfo() {
		return pokerInfo;
	}
	public void setPokerInfo(PokerInfo[] pokerInfo) {
		this.pokerInfo = pokerInfo;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}

	public PokerTypeEnum getType() {
		return type;
	}
	
	public String getKey() {
		return key;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		
		buffer.append(getChar(pokerInfo[0].getType())).append(pokerInfo[0]).append(",");
		buffer.append(getChar(pokerInfo[1].getType())).append(pokerInfo[1]).append(",");
		buffer.append(getChar(pokerInfo[2].getType())).append(pokerInfo[2]);
		
		return buffer.toString();
	}
	
	public char getChar(byte type) {
		if (type == 4) {
			return '♠';
		} else if (type == 3) {
			return '♥';
		} else if (type == 2) {
			return '♣';
		} else if (type == 1) {
			return '♦';
		} else {
			return 0;
		}
	}
}
