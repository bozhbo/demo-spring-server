package com.snail.webgame.game.cache;

import java.util.HashSet;
import java.util.Set;

public class QueryChargePointList {
	
	private static Set<Integer> roleGiftList = new HashSet<Integer>();

	public static Set<Integer> getGiftRoleList() {
		return roleGiftList;
	}

	public static void addGiftRole(int roleId){
		roleGiftList.add(roleId);
	}
	
	public static void removeGiftRole(int roleId){
		
		if(roleGiftList.contains(roleId)){
			roleGiftList.remove(roleId);
		}
	}
	
}
