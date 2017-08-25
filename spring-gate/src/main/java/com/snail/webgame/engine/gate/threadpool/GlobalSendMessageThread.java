package com.snail.webgame.engine.gate.threadpool;

import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.mina.common.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.engine.gate.util.IdentityMap;
import com.snail.webgame.engine.gate.util.MessageServiceManage;

public class GlobalSendMessageThread extends Thread {
	
	private static final Logger log =LoggerFactory.getLogger("logs");

	private ArrayBlockingQueue<byte[]> queue = new ArrayBlockingQueue<byte[]>(20000);
	
	private MessageServiceManage msgmgt;
	private boolean flag = false;
	
	public GlobalSendMessageThread(MessageServiceManage msgmgt) {
		this.msgmgt = msgmgt;
	}
	
	@Override
	public void run() {
		byte[] message = null;
		byte[] message1 = null;
		int index = 0;
		boolean doubleSend = false;
		
		while (!flag) {
			try {
				doubleSend = false;
				message = queue.poll(5, TimeUnit.SECONDS);
				
				if (message != null) {
					if (queue.peek() != null) {
						message1 = queue.poll(5, TimeUnit.SECONDS);
						msgmgt.setRoleId(message1, 0);
						msgmgt.setMessageType(msgmgt.getGateServerId(message1), message1);
						doubleSend = true;
					}
					
					msgmgt.setRoleId(message, 0);
					msgmgt.setMessageType(msgmgt.getGateServerId(message), message);
					
					Set<Entry<Integer,IoSession>> set = IdentityMap.entrySet();
					
					if (doubleSend) {
						for (Entry<Integer, IoSession> entry : set) {
							if (entry.getValue() != null && entry.getValue().isConnected()) {
								entry.getValue().write(message);
								entry.getValue().write(message1);
							}
						}
					} else {
						for (Entry<Integer, IoSession> entry : set) {
							if (entry.getValue() != null && entry.getValue().isConnected()) {
								entry.getValue().write(message);
							}
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (index++ > 20) {
					try {
						TimeUnit.MILLISECONDS.sleep(200);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
					index = 0;
				}
			}
		}
	}
	
	public boolean addMessage(byte[] message) {
		if (message == null) {
			return false;
		}

		try {
			boolean result = this.queue.add(message);
			return result;
		} catch (Exception e) {
			log.error("addMessage : add queue error", e);
			return false;
		}
	}
	
	public void cancel() {
		flag = true;
	}
}
