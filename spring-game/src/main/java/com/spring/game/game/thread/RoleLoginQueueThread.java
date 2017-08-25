package com.snail.webgame.game.thread;

import org.apache.mina.common.IoSession;
import org.epilot.ccf.config.Resource;
import org.epilot.ccf.core.protocol.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.engine.common.Flag;
import com.snail.webgame.engine.common.ServerName;
import com.snail.webgame.game.cache.RoleLoginQueueInfoMap;
import com.snail.webgame.game.cache.ServerMap;
import com.snail.webgame.game.common.GameMessageHead;
import com.snail.webgame.game.common.GameValue;
import com.snail.webgame.game.common.RoleLoginQueueInfo;
import com.snail.webgame.game.common.util.Command;
import com.snail.webgame.game.protocal.rolemgt.service.RoleMgtService;
import com.snail.webgame.game.protocal.rolemgt.verify.UserVerifyResp;

public class RoleLoginQueueThread extends Thread {
	private static final Logger logger = LoggerFactory.getLogger("logs");

	public void run() {
		while (!Thread.currentThread().isInterrupted()) {
			try {
				if(GameValue.GAME_LOGIN_QUEUE_FLAG == 1 && GameValue.GAME_LOGIN_ONLINE_NUM > 0) {
					synchronized (Flag.OBJ_LOGIN_QUEUE) {
						// 可进数量
						int permitLoginNum = RoleLoginQueueInfoMap.getPermitNum();
						
						// 如果有空位
						if(permitLoginNum > 0){
							// 如果有人排队
							RoleLoginQueueInfo queueInfo;
							
							for (int i = 0; i < permitLoginNum; i++) {
								queueInfo = RoleLoginQueueInfoMap.removeFirst();
								
								if(queueInfo == null){
									break;
								}
								
								Message message = new Message();
								GameMessageHead head = queueInfo.getHead();
								head.setMsgType(Command.USER_VERIFY_ROLE_RESP);
								message.setHeader(head);
								UserVerifyResp resp = RoleMgtService.getUserVerifyResp(queueInfo.getAccount(), head);
								message.setBody(resp);
								
								IoSession session = ServerMap.getServerSession(ServerName.GATE_SERVER_NAME + "-" + head.getUserID1());

								if (session != null && session.isConnected()){
									session.write(message);
								}
	
								if (logger.isInfoEnabled()){
									logger.info(Resource.getMessage("game", "GAME_ROLE_INFO_29")+",account = " + queueInfo.getAccount());	
								}
								
								RoleLoginQueueInfoMap.addLoginList(queueInfo.getAccount());
							}
						}
					}
				}
			} catch (Exception e) {
				if(logger.isErrorEnabled()){
					e.printStackTrace();
				}
			} finally {
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void cancel() {
		interrupt();
	}
}
