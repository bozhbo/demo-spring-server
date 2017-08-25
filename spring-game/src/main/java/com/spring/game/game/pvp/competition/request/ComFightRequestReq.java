package com.snail.webgame.game.pvp.competition.request;

import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

import org.apache.mina.common.ByteBuffer;

import com.snail.webgame.engine.component.room.protocol.info.impl.BaseRoomResp;
import com.snail.webgame.engine.component.room.protocol.util.RoomValue;

/**
 * 
 * 类介绍:竞技场PVP开始战斗请求Request
 *
 * @author zhoubo
 * @2014-11-24
 */
public class ComFightRequestReq extends BaseRoomResp {

	private byte fightType; // 1-竞技场PVP 2-地图PVP 3-组队对攻战 4-单人对攻战  5-二人组队对攻战 6-组队副本  7-3V3 三人  8-单人3V3 9-3V3二人 
	private int roleId;
	private byte group;
	private String nickName;
	private String serverName;
	private String uuid;
	private String voiceUid;
	private CompetitionVo competitionVo;
	private MutualVo mutualVo;
	private TeamChallengeVo teamChallengeVo;
	private MapVo mapVo;
	private byte warriorCount;
	private List<WarriorVo> warriorList;
	private int showPlanId;//1-显示套装  0-显示时装
	
	@Override
	public void resp2Bytes(ByteBuffer buffer, ByteOrder order) {
		setByte(buffer, order, fightType);
		setInt(buffer, order, roleId);
		setByte(buffer, order, group);
		setString(buffer, order, nickName);
		setString(buffer, order, serverName);
		setString(buffer, order, uuid);
		setString(buffer, order, voiceUid);
		
		if (competitionVo != null) {
			competitionVo.resp2Bytes(buffer, order);
		}
		
		if (mutualVo != null) {
			mutualVo.resp2Bytes(buffer, order);
		}
		
		if (mapVo != null) {
			mapVo.resp2Bytes(buffer, order);
		}

		if (teamChallengeVo != null) {
			teamChallengeVo.resp2Bytes(buffer, order);
		}
		
		setByte(buffer, order, warriorCount);
		
		if (warriorCount > 0) {
			for (WarriorVo vo : warriorList) {
				vo.resp2Bytes(buffer, order);
			}
		}
		setInt(buffer, order, showPlanId);
	}
	
	@Override
	public void bytes2Req(ByteBuffer buffer, ByteOrder order) {
		this.fightType = getByte(buffer, order);
		this.roleId = getInt(buffer, order);
		this.group = getByte(buffer, order);
		this.nickName = getString(buffer, order);
		this.serverName = getString(buffer, order);
		this.uuid = getString(buffer, order);
		this.voiceUid = getString(buffer, order);
		
		if (this.getMsgType() != RoomValue.MESSAGE_TYPE_SEND_MSG_RESULT_FE15) {
			// 非战斗服务器准备就绪接口，需要读取以下字段
			if (fightType == 1) {
				this.competitionVo = new CompetitionVo();
				this.competitionVo.bytes2Req(buffer, order);
			}
			
			if (fightType == 3 || fightType == 4) {
				this.mutualVo = new MutualVo();
				this.mutualVo.bytes2Req(buffer, order);
			}
			
			if (fightType == 5) {
				this.mapVo = new MapVo();
				this.mapVo.bytes2Req(buffer, order);
			}
			
			if (fightType == 6) {
				this.teamChallengeVo = new TeamChallengeVo();
				this.teamChallengeVo.setMsgType(this.getMsgType());
				this.teamChallengeVo.bytes2Req(buffer, order);
			}
			this.warriorCount = getByte(buffer, order);
			this.warriorList = new ArrayList<WarriorVo>(this.warriorCount);
			
			for (int index = 0;index < this.warriorCount;index ++) {
				WarriorVo vo = new WarriorVo();
				vo.bytes2Req(buffer, order);
				this.warriorList.add(vo);
			}
		}
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

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getVoiceUid() {
		return voiceUid;
	}

	public void setVoiceUid(String voiceUid) {
		this.voiceUid = voiceUid;
	}

	public CompetitionVo getCompetitionVo() {
		return competitionVo;
	}

	public void setCompetitionVo(CompetitionVo competitionVo) {
		this.competitionVo = competitionVo;
	}

	public byte getWarriorCount() {
		return warriorCount;
	}

	public void setWarriorCount(byte warriorCount) {
		this.warriorCount = warriorCount;
	}

	public List<WarriorVo> getWarriorList() {
		return warriorList;
	}

	public void setWarriorList(List<WarriorVo> warriorList) {
		this.warriorList = warriorList;
	}

	public byte getFightType() {
		return fightType;
	}

	public void setFightType(byte fightType) {
		this.fightType = fightType;
	}

	public byte getGroup() {
		return group;
	}

	public void setGroup(byte group) {
		this.group = group;
	}

	public MutualVo getMutualVo() {
		return mutualVo;
	}

	public void setMutualVo(MutualVo mutualVo) {
		this.mutualVo = mutualVo;
	}

	public MapVo getMapVo() {
		return mapVo;
	}

	public void setMapVo(MapVo mapVo) {
		this.mapVo = mapVo;
	}

	public int getShowPlanId() {
		return showPlanId;
	}

	public void setShowPlanId(int showPlanId) {
		this.showPlanId = showPlanId;
	}

	public TeamChallengeVo getTeamChallengeVo() {
		return teamChallengeVo;
	}

	public void setTeamChallengeVo(TeamChallengeVo teamChallengeVo) {
		this.teamChallengeVo = teamChallengeVo;
	}
}
