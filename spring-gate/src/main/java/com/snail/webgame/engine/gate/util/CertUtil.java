package com.snail.webgame.engine.gate.util;

import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Decoder;

/**
 * DESede/DES/BlowFish DESede �� key������24���ֽ� des��
 * key������8���ֽ�,���ܽ����ٶ���� blowfish key�ǿɱ�� ������죬ǿ�����
 * 
 * @author tangjie
 */
public class CertUtil {

	public static String Md5(String plainText) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(plainText.getBytes());
			byte b[] = md.digest();
			return byte2hex(b);

		}
		catch (Exception e) {

		}
		return null;
	}

	/**
	 * ����
	 * 
	 * @param src
	 *            ���Դ
	 * @param key
	 *            ��Կ�����ȱ�����8�ı���
	 * @param name
	 *            �㷨�����
	 * @return ���ؼ��ܺ�����
	 * @throws Exception
	 */
	public static byte[] encrypt(byte[] src, byte[] key, String name) {

		try {
			SecretKeySpec securekey = new SecretKeySpec(key, name);
			// Cipher����ʵ����ɼ��ܲ���
			Cipher cipher = Cipher.getInstance(name);
			// ���ܳ׳�ʼ��Cipher����
			cipher.init(Cipher.ENCRYPT_MODE, securekey);
			// ���ڣ���ȡ��ݲ�����
			// ��ʽִ�м��ܲ���
			return cipher.doFinal(src);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * ����
	 * 
	 * @param src
	 *            ���Դ
	 * @param key
	 *            ��Կ�����ȱ�����8�ı���
	 * @param name
	 *            �㷨�����
	 * @return ���ؽ��ܺ��ԭʼ���
	 * @throws Exception
	 */
	public static byte[] decrypt(byte[] src, byte[] key, String name) {

		try {
			SecretKeySpec securekey = new SecretKeySpec(key, name);
			// Cipher����ʵ����ɽ��ܲ���
			Cipher cipher = Cipher.getInstance(name);
			// ���ܳ׳�ʼ��Cipher����
			cipher.init(Cipher.DECRYPT_MODE, securekey);
			// ���ڣ���ȡ��ݲ�����
			// ��ʽִ�н��ܲ���
			return cipher.doFinal(src);
		}
		catch (Exception e) {

		}
		return null;

	}

	/**
	 * �������
	 * 
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public static String decrypt(String data, String key, String name) {
		byte b[] = decrypt(hex2byte(data.getBytes()), key.getBytes(), name);
		if (b != null) {
			return new String(b);
		}
		else {
			return null;
		}

	}

	/**
	 * �������
	 * 
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public static String encrypt(String data, String key, String name) {

		return byte2hex(encrypt(data.getBytes(), key.getBytes(), name));

	}

	public static String encrypt(String data, byte[] b, String name) {

		return byte2hex(encrypt(data.getBytes(), b, name));

	}

	public static String byte2hex(byte[] b) // ������ת�ַ�
	{
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1)
				hs = hs + "0" + stmp;
			else
				hs = hs + stmp;

		}
		return hs.toUpperCase();
	}

	public static byte[] hex2byte(byte[] b) {
		if ((b.length % 2) != 0) {
			return null;
		}
		byte[] b2 = new byte[b.length / 2];
		for (int n = 0; n < b.length; n += 2) {
			String item = new String(b, n, 2);
			b2[n / 2] = (byte) Integer.parseInt(item, 16);
		}
		return b2;
	}

	public static long getTime(String s1) {

		try {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

			Date dt2 = (Date) formatter.parse(s1);

			return dt2.getTime();
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return 0;
	}

	public static String getBASE64(String s) {

		if (s == null) {
			return null;
		}

		try {
			return (new sun.misc.BASE64Encoder().encode(s.getBytes()));
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static String getFromBASE64(String s) {
		if (s == null) {
			return null;
		}

		BASE64Decoder decoder = new BASE64Decoder();

		try {
			byte[] b = decoder.decodeBuffer(s);
			return new String(b);

		}
		catch (Exception e) {
			return null;
		}
	}

	/**
	 * 生成license
	 * @param fileName 文件名
	 * @param ip IP地址
	 * @param date 有效日期  格式：(yyyy/MM/dd HH:mm:ss)
	 */
	public static String makeLicense(String fileName, String ip, String date) {

		// String port = "8082";
		// String pass1 = "0123456789012345";
		String pass1 = "4343344fsd43";
		String pass2 = "514fdfgdfggg";
		
		String strK = ip + fileName;
		String mm = Md5(strK);

		String m1 = mm.substring(0, 16);
		String m2 = mm.substring(16, 32);

		String strK1 = ip + fileName;

		String mm2 = Md5(strK1);

		String L1 = mm2.substring(0, 16);
		String L2 = mm2.substring(16, 32);

		long time = getTime(date);

		String n1 = encrypt(pass2, L2, "blowfish");

		int length1 = n1.length();

		String n2 = encrypt(pass1, L1, "blowfish");

		int length2 = n2.length();

		String p1 = String.format("%04d", length1);
		String p2 = String.format("%04d", length2);

		// System.out.println(p1);
		// System.out.println(p2);

		String str0 = ip + "," + time;

		String str2 = encrypt(str0, pass2, "blowfish");

		String str1 = p1 + n1 + str2;

		// System.out.println(str1);

		String str3 = encrypt(str1, pass1, "blowfish");

		String str4 = p2 + n2 + str3;

		// System.out.println(str4);

		String str5 = encrypt(str4, L2, "blowfish");

		// System.out.println(str5);

		// System.out.println(m1);
		// System.out.println(m2);
		return m1 + str5 + m2;

	}
}
