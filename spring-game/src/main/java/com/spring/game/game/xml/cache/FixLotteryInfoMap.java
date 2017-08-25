package com.snail.webgame.game.xml.cache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.snail.webgame.game.xml.info.FixLotteryConfigInfo;

public class FixLotteryInfoMap {
	//key 主将ID 对应Hero.xml value<key- 第几次十连抽 value 每次的固定奖励>
	// 银子十连抽
	private static Map<Integer, Map<Integer, List<FixLotteryConfigInfo>>> moneyMap = new HashMap<Integer, Map<Integer, List<FixLotteryConfigInfo>>>();
	// 金子十练抽
	private static Map<Integer, Map<Integer, List<FixLotteryConfigInfo>>> coinMap = new HashMap<Integer, Map<Integer, List<FixLotteryConfigInfo>>>();
	
	// 武将十练抽<主武将编号,<第几次,List<奖励>>>
	private static Map<Integer, Map<Integer, List<FixLotteryConfigInfo>>> heroMap = new HashMap<Integer, Map<Integer, List<FixLotteryConfigInfo>>>();

	/**
	 * 
	 * @param costType 消耗货币类型
	 * @param roleType 主将ID Hero.xml
	 * @param no 抽奖次数
	 * @param list 当次抽奖的奖励
	 */
	public static void addFixLotteryInfoList(int costType, int roleType, int no, List<FixLotteryConfigInfo> list){
		if(costType == 1){
			//银子
			if(!moneyMap.containsKey(roleType)){
				Map<Integer, List<FixLotteryConfigInfo>> map = new HashMap<Integer, List<FixLotteryConfigInfo>>();
				map.put(no, list);
				moneyMap.put(roleType, map);
			
			}else{
				moneyMap.get(roleType).put(no, list);
			}
			
			
		}else if(costType == 2){
			//金子
			if(!coinMap.containsKey(roleType)){
				Map<Integer, List<FixLotteryConfigInfo>> map = new HashMap<Integer, List<FixLotteryConfigInfo>>();
				map.put(no, list);
				coinMap.put(roleType, map);
			
			}else{
				coinMap.get(roleType).put(no, list);
			}
		}else if(costType == 3){
			//武将抽
			if(!heroMap.containsKey(roleType)){
				Map<Integer, List<FixLotteryConfigInfo>> map = new HashMap<Integer, List<FixLotteryConfigInfo>>();
				map.put(no, list);
				heroMap.put(roleType, map);
			
			}else{
				heroMap.get(roleType).put(no, list);
			}
		}
		
		
	}

	public static boolean fixLotteryNoCheck(int costType, int roleType, int no){
		if(costType == 1){
			if(moneyMap.get(roleType) == null) return false;
			if(moneyMap.get(roleType).get(no) == null) return false;
			
		}else if (costType == 2){
			if(coinMap.get(roleType) == null) return false;
			if(coinMap.get(roleType).get(no) == null) return false;
		}else if (costType == 3){
			if(heroMap.get(roleType) == null) return false;
			if(heroMap.get(roleType).get(no) == null) return false;
		}
			
		return true;
	}

	/**
	 * 
	 * @param costType 1-银子 2-金子  3-武将
	 * @param roleType 主武将编号
	 * @param no 第几次抽奖
	 * @return
	 */
	public static List<FixLotteryConfigInfo> getFixLotteryInfoList(int costType, int roleType, int no){
		if(costType == 1){
			 Map<Integer, List<FixLotteryConfigInfo>> map = moneyMap.get(roleType);
			 if(map == null){
				 return null;
			 }else{
				 return map.get(no);
			 }
			
		}else if (costType == 2){
			 Map<Integer, List<FixLotteryConfigInfo>> map = coinMap.get(roleType);
			 if(map == null){
				 return null;
			 }else{
				 return map.get(no);
			 }
		}else if (costType == 3){
			 Map<Integer, List<FixLotteryConfigInfo>> map = heroMap.get(roleType);
			 if(map == null){
				 return null;
			 }else{
				 return map.get(no);
			 }
		}
		
		return null;
			
	}
}
