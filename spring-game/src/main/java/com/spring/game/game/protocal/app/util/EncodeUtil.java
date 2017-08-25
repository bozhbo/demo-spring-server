package com.snail.webgame.game.protocal.app.util;

import java.security.MessageDigest;

/**
 * 
 * @author shenggm
 * @since 2013-10-12
 * @version V1.0.0
 */
public class EncodeUtil {
	public static String md5_32(String string) {
		if (isEmpty(string))
			return "";
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
		try {
			byte[] bytes = string.getBytes();
			MessageDigest messageDigest = MessageDigest.getInstance("MD5");
			messageDigest.update(bytes);
			byte[] updateBytes = messageDigest.digest();
			int len = updateBytes.length;
			char myChar[] = new char[len * 2];
			int k = 0;
			for (int i = 0; i < len; i++) {
				byte byte0 = updateBytes[i];
				myChar[k++] = hexDigits[byte0 >>> 4 & 0x0f];
				myChar[k++] = hexDigits[byte0 & 0x0f];
			}
			return new String(myChar);
		} catch (Exception e) {
			return null;
		}
	}

	// 计费 2 帐号的编码方法（java实现）
	public static String enCode(byte[] bsrc) { // 加码函数，将系统用到的控制符变成转义符号
		String dest = "", str;
		byte bb;
		int num;
		if (bsrc == null) {
			return "";
		}
		for (int ii = 0; ii < bsrc.length; ii++) {
			bb = bsrc[ii];
			if (bb >= 0) {
				num = bb;
			} else {
				num = (bb & 0x7F) + (1 << 7);
			}
			str = Integer.toHexString(num);
			if (str.length() < 2) {
				str = "0" + str;
			}
			dest += str.toUpperCase();
		}
		return dest;
	}

	public static boolean isEmpty(String str) {
		return null == str || "" == str || str.equals("") || str.length() == 0 ? true : false;

	}

}
