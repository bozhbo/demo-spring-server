package com.snail.webgame.game.info;

import java.sql.Timestamp;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.snail.webgame.engine.common.to.BaseTO;
import com.snail.webgame.game.common.FightInfo;
import com.snail.webgame.game.xml.cache.MineXMLInfoMap;
import com.snail.webgame.game.xml.info.MineXMLInfo;

/**
 * 矿点信息
 * @author zenggang
 */
public class MineInfo extends BaseTO {

	private int mineNo; // 矿类型编号
	private int position;// 矿位置编号（唯一）
	private Timestamp createTime;// 矿生成时间

	// 矿点占领信息 <id,info>
	private Map<Integer, MineRole> roles = new ConcurrentHashMap<Integer, MineRole>();
	
	// 战斗信息(战斗中)
	private Map<Integer,FightInfo> fightMap = new ConcurrentHashMap<Integer, FightInfo>();

	public int getMineNo() {
		return mineNo;
	}

	public void setMineNo(int mineNo) {
		this.mineNo = mineNo;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public Map<Integer, MineRole> getRoles() {
		return roles;
	}
	
	public MineRole getMineRole(int mineRoleId){
		return roles.get(mineRoleId);
	}

	public void addMineRole(MineRole mineRole) {
		roles.put(mineRole.getId(), mineRole);
	}

	public void removeMineRole(int mineRoleId) {
		roles.remove(mineRoleId);
	}

	public int getZlSize() {
		int size = 0;
		for (MineRole mineRole : roles.values()) {
			if (!mineRole.isEnd()) {
				size++;
			}
		}
		return size;
	}

	public MineRole getMineRoleZLbyRoleId(int roleId) {
		for (MineRole mineRole : roles.values()) {
			if (mineRole.isEnd()) {
				continue;
			}
			if (mineRole.getRoleId() == roleId) {
				return mineRole;
			}
		}
		return null;
	}

	public MineRole getMineRoleZLAndHelpbyRoleId(int roleId) {
		for (MineRole mineRole : roles.values()) {
			if (mineRole.isEnd()) {
				continue;
			}
			if (mineRole.getRoleId() == roleId) {
				return mineRole;
			}
			if (mineRole.getHelpRoles(roleId) != null) {
				return mineRole;
			}
		}
		return null;
	}

	/**
	 * 是否开采结束
	 * @return
	 */
	public boolean isClosed() {
		/*MineXMLInfo xmlInfo = MineXMLInfoMap.getMineXMLInfo(mineNo);
		if (xmlInfo == null) {
			return false;
		}
		long endTime = createTime.getTime() + xmlInfo.getMineTime() * 60 * 1000;
		long nowTime = System.currentTimeMillis();
		return nowTime >= endTime;*/
		
		return false;
	}
	
	public long getOldEndTime() {
		MineXMLInfo xmlInfo = MineXMLInfoMap.getMineXMLInfo(mineNo);
		if (xmlInfo == null) {
			return 0;
		}
		return createTime.getTime() + xmlInfo.getMineTime() * 60 * 1000;
	}
	
	public boolean isOldClosed() {
		long endTime = getOldEndTime();
		if (endTime == 0) {
			return false;
		}
		long nowTime = System.currentTimeMillis();
		return nowTime >= endTime;
	}

	/**
	 * 是否在位置保护范围内（该位置不能创建新矿）
	 * @return
	 */
	public boolean isPostionProtect() {
		/*MineXMLInfo xmlInfo = MineXMLInfoMap.getMineXMLInfo(mineNo);
		if (xmlInfo == null) {
			return false;
		}
		long endTime = createTime.getTime() + xmlInfo.getMineTime() * 60 * 1000;
		long protectTime = endTime + xmlInfo.getResreshCD() * 60 * 1000;
		long nowTime = System.currentTimeMillis();
		return nowTime < protectTime;*/
		
		return true;
	}

	/**
	 * 是否可以删除
	 * @param mins
	 * @return
	 */
	public boolean isCanDel() {
		/*if (isPostionProtect()) {
			return false;
		}
		if (roles.size() > 0) {
			// 开采结束,没结算完成
			return false;
		}
		return true;*/
		return false;
	}

	/**
	 * 是否可以开采
	 * @return
	 */
	public boolean isCanCollect() {
		MineXMLInfo xmlInfo = MineXMLInfoMap.getMineXMLInfo(mineNo);
		if (xmlInfo == null) {
			return false;
		}
		/*long endTime = createTime.getTime() + xmlInfo.getMineTime() * 60 * 1000;
		long nowTime = System.currentTimeMillis();
		int zlNum = getZlSize();
		return nowTime < endTime && zlNum < xmlInfo.getMaxMiners();*/
		int zlNum = getZlSize();
		return zlNum < xmlInfo.getMaxMiners();
	}
	
	
	/**
	 * 战斗信息(战斗中)
	 * @return
	 */
	public Map<Integer, FightInfo> getFightMap() {
		return fightMap;
	}
	
	public void addFightInfo(FightInfo fightInfo) {
		fightMap.put(fightInfo.getRoleId(), fightInfo);
	}
	
	public void removeFightInfo(int roleId) {
		fightMap.remove(roleId);
	}

	@Override
	public byte getSaveMode() {
		return ONLINE;
	}

}
