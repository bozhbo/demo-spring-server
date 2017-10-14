package com.snail.webgame.engine.gate.receive.manage;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

import org.apache.mina.common.IdleStatus;
import org.apache.mina.common.IoHandlerAdapter;
import org.apache.mina.common.IoSession;
import org.apache.mina.common.TransportType;
import org.apache.mina.transport.socket.nio.SocketSessionConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.engine.gate.cache.BlacklistMap;
import com.snail.webgame.engine.gate.cache.DisconnectSessionMap;
import com.snail.webgame.engine.gate.cache.SequenceMap;
import com.snail.webgame.engine.gate.cache.ServerMap;
import com.snail.webgame.engine.gate.common.ContentValue;
import com.snail.webgame.engine.gate.common.DisconnectInfo;
import com.snail.webgame.engine.gate.common.DisconnectPhase;
import com.snail.webgame.engine.gate.common.SocketConfig;
import com.snail.webgame.engine.gate.config.Command;
import com.snail.webgame.engine.gate.config.TempTestConfig;
import com.snail.webgame.engine.gate.config.WebGameConfig;
import com.snail.webgame.engine.gate.util.MessageServiceManage;
import com.snail.webgame.engine.gate.util.SequenceId;
import com.snail.webgame.engine.gate.util.Util;
import com.spring.common.ErrorCode;
import com.spring.common.ServerName;

public class ConnectProtocolHandler extends IoHandlerAdapter {

	private static String xmlStr = "<cross-domain-policy>" + "<allow-access-from domain=\""
			+ WebGameConfig.getInstance().getFlashConfig().getDoamin() + "\" to-ports=\""
			+ WebGameConfig.getInstance().getFlashConfig().getPort() + "\" />" + "</cross-domain-policy>";

	private static final Logger log = LoggerFactory.getLogger("logs");

	private MessageServiceManage msgmgt = null;

	public ConnectProtocolHandler(MessageServiceManage msgmgt) {
		this.msgmgt = msgmgt;
	}

	public void exceptionCaught(IoSession session, Throwable arg1) throws Exception {
		log.warn("ConnectProtocolHandler : session exception ", arg1);

		if (session.getAttribute("SequenceId") != null) {
			// Android设备断开wifi后客户端重连请求失败重新登录
			// 此时原session已被客户端弃用
			// 如果再从该session读数据(暂时不确定读什么数据)时则报异常
			// 另外一种情况则是被弃用的链接不报异常,服务器认为该连接正常,所以需要在别的地方(A点)处理这种情况下的废弃缓存信息
			int sequenceId = (Integer) session.getAttribute("SequenceId");
			SequenceMap.removeSession(sequenceId);
		}
		session.close();
	}

