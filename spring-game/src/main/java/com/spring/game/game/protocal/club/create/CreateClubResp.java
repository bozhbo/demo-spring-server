package com.snail.webgame.game.protocal.club.create;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class CreateClubResp extends MessageBody {
	private int result;
	private int id; // 公会的主键ID
	private byte sourceType;// 1:银子 2:金子
	private int sourceChange;// 资源变动数,正值为增加,负值为减少
	private long time;// 当result == 2的时候 提示给客户端显示的剩余公会创建时间

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("result", 0);
		ps.add("id", 0);
		ps.add("sourceType", 0);
		ps.add("sourceChange", 0);
		ps.add("time", 0);
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

}
