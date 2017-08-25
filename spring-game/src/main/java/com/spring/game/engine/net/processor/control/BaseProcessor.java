package com.snail.webgame.engine.net.processor.control;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.engine.component.room.protocol.info.IRoomHead;
import com.snail.webgame.engine.game.base.cache.MessageTypeMap;
import com.snail.webgame.engine.game.base.info.common.MessageTypeInfo;
import com.snail.webgame.engine.net.msg.impl.BaseMessage;

/**
 * 
 * 类介绍:Processor基类 所有消息通信的入口类，此类调用继承于{#BaseAction}类的具体类的相应接口
 *
 * @author zhoubo
 * @version 1.0.0
 * @since 2016年1月21日
 */
public final class BaseProcessor {

	private static final Logger logger = LoggerFactory.getLogger("engine");

	public void processor(BaseMessage message) {
		IRoomHead messageHead = message.getiRoomHead();
		MessageTypeInfo messageTypeInfo = MessageTypeMap.getMessageTypeInfo(messageHead.getMsgType());

		if (messageTypeInfo == null) {
			logger.error("BaseProcessor : there is no IGameProcessor for messagetype = " + Integer.toHexString(messageHead.getMsgType()));
		} else {
			try {
				switch (messageTypeInfo.getMethodParameters()) {
				case 0:
					messageTypeInfo.getMethod().invoke(messageTypeInfo.getProcessor());
					break;
				case 1:
					messageTypeInfo.getMethod().invoke(messageTypeInfo.getProcessor(), message);
					break;
				case 2:
					messageTypeInfo.getMethod().invoke(messageTypeInfo.getProcessor(), message, message.getiRoomBody());
					break;

				default:
					break;
				}
			} catch (Exception e) {
				logger.error("BaseProcessor : error", e);
				
				// TODO
				// 增加全局错误码
			}

			logger.info(String.format("MessageTyep = %s request end", Integer.toHexString(messageHead.getMsgType())));
		}
	}
}
