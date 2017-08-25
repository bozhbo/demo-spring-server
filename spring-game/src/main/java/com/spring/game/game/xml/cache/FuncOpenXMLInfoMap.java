package com.snail.webgame.game.xml.cache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.snail.webgame.game.common.fightdata.DropInfo;
import com.snail.webgame.game.condtion.AbstractConditionCheck;
import com.snail.webgame.game.condtion.conds.RoleLvCond;
import com.snail.webgame.game.xml.info.FuncOpenXMLInfo;

public class FuncOpenXMLInfoMap {

	private static Map<Integer, FuncOpenXMLInfo> map = new HashMap<Integer, FuncOpenXMLInfo>();

	public static void addFuncOpenXMLInfo(FuncOpenXMLInfo xmlInfo) {
		map.put(xmlInfo.getNo(), xmlInfo);
	}

	public static Map<Integer, FuncOpenXMLInfo> getMap() {
		return map;
	}

	public static FuncOpenXMLInfo getFuncOpenXMLInfo(int no) {
		return map.get(no);
	}

	public static List<DropInfo> fetchPrizeByNo(int no) {
		FuncOpenXMLInfo xmlInfo = map.get(no);
		if (xmlInfo != null && xmlInfo.getPrizes() != null && xmlInfo.getPrizes().size() > 0) {
			return xmlInfo.getPrizes();
		}

		return null;
	}

	public static int getFuncOpenHeroLevel(int no) {
		FuncOpenXMLInfo xmlInfo = map.get(no);
		if (xmlInfo != null && xmlInfo.getCheckConds() != null && xmlInfo.getCheckConds().size() > 0) {
			for (AbstractConditionCheck cond : xmlInfo.getCheckConds()) {
				if (cond instanceof RoleLvCond) {
					return ((RoleLvCond) cond).getLevel();
				}
			}
		}
		return 0;
	}
}
