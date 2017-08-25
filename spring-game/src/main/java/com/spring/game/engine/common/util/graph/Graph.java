package com.snail.webgame.engine.common.util.graph;

import org.critterai.math.Vector3;

/**
 * 图形
 * @author zenggy
 *
 */
public interface Graph {


	/**
	 * 判断点是否在此图形内
	 * @param p
	 * @return
	 */
	public boolean pointInGraph(Vector3 p);
}
