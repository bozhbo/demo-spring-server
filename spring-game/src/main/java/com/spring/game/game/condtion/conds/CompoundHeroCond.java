package com.snail.webgame.game.condtion.conds;

import com.snail.webgame.game.cache.BagItemMap;
import com.snail.webgame.game.cache.HeroInfoMap;
import com.snail.webgame.game.common.xml.cache.HeroXMLInfoMap;
import com.snail.webgame.game.common.xml.info.HeroXMLInfo;
import com.snail.webgame.game.condtion.AbstractConditionCheck;
import com.snail.webgame.game.condtion.CheckDataChg;
import com.snail.webgame.game.info.BagItemInfo;
import com.snail.webgame.game.info.RoleInfo;

/**
 * 检查是否有武将能招募
 * 
 * @author nijp
 *
 */
public class CompoundHeroCond extends AbstractConditionCheck {

	@Override
	public AbstractConditionCheck generate(String[] agrs) {
		return new CompoundHeroCond();
	}

	@Override
	public int checkCond(RoleInfo roleInfo, int action,
			CheckDataChg questInProgress, int index, Object obj) {
		
		for (HeroXMLInfo heroXMLInfo : HeroXMLInfoMap.getHeroMap().values()) {
			if (heroXMLInfo.getRace() == 0) {//去除主角英雄
				 continue;
			}
			if (HeroInfoMap.getHeroInfoByNo(roleInfo.getId(), heroXMLInfo.getNo()) == null) {// 没有的英雄才出现在列表中
				int costSilver = 0;// 消耗的银子
				int costStarNum = heroXMLInfo.getChipNum();// 消耗的星石
				// 消耗的银子和星石为小于初始星级的总和
//				for (HeroStarXMLUpgrade heroStarXMLUpgrade : heroXMLInfo.getStarMap().values()) {
//					if (heroStarXMLUpgrade.getNo() <= heroXMLInfo.getStar()) {
//						costSilver = costSilver + heroStarXMLUpgrade.getNeedSilver();
//						costStarNum = costStarNum + heroStarXMLUpgrade.getNeedChips();
//					}
//				}
				int remianStarNum = 0;
				BagItemInfo bagItemInfo = BagItemMap.getBagItembyNo(roleInfo, heroXMLInfo.getChipNo());
				if (bagItemInfo != null) {
					remianStarNum = bagItemInfo.getNum();
				}
				//验证星石
				if (costStarNum > remianStarNum) {
					continue;
				}
		
				// 验证银子
				if (roleInfo.getMoney() < costSilver) {
					continue;
				}
				
				updateValue(index, questInProgress, 1);
				return 1;
			}
		}
		
		return 0;
	}
	
}
