package com.spring.logic.gf.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.spring.logic.gf.GoldFlowerConfig;
import com.spring.logic.gf.enums.PokerTypeEnum;
import com.spring.logic.gf.info.GoldFlowerInfo;
import com.spring.logic.gf.info.PokerInfo;

public class GoldFlowerService {

	public static GoldFlowerInfo getGoldFlowerInfo(int a, int b, int c) {
		if (GoldFlowerConfig.isBoom(a, b, c)) {
			return new GoldFlowerInfo(PokerTypeEnum.POKER_TYPE_BOOM, new PokerInfo(a), new PokerInfo(b), new PokerInfo(c));
		} else if (GoldFlowerConfig.isGreatGF(a, b, c)) {
			return new GoldFlowerInfo(PokerTypeEnum.POKER_TYPE_GREAT_GF, new PokerInfo(a), new PokerInfo(b), new PokerInfo(c));
		} else if (GoldFlowerConfig.isGF(a, b, c)) {
			return new GoldFlowerInfo(PokerTypeEnum.POKER_TYPE_GF, new PokerInfo(a), new PokerInfo(b), new PokerInfo(c));
		} else if (GoldFlowerConfig.isStraight(a, b, c)) {
			return new GoldFlowerInfo(PokerTypeEnum.POKER_TYPE_STRAIGHT, new PokerInfo(a), new PokerInfo(b), new PokerInfo(c));
		} else if (GoldFlowerConfig.isPairs(a, b, c)) {
			return new GoldFlowerInfo(PokerTypeEnum.POKER_TYPE_PAIRS, new PokerInfo(a), new PokerInfo(b), new PokerInfo(c));
		} else if (GoldFlowerConfig.isMin(a, b, c)) {
			return new GoldFlowerInfo(PokerTypeEnum.POKER_TYPE_MIN, new PokerInfo(a), new PokerInfo(b), new PokerInfo(c));
		} else {
			return new GoldFlowerInfo(PokerTypeEnum.POKER_TYPE_ALONE, new PokerInfo(a), new PokerInfo(b), new PokerInfo(c));
		}
	}

	public List<Integer> randomPoker(int count) {
		List<Integer> list = new ArrayList<>(18);

		int allCount = count * 3;

		Set<Integer> chooseSet = new HashSet<>(18);

		while (chooseSet.size() < allCount) {
			int value = random(52);

			if (chooseSet.add(value)) {
				list.add(value);
			}
		}

		return list;
	}

	public int random(int v) {
		return (int) (Math.random() * v) + 1;
	}
}
