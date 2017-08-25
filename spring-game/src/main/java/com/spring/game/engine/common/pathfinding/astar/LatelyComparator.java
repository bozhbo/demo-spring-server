package com.snail.webgame.engine.common.pathfinding.astar;

import java.awt.Point;
import java.util.Comparator;

import com.snail.webgame.engine.common.util.CommUtil;

public class LatelyComparator implements Comparator<Point>{

	private int sX;
	private int sY;
	
	public LatelyComparator(int sX, int sY){
		this.sX = sX;
		this.sY = sY;
	}
	
	public int compare(Point o1, Point o2) {
		float len1 = CommUtil.getDistance(sX, sY, o1.x, o1.y);
		float len2 = CommUtil.getDistance(sX, sY, o2.x, o2.y);
		if(len1 < len2){
			return -1;
		}
		else if(len1 > len2){
			return 1;
		}
		else{
			return 0;
		}
	}
}
