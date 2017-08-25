package com.snail.webgame.game.core;

import com.snail.webgame.engine.component.info.monitor.server.MonitorService;
import com.snail.webgame.game.config.GameConfig;

public class MonitorGameService {

	private static MonitorService service;

	/**
	 * 连接Monitor
	 */
	public static void connect() {
		if(service == null){
			service = new MonitorService();
		}
		GameConfig gameConfig = GameConfig.getInstance();
		service.connectMonitorServer(gameConfig.getMonitorId(), gameConfig.getMonitorIP(), gameConfig.getMonitorPort(),
				gameConfig.getMonitorLog(), new MonitorInfoHandlerImpl());
	}
	
	/**
	 * 启动监控服务器，被连接
	 */
	public static void beConnect() {
		if(service == null){
			service = new MonitorService();
		}
		service.startMonitorServer(null, GameConfig.getInstance().getMonitorServerIp(), GameConfig.getInstance().getMonitorServerPort(), GameConfig.getInstance().getMonitorLog(), new MonitorInfoServerHandlerImpl());
	}
	
	

	public static void disconnect() {
		service.shutdownMonitorServer();
	}

	public static MonitorService getMonitorService() {
		return service;
	}
}
