package com.snail.webgame.engine.gate.filter.defence;

import java.net.InetSocketAddress;

import org.apache.mina.common.IoFilterAdapter;
import org.apache.mina.common.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.engine.gate.cache.BlacklistMap;
import com.snail.webgame.engine.gate.config.WebGameConfig;
import com.snail.webgame.engine.gate.util.MessageServiceManage;
/**
 * 服务器消息流量防御
 * @author leiqiang
 *
 */
public class DefenceIoFilter extends IoFilterAdapter {

	private MessageServiceManage msgmgt = new MessageServiceManage();
	private static final Logger log = LoggerFactory.getLogger(DefenceIoFilter.class.getName());
	private int defenceFlag;
	public DefenceIoFilter(int defenceFlag)
	{
		this.defenceFlag = defenceFlag;
	}
	@Override
	public void messageReceived(NextFilter nextFilter, IoSession session,
			Object message) throws Exception {
		// TODO Auto-generated method stub
		if(defenceFlag == 1 && message instanceof byte[])
		{
			String lastReqTimeStr = (String) session.getAttribute("lastReqTime");
			String freqNumStr = (String) session.getAttribute("freqNum");
			if(lastReqTimeStr == null || freqNumStr == null)
			{
				session.setAttribute("lastReqTime",String.valueOf(System.currentTimeMillis()));
				session.setAttribute("freqNum",String.valueOf(1));
			}
			else
			{
				int freqNum = Integer.valueOf(freqNumStr);
				long lastReqTime = Long.valueOf(lastReqTimeStr);
				long currTime = System.currentTimeMillis();
				long time = currTime-lastReqTime;
				if(time<=250)
				{
					freqNum = freqNum + 1;
					
					if(freqNum==30)
					{
						int msgType = msgmgt.getMessageType((byte[]) message);
						if(log.isWarnEnabled())
						{
							InetSocketAddress address = (InetSocketAddress) session.getRemoteAddress();
							String ip = address.getAddress().getHostAddress();
							log.warn("Server received a lot of messages sent by the client: MsgType ="
										+msgType+",ip="+ip+",num="+freqNum);
						}
					}
					else if(freqNum==300)
					{
						int msgType = msgmgt.getMessageType((byte[]) message);
						InetSocketAddress address = (InetSocketAddress) session.getRemoteAddress();
						String ip = address.getAddress().getHostAddress();
						if(log.isWarnEnabled())
						{
							log.warn("Close this session,Server received a lot of messages sent by the client: MsgType ="
											+msgType+",ip="+ip+",num="+freqNum);
						}
						
						//加入黑名单
						BlacklistMap.addBlack(ip, System.currentTimeMillis()+WebGameConfig.getInstance().getBlackListTime());
						
						session.removeAttribute("lastReqTime");
						session.removeAttribute("freqNum");
						session.close();
					}
					
					if(freqNum>30)
					{
						return;
					}
				}
				else
				{
					freqNum = 1;
				}
				session.setAttribute("lastReqTime",String.valueOf(currTime));
				session.setAttribute("freqNum",String.valueOf(freqNum));
			}
		}
		
		nextFilter.messageReceived(session, message);
	}
	
	@Override
	public void filterClose(NextFilter nextFilter, IoSession session) throws Exception {
		try {
			if (session != null) {
				if (session.getRemoteAddress() != null) {
					log.info("DefenceIoFilter : close session " + session.getRemoteAddress().toString());
				} else {
					log.info("DefenceIoFilter : close session ");
				}
			}
		} catch (Exception e) {
			
		}
		
		nextFilter.filterClose(session);
	}
}
