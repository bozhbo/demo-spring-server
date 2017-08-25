package com.snail.webgame.game.main;

import java.sql.SQLException;

import org.epilot.ccf.server.acceptor.Server;
import org.epilot.ccf.threadpool.ThreadPoolManager;

import com.snail.webgame.engine.component.room.protocol.server.RoomServer;
import com.snail.webgame.game.charge.text.util.TextUtil;
import com.snail.webgame.game.common.GameValue;
import com.snail.webgame.game.config.GameConfig;
import com.snail.webgame.game.config.LoadBlackAccountList;
import com.snail.webgame.game.config.LoadWordList;
import com.snail.webgame.game.config.LoadWriteAccountList;
import com.snail.webgame.game.core.ChargeGameService;
import com.snail.webgame.game.core.GmccGameService;
import com.snail.webgame.game.core.MonitorGameService;
import com.snail.webgame.game.core.VoiceGameService;
import com.snail.webgame.game.init.GameInit;
import com.snail.webgame.game.pvp.service.PvpFightService;
import com.snail.webgame.game.thread.CheckConfigThread;
import com.snail.webgame.game.thread.CheckGameThread;
import com.snail.webgame.game.thread.PatchLoadThread;
import com.snail.webgame.game.thread.PushServerSendMsgThread;
import com.snail.webgame.game.thread.RoleLoginQueueThread;
import com.snail.webgame.game.thread.ScheduleThread;
import com.snail.webgame.game.thread.SendServerMsgThread;
import com.snail.webgame.game.thread.ShutDownThread;
import com.snail.webgame.game.tool.acceptor.ToolAcceptor;

public class GameAsMain {
	
	public static void main(String[] args) throws SQLException {
		System.out.println("[SYSTEM] Start server,please wait!!!");
		loadGameConfig();
		if (!GameInit.instance().init()) {
			System.out.println("[SYSTEM] Start server,GameInit Fall!!!!");
			System.exit(0);
		}
		
		start();
		System.out.println("[SYSTEM] Start server successful!");
		
	}
	
	public static void loadGameConfig() {
		GameConfig.getInstance();
		new LoadWordList().load();
		
		if(GameValue.ACCOUNT_FLAG == 1)
		{
			new LoadBlackAccountList().load();
			new LoadWriteAccountList().load();
		}
	}

	public static void start() {
		Server server = new Server();
		server.start();

		// 定时线程
		ScheduleThread.start();

		// 连接维护信息
		CheckGameThread checkGameThread = new CheckGameThread();
		checkGameThread.setName("CheckGameThread");
		checkGameThread.start();

		// 排队登录
		RoleLoginQueueThread loginQueueThread = new RoleLoginQueueThread();
		loginQueueThread.setName("登录排队");
		loginQueueThread.start();

		// 连接计费
		if (GameValue.GAME_VALIDATEIN_FLAG == 1) {
			ChargeGameService.connect();
		}
		
		// 连接语音
		if (GameValue.GAME_VOICE_FLAG == 1) {
			VoiceGameService.connect();
		}

		if (GameValue.GAME_GMCC_FLAG == 1) {
			// 连接 GMCC
			GmccGameService.connect();
		}

		if (GameValue.GAME_TOOL_FLAG == 1) {
			// 运营工具
			ToolAcceptor.startAcceptor();
		}

		if (GameValue.GAME_MONITOR_FLAG == 1) {
			// 连接 Monitor
			MonitorGameService.connect();
			MonitorGameService.beConnect();
		}
		
		RoomServer roomServer = new RoomServer();
		
		try {
			// 连接PVP战斗管理服务器
			PvpFightService.connectRoomManageServer();
			
			// 启动PVP战斗监听端口
			PvpFightService.startFightListener(roomServer);
		} catch (Exception e1) {
			System.out.println("[SYSTEM] RoomManage server connect error, " + e1.getMessage());
		}

		SendServerMsgThread ssmt = new SendServerMsgThread();
		ssmt.start();
		
		PushServerSendMsgThread psmt = new PushServerSendMsgThread();
		psmt.start();
		
//		ExecuteLogsThread executeLogsThread = new ExecuteLogsThread();
//		executeLogsThread.start();
		GameConfig.getInstance().startLogThread();

		// 启动连接到苹果充值中转服务器
		if (GameConfig.getInstance().getAndroid_Ios_Flag() == 2) {
			// 苹果服务器，添加充值中转服
			TextUtil.startAppChargeThread(GameConfig.getInstance().getAppChargeIP(), GameConfig.getInstance().getAppChargePort(), ThreadPoolManager.getInstance().Pool("webgame"));
		}
		
		// 检测公会聊天房间
		if (GameValue.GAME_VOICE_FLAG == 1) {
			VoiceGameService.serverStartCreateClubRoom();
		}
		
		// 启动补丁包检查线程
		Thread plt = new Thread(new PatchLoadThread());
		plt.setDaemon(true);
		plt.start();
		
		//动态更新配置文件
		CheckConfigThread checkConfigThread = new CheckConfigThread();
		checkConfigThread.start();
		
		
		Runtime.getRuntime().addShutdownHook(new ShutDownThread(server, loginQueueThread, checkGameThread, roomServer));
	}
}
