package com.snail.webgame.game.protocal.appellation.get;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;
/**
 * 获得称谓消息体
 * @author nijy
 *
 */
public class GetAppellationResp extends MessageBody{
	private int result;
	// 1-获得 2-过期
	private byte chenghaoType;
	//称谓id
	private int id;
	//称谓到点时间  -如果是过期称谓,无视这个字段.如果是无时间限称谓.则这个值是-1
	private int sec;
	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("result", 0);
		ps.add("chenghaoType", 0);
		ps.add("id", 0);
		ps.add("sec", 0);
	}
	public int getResult() {
		return result;
	}
	public void setResult(int result) {
		this.result = result;
	}
	public byte getChenghaoType() {
		return chenghaoType;
	}
	public void setChenghaoType(byte chenghaoType) {
		this.chenghaoType = chenghaoType;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getSec() {
		return sec;
	}
	public void setSec(int sec) {
		this.sec = sec;
	}
	
}
