package com.snail.webgame.engine.common.pathfinding.astar;

import java.util.ArrayList;

/**
 * A path determined by some path finding algorithm. A series of steps from
 * the starting location to the target location. This includes a step for the
 * initial location.
 * 
 * @author Kevin Glass
 */
public class Path {
	/** The list of steps building up this path */
	private ArrayList<Step> steps = new ArrayList<Step>();//
	
	private ArrayList<Step> smoothPaths = new ArrayList<Step>();//
	private ArrayList<Integer> lineNums = null;
	
	/**
	 * Create an empty path
	 */
	public Path() {
		
	}

 
	public ArrayList<Step> getSteps() {
		return steps;
	}


	public void setSteps(ArrayList<Step> steps) {
		this.steps = steps;
	}


	public ArrayList<Step> getSmoothPaths() {
		return smoothPaths;
	}


	public void setSmoothPaths(ArrayList<Step> smoothPaths) {
		this.smoothPaths = smoothPaths;
	}


	public ArrayList<Integer> getLineNums() {
		return lineNums;
	}


	public void setLineNums(ArrayList<Integer> lineNums) {
		this.lineNums = lineNums;
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
	 */
	public void appendStep(float x, float y) {
		steps.add(new Step(x,y));
	}

	/**
	 * Prepend a step to the path.  
	 * 
	 * @param x The x coordinate of the new step
	 * @param y The y coordinate of the new step
	 */
	public void prependStep(float x, float y) {
		steps.add(0, new Step(x, y));
	}

	public Step getEndStep(){
		return this.smoothPaths.get(smoothPaths.size()-1);
	}


}
