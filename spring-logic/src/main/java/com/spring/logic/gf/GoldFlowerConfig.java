package com.spring.logic.gf;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.spring.logic.gf.compare.AloneCompare;
import com.spring.logic.gf.compare.BoomCompare;
import com.spring.logic.gf.compare.GFCompare;
import com.spring.logic.gf.compare.GreatGFCompare;
import com.spring.logic.gf.compare.PairsCompare;
import com.spring.logic.gf.compare.StraightCompare;
import com.spring.logic.gf.enums.PokerTypeEnum;
import com.spring.logic.gf.info.GoldFlowerInfo;
import com.spring.logic.gf.info.PokerInfo;

public class GoldFlowerConfig {

	public static int arrays[] = new int[52];

	/**
	 * 1-炸弹 2-顺金 3-金花 4-拖拉机 5-对子 6-单张 7-235
	 */
	public static Map<PokerTypeEnum, List<GoldFlowerInfo>> map = new HashMap<>();
	
	/**
	 * 
	 */
	public static List<GoldFlowerInfo> allList = new ArrayList<>();
	
	public static void init() {
		for (int i = 0; i < 52; i++) {
			arrays[i] = i + 1;
		}

		for (int i = 0; i < 7; i++) {
			map.put(PokerTypeEnum.values()[i], new ArrayList<>());
		}

		int level = 0;

		for (int i = 0; i < arrays.length; i++) {
			for (int j = i + 1; j < arrays.length; j++) {
				for (int n = j + 1; n < arrays.length; n++) {
					int a = arrays[i];
					int b = arrays[j];
					int c = arrays[n];

					if (isBoom(a, b, c)) {
						map.get(PokerTypeEnum.POKER_TYPE_BOOM).add(new GoldFlowerInfo(PokerTypeEnum.POKER_TYPE_BOOM,
								new PokerInfo(a), new PokerInfo(b), new PokerInfo(c)));
					} else if (isGreatGF(a, b, c)) {
						map.get(PokerTypeEnum.POKER_TYPE_GREAT_GF)
								.add(new GoldFlowerInfo(PokerTypeEnum.POKER_TYPE_GREAT_GF, new PokerInfo(a),
										new PokerInfo(b), new PokerInfo(c)));
					} else if (isGF(a, b, c)) {
						map.get(PokerTypeEnum.POKER_TYPE_GF).add(new GoldFlowerInfo(PokerTypeEnum.POKER_TYPE_GF,
								new PokerInfo(a), new PokerInfo(b), new PokerInfo(c)));
					} else if (isStraight(a, b, c)) {
						map.get(PokerTypeEnum.POKER_TYPE_STRAIGHT)
								.add(new GoldFlowerInfo(PokerTypeEnum.POKER_TYPE_STRAIGHT, new PokerInfo(a),
										new PokerInfo(b), new PokerInfo(c)));
					} else if (isPairs(a, b, c)) {
						map.get(PokerTypeEnum.POKER_TYPE_PAIRS).add(new GoldFlowerInfo(PokerTypeEnum.POKER_TYPE_PAIRS,
								new PokerInfo(a), new PokerInfo(b), new PokerInfo(c)));
					} else if (isMin(a, b, c)) {
						map.get(PokerTypeEnum.POKER_TYPE_MIN).add(new GoldFlowerInfo(PokerTypeEnum.POKER_TYPE_MIN,
								new PokerInfo(a), new PokerInfo(b), new PokerInfo(c)));
					} else {
						map.get(PokerTypeEnum.POKER_TYPE_ALONE).add(new GoldFlowerInfo(PokerTypeEnum.POKER_TYPE_ALONE,
								new PokerInfo(a), new PokerInfo(b), new PokerInfo(c)));
					}

					level++;
				}
			}
		}

		Collections.sort(map.get(PokerTypeEnum.POKER_TYPE_BOOM), new BoomCompare());
		Collections.sort(map.get(PokerTypeEnum.POKER_TYPE_GREAT_GF), new GreatGFCompare());
		Collections.sort(map.get(PokerTypeEnum.POKER_TYPE_GF), new GFCompare());
		Collections.sort(map.get(PokerTypeEnum.POKER_TYPE_STRAIGHT), new StraightCompare());
		Collections.sort(map.get(PokerTypeEnum.POKER_TYPE_PAIRS), new PairsCompare());
		Collections.sort(map.get(PokerTypeEnum.POKER_TYPE_ALONE), new AloneCompare());
		Collections.sort(map.get(PokerTypeEnum.POKER_TYPE_MIN), new AloneCompare());

		level = setLevel(map.get(PokerTypeEnum.POKER_TYPE_BOOM), level);
		level = setLevel(map.get(PokerTypeEnum.POKER_TYPE_GREAT_GF), level);
		level = setLevel(map.get(PokerTypeEnum.POKER_TYPE_GF), level);
		level = setLevel(map.get(PokerTypeEnum.POKER_TYPE_STRAIGHT), level);
		level = setLevel(map.get(PokerTypeEnum.POKER_TYPE_PAIRS), level);
		level = setLevel(map.get(PokerTypeEnum.POKER_TYPE_ALONE), level);
		level = setLevel(map.get(PokerTypeEnum.POKER_TYPE_MIN), level);
		
		allList.addAll(map.get(PokerTypeEnum.POKER_TYPE_BOOM));
		allList.addAll(map.get(PokerTypeEnum.POKER_TYPE_GREAT_GF));
		allList.addAll(map.get(PokerTypeEnum.POKER_TYPE_GF));
		allList.addAll(map.get(PokerTypeEnum.POKER_TYPE_STRAIGHT));
		allList.addAll(map.get(PokerTypeEnum.POKER_TYPE_PAIRS));
		allList.addAll(map.get(PokerTypeEnum.POKER_TYPE_ALONE));
		allList.addAll(map.get(PokerTypeEnum.POKER_TYPE_MIN));
	}

