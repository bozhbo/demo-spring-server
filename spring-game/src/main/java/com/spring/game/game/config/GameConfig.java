package com.snail.webgame.game.config;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.log4j.PropertyConfigurator;
import org.dom4j.Document;
import org.dom4j.Element;

import com.snail.webgame.engine.common.util.XMLUtil4DOM;
import com.snail.webgame.engine.component.room.protocol.info.RoomServerInfo;
import com.snail.webgame.game.common.GameValue;
import com.snail.webgame.game.dao.GameLogDAO;
import com.snail.webgame.game.thread.ExecuteLogsThread;
import com.snail.webgame.game.thread.TempExecuteLogsThread;

public class GameConfig {

	private static GameConfig config = null;
	private static String configPath = "/config/system-config.xml";

	private String serverName;
	private int gameServerId;
	private int gameType;
	private String gameName;

	private String toolIp;
	private int toolPort;
	private String toolLog;

	private String gmccIp;
	private int gmccPort;
	private String gmccLog;

	private String monitorIP;
	private int monitorPort;
	private String monitorId;
	private String monitorLog;
	
	private int monitorServerPort;// 监控服务器端口
	private String monitorServerIp;
	
	private String chargeIp;
	private int chargePort;
	private String chargeLog;
	
	private String voiceIp;
	private int voicePort;
	private String voiceLog;
	private String voiceAccount;
	private String voicePass;
	private String voiceKey;
	
	private String serverIp;
	private int innerPort;
	private int fightPort;
	
	private String roomId;
	private String roomIp;
	private int roomPort;
	
	// APP充值
	private String sAccountTypeId;
	
	private List<RoomServerInfo> roomServerList = new CopyOnWriteArrayList<RoomServerInfo>();
	
	private String pushIP;//推送服务器IP
	private int pushPort;//推送服务器端口
	
	private String appChargeIP;//苹果充值服务器IP
	private int appChargePort;//苹果充值服务器端口
	
	
	private int worldType = 0;// 聊天字类型 1-中国 2-北美
	private int loginFreq = 0;// 玩家登陆间隔单位（毫秒）小于100不做限制
	private int loginFreqNum = 0;// 玩家登陆间隔数量默认0不做限制
	private String gateServerName;
	private int indulgeFlag = 0;// 防沉迷是否开启 0-关闭 1-打开
	private int indulgeTime = 0;// 防沉迷时间
	private int configXmlFlag = 1;// 从数据库读取配置开关 0-关闭 1-打开
	private int Android_Ios_Flag = 1;//1-混服,2-苹果服务器,3-其它
	
	private String version = "0.0.2"; // 服务器版本
	
	private static int processorDistributor = 0;
	
	private static ExecuteLogsThread[] logThread = new ExecuteLogsThread[1];
	private static TempExecuteLogsThread[] stringLogThread = new TempExecuteLogsThread[3];

	private GameConfig() {
		init();
	}

	public synchronized static GameConfig getInstance() {
		if (config == null) {
			config = new GameConfig();
		}
		return config;
	}

	public synchronized static GameConfig getInstance(String path) {
		if (config == null) {
			configPath = path;
			config = new GameConfig();

		}
		return config;
	}

