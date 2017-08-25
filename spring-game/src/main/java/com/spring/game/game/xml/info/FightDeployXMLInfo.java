package com.snail.webgame.game.xml.info;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.snail.webgame.game.condtion.AbstractConditionCheck;

public class FightDeployXMLInfo {
	
	private int type;
	// <no,DeployCond>
	private Map<Integer, DeployCond> deployCondMap;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Map<Integer, DeployCond> getDeployCondMap() {
		return deployCondMap;
	}

	public void setDeployCondMap(Map<Integer, DeployCond> deployCondMap) {
		this.deployCondMap = deployCondMap;
	}

	/**
	 * 添加具体位置条件
	 * @param deployCond
	 */
	public void addDeployCond(DeployCond deployCond) {
		if (this.deployCondMap == null) {
			this.deployCondMap = new HashMap<Integer, DeployCond>();
		}
		deployCondMap.put(deployCond.getNo(), deployCond);
	}

	/**
	 * 获取格子条件
	 * @param posNo
	 * @return
	 */
	public DeployCond getPosDeployCond(int posNo) {
		if (this.deployCondMap == null) {
			return null;
		}
		return deployCondMap.get(posNo);
	}

	/**
	 * 位置
	 * @return
	 */
	public Set<Integer> getPos() {
		if (this.deployCondMap == null) {
			return null;
		}
		return deployCondMap.keySet();
	}

	// 上阵武将/所从条件
	public static class DeployCond {

		private int no;
		private List<AbstractConditionCheck> list;// 条件
		private int deployHeroLv;// 上阵武将等级

		public int getNo() {
			return no;
		}

		public void setNo(int no) {
			this.no = no;
		}
		
		public List<AbstractConditionCheck> getList() {
			return list;
		}

		public void setList(List<AbstractConditionCheck> list) {
			this.list = list;
		}

		public int getDeployHeroLv() {
			return deployHeroLv;
		}

		public void setDeployHeroLv(int deployHeroLv) {
			this.deployHeroLv = deployHeroLv;
		}

	}
}
