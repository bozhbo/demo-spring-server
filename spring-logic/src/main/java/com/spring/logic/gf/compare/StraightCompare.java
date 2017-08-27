package com.spring.logic.gf.compare;

import java.util.Comparator;

import com.spring.logic.gf.info.GoldFlowerInfo;

public class StraightCompare implements Comparator<GoldFlowerInfo> {

	@Override
	public int compare(GoldFlowerInfo o1, GoldFlowerInfo o2) {
		int v1 = o1.getPokerInfo()[0].getValue() == 1 ? 14 : o1.getPokerInfo()[0].getValue();
		int v2 = o2.getPokerInfo()[0].getValue() == 1 ? 14 : o2.getPokerInfo()[0].getValue();
		
		if (v2 - v1 == 0) {
			return o2.getPokerInfo()[0].getType() - o1.getPokerInfo()[0].getType();
		} else {
			return v2 - v1;
		}
	}
}
