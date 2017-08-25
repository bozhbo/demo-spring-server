package com.snail.webgame.engine.common.pathfinding.mesh;

/**
 * @author wangxf
 * @date 2012-8-14
 * A single step within the path
 */
public class Step3D {
	/** The x coordinate at the given step */
	private float x;
	/** The y coordinate at the given step */
	private float y;
	/** The z coordinate at the given step */
	private float z;
	
	
	public Step3D()
	{
		
	}
	/**
	 * Create a new step
	 * 
	 * @param x The x coordinate of the new step
	 * @param y The y coordinate of the new step
	 */
	public Step3D(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
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
	 * Get the z coordinate of the new step
	 * 
	 * @return The z coodindate of the new step
	 */
	public float getZ() {
		return z;
	}
	
	public void setX(float x) {
		this.x = x;
	}
	public void setY(float y) {
		this.y = y;
	}
	public void setZ(float z) {
		this.z = z;
	}
	/**
	 * @see Object#hashCode()
	 */
	public int hashCode() {
		return (int)(x*y*z);
	}
	
	/**
	 * @see Object#equals(Object)
	 */
	public boolean equals(Object other) {
		if (other instanceof Step3D) {
			Step3D o = (Step3D) other;
			
			return (o.x == x) && (o.y == y) && (o.z == z);
		}
		
		return false;
	}
}
