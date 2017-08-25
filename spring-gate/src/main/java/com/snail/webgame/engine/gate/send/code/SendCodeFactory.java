package com.snail.webgame.engine.gate.send.code;

import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

public class SendCodeFactory implements ProtocolCodecFactory {
	private final ProtocolEncoder encoder;
	private final ProtocolDecoder decoder;
	
	public SendCodeFactory()
	{
			encoder=new SendRequestEncoder();
			decoder =new SendRequestDecode();

	}
	
	public ProtocolDecoder getDecoder() throws Exception {


		return  decoder;
	}

	public ProtocolEncoder getEncoder() throws Exception {

		return encoder;
	}
	
}
