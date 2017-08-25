package com.snail.webgame.game.xml.info;

import java.util.HashMap;
import java.util.Map;

public class RideQuaXMLInfo {
	
	private int no;// 坐骑编号
	
	/**
	 * 坐骑进阶配置信息 key：quality
	 */
	private Map<Integer, QualityInfo> rideQuaMap = new HashMap<Integer, QualityInfo>();
	
	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public Map<Integer, QualityInfo> getRideQuaMap() {
		return rideQuaMap;
	}

	public void setRideQuaMap(Map<Integer, QualityInfo> rideQuaMap) {
		this.rideQuaMap = rideQuaMap;
	}

	/**
	 * 坐骑品阶配置信息
	 * 
	 * @author nijp
	 *
	 */
	public static class QualityInfo {
		
		private int quality;// 坐骑品阶
		private float addAttrRate;// 
		
		public QualityInfo() {
		}

		public QualityInfo(int quality, float addAttrRate) {
			this.quality = quality;
			this.addAttrRate = addAttrRate;
		}

		public int getQuality() {
			return quality;
		}

		public void setQuality(int quality) {
			this.quality = quality;
		}

		public float getAddAttrRate() {
			return addAttrRate;
		}

		public void setAddAttrRate(float addAttrRate) {
			this.addAttrRate = addAttrRate;
		}
	}
	
}
