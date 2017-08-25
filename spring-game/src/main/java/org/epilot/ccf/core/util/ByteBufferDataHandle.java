package org.epilot.ccf.core.util;


import java.nio.ByteOrder;

import org.apache.mina.common.ByteBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 提供对网络字节流读取的方法
 *
 */
public class ByteBufferDataHandle {
	
	private  ByteBuffer buffer;
	private static final Logger log =LoggerFactory.getLogger("ccf");
	
	public ByteBufferDataHandle(ByteBuffer buffer)
	{
		this.buffer =buffer;
	}
	public int remain()
	{
		return buffer.remaining();
	}
	public void readLittleByte(byte [] b)
	{
		buffer.order(ByteOrder.LITTLE_ENDIAN).get(b);
	}
	
	public void readBigByte(byte [] b)
	{
		buffer.order(ByteOrder.BIG_ENDIAN).get(b);
	}
	
	public void writeLittleByte(byte [] b)
	{
		buffer.order(ByteOrder.LITTLE_ENDIAN).put(b);
	}
	
	public void writeBigByte(byte [] b)
	{
		buffer.order(ByteOrder.BIG_ENDIAN).put(b);
	}
	
	public void setBigData(byte b)
	{
		buffer.put(b);
	}
	public byte getBigByte()
	{
		if(buffer.remaining()>=1)
		{
			return buffer.get();
		}
		else
		{
			if(log.isErrorEnabled())
			{
				log.error("getBigByte() method: Buffer remaining"+buffer.remaining());
			}
			return 0;
		}
	}
	public void setLittleData(byte b)
	{
		buffer.put(b);
	}
	public byte getLittleByte()
	{
		if(buffer.remaining()>=1)
		{
			return buffer.get();
		}
		else
		{
			if(log.isErrorEnabled())
			{
				log.error("getLittleByte()method:Buffer remaing"+buffer.remaining());
			}
			return 0;
		}
	}
	
	
	public void setBigData(Short b)
	{
		buffer.putShort(b);
	}
	public Short getBigShort()
	{
		if(buffer.remaining()>=2)
		{
			return buffer.getShort();
		}
		else
		{
			if(log.isErrorEnabled())
			{
				log.error("getBigShort() method: Buffer remaing"+buffer.remaining());
			}
			return 0;
		}
	}
	public void setLittleData(Short b)
	{
		buffer.order(ByteOrder.LITTLE_ENDIAN).putShort(b);
	}
	public Short getLittleShort()
	{
		if(buffer.remaining()>=2)
		{
			return buffer.order(ByteOrder.LITTLE_ENDIAN).getShort();
		}
		else
		{
			if(log.isErrorEnabled())
			{
				log.error("getLittleShort() method: Buffer remaing "+buffer.remaining());
			}
				return 0;
		}
	}	
	
	
	public void setBigData(int b)
	{
		buffer.putInt(b);
	}
	public int getBigInt()
	{
		if(buffer.remaining()>=4)
		{
			return buffer.getInt();
		}
		else
		{
			if(log.isErrorEnabled())
			{
				log.error("getBigInt()method: Buffer remaing "+buffer.remaining());
			}
			return 0;
		}
	}
	public void setLittleData(int b)
	{
		buffer.order(ByteOrder.LITTLE_ENDIAN).putInt(b);
	}
	public int getLittleInt()
	{
		if(buffer.remaining()>=4)
		{
			return buffer.order(ByteOrder.LITTLE_ENDIAN).getInt();
		}
		else
		{
			if(log.isErrorEnabled())
			{
				log.error("getLittleInt()method: Buffer remaing "+buffer.remaining());
			}
			
			return 0;
		}
	}
	
	public void setBigData(float b)
	{
		buffer.putFloat(b);
	}
	public float getBigFloat()
	{
		if(buffer.remaining()>=4)
		{
			return buffer.getFloat();
		}
		else
		{
			if(log.isErrorEnabled())
			{
				log.error("getBigFloat() method: Buffer remaing"+buffer.remaining());
			}
			
			return 0;
		}
	}
	public void setLittleData(float b)
	{
		buffer.order(ByteOrder.LITTLE_ENDIAN).putFloat(b);
	}
	public float getLittleFloat()
	{
		if(buffer.remaining()>=4)
		{
			return buffer.order(ByteOrder.LITTLE_ENDIAN).getFloat();
		}else
		{
			if(log.isErrorEnabled())
			{
				log.error("getLittleFloat() method: Buffer remaing"+buffer.remaining());
			}
			
			return 0;
		}
		
	}
	
	public void setBigData(long b)
	{
		buffer.putLong(b);
	}
	public long getBigLong()
	{
		return buffer.getLong();
	}
	public void setLittleData(long b)
	{
		
		buffer.order(ByteOrder.LITTLE_ENDIAN).putLong(b);

	}
	public long getLittleLong()
	{
		return buffer.order(ByteOrder.LITTLE_ENDIAN).getLong();
	}
	
	public void setBigData(double b)
	{
		buffer.putDouble(b);
	}
	public double getBigDouble()
	{
		if(buffer.remaining()>=8)
		{
			return buffer.getDouble();
		}
		else
		{
			if(log.isErrorEnabled())
			{
				log.error("getBigDouble() method: Buffer remaing"+buffer.remaining());
			}
			return 0;
		}
	}
	public void setLittleData(double b)
	{
		buffer.order(ByteOrder.LITTLE_ENDIAN).putDouble(b);
	}
	public double getLittleDouble()
	{
		if(buffer.remaining()>=8)
		{
			return buffer.order(ByteOrder.LITTLE_ENDIAN).getDouble();
		}
		else
		{
			if(log.isErrorEnabled())
			{
				log.error("getLittleDouble() method: Buffer remaing"+buffer.remaining());
			}
				return 0;
		}
	}
	
	public void setBigData(String str,AbstractStringHandle stringHandle)
	{
		buffer.put(stringHandle.encodeStringB(str));
	}
	
	public String getBigString(AbstractStringHandle stringHandle)
	{
		return stringHandle.decodeStringB(buffer);
	}
	
	public void setLittleData(String str,AbstractStringHandle stringHandle)
	{
		buffer.put(stringHandle.encodeStringL(str));
	}
	
	public String getLittleString(AbstractStringHandle stringHandle)
	{
		return stringHandle.decodeStringL(buffer);
	}
}
