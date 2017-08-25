package com.snail.webgame.game.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import com.snail.webgame.game.info.HeroRetrInfo;


/**
 * 魂匣对象缓存类
 */
public class HeroRetrvitMap {

	// <heroType,HeroRetrInfo> 魂匣Type 1-每日英雄1 2-每日英雄2 3-每日英雄3 4-每周英雄
	private static ConcurrentHashMap<Integer, HeroRetrInfo> map = new ConcurrentHashMap<Integer, HeroRetrInfo>();

	/**
	 * 添加魂匣
	 * 
	 * @param heroType
	 * @param heroRetrInfo
	 */
	public static void addHeroRetrvit(int heroType,HeroRetrInfo heroRetrInfo) {
		if(heroType > 4 || heroType < 1 || heroRetrInfo == null){
			return;
		}
		map.put(heroType, heroRetrInfo);
	}

	/**
	 * 根据heroType获取英雄
	 * 
	 * @param heroType 1-周英雄 2，3，4-日英雄
	 * @return
	 */
	public static HeroRetrInfo getHeroInfo(int heroType) {

		return map.get(heroType);
	}

	/**
	 * 根据条件获得英雄列表
	 * 
	 * @param type 1-每日 2-每周
	 * @return
	 */
	public static List<HeroRetrInfo> getHeroByType(int type) {
		List<HeroRetrInfo> list = new ArrayList<HeroRetrInfo>();
		if(type == 1)
		{
			for(int i=0;i<=3;i++)
			{
				HeroRetrInfo heroRetrInfo = map.get(i);
				if(heroRetrInfo != null){
					list.add(heroRetrInfo);
				}
			}
		}else if(type == 2){
			HeroRetrInfo heroRetrInfo = map.get(4);
			if(heroRetrInfo != null){
				list.add(heroRetrInfo);
			}
		}
		return list;
	}

}
