package com.snail.webgame.game.pvp.competition.end;

import java.nio.ByteOrder;

import org.apache.mina.common.ByteBuffer;

import com.snail.webgame.engine.component.room.protocol.info.impl.BaseRoomReq;
import com.snail.webgame.game.pvp.competition.request.MapVo;
import com.snail.webgame.game.pvp.competition.request.TeamChallengeVo;

public class ComFightEndRe extends BaseRoomReq {
	private byte fightType; // 1-竞技场PVP 2-地图PVP
	private int roleId;  //角色id
	private String serverName;//所在服务器名
	private String uuid; // 角色战斗标识
	private byte winner ; //胜负：1.负;2.胜.3.平手 4-主动退出 5-根本就没进入游戏
	private EndCompetitionVo endCompetitionVo;
	private MapVo mapVo;
	private TeamChallengeVo teamChallengeVo;

	@Override
	public void bytes2Req(ByteBuffer buffer, ByteOrder order) {
		this.roleId = getInt(buffer, order);
		this.serverName = getString(buffer, order);
		this.uuid = getString(buffer, order);
		this.winner = getByte(buffer, order);
		
		if (fightType == 1) {
			endCompetitionVo = new EndCompetitionVo();
			endCompetitionVo.bytes2Req(buffer, order);
		}
		
		if (fightType == 5) {
			mapVo = new MapVo();
			mapVo.bytes2Req(buffer, order);
		}
		
		if (fightType == 6) {
			teamChallengeVo = new TeamChallengeVo();
			teamChallengeVo.bytes2Req(buffer, order);
		}
	}

	public byte getFightType() {
		return fightType;
	}

	public void setFightType(byte fightType) {
		this.fightType = fightType;
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public byte getWinner() {
		return winner;
	}

	public void setWinner(byte winner) {
		this.winner = winner;
	}

	public EndCompetitionVo getEndCompetitionVo() {
		return endCompetitionVo;
	}

	public void setEndCompetitionVo(EndCompetitionVo endCompetitionVo) {
		this.endCompetitionVo = endCompetitionVo;
	}

	public MapVo getMapVo() {
		return mapVo;
	}

	public void setMapVo(MapVo mapVo) {
		this.mapVo = mapVo;
	}

	public TeamChallengeVo getTeamChallengeVo() {
		return teamChallengeVo;
	}

	public void setTeamChallengeVo(TeamChallengeVo teamChallengeVo) {
		this.teamChallengeVo = teamChallengeVo;
	}
}