package com.snail.webgame.engine.gate.filter.crypto;

import java.util.Random;

/**
 * 摘要：辅助工具
 * @author leiqiang
 * @date   2014-7-31
 */
public class CryptoUtil {
    public static byte[] getRandomKey(int length)
    {
    	byte[] key = new byte[length];
    	StringBuffer buf = new StringBuffer("0123456789abcdefghigklmnopqrstuvwxyzABCDEFGHIGKLMNOPQRSTUVWXYZ");
    	for(int i = 0; i < length; i++)
    	{
    		key[i] = (byte)buf.charAt(new Random().nextInt(buf.length()));
    	}
    	return key;
    }
    public static byte[] int2bytes(int n)
    {
    	byte[] b = new byte[4];
    	for(int i = 0; i < 4; i++)
    	{
    		b[i] = (byte) (n >> (24 - i * 8));
    	}
    	return b;
    }
    public static int bytes2int(byte[] b)
    {
    	int value = 0;
    	for(int i = 0; i < b.length; i++)
    	{
    		int shift = (b.length - 1 - i) * 8;
    		value += (b[i] & 0xFF) << shift;
    	}
    	return value;
    }
}
