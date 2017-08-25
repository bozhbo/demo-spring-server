package com.snail.webgame.game.protocal.scene.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.snail.webgame.game.common.GameValue;
import com.snail.webgame.game.protocal.scene.info.RolePoint;
import com.snail.webgame.game.protocal.scene.info.SceneInfo;
import com.snail.webgame.game.protocal.scene.sys.SceneIdGen;

public class SceneInfoMap {
	
	//<sceneNo,<sceneId,sceneInfo>>
	private static ConcurrentHashMap<Integer,ConcurrentHashMap<Integer,SceneInfo>> sceneMap = new ConcurrentHashMap<Integer,ConcurrentHashMap<Integer,SceneInfo>>();
	
	/**
	 * 玩家加入场景,每个国家一个场景编号,每个国家又分N个场景Id,每个里容纳人数可调整
	 * @param info
	 */
	public synchronized static void addScene(RolePoint info)
	{
		if(sceneMap.containsKey(info.getNo()))
		{
			ConcurrentHashMap<Integer,SceneInfo> raceSceneMap = sceneMap.get(info.getNo());
			if(raceSceneMap == null)
			{
				ConcurrentHashMap<Integer,SceneInfo> raceSceneMap1 = new ConcurrentHashMap<Integer,SceneInfo>();
				SceneInfo sceneInfo = new SceneInfo();
				sceneInfo.setSceneNo(info.getNo());
				sceneInfo.setId(SceneIdGen.getSequenceId());
							
				info.setSceneId(sceneInfo.getId());			
				sceneInfo.addRolePoint(info);
				
				raceSceneMap1.put(sceneInfo.getId(), sceneInfo);
				
				sceneMap.put(sceneInfo.getSceneNo(), raceSceneMap1);
			}
			else
			{
				if(raceSceneMap.containsKey(info.getSceneId()))
				{
					// 判断原来是否在场景中
					SceneInfo sceneInfo = raceSceneMap.get(info.getSceneId());
					if(sceneInfo != null)
					{
						Map<Integer,RolePoint> rolePointMap = sceneInfo.getRolePointMap();
						if(rolePointMap != null && rolePointMap.containsKey(info.getRoleId()))
						{
							return;
						}
					}
				}
				
				for(SceneInfo sceneInfo : raceSceneMap.values())
				{
					Map<Integer,RolePoint> rolePointMap = sceneInfo.getRolePointMap();
					if(rolePointMap != null && rolePointMap.size() < GameValue.SCENE_ROLE_NUM)
					{
						info.setSceneId(sceneInfo.getId());
						rolePointMap.put(info.getRoleId(), info);
						
						return;
					}
				}
				
				SceneInfo sceneInfo = new SceneInfo();
				sceneInfo.setSceneNo(info.getNo());
				sceneInfo.setId(SceneIdGen.getSequenceId());
								
				info.setSceneId(sceneInfo.getId());				
				sceneInfo.addRolePoint(info);
				
				raceSceneMap.put(sceneInfo.getId(), sceneInfo);
			}
		}
		else
		{
			ConcurrentHashMap<Integer,SceneInfo> raceSceneMap1 = new ConcurrentHashMap<Integer,SceneInfo>();
			SceneInfo sceneInfo = new SceneInfo();
			sceneInfo.setSceneNo(info.getNo());
			sceneInfo.setId(SceneIdGen.getSequenceId());
						
			info.setSceneId(sceneInfo.getId());				
			sceneInfo.addRolePoint(info);
			
			raceSceneMap1.put(sceneInfo.getId(), sceneInfo);
			
			sceneMap.put(sceneInfo.getSceneNo(), raceSceneMap1);
		}
	}
	
	/**
	 * 玩家下线,从场景删除
	 * @param info
	 */
	public static void delRolepoint(RolePoint info)
	{
		if(sceneMap.containsKey(info.getNo()))
		{
			ConcurrentHashMap<Integer,SceneInfo> raceSceneMap = sceneMap.get(info.getNo());
			if(raceSceneMap.containsKey(info.getSceneId()))
			{
				SceneInfo sceneInfo = raceSceneMap.get(info.getSceneId());
				if(sceneInfo != null)
				{
					Map<Integer,RolePoint> rolePointMap = sceneInfo.getRolePointMap();
					if(rolePointMap != null && rolePointMap.containsKey(info.getRoleId()))
					{
						rolePointMap.remove(info.getRoleId());
					}
				}
			}
		}
	}
	
	/**
	 * 获取场景中玩家坐标
	 * @param roleId
	 * @param sceneNo
	 * @param sceneId
	 * @return
	 */
	public static RolePoint getRolePoint(int roleId,int sceneNo,int sceneId)
	{
		if(sceneMap.containsKey(sceneNo))
		{
			ConcurrentHashMap<Integer,SceneInfo> raceSceneMap = sceneMap.get(sceneNo);
			
			if (raceSceneMap == null) {
				return null;
			}
			
			if(raceSceneMap.containsKey(sceneId))
			{
				SceneInfo sceneInfo = raceSceneMap.get(sceneId);
				if(sceneInfo == null)
				{
					return null;
				}
				Map<Integer,RolePoint> rolePointMap = sceneInfo.getRolePointMap();
				if(rolePointMap != null && rolePointMap.containsKey(roleId))
				{
					return rolePointMap.get(roleId);
				}
			}
		}
		return null;
	}
	
	/**
	 * 获取场景中所有玩家,除自己外
	 * @param sceneNo
	 * @param sceneId
	 * @return
	 */
	public static Map<Integer,RolePoint> getSceneRoleMap(int sceneNo,int sceneId)
	{
		if(sceneMap.containsKey(sceneNo))
		{
			ConcurrentHashMap<Integer,SceneInfo> raceSceneMap = sceneMap.get(sceneNo);
			if(raceSceneMap.containsKey(sceneId))
			{
				SceneInfo sceneInfo = raceSceneMap.get(sceneId);
				if(sceneInfo == null)
				{
					return null;
				}
				return sceneInfo.getRolePointMap();
			}
		}
		return null;
	}

}
