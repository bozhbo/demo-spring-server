package com.spring.logic.gf.compare;

import java.util.Comparator;

import com.spring.logic.gf.info.GoldFlowerInfo;

public class PairsCompare implements Comparator<GoldFlowerInfo> {

	@Override
	public int compare(GoldFlowerInfo o1, GoldFlowerInfo o2) {
		int v1 = o1.getPokerInfo()[0].getValue() == 1 ? 14 : o1.getPokerInfo()[0].getValue();
		int v2 = o2.getPokerInfo()[0].getValue() == 1 ? 14 : o2.getPokerInfo()[0].getValue();
		
		int v3 = o1.getPokerInfo()[1].getValue() == 1 ? 14 : o1.getPokerInfo()[1].getValue();
		int v4 = o2.getPokerInfo()[1].getValue() == 1 ? 14 : o2.getPokerInfo()[1].getValue();
		
		int v5 = o1.getPokerInfo()[2].getValue() == 1 ? 14 : o1.getPokerInfo()[2].getValue();
		int v6 = o2.getPokerInfo()[2].getValue() == 1 ? 14 : o2.getPokerInfo()[2].getValue();
		
		if (v2 - v1 == 0) {
			if (v6 - v5 == 0) {
				return o2.getPokerInfo()[2].getType() - o1.getPokerInfo()[2].getType();
			} else {
				return v6 - v5;
			}
		} else {
			return v2 - v1;
		}
	}
}