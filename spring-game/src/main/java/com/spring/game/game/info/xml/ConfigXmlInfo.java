package com.snail.webgame.game.info.xml;

import com.snail.webgame.engine.common.to.BaseTO;

public class ConfigXmlInfo extends BaseTO {

	private String xmlName; // 配置文件名称
	private String xmlLogId; // 配置文件记录ID
	private String xmlLogName; // 配置文件记录名称
	private String xmlContent; // 配置文件内容
	private String modXmlContent; // 已修改配置文件内容
	private int delFlag; // 删除标识

	public String getXmlName() {
		return xmlName;
	}

	public void setXmlName(String xmlName) {
		this.xmlName = xmlName;
	}

	public String getXmlLogId() {
		return xmlLogId;
	}

	public void setXmlLogId(String xmlLogId) {
		this.xmlLogId = xmlLogId;
	}

	public String getXmlLogName() {
		return xmlLogName;
	}

	public void setXmlLogName(String xmlLogName) {
		this.xmlLogName = xmlLogName;
	}

	public String getXmlContent() {
		return xmlContent;
	}

	public void setXmlContent(String xmlContent) {
		this.xmlContent = xmlContent;
	}

	public String getModXmlContent() {
		return modXmlContent;
	}

	public void setModXmlContent(String modXmlContent) {
		this.modXmlContent = modXmlContent;
	}

	public int getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(int delFlag) {
		this.delFlag = delFlag;
	}

	@Override
	public byte getSaveMode() {
		return ONLINE;
	}
}
