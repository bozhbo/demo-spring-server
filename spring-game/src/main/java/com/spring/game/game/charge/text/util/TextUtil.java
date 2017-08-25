package com.snail.webgame.game.charge.text.util;

import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import org.apache.mina.common.ByteBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.game.charge.text.AppClientThread;

/**
 * 
 * 类介绍:文本协议协助类
 *
 * @author zhoubo
 * @2015年6月30日
 */
public class TextUtil {
	
	private static AppClientThread appClientThread = null;
	
	private static final CharsetEncoder encoder = Charset.forName("UTF-8").newEncoder();
	private static final Logger logger = LoggerFactory.getLogger("logs");

	/**
	 * 字符起始标识
	 */
	public static final byte string = (byte) '$';
	
	/**
	 * 消息内容分割标识
	 */
	public static final byte block = (byte) ' ';
	
	/**
	 * 结束标识
	 */
	public static final byte endR = (byte) '\r';
	
	/**
	 * 结束标识
	 */
	public static final byte endN = (byte) '\n';
	
	/**
	 * GBK编码起始标识
	 */
	public static final byte gbkPrefix1 = (byte) '\\';
	
	/**
	 * GBK编码起始标识
	 */
	public static final byte gbkPrefix2 = (byte) 'x';

	public static boolean isAsc(byte b) {
		return (b > 0x20) && (b < 0x7F) && (b != 0x25) && (b != 0x5c);
	}
	
	public static boolean isGbk(byte b) {
		return (b <= 0x20) || (b >= 0x7F) || (b == 0x25) || (b == 0x5c);
	}
	
	/**
	 * 编码发送信息
	 * 
	 * @param str	字符串
	 * @return	二进制数组
	 */
	public static byte[] getEncodeBytes(String str) {
		if (str == null || "".equals(str)) {
			return null;
		}
		
		byte[] bytes = str.getBytes(encoder.charset());
		List<Byte> list = new ArrayList<Byte>();
		
		for (byte b : bytes) {
			if (isGbk(b)) {
				list.add(gbkPrefix1);
				list.add(gbkPrefix2);
				char[] chars = Integer.toHexString(b & 0xFF).toUpperCase().toCharArray();
				
				for (char c : chars) {
					list.add((byte)c);
				}
			} else {
				list.add(b);
			}
		}
		
		bytes = new byte[list.size()];
		
		for (int i = 0; i < bytes.length; i++) {
			bytes[i] = list.get(i);
		}
		
		return bytes;
	}
	
	/**
	 * 发送充值订单
	 * 
	 * @param objs
	 * 1.方法名 [AppleCharge]
	 * 2.平台充值订单id (String)
	 * 3.卡类型 (String)
	 * 4.蜗牛通行证 (String)
	 * 5.充值ip (String)
	 * 6.购买数量 (int)
	 * 7.总价 (int)
	 * 8.账户类型 (int)
	 * 9.充入区域id (int)
	 * 10.计费扩展json (String)
	 * 11.扩展字段 (String)
	 * 
	 * @return
	 */
	public static boolean sendMessage(Object ... objs) {
		if (appClientThread != null) {
			ByteBuffer in = ByteBuffer.allocate(1024);
			in.setAutoExpand(true);
			in.order(ByteOrder.LITTLE_ENDIAN);
			
			for (int i = 0; i < objs.length; i++) {
				if (objs[i] == null || "".equals(objs[i].toString())) {
					in.put(string);
				} else {
					if (objs[i] instanceof String) {
						in.put(string);
						in.put(TextUtil.getEncodeBytes((String)objs[i]));
					} else {
						in.put(TextUtil.getEncodeBytes(String.valueOf(((Integer)objs[i]).intValue())));
					}
				}
				
				if (i + 1 < objs.length) {
					in.put(block);
				}
				
				logger.info("send = " + objs[i].toString());
			}
			
			in.put(endR);
			in.put(endN);
			
			in.flip();
			
			if(appClientThread.getSession() != null && appClientThread.getSession().isConnected()){
				appClientThread.getSession().write(in);
			} else {
				logger.info("appClientThread.getSession() is null  or  not connnected !");
				return false;
			}
			return true;
		} else {
			return false;
		}
	}
	
	public static void startAppChargeThread(String serverIp, int serverPort, Executor handlerExecutor) {
		appClientThread = new AppClientThread(serverIp, serverPort, handlerExecutor);
		appClientThread.start();
	}
}
