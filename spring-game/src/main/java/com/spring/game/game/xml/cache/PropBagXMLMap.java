package com.snail.webgame.game.xml.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.snail.webgame.game.common.GameValue;
import com.snail.webgame.game.xml.info.DropXMLInfo;

public class PropBagXMLMap {
	private static HashMap<String, List<DropXMLInfo>> map = new HashMap<String, List<DropXMLInfo>>();

	// 记录已经使用的bagNo
	private static Map<String, Integer> usedBagNoMap = new HashMap<String, Integer>();

	public static void addPropBagXMLList(String bagNo, List<DropXMLInfo> dropXMLList,boolean modify) {
		if (map.containsKey(bagNo) && !modify) {
			throw new RuntimeException("Load PropBag.xml error! Type: " + bagNo + " repeat");
		}
		map.put(bagNo, dropXMLList);
	}

	public static List<DropXMLInfo> getPropBagXMLList(String bagNo) {
		return map.get(bagNo);
	}

	public static List<DropXMLInfo> getPropBagXMLListbyStr(String bagNoStr) {
		List<DropXMLInfo> list = new ArrayList<DropXMLInfo>();
		if (bagNoStr != null && bagNoStr.length() > 0) {
			for (String bagNo : bagNoStr.split(",")) {
				if (map.containsKey(bagNo)) {
					list.addAll(map.get(bagNo));
				}
			}
		}
		return list;
	}

	/**
	 * 记录已经使用的bagNO
	 * 
	 * @param bagNo
	 */
	public static void addUsedBagNo(String bagNo) {
		usedBagNoMap.put(bagNo, 1);
	}

	/**
	 * 统计没有使用的奖励
	 * 
	 * @return
	 */
	public static String checkUsedBagNo() {

		if (usedBagNoMap.size() >= map.size()) {
			return null;
		}

		StringBuilder noUsedBagNoBuilder = new StringBuilder();

		for (String no : map.keySet()) {
			if (usedBagNoMap.get(no) == null) {
				noUsedBagNoBuilder.append(no);
				noUsedBagNoBuilder.append(",");
			}
		}

		return noUsedBagNoBuilder.toString();
	}
	
	/**
	 * 获取礼包的随机数的最小值
	 * @param bagNo
	 * @return
	 */
	public static int getMinRandom(String bagNo){
		
		List<DropXMLInfo> prizeXmls = map.get(bagNo);
		int min = 0;
		for (DropXMLInfo dropXMLInfo : prizeXmls) {
			if (min > dropXMLInfo.getMinRand()) {
				min = dropXMLInfo.getMinRand();
			}
		}
		return min;
	}
	
	/**
	 * 获取礼包的随机数的最大值
	 * @param bagNo
	 * @return
	 */
	public static int getMaxRandom(String bagNo){
		
		List<DropXMLInfo> prizeXmls = map.get(bagNo);
		int max = 0;
		for (DropXMLInfo dropXMLInfo : prizeXmls) {
			if (max < dropXMLInfo.getMaxRand()) {
				max = dropXMLInfo.getMaxRand();
			}
		}
		return max;
	}
	
	/**
	 * 获取dropXml的Str （签到专用 - 运营需求）
	 * @param bagNoStr 
	 * @return
	 */
	public static String getPropBagXMLByStr(String bagNoStr, String itemNo) {
		String propBagStr = "";
		if (bagNoStr != null && bagNoStr.length() > 0) {
			for (String bagNo : bagNoStr.split(","))
			{
				if (map.containsKey(bagNo)) {
					List<DropXMLInfo> prizeXmls = map.get(bagNo);
					for(DropXMLInfo info : prizeXmls)
					{
						String propNo = info.getItemNo();
						if(propNo.startsWith(GameValue.PROP_STAR_N0) && itemNo.startsWith(GameValue.PROP_STAR_N0))
						{
							propNo = itemNo;
						}
						if(propBagStr.length() == 0)
						{
							propBagStr = propNo + "," + info.getItemMinNum();
						}
						else 
						{
							propBagStr = propBagStr + ";" + propNo + "," + info.getItemMinNum();
						}
					}
				}
			}
		}
		return propBagStr;
	}
	
	/**
	 * 获取dropXMLInfo （签到专用 - 运营需求）
	 * @param bagNoStr
	 * @param itemNo
	 * @return
	 */
	public static List<DropXMLInfo> getPropBagXMLListbyStr(String bagNoStr, String itemNo) {
		List<DropXMLInfo> list = new ArrayList<DropXMLInfo>();
		if (bagNoStr != null && bagNoStr.length() > 0) {
			for (String bagNo : bagNoStr.split(",")) {
				if (map.containsKey(bagNo)) {
					List<DropXMLInfo> prizeXmls = map.get(bagNo);
					for(DropXMLInfo info : prizeXmls)
					{
						String propNo = info.getItemNo();
						if(propNo.startsWith(GameValue.PROP_STAR_N0) && itemNo.startsWith(GameValue.PROP_STAR_N0))
						{
							DropXMLInfo xmlInfo = new DropXMLInfo();
							xmlInfo.setItemNo(itemNo);
							xmlInfo.setItemMinNum(info.getItemMinNum());
							xmlInfo.setItemMaxNum(info.getItemMaxNum());
							xmlInfo.setMinRand(info.getMinRand());
							xmlInfo.setMaxRand(info.getMaxRand());
							xmlInfo.setDropType(info.getDropType());
							xmlInfo.setParam(info.getParam());
							
							list.add(xmlInfo);
						} else {
							list.add(info);
						}
					}
				}
			}
		}
		return list;
	}
}
