package com.spring.world.room.service;

public interface RoomManageService {

	public void roomServerRegister(int roomServerId);
	
	public void roomServerClose(int roomServerId);
	
	public void roomInfo(String info);
}
