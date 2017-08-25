package com.snail.webgame.engine.gate.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.PropertyConfigurator;
import org.dom4j.Document;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.engine.common.util.XMLUtil4DOM;
import com.snail.webgame.engine.gate.common.ConnectConfig;
import com.snail.webgame.engine.gate.common.FlashConfig;
import com.snail.webgame.engine.gate.common.LocalConfig;
import com.snail.webgame.engine.gate.common.SocketConfig;
import com.snail.webgame.engine.gate.filter.crypto.Arithmetic;
import com.snail.webgame.engine.gate.util.Util;

public class WebGameConfig {

	private static final Logger log = LoggerFactory.getLogger("logs");
	private static WebGameConfig config;

	private static String filePath1 = "/config/system-config.xml";
	private static String filePath2 = "/config/function-config.xml";

	private LocalConfig localConfig = new LocalConfig();

	private Map<String, ConnectConfig> connMap = new ConcurrentHashMap<String, ConnectConfig>();
	private FlashConfig flashConfig = new FlashConfig();
	private SocketConfig socketConfig = new SocketConfig();
	private int gameServerId;
	private HashMap<Integer, String> transmitMap = new HashMap<Integer, String>();
	private Arithmetic encryptType = null;
	private String licensePath = "";
	private int defenseFlag = 0;
	private int reconnectFlag = 0;
	private int disconnectNotify = 0;
	private int blackListTime = 0;
	
	private WebGameConfig() {
		loadConfig();
	}

	public synchronized static WebGameConfig getInstance() {
		if (config == null) {
			config = new WebGameConfig();
		}
		return config;
	}

	/**
	 * 载入配置文件
	 * 
	 * @param filePath
	 *            配置文件路径
	 */
	private void loadConfig() {
		InputStream systemFileIs = WebGameConfig.class.getResourceAsStream(filePath1);
		if (systemFileIs != null) {
			readConfig1(systemFileIs);
		} else {
			if (log.isErrorEnabled()) {
				log.error("System can not find " + filePath1 + ",system can not be inited!");
			}
		}

		InputStream functionFileIs = WebGameConfig.class.getResourceAsStream(filePath2);
		if (functionFileIs != null) {
			readConfig2(functionFileIs);
		} else {
			if (log.isErrorEnabled()) {
				log.error("System can not find " + filePath2 + ",system can not be inited!");
			}
		}

	}

	/**
	 * 读取配置文件
	 * 
	 * @param file
	 */
	private void readConfig1(InputStream is) {
		Document doc = XMLUtil4DOM.file2Dom(is);
		Element rootEle = null;
		if (doc != null) {
			rootEle = doc.getRootElement();
			String log4jPaht = rootEle.elementTextTrim("log4j-path");

			licensePath = rootEle.elementTextTrim("license-path");

			// 获得log4j的配置文件
			if (log4jPaht != null && log4jPaht.length() > 0 && log4jPaht.endsWith("log4j.properties")) {
				InputStream islog = WebGameConfig.class.getResourceAsStream(log4jPaht);
				Properties properties = new Properties();
				try {
					properties.load(islog);
				} catch (IOException e) {
					e.printStackTrace();
				}
				PropertyConfigurator.configure(properties);
			}

			gameServerId = Integer.valueOf(rootEle.elementTextTrim("game-server-id"));

			// 获得加密方法和密钥
			String flag = rootEle.elementTextTrim("encrypt-type");
			if (flag != null && flag.length() > 0) {
				encryptType = new Arithmetic(Integer.valueOf(flag));
			}

			String defenseFlagStr = rootEle.elementTextTrim("defense-flag");
			if (defenseFlagStr != null && defenseFlagStr.length() > 0) {
				defenseFlag = Integer.valueOf(defenseFlagStr);
			}

			String reconnect = rootEle.elementTextTrim("reconnect-flag");
			if (reconnect != null && reconnect.length() > 0) {
				reconnectFlag = Integer.parseInt(reconnect);
			}

			String disconnectNotifyStr = rootEle.elementTextTrim("disconnect-notify");
			if (disconnectNotifyStr != null && disconnectNotifyStr.length() > 0) {
				disconnectNotify = Integer.parseInt(disconnectNotifyStr);
			}

			String blackTime = rootEle.elementTextTrim("black-list-time");
			if (blackTime != null && !"".equals(blackTime))
				blackListTime = Integer.parseInt(blackTime) * 1000;

			List ConfigGServer = rootEle.elements("local-server");

			if (ConfigGServer != null && ConfigGServer.size() > 0) {
				for (int i = 0; i < ConfigGServer.size(); i++) {
					Element tempElement = (Element) ConfigGServer.get(i);
					String id = tempElement.elementTextTrim("gate-server-id");
					String localIP = tempElement.elementTextTrim("local-ip");
					String localPort = tempElement.elementTextTrim("local-port");

					String romateIP = tempElement.elementTextTrim("remote-ip");
					String romatePort = tempElement.elementTextTrim("remote-port");

					localConfig.setServerId(Integer.valueOf(id));

					localConfig.setLocalIP(localIP);
					localConfig.setLocalPort(localPort);
					localConfig.setRomateIP(romateIP);
					localConfig.setRomatePort(romatePort);
					localConfig.setGateServerId(Util.int2bytes(Integer.valueOf(id)));

				}
			}
			List configConnect = rootEle.elements("conncet-server");

			if (configConnect != null && configConnect.size() > 0) {
				for (int i = 0; i < configConnect.size(); i++) {

					Element tempElement = ((Element) configConnect.get(i));

					List list = tempElement.elements("server");

					if (list != null) {
						for (int j = 0; j < list.size(); j++) {
							Element tempElement1 = ((Element) list.get(j));
							ConnectConfig connConfig = new ConnectConfig();
							String serverName = tempElement1.elementTextTrim("name");
							String IP = tempElement1.elementTextTrim("ip");
							String port = tempElement1.elementTextTrim("port");

							connConfig.setServerName(serverName);
							connConfig.setServerIP(IP);
							connConfig.setServerPort(Integer.valueOf(port));

							connMap.put(serverName, connConfig);
						}
					}
				}
			}
			List configScoket = rootEle.elements("socket-option");
			if (configScoket != null && configScoket.size() > 0) {
				Element tempElement = (Element) configScoket.get(0);
				String receiveBuffer = tempElement.elementTextTrim("SocketReceiveBuffer");

				if (receiveBuffer != null && receiveBuffer.length() > 0) {
					socketConfig.setSocketReceiveBuffer(Integer.valueOf(receiveBuffer));
				}

				String sendBuffer = tempElement.elementTextTrim("SocketSendBuffer");
				if (sendBuffer != null && sendBuffer.length() > 0) {
					socketConfig.setSocketSendBuffer(Integer.valueOf(sendBuffer));
				}

				String keepAlive = tempElement.elementTextTrim("KeepAlive");
				if (keepAlive != null && keepAlive.length() > 0) {
					socketConfig.setKeepAlive(Boolean.valueOf(keepAlive));
				}
				String tcpNoDelay = tempElement.elementTextTrim("TcpNoDelay");
				if (tcpNoDelay != null && tcpNoDelay.length() > 0) {
					socketConfig.setTcpNoDelay(Boolean.valueOf(tcpNoDelay));
				}

				String[] idleTime = null;
				List temp = tempElement.elements("IdleTime");
				if (temp != null && temp.size() > 0) {
					idleTime = new String[2];
					Element temp1 = (Element) temp.get(0);
					idleTime[0] = temp1.elementTextTrim("state");
					idleTime[1] = temp1.elementTextTrim("Timeout");
					socketConfig.setIdleState(Integer.valueOf(idleTime[0]));
					socketConfig.setTimeout(Integer.valueOf(idleTime[1]));
				}

			}

			List ConfigGPool = rootEle.elements("flash-domain");

			if (ConfigGPool != null && ConfigGPool.size() > 0) {
				for (int i = 0; i < ConfigGPool.size(); i++) {
					Element tempElement = (Element) ConfigGPool.get(i);
					String domain = tempElement.elementTextTrim("domain");
					String port = tempElement.elementTextTrim("port");

					flashConfig.setDoamin(domain);
					flashConfig.setPort(port);
				}
			}

		}
	}

