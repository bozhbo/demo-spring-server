package com.snail.webgame.engine.gate.util;

import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.List;

import org.apache.mina.common.ByteBuffer;
import org.apache.mina.common.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.engine.gate.cache.DisconnectSessionMap;
import com.snail.webgame.engine.gate.cache.SequenceMap;
import com.snail.webgame.engine.gate.cache.ServerMap;
import com.snail.webgame.engine.gate.common.ContentValue;
import com.snail.webgame.engine.gate.common.DisconnectPhase;
import com.snail.webgame.engine.gate.config.Command;
import com.snail.webgame.engine.gate.config.WebGameConfig;
import com.spring.common.ErrorCode;
import com.spring.common.ServerName;

public class MessageServiceManage {

	private static final Logger log = LoggerFactory.getLogger("logs");
	/**
	 * 消息头总字节数量
	 */
	private static final byte HEAD_TOTAL_BYTES = 28;
	/**
	 * 消息长度
	 */
	private static final byte INDEX_MESSAGE_LEN = 0;
	/**
	 * 消息版本号
	 */
	private static final byte INDEX_VERSION = 4;
	/**
	 * 验证后的角色ID
	 */
	private static final byte INDEX_ROLE_ID = 8;
	/**
	 * 接入服务器ID
	 */
	private static final byte INDEX_GATE_ID = 12;
	/**
	 * 序列号
	 */
	private static final byte INDEX_SEQUENCEID = 16;
	/**
	 * 场景ID
	 */
	private static final byte INDEX_SCENE_ID = 20;
	/**
	 * 消息类型
	 */
	private static final byte INDEX_MESSAGE_ID = 24;



	/**
	 * 处理结果
	 */
	private static final byte BODY_INDEX_FIELD_0 = 28;
	/**
	 * 字符串类型账号（short长度+str） 
	 * A006整形
	 */
	private static final byte BODY_INDEX_FIELD_1 = 32;		
	
	
	public ByteBuffer getNewMsgHeader(int MsgType, int roleId) {

		ByteBuffer buffer = ByteBuffer.allocate(28, false);
		buffer.setAutoExpand(true);
		buffer.putInt(0);// 长度

		buffer.putInt(0x00000100);// 版本号

		buffer.putInt(roleId);// UserID0

		buffer.putInt(WebGameConfig.getInstance().getLocalConfig()
				.getServerId());// UserID1

		buffer.putInt(0);// UserID2
		buffer.putInt(0);// UserID3

		buffer.putInt(MsgType);
		buffer.putShort((short)0);

		return buffer;
	}

	/**
	 * 获取玩家的账号
	 * 
	 * @param roleId
	 * @param result
	 */
	public String checkLoginAccount(int sequenceId, byte[] message) {
		byte[] l = new byte[2];
		System.arraycopy(message, BODY_INDEX_FIELD_1, l, 0, 2);
		int len = Util.byteArrayToInt(l);

		byte[] a = new byte[len];
		System.arraycopy(message, 2 + BODY_INDEX_FIELD_1, a, 0, len);

		return new String(a, Charset.forName("UTF-8"));
	}
	/**
	 * 获取验证串字段{@link Command.RECONNECT_REQ}
	 * @param message
	 */
	public String getAcccode(byte[] message)
	{
		//验证串长度
		byte acccodelen[] = new byte[2];
		System.arraycopy(message, BODY_INDEX_FIELD_0, acccodelen, 0, 2);
		int ul = Util.byteArrayToInt(acccodelen);
		//验证串
		byte acccode[] = new byte[ul];
		System.arraycopy(message, BODY_INDEX_FIELD_0 + 2, acccode, 0, ul);
		
		return new String(acccode, Charset.forName("UTF-8"));
	}
	public void sendActiveServerMessage(IoSession session, int flag) {

		int groupServerId = WebGameConfig.getInstance().getGameServerId();
		int gateServerId = WebGameConfig.getInstance().getLocalConfig()
				.getServerId();
		ByteBuffer buffer = getNewMsgHeader(Command.GAME_SERVER_ACTIVE_REQ, 0);

		buffer.put(Util.encodeStringB(ServerName.GATE_SERVER_NAME + "-"
				+ gateServerId));

		buffer.putInt(flag);

		buffer.put(Util.encodeStringB(String.valueOf(groupServerId)));

		int length = buffer.position();
		buffer.putInt(0, length - 4);
		buffer.flip();

		session.write(buffer);

	}

