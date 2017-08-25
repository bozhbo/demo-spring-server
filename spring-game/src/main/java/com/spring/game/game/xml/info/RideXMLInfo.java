package com.snail.webgame.game.xml.info;

import java.util.HashMap;
import java.util.Map;

import com.snail.webgame.game.common.GameValue;
import com.snail.webgame.game.common.HeroPropertyInfo;

public class RideXMLInfo {
	
	/**
	 * 对应开关坐骑编号
	 */
	public final static int RIDE_OPEN_NO_1 = 72000001;
	public final static int RIDE_OPEN_NO_2 = 72000004;
	public final static int RIDE_OPEN_NO_3 = 72000006;
	public final static int RIDE_OPEN_NO_4 = 72000002;
	public final static int RIDE_OPEN_NO_5 = 72000003;
	public final static int RIDE_OPEN_NO_6 = 72000005;

	private int no;// 坐骑编号

	private int rideChipNo;// 坐骑碎片编号
	private int callChipCost;// 合成坐骑消耗
	
	private int initQua;// 初始品阶
	
	/**
	 * 坐骑升级配置信息 key：rideLv
	 */
	private Map<Integer, LvUpInfo> rideLvUpMap = new HashMap<Integer, LvUpInfo>();

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public int getRideChipNo() {
		return rideChipNo;
	}

	public void setRideChipNo(int rideChipNo) {
		this.rideChipNo = rideChipNo;
	}

	public int getCallChipCost() {
		return callChipCost;
	}

	public void setCallChipCost(int callChipCost) {
		this.callChipCost = callChipCost;
	}

	public Map<Integer, LvUpInfo> getRideLvUpMap() {
		return rideLvUpMap;
	}

	public void setRideLvUpMap(Map<Integer, LvUpInfo> rideLvUpMap) {
		this.rideLvUpMap = rideLvUpMap;
	}

	public int getInitQua() {
		return initQua;
	}

	public void setInitQua(int initQua) {
		this.initQua = initQua;
	}
	
	/**
	 * 检测坐骑开关是否打开
	 * 
	 * @param rideNo
	 */
	public static boolean checkRideIsOpen(int rideNo) {
		int flag = 0;
		switch (rideNo) {
		case RIDE_OPEN_NO_1:
			flag = GameValue.RIDE_OPEN_1;
			break;
		case RIDE_OPEN_NO_2:
			flag = GameValue.RIDE_OPEN_2;
			break;
		case RIDE_OPEN_NO_3:
			flag = GameValue.RIDE_OPEN_3;
			break;
		case RIDE_OPEN_NO_4:
			flag = GameValue.RIDE_OPEN_4;
			break;
		case RIDE_OPEN_NO_5:
			flag = GameValue.RIDE_OPEN_5;
			break;
		case RIDE_OPEN_NO_6:
			flag = GameValue.RIDE_OPEN_6;
			break;

		default:
			break;
		}
		
		if (flag == 1) {
			return true;
		}
		return false;
	}
	
	public static String fetchOpenRideNo() {
		StringBuilder sb = new StringBuilder();
		if (GameValue.RIDE_OPEN_1 == 1) {
			sb.append(RIDE_OPEN_NO_1).append(",");
		}
		if (GameValue.RIDE_OPEN_2 == 1) {
			sb.append(RIDE_OPEN_NO_2).append(",");
		}
		if (GameValue.RIDE_OPEN_3 == 1) {
			sb.append(RIDE_OPEN_NO_3).append(",");
		}
		if (GameValue.RIDE_OPEN_4 == 1) {
			sb.append(RIDE_OPEN_NO_4).append(",");
		}
		if (GameValue.RIDE_OPEN_5 == 1) {
			sb.append(RIDE_OPEN_NO_5).append(",");
		}
		if (GameValue.RIDE_OPEN_6 == 1) {
			sb.append(RIDE_OPEN_NO_6).append(",");
		}
		
		if (sb.length() > 0) {
			return sb.deleteCharAt(sb.length() - 1).toString();
		}
		
		return "";
	}

	/**
	 * 坐骑升级配置信息
	 * 
	 * @author nijp
	 * 
	 */
	public static class LvUpInfo extends HeroPropertyInfo{
		private int lv;

		public int getLv() {
			return lv;
		}

		public void setLv(int lv) {
			this.lv = lv;
		}
	}

	/**
	 * 坐骑升级、进阶消耗
	 * 
	 * @author nijp
	 * 
	 */
	public static class UpCostInfo {
		private int lvOrQua;// 坐骑等级或品质
		private int itemNo;// 消耗的道具编号
		private int itemNum;// 消耗的道具数量
		private int moneyCost;// 银币消耗

		public int getLvOrQua() {
			return lvOrQua;
		}

		public void setLvOrQua(int lvOrQua) {
			this.lvOrQua = lvOrQua;
		}

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

		public int getMoneyCost() {
			return moneyCost;
		}

		public void setMoneyCost(int moneyCost) {
			this.moneyCost = moneyCost;
		}
	}
}
