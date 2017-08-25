package com.snail.webgame.engine.gate.thread;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.engine.common.ServerName;
import com.snail.webgame.engine.gate.cache.DisconnectSessionMap;
import com.snail.webgame.engine.gate.cache.SequenceMap;
import com.snail.webgame.engine.gate.common.ContentValue;
import com.snail.webgame.engine.gate.common.DisconnectInfo;
import com.snail.webgame.engine.gate.common.DisconnectPhase;
import com.snail.webgame.engine.gate.config.WebGameConfig;
import com.snail.webgame.engine.gate.util.IdentityMap;
import com.snail.webgame.engine.gate.util.MessageServiceManage;

/**
 * 定时断开超时的客户端链接
 * @author leiqiang
 *
 */
public class CloseSessionThread extends Thread {

	private int interval = WebGameConfig.getInstance().getReconnectFlag() * 1000;
	private int notify = WebGameConfig.getInstance().getDisconnectNotify() * 1000;
	private static final Logger log =LoggerFactory.getLogger("logs");
	MessageServiceManage msgmgt = null;
	public CloseSessionThread(MessageServiceManage msgmgt)
	{
		this.msgmgt = msgmgt;
	}
	@Override
	public void run() {
		if(interval < 30 * 1000)
		{
			interval = 30 * 1000;
		}
		ArrayList<Integer> roleIds = new ArrayList<Integer>();
		ArrayList<Integer> notifyRoleIds = new ArrayList<Integer>();
		while(true)
		{
			synchronized(ContentValue.lock)
			{
				if(DisconnectSessionMap.getSize() > 0)
				{
					ConcurrentHashMap<Integer,DisconnectInfo> map = DisconnectSessionMap.getMap();
					Iterator<Integer> it = map.keySet().iterator();
					while(it.hasNext())
					{
						int sequenceId = it.next();
						DisconnectInfo disconnectInfo = map.get(sequenceId);
						
						//通知
						if(notify > 0 && !disconnectInfo.isNotified() && (System.currentTimeMillis() - disconnectInfo.getTime()) > notify)
						{
							//增加需要通知的角色
							if(disconnectInfo.getRoleId() > 0)
							{
								notifyRoleIds.add(disconnectInfo.getRoleId());
							}
							disconnectInfo.setNotified(true);
						}
						
						//断开时间超过5分钟
						if((System.currentTimeMillis() - disconnectInfo.getTime()) > interval)
						{
							//移除序列号
							SequenceMap.removeSession(sequenceId);
							//移除角色认证信息
							if(disconnectInfo.getRoleId() > 0)
							{
								IdentityMap.removeSession(disconnectInfo.getRoleId());
								roleIds.add(disconnectInfo.getRoleId());
							}
							//删除自身
							it.remove();
						}
					}
				}
			}
			
			if (notifyRoleIds.size() > 0) {
				log.info("CloseSessionThread : notifyRoleIds = " + notifyRoleIds.size());
			}
			
			for(int roleId : notifyRoleIds)
			{
				//通知其它服务器该玩家断开已经一段时间
				List<String> serverList = new ArrayList<String>();
				serverList.add(ServerName.GAME_SERVER_NAME);
				serverList.add(ServerName.MAIL_SERVER_NAME);
				msgmgt.reportUserDisconnect(serverList, "",roleId,DisconnectPhase.DISCONNECT_NOT_TIMEOUT);
			}
			for(int roleId : roleIds)
			{
				//通知其它服务器该玩家断开
				List<String> serverList = new ArrayList<String>();
				serverList.add(ServerName.GAME_SERVER_NAME);
				serverList.add(ServerName.MAIL_SERVER_NAME);
				msgmgt.reportUserDisconnect(serverList, "",roleId,DisconnectPhase.DISCONNECT_TIMEOUT);
			}
			if(roleIds.size() > 50)
			{
				if(log.isWarnEnabled())
				{
					log.warn("CloseSessionThread disconnect sessions="+roleIds.size()+",Interval="+WebGameConfig.getInstance().getReconnectFlag()+"s.");
				}
			}
			roleIds.clear();
			notifyRoleIds.clear();
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
