package com.snail.webgame.game.protocal.appellation.entity;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class ResetTitleInfosResp extends MessageBody{
	private int result;
	private int flag; // 0 - 全部刷新  1 - 单个刷新
	private int title; // 当前使用的称号
	private String titles; //chenghao.xml的No:time;chenghao.xml的No:time;
	private int fightValue;

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("result", 0);
		ps.add("flag", 0);
		ps.add("title", 0);
		ps.addString("titles", "flashCode", 0);
		ps.add("fightValue", 0);
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public int getTitle() {
		return title;
	}

	public void setTitle(int title) {
		this.title = title;
	}

	public String getTitles() {
		return titles;
	}

	public void setTitles(String titles) {
		this.titles = titles;
	}

	public int getFightValue() {
		return fightValue;
	}

	public void setFightValue(int fightValue) {
		this.fightValue = fightValue;
	}

}
