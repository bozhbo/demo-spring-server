package org.epilot.ccf.core.protocol;

/**
 * 
 * 消息实体抽象类
 */
abstract public class  MessageBody implements Cloneable{
	
	
	/**
	 * 设置协议字段顺序
	 * @return
	 */
	protected abstract void setSequnce(ProtocolSequence ps);
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
	public MessageBody clone(){
		MessageBody o = null;
        try{
            o = (MessageBody)super.clone();
        }catch(CloneNotSupportedException e){
            e.printStackTrace();
        }
        return o;
    }

}
