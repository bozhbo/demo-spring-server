package com.snail.webgame.game.cache;

import java.util.ArrayList;
import java.util.List;

public class BlackWriteAccountMap {
	
	private static List<String> blackAccountList = new ArrayList<String>();
	private static List<String> writeAccountList = new ArrayList<String>();
	
	public static List<String> getBlackAccountList() {
		return blackAccountList;
	}
	public static void setBlackAccountList(List<String> blackAccountList) {
		BlackWriteAccountMap.blackAccountList = blackAccountList;
	}
	public static List<String> getWriteAccountList() {
		return writeAccountList;
	}
	public static void setWriteAccountList(List<String> writeAccountList) {
		BlackWriteAccountMap.writeAccountList = writeAccountList;
	}
	
	
	
	
}
