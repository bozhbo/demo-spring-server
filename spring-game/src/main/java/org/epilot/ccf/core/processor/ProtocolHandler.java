package org.epilot.ccf.core.processor;




/**
 * 协议处理器
 *
 */
public class ProtocolHandler implements Runnable{
	
	private Request request;
	private Response response;
	private ProtocolProcessor protocolProcessor;
	public ProtocolHandler(Request request,Response response,ProtocolProcessor protocolProcessor)
	{	
		this.request = request;
		this.response = response;
		this.protocolProcessor = protocolProcessor;
	}
	/**
	 * 执行处理
	 *
	 */
	public void run() {

		
		protocolProcessor.execute(request, response);
		

	}

}
