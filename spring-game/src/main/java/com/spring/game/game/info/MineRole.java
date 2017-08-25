package com.snail.webgame.game.info;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.snail.webgame.engine.common.to.BaseTO;
import com.snail.webgame.game.cache.MineInfoMap;
import com.snail.webgame.game.xml.cache.MineXMLInfoMap;
import com.snail.webgame.game.xml.info.MineXMLInfo;

/**
 * 矿点占领信息
 * @author zenggang
 */
public class MineRole extends BaseTO {

	public static final byte NOT_GET_PRIZE = 0;// 未领取奖励
	public static final byte GET_PRIZE = 1;// 已领取奖励

	public static final byte END_BY_QD = 1;// 被抢夺
	public static final byte END_BY_FQ = 2;// 被放弃

	private int mineId;// 矿id
	private int roleId; // 占领人
	private Timestamp createTime;// 占领开采时间
	private Timestamp endTime = null;// 被抢夺(放弃)时间(空：未被抢夺或放弃)
	private byte endStatus = 0;// 1-被抢夺 2-被放弃
	private byte status = 0;// 0-未领取奖励 1-已领取奖励

	// 防守阵形<pos,heroId>
	private Map<Integer, Integer> heroMap = new HashMap<Integer, Integer>();

	// 协防人<roleId，MineHelpRole>
	private Map<Integer, MineHelpRole> helpRoles = new HashMap<Integer, MineHelpRole>();

	public MineRole() {

	}

	public MineRole(int mineId, int roleId, Map<Integer, Integer> heroMap) {
		this.mineId = mineId;
		this.roleId = roleId;
		this.createTime = new Timestamp(System.currentTimeMillis());
		this.heroMap = heroMap;
	}

	public int getMineId() {
		return mineId;
	}

	public void setMineId(int mineId) {
		this.mineId = mineId;
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public Timestamp getEndTime() {
		return endTime;
	}

	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}

	/**
	 * 是否结束
	 * @return
	 */
	public boolean isEnd() {
		return getCurrEndTime() <= System.currentTimeMillis();
	}

	/**
	 * 矿开采结束时间
	 * @return
	 */
	public long getCurrEndTime() {
		if (endTime != null) {
			return endTime.getTime();
		}
		MineInfo mineInfo = MineInfoMap.getMineInfo(mineId);
		if (mineInfo == null) {
			return 0;
		}
		MineXMLInfo xmlInfo = MineXMLInfoMap.getMineXMLInfo(mineInfo.getMineNo());
		if (xmlInfo == null) {
			return 0;
		}
		/*long startTime = createTime.getTime();
		long endTime = mineInfo.getCreateTime().getTime() + xmlInfo.getMineTime() * 60 * 1000;
		List<Long> helpTimes = new ArrayList<Long>();
		for (MineHelpRole helpRole : helpRoles.values()) {
			long time = helpRole.getCreateTime().getTime();
			if (time >= startTime && time <= endTime) {
				helpTimes.add(time);
			}
		}
		Collections.sort(helpTimes);
		for (int i = 0; i < helpTimes.size(); i++) {
			startTime = helpTimes.get(i);
			if (startTime > endTime) {
				return endTime;
			}
			endTime = startTime + ((endTime - startTime) * (i + 1)) / (i + 2);
		}*/
		return createTime.getTime() + xmlInfo.getNewMineTime() * 60 * 1000;
	}

	public byte getEndStatus() {
		return endStatus;
	}

	public void setEndStatus(byte endStatus) {
		this.endStatus = endStatus;
	}

	public byte getStatus() {
		return status;
	}

	public void setStatus(byte status) {
		this.status = status;
	}

	public Map<Integer, Integer> getHeroMap() {
		return heroMap;
	}

	public void setHeroMap(Map<Integer, Integer> heroMap) {
		this.heroMap = heroMap;
	}

	public Map<Integer, MineHelpRole> getHelpRoles() {
		return helpRoles;
	}

	public MineHelpRole getHelpRoles(int roleId) {
		return helpRoles.get(roleId);
	}

	public void setHelpRoles(Map<Integer, MineHelpRole> helpRoles) {
		this.helpRoles = helpRoles;
	}

	public List<MineHelpRole> getHelpRoleListbySort() {
		List<MineHelpRole> list = new ArrayList<MineHelpRole>(helpRoles.values());
		Collections.sort(list, new Comparator<MineHelpRole>() {
			@Override
			public int compare(MineHelpRole o1, MineHelpRole o2) {
				if (o1.getHelpPos() < o2.getHelpPos()) {
					return -1;
				} else if (o1.getHelpPos() > o2.getHelpPos()) {
					return 1;
				}
				return 0;
			}
		});
		return list;
	}

	public byte getNewHelpPos() {
		int maxHelpPos = 0;
		for (MineHelpRole help : helpRoles.values()) {
			maxHelpPos = Math.max(maxHelpPos, help.getHelpPos());
		}
		return (byte) (maxHelpPos + 1);
	}

	/**
	 * 是否可以抢夺
	 * @return
	 */
	public boolean isCanLoot() {
		MineInfo info = MineInfoMap.getMineInfo(mineId);
		if (info == null) {
			return false;
		}
		MineXMLInfo xmlInfo = MineXMLInfoMap.getMineXMLInfo(info.getMineNo());
		if (xmlInfo == null) {
			return false;
		}
		long startTime = createTime.getTime() + xmlInfo.getGuardTime() * 60 * 1000;
		long endTime = getCurrEndTime();
		long nowTime = System.currentTimeMillis();
		return nowTime >= startTime && nowTime < endTime;
	}

	@Override
	public byte getSaveMode() {
		return ONLINE;
	}
}
