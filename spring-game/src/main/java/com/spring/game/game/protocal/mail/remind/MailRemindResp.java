package com.snail.webgame.game.protocal.mail.remind;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class MailRemindResp extends MessageBody {
	
	private int result;
	private int flag;//1-已满 2-新邮件

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	protected void setSequnce(ProtocolSequence ps)
	{
		ps.add("result", 0);	
		ps.add("flag", 0);
	}

}
