package com.snail.webgame.engine.game.base.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 类介绍:消息读写注解
 *
 * @author zhoubo
 * @version 1.0.0
 * @since 2016年1月21日
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface MessageType {

	public int inputMsgType();
}
