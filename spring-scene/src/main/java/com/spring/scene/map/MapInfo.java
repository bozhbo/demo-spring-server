package com.spring.scene.map;

public class MapInfo {

	private int mapId;
	private int rowSize;
	private int colSize;
	private double gridUnit; // 1.5M/2M
	private int gridTyoe; // 0-OK 1-NG
	private double x; // centrol point
	private double y;
	private double z;
	
	public int getMapId() {
		return mapId;
	}
	public void setMapId(int mapId) {
		this.mapId = mapId;
	}
	public int getRowSize() {
		return rowSize;
	}
	public void setRowSize(int rowSize) {
		this.rowSize = rowSize;
	}
	public int getColSize() {
		return colSize;
	}
	public void setColSize(int colSize) {
		this.colSize = colSize;
	}
	public double getGridUnit() {
		return gridUnit;
	}
	public void setGridUnit(double gridUnit) {
		this.gridUnit = gridUnit;
	}
	public int getGridTyoe() {
		return gridTyoe;
	}
	public void setGridTyoe(int gridTyoe) {
		this.gridTyoe = gridTyoe;
	}
	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
	}
	public double getY() {
		return y;
	}
	public void setY(double y) {
		this.y = y;
	}
	public double getZ() {
		return z;
	}
	public void setZ(double z) {
		this.z = z;
	}
	
	
}
