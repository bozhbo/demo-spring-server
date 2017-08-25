package com.snail.webgame.game.xml.info;

import java.util.ArrayList;
import java.util.List;

import com.snail.webgame.game.condtion.AbstractConditionCheck;

/**
 * 阵型xml
 * 
 * @author tangjq
 * 
 */
public class FormationXMLInfo {

	// 最大位置
	public static final int MAX_POSITION = 16;

	// 阵型no
	private int no;
	// 阵形开启条件
	private List<AbstractConditionCheck> conditions = new ArrayList<AbstractConditionCheck>();
	// 开启的位置
	private List<Integer> positions = new ArrayList<Integer>();

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

	public List<Integer> getPositions() {
		return positions;
	}

	public void setPositions(List<Integer> positions) {
		this.positions = positions;
	}

	/**
	 * 验证位置
	 * 
	 * @param formationPos
	 * @return
	 */
	public boolean checkPos(int formationPos) {
		if (positions.contains(formationPos)) {
			return true;
		}
		return false;
	}

	/**
	 * 根据阵型位置获取战斗获取战斗中位置
	 * 
	 * @param formationPos
	 * @return
	 */
	public int getFightPosByFormationPos(int formationPos) {
		if (!checkPos(formationPos)) {
			return 0;
		}

		return formationPos;
	}
}
