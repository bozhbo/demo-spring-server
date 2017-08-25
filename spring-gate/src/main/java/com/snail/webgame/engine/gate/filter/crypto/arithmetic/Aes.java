package com.snail.webgame.engine.gate.filter.crypto.arithmetic;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import com.snail.webgame.engine.gate.filter.crypto.CryptoConstant;

/**
 * 摘要：对称加密算法-AES
 * @author leiqiang
 * @date   2014-7-17
 */
public class Aes {
	/**
	 * 加密
	 * @param data
	 * @return
	 */
	public static byte[] encrypt(byte[] data)
	{
		return encrypt(data,CryptoConstant.AES_KEY_LEN16, CryptoConstant.AES_IV_LEN16);
	}
	/**
	 * 加密
	 * @param data
	 * @param key
	 * @param iv
	 * @return
	 */
	public static byte[] encrypt(byte[] data, byte[] key, byte[] iv)
	{
		SecretKeySpec secretKey = new SecretKeySpec(key,"AES");
		try {
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
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
	public static byte[] decrypt(byte[] data)
	{
		return decrypt(data,CryptoConstant.AES_KEY_LEN16, CryptoConstant.AES_IV_LEN16);
	}
	/**
	 * 解密
	 * @param data
	 * @param key
	 * @param iv
	 * @return
	 */
	public static byte[] decrypt(byte[] data, byte[] key, byte[] iv)
	{
		SecretKeySpec secretKey = new SecretKeySpec(key,"AES");
		try {
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			IvParameterSpec ivSpec = new IvParameterSpec(iv);
			cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);
			return cipher.doFinal(data);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return null;
	}
}
