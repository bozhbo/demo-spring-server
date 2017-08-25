package com.snail.webgame.game.protocal.scene.sys;

public class SceneIdGen {
	
	private static int id =1 ;
	

	public static synchronized int getSequenceId()
	{
		return id++;
	}

}
