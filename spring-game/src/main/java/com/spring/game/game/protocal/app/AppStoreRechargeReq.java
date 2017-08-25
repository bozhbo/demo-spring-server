package com.snail.webgame.game.protocal.app;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

/**
 * appstore充值成功后的请求
 * 
 * @author shenggm
 * @since 2013年12月13日15:49:45
 * @version V1.0.0
 */
public class AppStoreRechargeReq extends MessageBody {
	private String pid;// 卡类型
	private int amount;// 数量
	private String transactionData;// 订单号,就是苹果回调的json串
	private String transactionIdentifier;

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.addString("pid", "flashCode", 0);
		ps.add("amount", 0);
		ps.addString("transactionData", "flashCode", 0);
		ps.addString("transactionIdentifier", "flashCode", 0);
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public String getTransactionData() {
		return transactionData;
	}

	public void setTransactionData(String transactionData) {
		this.transactionData = transactionData;
	}

	public String getTransactionIdentifier() {
		return transactionIdentifier;
	}

	public void setTransactionIdentifier(String transactionIdentifier) {
		this.transactionIdentifier = transactionIdentifier;
	}

	@Override
	public String toString() {

		StringBuilder builder = new StringBuilder();
		builder.append("AppStoreRechargeReq").append(" [");
		builder.append("pid=").append(pid).append(", ");
		builder.append("amount=").append(amount).append(", ");
		builder.append("transactionData=").append(transactionData).append("]");
		return builder.toString();
	}

}
