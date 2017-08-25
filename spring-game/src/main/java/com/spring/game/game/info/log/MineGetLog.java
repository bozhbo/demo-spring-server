package com.snail.webgame.game.info.log;

import java.sql.Timestamp;

import com.snail.webgame.engine.common.to.BaseTO;

/**
 * 矿收益日志
 * @author zenggang
 */
public class MineGetLog extends BaseTO {

	private String account;// 通行证帐号 (大写)
	private String roleName;// 角色名
	private String eventId;// 异动途径ID 对应 GameAction行为Id
	private String itemNo;// 物品ID
	private int itemNum;// 物品数量
	private byte state;// 异动状态 失败:0，成功:1
	private Timestamp time;// 日志时间

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public String getItemNo() {
		return itemNo;
	}

	public void setItemNo(String itemNo) {
		this.itemNo = itemNo;
	}

	public int getItemNum() {
		return itemNum;
	}

	public void setItemNum(int itemNum) {
		this.itemNum = itemNum;
	}

	public byte getState() {
		return state;
	}

	public void setState(byte state) {
		this.state = state;
	}

	public Timestamp getTime() {
		return time;
	}

	public void setTime(Timestamp time) {
		this.time = time;
	}

	@Override
	public byte getSaveMode() {
		return ONLINE;
	}

}
