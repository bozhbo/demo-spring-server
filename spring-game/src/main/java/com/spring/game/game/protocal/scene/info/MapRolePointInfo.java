package com.snail.webgame.game.protocal.scene.info;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;



/**
 * 世界地图中玩家坐标
 * @author hongfm
 *
 */
public class MapRolePointInfo {

	private int roleId;
	private String roleName;
	private int rolePic;
	private int race;
	private short roleLv;
	private float pointX;
	private float pointZ;
	private byte status;//0-普通状态  1-战斗中 2-新手状态 3-保护状态 4-押镖状态 5-押镖战斗中(与护送人员战斗) 6-押镖战斗中(与本人战斗) 7-护镖中  8-护镖战斗中（此状态是护镖人用的状态）
	private boolean outCity;//是否刚出城
	private List<Integer> helpList;//请求护送人员名单
	private int screenPointX;// 当前屏幕X
	private int screenPointZ;// 当前屏幕Z
	
	//玩家在大地图某个点驻足
	private HashMap<Integer,Long> stayNpcMap = new HashMap<Integer,Long>();
	
	private ConcurrentHashMap<Integer,Integer> screenRoleIds = new ConcurrentHashMap<Integer,Integer>();//玩家设备屏幕内可见其它玩家
	
	private ConcurrentHashMap<Integer,Integer> inOtherRoleScreen = new ConcurrentHashMap<Integer,Integer>();//自己在其它玩家的屏幕视野内,用于后期自己移动后，通知其它玩家
	
	private boolean isWaitingHelp;// 是否在等待好友护镖同意，如果大地图上移动，改为false;
	
	private int biaocheOtherRoleId;// 护镖的时候，如果status=4，此字段标示护镖人，如果status=7，此字段表示押镖人
	
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

	public int getRolePic() {
		return rolePic;
	}

	public void setRolePic(int rolePic) {
		this.rolePic = rolePic;
	}

	public int getRace() {
		return race;
	}

	public void setRace(int race) {
		this.race = race;
	}

	public short getRoleLv() {
		return roleLv;
	}

	public void setRoleLv(short roleLv) {
		this.roleLv = roleLv;
	}

	public float getPointX() {
		return pointX;
	}

	public void setPointX(float pointX) {
		this.pointX = pointX;
	}

	public float getPointZ() {
		return pointZ;
	}

	public void setPointZ(float pointZ) {
		this.pointZ = pointZ;
	}

	/**
	 * 0-普通状态  1-战斗中 2-新手状态 3-保护状态 4-押镖状态 5-押镖战斗中(与护送人员战斗) 6-押镖战斗中(与本人战斗) 7-护镖中  8-护镖战斗中（此状态是护镖人用的状态）
	 * @return
	 */
	public byte getStatus() {
		return status;
	}

	/**
	 * 0-普通状态  1-战斗中 2-新手状态 3-保护状态 4-押镖状态 5-押镖战斗中(与护送人员战斗) 6-押镖战斗中(与本人战斗) 7-护镖中  8-护镖战斗中（此状态是护镖人用的状态）
	 * @param status
	 */
	public void setStatus(byte status) {
		this.status = status;
	}

	public HashMap<Integer, Long> getStayNpcMap() {
		return stayNpcMap;
	}

	public void setStayNpcMap(HashMap<Integer, Long> stayNpcMap) {
		this.stayNpcMap = stayNpcMap;
	}
	

	public ConcurrentHashMap<Integer, Integer> getScreenRoleIds() {
		return screenRoleIds;
	}

	public void setScreenRoleIds(ConcurrentHashMap<Integer, Integer> screenRoleIds) {
		this.screenRoleIds = screenRoleIds;
	}

	public ConcurrentHashMap<Integer, Integer> getInOtherRoleScreen() {
		return inOtherRoleScreen;
	}

	public void setInOtherRoleScreen(
			ConcurrentHashMap<Integer, Integer> inOtherRoleScreen) {
		this.inOtherRoleScreen = inOtherRoleScreen;
	}

	public boolean isOutCity() {
		return outCity;
	}

	public void setOutCity(boolean outCity) {
		this.outCity = outCity;
	}

	public List<Integer> getHelpList() {
		if(helpList == null)
		{
			helpList = new ArrayList<Integer>();
		}
		return helpList;
	}

	public void setHelpList(List<Integer> helpList) {
		this.helpList = helpList;
	}

	public boolean isWaitingHelp() {
		return isWaitingHelp;
	}

	public void setWaitingHelp(boolean isWaitingHelp) {
		this.isWaitingHelp = isWaitingHelp;
	}

	public int getBiaocheOtherRoleId() {
		return biaocheOtherRoleId;
	}

	public void setBiaocheOtherRoleId(int biaocheOtherRoleId) {
		this.biaocheOtherRoleId = biaocheOtherRoleId;
	}

	public int getScreenPointX() {
		return screenPointX;
	}

	public void setScreenPointX(int screenPointX) {
		this.screenPointX = screenPointX;
	}

	public int getScreenPointZ() {
		return screenPointZ;
	}

	public void setScreenPointZ(int screenPointZ) {
		this.screenPointZ = screenPointZ;
	}

}
