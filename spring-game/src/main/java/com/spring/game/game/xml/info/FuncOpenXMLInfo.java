package com.snail.webgame.game.xml.info;

import java.util.List;

import com.snail.webgame.game.common.fightdata.DropInfo;
import com.snail.webgame.game.condtion.AbstractConditionCheck;

/**
 * 功能开启
 * 
 * @author nijp
 *
 */
public class FuncOpenXMLInfo {

	public static final int OUT_CITY_NO = 16;// 出城开启
	public static final int MINE_NO = 23;// 资源开采系统开启

	private int no;

	private List<AbstractConditionCheck> checkConds;// 功能开启检测条件
	private List<DropInfo> prizes;

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public List<AbstractConditionCheck> getCheckConds() {
		return checkConds;
	}

	public void setCheckConds(List<AbstractConditionCheck> checkConds) {
		this.checkConds = checkConds;
	}

	public List<DropInfo> getPrizes() {
		return prizes;
	}

	public void setPrizes(List<DropInfo> prizes) {
		this.prizes = prizes;
	}

}
