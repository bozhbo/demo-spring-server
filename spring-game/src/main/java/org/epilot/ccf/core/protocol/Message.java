package org.epilot.ccf.core.protocol;


 public class Message {
	
	private MessageHeader header;
	private MessageBody body;
	
	public MessageBody getBody() {
		return body;
	}
	public void setBody(MessageBody body) {
		this.body = body;
	}
	public MessageHeader getHeader() {
		return header;
	}
	public void setHeader(MessageHeader header) {
		this.header = header;
	}
	
	
}
