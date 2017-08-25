package com.snail.webgame.game.protocal.rank.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class RankComparator {

	// 参数
	private static Comparator<RankInfo> cmp = new Comparator<RankInfo>() {
		@Override
		public int compare(RankInfo arg0, RankInfo arg1) {
			if (arg0.getParam() < arg1.getParam()) {
				return 1;
			} else if (arg0.getParam() > arg1.getParam()) {
				return -1;
			}
			return 0;
		}
	};

	// 等级
	private static Comparator<RankInfo> cmp1 = new Comparator<RankInfo>() {
		@Override
		public int compare(RankInfo arg0, RankInfo arg1) {
			if (arg0.getLevel() < arg1.getLevel()) {
				return 1;
			} else if (arg0.getLevel() > arg1.getLevel()) {
				return -1;
			}
			return 0;
		}
	};

	// 战斗力
	private static Comparator<RankInfo> cmp2 = new Comparator<RankInfo>() {
		@Override
		public int compare(RankInfo arg0, RankInfo arg1) {
			if (arg0.getFightValue() < arg1.getFightValue()) {
				return 1;
			} else if (arg0.getFightValue() > arg1.getFightValue()) {
				return -1;
			}
			return 0;
		}
	};

	/**
	 * sort
	 * @param list
	 * @param cmpList
	 */
	private static void sort(List<RankInfo> list, final List<Comparator<RankInfo>> cmpList) {
		if (cmpList == null) {
			return;
		}

		Comparator<RankInfo> cmp = new Comparator<RankInfo>() {
			@Override
			public int compare(RankInfo arg0, RankInfo arg1) {
				for (Comparator<RankInfo> com : cmpList) {
					int compare = com.compare(arg0, arg1);
					if (compare > 0) {
						return 1;
					} else if (compare < 0) {
						return -1;
					}
				}
				return 0;
			}
		};
		Collections.sort(list, cmp);
	}

	public static void rankHeroNum(List<RankInfo> list) {
		List<Comparator<RankInfo>> mCmpList = new ArrayList<Comparator<RankInfo>>();
		mCmpList.add(cmp);
		mCmpList.add(cmp1);
		mCmpList.add(cmp2);
		sort(list, mCmpList);
	}

	public static void rankLevel(List<RankInfo> list) {
		List<Comparator<RankInfo>> mCmpList = new ArrayList<Comparator<RankInfo>>();
		mCmpList.add(cmp1);
		mCmpList.add(cmp2);
		sort(list, mCmpList);
	}
	
	public static void rankFightValue(List<RankInfo> list) {
		Collections.sort(list, cmp);
	}

}
