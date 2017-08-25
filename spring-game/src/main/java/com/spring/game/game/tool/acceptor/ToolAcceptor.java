package com.snail.webgame.game.tool.acceptor;

import com.snail.webgame.engine.component.tool.facade.ToolService;
import com.snail.webgame.engine.component.tool.facade.ToolServiceHandler;
import com.snail.webgame.game.config.GameConfig;
import com.snail.webgame.game.tool.handler.ToolServiceHandlerImpl;

/**
 * Tool简化启动
 * @author caowl
 */
public class ToolAcceptor {

	private static ToolService toolService;

	/**
	 * 启动ToolService server
	 * @return
	 */
	public static boolean startAcceptor() {

		GameConfig config = GameConfig.getInstance();
		String ip = config.getToolIp();
		int port = config.getToolPort();
		String level = config.getToolLog();
		String gameServerId = "" + config.getGameServerId();

		toolService = ToolService.getToolService();
		ToolServiceHandler handler = new ToolServiceHandlerImpl();
		boolean success = toolService.startToolServer(ip, port, gameServerId, level);
		if (success) {
			toolService.register(handler);
			return true;
		}
		return false;
	}

	public static void disconnect() {
		toolService.shutdown();
	}

}
