package com.snail.webgame.game.info.log;

import java.sql.Timestamp;

import com.snail.webgame.engine.common.to.BaseTO;

/**
 * 场景统计表
 * @author zenggang
 */
public class SceneLog extends BaseTO {
	private String serial;// 序列号
	private String sceneId;// 场景id
	private Timestamp date;// 统计日期
	private int enter;// 进入次数
	private float totalTime;// 所有玩家累计停留时长 单位：小时

	public String getSerial() {
		return serial;
	}

	public void setSerial(String serial) {
		this.serial = serial;
	}

	public String getSceneId() {
		return sceneId;
	}

	public void setSceneId(String sceneId) {
		this.sceneId = sceneId;
	}

	public Timestamp getDate() {
		return date;
	}

	public void setDate(Timestamp date) {
		this.date = date;
	}

	public int getEnter() {
		return enter;
	}

	public void setEnter(int enter) {
		this.enter = enter;
	}

	public float getTotalTime() {
		return totalTime;
	}

	public void setTotalTime(float totalTime) {
		this.totalTime = totalTime;
	}

	@Override
	public byte getSaveMode() {
		return ONLINE;
	}
}
