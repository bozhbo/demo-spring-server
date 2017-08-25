package com.snail.webgame.engine.common.pathfinding.mesh;

import java.util.ArrayList;

/**
 * @author wangxf
 * @date 2012-8-14
 * 3D地图寻路路径
 */
public class Path3D {
	/** The list of steps building up this path */
	private ArrayList<Step3D> steps = new ArrayList<Step3D>();
	
	/**
	 * Create an empty path
	 */
	public Path3D() {
		
	}

 
	public ArrayList<Step3D> getSteps() {
		return steps;
	}


	public void setSteps(ArrayList<Step3D> steps) {
		this.steps = steps;
	}
	
	public void remove(int index)
	{
		steps.remove(index);
	}
 
	/**
	 * Append a step to the path.  
	 * 
	 * @param x The x coordinate of the new step
	 * @param y The y coordinate of the new step
	 * @param z The z coordinate of the new step
	 */
	public void appendStep(float x, float y, float z) {
		steps.add(new Step3D(x, y, z));
	}

	/**
	 * Prepend a step to the path.  
	 * 
	 * @param x The x coordinate of the new step
	 * @param y The y coordinate of the new step
	 * @param z The z coordinate of the new step
	 */
	public void prependStep(float x, float y, float z) {
		steps.add(0, new Step3D(x, y, z));
	}
}
