package com.snail.webgame.game.protocal.scene.cache;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.snail.webgame.game.cache.RoleInfoMap;
import com.snail.webgame.game.common.GameValue;
import com.snail.webgame.game.info.BossInfo;
import com.snail.webgame.game.info.RoleInfo;
import com.snail.webgame.game.protocal.scene.info.MapRolePointInfo;
import com.snail.webgame.game.xml.info.SceneXMLInfo.MapCityXMLNPC;

public class MapRoleInfoMap {
	
	 /**
     * 角色刷新范围半径
     */
    public static int refreshRadiiX = 325;
    /**
     * 角色刷新范围半径
     */
    public static int refreshRadiiZ = 300;
	
	//<roleId,MapRolePointInfo>
	private static ConcurrentHashMap<Integer,MapRolePointInfo> mapPointMap = new ConcurrentHashMap<Integer,MapRolePointInfo>();
	
	//大地图上的NPC
	private static HashMap<Integer,MapCityXMLNPC> mapNpcMap = new HashMap<Integer,MapCityXMLNPC>();
	
	// 大地图上正在PVP战斗的玩家<roleId,time>
	private static ConcurrentHashMap<Integer,Long> mapPvPFightRoleMap = new ConcurrentHashMap<Integer,Long>();
	
	//大地图上的世界boss
	private static List<BossInfo> mapBossList = new ArrayList<BossInfo>();
	
	private static class InternalClass {
		public final static MapRoleInfoMap instance = new MapRoleInfoMap();
	}

	public static MapRoleInfoMap getInstance() {
		return InternalClass.instance;
	}
    
    public static void addMapPoint(MapRolePointInfo info)
    {
    	mapPointMap.put(info.getRoleId(), info);
    }
    
    public static MapRolePointInfo getMapPointInfo(int roleId)
    {
    	return mapPointMap.get(roleId);
    }
    
    public static void removeMapPointInfo(int roleId)
    {
    	mapPointMap.remove(roleId);
    }
    
    public static void addMapNpc(MapCityXMLNPC xmlInfo)
    {
    	mapNpcMap.put(xmlInfo.getNo(), xmlInfo);
    }
    
    public static HashMap<Integer,MapCityXMLNPC> getMapNpc()
    {
    	return mapNpcMap;
    }
    	
	public static Set<Integer> getMapRoleId() {
		return mapPointMap.keySet();
	}

	public static List<MapRolePointInfo> mapToList()
	{
		List<MapRolePointInfo> mapRoleList = new ArrayList<MapRolePointInfo>();
		for(int roleId : mapPointMap.keySet())
		{
			MapRolePointInfo rolePoint = mapPointMap.get(roleId);
			if(rolePoint == null)
			{
				continue;
			}
			mapRoleList.add(rolePoint);
		}
		return mapRoleList;
	}
	
	
	public static int getAllRole()
	{
		return mapPointMap.size();
	}
	
	/**
	 * 获取玩家设备屏幕内符合要求的玩家
	 * @param x
	 * @param z
	 * @param meRoleId
	 * @param roleLv
	 * @return
	 */
	public static ConcurrentHashMap<Integer,Integer> getScreenRole(int x,int z,int meRoleId)
	{
		ConcurrentHashMap<Integer,Integer> roleList = new ConcurrentHashMap<Integer,Integer>();
		roleList.put(meRoleId,meRoleId);
		int startX = x - refreshRadiiX;
		int endX = x+refreshRadiiX;
		int startZ= z-refreshRadiiZ;
		int endZ = z+refreshRadiiZ;
		
		List<MapRolePointInfo> mapRoleList =  mapToList();
		Collections.shuffle(mapRoleList);
		
		for(MapRolePointInfo mapRolePoint : mapRoleList)
		{
			RoleInfo roleInfo = RoleInfoMap.getRoleInfo(mapRolePoint.getRoleId());
			if(roleInfo == null)
			{
				continue;
			}
			
			// 屏幕内可见玩家数
			if(roleList.containsKey(meRoleId) && roleList.size() > GameValue.MAP_AREA_SEE_NUM)
			{
				return roleList;
			}
			
			if(!roleList.containsKey(meRoleId) && roleList.size() >= GameValue.MAP_AREA_SEE_NUM)
			{
				return roleList;
			}
			
			if(mapRolePoint.getPointX()>=startX && mapRolePoint.getPointX()<=endX 
					&& mapRolePoint.getPointZ()>=startZ&&mapRolePoint.getPointZ()<=endZ
					&& !roleList.contains(mapRolePoint.getRoleId()))
			{
				roleList.put(mapRolePoint.getRoleId(),mapRolePoint.getRoleId());
			}
		}
		
		return roleList;
	}
	
	public static ConcurrentHashMap<Integer,Long> getMapPVPFightMap()
	{
		return mapPvPFightRoleMap;
	}
	
	//添加世界boss
    public static void addWorldBoss(BossInfo bossInfo)
    {
    	BossInfo boss = new BossInfo();
    	boss.setBossType(bossInfo.getBossType());
    	boss.setBosslevel(bossInfo.getBosslevel());
    	boss.setBossNo(bossInfo.getBossNo());
    	boss.setAllHP(bossInfo.getAllHP());
    	boss.setCurrHP(bossInfo.getCurrHP());
    	boss.setRate(bossInfo.getRate());
    	boss.setMapNo(bossInfo.getMapNo());
    	mapBossList.add(boss);
    }
    
    //获得世界boss
    public static List<BossInfo> getWorldBoss()
    {
    	return mapBossList;
    }
    
    //删除世界BOSS
    public static void removeBoss()
    {
    	mapBossList.clear();
    }
    
}
