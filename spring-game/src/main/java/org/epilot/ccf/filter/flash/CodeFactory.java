package org.epilot.ccf.filter.flash;

import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

public class CodeFactory implements ProtocolCodecFactory {
	private final ProtocolEncoder encoder;
	private final ProtocolDecoder decoder;
	
	public CodeFactory(String name)
	{
			encoder=new RequestEncoder(name);
			decoder =new RequestDecode(name);

	}
	
	public ProtocolDecoder getDecoder() throws Exception {


		return  decoder;
	}

	public ProtocolEncoder getEncoder() throws Exception {

		return encoder;
	}
	
}
