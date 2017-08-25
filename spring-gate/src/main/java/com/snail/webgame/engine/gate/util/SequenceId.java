package com.snail.webgame.engine.gate.util;

public class SequenceId {

	private static int id =100 ;
	
	public static synchronized int getSequenceId()
	{
		return id++;
	}
}
