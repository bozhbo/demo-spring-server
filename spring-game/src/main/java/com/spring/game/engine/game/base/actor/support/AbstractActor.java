package com.snail.webgame.engine.game.base.actor.support;


import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.snail.webgame.engine.net.msg.impl.BaseMessage;

public abstract class AbstractActor implements IActor {
	
	private volatile AtomicBoolean inUse = new AtomicBoolean(false); 
	private Queue<BaseMessage> queue = new LinkedBlockingQueue<>(100);
	private Lock lock = new ReentrantLock();
	
	@Override
	public boolean lock() {
		return lock.tryLock();
	}
	
	@Override
	public void unlock() {
		lock.unlock();
	}
	
	public void addMessage(BaseMessage message) {
		queue.add(message);
	}
	
	public BaseMessage peekMessage() {
		return queue.peek();
	}
	
	public boolean addUse() {
		return inUse.compareAndSet(false, true);
	}
	
	public boolean removeUse() {
		return inUse.compareAndSet(true, false);
	}
}
