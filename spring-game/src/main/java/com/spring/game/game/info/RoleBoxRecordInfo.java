package com.snail.webgame.game.info;

import java.sql.Timestamp;

import com.snail.webgame.engine.common.to.BaseTO;

/**
 * 玩家礼包购买记录
 * 
 * @author nijp
 *
 */
public class RoleBoxRecordInfo extends BaseTO {

	private int boxId;// 礼包id
	private int boxVersion;// 礼包版本号，用于玩家购买次数清零
	private int buyNum;// 购买次数
	private Timestamp buyTime;// 购买时间
	
	private int roleId;
	
	public int getBoxId() {
		return boxId;
	}

	public void setBoxId(int boxId) {
		this.boxId = boxId;
	}

	public int getBoxVersion() {
		return boxVersion;
	}

	public void setBoxVersion(int boxVersion) {
		this.boxVersion = boxVersion;
	}

	public int getBuyNum() {
		return buyNum;
	}

	public void setBuyNum(int buyNum) {
		this.buyNum = buyNum;
	}

	public Timestamp getBuyTime() {
		return buyTime;
	}

	public void setBuyTime(Timestamp buyTime) {
		this.buyTime = buyTime;
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	@Override
	public byte getSaveMode() {
		return 0;
	}
	
}
