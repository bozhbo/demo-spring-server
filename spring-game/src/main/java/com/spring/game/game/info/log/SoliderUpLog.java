package com.snail.webgame.game.info.log;

import java.sql.Timestamp;

import com.snail.webgame.engine.common.to.BaseTO;

/**
 * 
 * 类介绍:兵种升级日志
 *
 * @author hongfm
 * @2015年6月4日
 */
public class SoliderUpLog extends BaseTO {

	private String account;// 通行证帐号 (大写)
	private String roleName;// 角色名
	private int roleId;//角色Id
	private byte soliderType;//兵种类型
	private byte isUp;//升级成功与否
	private int beforeLv;
	private int atterLv;
	private int useMoney;//消耗银子
	private int useItemNo;//消耗道具
	private int useItemNum;//消耗道具数量
	private Timestamp time;
	private String content;
	
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

	public byte getSoliderType() {
		return soliderType;
	}

	public void setSoliderType(byte soliderType) {
		this.soliderType = soliderType;
	}

	public byte getIsUp() {
		return isUp;
	}

	public void setIsUp(byte isUp) {
		this.isUp = isUp;
	}

	public int getBeforeLv() {
		return beforeLv;
	}

	public void setBeforeLv(int beforeLv) {
		this.beforeLv = beforeLv;
	}

	public int getAtterLv() {
		return atterLv;
	}

	public void setAtterLv(int atterLv) {
		this.atterLv = atterLv;
	}

	public Timestamp getTime() {
		return time;
	}

	public void setTime(Timestamp time) {
		this.time = time;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	
	public int getUseMoney() {
		return useMoney;
	}

	public void setUseMoney(int useMoney) {
		this.useMoney = useMoney;
	}

	public int getUseItemNo() {
		return useItemNo;
	}

	public void setUseItemNo(int useItemNo) {
		this.useItemNo = useItemNo;
	}

	public int getUseItemNum() {
		return useItemNum;
	}

	public void setUseItemNum(int useItemNum) {
		this.useItemNum = useItemNum;
	}

	@Override
	public byte getSaveMode() {
		return ONLINE;
	}

}
