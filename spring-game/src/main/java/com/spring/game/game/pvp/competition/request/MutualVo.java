package com.snail.webgame.game.pvp.competition.request;

import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

import org.apache.mina.common.ByteBuffer;

import com.snail.webgame.engine.component.room.protocol.info.impl.BaseRoomResp;

/**
 * 
 * 类介绍:对攻站请求VO
 *
 * @author zhoubo
 * @2015年5月25日
 */
public class MutualVo extends BaseRoomResp {
	
	private long matchTime = 0; // 加入匹配队列时间
	private int turnTimes = 0;// 扩展匹配范围次数
	
	private double prefixValue;// 匹配左值
	private double prefixAddValue;// 匹配左值扩展值
	private double suffixValue;// 匹配右值
	private double suffixAddValue;// 匹配右值扩展值

	private int level;// 等级
	private int fightValue; // 战斗力
	private int count;
	private List<ComFightRequestReq> list;
	
	@Override
	public void bytes2Req(ByteBuffer buffer, ByteOrder order) {
		this.level = getInt(buffer, order);
		this.fightValue = getInt(buffer, order);
		this.count = getInt(buffer, order);
		
		if (count > 0) {
			list = new ArrayList<ComFightRequestReq>();
			
			for (int i = 0; i < count; i++) {
				ComFightRequestReq fightRequestReq = new ComFightRequestReq();
				fightRequestReq.setMsgType(this.getMsgType());
				fightRequestReq.bytes2Req(buffer, order);
				list.add(fightRequestReq);
			}
		}
	}
	
	@Override
	public void resp2Bytes(ByteBuffer buffer, ByteOrder order) {
		setInt(buffer, order, level);
		setInt(buffer, order, fightValue);
		setInt(buffer, order, this.count);
		
		if (list != null && list.size() > 0) {
			for (ComFightRequestReq fightRequestReq : list) {
				fightRequestReq.resp2Bytes(buffer, order);
			}
		}
	}

	public long getMatchTime() {
		return matchTime;
	}

	public void setMatchTime(long matchTime) {
		this.matchTime = matchTime;
	}

	public int getTurnTimes() {
		return turnTimes;
	}

	public void setTurnTimes(int turnTimes) {
		this.turnTimes = turnTimes;
	}

	public double getPrefixValue() {
		return prefixValue;
	}

	public void setPrefixValue(double prefixValue) {
		this.prefixValue = prefixValue;
	}

	public double getPrefixAddValue() {
		return prefixAddValue;
	}

	public void setPrefixAddValue(double prefixAddValue) {
		this.prefixAddValue = prefixAddValue;
	}

	public double getSuffixValue() {
		return suffixValue;
	}

	public void setSuffixValue(double suffixValue) {
		this.suffixValue = suffixValue;
	}

	public double getSuffixAddValue() {
		return suffixAddValue;
	}

	public void setSuffixAddValue(double suffixAddValue) {
		this.suffixAddValue = suffixAddValue;
	}

	public int getFightValue() {
		return fightValue;
	}

	public void setFightValue(int fightValue) {
		this.fightValue = fightValue;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public List<ComFightRequestReq> getList() {
		return list;
	}

	public void setList(List<ComFightRequestReq> list) {
		this.list = list;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}
	
}
