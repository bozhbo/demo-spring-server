package com.snail.webgame.game.info;

import com.snail.webgame.engine.common.to.BaseTO;
import com.snail.webgame.game.common.GameValue;

/**
 * 物品背包
 * @author tangjq
 */
public class BagItemInfo extends BaseTO {

	/**
	 * 操作物品 (2-宝物  3-将星 4-宝石 5-消耗品 6-材料  8-碎片)
	 */
	public static final int TYPE_PROP = 2;
	public static final int TYPE_STAR = 3;
	public static final int TYPE_STONE = 4;
	public static final int TYPE_OTHER = 5;
	public static final int TYPE_MAT = 6;
	public static final int TYPE_CHIP = 8;

	public static final int BAG_TYPE_SIZE = 6;

	private int roleId;

	private int itemType;// 道具类型(2-宝物 3-将星 4-宝石 5-其他)
	private int itemNo;// 道具编号
	private int num;// 数量

	private int colour;// 颜色
	private int level;// 装备等级（装备,宝石特有）
	private int isTransition; //是否是英雄转换的星石头 0 - 不是 1 - 是

	public BagItemInfo() {

	}

	public BagItemInfo(int roleId, int itemType, int itemNo, int num, int colour, int level) {
		this.roleId = roleId;
		this.itemType = itemType;
		this.itemNo = itemNo;
		this.num = num;
		this.colour = colour;
		this.level = level;
	}

	public int getItemType() {
		return itemType;
	}

	public void setItemType(int itemType) {
		this.itemType = itemType;
	}

	public int getItemNo() {
		return itemNo;
	}

	public void setItemNo(int itemNo) {
		this.itemNo = itemNo;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public int getColour() {
		return colour;
	}

	public void setColour(int colour) {
		this.colour = colour;
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	/**
	 * 根据装备编号获得装备类型
	 * @param itemNo
	 * @return
	 */
	public static int getItemType(String itemNo) {
		if (itemNo.startsWith(GameValue.PROP_STAR_N0)) {
			return BagItemInfo.TYPE_STAR;
		} else if (itemNo.startsWith(GameValue.PROP_TREASURE_N0)) {
			return BagItemInfo.TYPE_PROP;
		} else if (itemNo.startsWith(GameValue.PROP_STONE_N0)) {
			return BagItemInfo.TYPE_STONE;
		} else if (itemNo.startsWith(GameValue.PROP_ITEM_N0)) {
			return BagItemInfo.TYPE_MAT;
		} else if (itemNo.startsWith(GameValue.PROP_OTHER_N0)) {
			return BagItemInfo.TYPE_OTHER;
		} else if(itemNo.startsWith(GameValue.PROP_PRIZE_N0)){
			return BagItemInfo.TYPE_CHIP;
		} else if(itemNo.startsWith(GameValue.WEAPAN_CHIP_NO)){
			return BagItemInfo.TYPE_CHIP;
		} else if(itemNo.startsWith(GameValue.PROP_CHIP_NO)){
			return BagItemInfo.TYPE_CHIP;
		} else if(itemNo.startsWith(GameValue.PROP_RECRUIT_NO)){
			return BagItemInfo.TYPE_OTHER;
		}
			
		return 0;
	}

	@Override
	public byte getSaveMode() {
		return 0;
	}

	public int getIsTransition() {
		return isTransition;
	}

	public void setIsTransition(int isTransition) {
		this.isTransition = isTransition;
	}
	
}
