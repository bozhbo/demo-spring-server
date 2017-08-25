package com.snail.webgame.engine.common.util.graph;

import org.critterai.math.Vector3;


public class Triangle3D implements Graph {
	protected Vector3 a;
	protected Vector3 b;
	protected Vector3 c;
	
	/**
	 * @param a 三角形三个顶点
	 * @param b
	 * @param c
	 */
	public Triangle3D(Vector3 a, Vector3 b, Vector3 c){
		this.a = a;
		this.b = b;
		this.c = c;
	}
	
	
	protected boolean sameSide(Vector3 a, Vector3 b, Vector3 c, Vector3 v){
		Vector3 ab = new Vector3(b.x-a.x, b.y-a.y, b.z-a.z);
		Vector3 ac = new Vector3(c.x-a.x, c.y-a.y, c.z-a.z);
		Vector3 av = new Vector3(v.x-a.x, v.y-a.y, v.z-a.z);
		
		Vector3 v1 = new Vector3();
		Vector3.cross(ab, ac, v1);
		Vector3 v2 = new Vector3();
		Vector3.cross(ab, av, v2);
		
		return Vector3.dot(v1.x, v1.y, v1.z, v2.x, v2.y, v2.z) >= 0;
	}
	/**
	 * 判断点是否在此图形内
	 * @param p
	 * @return
	 */
	public boolean pointInGraph(Vector3 p){
		return sameSide(this.a, this.b, this.c, p)
			&& sameSide(this.b, this.c, this.a, p)
			&& sameSide(this.c, this.a, this.b, p)
			&& ((p.y >= this.a.y-0.5 && p.y <= this.b.y+2) || (p.y >= this.b.y-0.5 && p.y <= this.a.y+2));
	}
}
