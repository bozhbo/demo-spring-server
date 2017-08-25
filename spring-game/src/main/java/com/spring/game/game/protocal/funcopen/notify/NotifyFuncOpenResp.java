package com.snail.webgame.game.protocal.funcopen.notify;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class NotifyFuncOpenResp extends MessageBody {

	private String newFuncOpenStr;// 有新的功能开启  格式：功能编号no,功能编号no
	
	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.addString("newFuncOpenStr", "flashCode", 0);
	}

	public String getNewFuncOpenStr() {
		return newFuncOpenStr;
	}

	public void setNewFuncOpenStr(String newFuncOpenStr) {
		this.newFuncOpenStr = newFuncOpenStr;
	}

}
