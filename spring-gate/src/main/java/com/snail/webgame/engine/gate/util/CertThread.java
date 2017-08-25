package com.snail.webgame.engine.gate.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.engine.gate.config.CertConfig;

public class CertThread extends Thread {

	private static final Logger log = LoggerFactory.getLogger("logs");

	public void run() {
		while (true) {
			if (System.currentTimeMillis() > CertConfig.getInstance().getTime()) {
				log.warn("Certificate expired,the system will stop running!!!! ");

				System.exit(1);
			}

			try {
				Thread.sleep(60000);
			} catch (InterruptedException e) {

				e.printStackTrace();
			}
		}

	}
}
