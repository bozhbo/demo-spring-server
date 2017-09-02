package com.snail.webgame.engine.gate.util;

import java.io.UnsupportedEncodingException;

public class Util {
	
	public static int IPtoInt(String ip) {
		int res = 0;
		if (ip != null && ip.length() > 0) {
			String[] aip = ip.split("[.]");
			if (aip != null && aip.length == 4) {
				for (int i = 0; i < 4; i++) {
					res = res + Integer.parseInt(aip[i]);
					if (i < 3)
						res = res << 8;
				}
			}
		}
		return res;
	}

	public static int byteArrayToInt(byte[] b) {
		int value = 0;
		for (int i = 0; i < b.length; i++) {
			int shift = (b.length - 1 - i) * 8;
			value += (b[i] & 0xFF) << shift;
		}
		return value;
	}
	
	public static short byteArrayToShort(byte[] b) {
		short value = 0;
		for (int i = 0; i < b.length; i++) {
			short shift = (short)((b.length - 1 - i) * 8);
			value += (b[i] & 0xFF) << shift;
		}
		return value;
	}

	public static long byteArrayToLong(byte[] b) {
		long value = 0;
		for (int i = 0; i < b.length; i++) {
			long shift = i * 8;
			value += (b[i] & 0xFF) << shift;
		}
		return value;
	}

	public static byte[] int2bytes(int n) {
		byte[] b = new byte[4];
		for (int i = 0; i < 4; i++) {
			b[i] = (byte) (n >> (24 - i * 8));
		}
		return b;
	}

	public static byte[] encodeStringB(String str) {

		if (str != null && str.length() > 0) {
			byte[] m = null;
			try {
				m = str.getBytes("UTF-8");
			} catch (UnsupportedEncodingException e) {

			}

			if (m != null && m.length > 0) {
				byte n[] = new byte[m.length + 2];

				System.arraycopy(m, 0, n, 2, m.length);
				byte k[] = shortToBytes((short) m.length);
				System.arraycopy(k, 0, n, 0, 2);

				return n;
			}

		}
		return shortToBytes((short) 0);
	}

	public static byte[] shortToBytes(short sNum) {
		byte[] bytesRet = new byte[2];
		bytesRet[0] = (byte) ((sNum >> 8) & 0xFF);
		bytesRet[1] = (byte) (sNum & 0xFF);
		return bytesRet;
	}
	public static void main(String args[])
	{
		byte[] bs = new byte[]{1,2,3,4,3,5};
		System.out.println(bytesToStr(bs));
	}
	public static String bytesToStr(byte[] bs)
	{
		if(bs == null)
		{
			return "";
		}
		StringBuffer sb = new StringBuffer();
		sb.append("[len=");
		sb.append(bs.length);
		sb.append("{");
		for(int i = 0; i < bs.length; i++)
		{
			sb.append(bs[i]);
			sb.append(",");
		}
		sb.append("}]");
		return sb.toString();
	}
}
