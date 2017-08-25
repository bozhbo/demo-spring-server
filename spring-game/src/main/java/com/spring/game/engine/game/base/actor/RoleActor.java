package com.snail.webgame.engine.game.base.actor;

import com.snail.webgame.engine.game.base.actor.support.AbstractActor;

public class RoleActor extends AbstractActor {

	private ActorInfo actorInfo;

	public RoleActor(ActorInfo actorInfo) {
		super();
		this.actorInfo = actorInfo;
	}

	public ActorInfo getActorInfo() {
		return actorInfo;
	}

	public void setActorInfo(ActorInfo actorInfo) {
		this.actorInfo = actorInfo;
	}

	@Override
	public Integer getId() {
		return actorInfo.getId();
	}

}
