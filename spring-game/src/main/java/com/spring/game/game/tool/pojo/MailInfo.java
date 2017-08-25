package com.snail.webgame.game.tool.pojo;

public class MailInfo {

	private String topic; //主题
	private String content; // 内容
	private String receiver; //接收人角色ID
	private byte sendType; // 发送类别  1-发送全部玩家 0-发送给receiver
	private String attachment; //附件

	
	public MailInfo() {
		
	}
	
	public MailInfo(String topic, String content, String receiver, byte sendType, String attachment) {
		this.topic = topic;
		this.content = content;
		this.receiver = receiver;
		this.sendType = sendType;
		this.attachment = attachment;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public byte getSendType() {
		return sendType;
	}

	public void setSendType(byte sendType) {
		this.sendType = sendType;
	}

	public String getAttachment() {
		return attachment;
	}

	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}

}