	/**
	 * 单位 秒
	 * 
	 * @return
	 */
	public int getReconnectFlag() {
		return reconnectFlag;
	}

	/**
	 * 断开通知时间 单位秒
	 * 
	 * @return
	 */
	public int getDisconnectNotify() {
		return disconnectNotify;
	}

	private void readConfig2(InputStream is) {
		Document doc = XMLUtil4DOM.file2Dom(is);
		Element rootEle = null;
		if (doc != null) {
			rootEle = doc.getRootElement();

			List ConfigTPool = rootEle.elements("message-transmit");

			if (ConfigTPool != null && ConfigTPool.size() > 0) {

				for (int i = 0; i < ConfigTPool.size(); i++) {
					Element tempElement = (Element) ConfigTPool.get(i);
					List temp = tempElement.elements("message");
					if (temp != null && temp.size() > 0) {
						for (int j = 0; j < temp.size(); j++) {
							Element temp1 = (Element) temp.get(j);
							String serverName = temp1.elementTextTrim("serverName");

							List msgTypes = temp1.elements("msgType");

							if (msgTypes != null && msgTypes.size() > 0) {
								for (int k = 0; k < msgTypes.size(); k++) {
									Element temp2 = (Element) msgTypes.get(k);

									String msgType = temp2.getTextTrim();
									if (msgType != null) {
										if (msgType.startsWith("0x")) {
											int type = Integer.parseInt(msgType.substring(2), 16);
											transmitMap.put(type, serverName);

										} else {
											transmitMap.put(Integer.valueOf((msgType)), serverName);
										}
									}

								}

							}

						}
					}
				}
			}
		}
	}

	/**
	 * 获得本地服务器配置文件
	 * 
	 * @return
	 */
	public LocalConfig getLocalConfig() {
		return localConfig;
	}

	/**
	 * 获得flash配置文件
	 * 
	 * @return
	 */
	public FlashConfig getFlashConfig() {
		return flashConfig;
	}

	/**
	 * 获得连接用配置文件
	 * 
	 * @return
	 */
	public Map<String, ConnectConfig> getConnectConfig() {
		return connMap;
	}

	public SocketConfig getScoketConfig() {

		return socketConfig;

	}

	/**
	 * 获得加密方式
	 * 
	 * @return
	 */
	public Arithmetic getEncryptType() {
		return encryptType;
	}

	public String getTramsitServer(int msgType) {
		return transmitMap.get(msgType);
	}

	public int getGameServerId() {
		return gameServerId;
	}

	public String getLicensePath() {
		return licensePath;
	}

	public int getDefenseFlag() {
		return defenseFlag;
	}

	public static String getRandomString(int length) {
		StringBuffer buffer = new StringBuffer("0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ");
		StringBuffer sb = new StringBuffer();
		Random r = new Random();
		int range = buffer.length();
		for (int i = 0; i < length; i++) {
			sb.append(buffer.charAt(r.nextInt(range)));
		}
		return sb.toString();
	}

	public int getBlackListTime() {
		return blackListTime;
	}

}
