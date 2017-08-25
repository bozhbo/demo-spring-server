package com.snail.webgame.game.pvp.competition.request;

import java.nio.ByteOrder;

import org.apache.mina.common.ByteBuffer;

import com.snail.webgame.engine.component.room.protocol.info.impl.BaseRoomReq;

/**
 * 技能信息
 * @author qiuhd
 * @since  2014年11月21日
 * @version V1.0.0
 */
public class SkillInfoVo extends BaseRoomReq {
	
	private int heroId;
	private int skillNo;
	private int level;
	private byte position;
	private int ai;
	private byte aiOrder;
	
	public SkillInfoVo() {
		
	}
	
	public SkillInfoVo(int heroId, int skillNo, int level, byte position, int ai, byte aiOrder) {
		this.heroId = heroId;
		this.skillNo = skillNo;
		this.level = level;
		this.position = position;
		this.ai = ai;
		this.aiOrder = aiOrder;
	}

	@Override
	public void bytes2Req(ByteBuffer buffer, ByteOrder order) {
		this.heroId = getInt(buffer, order);
		this.skillNo = getInt(buffer, order);
		this.level = getInt(buffer, order);
		this.position = getByte(buffer, order);
		this.ai = getInt(buffer, order);
		this.aiOrder = getByte(buffer, order);
	}
	
	@Override
	public void resp2Bytes(ByteBuffer buffer, ByteOrder order) {
		this.setInt(buffer, order, this.heroId);
		this.setInt(buffer, order, skillNo);
		this.setInt(buffer, order, level);
		this.setByte(buffer, order, this.position);
		this.setInt(buffer, order, this.ai);
		this.setByte(buffer, order, this.aiOrder);
	}

	public int getSkillNo() {
		return skillNo;
	}

	public void setSkillNo(int skillNo) {
		this.skillNo = skillNo;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public byte getPosition() {
		return position;
	}

	public void setPosition(byte position) {
		this.position = position;
	}
	
	public int getHeroId() {
		return heroId;
	}

	public void setHeroId(int heroId) {
		this.heroId = heroId;
	}

	public int getAi() {
		return ai;
	}

	public void setAi(int ai) {
		this.ai = ai;
	}

	public byte getAiOrder() {
		return aiOrder;
	}

	public void setAiOrder(byte aiOrder) {
		this.aiOrder = aiOrder;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + skillNo;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SkillInfoVo other = (SkillInfoVo) obj;
		if (skillNo != other.skillNo)
			return false;
		return true;
	}
}
