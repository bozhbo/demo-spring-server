package com.snail.client.web.robot;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.snail.client.web.control.ClientControl;
import com.spring.common.GameMessageType;

public class RobotThread extends Thread {
	
	private static final Log logger = LogFactory.getLog(RobotThread.class);
	
	private String serverName = "GameServer";
	private Map<String, String> serverMap = new HashMap<>();
	
	private int roles;
	private int offset;
	
	private int state = 0;
	
	public RobotThread(int offset, int roles) {
		this.offset = offset;
		this.roles = roles;
	}

	@Override
	public void run() {
		for (int i = 0; i < roles; i++) {
			ClientControl.netService.connectGame("127.0.0.1", 8088, serverName + (i + 1));
		}
		
		while (true) {
			if (ClientControl.roleService.errorCode.get() != 1) {
				logger.error("error " + ClientControl.roleService.errorCode.get());
				break;
			}
			
			if (state == 0) {
				state = 1;
				
				for (int i = 0; i < roles; i++) {
					if (!ClientControl.netService.isConnected(serverName + (i + 1))) {
						state = 0;
					}
				}
			} else if (state == 1) {
				for (int i = 0; i < roles; i++) {
					ClientControl.roleService.login("account" + offset + (i + 1), "account" + offset + (i + 1), serverName + (i + 1));
					serverMap.put("account" + offset + (i + 1), serverName + (i + 1));
				}
				
				state = 2;
			} else if (state == 2) {
				// 发送登录消息成功
				if (ClientControl.roleService.roleLoginMap.size() == roles) {
					state = 3;
				}
			} else if (state == 3) {
				// 全部登录成功
				for (int i = 0; i < roles; i++) {
					ClientControl.roleService.init(serverName + (i + 1));
				}
				
				state = 4;
			} else if (state == 4) {
				// 发送初始化消息成功
				if (ClientControl.roleService.roleIdMap.size() == roles) {
					state = 5;
				}
			} else if (state == 5) {
				// 全部初始化成功
				for (int i = 0; i < roles; i++) {
					ClientControl.roleService.fastStart(serverName + (i + 1));
				}
				
				state = 6;
			} else if (state == 6) {
				Set<Entry<Integer, RobotRoleInfo>> set = ClientControl.roleService.roleIdMap.entrySet();
				
				for (Entry<Integer, RobotRoleInfo> entry : set) {
					RobotRoleInfo roleInfo = entry.getValue();
					String account = ClientControl.roleService.roleLoginMap.get(roleInfo.getRoleId());
					
					if (roleInfo.getState() == 1) {
						// 进入房间成功(准备)
						ClientControl.roleService.sendCommonMsg(GameMessageType.GAME_CLIENT_PLAY_SEND_READY, "", serverMap.get(account));
						roleInfo.setState(2);
					} else if (roleInfo.getState() == 3) {
						// 轮到操作
						if (roleInfo.opCount >= 3) {
							ClientControl.roleService.sendCommonMsg(GameMessageType.GAME_CLIENT_PLAY_RECEIVE_GIVE_UP, "", serverMap.get(account));
						} else {
							ClientControl.roleService.sendCommonMsg(GameMessageType.GAME_CLIENT_PLAY_SEND_FOLLOW, "", serverMap.get(account));
							roleInfo.opCount++;
						}
						
						roleInfo.setState(2);
					} else if (roleInfo.getState() == 6) {
						roleInfo.opCount = 0;
						roleInfo.setState(7);
					}
				}
			}
			
			try {
				TimeUnit.MILLISECONDS.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