	public void messageReceived(IoSession session, Object message) throws Exception {
		if (message instanceof String) {
			if (((String) message).startsWith("<policy-file-request/>")) {
				if (session.isConnected()) {
					session.write(xmlStr);// 返回flash的安全校验

				}
			}
		} else if (message instanceof byte[]) {
			if (!session.isConnected()) {
				return;
			}
			int msgType = msgmgt.getMessageType((byte[]) message);

			// 游戏客户端激活链接
			if (msgType == Command.GAME_CLIENT_ACTIVE_REQ) {
				session.write(message);
				return;
			}

			// 断线重连
			if (WebGameConfig.getInstance().getReconnectFlag() > 0 && msgType == Command.RECONNECT_REQ) {
				int sequenceId = msgmgt.getMsgSequence((byte[]) message);
				if (SequenceMap.exist(sequenceId)) {
					// 判断该连接是否是原连接
					if (session.getAttribute("SequenceId") != null) {
						int newSeq = (Integer) session.getAttribute("SequenceId");
						if (sequenceId == newSeq) {
							// 重连消息原样返回
							session.write(message).join(1000);
							log.warn("user connection is normal.ip:"
									+ ((InetSocketAddress) session.getRemoteAddress()).getAddress().getHostAddress());
							return;
						}
					}

//					// 合法账号
//					IoSession oldSession = SequenceMap.getSession(sequenceId);
//					String acc = (String) oldSession.getAttribute("Account");
//					// 获取重连消息中的校验串
//					String acccode = msgmgt.getAcccode((byte[]) message);
//					if (acc == null || acccode == null || acc.length() == 0 || acccode.length() == 0) {
//						if (log.isWarnEnabled()) {
//							InetSocketAddress address1 = (InetSocketAddress) session.getRemoteAddress();
//							String ip = address1.getAddress().getHostAddress();
//							log.warn("user reconnect failure, IP=" + ip + " acc=" + acc + " acccode=" + acccode);
//						}
//						msgmgt.sendDisconnectMsg(session, "", 0, Command.USER_DISCONNECT_REQ, ErrorCode.ERROR_REQUEST,
//								DisconnectPhase.DISCONNECT, false);
//						return;
//					}
//
//					if (acc.equalsIgnoreCase(FixedDes.decrypt(acccode)))// 合法
//					{
//						session.setAttribute("Account", acc);
//						session.setAttribute("SequenceId", sequenceId);
//						session.setAttribute("lastReqTime", oldSession.getAttribute("lastReqTime"));
//						session.setAttribute("freqNum", oldSession.getAttribute("freqNum"));
//						// 设置验证信息
//						Object identity = oldSession.getAttribute("identity");
//						if (identity != null) {
//							session.setAttribute("identity", identity);
//							IdentityMap.addSession((Integer) identity, session);
//						}
//
//						SequenceMap.addSession(sequenceId, session);
//
//						if (oldSession != null && oldSession.isConnected()) {
//							log.info("ConnectProtocolHandler : close old session");
//							oldSession.close();
//						}
//
//						oldSession.setAttribute("identity", null);
//						oldSession.setAttribute("SequenceId", null);
//
//						// 重连成功该消息原样返回
//						session.write(message);
//						synchronized (ContentValue.lock) {
//							DisconnectSessionMap.removeDisconnectInfo(sequenceId);
//						}
//						// 通知其它服务器玩家重连成功
//						if (identity != null) {
//							List<String> serverList = new ArrayList<String>();
//							serverList.add(ServerName.GAME_SERVER_NAME);
//							serverList.add(ServerName.MAIL_SERVER_NAME);
//							msgmgt.reportUserDisconnect(serverList, "", (Integer) identity,
//									DisconnectPhase.DISCONNECT_RECONNECT_OK);
//						}
//						if (log.isWarnEnabled()) {
//							InetSocketAddress address = (InetSocketAddress) session.getRemoteAddress();
//							String ip = address.getAddress().getHostAddress();
//							log.warn("user reconnect success, IP=" + ip + " old session identity=" + identity + " acc="
//									+ acc);
//						}
//						return;
//					} else {
//						if (log.isWarnEnabled()) {
//							InetSocketAddress address1 = (InetSocketAddress) session.getRemoteAddress();
//							String ip = address1.getAddress().getHostAddress();
//							log.warn("user reconnect failure, IP=" + ip + " acc=" + acc + " acccode=" + acccode);
//						}
//						msgmgt.sendDisconnectMsg(session, "", 0, Command.USER_DISCONNECT_REQ, ErrorCode.ERROR_REQUEST,
//								DisconnectPhase.DISCONNECT, false);
//					}

				} else {
					if (log.isWarnEnabled()) {
						InetSocketAddress address = (InetSocketAddress) session.getRemoteAddress();
						String ip = address.getAddress().getHostAddress();
						log.warn("user reconnect failure, IP=" + ip + " sequenceId=" + sequenceId + " is not found!");
					}
					msgmgt.sendDisconnectMsg(session, "", 0, Command.USER_DISCONNECT_REQ, ErrorCode.USER_LOGIN_ERROR_3,
							DisconnectPhase.DISCONNECT, false);
				}
				return;
			} else if (msgType == Command.USER_VERIFY_ROLE_REQ || msgType == Command.USER_LOGIN_REQ
					|| msgType == Command.USER_CREATE_ROLE_REQ || msgType == Command.USER_LOGIN_QUEUE_REQ 
					|| msgType == Command.USER_LOGIN_ACTIVATE_REQ)// 用户登录游戏服务器
			{
				if (msgType == Command.USER_LOGIN_REQ || msgType == Command.USER_VERIFY_ROLE_REQ
						|| msgType == Command.USER_CREATE_ROLE_REQ) {
					// 登陆请求消息头里边放入客户端ip(原场景字段)
					InetSocketAddress address = (InetSocketAddress) session.getRemoteAddress();
					String ip = address.getAddress().getHostAddress();
					msgmgt.setSceneId((byte[]) message, Util.IPtoInt(ip));
				}
				Object seq = session.getAttribute("SequenceId");
				int sequenceId = -1;
				if (seq != null) {
					sequenceId = (Integer) seq;
				} else {
					sequenceId = SequenceId.getSequenceId();
					session.setAttribute("SequenceId", sequenceId);

					SequenceMap.addSession(sequenceId, session);
				}
				msgmgt.setMsgSequence(sequenceId, (byte[]) message);
			} else {
				if (session.getAttribute("identity") == null)// 用户没有通过身份验证
				{
					if (log.isWarnEnabled()) {
						InetSocketAddress address = (InetSocketAddress) session.getRemoteAddress();
						String ip = address.getAddress().getHostAddress();
						log.warn("user is not pass check identity,  MsgType :0x" + Integer.toHexString(msgType) + ",IP="
								+ ip + Util.bytesToStr((byte[]) message));
					}
					// 用户下线
					msgmgt.sendDisconnectMsg(session, "", 0, Command.USER_DISCONNECT_REQ, ErrorCode.USER_LOGIN_ERROR_3,
							DisconnectPhase.DISCONNECT, false);
					return;
				}
				else {
					// DEBUG
					log.info("role " + (Integer)session.getAttribute("identity") + ", msg = " + Integer.toHexString(msgType));
				}

			}

			// 根据列表转发各种消息
			String serverName = WebGameConfig.getInstance().getTramsitServer(msgType);
			if (serverName != null) {

				if (serverName.startsWith(ServerName.GAME_SCENE_SERVER)) {
					serverName = serverName + "-" + msgmgt.getSceneId((byte[]) message);
				} else if (serverName.startsWith(ServerName.ROOM_SERVER_NAME)) {
					serverName = serverName + "-" + msgmgt.getSceneId((byte[]) message);
				}

				IoSession revSess = ServerMap.getSession(serverName);

				if (revSess != null && revSess.isConnected()) {
					Object identity = session.getAttribute("identity");
					if (identity != null) {
						msgmgt.setRoleId((byte[]) message, (Integer) identity);

					}
					msgmgt.setGateServerId((byte[]) message,
							WebGameConfig.getInstance().getLocalConfig().getGateServerId());

					revSess.write(message);
				} else {
					if (msgType == Command.USER_VERIFY_ROLE_REQ) {
						session.write(msgmgt.getUserVerifyResp(ErrorCode.GATE_SERVER_ERROR_1));
					} else {
						log.error("this is no server session for msg " + Integer.toHexString(msgType));
					}
				}
			} else {
				if (log.isWarnEnabled()) {
					InetSocketAddress address = (InetSocketAddress) session.getRemoteAddress();
					String ip = address.getAddress().getHostAddress();
					log.warn("It is not exist receiveServer for  MsgType :0x" + Integer.toHexString(msgType) + ",ip="
							+ ip + Util.bytesToStr((byte[]) message));
				}
			}
		}

	}

