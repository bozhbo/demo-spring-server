package com.snail.webgame.game.protocal.fight.service;

public class FightIdGen {

	private static int id = 1;

	public static synchronized int getSequenceId() {
		return id++;
	}

}
