package com.snail.webgame.engine.game.base.info.common;

import java.lang.reflect.Method;

import com.snail.webgame.engine.net.processor.IGameProcessor;

public class MessageTypeInfo {

	private IGameProcessor processor; // 处理请求Processor对象
	private Method method;// 处理请求Processor中具体方法
	private int methodParameters; // 方法参数数量
	
	public IGameProcessor getProcessor() {
		return processor;
	}
	public void setProcessor(IGameProcessor processor) {
		this.processor = processor;
	}
	public Method getMethod() {
		return method;
	}
	public void setMethod(Method method) {
		this.method = method;
	}
	public int getMethodParameters() {
		return methodParameters;
	}
	public void setMethodParameters(int methodParameters) {
		this.methodParameters = methodParameters;
	}
	
}
