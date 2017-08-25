package com.snail.webgame.game.info.log;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import com.snail.webgame.engine.common.to.BaseTO;
import com.snail.webgame.game.common.HeroRecord;
import com.snail.webgame.game.info.HeroInfo;

/**
 * 竞技场日志
 * @author hongfm
 * @2015年6月4日
 */
public class RoleArenaLog extends BaseTO {

	private String account;// 通行证帐号 (大写)
	private String roleName;// 角色名
	private int roleId;// 角色id
	private int arenaId;// 竞技场id

	private int beforePlace;// 战斗前排名
	private int afterPlace;// 战斗后排名
	// 挑战方战斗布阵信息
	private Map<Byte, HeroRecord> deployMap = new HashMap<Byte, HeroRecord>();

	private String defendRoleName;// 对手名字
	private int defendRoleId;// 对手ID
	private int defendArenaId;// 对手竞技场id

	private int defendBeforePlace;// 战斗前排名
	private int defendAfterPlace;// 战斗后排名
	// 遭受挑战方战斗布阵信息
	private Map<Byte, HeroRecord> defendDeployMap = new HashMap<Byte, HeroRecord>();

	private int battleResult;// 1-胜 2-败

	private String getItem;// 对战获得道具
	private int useEnergy;// 挑战消耗精力

	private String comment;// 备注
	private Timestamp beginTime;// 战斗开始时间
	private Timestamp createTime;// 日志时间（战斗结束时间）

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public int getArenaId() {
		return arenaId;
	}

	public void setArenaId(int arenaId) {
		this.arenaId = arenaId;
	}

	public int getBeforePlace() {
		return beforePlace;
	}

	public void setBeforePlace(int beforePlace) {
		this.beforePlace = beforePlace;
	}

	public int getAfterPlace() {
		return afterPlace;
	}

	public void setAfterPlace(int afterPlace) {
		this.afterPlace = afterPlace;
	}

	public Map<Byte, HeroRecord> getDeployMap() {
		return deployMap;
	}

	public void setDeployMap(Map<Byte, HeroRecord> deployMap) {
		this.deployMap = deployMap;
	}

	public String getDefendRoleName() {
		return defendRoleName;
	}

	public void setDefendRoleName(String defendRoleName) {
		this.defendRoleName = defendRoleName;
	}

	public int getDefendRoleId() {
		return defendRoleId;
	}

	public void setDefendRoleId(int defendRoleId) {
		this.defendRoleId = defendRoleId;
	}

	public int getDefendArenaId() {
		return defendArenaId;
	}

	public void setDefendArenaId(int defendArenaId) {
		this.defendArenaId = defendArenaId;
	}

	public Map<Byte, HeroRecord> getDefendDeployMap() {
		return defendDeployMap;
	}

	public void setDefendDeployMap(Map<Byte, HeroRecord> defendDeployMap) {
		this.defendDeployMap = defendDeployMap;
	}

	public int getDefendBeforePlace() {
		return defendBeforePlace;
	}

	public void setDefendBeforePlace(int defendBeforePlace) {
		this.defendBeforePlace = defendBeforePlace;
	}

	public int getDefendAfterPlace() {
		return defendAfterPlace;
	}

	public void setDefendAfterPlace(int defendAfterPlace) {
		this.defendAfterPlace = defendAfterPlace;
	}

	public int getBattleResult() {
		return battleResult;
	}

	public void setBattleResult(int battleResult) {
		this.battleResult = battleResult;
	}

	public String getGetItem() {
		return getItem;
	}

	public void setGetItem(String getItem) {
		this.getItem = getItem;
	}

	public int getUseEnergy() {
		return useEnergy;
	}

	public void setUseEnergy(int useEnergy) {
		this.useEnergy = useEnergy;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Timestamp getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Timestamp beginTime) {
		this.beginTime = beginTime;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	@Override
	public byte getSaveMode() {
		return ONLINE;
	}

	public int getRoleMainHero() {
		HeroRecord record = deployMap.get(HeroInfo.DEPLOY_TYPE_MAIN);
		if (record != null) {
			return record.getHeroNo();
		}
		return 0;
	}

	public String getRoleHeros() {
		StringBuilder sb = new StringBuilder();
		HeroRecord record = null;
		for (byte deployStatus = 2; deployStatus < 6; deployStatus++) {
			record = deployMap.get(deployStatus);
			if (record != null) {
				if (sb.length() > 0) {
					sb.append(",");
				}
				sb.append(record.getHeroNo());
			}
		}
		return sb.toString();
	}

	public int getDefendMainHero() {
		HeroRecord record = defendDeployMap.get(HeroInfo.DEPLOY_TYPE_MAIN);
		if (record != null) {
			return record.getHeroNo();
		}
		return 0;
	}

	public String getDefendHeros() {
		StringBuilder sb = new StringBuilder();
		HeroRecord record = null;
		for (byte deployStatus = 2; deployStatus < 6; deployStatus++) {
			record = defendDeployMap.get(deployStatus);
			if (record != null) {
				if (sb.length() > 0) {
					sb.append(",");
				}
				sb.append(record.getHeroNo());
			}
		}
		return sb.toString();
	}
}
