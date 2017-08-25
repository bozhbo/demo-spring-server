package com.snail.webgame.game.info;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.snail.webgame.engine.common.to.BaseTO;
import com.snail.webgame.game.common.HeroRecord;
import com.snail.webgame.game.info.log.RoleArenaLog;

/**
 * 竞技场信息
 */
public class FightArenaInfo extends BaseTO {

	private int roleId;// 0-系统随机 other-用户编号
	private String roleName;// 角色名称
	// deployNo-npcNo;
	private Map<Byte, HeroRecord> fightDeployMap = new HashMap<Byte, HeroRecord>();

	private int maxPlace;// 历史最高排名
	private int place;// 当前排名
	private int initPlace;// 初始排名

	// 缓存最后匹配记录
	private List<FightArenaInfo> matchs = new ArrayList<FightArenaInfo>();

	// 缓存战斗记录
	private List<RoleArenaLog> logs = new ArrayList<RoleArenaLog>();

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public Map<Byte, HeroRecord> getFightDeployMap() {
		return fightDeployMap;
	}

	public void setFightDeployMap(Map<Byte, HeroRecord> fightDeployMap) {
		this.fightDeployMap = fightDeployMap;
	}

	public int getMaxPlace() {
		return maxPlace;
	}

	public void setMaxPlace(int maxPlace) {
		this.maxPlace = maxPlace;
	}

	public int getPlace() {
		return place;
	}

	public void setPlace(int place) {
		this.place = place;
	}

	public int getInitPlace() {
		return initPlace;
	}

	public void setInitPlace(int initPlace) {
		this.initPlace = initPlace;
	}

	public long getResetTime() {
		return 0;
	}

	public List<FightArenaInfo> getMatchs() {
		return matchs;
	}

	public void setMatchs(List<FightArenaInfo> matchs) {
		this.matchs = matchs;
	}

	public List<RoleArenaLog> getLogs() {
		return logs;
	}

	public void setLogs(List<RoleArenaLog> logs) {
		this.logs = logs;
	}

	@Override
	public byte getSaveMode() {
		return ONLINE;
	}

}
