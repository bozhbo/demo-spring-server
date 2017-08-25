package org.epilot.ccf.core.protocol;



abstract public class MessageHeader implements Cloneable {

	public abstract int getLength();
	public abstract void setLength(int length);
	public abstract long getProtocolId();
	public abstract void setProtocolId(long Id);
	public  String headerReserve;//保留字段
	/**
	 * 设置协议字段顺序
	 * @return
	 */
	protected abstract void setSequnce(ProtocolSequence ps);
	
	 
	public String getHeaderReserve() {
		return headerReserve;
	}
	public void setHeaderReserve(String headerReserve) {
		this.headerReserve = headerReserve;
	}
	/**
	 * 获得协议字段顺序
	 * @return
	 */
	public  ProtocolSequence getSequnce()
	{	
		ProtocolSequence ps =new ProtocolSequence();
		setSequnce(ps);
		return ps;
	}
	
	public MessageHeader clone(){
		MessageHeader o = null;
        try{
            o = (MessageHeader)super.clone();
        }catch(CloneNotSupportedException e){
            e.printStackTrace();
        }
        return o;
    }

}
