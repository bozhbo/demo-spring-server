package com.snail.webgame.engine.common.pathfinding.astar;

import java.util.ArrayList;
import java.util.List;


 
public class UnitMover implements Mover {
	private int gridNum;
	private String type;//阻挡类型
	private List<String> teamType;//不阻挡类型队列
	private boolean moveFlag;//true  不判断方阵阻挡
 
	public UnitMover(String type,int gridNum,boolean moveFlag) {
		this.type = type;
		this.teamType = new ArrayList<String>();
		this.gridNum = gridNum;
		this.moveFlag = moveFlag;
	}
	public UnitMover(String type, List<String> teamType, int gridNum,boolean moveFlag) {
		this.type = type;
		this.teamType = teamType;
		this.gridNum = gridNum;
		this.moveFlag = moveFlag;
	}
 
	public String getType() {
		return type;
	}
	
	public boolean isMoveFlag() {
		return moveFlag;
	}

	public List<String> getTeamType() {
		return teamType;
	}
	public int getGridNum()
	{
		return gridNum;
	}
	
}
