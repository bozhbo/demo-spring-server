package com.spring.common.pathfinding.mesh;

import java.util.ArrayList;
import java.util.List;

import com.spring.common.info.MapPoint;
import com.spring.common.info.Point3D;

public class GameMap3D {

	private int maxX = 0;
	private int maxY = 0;
	private int maxZ = 0;

	private LocationHelper helper;

	private List<Point3D> point;

	private MapPoint[][][] mapPoints = null;

	public GameMap3D(LocationHelper helper, MapPoint[][][] mapPoints, int maxX, int maxY, int maxZ, List<Point3D> point) {

		this.helper = helper;
		this.mapPoints = mapPoints;
		this.point = point;
		this.maxX = maxX;
		this.maxY = maxY;
		this.maxZ = maxZ;
	}

	public List<Point3D> getPoint() {
		return point;
	}

	public void setPoint(List<Point3D> point) {
		this.point = point;
	}

	public LocationHelper getHelper() {
		return helper;
	}

	/**
	 * 设置地图上玩家
	 * 
	 * @param x
	 * @param z
	 * @param roleId
	 */
	public void setRole(int x, int y, int z, long roleId) {
		MapPoint result = mapPoints[x][y][z];
		if (result == null) {
			result = new MapPoint();
			mapPoints[x][y][z] = result;
		}
		if (!result.getRoleIds().contains(roleId))
			result.getRoleIds().add(roleId);
	}

	/**
	 * 删除地图上玩家
	 * 
	 * @param x
	 * @param z
	 * @param roleId
	 */
	public void delRole(int x, int y, int z, long roleId) {
		MapPoint result = mapPoints[x][y][z];
		if (result == null) {
			result = new MapPoint();
			mapPoints[x][y][z] = result;
		}
		result.getRoleIds().remove(roleId);
	}

	/**
	 * 设置地图上NPC
	 * 
	 * @param x
	 * @param z
	 * @param npcId
	 */
	public void setNPC(int x, int y, int z, long npcId) {
		MapPoint result = mapPoints[x][y][z];
		if (result == null) {
			result = new MapPoint();
			mapPoints[x][y][z] = result;
		}
		if (!result.getNpcIds().contains(npcId)) {
			result.getNpcIds().add(npcId);
		}
	}

	/**
	 * 删除地图上NPC
	 * 
	 * @param x
	 * @param z
	 * @param npcId
	 */
	public void delNPC(int x, int y, int z, long npcId) {
		MapPoint result = mapPoints[x][y][z];
		if (result == null) {
			result = new MapPoint();
			mapPoints[x][y][z] = result;
		}
		result.getNpcIds().remove(npcId);
	}
	
	/**
	 * 设置地图上掉落物品
	 * @param x
	 * @param y
	 * @param z
	 * @param dropId
	 * @author wangxf
	 * @date 2012-9-5
	 *//*
	public void setDrop(int x, int y, int z, long dropId){
		MapPoint result = mapPoints[x][y][z];
		if (result == null) {
			result = new MapPoint();
			mapPoints[x][y][z] = result;
		}
		if(!result.getDropBagIds().contains(dropId)) {
			result.getDropBagIds().add(dropId);
		}
	}
	
	*//**
	 * 删除地图上掉落物品
	 * @param x
	 * @param y
	 * @param z
	 * @param dropId
	 * @author wangxf
	 * @date 2012-9-5
	 *//*
	public void delDrop(int x, int y, int z, long dropId){
		MapPoint result = mapPoints[x][y][z];
		if (result == null) {
			result = new MapPoint();
			mapPoints[x][y][z] = result;
		}
		result.getDropBagIds().remove(dropId);
	}*/
	
	/**
	 * 设置地图上掉落包
	 * @param x
	 * @param y
	 * @param z
	 * @param dropId
	 * @author wangxf
	 * @date 2012-9-5
	 */
	public void setDropBag(int x, int y, int z, long id){
		MapPoint result = mapPoints[x][y][z];
		if (result == null) {
			result = new MapPoint();
			mapPoints[x][y][z] = result;
		}
		if(!result.getDropBagIds().contains(id)) {
			result.getDropBagIds().add(id);
		}
	}
	
	/**
	 * 删除地图上掉落包
	 * @param x
	 * @param y
	 * @param z
	 * @param dropId
	 * @author wangxf
	 * @date 2012-9-5
	 */
	public void delDropBag(int x, int y, int z, long id){
		MapPoint result = mapPoints[x][y][z];
		if (result == null) {
			result = new MapPoint();
			mapPoints[x][y][z] = result;
		}
		result.getDropBagIds().remove(id);
	}

