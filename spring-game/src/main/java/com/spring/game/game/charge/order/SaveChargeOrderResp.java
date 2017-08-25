package com.snail.webgame.game.charge.order;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

/**
 *保存订单号的响应
 * 
 * @author nijp
 *
 */
public class SaveChargeOrderResp extends MessageBody {
	private int result;			// 结果
	private String itemId; 		// 商品Id
	private String orderIdStr; 	// 订单号
	
	private String vivoOrder;// 
	private String vivoSignature;

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("result", 0);
		ps.addString("itemId", "flashCode", 0);
		ps.addString("orderIdStr", "flashCode", 0);
		
		ps.addString("vivoOrder", "flashCode", 0);
		ps.addString("vivoSignature", "flashCode", 0);
		
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getOrderIdStr() {
		return orderIdStr;
	}

	public void setOrderIdStr(String orderIdStr) {
		this.orderIdStr = orderIdStr;
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public String getVivoOrder() {
		return vivoOrder;
	}

	public void setVivoOrder(String vivoOrder) {
		this.vivoOrder = vivoOrder;
	}

	public String getVivoSignature() {
		return vivoSignature;
	}

	public void setVivoSignature(String vivoSignature) {
		this.vivoSignature = vivoSignature;
	}

}
