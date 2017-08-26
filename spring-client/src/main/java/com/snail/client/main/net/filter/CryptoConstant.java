package com.snail.client.main.net.filter;

import java.nio.charset.Charset;

/**
 * 文件说明：
 * @author leiqiang
 * @date   2014-7-17
 */
public class CryptoConstant {

	
	public static final int DES_KEY_LEN = 8;
    public static final byte[] DES_KEY_LEN8 = "AABBCCDD".getBytes(Charset.forName("UTF-8"));
    public static final byte[] DES_IV_LEN8 = "AAAABBBB".getBytes(Charset.forName("UTF-8"));

    public static final int BLOWFISH_KEY_LEN = 8;
    public static final byte[] BLOWFISH_KEY_LEN8 = "AABBCCDD".getBytes(Charset.forName("UTF-8"));
    public static final byte[] BLOWFISH_IV_LEN8 = "AAAABBBB".getBytes(Charset.forName("UTF-8"));
    
	public static final int AES_KEY_LEN = 16;
    public static final byte[] AES_KEY_LEN16 = "AABBCCDDAABBCCDD".getBytes(Charset.forName("UTF-8"));;
    public static final byte[] AES_IV_LEN16 = "AAAABBBBAAAABBBB".getBytes(Charset.forName("UTF-8"));

    public static final int DES3_KEY_LEN = 24;
    public static final byte[] DES3_KEY_LEN24 = "abcdefghigklmnopqrstuvwx".getBytes(Charset.forName("UTF-8"));
    public static final byte[] DES3_IV_LEN8 = "abcdefgh".getBytes(Charset.forName("UTF-8"));
}
