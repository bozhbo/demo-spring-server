package com.snail.webgame.game.protocal.scene.updatePoint;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;


/**
 * 场景AI1移动,AI2更新AI1坐标
 * @author hongfm
 *
 */
public class OtherUpdatePointResp extends MessageBody {

	private int AIType;//1-角色,2-NPC
	private long changeAIId;//AI编号(角色时roleId,NPC时npcNO)
	private float curPointX;//移动AI当前X
	private float curPointY;//移动AI当前Y
	private float curPointZ;//移动AI当前Y
	
	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("AIType", 0);
		ps.add("changeAIId", 0);
		ps.add("curPointX", 0);
		ps.add("curPointY", 0);
		ps.add("curPointZ", 0);
	}

	public int getAIType() {
		return AIType;
	}

	public void setAIType(int aIType) {
		AIType = aIType;
	}

	public long getChangeAIId() {
		return changeAIId;
	}

	public void setChangeAIId(long changeAIId) {
		this.changeAIId = changeAIId;
	}

	public float getCurPointX() {
		return curPointX;
	}

	public void setCurPointX(float curPointX) {
		this.curPointX = curPointX;
	}

	public float getCurPointY() {
		return curPointY;
	}

	public void setCurPointY(float curPointY) {
		this.curPointY = curPointY;
	}

	public float getCurPointZ() {
		return curPointZ;
	}

	public void setCurPointZ(float curPointZ) {
		this.curPointZ = curPointZ;
	}

	
}
