/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *  
 *    http://www.apache.org/licenses/LICENSE-2.0
 *  
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License. 
 *  
 */
package org.epilot.ccf.filter;


import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.mina.common.IdleStatus;
import org.apache.mina.common.IoFilterAdapter;
import org.apache.mina.common.IoSession;
import org.epilot.ccf.threadpool.ThreadPoolManager;




/**
 * 一个无序线程池，该池负责将待发送的消息进行编码
 * 默认Executors.newFixedThreadPool(50)
 * 使用该线程要保证每个线程的安全性.
 * @author tangjie
 *
 */
		
public class ExecutorFilterSend extends IoFilterAdapter {

	private final Executor executor;


	public ExecutorFilterSend() {
		
		if( ThreadPoolManager.getInstance().Pool("sendPool") == null)
		{
			executor=new ThreadPoolExecutor(
				50, 50,
	            0, TimeUnit.SECONDS,
	            new LinkedBlockingQueue<Runnable>(100),
	            new ThreadPoolExecutor.CallerRunsPolicy());
		}
		else
		{
			executor = ThreadPoolManager.getInstance().Pool("sendPool");
		}
	}


	public Executor getExecutor() {
		return executor;
	}

	private void fireEvent(NextFilter nextFilter, IoSession session,
			EventType type, Object data) {
		Event event = new Event(type, nextFilter, data);
		SessionBuffer buf = SessionBuffer.getSessionBuffer(session);
		
		executor.execute(new ProcessEventsRunnable(buf, event));
	}
	private static class SessionBuffer {
		private static final String KEY = SessionBuffer.class.getName()
				+ ".KEY";

		private static SessionBuffer getSessionBuffer(IoSession session) {
			synchronized (session) {
				SessionBuffer buf = (SessionBuffer) session.getAttribute(KEY);
				if (buf == null) {
					buf = new SessionBuffer(session);
					session.setAttribute(KEY, buf);
				}
				return buf;
			}
		}

		private final IoSession session;


		private SessionBuffer(IoSession session) {
			this.session = session;
		}
	}

	protected static class EventType {
		public static final EventType OPENED = new EventType("OPENED");

		public static final EventType CLOSED = new EventType("CLOSED");

		public static final EventType READ = new EventType("READ");

		public static final EventType WRITTEN = new EventType("WRITTEN");

		public static final EventType RECEIVED = new EventType("RECEIVED");

		public static final EventType SENT = new EventType("SENT");

		public static final EventType IDLE = new EventType("IDLE");

		public static final EventType EXCEPTION = new EventType("EXCEPTION");

		private final String value;

		private EventType(String value) {
			this.value = value;
		}

		public String toString() {
			return value;
		}
	}

	protected static class Event {
		private final EventType type;

		private final NextFilter nextFilter;

		private final Object data;

		Event(EventType type, NextFilter nextFilter, Object data) {
			this.type = type;
			this.nextFilter = nextFilter;
			this.data = data;
		}

		public Object getData() {
			return data;
		}

		public NextFilter getNextFilter() {
			return nextFilter;
		}

		public EventType getType() {
			return type;
		}
	}

	public void sessionCreated(NextFilter nextFilter, IoSession session) {
        nextFilter.sessionCreated(session);
    }

    public void sessionOpened(NextFilter nextFilter, IoSession session) {
    	 nextFilter.sessionOpened(session);
    }

    public void sessionClosed(NextFilter nextFilter, IoSession session) {
    	 nextFilter.sessionClosed(session);
    }

    public void sessionIdle(NextFilter nextFilter, IoSession session,
            IdleStatus status) {
    	 nextFilter.sessionIdle(session,status);
    }

    public void exceptionCaught(NextFilter nextFilter, IoSession session,
            Throwable cause) {
    	 nextFilter.exceptionCaught(session, cause);
    }

    public void messageReceived(NextFilter nextFilter, IoSession session,
            Object message) {
    	nextFilter.messageReceived(session, message);
    }

    public void messageSent(NextFilter nextFilter, IoSession session,
            Object message) {
    	 nextFilter.messageSent(session, message);
    }

	protected void processEvent(NextFilter nextFilter, IoSession session,
			EventType type, Object data) {
	
		if (type == EventType.RECEIVED) {
			nextFilter.messageReceived(session, data);
		} else if (type == EventType.SENT) {
			nextFilter.messageSent(session, data);		
		} else if (type == EventType.EXCEPTION) {
			nextFilter.exceptionCaught(session, (Throwable) data);
		} else if (type == EventType.IDLE) {
			nextFilter.sessionIdle(session, (IdleStatus) data);
		} else if (type == EventType.OPENED) {
			nextFilter.sessionOpened(session);
		} else if (type == EventType.CLOSED) {
			nextFilter.sessionClosed(session);
		}else if (type == EventType.WRITTEN) {
			nextFilter.filterWrite(session, (WriteRequest)data);
		}
	}

	public void filterWrite(NextFilter nextFilter, IoSession session,
			WriteRequest writeRequest) {
		fireEvent(nextFilter, session, EventType.WRITTEN, writeRequest);
		
	}

	public void filterClose(NextFilter nextFilter, IoSession session)
			throws Exception {
		nextFilter.filterClose(session);
	}

	private class ProcessEventsRunnable implements Runnable {
		private final SessionBuffer buffer;

		private final Event event;

		ProcessEventsRunnable(SessionBuffer buffer, Event event) {
			this.buffer = buffer;
			this.event = event;
		}

		public void run() {
			Event event = this.event;
		
			processEvent(event.getNextFilter(), buffer.session,
					event.getType(), event.getData());
			
		}
	}
}
