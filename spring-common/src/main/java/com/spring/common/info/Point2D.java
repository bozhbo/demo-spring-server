package com.spring.common.info;

/**
 * @author wangxf
 * @date 2012-8-1
 * 2维地图的点
 */
public class Point2D {
	public float x;
	public float z;
	
	public Point2D(float x, float z) {
		this.x = x;
		this.z = z;
	}

	/**
	 * 
	 */
	public Point2D() {
		// TODO Auto-generated constructor stub
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getZ() {
		return z;
	}

	public void setZ(float z) {
		this.z = z;
	}
	
}
