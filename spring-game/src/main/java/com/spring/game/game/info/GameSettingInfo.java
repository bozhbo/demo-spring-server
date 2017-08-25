package com.snail.webgame.game.info;

import com.snail.webgame.engine.common.to.BaseTO;

public class GameSettingInfo extends BaseTO {

	private String key;
	private String value;
	private String comment;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	@Override
	public byte getSaveMode() {
		return 0;
	}

}
