package com.snail.webgame.engine.common.util.graph;

import org.critterai.math.Vector3;

/**
 * 圆
 * @author zenggy
 *
 */
public class Circular implements Graph {
	protected Vector3 v;//圆心
	protected float r;//半径
	protected float h;//圆高
	/**
	 * 
	 * @param v 圆心
	 * @param r 半径
	 */
	public Circular(Vector3 v, float r){
		this.v = v;
		this.r = r;
		this.h = 10;
	}
	
	/**
	 * 
	 * @param v 圆心
	 * @param r 半径
	 * @param h 圆高
	 */
	public Circular(Vector3 v, float r, float h){
		this.v = v;
		this.r = r;
		this.h = h;
	}
	
	/**
	 * 判断点是否在此图形内
	 * @param p
	 * @return
	 */
	public boolean pointInGraph(Vector3 p){
		double k = Math.sqrt(Math.pow(Math.abs(p.x-v.x), 2) + Math.pow(Math.abs(p.z-v.z), 2));
		if(k <= r && p.y >= v.y && p.y <= v.y+this.h){
			return true;
		}
		return false;
	}
}