	public void messageSent(IoSession session, Object arg1) throws Exception {

		if (session.containsAttribute(ContentValue.MSG_SENT_CLOSE)) {
			log.info("ConnectProtocolHandler : close for messageSent");
			session.close();
		}
	}

	public void sessionClosed(IoSession session) throws Exception {
		try {
			if (session != null) {
				if (session.getRemoteAddress() != null) {
					log.info("ConnectProtocolHandler : sessionClosed" + session.getRemoteAddress().toString());
				} else {
					log.info("ConnectProtocolHandler : sessionClosed");
				}
			}
		} catch (Exception e) {

		}

		if (session.getAttribute("SequenceId") != null) {
			// 暂时断开，规定时间内认证信息不清除
			int sequenceId = (Integer) session.getAttribute("SequenceId");

			DisconnectInfo disconnectInfo = new DisconnectInfo();
			disconnectInfo.setSeq(sequenceId);
			if (session.getAttribute("identity") != null) {
				int roleId = (Integer) session.getAttribute("identity");
				disconnectInfo.setRoleId(roleId);
			}

			disconnectInfo.setSession(session);
			disconnectInfo.setTime(System.currentTimeMillis());
			synchronized (ContentValue.lock) {
				DisconnectSessionMap.addDisconnectInfo(disconnectInfo);
			}

			// 通知其它服务器该玩家断线
			List<String> serverList = new ArrayList<String>();
			serverList.add(ServerName.GAME_SERVER_NAME);
			if (disconnectInfo.getRoleId() > 0) {
				serverList.add(ServerName.MAIL_SERVER_NAME);
			}

			if (session.getAttribute("Account") != null) {
				msgmgt.reportUserDisconnect(serverList, session.getAttribute("Account").toString(),
						disconnectInfo.getRoleId(), DisconnectPhase.DISCONNECT_TEMP);
			}

		}
		if (session.getAttribute("identity") != null) {

		} else {
			if (session.getAttribute("Account") != null) {
				String account = (String) session.getAttribute("Account");
				// 通知游戏服务器
				IoSession gameSess = ServerMap.getSession(ServerName.GAME_SERVER_NAME);
				if (gameSess != null && gameSess.isConnected()) {
					msgmgt.reportUpdateLoginQueue(gameSess, account);
				}
			}
		}

		if (session.getAttribute("SequenceId") != null) {
			// int sequenceId = (Integer) session.getAttribute("SequenceId");
			// SequenceMap.removeSession(sequenceId);
			// log.warn("Client is error.
			// sessionClosed.sequenceId:"+sequenceId);
		}
	}