	public MapPoint getMapPoint(int x, int y, int z){
		return mapPoints[x][y][z];
	}
	public List<MapPoint> getMapPointArea(int startX, int endX, int startY, int endY, int startZ, int endZ) {
		List<MapPoint> result = new ArrayList<MapPoint>();
		if (startX < 0)
			startX = 0;
		if (startY < 0)
			startY = 0;
		if (startZ < 0)
			startZ = 0;
		if (endX >= maxX)
			endX = maxX - 1;
		if (endY >= maxY)
			endY = maxY - 1;
		if (endZ >= maxZ)
			endZ = maxZ - 1;

		for (int x = startX; x <= endX; x++) {
			for (int y = startY; y <= endY; y++) {
				for (int z = startZ; z <= endZ; z++) {
					MapPoint mapPoint = mapPoints[x][y][z];
					if (mapPoint != null) {
						result.add(mapPoint);
					}
				}
			}

		}
		return result;
	}

//	/**
//	 * 获得该点附近其它角色(不包括自己)
//	 * 
//	 * @param 自己
//	 * @param radiiX
//	 *            X轴半径
//	 * @param radiiY
//	 *            Y轴半径
//	 * @param radiiZ
//	 *            Z轴半径
//	 * @return
//	 */
//	public List<Long> getAreaOtherRole(AIInfo aiInfo, int radiiX, int radiiY, int radiiZ) {
//		int x = (int) aiInfo.getCurrX();
//		int y = (int) aiInfo.getCurrY();
//		int z = (int) aiInfo.getCurrZ();
//		List<Long> roleIds = getAreaAllRole(x, y, z, radiiX, radiiY, radiiZ);
//		if (aiInfo instanceof RoleInfo)
//			roleIds.remove(aiInfo.getId());
//		return roleIds;
//	}
//
//	/**
//	 * 获得该点附近其它角色(不包括自己)
//	 * 
//	 * @param 自己
//	 * @param radiiX
//	 *            X轴半径
//	 * @param radiiY
//	 *            Y轴半径
//	 * @param radiiZ
//	 *            Z轴半径
//	 * @return
//	 */
//	public List<Long> getAreaOtherRole(AIInfo aiInfo, int x, int y, int z, int radiiX, int radiiY, int radiiZ) {
//		List<Long> roleIds = getAreaAllRole(x, y, z, radiiX, radiiY, radiiZ);
//		if (aiInfo instanceof RoleInfo)
//			roleIds.remove(aiInfo.getId());
//		return roleIds;
//	}

	public int getMaxX() {
		return maxX;
	}

	public int getMaxY() {
		return maxY;
	}

