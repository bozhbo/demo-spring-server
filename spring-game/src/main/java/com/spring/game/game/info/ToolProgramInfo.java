package com.snail.webgame.game.info;

import java.sql.Timestamp;

import com.snail.webgame.engine.common.to.BaseTO;

public class ToolProgramInfo extends BaseTO {
	
	private String sid;
	private String person;
	private String xml;
	private Timestamp timestamp;

	@Override
	public byte getSaveMode() {
		// TODO Auto-generated method stub
		return 0;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public String getPerson() {
		return person;
	}

	public void setPerson(String person) {
		this.person = person;
	}

	public String getXml() {
		return xml;
	}

	public void setXml(String xml) {
		this.xml = xml;
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

}
