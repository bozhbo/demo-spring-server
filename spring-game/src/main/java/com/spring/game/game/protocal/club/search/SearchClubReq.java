package com.snail.webgame.game.protocal.club.search;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class SearchClubReq extends MessageBody {
	private int flag; // 0 - 请求列表 1 - 搜索
	private String name; // 如果是数字就是公会ID 否则就是公会名

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("flag", 0);
		ps.addString("name", "flashCode", 0);
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