	@SuppressWarnings("unchecked")
	private void init() {
		Document doc = XMLUtil4DOM.file2Dom(GameConfig.class.getResourceAsStream(configPath));
		
		Element rootEle = null;
		if (doc != null) {
			rootEle = doc.getRootElement();
			String log4jPaht = rootEle.elementTextTrim("log4j-path");
			if (log4jPaht != null && log4jPaht.length() > 0 && log4jPaht.endsWith("log4j.properties")) {
				InputStream is = GameConfig.class.getResourceAsStream(log4jPaht);
				Properties properties = new Properties();
				try {
					properties.load(is);
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					if (is != null) {
						try {
							is.close();
						} catch (IOException e) {
						}
					}
				}
				
				try{
					//检查日志文件
					File file = new File("logs");
					if(!file.exists() && !file.isDirectory())
					{
						file.mkdir();
						
						File file1 = new File("logs/log");
						if(!file1.exists())
						{
							try {
								file1.createNewFile();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
					
					//检查日志文件
					File file2 = new File("ccfLogs");
					if(!file2.exists() && !file2.isDirectory())
					{
						file2.mkdir();
						
						File file22 = new File("ccfLogs/log");
						if(!file22.exists())
						{
							try {
								file22.createNewFile();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				}catch(Exception e){
					e.printStackTrace();
				}
				
				PropertyConfigurator.configure(properties);
			}

			serverIp = rootEle.elementTextTrim("server-ip");
			serverName = rootEle.elementTextTrim("server-name");
			gameType = Integer.valueOf(rootEle.elementTextTrim("gameType"));
			gameServerId = Integer.valueOf(rootEle.elementTextTrim("game-server-id"));
			gameName = rootEle.elementTextTrim("game-server-name");
			gateServerName = rootEle.elementTextTrim("gate-server-name");

			// 极效IP和端口
			GameValue.GAME_TOOL_FLAG = Integer.valueOf(rootEle.elementTextTrim("tool-flag"));
			if(GameValue.GAME_TOOL_FLAG == 1)
			{
				toolIp = rootEle.elementTextTrim("tool-ip");
				toolPort = Integer.valueOf(rootEle.elementTextTrim("tool-port"));
				toolLog = rootEle.elementTextTrim("tool-log");
			}
			
			// GMCC的IP和端口
			GameValue.GAME_GMCC_FLAG = Integer.valueOf(rootEle.elementTextTrim("gmcc-flag"));
			if(GameValue.GAME_GMCC_FLAG == 1)
			{
				gmccIp = rootEle.elementTextTrim("gmcc-ip");
				gmccPort = Integer.valueOf(rootEle.elementTextTrim("gmcc-port"));
				gmccLog = rootEle.elementTextTrim("gmcc-log");
			}
			
			// NX监控服务器IP和端口
			GameValue.GAME_MONITOR_FLAG = Integer.valueOf(rootEle.elementTextTrim("monitor-flag"));
			if(GameValue.GAME_MONITOR_FLAG == 1)
			{
				monitorIP = rootEle.elementTextTrim("monitor-ip");
				monitorPort = Integer.valueOf(rootEle.elementTextTrim("monitor-port"));
				monitorId = rootEle.elementTextTrim("monitor-id");
				monitorLog = rootEle.elementTextTrim("monitor-log");
			}
			
			//启动监控服务器，被远程连接
			String monitorServerPortStr = rootEle.elementTextTrim("monitor-server-port");
			if (monitorServerPortStr != null && monitorServerPortStr.length() > 0) {
				monitorServerPort = Integer.valueOf(monitorServerPortStr);
			}
			String monitorIpStr = rootEle.elementTextTrim("monitor-server-ip");
			if (monitorIpStr != null && monitorIpStr.length() > 0) {
				monitorServerIp = monitorIpStr;
			}

			// 计费登录器IP和端口
			GameValue.GAME_VALIDATEIN_FLAG = Integer.valueOf(rootEle.elementTextTrim("charge-flag"));
			if(GameValue.GAME_VALIDATEIN_FLAG == 1)
			{
				chargeIp = rootEle.elementTextTrim("charge-ip");
				chargePort = Integer.valueOf(rootEle.elementTextTrim("charge-port"));
				chargeLog = rootEle.elementTextTrim("charge-log");
			}
			
			//语音服务器器IP和端口
			GameValue.GAME_VOICE_FLAG = Integer.valueOf(rootEle.elementTextTrim("voice-flag"));
			if (GameValue.GAME_VOICE_FLAG == 1) {
				voiceIp = rootEle.elementTextTrim("voice-ip");
				voicePort = Integer.valueOf(rootEle.elementTextTrim("voice-port"));
				voiceLog = rootEle.elementTextTrim("voice-log");
				voiceAccount = rootEle.elementTextTrim("voice-account");
				voicePass = rootEle.elementTextTrim("voice-pass");
				voiceKey = rootEle.elementTextTrim("voice-key");
			}
			
			// 推送服务器IP和端口
			GameValue.PUSH_SERVER_FLAG = Integer.valueOf(rootEle.elementTextTrim("push-server-flag"));
			if(GameValue.PUSH_SERVER_FLAG == 1)
			{
				pushIP = rootEle.elementTextTrim("push-ip");
				pushPort = Integer.valueOf(rootEle.elementTextTrim("push-port"));
			}
			
			if (rootEle.elementTextTrim("app-charge-ip") != null) {
				appChargeIP = rootEle.elementTextTrim("app-charge-ip");
				appChargePort = Integer.valueOf(rootEle.elementTextTrim("app-charge-port"));
			}
			
			// 此配置用于内部监听和对战斗服务器数据通信
			String innerPortStr = rootEle.elementTextTrim("server-inner-port");			
			if (innerPortStr != null && !"".equals(innerPortStr)) {
				innerPort = Integer.valueOf(innerPortStr);
			}
			
			String fightPortStr = rootEle.elementTextTrim("fight-inner-port");
			
			if (fightPortStr != null && !"".equals(fightPortStr)) {
				fightPort = Integer.valueOf(fightPortStr);
			}
			
			// 连接远程竞技场PVP跨服管理服务器 IP和端口
			roomId = rootEle.elementTextTrim("room-id");
			roomIp = rootEle.elementTextTrim("room-ip");
			String roomPortStr = rootEle.elementTextTrim("room-port");
			if (roomPortStr != null && roomPortStr.length() > 0) {
				roomPort = Integer.valueOf(roomPortStr);
			}
			
			List<Element> roomBacksList = rootEle.elements("room-backs");
			
			if (roomBacksList != null) {
				for (Element element : roomBacksList) {
					List<Element> roomBackList = element.elements("room-back");
					
					for (Element element2 : roomBackList) {
						RoomServerInfo roomServerInfo = new RoomServerInfo();
						roomServerInfo.setServerName(element2.elementTextTrim("room-id"));
						roomServerInfo.setServerIp(element2.elementTextTrim("room-ip"));
						
						String backRoomPortStr = element2.elementTextTrim("room-port");
						
						if (backRoomPortStr != null && backRoomPortStr.length() > 0) {
							roomServerInfo.setServerPort(Integer.valueOf(backRoomPortStr));
						}
						
						roomServerList.add(roomServerInfo);
					}
				}
			}
            
            // APP充值
			sAccountTypeId = rootEle.elementText("recharge-sAccountTypeID");
			
			String worldTypeStr = rootEle.elementTextTrim("world-type");
			if (worldTypeStr != null) {
				worldType = Integer.valueOf(worldTypeStr);
			}
			
			List<Element> ConfigFreq = rootEle.elements("login-freq");
			if (ConfigFreq != null && ConfigFreq.size() > 0) {
				for (int i = 0; i < ConfigFreq.size(); i++) {
					Element tempElement = (Element) ConfigFreq.get(i);
					String timeStr = tempElement.elementTextTrim("time");
					String numStr = tempElement.elementTextTrim("num");
					if (timeStr != null && numStr != null && timeStr.length() > 0 && numStr.length() > 0) {
						loginFreq = Integer.valueOf(timeStr);
						loginFreqNum = Integer.valueOf(numStr);
					}
				}
			}
			
			String indulgeFlagStr = rootEle.elementTextTrim("indulgePrompt-flag");
			if ((indulgeFlagStr != null) && (indulgeFlagStr.trim().length() > 0)) {
				indulgeFlag = Integer.valueOf(indulgeFlagStr);
				GameValue.GAME_INDULGE_ON = indulgeFlag == 1 ? true : false;
			}
			String indulgeTimeStr = rootEle.elementTextTrim("indulgePrompt-time");
			if (indulgeTimeStr != null && indulgeTimeStr.trim().length() > 0) {
				indulgeTime = Integer.valueOf(indulgeTimeStr);
				GameValue.GAME_INDULGE_TIME = indulgeTime;
			}

			String configXmlFlagStr = rootEle.elementTextTrim("configXml-flag");
			if (configXmlFlagStr != null && configXmlFlagStr.trim().length() > 0) {
				configXmlFlag = Integer.valueOf(configXmlFlagStr);
				GameValue.GAME_CONFIG_XML_FLAG = configXmlFlag;
			}
			
			
			
			String memyClearTime = rootEle.elementTextTrim("memy-clear-time");
			if (memyClearTime != null && memyClearTime.trim().length() > 0) {
				GameValue.GAME_LOAD_GAP = Integer.valueOf(memyClearTime);
			}
			
			String logInsertPerNum = rootEle.elementTextTrim("log-insert-per-num");
			if (logInsertPerNum != null && logInsertPerNum.trim().length() > 0) {
				GameLogDAO.logInsertNum = Integer.valueOf(logInsertPerNum);
			}
			
			String black_write_account_flag = rootEle.elementTextTrim("black-write-account-flag");
			if (black_write_account_flag != null && black_write_account_flag.trim().length() > 0) {
				GameValue.ACCOUNT_FLAG = Integer.valueOf(black_write_account_flag);
			}

			String Android_IOS_FLAG_STR = rootEle.elementTextTrim("Android_Ios_Flag");
			if (Android_IOS_FLAG_STR != null && Android_IOS_FLAG_STR.trim().length() > 0) {
				Android_Ios_Flag = Integer.valueOf(Android_IOS_FLAG_STR);
			}
		}
	}

	public void startLogThread() {
		for (int i = 0; i < logThread.length; i++) {
			logThread[i] = new ExecuteLogsThread();
			logThread[i].setName("ExecuteLogsThread" + (i + 1));
			logThread[i].start();
		}
		
		for (int i = 0; i < stringLogThread.length; i++) {
			stringLogThread[i] = new TempExecuteLogsThread();
			stringLogThread[i].setName("stringLogThread" + (i + 1));
			stringLogThread[i].start();
		}
	}
	
	public synchronized ExecuteLogsThread getExecuteLogsThread() {
		if (processorDistributor >= 10000000) {
            processorDistributor = 10000000 % logThread.length;
        }

        return logThread[processorDistributor++ % logThread.length];
	}
	
	public synchronized TempExecuteLogsThread getTempExecuteLogsThread() {
		if (processorDistributor >= 10000000) {
            processorDistributor = 10000000 % stringLogThread.length;
        }

        return stringLogThread[processorDistributor++ % stringLogThread.length];
	}
	
	public void stopWriteLog() {
		for (int i = 0; i < logThread.length; i++) {
			logThread[i].cancel();
			logThread[i].interrupt();
		}
		
		for (int i = 0; i < stringLogThread.length; i++) {
			stringLogThread[i].cancel();
			stringLogThread[i].interrupt();
		}
	}
	
	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public int getGameServerId() {
		return gameServerId;
	}

	public void setGameServerId(int gameServerId) {
		this.gameServerId = gameServerId;
	}

	public int getGameType() {
		return gameType;
	}

	public void setGameType(int gameType) {
		this.gameType = gameType;
	}

	public String getGameName() {
		return gameName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public String getToolIp() {
		return toolIp;
	}

	public void setToolIp(String toolIp) {
		this.toolIp = toolIp;
	}

	public int getToolPort() {
		return toolPort;
	}

	public void setToolPort(int toolPort) {
		this.toolPort = toolPort;
	}

	public String getToolLog() {
		return toolLog;
	}

	public void setToolLog(String toolLog) {
		this.toolLog = toolLog;
	}

	public String getGmccIp() {
		return gmccIp;
	}

	public void setGmccIp(String gmccIp) {
		this.gmccIp = gmccIp;
	}

	public int getGmccPort() {
		return gmccPort;
	}

	public void setGmccPort(int gmccPort) {
		this.gmccPort = gmccPort;
	}

	public String getGmccLog() {
		return gmccLog;
	}

	public void setGmccLog(String gmccLog) {
		this.gmccLog = gmccLog;
	}

	public String getMonitorIP() {
		return monitorIP;
	}

	public void setMonitorIP(String monitorIP) {
		this.monitorIP = monitorIP;
	}

	public int getMonitorPort() {
		return monitorPort;
	}

	public void setMonitorPort(int monitorPort) {
		this.monitorPort = monitorPort;
	}

	public String getMonitorId() {
		return monitorId;
	}

	public void setMonitorId(String monitorId) {
		this.monitorId = monitorId;
	}

	public String getMonitorLog() {
		return monitorLog;
	}

	public void setMonitorLog(String monitorLog) {
		this.monitorLog = monitorLog;
	}

	public String getChargeIp() {
		return chargeIp;
	}

	public void setChargeIp(String chargeIp) {
		this.chargeIp = chargeIp;
	}

	public int getChargePort() {
		return chargePort;
	}

	public void setChargePort(int chargePort) {
		this.chargePort = chargePort;
	}

	public String getChargeLog() {
		return chargeLog;
	}

	public void setChargeLog(String chargeLog) {
		this.chargeLog = chargeLog;
	}
	
	public String getVoiceIp() {
		return voiceIp;
	}

	public int getVoicePort() {
		return voicePort;
	}

	public String getVoiceLog() {
		return voiceLog;
	}
	
	public String getVoiceAccount() {
		return voiceAccount;
	}

	public String getVoicePass() {
		return voicePass;
	}

	public String getVoiceKey() {
		return voiceKey;
	}

	public String getRoomId() {
		return roomId;
	}

	public String getRoomIp() {
		return roomIp;
	}

	public int getRoomPort() {
		return roomPort;
	}

	public String getPushIP() {
		return pushIP;
	}

	public void setPushIP(String pushIP) {
		this.pushIP = pushIP;
	}

	public int getPushPort() {
		return pushPort;
	}

	public void setPushPort(int pushPort) {
		this.pushPort = pushPort;
	}

	public List<RoomServerInfo> getRoomServerList() {
		return roomServerList;
	}

	public int getInnerPort() {
		return innerPort;
	}

	public String getServerIp() {
		return serverIp;
	}

	public int getFightPort() {
		return fightPort;
	}
	
	public String getAppChargeIP() {
		return appChargeIP;
	}

	public int getAppChargePort() {
		return appChargePort;
	}

	public int getMonitorServerPort() {
		return monitorServerPort;
	}

	public void setMonitorServerPort(int monitorServerPort) {
		this.monitorServerPort = monitorServerPort;
	}

	public String getsAccountTypeId() {
		return sAccountTypeId;
	}

	public int getWorldType() {
		return worldType;
	}

	public void setWorldType(int worldType) {
		this.worldType = worldType;
	}

	public int getLoginFreq() {
		return loginFreq;
	}

	public void setLoginFreq(int loginFreq) {
		this.loginFreq = loginFreq;
	}

	public int getLoginFreqNum() {
		return loginFreqNum;
	}

	public void setLoginFreqNum(int loginFreqNum) {
		this.loginFreqNum = loginFreqNum;
	}

	public String getGateServerName() {
		return gateServerName;
	}

	public void setGateServerName(String gateServerName) {
		this.gateServerName = gateServerName;
	}

	public int getIndulgeFlag() {
		return indulgeFlag;
	}

	public void setIndulgeFlag(int indulgeFlag) {
		this.indulgeFlag = indulgeFlag;
	}

	public int getIndulgeTime() {
		return indulgeTime;
	}

	public void setIndulgeTime(int indulgeTime) {
		this.indulgeTime = indulgeTime;
	}

	public int getConfigXmlFlag() {
		return configXmlFlag;
	}

	public void setConfigXmlFlag(int configXmlFlag) {
		this.configXmlFlag = configXmlFlag;
	}

	public String getMonitorServerIp() {
		return monitorServerIp;
	}

	public void setMonitorServerIp(String monitorServerIp) {
		this.monitorServerIp = monitorServerIp;
	}

	public int getAndroid_Ios_Flag() {
		return Android_Ios_Flag;
	}

	public void setAndroid_Ios_Flag(int android_Ios_Flag) {
		Android_Ios_Flag = android_Ios_Flag;
	}

	public String getVersion() {
		return version;
	}
	
	
}
