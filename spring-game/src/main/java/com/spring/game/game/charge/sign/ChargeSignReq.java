package com.snail.webgame.game.charge.sign;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

/**
 * 
 * @author nijp
 *
 */
public class ChargeSignReq extends MessageBody {
	
	private String appId;// 游戏id
	private String productId;// 商品Id
	private String orderId;// 订单号
	private String productSubject;// 订单标题
	private String productBody;// 道具说明
	private int buyAmount;// 购买道具的数量
	private String productPerPrice;// 道具单价
	private String createTime;// 创建时间戳
	private String uid;// 
	
	protected void setSequnce(ProtocolSequence ps) {
		ps.addString("appId", "flashCode", 0);
		ps.addString("productId", "flashCode", 0);
		ps.addString("orderId", "flashCode", 0);
		ps.addString("productSubject", "flashCode", 0);
		ps.addString("productBody", "flashCode", 0);
		ps.add("buyAmount", 0);
		ps.addString("productPerPrice", "flashCode", 0);
		ps.addString("createTime", "flashCode", 0);
		ps.addString("uid", "flashCode", 0);
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getProductSubject() {
		return productSubject;
	}

	public void setProductSubject(String productSubject) {
		this.productSubject = productSubject;
	}

	public String getProductBody() {
		return productBody;
	}

	public void setProductBody(String productBody) {
		this.productBody = productBody;
	}

	public int getBuyAmount() {
		return buyAmount;
	}

	public void setBuyAmount(int buyAmount) {
		this.buyAmount = buyAmount;
	}

	public String getProductPerPrice() {
		return productPerPrice;
	}

	public void setProductPerPrice(String productPerPrice) {
		this.productPerPrice = productPerPrice;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

}