	public void sessionCreated(IoSession session) throws Exception {

	}

	public void sessionIdle(IoSession session, IdleStatus arg1) throws Exception {
		log.warn("ConnectProtocolHandler : sessionIdle");
		session.close();
	}

	public void sessionOpened(IoSession session) throws Exception {
		InetSocketAddress address = (InetSocketAddress) session.getRemoteAddress();
		String ip = address.getAddress().getHostAddress();
		if (BlacklistMap.isBlack(ip)) {
			if (log.isWarnEnabled()) {
				log.warn("Close this session, the ip = " + ip + " in blacklist!");
			}
			session.close();
			return;
		}
		if (!TempTestConfig.instance().validate(ip)) {
			if (log.isWarnEnabled()) {
				log.warn("Close this session, the ip = " + ip + " is not in temptest!");
			}
			session.close();
			return;
		}

		if (session.getTransportType() == TransportType.SOCKET) {
			SocketConfig socketConfig = WebGameConfig.getInstance().getScoketConfig();

			if (socketConfig != null) {
				((SocketSessionConfig) session.getConfig()).setSendBufferSize(socketConfig.getSocketSendBuffer());

				((SocketSessionConfig) session.getConfig()).setReceiveBufferSize(socketConfig.getSocketReceiveBuffer());

				((SocketSessionConfig) session.getConfig()).setKeepAlive(socketConfig.isKeepAlive());

				((SocketSessionConfig) session.getConfig()).setTcpNoDelay(socketConfig.isTcpNoDelay());
				int state = socketConfig.getIdleState();
				int timeout = socketConfig.getTimeout();
				if (state == 1) {
					session.setIdleTime(IdleStatus.READER_IDLE, timeout);
				} else if (state == 2) {
					session.setIdleTime(IdleStatus.WRITER_IDLE, timeout);
				} else if (state == 3) {
					session.setIdleTime(IdleStatus.BOTH_IDLE, timeout);
				}
			}
		}
		
		log.info("remote client is open " + session.getRemoteAddress().toString());
	}

}
