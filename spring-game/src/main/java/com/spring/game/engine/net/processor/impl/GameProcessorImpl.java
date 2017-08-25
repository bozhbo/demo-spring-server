package com.snail.webgame.engine.net.processor.impl;

import com.snail.webgame.engine.component.room.protocol.info.IRoomBody;
import com.snail.webgame.engine.game.base.actor.ActorInfo;
import com.snail.webgame.engine.game.base.actor.RoleActor;
import com.snail.webgame.engine.game.base.annotation.GameFunction;
import com.snail.webgame.engine.game.base.init.GameConfig;
import com.snail.webgame.engine.game.thread.ExecServiceTask;
import com.snail.webgame.engine.net.msg.impl.BaseMessage;
import com.snail.webgame.engine.net.msg.impl.GameMessageReq;
import com.snail.webgame.engine.net.processor.IGameProcessor;

public class GameProcessorImpl implements IGameProcessor {

	private int msgType;
	private Class<IRoomBody> req;
	private int startMsgType;
	private int endMsgType;

	public int getMsgType() {
		return msgType;
	}

	public void setMsgType(int msgType) {
		this.msgType = msgType;
	}

	public Class<IRoomBody> getReq() {
		return req;
	}

	public void setReq(Class<IRoomBody> req) {
		this.req = req;
	}
	
	public int getStartMsgType() {
		return startMsgType;
	}

	public void setStartMsgType(int startMsgType) {
		this.startMsgType = startMsgType;
	}

	public int getEndMsgType() {
		return endMsgType;
	}

	public void setEndMsgType(int endMsgType) {
		this.endMsgType = endMsgType;
	}

	protected void call(BaseMessage message, GameFunction<IRoomBody, ActorInfo, IRoomBody> f, final RoleActor ... roleActor) {
		if (message.getiRoomBody() != null && (message.getiRoomBody() instanceof GameMessageReq)) {
			if (!((GameMessageReq)message.getiRoomBody()).validate()) {
				// 验证失败,返回错误
				
			}
		}
		
		if (roleActor == null || roleActor.length == 0) {
			return;
		}
		
		roleActor[0].addMessage(message);
		
		if (roleActor[0].addUse()) {
			// 未添加
			GameConfig.getExecThreadPool().getThreadPool().execute(new ExecServiceTask(roleActor[0], f, roleActor));
		}
	}
}
