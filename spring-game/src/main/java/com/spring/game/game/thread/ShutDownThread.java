package com.snail.webgame.game.thread;

import java.util.concurrent.TimeUnit;

import org.epilot.ccf.server.acceptor.Server;
import org.epilot.ccf.threadpool.ThreadPoolManager;
import org.logicalcobwebs.proxool.ProxoolFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.engine.component.room.protocol.client.RoomClient;
import com.snail.webgame.engine.component.room.protocol.server.RoomServer;
import com.snail.webgame.game.common.GameValue;
import com.snail.webgame.game.config.GameConfig;
import com.snail.webgame.game.core.ChargeGameService;
import com.snail.webgame.game.core.GmccGameService;
import com.snail.webgame.game.core.MonitorGameService;
import com.snail.webgame.game.core.VoiceGameService;
import com.snail.webgame.game.dao.GameLogDAO;
import com.snail.webgame.game.protocal.rolemgt.service.RoleMgtService;
import com.snail.webgame.game.protocal.scene.sceneRefre.SceneRefreThread;
import com.snail.webgame.game.tool.acceptor.ToolAcceptor;

public class ShutDownThread extends Thread {

	private static final Logger logger = LoggerFactory.getLogger("logs");

	private Server server;
	private RoleLoginQueueThread loginQueueThread;
	private CheckGameThread checkGameThread;
	private RoomServer roomServer;

	public ShutDownThread(Server server, RoleLoginQueueThread loginQueueThread, CheckGameThread checkGameThread,RoomServer roomServer) {
		this.server = server;
		this.loginQueueThread = loginQueueThread;
		this.checkGameThread = checkGameThread;
		this.roomServer = roomServer;
	}

	public void run() {
		// 关闭已经连接到监听端口的所有连接
		server.unbind(GameConfig.getInstance().getServerName());
		if (logger.isWarnEnabled()) {
			logger.warn("Server unbind'" + GameConfig.getInstance().getServerName() + "' Listener....");
		}
		
		// 关闭命令端口
		if (roomServer != null) {
			roomServer.unbind();
		}

		// 关闭登录排队
		if (GameValue.GAME_LOGIN_QUEUE_FLAG == 1) {
			loginQueueThread.cancel();
		}

		// 连接维护线程
		checkGameThread.cancel();

		// 关闭计费链接
		if (GameValue.GAME_VALIDATEIN_FLAG == 1) {
			ChargeGameService.disconnect();
		}
		
		// 关闭语音
		if (GameValue.GAME_VOICE_FLAG == 1) {
			VoiceGameService.disconnect();
		}

		// GMCC连接关闭
		if (GameValue.GAME_GMCC_FLAG == 1) {
			GmccGameService.disconnect();
		}
		
		// 极效工具服务关闭
		if (GameValue.GAME_TOOL_FLAG == 1) {
			ToolAcceptor.disconnect();
		}

		// Monitor连接关闭
		if (GameValue.GAME_MONITOR_FLAG == 1) {
			MonitorGameService.disconnect();
		}
		
		//定时线程关闭
		ScheduleThread.shutdown();
		
		try {
			TimeUnit.SECONDS.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// 更新在线角色,离线时间
		RoleMgtService.allUserLoginOut();

		// 关闭PVP战斗管理服务器
		RoomClient.shutdown(GameConfig.getInstance().getRoomId());
		
		// 剩余日志全部入库
		GameLogDAO.getInstance().shutDown();
		
		// 关闭线程池
		ThreadPoolManager.getInstance().close();
		ScreanNotifyThread.getInstance().close();
		SceneRefreThread.getInstance().close();
		if (logger.isWarnEnabled()) {
			logger.warn("Server is shut down all processing  thread pool...");
		}
		
		try {
			TimeUnit.SECONDS.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		GameConfig.getInstance().stopWriteLog();
			
		// 关闭数据库连接
		ProxoolFacade.shutdown();
		if (logger.isWarnEnabled()) {
			logger.warn("System is shut down the database connection.....");
		}

		if (logger.isWarnEnabled()) {
			logger.warn("Normal closure of the server!");
		}
	}
}
