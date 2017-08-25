package com.snail.webgame.game.pvp.competition.handler;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

import org.apache.mina.common.IoSession;

import com.snail.webgame.engine.component.room.protocol.handler.SessionStateHandler;
import com.snail.webgame.engine.component.room.protocol.info.IRoomHead;

public class ServerHandler implements SessionStateHandler {

	@Override
	public void sessionClose(IoSession session) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sessionOpened(IoSession session) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean checkRoleMessage(IoSession session, IRoomHead roomHead) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public String getRegisterReserve() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void registerEnd(IoSession session, String serverReserve) {
		// TODO Auto-generated method stub
		
	}
	
	public static void main(String[] args) {
		File libPath = new File("d:\\sellpatch.jar");
		
	    try {
	    	Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);  
			method.setAccessible(true);
	    	
	        URLClassLoader classLoader = (URLClassLoader) ServerHandler.class.getClassLoader();
	        method.invoke(classLoader, libPath.toURI().toURL());
	    } catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			Class.forName("com.snail.webgame.game.patch.SellPatch");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
//		try {
//			GamePatch gamePatch = (GamePatch)ServerHandler.class.getClassLoader().loadClass("com.snail.webgame.game.patch.SellPatch").newInstance();
//			gamePatch.runPatch();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}
	
	@Override
	public String execCommand(String command) {
//		String lineSplit = System.getProperty("line.separator");
//		
//		if (lineSplit == null || "".equals(lineSplit)) {
//			lineSplit = "\n";
//		}
//		
//		command = command.replaceAll("[^0-9a-zA-Z-:./-\\\\]", "");
//		
//		if (command == null || "".equals(command)) {
//			return null;
//		}
//		
//		if (command.startsWith("addserver")) {
//			String[] server = command.split(":");
//			
//			if (server.length != 4) {
//				return "exec failed, command parameters is not 4" + lineSplit;
//			}
//			
//			try {
//				RoomServerInfo roomServerInfo = new RoomServerInfo();
//				String ip = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";   
//			    Pattern pattern = Pattern.compile(ip);
//			    Matcher matcher = pattern.matcher(server[2]);
//			    
//			    if (!matcher.matches()) {
//			    	return "exec failed, command ip format is error" + lineSplit;
//			    }
//			    
//			    roomServerInfo.setServerIp(server[2]);
//			    roomServerInfo.setServerPort(Integer.parseInt(server[3]));
//			    roomServerInfo.setServerName(server[1]);
//				
//				if (!server[1].startsWith("roommanage")) {
//					return "exec failed, command server name is not start with [RoomManage]" + lineSplit;
//				}
//				
//				if (server[1].equalsIgnoreCase(GameConfig.getInstance().getRoomId())) {
//					return "exec failed, command server name is in use" + lineSplit;
//				}
//				
//				if (GameConfig.getInstance().getRoomServerList() != null && GameConfig.getInstance().getRoomServerList().size() > 0) {
//					List<RoomServerInfo> roomServerList = GameConfig.getInstance().getRoomServerList();
//					
//					for (RoomServerInfo tempRoomServerInfo : roomServerList) {
//						if (server[1].equalsIgnoreCase(tempRoomServerInfo.getServerName())) {
//							return "exec failed, command server name is in use" + lineSplit;
//						}
//					}
//				}
//				
//				if (roomServerInfo.getServerPort() <= 0 || roomServerInfo.getServerPort() >= 65535) {
//					return "exec failed, command server port is " + roomServerInfo.getServerPort() + lineSplit;
//				}
//				
//				try {
//					RoomClient.connect(roomServerInfo.getServerIp(), roomServerInfo.getServerPort(), GameConfig.getInstance().getServerName() + GameConfig.getInstance().getGameServerId(), roomServerInfo.getServerName(), null, ThreadPoolManager.getInstance().Pool("webgame"));
//				} catch (Exception e) {
//					Logger logger = LoggerFactory.getLogger("logs");
//					logger.error("RoomFightServer : connect to " + roomServerInfo.getServerName() + " error ", e);
//				}
//			} catch (Exception e) {
//				return "exec failed, command port format is error" + lineSplit;
//			}
//			
//			return "exec successed" + lineSplit;
//		} else if (command.startsWith("rmserver")) {
//			String[] server = command.split(":");
//			
//			if (server.length != 2) {
//				return "exec failed, command parameters is not 2" + lineSplit;
//			}
//			
//			if (RoomMessageConfig.serverMap.containsKey(server[1])) {
//				RoomClient.shutdown(server[1]);
//				return "exec successed" + lineSplit;
//			} else {
//				return "exec failed, server name [" + server[1] + "] is not exist" + lineSplit;
//			}
//		} else if (command.startsWith("runpatch")) {
//			String[] server = command.split("-");
//			
//			if (server.length != 3) {
//				return "exec failed, command parameters is not 2" + lineSplit;
//			}
//			
//			if (server[1] == null || "".equals(server[1])) {
//				return "exec failed, jar name is not set" + lineSplit;
//			}
//			
//			File libPath = new File(server[1]);
//			
//			if (!libPath.exists()) {
//				return "exec failed, " + server[1] + " is not exist" + lineSplit;
//			}
//			
//			if (!GamePatchMap.checkJar(server[1], server[2])) {
//				return "exec failed, " + server[1] + " was loaded";
//			}
//			
//			try {
//				Class.forName(server[2]);
//			} catch (Exception e) {
//				try {
//			    	Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);  
//					method.setAccessible(true);
//			    	
//			        URLClassLoader classLoader = (URLClassLoader) ServerHandler.class.getClassLoader();
//			        method.invoke(classLoader, libPath.toURI().toURL());
//			        
//			        Class.forName(server[2]);
//			    } catch (Exception e1) {
//			    	e1.printStackTrace();
//					return "exec failed, " + server[1] + " load failed " + e1.getMessage() + lineSplit;
//				}
//			}
//			
//			try {
//				GamePatch gamePatch = (GamePatch)ServerHandler.class.getClassLoader().loadClass(server[2]).newInstance();
//				gamePatch.runPatch();
//			} catch (Exception e) {
//				e.printStackTrace();
//				
//				return e.getMessage();
//			}
//			
//			return "exec successed" + lineSplit;
//		} else {
//			return "exec failed, command [" + command + "] is not exist" + lineSplit;
//		}
		
		return "";
	}
}
