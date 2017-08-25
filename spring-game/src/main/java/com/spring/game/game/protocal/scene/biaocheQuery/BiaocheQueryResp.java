package com.snail.webgame.game.protocal.scene.biaocheQuery;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;


/**
 * 查询镖车
 * @author hongfm
 *
 */
public class BiaocheQueryResp extends MessageBody {

	private int result;
	private byte biaocheRefNum;//镖车已刷新次数
	private byte biaocheFreeRefNum;//剩余镖车免费刷新次数
	private byte biaocheType;//当前镖车类型
	private byte leftFreeYabiaoNum;//剩余免费压镖次数
	private byte leftJiebiaoNum;//剩余截镖次数
	
	@Override
	protected void setSequnce(ProtocolSequence ps)
	{
		ps.add("result", 0);
		ps.add("biaocheRefNum", 0);
		ps.add("biaocheFreeRefNum", 0);
		ps.add("biaocheType", 0);
		ps.add("leftFreeYabiaoNum", 0);
		ps.add("leftJiebiaoNum", 0);
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
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

	public byte getBiaocheType() {
		return biaocheType;
	}

	public void setBiaocheType(byte biaocheType) {
		this.biaocheType = biaocheType;
	}
	

	public byte getLeftFreeYabiaoNum() {
		return leftFreeYabiaoNum;
	}

	public void setLeftFreeYabiaoNum(byte leftFreeYabiaoNum) {
		this.leftFreeYabiaoNum = leftFreeYabiaoNum;
	}

	public byte getLeftJiebiaoNum() {
		return leftJiebiaoNum;
	}

	public void setLeftJiebiaoNum(byte leftJiebiaoNum) {
		this.leftJiebiaoNum = leftJiebiaoNum;
	}
	
}
