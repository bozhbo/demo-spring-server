package com.snail.webgame.engine.game.thread;

import com.snail.webgame.engine.component.room.protocol.info.IRoomBody;
import com.snail.webgame.engine.game.base.actor.ActorInfo;
import com.snail.webgame.engine.game.base.actor.RoleActor;
import com.snail.webgame.engine.game.base.annotation.GameFunction;
import com.snail.webgame.engine.game.base.info.msg.DummyReq;
import com.snail.webgame.engine.game.base.info.msg.DummyResp;
import com.snail.webgame.engine.game.base.init.GameConfig;
import com.snail.webgame.engine.net.msg.impl.BaseMessage;

public class ExecServiceTask implements Runnable {
	
	private RoleActor roleActor;
	private GameFunction<IRoomBody, ActorInfo, IRoomBody> f;
	private RoleActor[] roleActors;
	
	public ExecServiceTask(RoleActor roleActor, GameFunction<IRoomBody, ActorInfo, IRoomBody> f, final RoleActor ... roleActors) {
		this.roleActor = roleActor;
		this.f = f;
		this.roleActors = roleActors;
	}
	
	@Override
	public void run() {
		int loop = 5;
		
		while (loop-- > 0) {
			BaseMessage message = roleActor.peekMessage();
			
			if (message == null) {
				// 执行结束
				if (roleActor.removeUse()) {
					// 移除
					break;
				} else {
					// 移除失败
					GameConfig.getExecThreadPool().getThreadPool().execute(this);
				}
			} else {
				boolean flag = true;
				int index = -1;
				ActorInfo[] actorInfos = new ActorInfo[roleActors.length];
				
				try {
					for (int i = 0; i < roleActors.length; i++) {
						flag = (flag & roleActors[i].lock());
						
						if (!flag) {
							// 分配失败放入队列
							break;
						} else {
							actorInfos[i] = roleActors[i].getActorInfo();
							index++;
						}
					}
					
					if (flag) {
						IRoomBody resp = f.apply(message.getiRoomBody() == null ? new DummyReq() : message.getiRoomBody(), actorInfos);
						
						if (resp instanceof DummyResp) {
							// 无返回值
						} else {
							message.getiRoomHead().setMsgType(resp.getMsgType());
							message.setiRoomBody(resp);
							message.sendMessage();
						}
					} else {
						break;
					}
				} catch (Exception e) {
					// TODO: handle exception
				} finally {
					for (int i = 0; i <= index; i++) {
						roleActors[i].unlock();
					}
					
					actorInfos = null;
				}
			}
		}
	}

}