	/**
	 * 判断游戏客户端能否登录,若能登录保存用户session
	 * 
	 * @param roleId
	 * @param result
	 */
	public int[] checkLoginResult(IoSession userSess, int sequenceId, byte[] message) {
		int[] ret = new int[2];

		int result = getResult(message);

		byte b[] = new byte[4];
		System.arraycopy(message, INDEX_ROLE_ID, b, 0, 4);

		int roleId = Util.byteArrayToInt(b);

		int sceneId = getSceneId(message);

		if(result == 1 && roleId > 0)
		{
			if (userSess != null && userSess.isConnected()) {

				userSess.setAttribute("identity", roleId);

				//如果玩家正常游戏中直接杀掉进程或者崩端
				//则需要在玩家再次登录成功后清除如下缓存
				//1、DisconnectSessionMap 避免超时后会向其它服发送3号断开连接
				//2、SequenceMap 删除崩端前的信息,重新登录重新验证
				//3、IdentityMap 删除崩端前的信息,重新登录重新验证
				synchronized(ContentValue.lock)
				{
					Iterator<Integer> it = DisconnectSessionMap.getMap().keySet().iterator();
					int mapSize = DisconnectSessionMap.getMap().size();
					if(mapSize > 50)
					{
						if(log.isErrorEnabled())
						{
							log.error("[DisconnectSessionMap size]="+mapSize);
						}
					}
					while(it.hasNext())
					{
						int seq = it.next();
						if(roleId == DisconnectSessionMap.getMap().get(seq).getRoleId())
						{
							it.remove();
							if(seq != sequenceId)
							{
								SequenceMap.removeSession(seq);
							}
							IdentityMap.removeSession(roleId);
							break;
						}
					}
				}
				
				// 这个帐号被其他用户登录踢出那个用户下线
				if (IdentityMap.isExistRole(roleId)) {
					// （如果存在 说明游戏服务器上没有发送迫使下线的信息）
					IoSession oldSession = IdentityMap.getSession(roleId);				
					if (oldSession != null && oldSession.isConnected()) {

						if (oldSession.getAttribute("identity") != null) {
							oldSession.removeAttribute("identity");
							sendDisconnectMsg(oldSession,  "",roleId, Command.USER_DISCONNECT_REQ,ErrorCode.USER_LOGIN_ERROR_2,DisconnectPhase.DISCONNECT,true);
						}
						if(oldSession.getAttribute("SequenceId") != null)
						{
							int oldSequenceId = (Integer)oldSession.getAttribute("SequenceId");
							SequenceMap.removeSession(oldSequenceId);
							oldSession.removeAttribute("SequenceId");
						}
						
						log.warn("MessageServiceManage : close for replace account");
						oldSession.close();
					}
					
					// 替换掉这个session
					IdentityMap.addSession(roleId, userSess);
				} else {
					IdentityMap.addSession(roleId, userSess);

					setRoleId((byte[]) message, roleId);
					//setSceneId((byte[]) message, sceneId);
					//setGateServerId((byte[]) message, WebGameConfig.getInstance().getLocalConfig().getGateServerId());

					
					// 广播此用户登录信息
					reportUserLogin(message);
				}
			} else{

				// 用户已经登录成功但是断开,必须向游戏服务器发送注销请求
				IoSession tradeSess = ServerMap
						.getSession(ServerName.GAME_SERVER_NAME);
				if (tradeSess != null && tradeSess.isConnected()) {
					sendDisconnectMsg(tradeSess,  "",roleId, Command.USER_DISCONNECT_REQ,1,DisconnectPhase.DISCONNECT,false);
				}

				IoSession sceneSession = ServerMap
						.getSession(ServerName.GAME_SCENE_SERVER + "-" + sceneId);
				if (sceneSession != null && sceneSession.isConnected()) {
					sendDisconnectMsg(sceneSession,  "",roleId, Command.USER_DISCONNECT_REQ,1,DisconnectPhase.DISCONNECT,false);
				}
			}
		}

		ret[0] = result;
		ret[1] = roleId;
		return ret;
	}

