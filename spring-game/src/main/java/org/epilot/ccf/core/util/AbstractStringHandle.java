package org.epilot.ccf.core.util;

import org.apache.mina.common.ByteBuffer;


/**
 * 字符串处理的抽象方法
 * @author tangjie
 *
 */
public abstract class AbstractStringHandle implements Cloneable {
	/**
	 * 字符串编码成Big Endian
	 * @param str
	 * @return
	 */
	public abstract byte[] encodeStringB(String str);
	/**
	 * 字符串编码Little Endian
	 * @param str
	 * @return
	 */
	public abstract byte[] encodeStringL(String str);
	/**
	 * 把Big Endian解码成字符串
	 * @return
	 */
	public abstract String decodeStringB(ByteBuffer buffer);
	/**
	 * 把Little Endian解码成字符串
	 * @return
	 */
	public abstract String decodeStringL(ByteBuffer buffer);
	
	public AbstractStringHandle clone(){
		AbstractStringHandle o = null;
        try{
            o = (AbstractStringHandle)super.clone();
        }catch(CloneNotSupportedException e){
            e.printStackTrace();
        }
        return o;
    }
}
