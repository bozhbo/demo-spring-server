package com.snail.webgame.game.protocal.shizhuang.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.math.NumberUtils;

import com.snail.webgame.game.cache.BagItemMap;
import com.snail.webgame.game.cache.EquipInfoMap;
import com.snail.webgame.game.cache.HeroInfoMap;
import com.snail.webgame.game.common.ErrorCode;
import com.snail.webgame.game.common.GameValue;
import com.snail.webgame.game.info.BagItemInfo;
import com.snail.webgame.game.info.EquipInfo;
import com.snail.webgame.game.info.HeroInfo;
import com.snail.webgame.game.info.RoleInfo;
import com.snail.webgame.game.xml.cache.EquipXMLInfoMap;
import com.snail.webgame.game.xml.cache.PropXMLInfoMap;
import com.snail.webgame.game.xml.info.EquipXMLInfo;

public class ShizhuangService {

	public static void refreshRoleLockShizhuang(RoleInfo roleInfo) {
		HeroInfo heroInfo = HeroInfoMap.getMainHeroInfo(roleInfo);
		if (heroInfo == null) {
			return;
		}
		roleInfo.getLockShizhuangMap().clear();
		Map<Integer, Integer> allLock = roleInfo.getLockShizhuang();
		if (allLock != null && allLock.size() > 0) {
			int equipId = 0;
			EquipInfo equipInfo = null;
			for (int equipType : allLock.keySet()) {
				equipId = allLock.get(equipType);
				equipInfo = EquipInfoMap.getHeroEquip(heroInfo, equipId);
				if (equipInfo == null) {
					equipInfo = EquipInfoMap.getBagEquip(roleInfo.getId(), equipId);
				}
				if (equipInfo != null) {
					roleInfo.getLockShizhuangMap().put(equipType, equipInfo);
				}
			}
		}
	}

	/**
	 * 查询时装套装收集情况(相同的时装只保存1件)返回套数
	 */
	public static int shizhuangTypeCondition(RoleInfo roleInfo) {
		Map<Integer, List<Integer>> myShizhuang = new HashMap<Integer, List<Integer>>();
		Map<Integer, EquipInfo> bagEquipMap = EquipInfoMap.getBagEquipMap(roleInfo.getId());
		if (bagEquipMap != null) {
			// 背包时装列表
			for (Entry<Integer, EquipInfo> en : bagEquipMap.entrySet()) {
				EquipXMLInfo equipInfo = EquipXMLInfoMap.getEquipXMLInfo(en.getValue().getEquipNo());
				if (equipInfo == null || (equipInfo.getEquipType() != 9 && equipInfo.getEquipType() != 10)) {
					continue;
				}
				List<Integer> allByType = myShizhuang.get(equipInfo.getShizhuangType());
				if (allByType == null) {
					myShizhuang.put(equipInfo.getShizhuangType(), allByType = new ArrayList<Integer>());
				}
				if (!allByType.contains(en.getValue().getEquipNo())) {
					allByType.add(en.getValue().getEquipNo());
				}
			}
		}
		HeroInfo heroInfo = HeroInfoMap.getMainHeroInfo(roleInfo);
		if (heroInfo != null) {
			// 主将时装列表
			Map<Integer, EquipInfo> heroEquipMap = EquipInfoMap.getHeroEquipMap(heroInfo);
			if (heroEquipMap != null) {
				for (Entry<Integer, EquipInfo> en : heroEquipMap.entrySet()) {
					EquipXMLInfo equipInfo = EquipXMLInfoMap.getEquipXMLInfo(en.getValue().getEquipNo());
					if (equipInfo == null || (equipInfo.getEquipType() != 9 && equipInfo.getEquipType() != 10)) {
						continue;
					}
					List<Integer> allByType = myShizhuang.get(equipInfo.getShizhuangType());
					if (allByType == null) {
						myShizhuang.put(equipInfo.getShizhuangType(), allByType = new ArrayList<Integer>());
					}
					if (!allByType.contains(en.getValue().getEquipNo())) {
						allByType.add(en.getValue().getEquipNo());
					}
				}
			}
		}
		int num = 0;
		for (Entry<Integer, List<Integer>> en : myShizhuang.entrySet()) {
			List<EquipXMLInfo> list = EquipXMLInfoMap.getAddShizhuangEquip(en.getKey());
			if (list == null || en.getValue().size() != list.size()) {
				continue;
			}
			num++;
		}
		return num;
	}

	/**
	 * 判断itemNo 是否已存在的时装/已存在的时装碎片
	 * @param roleInfo
	 * @param itemNo
	 * @param itemNum
	 * @return
	 */
	public static int checkIsShizhuangItemNo(RoleInfo roleInfo, String itemNo, int itemNum) {
		int propNo = 0;// 0-不是时装碎片
		if (itemNo.startsWith(GameValue.PROP_PRIZE_N0)) {
			propNo = NumberUtils.toInt(itemNo);
			Integer szItemNo = PropXMLInfoMap.getSZItemNo(propNo);
			if (szItemNo != null) {
				itemNo = szItemNo + "";
			}
		}
		return checkIsHavingSzEquip(roleInfo, itemNo, itemNum, propNo);
	}

	/**
	 * 判断角色是否有该时装或满足合成一个时装的碎片
	 * @param roleInfo
	 * @param szEquipNo
	 * @param itemNum
	 * @param chipNo 0-不是时装碎片
	 * @return
	 */
	private static int checkIsHavingSzEquip(RoleInfo roleInfo, String szEquipNo, int itemNum, int chipNo) {
		if (szEquipNo != null && szEquipNo.length() > 0 && szEquipNo.startsWith(GameValue.EQUIP_N0)) {
			int equipNo = NumberUtils.toInt(szEquipNo);
			EquipXMLInfo xmlInfo = EquipXMLInfoMap.getEquipXMLInfo(equipNo);
			if (xmlInfo != null && (xmlInfo.getEquipType() == 9 || xmlInfo.getEquipType() == 10)) {
				
				HeroInfo mainHero = HeroInfoMap.getMainHeroInfo(roleInfo);
				if (mainHero != null) {
					// 判断主武将是否有该时装
					EquipInfo equipInfo = EquipInfoMap.getHeroEquipbyType(mainHero, xmlInfo.getEquipType());
					if (equipInfo != null && equipInfo.getEquipNo() == equipNo) {
						return ErrorCode.ITEM_ADD_ERROR_4;
					}
				}
				// 判断背包是否有该时装
				if (EquipInfoMap.checkBagEquip(roleInfo.getId(), equipNo)) {
					return ErrorCode.ITEM_ADD_ERROR_4;
				}
				
				if (chipNo != 0) {
					// 碎片判断是否已满足合成一个时装
					HashMap<Integer, Integer> chipMap = PropXMLInfoMap.getComposeXMLInfo(equipNo);
					if (chipMap != null && chipMap.containsKey(chipNo)) {
						int chipNum = chipMap.get(chipNo);
						if (chipNum < itemNum) {
							// 已超出数量
							return ErrorCode.ITEM_ADD_ERROR_2;
						} else {
							// 背包碎片已超出数量
							BagItemInfo item = BagItemMap.checkBagItem(roleInfo, chipNo);
							if(item != null && item.getNum() + itemNum > chipNum)
							{
								return ErrorCode.ITEM_ADD_ERROR_2;
							}
						}
					}
				} else {
					if (itemNum != 1) {
						// 只能掉落一个时装
						return ErrorCode.ITEM_ADD_ERROR_5;
					}
				}
			}
		}
		return 1;
	}
}
