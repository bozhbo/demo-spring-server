package com.spring.room.event;

import com.spring.logic.message.request.server.DeployRoleReq;
import com.spring.logic.room.event.IRoomEvent;

public class DeployRoleInfoEvent implements IRoomEvent {

	private DeployRoleReq req;
	
	public DeployRoleInfoEvent(DeployRoleReq req) {
		this.req = req;
	}

	public DeployRoleReq getReq() {
		return req;
	}

	public void setReq(DeployRoleReq req) {
		this.req = req;
	}
	
	
}
