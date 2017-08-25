package org.epilot.ccf.common;


public class ProtocolsHead {
	private String className;
	private String endian;
	int lengthIndex;
	private String lengthProperty;
 
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getEndian() {
		return endian;
	}
	public void setEndian(String endian) {
		this.endian = endian;
	}
	public String getLengthProperty() {
		return lengthProperty;
	}
	public void setLengthProperty(String lengthProperty) {
		this.lengthProperty = lengthProperty;
	}
	public int getLengthIndex() {
		return lengthIndex;
	}
	public void setLengthIndex(int lengthIndex) {
		this.lengthIndex = lengthIndex;
	}

}
