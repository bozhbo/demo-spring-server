package com.snail.webgame.game.xml.info;
/**
 * 跨服竞技场每日奖励
 * @author luwd
 *
 */
public class KuafuXMLPrize {
	private int no;
	private int minPlace;// 排名区间
	private int maxPlace;// 排名区间 0-表示无限大
	private String placeDropNoStr;// 排名掉落

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public int getMinPlace() {
		return minPlace;
	}

	public void setMinPlace(int minPlace) {
		this.minPlace = minPlace;
	}

	public int getMaxPlace() {
		return maxPlace;
	}

	public void setMaxPlace(int maxPlace) {
		this.maxPlace = maxPlace;
	}

	public String getPlaceDropNoStr() {
		return placeDropNoStr;
	}

	public void setPlaceDropNoStr(String placeDropNoStr) {
		this.placeDropNoStr = placeDropNoStr;
	}
}
