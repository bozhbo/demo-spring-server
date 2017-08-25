package com.snail.webgame.game.protocal.scene.biaocheRef;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;


/**
 * 镖车刷新
 * @author hongfm
 *
 */
public class BiaocheRefResp extends MessageBody {

	private int result;
	private byte biaocheType;//镖车类型
	private byte sourceType;//1:银子	2:金子
	private int sourceChange;//资源变动数,正值为增加,负值为减少
	private byte biaocheRefNum;//镖车已刷新次数
	private byte biaocheFreeRefNum;//剩余镖车免费刷新次数
	
	@Override
	protected void setSequnce(ProtocolSequence ps)
	{
		ps.add("result", 0);
		ps.add("biaocheType", 0);
		ps.add("sourceType", 0);
		ps.add("sourceChange", 0);
		ps.add("biaocheRefNum", 0);
		ps.add("biaocheFreeRefNum", 0);
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public byte getBiaocheType() {
		return biaocheType;
	}

	public void setBiaocheType(byte biaocheType) {
		this.biaocheType = biaocheType;
	}

	public byte getSourceType() {
		return sourceType;
	}

	public void setSourceType(byte sourceType) {
		this.sourceType = sourceType;
	}

	public int getSourceChange() {
		return sourceChange;
	}

	public void setSourceChange(int sourceChange) {
		this.sourceChange = sourceChange;
	}

	public byte getBiaocheRefNum() {
		return biaocheRefNum;
	}

	public void setBiaocheRefNum(byte biaocheRefNum) {
		this.biaocheRefNum = biaocheRefNum;
	}

	public byte getBiaocheFreeRefNum() {
		return biaocheFreeRefNum;
	}

	public void setBiaocheFreeRefNum(byte biaocheFreeRefNum) {
		this.biaocheFreeRefNum = biaocheFreeRefNum;
	}
	
}