	public byte[] getUserVerifyResp(int result) {
		ByteBuffer buffer = getNewMsgHeader(Command.USER_VERIFY_ROLE_RESP, 0);

		buffer.putInt(result);

		int length = buffer.position();
		buffer.putInt(0, length - 4);
		buffer.flip();

		byte b[] = new byte[length];
		buffer.get(b);

		return b;

	}
	/**
	 * 向其他服务器广播用户登录信息
	 * 
	 * @param message
	 */
	public void reportUserLogin(byte[] message) {
		IoSession chatSess = ServerMap.getSession(ServerName.MAIL_SERVER_NAME);
		if (chatSess != null && chatSess.isConnected()) {
			chatSess.write(message);
		}
	}
	/**
	 * 通知其它服务器玩家断开
	 * @param serverList
	 * @param account 账号
	 * @param roleId
	 * @param disconnectPhase {@link DisconnectPhase}1-暂时断开 2-暂时断开后在规定时间内重连上 3-暂时断开后超过规定时间 4-直接断开
	 */
	public void reportUserDisconnect(List<String> serverList, String account,int roleId,DisconnectPhase disconnectPhase) 
	{
		for (String serverName : serverList) 
		{
			IoSession serverSession = ServerMap.getSession(serverName);
			if (serverSession != null && serverSession.isConnected()) 
			{
				sendDisconnectMsg(serverSession, account,roleId,Command.USER_DISCONNECT_REQ, 1, disconnectPhase, false);
			}
		}
	}

	/**
	 * 向指定session发送连接断开消息
	 * @param session(客户端连接、服务器连接)
	 * @param account 账号
	 * @param roleId
	 * @param msgType
	 * @param result 断开原因
	 * @param disconnectPhase {@link DisconnectPhase} 1-暂时断开 2-暂时断开后在规定时间内重连上 3-暂时断开后超过规定时间 4-直接断开
	 * @param wait
	 */
	public void sendDisconnectMsg(IoSession session, String account,int roleId, int msgType,int result,DisconnectPhase disconnectPhase,boolean wait) {
		ByteBuffer buffer = getNewMsgHeader(msgType, roleId);

		buffer.putInt(result);
		buffer.putInt(roleId);
		buffer.put(disconnectPhase.getPhaseValue());
		buffer.put(Util.encodeStringB(account));

		int length = buffer.position();
		buffer.putInt(0, length - 4);
		buffer.flip();
		byte[] b = new byte[buffer.limit()];
		buffer.get(b);
		if(wait)
		{
			//尽可能确保发送成功
			long s = System.currentTimeMillis();
			session.write(b).join(1000);
			long e = System.currentTimeMillis();
			if(log.isErrorEnabled())
			{
				log.error("[sendDisconnectMsg] time:"+(e-s)+",roleId:"+roleId+",msgType:"+msgType+",result:"+result);
			}
		}
		else
		{
			session.write(b);
		}
	}
	/**
	 * 设置接入服务器ID
	 * 
	 * @param b
	 * @param m
	 */
	public void setGateServerId(byte[] b, byte[] m) {
		if (b != null && b.length >= HEAD_TOTAL_BYTES) {
			b[INDEX_GATE_ID] = m[0];
			b[INDEX_GATE_ID + 1] = m[1];
			b[INDEX_GATE_ID + 2] = m[2];
			b[INDEX_GATE_ID + 3] = m[3];
		}
	}
	
	public int getGateServerId(byte[] b) {
		if (b.length >= HEAD_TOTAL_BYTES) {

			byte[] m = new byte[4];
			m[0] = b[INDEX_GATE_ID];
			m[1] = b[INDEX_GATE_ID + 1];
			m[2] = b[INDEX_GATE_ID + 2];
			m[3] = b[INDEX_GATE_ID + 3];
			int sequence = Util.byteArrayToInt(m);

			m = null;

			return sequence;
		}
		return 0;
	}

	/**
	 * 获得消息中的sequenceId
	 * 
	 * @param b
	 * @return
	 */
	public int getMsgSequence(byte[] b) {
		if (b.length >= HEAD_TOTAL_BYTES) {

			byte[] m = new byte[4];
			m[0] = b[INDEX_SEQUENCEID];
			m[1] = b[INDEX_SEQUENCEID + 1];
			m[2] = b[INDEX_SEQUENCEID + 2];
			m[3] = b[INDEX_SEQUENCEID + 3];
			int sequence = Util.byteArrayToInt(m);

			m = null;

			return sequence;
		}
		return 0;
	}

	/**
	 * 放置一个sequenceId
	 * 
	 * @param sequenceId
	 * @param b
	 */
	public void setMsgSequence(int sequenceId, byte[] b) {
		if (b.length >= HEAD_TOTAL_BYTES) {

			byte[] m = Util.int2bytes(sequenceId);
			b[INDEX_SEQUENCEID] = m[0];
			b[INDEX_SEQUENCEID + 1] = m[1];
			b[INDEX_SEQUENCEID + 2] = m[2];
			b[INDEX_SEQUENCEID + 3] = m[3];

			m = null;
		}
	}

