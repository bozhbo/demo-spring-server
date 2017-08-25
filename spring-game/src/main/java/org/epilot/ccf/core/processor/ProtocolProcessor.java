package org.epilot.ccf.core.processor;
/**
 * 
 * 具体的处理过程，在多线程中执行，
 * 此框架中execute是不安全的
 *
 */
public  abstract class ProtocolProcessor {


	public abstract void execute(Request request,Response response);

}
