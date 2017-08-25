package org.epilot.ccf.server.send;

import org.apache.mina.common.IoSession;

public class SendMsgRunnable implements Runnable {

	private IoSession session;
	private Object obj;
	public SendMsgRunnable(IoSession session,Object obj)
	{
		this.session = session;
		this.obj = obj;
	}
	public void run()
	{
		session.write(obj);
	}
}
