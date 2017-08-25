package com.snail.webgame.game.protocal.quest.query;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class QuestInfoRe extends MessageBody {

	private int questProtoNo;// 任务编号
	private byte status;// 1-未完成 2-已经完成 3-已领取奖励
	private int value;// 完成值
	private byte npcTalkOrder;// 当前对话序列
	
	private byte isEffect;// 1-播特效 
	
	private int curRunQuestSeq;// 当前第几个跑环任务
	private int dailyMaxRunNum;// 每日最多跑环任务个数

	protected void setSequnce(ProtocolSequence ps) {
		ps.add("questProtoNo", 0);
		ps.add("status", 0);
		ps.add("value", 0);
		ps.add("npcTalkOrder", 0);
		ps.add("isEffect", 0);
		ps.add("curRunQuestSeq", 0);
		ps.add("dailyMaxRunNum", 0);
	}

	public int getQuestProtoNo() {
		return questProtoNo;
	}

	public void setQuestProtoNo(int questProtoNo) {
		this.questProtoNo = questProtoNo;
	}

	public byte getStatus() {
		return status;
	}

	public void setStatus(byte status) {
		this.status = status;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public byte getNpcTalkOrder() {
		return npcTalkOrder;
	}

	public void setNpcTalkOrder(byte npcTalkOrder) {
		this.npcTalkOrder = npcTalkOrder;
	}

	public byte getIsEffect() {
		return isEffect;
	}

	public void setIsEffect(byte isEffect) {
		this.isEffect = isEffect;
	}

	public int getCurRunQuestSeq() {
		return curRunQuestSeq;
	}

	public void setCurRunQuestSeq(int curRunQuestSeq) {
		this.curRunQuestSeq = curRunQuestSeq;
	}

	public int getDailyMaxRunNum() {
		return dailyMaxRunNum;
	}

	public void setDailyMaxRunNum(int dailyMaxRunNum) {
		this.dailyMaxRunNum = dailyMaxRunNum;
	}

}
