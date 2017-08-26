package com.snail.client.main.net.filter;

import java.net.InetSocketAddress;

import org.apache.mina.common.IoFilterAdapter;
import org.apache.mina.common.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CryptoIoFilter extends IoFilterAdapter {

	private static final Logger log = LoggerFactory.getLogger(CryptoIoFilter.class.getName());

	@Override
	public void messageReceived(NextFilter nextFilter, IoSession session, Object message) throws Exception {
		if (message instanceof byte[]) {
			byte b[] = Crypto.decryptMessage(Arithmetic.DES, (byte[]) message, session);
			// 异常处理
			if (b == null) {
				if (log.isWarnEnabled()) {
					InetSocketAddress address = (InetSocketAddress) session.getRemoteAddress();
					String ip = address.getAddress().getHostAddress();
					String port = String.valueOf(address.getPort());
					log.warn("Crypto.decryptMessage error,session will close... ip=" + ip + ":" + port + ",encryptType="
							+ 1);
				}
				session.close();
				return;
			}
			nextFilter.messageReceived(session, b);
		} else {
			nextFilter.messageReceived(session, message);
		}
	}

	@Override
	public void filterWrite(NextFilter nextFilter, IoSession session, WriteRequest writeRequest) throws Exception {
		if (writeRequest.getMessage() instanceof byte[]) {
			byte b[] = Crypto.encryptMessage(Arithmetic.DES, (byte[]) writeRequest.getMessage());
			// 异常处理
			if (b == null) {
				if (log.isWarnEnabled()) {
					InetSocketAddress address = (InetSocketAddress) session.getRemoteAddress();
					String ip = address.getAddress().getHostAddress();
					String port = String.valueOf(address.getPort());
					log.warn("Crypto.encryptMessage error,session will close... ip=" + ip + ":" + port + ",encryptType="
							+ 1);
				}
				session.close();
				return;
			}
			nextFilter.filterWrite(session,
					new WriteRequest(b, writeRequest.getFuture(), writeRequest.getDestination()));
		} else {
			nextFilter.filterWrite(session, writeRequest);
		}
	}
}
