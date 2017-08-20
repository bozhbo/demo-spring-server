package com.spring.scene.dao;

import java.util.List;

import com.spring.scene.obj.GamePlayer;

public interface IRoleDao {

	public GamePlayer loadGamePlayer(String uid);
	
	public List<GamePlayer> getGamePlayers(String account);
	
	public void saveGamePlayer(GamePlayer gamePlayer);
	
	public void setPlayerOnline(GamePlayer gamePlayer);
	
	public void setPlayerOffline(GamePlayer gamePlayer);
	
	public void deletePlayer(GamePlayer gamePlayer);
	
	public void reusePlayer(GamePlayer gamePlayer);
}
