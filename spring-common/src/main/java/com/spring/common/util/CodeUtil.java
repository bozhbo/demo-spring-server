package com.spring.common.util;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * DESede/DES/BlowFish
 * DESede 的 key长度是24个字节
 * des的 key长度是8个字节,加密解密速度最快
 * blowfish key是可变的 加密最快，强度最高
 * @author tangjie
 */
public class CodeUtil {

	private static Map<String, byte[]> secretKeyMap = new HashMap<String, byte[]>();
	public static String Md5(String plainText ) 
	{ 
		try { 
			MessageDigest md = MessageDigest.getInstance("MD5"); 
			md.update(plainText.getBytes()); 
			byte b[] = md.digest(); 
			return byte2hex(b);
			
		} 
		catch (Exception e)
		{ 
		 
		} 
		return null;
	} 
	 /** 
      * 加密 
      * @param src 数据源 
      * @param key 密钥，长度必须是8的倍数 
      * @param name 算法的名称
      * @return  返回加密后的数据 
      * @throws Exception 
      */ 
	public static byte[] encrypt(byte[] src, byte[] key,String name) { 
		 
		try
		{
			 SecretKeySpec securekey = new SecretKeySpec(key,name);
	         // Cipher对象实际完成加密操作 
	         Cipher cipher = Cipher.getInstance(name); 
	         // 用密匙初始化Cipher对象 
	         cipher.init(Cipher.ENCRYPT_MODE, securekey); 
	         // 现在，获取数据并加密 
	         // 正式执行加密操作 
	         return cipher.doFinal(src); 
		}
		catch(Exception e)
		{
			 
		}
		 return null;
      } 
	/**
	 * 获得密钥
	 * @param name 算法名称
	 * @return
	 */
	 public static byte[] getSecretKey(String name){
		 try {
			 
			 if(secretKeyMap.containsKey(name)){
				 return secretKeyMap.get(name);
			 }
			//获取密钥生成器
			 KeyGenerator kg = KeyGenerator.getInstance(name);
			 
			 SecureRandom secureRandom =  SecureRandom.getInstance("SHA1PRNG");
			 secureRandom.setSeed(("1234567890123456789").getBytes());
			 
			 int len = 0;
			 if("DES".equals(name)){
				 len = 56;
			 }
			 else if("DESede".equals(name)){
				 len = 112;
			 }
			 else if("AES".equals(name)){
				 len = 128;
			 }
			 else if("BlowFish".equals(name)){
				 len = 32;
			 }
			 kg.init(len, secureRandom);
			 
			 SecretKey key = kg.generateKey();
			 byte[] keys = key.getEncoded();
			 
			 secretKeyMap.put(name, keys);
			 return keys;
		}
		catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	 }
	  /** 
      * 解密 
      * @param src 数据源 
      * @param key 密钥，长度必须是8的倍数 
      *  @param name 算法的名称
      * @return 返回解密后的原始数据 
      * @throws Exception 
      */ 
	 public static byte[] decrypt(byte[] src, byte[] key, String name){ 

		 try
		 {
			 SecretKeySpec securekey = new SecretKeySpec(key,name);
	         // Cipher对象实际完成解密操作 
	         Cipher cipher = Cipher.getInstance(name); 
	         // 用密匙初始化Cipher对象 
	         cipher.init(Cipher.DECRYPT_MODE, securekey); 
	         // 现在，获取数据并解密 
	         // 正式执行解密操作 
	         return cipher.doFinal(src); 
		 }
		 catch(Exception e)
		 {
			e.printStackTrace();
		 }
		 return null;
		 
      } 
	 /** 
	 * 密码解密 
	 * @param data 
	 * @return 
	 * @throws Exception 
	 */ 
	 public  static String decrypt(String data,String key,String name){ 
		 byte b[] =  decrypt(hex2byte(data.getBytes()), key.getBytes(),name);
		 if(b!=null)
		 {
			 return new String(b); 
		 }
		 else
		 {
			 return null;
		 }
	 
	 } 
	 /** 
	 * 密码加密 
	 * @param password 
	 * @return 
	 * @throws Exception 
	 */ 
	 public  static String encrypt(String data,String key,String name){ 
	 
		 return  byte2hex(encrypt(data.getBytes(),key.getBytes(),name)); 
		  
	 } 
	 
	 public static String byte2hex(byte[] b) //二行制转字符串
	 {
	     String hs="";
	     String stmp="";
	      for (int n=0;n<b.length;n++)
	      {
	       stmp=(java.lang.Integer.toHexString(b[n] & 0XFF));
	       if (stmp.length()==1) hs=hs+"0"+stmp;
	       else hs=hs+stmp;
	
	      }
	     return hs.toUpperCase();
	  }
	 
   public static byte[] hex2byte(byte[] b) 
   { 
     if((b.length%2)!=0)  
     {
        return null;
     }
       byte[] b2 = new byte[b.length/2]; 
       for (int n = 0; n < b.length; n+=2)
       { 
           String item = new String(b,n,2); 
           b2[n/2] = (byte)Integer.parseInt(item,16); 
       } 
         return b2; 
   }
	 
   public static void main(String args[])
   {
	  
	   String code = encrypt("test","1234567812345678","blowfish");
	   System.out.println(code);
	   //String code1 = decrypt(code,"1234567812345678","aes");
	  // System.out.println(code1);
	  // System.out.println(Md5("12345fsdfsdfsdeqweqweqwewqe"));
	   
   }
}
