package com.snail.webgame.game.push;

import org.apache.mina.common.IoSession;

import com.snail.webgame.engine.component.push.connect.Connect;
import com.snail.webgame.game.cache.ServerMap;
import com.snail.webgame.game.config.GameConfig;

public class PushService111 {
	
	private static PushIoHandle handle = new PushIoHandle();
	
	/**
	 * 获取与推送接入服务器连接
	 * @return
	 */
	public static IoSession getIoSession()
	{
		IoSession session = ServerMap.getServerSession("PushServer");
		
		if(session == null || !session.isConnected())
		{
			session = Connect.connectServer("PushServer", GameConfig.getInstance().getPushIP(),
					GameConfig.getInstance().getPushPort(), handle);
			
			if(session!=null&&session.isConnected()){
				ServerMap.addServer("PushServer", session);
			}
		}
		
		return session;
	}

}