	public int getMaxZ() {
		return maxZ;
	}

//	/**
//	 * 检查附近玩家和NPC变化
//	 * 
//	 * @param aiInfo
//	 * @param radiiX
//	 *            X轴半径
//	 * @param radiiY
//	 *            Y轴半径
//	 * @param radiiZ
//	 *            Z轴半径
//	 */
//	public ViewChangeInfo checkAreaChange(AIInfo aiInfo, int radiiX, int radiiY, int radiiZ) {
//		ViewChangeInfo result = new ViewChangeInfo();
//
//		// 新附近的玩家
//		List<Long> newRoleIds = new ArrayList<Long>();
//		// 新附近的NPC
//		List<Long> newNpcIds = new ArrayList<Long>();
//		/**
//		 * 新附近的掉落包
//		 */
//		List<Long> newDropBagIds = new ArrayList<Long>();
//		// 老附近的玩家
//		List<Long> oldRoleIds = aiInfo.getOtherRoleIds();
//
//		// 附近新出现的玩家
//		List<Long> addRoleIds = new ArrayList<Long>();
//		// 附近消失的玩家
//		List<Long> delRoleIds = new ArrayList<Long>();
//		delRoleIds.addAll(oldRoleIds);
//
//		// 老附近的NPC
//		List<Long> oldNPCIds = aiInfo.getNpcIds();
//
//		// 附近新出现的NPC
//		List<Long> addNPCIds = new ArrayList<Long>();
//		// 附近消失的NPC
//		List<Long> delNPCIds = new ArrayList<Long>();
//		delNPCIds.addAll(oldNPCIds);
//		
//		// 老附近的掉落物品
//		List<Long> oldDropBagIds = aiInfo.getDropBagIds();
//		
//		// 附近新出现的掉落物品
//		List<Long> addDropBagIds = new ArrayList<Long>();
//		// 附近消失的掉落物品
//		List<Long> delDropBagIds = new ArrayList<Long>();
//		delDropBagIds.addAll(oldDropBagIds);
//		
//		List<MapPoint> mapPoints = getMapPointArea(
//				(int)aiInfo.getCurrX() - radiiX, (int)aiInfo.getCurrX() + radiiX, 
//				(int)aiInfo.getCurrY() - radiiY, (int)aiInfo.getCurrY() + radiiY,
//				(int)aiInfo.getCurrZ() - radiiZ, (int)aiInfo.getCurrZ() + radiiZ);
//		if (mapPoints != null && !mapPoints.isEmpty()) {
//			for (MapPoint mapPoint : mapPoints) {
//				if (mapPoint != null) {
//					List<Long> tmpRoles = mapPoint.getRoleIds();
//					newRoleIds.addAll(tmpRoles);
//					for (long roleId : tmpRoles) {
//						// 排除自己
//						if (aiInfo instanceof RoleInfo && aiInfo.getId() == roleId) {
//							newRoleIds.remove(roleId);
//							continue;
//						}
//						// 附近新出现的玩家
//						if (!oldRoleIds.contains(roleId)) {
//							addRoleIds.add(roleId);
//						}
//						// 附近消失的玩家
//						if (delRoleIds.contains(roleId)) {
//							delRoleIds.remove(roleId);
//						}
//					}
//					if (aiInfo instanceof RoleInfo) {
//						List<Long> tmpNPCs = mapPoint.getNpcIds();
//						newNpcIds.addAll(tmpNPCs);
//						for (long npcId : tmpNPCs) {
//							// 附近新出现的NPC
//							if (!oldNPCIds.contains(npcId)) {
//								addNPCIds.add(npcId);
//							}
//							// 附近消失的NPC
//							if (delNPCIds.contains(npcId)) {
//								delNPCIds.remove(npcId);
//							}
//						}
//						
//						// 新位置附近的掉落包
//						List<Long> tmpDropBagIds = mapPoint.getDropBagIds();
//						newDropBagIds.addAll(tmpDropBagIds);
//						// 需要从地图上删除的掉落包
//						List<Long> tmpList = new ArrayList<Long>();
//						for(long dropId : tmpDropBagIds){
//							// 已经消失的掉落包，从地图上删除
//							if (DropBagMap.getDropBag(dropId) == null) {
//								// 从当前位置的集合中删除
//								newDropBagIds.remove(new Long(dropId));
//								// 从地图位置上删除
//								tmpList.add(dropId);
//								continue;
//							}
//							//附近新出现的掉落物品
//							if(!oldDropBagIds.contains(dropId)){
//								addDropBagIds.add(dropId);
//							}
//							//附近消失的掉落物品
//							if(delDropBagIds.contains(dropId)){
//								delDropBagIds.remove(dropId);
//							}
//						}
//						// 从地图上删除
//						if (tmpList.size() > 0) {
//							tmpDropBagIds.removeAll(tmpList);
//						}
//					}
//				}
//			}
//		}
//
//		result.setNewRoleIds(newRoleIds);
//		result.setNewNpcIds(newNpcIds);
//		result.setNewDropBagIds(newDropBagIds);
//		result.setAddRoleIds(addRoleIds);
//		result.setDelRoleIds(delRoleIds);
//		result.setAddNPCIds(addNPCIds);
//		result.setDelNPCIds(delNPCIds);
//		result.setAddDropBagIds(addDropBagIds);
//		result.setDelDropBagIds(delDropBagIds);
//		
//		return result;
//	}

	/**
	 * 获得该点附近所有角色
	 * 
	 * @param x
	 *            X坐标
	 * @param Y
	 *            y坐标
	 * @param z
	 *            z坐标
	 * @param radiiX
	 *            X轴半径
	 * @param radiiY
	 *            Y轴半径
	 * @param radiiZ
	 *            Z轴半径
	 * @return
	 */
	public List<Long> getAreaAllRole(int x, int y, int z, int radiiX, int radiiY, int radiiZ) {
		List<MapPoint> mapPoints = getMapPointArea(x - radiiX, x + radiiX, y - radiiY, y + radiiY, z - radiiZ, z
				+ radiiZ);
		List<Long> roleIds = new ArrayList<Long>();
		if (mapPoints != null && !mapPoints.isEmpty()) {
			for (MapPoint mapPoint : mapPoints) {
				if (mapPoint != null) {
					roleIds.addAll(mapPoint.getRoleIds());
				}
			}
		}
		return roleIds;
	}

