package com.snail.webgame.game.pvp.competition.ready;

import java.nio.ByteOrder;

import org.apache.mina.common.ByteBuffer;

import com.snail.webgame.engine.component.room.protocol.info.impl.BaseRoomReq;

/**
 * 
 * 类介绍:游戏服务器开始战斗消息
 *
 * @author zhoubo
 * @2014-11-25
 */
public class ComFightReadyResp extends BaseRoomReq {
	private byte fightType;// 1-竞技场PVP 2-地图PVP 3-对攻战 5-劫镖 6-组队副本  7-3V3
	private int result; // 战斗匹配结果 1-匹配成功 2-战斗服务器异常不能开始战斗 3-匹配服务器繁忙
	private String server; // ip:port
	private String roleInfs;// 角色集合 [roleId:uuid,roleId:uuid]
	
	@Override
	public void bytes2Req(ByteBuffer buffer, ByteOrder order) {
		this.fightType = getByte(buffer, order);
		this.result = getInt(buffer, order);
		this.server = getString(buffer, order);
		this.roleInfs = getString(buffer, order);
	}

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public String getRoleInfs() {
		return roleInfs;
	}

	public void setRoleInfs(String roleInfs) {
		this.roleInfs = roleInfs;
	}
	
	/**
	 *  1-竞技场PVP 2-地图PVP 3-对攻战 5-劫镖 6-组队副本  7-3V3
	 */
	public byte getFightType() {
		return fightType;
	}

	/**
	 *  1-竞技场PVP 2-地图PVP 3-对攻战 5-劫镖 6-组队副本  7-3V3
	 */
	public void setFightType(byte fightType) {
		this.fightType = fightType;
	}

	@Override
	public String toString() {
		return "ComFightReadyResp [result=" + result + ", server=" + server
				+ ", roleInfs=" + roleInfs + "]";
	}
}