	/**
	 * 获得用户ID
	 * 
	 * @param b
	 * @return
	 */
	public int getRoleId(byte[] b) {
		if (b != null && b.length >= HEAD_TOTAL_BYTES) {
			byte[] m = new byte[4];
			m[0] = b[INDEX_ROLE_ID];
			m[1] = b[INDEX_ROLE_ID + 1];
			m[2] = b[INDEX_ROLE_ID + 2];
			m[3] = b[INDEX_ROLE_ID + 3];

			int roleId = Util.byteArrayToInt(m);

			m = null;
			return roleId;
		}
		return 0;
	}

	/**
	 * 设置角色ID
	 * 
	 * @param b
	 */
	public void setRoleId(byte[] b, int roleId) {
		if (b != null && b.length >= HEAD_TOTAL_BYTES) {
			byte[] m = Util.int2bytes(roleId);

			b[INDEX_ROLE_ID] = m[0];
			b[INDEX_ROLE_ID + 1] = m[1];
			b[INDEX_ROLE_ID + 2] = m[2];
			b[INDEX_ROLE_ID + 3] = m[3];
			m = null;
		}
	}

	/**
	 * 获得场景服务器ID
	 * 
	 * @param b
	 * @return
	 */
	public int getSceneId(byte[] b) {
		if (b != null && b.length >= HEAD_TOTAL_BYTES) {
			byte[] m = new byte[4];
			m[0] = b[INDEX_SCENE_ID];
			m[1] = b[INDEX_SCENE_ID + 1];
			m[2] = b[INDEX_SCENE_ID + 2];
			m[3] = b[INDEX_SCENE_ID + 3];
			return Util.byteArrayToInt(m);
		}
		return 0;
	}

	/**
	 * 放置场景ID
	 * 
	 * @param b
	 */
	public void setSceneId(byte[] b, int sceneId) {
		if (b != null && b.length >= HEAD_TOTAL_BYTES) {
			byte[] m = Util.int2bytes(sceneId);

			b[INDEX_SCENE_ID] = m[0];
			b[INDEX_SCENE_ID + 1] = m[1];
			b[INDEX_SCENE_ID + 2] = m[2];
			b[INDEX_SCENE_ID + 3] = m[3];

			m = null;
		}
	}

	/**
	 * 获得该消息的类型
	 * 
	 * @return
	 */
	public int getMessageType(byte[] b) {
		if (b.length >= HEAD_TOTAL_BYTES) {
			byte[] m = new byte[4];
			m[0] = b[INDEX_MESSAGE_ID];
			m[1] = b[INDEX_MESSAGE_ID + 1];
			m[2] = b[INDEX_MESSAGE_ID + 2];
			m[3] = b[INDEX_MESSAGE_ID + 3];
			int k = Util.byteArrayToInt(m);
			if (k == 0) {
				for (int i = 0; i < HEAD_TOTAL_BYTES; i++) {
					System.out.print(b[i] + ",");
				}
			}
			m = null;
			return k;
		}

		return 0;
	}

	/**
	 * 设置该消息的类型
	 * 
	 * @return
	 */
	public void setMessageType(int msgType, byte[] b) {
		if (b.length >= HEAD_TOTAL_BYTES) {
			byte[] m = Util.int2bytes(msgType);
			b[INDEX_MESSAGE_ID] = m[0];
			b[INDEX_MESSAGE_ID + 1] = m[1];
			b[INDEX_MESSAGE_ID + 2] = m[2];
			b[INDEX_MESSAGE_ID + 3] = m[3];

			m = null;
		}

	}

	/**
	 * 获得消息处理结果字段值
	 * @param message
	 * @return
	 */
	public int getResult(byte[] message) {
		byte c[] = new byte[4];
		byte s[] = new byte[2];
		System.arraycopy(message, BODY_INDEX_FIELD_0, s, 0, 2);
		
		short len = Util.byteArrayToShort(s);
		
		System.arraycopy(message, BODY_INDEX_FIELD_0 + 2 + len, c, 0, 4);
		int result = Util.byteArrayToInt(c);

		c = null;
		s = null;
		
		return result;
	}

	/**
	 * 向游戏服务器广播登录队列
	 * 
	 * @param session
	 * @param account
	 */
	public void reportUpdateLoginQueue(IoSession session, String account) {
		ByteBuffer buffer = getNewMsgHeader(Command.CHECK_LOGIN_QUEUE_RESP, 0);

		buffer.putInt(1);
		buffer.put(Util.encodeStringB(account));
		int length = buffer.position();
		buffer.putInt(0, length - 4);
		buffer.flip();

		session.write(buffer);
	}
}
