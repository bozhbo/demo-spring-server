package com.snail.webgame.game.thread;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.mina.common.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.engine.component.push.PushObj;
import com.snail.webgame.engine.component.push.PushService;
import com.snail.webgame.game.common.GameValue;
import com.snail.webgame.game.push.PushService111;

/**
 * 异步推送
 * 
 * @author hongfm
 *
 */
public class PushServerSendMsgThread extends Thread {

	private static final Logger logger = LoggerFactory.getLogger("logs");

	private static BlockingQueue<PushObj> queue = new LinkedBlockingQueue<PushObj>(10000);

	public PushServerSendMsgThread() {
		this.setName("PushServerSendMsgThread");
	}
	
	
	public static BlockingQueue<PushObj> getQueue() {
		return queue;
	}

	@Override
	public void run()
	{
		PushObj pushObj = null;
		
		int i = 0;
		while (true) 
		{
			try 
			{
				pushObj = queue.take();

				String token = "";
				
				if(pushObj.getToken() != null && pushObj.getToken().size() > 0)
				{
					token = pushObj.getToken().get(0);
				}
				
				if(GameValue.PUSH_SERVER_FLAG == 1)
				{
					IoSession ioSession = PushService111.getIoSession();
					if (ioSession != null && ioSession.isConnected()) 
					{
						PushService.sendMsgToSingleToken(ioSession, pushObj.getClient(),pushObj.getAppPackage(), 
								token, pushObj.getTitle(), pushObj.getMessage(),1);
					}
					else
					{
						if (logger.isErrorEnabled())
						{
							logger.error("sendPushMessage ioSession is not available!");
						}
					}
				}
			} catch (Exception e) {
				if (logger.isErrorEnabled())
				{
					logger.error("SendServerMsgThread, takeMessage error", e);
				}
			} finally {
				pushObj = null;
				
				if (i++ > 500) {
					try {
						Thread.sleep(3000);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
					i = 0;
				}
			}
		}
	}
	
	/**
	 * 添加消息
	 * 
	 * @param sendMessageInfo
	 */
	public static void addMessage(PushObj pushObj) {
		try {
			queue.add(pushObj);
		} catch (Exception e) {
			logger.error("SendServerMsgThread addMessage error", e);
		}
	}
}
