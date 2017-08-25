package org.epilot.ccf.common;

public class ProcotolsMapping {
	String protocolId;
	String messageBody;
	String processor;
	String usedTreadPool;
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
}
