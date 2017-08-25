package com.snail.webgame.game.charge.order;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

/**
 * 
 * @author nijp
 *
 */
public class SaveChargeOrderReq  extends MessageBody {
	
	private String itemId;// 商品Id
	private String orderStr;// 订单号
	private String notifyUrl;// vivo渠道专用 回调地址
	private int chargePrice;// vivo渠道专用 充钱价格

	protected void setSequnce(ProtocolSequence ps) {
		ps.addString("itemId", "flashCode", 0);
		ps.addString("orderStr", "flashCode", 0);
		ps.addString("notifyUrl", "flashCode", 0);
		ps.add("chargePrice", 0);
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getOrderStr() {
		return orderStr;
	}

	public void setOrderStr(String orderStr) {
		this.orderStr = orderStr;
	}

	public String getNotifyUrl() {
		return notifyUrl;
	}

	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}

	public int getChargePrice() {
		return chargePrice;
	}

	public void setChargePrice(int chargePrice) {
		this.chargePrice = chargePrice;
	}

}
