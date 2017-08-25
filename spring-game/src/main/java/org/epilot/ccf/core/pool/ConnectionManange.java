package org.epilot.ccf.core.pool;

public interface  ConnectionManange {
	public void connect();
	public void send(Object obj);
	public void close();
	public void back();
}
