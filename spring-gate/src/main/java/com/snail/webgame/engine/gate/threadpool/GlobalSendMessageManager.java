package com.snail.webgame.engine.gate.threadpool;

import com.snail.webgame.engine.gate.util.MessageServiceManage;


public class GlobalSendMessageManager {
	private static GlobalSendMessageThread[] globalSendMessageThread = new GlobalSendMessageThread[4];
	
	private static int threads = 4;
	private static int processorDistributor = 0;
	private static int state = 0;
	
	public synchronized static void init(MessageServiceManage msgmgt) {
		for (int i = 0; i < threads; i++) {
			globalSendMessageThread[i] = new GlobalSendMessageThread(msgmgt);
			globalSendMessageThread[i].start();
		}
		
		state = 1;
	}
	
	public static GlobalSendMessageThread nextProcessor() {
        if (processorDistributor == Integer.MAX_VALUE) {
            processorDistributor = Integer.MAX_VALUE % globalSendMessageThread.length;
        }

        return globalSendMessageThread[processorDistributor++ % globalSendMessageThread.length];
    }
	
	public static void destroy() {
		if (state == 0) {
			return;
		}
		
		for (GlobalSendMessageThread globalSendMessageThread2 : globalSendMessageThread) {
			globalSendMessageThread2.cancel();
		}
	}
}
