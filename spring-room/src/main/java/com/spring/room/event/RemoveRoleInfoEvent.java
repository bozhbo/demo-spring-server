package com.spring.room.event;

import com.spring.logic.message.request.server.RemoveRoleReq;
import com.spring.logic.room.event.IRoomEvent;

public class RemoveRoleInfoEvent implements IRoomEvent {

	private RemoveRoleReq req;
	
	public RemoveRoleInfoEvent(RemoveRoleReq req) {
		this.req = req;
	}

	public RemoveRoleReq getReq() {
		return req;
	}

	public void setReq(RemoveRoleReq req) {
		this.req = req;
	}
	
	
}
