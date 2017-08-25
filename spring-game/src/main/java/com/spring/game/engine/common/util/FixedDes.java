package com.snail.webgame.engine.common.util;

import java.nio.charset.Charset;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * 使用固定KEY IV的DES加密算法
 * @author leiqiang
 *
 */
public class FixedDes {
	private static final Charset charset = Charset.forName("UTF-8");
    private static final byte[] DES_KEY_LEN8 = "snail123".getBytes(charset);
    private static final byte[] DES_IV_LEN8 = "123snail".getBytes(charset);
	public FixedDes() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * 加密
	 * @param data
	 * @return
	 */
	public static String encrypt(String data)
	{
		if(data == null || data.length() == 0)
		{
			return "";
		}
		byte[] endata = encrypt(data.getBytes(charset),DES_KEY_LEN8, DES_IV_LEN8);
		StringBuffer sb = new StringBuffer();
		for(int i = 0; i < endata.length; i++)
		{
			sb.append(endata[i]);
			sb.append("#");
		}
		return sb.toString().substring(0, sb.length()-1);
	}
	/**
	 * 加密
	 * @param data
	 * @param key
	 * @param iv
	 * @return
	 */
	private static byte[] encrypt(byte[] data, byte[] key, byte[] iv)
	{
		SecretKeySpec secretKey = new SecretKeySpec(key,"DES");
		try {
			Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
			IvParameterSpec ivSpec = new IvParameterSpec(iv);
			cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);
			return cipher.doFinal(data);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return null;
	}
	
	/**
	 * 解密
	 * @param data
	 * @return
	 */
	public static String decrypt(String data)
	{
		if(data == null || data.length() == 0)
		{
			return "";
		}
		String[] strs = data.split("#");
		byte[] strbs = new byte[strs.length];
		for(int i = 0; i < strs.length; i++)
		{
			strbs[i] = Byte.parseByte(strs[i]);
		}
		return new String(decrypt(strbs,DES_KEY_LEN8, DES_IV_LEN8),charset);
	}
	/**
	 * 解密
	 * @param data
	 * @param key
	 * @param iv
	 * @return
	 */
	private static byte[] decrypt(byte[] data, byte[] key, byte[] iv)
	{
		SecretKeySpec secretKey = new SecretKeySpec(key,"DES");
		try {
			Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
			IvParameterSpec ivSpec = new IvParameterSpec(iv);
			cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);
			return cipher.doFinal(data);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return null;
	}
}
