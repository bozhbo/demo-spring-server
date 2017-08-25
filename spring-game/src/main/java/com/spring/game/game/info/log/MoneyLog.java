package com.snail.webgame.game.info.log;

import java.sql.Timestamp;

import com.snail.webgame.engine.common.to.BaseTO;

/**
 * 用户资源变动日志
 * @author zenggang
 */
public class MoneyLog extends BaseTO {

	private String account;// 通行证帐号 (大写)
	private String roleName;// 角色名
	private Timestamp createTime;// 日志时间
	private int moneyType;// 币种类型 ConditionType
	private String eventId;// 异动途径ID 对应 GameAction行为Id
	private int eventType;// 获得0,失去1
	private String itemId;// 物品ID
	private int count;// 物品购买数量
	private int money;// 异动金钱
	private String recvAccount;// 记录被赠送帐号
	private String recvRoleName;// 记录被赠送角色
	private String sceneId;// 场景ID
	private String guildId;// 公会ID
	private String state;// 异动状态 失败:0，成功:1
	private int roleLevel;// 当前角色等级
	private int totalLogtime;// 累计登录时长单位：分钟
	private int before;// 异动前金钱存量
	private int after;// 异动后金钱存量
	private int tradeId;// 交易ID
	private String tradeFlag;// 是否交易 不是:0，是:1
	private String comment;// 备注

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

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public int getMoneyType() {
		return moneyType;
	}

	public void setMoneyType(int moneyType) {
		this.moneyType = moneyType;
	}

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public int getEventType() {
		return eventType;
	}

	public void setEventType(int eventType) {
		this.eventType = eventType;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
	}

	public String getRecvAccount() {
		return recvAccount;
	}

	public void setRecvAccount(String recvAccount) {
		this.recvAccount = recvAccount;
	}

	public String getRecvRoleName() {
		return recvRoleName;
	}

	public void setRecvRoleName(String recvRoleName) {
		this.recvRoleName = recvRoleName;
	}

	public String getSceneId() {
		return sceneId;
	}

	public void setSceneId(String sceneId) {
		this.sceneId = sceneId;
	}

	public String getGuildId() {
		return guildId;
	}

	public void setGuildId(String guildId) {
		this.guildId = guildId;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public int getRoleLevel() {
		return roleLevel;
	}

	public void setRoleLevel(int roleLevel) {
		this.roleLevel = roleLevel;
	}

	public int getTotalLogtime() {
		return totalLogtime;
	}

	public void setTotalLogtime(int totalLogtime) {
		this.totalLogtime = totalLogtime;
	}

	public int getBefore() {
		return before;
	}

	public void setBefore(int before) {
		this.before = before;
	}

	public int getAfter() {
		return after;
	}

	public void setAfter(int after) {
		this.after = after;
	}

	public int getTradeId() {
		return tradeId;
	}

	public void setTradeId(int tradeId) {
		this.tradeId = tradeId;
	}

	public String getTradeFlag() {
		return tradeFlag;
	}

	public void setTradeFlag(String tradeFlag) {
		this.tradeFlag = tradeFlag;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	@Override
	public byte getSaveMode() {
		return ONLINE;
	}
}
