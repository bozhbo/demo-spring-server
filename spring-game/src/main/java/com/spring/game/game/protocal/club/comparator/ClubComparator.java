package com.snail.webgame.game.protocal.club.comparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.snail.webgame.game.protocal.club.entity.ClubInfoRe;


public class ClubComparator {
	//等级
	private Comparator<ClubInfoRe> comByLevel = new Comparator<ClubInfoRe>(){

		@Override
		public int compare(ClubInfoRe o1, ClubInfoRe o2) {
			if(o1.getLevel() < o2.getLevel()){
				return 1;
			}else if(o1.getLevel() > o2.getLevel()){
				return - 1;
			}
			
			return 0;
		}
		
	};
	
	//建设度
	private Comparator<ClubInfoRe> comByBuild = new Comparator<ClubInfoRe>(){

		@Override
		public int compare(ClubInfoRe o1, ClubInfoRe o2) {
			if(o1.getBuild() < o2.getBuild()){
				return 1;
			}else if(o1.getBuild() > o2.getBuild()){
				return -1;
			}
			return 0;
		}
		
	};
	
	//创建时间
	private Comparator<ClubInfoRe> comByTime = new Comparator<ClubInfoRe>(){

		@Override
		public int compare(ClubInfoRe o1, ClubInfoRe o2) {
			if(o1.getCreateTime() > o2.getCreateTime()){
				return 1;
			}else if(o1.getCreateTime() < o2.getCreateTime()){
				return -1;
			}
			return 0;
		}
		
	};
	
	public ClubComparator(List<ClubInfoRe> list){
		List<Comparator<ClubInfoRe>> comList = new ArrayList<Comparator<ClubInfoRe>>();
		comList.add(comByLevel);
		comList.add(comByBuild);
		comList.add(comByTime);
		
		sort(list, comList);
			
	}

	private void sort(List<ClubInfoRe> list, final List<Comparator<ClubInfoRe>> comList) {
		if(list == null){
			return;
		}
		
		Comparator<ClubInfoRe> comparator = new Comparator<ClubInfoRe>(){

			@Override
			public int compare(ClubInfoRe o1, ClubInfoRe o2) {
				for(Comparator<ClubInfoRe> com : comList){
					int compara = com.compare(o1, o2);
					if(compara > 0){
						return 1;
					}else if( compara < 0){
						return -1;
					}
				}
				
				return 0;
			}};
		
		Collections.sort(list, comparator);
	}
	
	
}
