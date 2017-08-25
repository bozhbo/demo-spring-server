package com.snail.webgame.game.protocal.rank.service;

import java.util.Comparator;

import com.snail.webgame.game.info.RoleInfo;

public class RoleFightValueComparator implements Comparator<RoleInfo> {

	public int compare(RoleInfo o1, RoleInfo o2) {

		RoleInfo roleInfo1 = (RoleInfo) o1;
		RoleInfo roleInfo2 = (RoleInfo) o2;
		
		int total1 = roleInfo1.getFightValue();
		int total2 = roleInfo2.getFightValue();
		
		if(total1 < total2)
		{
			return 1;
		}
		else if(total1 == total2)
		{
			return 0;
		}
		else
		{
			return -1;
		}
	}

}
