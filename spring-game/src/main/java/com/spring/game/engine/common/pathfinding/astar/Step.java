package com.snail.webgame.engine.common.pathfinding.astar;

 

/**
 * A single step within the path
 * 
 * @author Kevin Glass
 */
public class Step {
	/** The x coordinate at the given step */
	private float x;
	/** The y coordinate at the given step */
	private float y;
	
	public Step()
	{
		
	}
	/**
	 * Create a new step
	 * 
	 * @param x The x coordinate of the new step
	 * @param y The y coordinate of the new step
	 */
	public Step(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Get the x coordinate of the new step
	 * 
	 * @return The x coodindate of the new step
	 */
	public float getX() {
		return x;
	}

	/**
	 * Get the y coordinate of the new step
	 * 
	 * @return The y coodindate of the new step
	 */
	public float getY() {
		return y;
	}
	
	/**
	 * @see Object#hashCode()
	 */
	public int hashCode() {
		return (int)(x*y);
	}
	
	/**
	 * @see Object#equals(Object)
	 */
	public boolean equals(Object other) {
		if (other instanceof Step) {
			Step o = (Step) other;
			
			return (o.x == x) && (o.y == y);
		}
		
		return false;
	}
	public void setX(float x) {
		this.x = x;
	}
	public void setY(float y) {
		this.y = y;
	}
	
	
}
