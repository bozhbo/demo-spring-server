package com.snail.webgame.game.info;

public class ChargeAccountInfo {
	
	private int accountId;//用户计费Id
	private String chargeAccount;//用户账号
	private String validate;//用户验证串
	private int issuerID;//运营商id
	private int gmLevel;//
	private String hmacStr;//一次性串
	private String accInfo;//角色防沉迷信息
	private String qihooUserId;// userId
	private String qihooToken;// token
	private long addTime;//账号验证时间(防止角色下线一分内重新登录验证账号但是没有进入游戏。一分钟后时间到达，数据清除。这时角色进入游戏就会过滤)
	
	public int getAccountId() {
		return accountId;
	}
	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}
	
	public String getChargeAccount() {
		return chargeAccount;
	}
	public void setChargeAccount(String chargeAccount) {
		this.chargeAccount = chargeAccount;
	}
	public String getValidate() {
		return validate;
	}
	public void setValidate(String validate) {
		this.validate = validate;
	}
	public int getIssuerID() {
		return issuerID;
	}
	public void setIssuerID(int issuerID) {
		this.issuerID = issuerID;
	}
	public int getGmLevel() {
		return gmLevel;
	}
	public void setGmLevel(int gmLevel) {
		this.gmLevel = gmLevel;
	}
	public String getHmacStr() {
		return hmacStr;
	}
	public void setHmacStr(String hmacStr) {
		this.hmacStr = hmacStr;
	}
	public String getAccInfo() {
		return accInfo;
	}
	public void setAccInfo(String accInfo) {
		this.accInfo = accInfo;
	}
	public String getQihooUserId() {
		return qihooUserId;
	}
	public void setQihooUserId(String qihooUserId) {
		this.qihooUserId = qihooUserId;
	}
	public String getQihooToken() {
		return qihooToken;
	}
	public void setQihooToken(String qihooToken) {
		this.qihooToken = qihooToken;
	}
	public long getAddTime() {
		return addTime;
	}
	public void setAddTime(long addTime) {
		this.addTime = addTime;
	}

}
