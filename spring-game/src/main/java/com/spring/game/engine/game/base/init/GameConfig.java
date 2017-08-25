package com.snail.webgame.engine.game.base.init;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.log4j.PropertyConfigurator;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.engine.game.base.spring.BaseBeanFactory;
import com.snail.webgame.engine.game.thread.GameThreadPool;
import com.snail.webgame.engine.net.processor.control.BaseProcessor;

/**
 * 类介绍:游戏启动配置加载类
 * 先于游戏启动前加载
 *
 * @author zhoubo
 * @version 1.0.0
 * @since 2016年1月21日
 */
public class GameConfig {
	
	/**
	 * 项目路径
	 */
	public static String basePath = null;
	
	/**
	 * 服务器IP
	 */
	public static String serverIp = null;
	
	/**
	 * 服务器端口
	 */
	public static int serverPort = 0;
	
	/**
	 * 服务器名称
	 */
	public static String serverName = "";
	
	/**
	 * 服务器Socket线程数
	 */
	public static int socketThreads = 1;
	
	/**
	 * 消息接收线程池
	 */
	private static GameThreadPool processorThreadPool = null;
	
	/**
	 * 业务处理线程池
	 */
	private static GameThreadPool execThreadPool = null;
	
	/**
	 * 全局处理控制类
	 */
	private static BaseProcessor controlProcessor = null;
	
	/**
	 * system-config.xml最后修改日期
	 */
	public static long configLastModifiedTime = 0;

	public static boolean init() {
		basePath = GameConfig.class.getResource("/").getPath();

		if (basePath == null) {
			return false;
		}
		
		if (!loadLog()) {
			return false;
		}
		
		if (!configLoad()) {
			return false;
		}
	
		BaseBeanFactory.init("/config/spring.xml");
		
		controlProcessor = new BaseProcessor();
		
		return true;
	}
	
	public static boolean loadLog() {
		try {
			InputStream is = GameConfig.class.getResourceAsStream("/config/log4j.properties");
			Properties properties = new Properties();

			try {
				properties.load(is);
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			} finally {
				if (is != null) {
					try {
						is.close();
					} catch (IOException e) {
						e.printStackTrace();
						return false;
					}
				}
			}

			PropertyConfigurator.configure(properties);
		} catch (Exception e) {
			return false;
		}

		return true;
	}
	
	/**
	 * 加载服务器配置文件
	 * 
	 * @return boolean true-成功 -false-失败
	 */
	private static boolean configLoad() {
		Logger logger = LoggerFactory.getLogger(GameConfig.class);
		
		try {
			File file = new File(basePath + File.separator + "config" + File.separator + "system-config.xml");
			
			if (file.exists()) {
				// 记录文件修改日期用于重新加载文件的条件判断
				configLastModifiedTime = file.lastModified();
			} else {
				System.out.println(basePath + File.separator + "config" + File.separator + "system-config.xml" + " is not exist");
				return false;
			}
			
			Document doc = file2Dom(GameConfig.class.getResourceAsStream("/config/system-config.xml"));
			Element rootEle = null;

			if (doc != null) {
				rootEle = doc.getRootElement();
				
				serverIp = rootEle.element("server-ip").getText();
				serverPort = Integer.parseInt(rootEle.element("server-port").getText());
				socketThreads = Integer.parseInt(rootEle.element("socket-threads").getText());
				serverName = rootEle.element("server-name").getText();

				Element proxyThreadPool = rootEle.element("ProcessHandlerThreadPool");

				if (proxyThreadPool != null) {
					int coreThreads = Integer.parseInt(proxyThreadPool.element("coreThreads").getText());
					int maxThreads = Integer.parseInt(proxyThreadPool.element("maxThreads").getText());
					int keepLive = Integer.parseInt(proxyThreadPool.element("keepAlive").getText());
					int queueSize = Integer.parseInt(proxyThreadPool.element("queueSize").getText());
					
					processorThreadPool = new GameThreadPool("ProcessHandlerThreadPool", coreThreads, maxThreads, keepLive, queueSize, new ThreadPoolExecutor.CallerRunsPolicy());
				}
			} else {
				return false;
			}
		} catch (Exception e) {
			logger.error("load thread pool failed", e);
			return false;
		}
		
		logger.info("load thread pool end");

		return true;
	}
	
	public static Document file2Dom(InputStream is) {
		Document dom = null;
		BufferedInputStream bis = null;
		try {
			SAXReader sax = new SAXReader();
			sax.setEncoding("UTF-8");
			bis = new BufferedInputStream(is);
			dom = sax.read(bis);
		} catch (DocumentException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bis != null) {
					bis.close();
				}
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return dom;
	}

	public static GameThreadPool getProcessorThreadPool() {
		return processorThreadPool;
	}

	public static BaseProcessor getControlProcessor() {
		return controlProcessor;
	}

	public static GameThreadPool getExecThreadPool() {
		return execThreadPool;
	}
	
	
}
