package com.spring.logic.gf.compare;

import java.util.Comparator;

import com.spring.logic.gf.info.PokerInfo;

public class PokerCompare implements Comparator<PokerInfo> {

	@Override
	public int compare(PokerInfo o1, PokerInfo o2) {
		int v1 = o1.getValue() == 1 ? 14 : o1.getValue();
		int v2 = o2.getValue() == 1 ? 14 : o2.getValue();
		
		if (v2 - v1 == 0) {
			return o2.getType() - o1.getType();
		} else {
			return v2 - v1;
		}
	}
}
