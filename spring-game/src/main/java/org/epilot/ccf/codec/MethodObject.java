package org.epilot.ccf.codec;
import java.util.LinkedHashMap;

public class MethodObject {
	private LinkedHashMap getMethod;
	private String getMethodCode[];
	private String getMethodType[];
	private LinkedHashMap setMethod;
	private String setMethodCode[];
	private String setMethodType[];
	public LinkedHashMap getGetMethod() {
		return getMethod;
	}
	public void setGetMethod(LinkedHashMap getMethod) {
		this.getMethod = getMethod;
	}
	public LinkedHashMap getSetMethod() {
		return setMethod;
	}
	public void setSetMethod(LinkedHashMap setMethod) {
		this.setMethod = setMethod;
	}
	public String[] getGetMethodCode() {
		return getMethodCode;
	}
	public void setGetMethodCode(String[] getMethodCode) {
		this.getMethodCode = getMethodCode;
	}
	public String[] getSetMethodCode() {
		return setMethodCode;
	}
	public void setSetMethodCode(String[] setMethodCode) {
		this.setMethodCode = setMethodCode;
	}
	public String[] getGetMethodType() {
		return getMethodType;
	}
	public void setGetMethodType(String[] getMethodType) {
		this.getMethodType = getMethodType;
	}
	public String[] getSetMethodType() {
		return setMethodType;
	}
	public void setSetMethodType(String[] setMethodType) {
		this.setMethodType = setMethodType;
	}
}
