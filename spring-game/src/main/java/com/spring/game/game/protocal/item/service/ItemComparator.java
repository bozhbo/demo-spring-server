package com.snail.webgame.game.protocal.item.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.snail.webgame.game.protocal.item.query.BagItemRe;

//import com.snail.webgame.game.info.BagItemInfo;

/**
 * 对物品道具进行排序（）
 * 
 * @author zhangyq
 * 
 */
public class ItemComparator {

	// 类型
	private Comparator<BagItemRe> cmp = new Comparator<BagItemRe>() {
		@Override
		public int compare(BagItemRe arg0, BagItemRe arg1) {
			if (arg0.getItemType() > arg1.getItemType()) {
				return 1;
			} else if (arg0.getItemType() < arg1.getItemType()) {
				return -1;
			}
			return 0;
		}
	};

	// 颜色
	private Comparator<BagItemRe> cmp1 = new Comparator<BagItemRe>() {
		@Override
		public int compare(BagItemRe arg0, BagItemRe arg1) {
			if (arg0.getColour() < arg1.getColour()) {
				return 1;
			} else if (arg0.getColour() > arg1.getColour()) {
				return -1;
			}
			return 0;
		}
	};

	// 等级
	private Comparator<BagItemRe> cmp2 = new Comparator<BagItemRe>() {
		@Override
		public int compare(BagItemRe arg0, BagItemRe arg1) {
			if (arg0.getLevel() < arg1.getLevel()) {
				return 1;
			} else if (arg0.getLevel() > arg1.getLevel()) {
				return -1;
			}
			return 0;
		}
	};

	// ID
	private Comparator<BagItemRe> cmp3 = new Comparator<BagItemRe>() {
		@Override
		public int compare(BagItemRe arg0, BagItemRe arg1) {
			if (arg0.getItemNo() > arg1.getItemNo()) {
				return 1;
			} else if (arg0.getItemNo() < arg1.getItemNo()) {
				return -1;
			}
			return 0;
		}
	};

	public ItemComparator(List<BagItemRe> list) {
		List<Comparator<BagItemRe>> mCmpList = new ArrayList<Comparator<BagItemRe>>();
		mCmpList.add(cmp);
		mCmpList.add(cmp1);
		mCmpList.add(cmp2);
		mCmpList.add(cmp3);

		sort(list, mCmpList);
	}

	public void sort(List<BagItemRe> list,
			final List<Comparator<BagItemRe>> cmpList) {
		if (cmpList == null) {
			return;
		}

		Comparator<BagItemRe> cmp = new Comparator<BagItemRe>() {
			@Override
			public int compare(BagItemRe arg0, BagItemRe arg1) {
				for (Comparator<BagItemRe> com : cmpList) {
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
}
