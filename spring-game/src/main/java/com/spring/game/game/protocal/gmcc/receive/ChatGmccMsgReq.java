package com.snail.webgame.game.protocal.gmcc.receive;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

/**
 * 游戏客户端收到GMCC主动发来的消息
 */
public class ChatGmccMsgReq extends MessageBody {
	private int flag;// 消息标志 1-客户端初始化回复 2-聊天消息 3-会话结束GM让玩家投票 4-GM主动联系玩家
	private int gmId;// GM Id
	private String gmNickname;// GM Name
	private String content;// 消息内容

	protected void setSequnce(ProtocolSequence ps) {
		ps.add("flag", 0);
		ps.add("gmId", 0);
		ps.addString("gmNickname", "flashCode", 0);
		ps.addString("content", "flashCode", 0);
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public int getGmId() {
		return gmId;
	}

	public void setGmId(int gmId) {
		this.gmId = gmId;
	}

	public String getGmNickname() {
		return gmNickname;
	}

	public void setGmNickname(String gmNickname) {
		this.gmNickname = gmNickname;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
