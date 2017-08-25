package com.snail.webgame.game.protocal.club.join;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class JoinClubResp extends MessageBody {
	private int result;
	private int action; // 0 - 加入 1 - 退出
	private int flag; // 0 - 直接加入 1 - 发出申请
	private long time; //当result == 2的时候 提升客户端的倒计时
	private int clubId;//申请加入的公会赋值

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("result", 0);
		ps.add("action", 0);
		ps.add("flag", 0);
		ps.add("time", 0);
		ps.add("clubId", 0);
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public int getAction() {
		return action;
	}

	public void setAction(int action) {
		this.action = action;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public int getClubId() {
		return clubId;
	}

	public void setClubId(int clubId) {
		this.clubId = clubId;
	}

}
