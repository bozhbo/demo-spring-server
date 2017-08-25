package org.epilot.ccf.common;

import java.util.HashMap;
import java.util.Map;


public class ProtocolsProcessor {
	String protocolId;
	String messageBody;
	String processor;
	String usedTreadPool;
	String messageCode;
	
	Map<String, String> propertyMap = new HashMap<String, String>();
	
	
	public String getMessageCode() {
		return messageCode;
	}
	public void setMessageCode(String messageCode) {
		this.messageCode = messageCode;
	}
	public String getMessageBody() {
		return messageBody;
	}
	public void setMessageBody(String messageBody) {
		this.messageBody = messageBody;
	}
	public String getProcessor() {
		return processor;
	}
	public void setProcessor(String processor) {
		this.processor = processor;
	}
	public String getProtocolId() {
		return protocolId;
	}
	public void setProtocolId(String protocolId) {
		this.protocolId = protocolId;
	}
	public String getUsedTreadPool() {
		return usedTreadPool;
	}
	public void setUsedTreadPool(String usedTreadPool) {
		this.usedTreadPool = usedTreadPool;
	}
	public Map<String, String> getPropertyMap() {
		return propertyMap;
	}
	public void setPropertyMap(Map<String, String> propertyMap) {
		this.propertyMap = propertyMap;
	}

}
