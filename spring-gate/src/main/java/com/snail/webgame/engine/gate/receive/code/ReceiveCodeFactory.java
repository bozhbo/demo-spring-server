package com.snail.webgame.engine.gate.receive.code;

import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

public class ReceiveCodeFactory implements ProtocolCodecFactory {
	private final ProtocolEncoder encoder;
	private final ProtocolDecoder decoder;
	
	public ReceiveCodeFactory()
	{
			encoder=new RevRequestEncoder();
			decoder =new RevRequestDecode();

	}
	
	public ProtocolDecoder getDecoder() throws Exception {


		return  decoder;
	}

	public ProtocolEncoder getEncoder() throws Exception {

		return encoder;
	}
	
}
