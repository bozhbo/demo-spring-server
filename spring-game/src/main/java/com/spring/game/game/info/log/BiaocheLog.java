package com.snail.webgame.game.info.log;

import java.sql.Timestamp;

import com.snail.webgame.engine.common.to.BaseTO;

/**
 * 
 * 类介绍:镖车日志
 *
 * @author hongfm
 * @2015年6月4日
 */
public class BiaocheLog extends BaseTO {

	private String account;// 通行证帐号 (大写)
	private String roleName;// 角色名
	private int roleId;//角色Id
	private int action;//1-刷新镖车,2-押镖,3-劫镖 4-护镖
	private int num;//次数
	private int silverChange;//获得银子
	private int lostSilver;//失去银子
	private int usedCoin;//金子消耗
	private byte biaocheType;//镖车类型
	private Timestamp time;
	private String content;
	private byte result;//1-成功,0-失败

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



	public int getAction() {
		return action;
	}



	public void setAction(int action) {
		this.action = action;
	}
	



	public int getNum() {
		return num;
	}



	public void setNum(int num) {
		this.num = num;
	}
	



	public int getSilverChange() {
		return silverChange;
	}



	public void setSilverChange(int silverChange) {
		this.silverChange = silverChange;
	}



	public String getContent() {
		return content;
	}



	public void setContent(String content) {
		this.content = content;
	}



	public Timestamp getTime() {
		return time;
	}



	public void setTime(Timestamp time) {
		this.time = time;
	}



	public byte getResult() {
		return result;
	}



	public void setResult(byte result) {
		this.result = result;
	}



	public int getLostSilver() {
		return lostSilver;
	}



	public void setLostSilver(int lostSilver) {
		this.lostSilver = lostSilver;
	}



	public int getUsedCoin() {
		return usedCoin;
	}



	public void setUsedCoin(int usedCoin) {
		this.usedCoin = usedCoin;
	}



	public byte getBiaocheType() {
		return biaocheType;
	}



	public void setBiaocheType(byte biaocheType) {
		this.biaocheType = biaocheType;
	}



	@Override
	public byte getSaveMode() {
		return ONLINE;
	}

}
