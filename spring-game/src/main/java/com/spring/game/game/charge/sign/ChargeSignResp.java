package com.snail.webgame.game.charge.sign;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

/**
 * 
 * @author nijp
 *
 */
public class ChargeSignResp extends MessageBody {
	private int result;// 结果
	private String orderId;// 订单号
	private String productId;// 商品id
	private String sign;

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("result", 0);
		ps.addString("orderId", "flashCode", 0);
		ps.addString("productId", "flashCode", 0);
		ps.addString("sign", "flashCode", 0);
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

}
