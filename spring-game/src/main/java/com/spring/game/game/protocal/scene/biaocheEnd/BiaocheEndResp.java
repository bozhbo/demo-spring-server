package com.snail.webgame.game.protocal.scene.biaocheEnd;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;


/**
 * 压镖结束
 * @author hongfm
 *
 */
public class BiaocheEndResp extends MessageBody {

	private int result;
	private String roleName;//护送押镖的好友名字
	private int silverNum;//获得的银子数量
	
	@Override
	protected void setSequnce(ProtocolSequence ps)
	{
		ps.add("result", 0);
		ps.addString("roleName", "flashCode", 0);
		ps.add("silverNum", 0);
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public int getSilverNum() {
		return silverNum;
	}

	public void setSilverNum(int silverNum) {
		this.silverNum = silverNum;
	}
	
}
