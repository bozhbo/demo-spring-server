package com.snail.webgame.game.protocal.app.common;

import java.util.Date;

import com.snail.webgame.engine.common.to.BaseTO;

/**
 * appStore充值异常记录
 * 
 * @author wangxf
 * @date 2014-3-5
 * 
 */
public class AppStoreExInfo extends BaseTO {
	private int sAgentId; // 平台ID
	private String sAgentPwd; // 平台密码
	private String sAgentOrderId; // 平台充值订单 如果是AppStore方式，则是苹果返回的 支付成功的验证串
	private String sCardType; // 卡类型 ，如果是AppStore方式 则传类似这样的苹果注册的一个商品pid
								// 如com.snailgames.CrazyFish.gold30
	private int amount; // 数量
	private int totalValue; // 总值(人民币数) 计费这边配置的 sCardType 对应的单价*amcount
	private String sUserName; // 蜗牛通行证，传入时需要编码
	private int sAccountTypeID; // 账户类型(0-中心 , 1- 分区)
	private String sAreaID; // 充入分区ＩＤ（如果有赠送道具，也赠入此分区ＩＤ）
	private String sImprestAccountIP; // 充值人IP
	private String sVerifyStr; // 校验串，前面参数依次串接再加上ＳＥＥＤ，最后ＭＤ５，再转大写．
	private long roleId; // 角色id
	private String account;	// 蜗牛通行证
	private Date createTime;	// 创建时间
	private String resultStr;	// 计费返回的结果
	private int errorCode;		// 计费返回的错误码
	private int isRetransfer;	// 是否重调标识，0不需要重调 1需要重调
	private Date retransferTime;	// 重调时间
	private String operateUser;		// 重调人

	@Override
	public byte getSaveMode() {
		// TODO Auto-generated method stub
		return 0;
	}

	public long getRoleId() {
		return roleId;
	}

	public void setRoleId(long roleId) {
		this.roleId = roleId;
	}

	public int getsAgentId() {
		return sAgentId;
	}

	public void setsAgentId(int sAgentId) {
		this.sAgentId = sAgentId;
	}

	public String getsAgentPwd() {
		return sAgentPwd;
	}

	public void setsAgentPwd(String sAgentPwd) {
		this.sAgentPwd = sAgentPwd;
	}

	public String getsAgentOrderId() {
		return sAgentOrderId;
	}

	public void setsAgentOrderId(String sAgentOrderId) {
		this.sAgentOrderId = sAgentOrderId;
	}

	public String getsCardType() {
		return sCardType;
	}

	public void setsCardType(String sCardType) {
		this.sCardType = sCardType;
	}

	public int getTotalValue() {
		return totalValue;
	}

	public void setTotalValue(int totalValue) {
		this.totalValue = totalValue;
	}

	public String getsUserName() {
		return sUserName;
	}

	public void setsUserName(String sUserName) {
		this.sUserName = sUserName;
	}

	public int getsAccountTypeID() {
		return sAccountTypeID;
	}

	public void setsAccountTypeID(int sAccountTypeID) {
		this.sAccountTypeID = sAccountTypeID;
	}

	public String getsAreaID() {
		return sAreaID;
	}

	public void setsAreaID(String sAreaID) {
		this.sAreaID = sAreaID;
	}

	public String getsImprestAccountIP() {
		return sImprestAccountIP;
	}

	public void setsImprestAccountIP(String sImprestAccountIP) {
		this.sImprestAccountIP = sImprestAccountIP;
	}

	public String getsVerifyStr() {
		return sVerifyStr;
	}

	public void setsVerifyStr(String sVerifyStr) {
		this.sVerifyStr = sVerifyStr;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getResultStr() {
		return resultStr;
	}

	public void setResultStr(String resultStr) {
		this.resultStr = resultStr;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public int getIsRetransfer() {
		return isRetransfer;
	}

	public void setIsRetransfer(int isRetransfer) {
		this.isRetransfer = isRetransfer;
	}

	public Date getRetransferTime() {
		return retransferTime;
	}

	public void setRetransferTime(Date retransferTime) {
		this.retransferTime = retransferTime;
	}

	public String getOperateUser() {
		return operateUser;
	}

	public void setOperateUser(String operateUser) {
		this.operateUser = operateUser;
	}

}