	private static boolean isBoom(int a, int b, int c) {
		PokerInfo p1 = new PokerInfo(a);
		PokerInfo p2 = new PokerInfo(b);
		PokerInfo p3 = new PokerInfo(c);

		if (p1.getValue() == p2.getValue() && p2.getValue() == p3.getValue()) {
			return true;
		} else {
			return false;
		}
	}

	private static boolean isGreatGF(int a, int b, int c) {
		PokerInfo p1 = new PokerInfo(a);
		PokerInfo p2 = new PokerInfo(b);
		PokerInfo p3 = new PokerInfo(c);

		if (p1.getType() == p2.getType() && p2.getType() == p3.getType()) {
			int d = Math.abs(p1.getValue() - p2.getValue());
			int e = Math.abs(p1.getValue() - p3.getValue());
			int f = Math.abs(p2.getValue() - p3.getValue());
			
			if (d == 2) {
				if (e == 1 && f == 1) {
					return true;
				}
			} else if (e == 2) {
				if (d == 1 && f == 1) {
					return true;
				}
			} else if (f == 2) {
				if (e == 1 && d == 1) {
					return true;
				}
			}
			
			if (d == 12) {
				if (p3.getValue() == 12) {
					return true;
				}
			} else if (e == 12) {
				if (p2.getValue() == 12) {
					return true;
				}
			} else if (f == 12) {
				if (p1.getValue() == 12) {
					return true;
				}
			}
		}

		return false;
	}

	private static boolean isGF(int a, int b, int c) {
		PokerInfo p1 = new PokerInfo(a);
		PokerInfo p2 = new PokerInfo(b);
		PokerInfo p3 = new PokerInfo(c);

		if (p1.getType() == p2.getType() && p2.getType() == p3.getType()) {
			return true;
		}

		return false;
	}

	private static boolean isStraight(int a, int b, int c) {
		PokerInfo p1 = new PokerInfo(a);
		PokerInfo p2 = new PokerInfo(b);
		PokerInfo p3 = new PokerInfo(c);

		if (Math.abs(p1.getValue() - p2.getValue()) == 1 && Math.abs(p2.getValue() - p3.getValue()) == 1
				&& Math.abs(p1.getValue() - p3.getValue()) == 1) {
			return true;
		}

		return false;
	}

	private static boolean isPairs(int a, int b, int c) {
		PokerInfo p1 = new PokerInfo(a);
		PokerInfo p2 = new PokerInfo(b);
		PokerInfo p3 = new PokerInfo(c);

		if (Math.abs(p1.getValue() - p2.getValue()) == 0 || Math.abs(p2.getValue() - p3.getValue()) == 0
				|| Math.abs(p1.getValue() - p3.getValue()) == 0) {
			return true;
		}

		return false;
	}

	private static boolean isMin(int a, int b, int c) {
		PokerInfo p1 = new PokerInfo(a);
		PokerInfo p2 = new PokerInfo(b);
		PokerInfo p3 = new PokerInfo(c);
		
		if (p1.getValue() == 1 || p2.getValue() == 1 || p3.getValue() == 1) {
			return false;
		}
		
		if (p1.getValue() + p2.getValue() + p3.getValue() == 10) {
			return true;
		}

		return false;
	}

	private static int setLevel(List<GoldFlowerInfo> list, int level) {
		for (GoldFlowerInfo goldFlowerInfo : list) {
			goldFlowerInfo.setLevel(level--);
		}

		return level;
	}
}
