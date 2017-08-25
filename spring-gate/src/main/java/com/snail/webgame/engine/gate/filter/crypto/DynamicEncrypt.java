package com.snail.webgame.engine.gate.filter.crypto;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import com.snail.webgame.engine.gate.config.WebGameConfig;

/**
 * 动态加密
 * 
 * @author leiqiang
 * @date 2013-8-27 上午9:57:13
 */
public class DynamicEncrypt
{
	private static ConcurrentHashMap<Integer, byte[]>	map	= new ConcurrentHashMap<Integer, byte[]>();
	private static byte[][]					bb	= null;
	
	/**
	 * 初始化底层消息通讯加密算法类型及密钥串
	 * 
	 * @param initNum
	 *            初始化密钥串数量 0-不初始化任何密钥串（即使用固定算法1固定串“ABCDXXYY”）
	 */
	public static void initDE(int initNum)
	{
		if(initNum > 0)
		{
			bb = new byte[initNum][];
			for(int i = 0; i < initNum; i++)
			{
				byte[] deInfo = new byte[9];
				deInfo[0] = (byte)(new Random().nextInt(4) + 1);
				byte[] str8 = WebGameConfig.getRandomString(8).getBytes(Charset.forName("UTF-8"));
				System.arraycopy(str8, 0, deInfo, 1, 8);
				bb[i] = deInfo;
			}
		}
	}

	/**
	 * 保存使用过的串
	 * 
	 * @param roleId
	 * @param deInfo
	 */
	public static void addUsedDE(int roleId, byte[] deInfo)
	{
		map.put(roleId, deInfo);
	}
	
	/**
	 * 判断该密钥是否使用过
	 * @param old_key
	 * @param key
	 * @return
	 */
	public static boolean used(byte[] old_key, byte[] key)
	{
		if(old_key != null && key != null)
		{
			if(old_key.length != key.length)
			{
				return false;
			}
			for(int i = 0; i < key.length; i++)
			{
				if(old_key[i] != key[i])
				{
					return false;
				}
			}
			return true;
		}
		return false;
	}
}