	/**
	 * 获取坐标点附近的npc信息
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @param refreshRadiiX
	 * @param refreshRadiiY
	 * @param refreshRadiiZ
	 * @return
	 * @author wangxf
	 * @date 2012-7-30
	 */
	public List<Long> getRoundNpcInfo(int x, int y, int z, int radiiX, int radiiY, int radiiZ) {
		List<MapPoint> mapPoints = getMapPointArea(x - radiiX, x + radiiX, y - radiiY, y + radiiY, z - radiiZ, z
				+ radiiZ);
		List<Long> npcIds = new ArrayList<Long>();
		if (mapPoints != null && !mapPoints.isEmpty()) {
			for (MapPoint mapPoint : mapPoints) {
				if (mapPoint != null) {
					npcIds.addAll(mapPoint.getNpcIds());
				}
			}
		}
		return npcIds;
	}

//	/**
//	 * 获得指定点附近的其它玩家和NPC
//	 * 
//	 * @param aiInfo
//	 * @param x
//	 * @param y
//	 * @param z
//	 * @param radiiX
//	 * @param radiiY
//	 * @param radiiZ
//	 * @return
//	 */
//	public Map<String, List<Long>> getAreaOtherRoleAndNPC(AIInfo aiInfo, int x, int y, int z, int radiiX, int radiiY,
//			int radiiZ) {
//		List<MapPoint> mapPoints = getMapPointArea(x - radiiX, x + radiiX, y - radiiY, y + radiiY, z - radiiZ, z
//				+ radiiZ);
//		List<Long> roleIds = new ArrayList<Long>();
//		List<Long> npcIds = new ArrayList<Long>();
//		if (mapPoints != null && !mapPoints.isEmpty()) {
//			for (MapPoint mapPoint : mapPoints) {
//				if (mapPoint != null) {
//					roleIds.addAll(mapPoint.getRoleIds());
//					npcIds.addAll(mapPoint.getNpcIds());
//				}
//			}
//		}
//		if (aiInfo instanceof RoleInfo)
//			roleIds.remove(aiInfo.getId());
//
//		Map<String, List<Long>> result = new HashMap<String, List<Long>>();
//		result.put("role", roleIds);
//		result.put("npc", npcIds);
//		return result;
//	}

	/**
	 * 获取指定坐标附近范围内的掉落物品
	 * @param x
	 * @param y
	 * @param z
	 * @param refreshRadiiX
	 * @param refreshRadiiY
	 * @param refreshRadiiZ
	 * @return
	 * @author wangxf
	 * @date 2012-9-5
	 */
	public List<Long> getRoundDropInfo(int x, int y, int z, int refreshRadiiX,
			int refreshRadiiY, int refreshRadiiZ) {
		List<MapPoint> mapPoints = getMapPointArea(x - refreshRadiiX, x + refreshRadiiX, 
				y - refreshRadiiY, y + refreshRadiiY,
				z- refreshRadiiZ, z + refreshRadiiZ);
		List<Long> DropBagIds = new ArrayList<Long>();
		if (mapPoints != null && !mapPoints.isEmpty()) {
			for (MapPoint mapPoint : mapPoints) {
				if (mapPoint != null) {
					DropBagIds.addAll(mapPoint.getDropBagIds());
				}
			}
		}
		return DropBagIds;
	}
	
	/**
	 * 获取指定坐标附近范围内的掉落包
	 * @param x
	 * @param y
	 * @param z
	 * @param refreshRadiiX
	 * @param refreshRadiiY
	 * @param refreshRadiiZ
	 * @return
	 * @author wangxf
	 * @date 2012-9-5
	 */
	public List<Long> getRoundDropBag(int x, int y, int z, int refreshRadiiX,
			int refreshRadiiY, int refreshRadiiZ) {
		List<MapPoint> mapPoints = getMapPointArea(x - refreshRadiiX, x + refreshRadiiX, 
				y - refreshRadiiY, y + refreshRadiiY,
				z- refreshRadiiZ, z + refreshRadiiZ);
		List<Long> dropBagIds = new ArrayList<Long>();
		if (mapPoints != null && !mapPoints.isEmpty()) {
			for (MapPoint mapPoint : mapPoints) {
				if (mapPoint != null) {
					dropBagIds.addAll(mapPoint.getDropBagIds());
				}
			}
		}
		return dropBagIds;
	}

}
