package com.snail.webgame.game.thread;

import java.io.File;
import java.sql.Timestamp;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.game.configdb.ConfigXmlFileList;
import com.snail.webgame.game.configdb.ConfigXmlService;


public class CheckConfigThread extends Thread {

	private volatile boolean cancel = false;
	
	private static final Logger log = LoggerFactory.getLogger("logs");
	public static HashMap<String,Long> modifyMap = new HashMap<String,Long>();

	public void run() {
		while (!cancel) {
			String localStr[] = ConfigXmlFileList.localXmlStr;
			// 直接读取包里面的xml文件
			for (int i = 0; i < localStr.length; i++) 
			{
				try
				{
					String xmlName = localStr[i].replace(".xml", "");
					
					String filePath =System.getProperty("user.dir") + File.separator + "config" + File.separator +"xml" + File.separator + xmlName + ".xml";
					File file = new File(filePath);
					long modifyTime = file.lastModified();
					if(modifyMap.get(xmlName) != null && modifyMap.get(xmlName) != modifyTime)
					{
						if (ConfigXmlService.loadGameConfigFromLocal(xmlName,true)) {
							if (log.isInfoEnabled()) {
								log.info("CheckConfigThread Modify Load local [" + localStr[i] + "] loads successful!,time="+new Timestamp(System.currentTimeMillis()));
							}
							modifyMap.put(xmlName, modifyTime);
						} else {
							// 装载包中的XML失败
							if (log.isErrorEnabled()) {
								log.error("CheckConfigThread Modify Load local [" + localStr[i] + "] loads failure!,time="+new Timestamp(System.currentTimeMillis()));
							}
						}
					}
				}
				catch(Exception e)
				{
					log.error("CheckConfigThread error",e);
				}
			}
			
			try {
				Thread.sleep(300000);
			} catch (InterruptedException e) {

				e.printStackTrace();
			}
		}
	}

	

	public static HashMap<String, Long> getModifyMap() {
		return modifyMap;
	}

	public static void setModifyMap(HashMap<String, Long> modifyMap) {
		CheckConfigThread.modifyMap = modifyMap;
	}

	public void cancel() {
		cancel = true;
	}
}
