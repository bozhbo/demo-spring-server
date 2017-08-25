package com.snail.webgame.game.protocal.equip.query;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

/**
 * 装备查询
 * @author zhangyq
 *
 */
public class QueryBagEquipReq extends MessageBody {

	private String idStr;//需查询装备的ID ","分割

	protected void setSequnce(ProtocolSequence ps) {
		ps.addString("idStr", "flashCode", 0);
	}

	public String getIdStr() {
		return idStr;
	}

	public void setIdStr(String idStr) {
		this.idStr = idStr;
	}
}