package com.snail.webgame.game.protocal.gmcc.send;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

/**
 * 游戏客户端发送给GMCC的消息
 */
public class SendGmccMsgReq extends MessageBody {
	private byte flag;// 0-初始化 1-申请GM 2-聊天 3-评价
	private String content;
	private String addContent;

	protected void setSequnce(ProtocolSequence ps) {
		ps.add("flag", 0);
		ps.addString("content", "flashCode", 0);
		ps.addString("addContent", "flashCode", 0);
	}

	public byte getFlag() {
		return flag;
	}

	public void setFlag(byte flag) {
		this.flag = flag;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getAddContent() {
		return addContent;
	}

	public void setAddContent(String addContent) {
		this.addContent = addContent;
	}
}
