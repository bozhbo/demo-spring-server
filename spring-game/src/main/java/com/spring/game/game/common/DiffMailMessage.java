package com.snail.webgame.game.common;

import java.util.List;

public class DiffMailMessage extends TimeMessage {

	private List<DiffMail> diffMailList;

	public DiffMailMessage(ETimeMessageType type, List<DiffMail> diffMailList) {
		super(type);
		this.diffMailList = diffMailList;
	}

	public List<DiffMail> getDiffMailList() {
		return diffMailList;
	}

	public void setDiffMailList(List<DiffMail> diffMailList) {
		this.diffMailList = diffMailList;
	}

}
