package com.snail.mina.protocol.thread;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.mina.protocol.config.RoomMessageConfig;

public class ServerStateThread extends Thread {
	private static Logger logger = LoggerFactory.getLogger("room");
	
	private static AtomicInteger aiInput = new AtomicInteger();
	private static AtomicInteger aiOutput = new AtomicInteger();
	private static volatile boolean flag = false;
	
	@Override
	public void run() {
		while (true) {
			if (!flag) {
				flag = true;
				aiInput.set(0);
				aiOutput.set(0);
			}
			
			try {
				TimeUnit.SECONDS.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			flag = false;
			int input = aiInput.get();
			int output = aiOutput.get();
			
			logger.warn("server input messages = " + input + " in 20 seconds");
			logger.warn("server output messages = " + output + " in 20 seconds");
			
			RoomMessageConfig.inputCount = (input / 20);
			RoomMessageConfig.outputCount = (output / 20);
			
			try {
				TimeUnit.SECONDS.sleep(60);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void addInput() {
		if (flag) {
			aiInput.incrementAndGet();
		}
	}
	
	public static void addOutput() {
		if (flag) {
			aiOutput.incrementAndGet();
		}
	}
}
