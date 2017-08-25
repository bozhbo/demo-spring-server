package org.epilot.ccf.server.send;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.mina.common.IoSession;
import org.epilot.ccf.threadpool.ThreadPoolManager;

public class ExecutorSend {
	
	private final Executor executor;
	private static ExecutorSend send;
	
	private ExecutorSend()
	{
		if( ThreadPoolManager.getInstance().Pool("sendPool") == null)
		{
			executor=new ThreadPoolExecutor(
				50, 50,
	            0, TimeUnit.SECONDS,
	            new LinkedBlockingQueue<Runnable>(100),
	            new ThreadPoolExecutor.CallerRunsPolicy());
		}
		else
		{
			executor = ThreadPoolManager.getInstance().Pool("sendPool");
		}
	}
	public static synchronized ExecutorSend getInstance()
	{
		if(send == null)
		{
			send = new ExecutorSend();
		}
		return send;
	}
	
	
	public void write(IoSession session,Object obj)
	{
		executor.execute(new SendMsgRunnable(session,obj));
	}
	
	
}
