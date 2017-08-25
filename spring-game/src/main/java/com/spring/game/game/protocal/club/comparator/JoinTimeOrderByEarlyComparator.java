package com.snail.webgame.game.protocal.club.comparator;

import java.util.Comparator;

import com.snail.webgame.game.info.RoleClubMemberInfo;

//按照公会加入先后顺序排序,早入会的在前面
public class JoinTimeOrderByEarlyComparator implements Comparator<RoleClubMemberInfo> {

	@Override
	public int compare(RoleClubMemberInfo o1, RoleClubMemberInfo o2) {
		if(o1.getJoinTime().getTime() > o2.getJoinTime().getTime()){
			return 1;
		}else if(o1.getJoinTime().getTime() < o2.getJoinTime().getTime()){
			return -1;
		}
		return 0;
	}

}
