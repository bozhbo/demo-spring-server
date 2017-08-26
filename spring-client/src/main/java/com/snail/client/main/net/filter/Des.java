package com.snail.client.main.net.filter;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * 摘要：对称加密算法-DES
 * @author leiqiang
 * @date   2014-7-17
 */
public class Des {
	/**
	 * 加密
	 * @param data
	 * @return
	 */
	public static byte[] encrypt(byte[] data)
	{
		return encrypt(data,CryptoConstant.DES_KEY_LEN8, CryptoConstant.DES_IV_LEN8);
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
	public static byte[] decrypt(byte[] data)
	{
		return decrypt(data,CryptoConstant.DES_KEY_LEN8, CryptoConstant.DES_IV_LEN8);
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
