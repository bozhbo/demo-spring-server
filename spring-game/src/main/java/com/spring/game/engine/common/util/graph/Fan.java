package com.snail.webgame.engine.common.util.graph;

import org.critterai.math.Vector3;

/**
 * 扇形
 * @author zenggy
 *
 */
public class Fan extends Circular implements Graph {

	/**
	 * 方向向量
	 */
	protected Vector3 way;
	/**
	 * 角度
	 */
	protected float angle;
	/**
	 * 构造体
	 * @param v 扇形顶点
	 * @param r 半径
	 * @param way 方向向量
	 * @param angle 扇形角度
	 */
	public Fan(Vector3 v, float r, Vector3 way, float angle) {
		super(v, r);
		this.way = way;
		this.angle = angle;
	}
	
	@Override
	public boolean pointInGraph(Vector3 p) {
		Vector3 v1 = new Vector3(p.x-this.v.x, p.y-this.v.y, p.z-this.v.z);
		
		//求出2向量的模
		double a = Math.sqrt(Math.pow(v1.x, 2) + Math.pow(v1.y, 2) + Math.pow(v1.z, 2));
		double b = Math.sqrt(Math.pow(this.way.x, 2) + Math.pow(this.way.y, 2) + Math.pow(this.way.z, 2));
		
		//再求出两个向量的向量积
		double ab = this.way.dot(v1);
		
		//两向量夹角
		double v1_way = Math.acos(ab/(a*b));
		//弧度转成角度
		v1_way = v1_way/Math.PI*180;
		
		return super.pointInGraph(p) && (v1_way <= this.angle/2);
	}

}
