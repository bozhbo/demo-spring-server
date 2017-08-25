package com.snail.webgame.game.protocal.hero.recruitList;

import java.util.Comparator;

public class DisRecruitHeroComparator implements Comparator<QueryRecuitHeroRe> {
	
	@Override
	public int compare(QueryRecuitHeroRe queryRecuitHeroRe1, QueryRecuitHeroRe queryRecuitHeroRe2) {

		if(queryRecuitHeroRe1.getLackNum()>queryRecuitHeroRe2.getLackNum()){
			return 1;
		}else if(queryRecuitHeroRe1.getLackNum()==queryRecuitHeroRe2.getLackNum()){
			if(queryRecuitHeroRe1.getNo()>queryRecuitHeroRe2.getNo()){
				return 1;
			}else if(queryRecuitHeroRe1.getNo()==queryRecuitHeroRe2.getNo()){
				return 0;
			}else{
				return -1;
			}
		}else{
			return -1;
		}
		
	}

}
