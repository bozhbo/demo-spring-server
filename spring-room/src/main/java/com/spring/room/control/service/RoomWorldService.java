package com.spring.room.control.service;

public interface RoomWorldService {
	
	/**
	 * 返回world添加房间成功
	 * 
	 * @param roomInfo
	 * @return
	 */
	public int deployRoomInfoSuccessed(int roomId);
	
	/**
	 * 返回world添加房间失败
	 * 
	 * @param roomInfo
	 * @return
	 */
	public int deployRoomInfoFailed(int roomId);
	
	/**
	 * 返回world移除房间成功
	 * 
	 * @param roomInfo
	 * @return
	 */
	public int removeRoomInfoSuccessed(int roomId);
	
	/**
	 * 返回world移除房间失败
	 * 
	 * @param roomInfo
	 * @return
	 */
	public int removeRoomInfoFailed(int roomId);
	
	/**
	 * 返回world添加玩家成功
	 * 
	 * @param roomInfo
	 * @param roleInfo
	 * @return
	 */
	public int deployRoleInfoSuccessed(int roomId, int roleId);
	
	/**
	 * 返回world添加玩家失败
	 * 
	 * @param roomInfo
	 * @param roleInfo
	 * @return
	 */
	public int deployRoleInfoFailed(int roomId, int roleId);
	
	/**
	 * 返回world移除玩家成功
	 * 
	 * @param roomInfo
	 * @param roleInfo
	 * @return
	 */
	public int removeRoleInfoSuccessed(int roomId, int roleId);
	
	/**
	 * 返回world移除玩家失败
	 * 
	 * @param roomInfo
	 * @param roleInfo
	 * @return
	 */
	public int removeRoleInfoFailed(int roomId, int roleId);
	
	/**
	 * 发送房间服务器信息
	 */
	public void reportRoomServerInfo(int roomCount, int roleCount);
}
