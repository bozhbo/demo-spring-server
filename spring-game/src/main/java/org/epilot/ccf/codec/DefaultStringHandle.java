package org.epilot.ccf.codec;


import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

import org.apache.mina.common.ByteBuffer;
import org.epilot.ccf.core.util.AbstractStringHandle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * 默认的处理方法 UTF-16BE(LE)类似与C++后面以(0x00 0x00)宽字符
 * @author tangjie
 *
 */
public class DefaultStringHandle extends AbstractStringHandle{
	protected static final Logger log =LoggerFactory.getLogger("ccf");
	
	public String decodeStringB(ByteBuffer buffer) {
	
		Charset charset = Charset.forName("UTF-16BE");//little endian 每个字符都占两个字节
		CharsetDecoder decoder =  charset.newDecoder();
		try {
			String str = buffer.getString(decoder);
			return str;
		} catch (CharacterCodingException e) {
			log.error("",e);
			return null;	
		}
		
	}


	public String decodeStringL(ByteBuffer buffer) {


		Charset charset = Charset.forName("UTF-16LE");//little endian 每个字符都占两个字节
		CharsetDecoder decoder =  charset.newDecoder();
		try {
			String str = buffer.getString(decoder);
			
			return str;
		} catch (CharacterCodingException e) {
			log.error("",e);
			return null;	
		}
		
	}

	
	public byte[] encodeStringB(String str) {
		if(str==null)
		{
			str="";
		}
		byte [] b = new byte[(1+str.length())*2];
		try {
			byte c[] = str.getBytes("UTF-16BE");
			System.arraycopy(c, 0, b, 0, c.length);
			b[b.length-2] = 0;
			b[b.length-1] = 0;
		} catch (Exception e) {
			log.error("",e);
		}
		return b;
	}

	
	public byte[] encodeStringL(String str) {
		if(str==null)
		{
			str="";
		}
		byte [] b = new byte[(1+str.length())*2];
		try {
			byte c[] = str.getBytes("UTF-16LE");
			System.arraycopy(c, 0, b, 0, c.length);
			b[b.length-2] = 0;
			b[b.length-1] = 0;
			
		} catch (Exception e) {
			log.error("",e);
		}
		
		return b;
	}

}
