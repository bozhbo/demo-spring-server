package com.snail.webgame.engine.gate.filter.crypto;

import java.util.concurrent.ConcurrentHashMap;

import org.apache.mina.common.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.engine.gate.common.ContentValue;
import com.snail.webgame.engine.gate.filter.crypto.arithmetic.Aes;
import com.snail.webgame.engine.gate.filter.crypto.arithmetic.Des;
import com.snail.webgame.engine.gate.filter.crypto.arithmetic.TripleDes;

/**
 * 摘要：提供对原始字节数据或特定网络消息进行加密解密
 * @author leiqiang
 * @date   2014-7-31
 */
public class Crypto {
	private static final Logger log =LoggerFactory.getLogger("logs");
	/**
	 * 加密网络消息（包含消息长度）
	 * @param cryptoType 0-不处理 1-Des 3-TripleDes 4-Aes 99-动态加密 可使用的算法类型见{@link Arithmetic}
	 * @param message
	 * @return
	 */
	public static byte[] encryptMessage(Arithmetic cryptoType, byte[] message)
	{
		if(cryptoType == null || cryptoType.equals(Arithmetic.NO_OP))
		{
			return message;
		}
		if(message == null || message.length < 4)
		{
			return null;
		}
		byte[] message_len = new byte[4];
		System.arraycopy(message, 0, message_len, 0, 4);
		byte[] data = new byte[CryptoUtil.bytes2int(message_len)];
		//byte[] data = new byte[message.length -4];
		System.arraycopy(message, 4, data, 0, data.length);
		if(cryptoType.equals(Arithmetic.RANDOM))
		{
			Arithmetic type = Arithmetic.randomArithmetic();
			byte[] key = null;
			byte[] en_data = null;
			if(type.equals(Arithmetic.DES))
			{
				key = CryptoUtil.getRandomKey(CryptoConstant.DES_KEY_LEN);
				en_data = Des.encrypt(data, key, key);
			}
			else if(type.equals(Arithmetic.DES3))
			{
				key = CryptoUtil.getRandomKey(CryptoConstant.DES3_KEY_LEN);
				byte[] iv = new byte[8];
				System.arraycopy(key, 0, iv, 0, 8);
				en_data = TripleDes.encrypt(data, key, iv);
			}
			else if(type.equals(Arithmetic.AES))
			{
				key = CryptoUtil.getRandomKey(CryptoConstant.AES_KEY_LEN);
				en_data = Aes.encrypt(data, key, key);
			}
			
			byte[] data_len = CryptoUtil.int2bytes(1 + key.length + en_data.length);
			byte[] newMessage = new byte[4 + 1 + key.length + en_data.length];
			System.arraycopy(data_len, 0, newMessage, 0, 4);//消息长度
			newMessage[4] = (byte)type.getValue();//随机算法
			System.arraycopy(key, 0, newMessage, 5, key.length);//随机秘钥
			System.arraycopy(en_data, 0, newMessage, 5 + key.length, en_data.length);
			message_len = null;
			data = null;
			key = null;
			en_data = null;
			data_len = null;
			return newMessage;
		}
		else
		{
			byte[] en_data = encrypt(cryptoType, data);
			byte[] en_data_len = CryptoUtil.int2bytes(en_data.length);
			byte[] newMessage = new byte[4 + en_data.length];
			System.arraycopy(en_data_len, 0, newMessage, 0, 4);
			System.arraycopy(en_data, 0, newMessage, 4, en_data.length);
			message_len = null;
			data = null;
			en_data = null;
			en_data_len = null;
			return newMessage;
		}
	}
	/**
	 * 解密网络消息（包含消息长度）
	 * @param cryptoType 0-不处理 1-Des 3-TripleDes 4-Aes 99-动态加密 可使用的算法类型见{@link Arithmetic}
	 * @param message
	 * @param session
	 * @return
	 */
	public static byte[] decryptMessage(Arithmetic cryptoType, byte[] message, IoSession session)
	{
		if(cryptoType == null || cryptoType.equals(Arithmetic.NO_OP))
		{
			return message;
		}
		if(message == null || message.length < 4)
		{
			if(log.isWarnEnabled())
			{
				log.warn("DynamicEncrypt message error.[" + message + "]");
			}
			return null;
		}
		byte[] message_len = new byte[4];
		System.arraycopy(message, 0, message_len, 0, 4);
		if(cryptoType.equals(Arithmetic.RANDOM))
		{
			Arithmetic type = new Arithmetic(message[4]);
			byte[] key = null;
			byte[] de_data = null;
			if(type.equals(Arithmetic.DES))
			{
				key = new byte[CryptoConstant.DES_KEY_LEN];
				System.arraycopy(message, 5, key, 0, key.length);
				byte[] data = new byte[CryptoUtil.bytes2int(message_len) - 1 - key.length];
				//byte[] data = new byte[message.length - 4 - 1 - key.length];
				System.arraycopy(message, 4 + 1 + key.length, data, 0, data.length);
				de_data = Des.decrypt(data, key, key);
			}
			else if(type.equals(Arithmetic.DES3))
			{
				key = new byte[CryptoConstant.DES3_KEY_LEN];
				System.arraycopy(message, 5, key, 0, key.length);
				byte[] iv = new byte[8];
				System.arraycopy(key, 0, iv, 0, 8);
				byte[] data = new byte[CryptoUtil.bytes2int(message_len) - 1 - key.length];
				//byte[] data = new byte[message.length - 4 - 1 - key.length];
				System.arraycopy(message, 4 + 1 + key.length, data, 0, data.length);
				de_data = TripleDes.decrypt(data, key, iv);
			}
			else if(type.equals(Arithmetic.AES))
			{
				key = new byte[CryptoConstant.AES_KEY_LEN];
				System.arraycopy(message, 5, key, 0, key.length);
				byte[] data = new byte[CryptoUtil.bytes2int(message_len) - 1 - key.length];
				//byte[] data = new byte[message.length - 4 - 1 - key.length];
				System.arraycopy(message, 4 + 1 + key.length, data, 0, data.length);
				de_data = Aes.decrypt(data, key, key);
			}
			// 判断该动态串与往次消息动态串是否相同
			if(session.getAttribute("identity") != null)
			{
				byte[] old_key = (byte[])session.getAttribute(ContentValue.KEY_BYTES);
				if(DynamicEncrypt.used(old_key, key))
				{
					if(log.isWarnEnabled())
					{
						log.warn("DynamicEncrypt.used");
					}
					return null;
				}
				if(key != null)
				{
					session.setAttribute(ContentValue.KEY_BYTES, key);
				}
			}
			if(de_data == null)
			{
				if(log.isErrorEnabled())
				{
					log.error("Crypto.decryptMessage de_data null. Arithmetic:"+type.toString());
				}
				return null;
			}
			byte[] de_data_len = CryptoUtil.int2bytes(de_data.length);
			byte[] newMessage = new byte[4 + de_data.length];
			System.arraycopy(de_data_len, 0, newMessage, 0, 4);
			System.arraycopy(de_data, 0, newMessage, 4, de_data.length);
			message_len = null;
			key = null;
			de_data = null;
			de_data_len = null;
			return newMessage;
		}
		else
		{
			byte[] data = new byte[CryptoUtil.bytes2int(message_len)];
			//byte[] data = new byte[message.length - 4];
			System.arraycopy(message, 4, data, 0, data.length);
			byte[] de_data = decrypt(cryptoType, data);
			byte[] de_data_len = CryptoUtil.int2bytes(de_data.length);
			byte[] newMessage = new byte[4 + de_data.length];
			System.arraycopy(de_data_len, 0, newMessage, 0, 4);
			System.arraycopy(de_data, 0, newMessage, 4, de_data.length);
			message_len = null;
			data = null;
			de_data = null;
			de_data_len = null;
			return newMessage;
		}
	}
	/**
	 * 加密原始字节数据
	 * @param cryptoType 加密算法类型见{@link Arithmetic}
	 * @param data
	 * @return
	 */
	public static byte[] encrypt(Arithmetic cryptoType, byte[] data)
	{
		if(cryptoType.equals(Arithmetic.DES))
		{
			return Des.encrypt(data);
		}
		else if(cryptoType.equals(Arithmetic.DES3))
		{
			return TripleDes.encrypt(data);
		}
		else if(cryptoType.equals(Arithmetic.AES))
		{
			return Aes.encrypt(data);
		}
		return null;
	}
	/**
	 * 解密原始字节数据
	 * @param cryptoType 加密算法类型见{@link Arithmetic}
	 * @param data
	 * @return
	 */
	public static byte[] decrypt(Arithmetic cryptoType, byte[] data)
	{
		if(cryptoType.equals(Arithmetic.DES))
		{
			return Des.decrypt(data);
		}
		else if(cryptoType.equals(Arithmetic.DES3))
		{
			return TripleDes.decrypt(data);
		}
		else if(cryptoType.equals(Arithmetic.AES))
		{
			return Aes.decrypt(data);
		}
		return null;
	}
}
