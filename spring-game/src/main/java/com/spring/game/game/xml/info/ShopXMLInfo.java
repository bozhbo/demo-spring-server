package com.snail.webgame.game.xml.info;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.snail.webgame.game.condtion.AbstractConditionCheck;

/**
 * 商店信息
 * @author zenggang
 */
public class ShopXMLInfo {

	public static final int SHOP_TYPE_1 = 1;// 一次购买单个，此时Cost为单个道具的价格
	public static final int SHOP_TYPE_2 = 2;// 一次购买多个，此时Cost为多个道具的价格-->

	private int no;// 商店编号
	private int shopType;// 商店类型
	private String costType;// 商店货币类型
	private String autoRefreshTime;// 定时更新时间 HH：mm
	private List<ShopRoleLevels> roleLvList;// 用户等级商店信息
	private Map<Integer, ShopRefresh> shopRefresh;// 商店刷新信息
	private int fixed;// 0:固定次数 1:无限次数，超过最后一次后价格不递增，按照最后一次的价格来

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public int getShopType() {
		return shopType;
	}

	public void setShopType(int shopType) {
		this.shopType = shopType;
	}

	public String getCostType() {
		return costType;
	}

	public void setCostType(String costType) {
		this.costType = costType;
	}

	public String getAutoRefreshTime() {
		return autoRefreshTime;
	}

	public void setAutoRefreshTime(String autoRefreshTime) {
		this.autoRefreshTime = autoRefreshTime;
	}

	public List<ShopRoleLevels> getRoleLvList() {
		return roleLvList;
	}

	public void setRoleLvList(List<ShopRoleLevels> roleLvList) {
		this.roleLvList = roleLvList;
	}

	public Map<Integer, ShopRefresh> getShopRefresh() {
		return shopRefresh;
	}

	public void setShopRefresh(Map<Integer, ShopRefresh> shopRefresh) {
		this.shopRefresh = shopRefresh;
	}
	
	public int getFixed() {
		return fixed;
	}

	public void setFixed(int fixed) {
		this.fixed = fixed;
	}


	/**
	 * 商店刷新信息
	 * @author zenggang
	 */
	public static class ShopRefresh {
		private int no;// 购买次数
		// 购买条件
		private List<AbstractConditionCheck> conditions = new ArrayList<AbstractConditionCheck>();

		public int getNo() {
			return no;
		}

		public void setNo(int no) {
			this.no = no;
		}

		public List<AbstractConditionCheck> getConditions() {
			return conditions;
		}

		public void setConditions(List<AbstractConditionCheck> conditions) {
			this.conditions = conditions;
		}
	}

	/**
	 * 用户等级商店信息
	 * @author zenggang
	 */
	public static class ShopRoleLevels {
		private int no;// 商店编号
		private int minRoleLv;
		private int maxRoleLv;
		private Map<Integer, List<ShopItem>> items;// 商品信息

		public int getNo() {
			return no;
		}

		public void setNo(int no) {
			this.no = no;
		}

		public int getMinRoleLv() {
			return minRoleLv;
		}

		public void setMinRoleLv(int minRoleLv) {
			this.minRoleLv = minRoleLv;
		}

		public int getMaxRoleLv() {
			return maxRoleLv;
		}

		public void setMaxRoleLv(int maxRoleLv) {
			this.maxRoleLv = maxRoleLv;
		}

		public Map<Integer, List<ShopItem>> getItems() {
			return items;
		}

		public void setItems(Map<Integer, List<ShopItem>> items) {
			this.items = items;
		}
	}

	/**
	 * 商店商品
	 * @author zenggang
	 */
	public static class ShopItem {
		private int itemNo;// 商品道具编号
		private int itemNum;// 商品道具数量
		private String costType;//货币类型
		private int cost;// 需要货币量
		private int rand;// 概率
		private String condition;

		/**
		 * 缓存字段 概率用
		 */
		private int minRand;
		private int maxRand;

		public int getItemNo() {
			return itemNo;
		}

		public void setItemNo(int itemNo) {
			this.itemNo = itemNo;
		}

		public int getItemNum() {
			return itemNum;
		}

		public void setItemNum(int itemNum) {
			this.itemNum = itemNum;
		}

		public String getCostType() {
			return costType;
		}

		public void setCostType(String costType) {
			this.costType = costType;
		}

		public int getCost() {
			return cost;
		}

		public void setCost(int cost) {
			this.cost = cost;
		}

		public int getRand() {
			return rand;
		}

		public void setRand(int rand) {
			this.rand = rand;
		}

		public int getMinRand() {
			return minRand;
		}

		public void setMinRand(int minRand) {
			this.minRand = minRand;
		}

		public int getMaxRand() {
			return maxRand;
		}

		public void setMaxRand(int maxRand) {
			this.maxRand = maxRand;
		}

		public String getCondition() {
			return condition;
		}

		public void setCondition(String condition) {
			this.condition = condition;
		}
		
	}
}
