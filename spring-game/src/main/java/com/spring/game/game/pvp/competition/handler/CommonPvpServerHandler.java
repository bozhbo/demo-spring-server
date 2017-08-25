package com.snail.webgame.game.pvp.competition.handler;

import java.util.Map.Entry;
import java.util.Set;

import org.apache.mina.common.IdleStatus;
import org.apache.mina.common.IoSession;
import org.apache.mina.common.TransportType;
import org.apache.mina.transport.socket.nio.SocketSessionConfig;
import org.epilot.ccf.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.engine.component.room.protocol.handler.SessionStateHandler;
import com.snail.webgame.engine.component.room.protocol.info.IRoomHead;
import com.snail.webgame.game.cache.RoleInfoMap;
import com.snail.webgame.game.common.GameServerName;
import com.snail.webgame.game.info.RoleInfo;
import com.snail.webgame.game.info.RoleLoadInfo;

public class CommonPvpServerHandler implements SessionStateHandler {
	
	private static Logger logger = LoggerFactory.getLogger("logs");

	@Override
	public void sessionClose(IoSession session) {
		if (session.getAttribute("serverName") != null) {
			logger.info(session.getAttribute("serverName").toString() + " is closed");
		}
	}

	@Override
	public void sessionOpened(IoSession session) {
		if (session.getTransportType() == TransportType.SOCKET && session.getConfig() instanceof SocketSessionConfig) {
			if (Config.getInstance().getSocketOption().getSocketReceiveBuffer() != null) {
				((SocketSessionConfig) session.getConfig()).setReceiveBufferSize(Integer.parseInt(Config.getInstance().getSocketOption().getSocketReceiveBuffer()));	
			}
			
			if (Config.getInstance().getSocketOption().getKeepAlive() != null) {
				((SocketSessionConfig) session.getConfig()).setKeepAlive(Boolean.parseBoolean(Config.getInstance().getSocketOption().getKeepAlive()));
			}
			
			if (Config.getInstance().getSocketOption().getTcpNoDelay() != null) {
				((SocketSessionConfig) session.getConfig()).setTcpNoDelay(Boolean.parseBoolean(Config.getInstance().getSocketOption().getTcpNoDelay()));
			}
			
			if (Config.getInstance().getSocketOption().getIdleTime() != null) {
				int state = Integer.parseInt(Config.getInstance().getSocketOption().getIdleTime()[0]);
				int timeout = Integer.parseInt(Config.getInstance().getSocketOption().getIdleTime()[1]);
				
				if (state == 1) {
					session.setIdleTime(IdleStatus.READER_IDLE, timeout);
				} else if (state == 2) {
					session.setIdleTime(IdleStatus.WRITER_IDLE, timeout);
				} else if (state == 3) {
					session.setIdleTime(IdleStatus.BOTH_IDLE, timeout);
				}
			}
		}
	}

	@Override
	public boolean checkRoleMessage(IoSession session, IRoomHead roomHead) {
		return true;
	}

	@Override
	public String getRegisterReserve() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void registerEnd(IoSession session, String serverReserve) {
		try {
			if (session.getAttribute("serverName").toString().startsWith(GameServerName.ROOM_FIGHT_NAME)) {
				// 战斗服务器
				if (serverReserve != null) {
					String[] strs = serverReserve.split("\\$");
					
					if (strs.length != 4) {
						// 此战斗服务器不合法，不允许连接
						logger.error("RoomFight send serverReserve is error");
						session.close();
					} else {
						session.setAttribute("fightServerIp", strs[0]);
						session.setAttribute("fightServerCurrentCounts", Integer.parseInt(strs[1]));
						session.setAttribute("fightServerMaxCounts", Integer.parseInt(strs[2]));
						
						// 战斗服务器启动时间
						long startTime = Long.parseLong(strs[3]);
						session.setAttribute("fightServerStartTime", startTime);
						
						if (System.currentTimeMillis() - startTime < 60000) {
							// 战斗服务器已经重启
							Set<Entry<Integer, RoleInfo>> set = RoleInfoMap.getRoleInfoEntrySet(); 
							
							for (Entry<Integer, RoleInfo> entry : set) {
								RoleLoadInfo roleLoadInfo = entry.getValue().getRoleLoadInfo();
								
								if (roleLoadInfo != null) {
									if ((roleLoadInfo.getInFight() == 2 || roleLoadInfo.getInFight() == 4 || roleLoadInfo.getInFight() == 7
											 || roleLoadInfo.getInFight() == 10 || roleLoadInfo.getInFight() == 12) && roleLoadInfo.getFightStartTime() > 0 && roleLoadInfo.getFightStartTime() < startTime && roleLoadInfo.getFightServer() != null && roleLoadInfo.getFightServer().equals(strs[0])) {
										// 恢复战斗中状态为正常状态
										roleLoadInfo.setInFight((byte)0);
									}
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error("ServerHandler : registerEnd is error", e);
		}
		
	}

	@Override
	public String execCommand(String command) {
		// TODO Auto-generated method stub
		return null;
	}

}
