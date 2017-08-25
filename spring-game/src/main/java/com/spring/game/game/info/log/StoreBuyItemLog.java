package com.snail.webgame.game.info.log;

import java.sql.Timestamp;

import com.snail.webgame.engine.common.to.BaseTO;

/**
 * 
 * 类介绍:商店物品购买日志
 *
 * @author hongfm
 * @2015年6月4日
 */
public class StoreBuyItemLog extends BaseTO {

	private String account;// 通行证帐号 (大写)
	private String roleName;// 角色名
	private int roleId;//角色Id
	private byte storeType;//商店类型
	private int itemNo;//购买物品编号
	private int itemNum;//购买物品数量s
	private String sourceType;//消耗货币类型
	private int sourceNum;//消耗货币数量
	private Timestamp createTime;// 日志时间
	

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


	public int getRoleId() {
		return roleId;
	}


	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}


	public Timestamp getCreateTime() {
		return createTime;
	}


	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}


	public byte getStoreType() {
		return storeType;
	}


	public void setStoreType(byte storeType) {
		this.storeType = storeType;
	}


	public int getItemNo() {
		return itemNo;
	}


	public void setItemNo(int itemNo) {
		this.itemNo = itemNo;
	}


	public int getItemNum() {
		return itemNum;
	}


	public void setItemNum(int itemNum) {
		this.itemNum = itemNum;
	}


	public String getSourceType() {
		return sourceType;
	}


	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}


	public int getSourceNum() {
		return sourceNum;
	}


	public void setSourceNum(int sourceNum) {
		this.sourceNum = sourceNum;
	}


	@Override
	public byte getSaveMode() {
		return ONLINE;
	}

}
