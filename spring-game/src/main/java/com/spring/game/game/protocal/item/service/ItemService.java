package com.snail.webgame.game.protocal.item.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.game.cache.BagItemMap;
import com.snail.webgame.game.cache.EquipInfoMap;
import com.snail.webgame.game.cache.HeroInfoMap;
import com.snail.webgame.game.common.ActionType;
import com.snail.webgame.game.common.ErrorCode;
import com.snail.webgame.game.common.GameValue;
import com.snail.webgame.game.common.fightdata.DropInfo;
import com.snail.webgame.game.common.util.RandomUtil;
import com.snail.webgame.game.common.xml.cache.HeroXMLInfoMap;
import com.snail.webgame.game.common.xml.cache.VipXMLInfoMap;
import com.snail.webgame.game.common.xml.info.HeroXMLInfo;
import com.snail.webgame.game.common.xml.info.VipType;
import com.snail.webgame.game.condtion.AbstractConditionCheck;
import com.snail.webgame.game.condtion.ConditionType;
import com.snail.webgame.game.core.GameLogService;
import com.snail.webgame.game.core.SceneService;
import com.snail.webgame.game.dao.EquipDAO;
import com.snail.webgame.game.dao.HeroDAO;
import com.snail.webgame.game.dao.ItemDAO;
import com.snail.webgame.game.dao.WeaponDao;
import com.snail.webgame.game.info.BagItemInfo;
import com.snail.webgame.game.info.EquipInfo;
import com.snail.webgame.game.info.HeroInfo;
import com.snail.webgame.game.info.RoleInfo;
import com.snail.webgame.game.info.RoleLoadInfo;
import com.snail.webgame.game.info.RoleWeaponInfo;
import com.snail.webgame.game.protocal.campaign.service.CampaignMgtService;
import com.snail.webgame.game.protocal.challenge.service.ChallengeService;
import com.snail.webgame.game.protocal.equip.service.EquipService;
import com.snail.webgame.game.protocal.fight.fightend.BattlePrize;
import com.snail.webgame.game.protocal.funcopen.service.FuncOpenMgtService;
import com.snail.webgame.game.protocal.hero.service.HeroService;
import com.snail.webgame.game.protocal.recruit.recruit.ChestItemRe;
import com.snail.webgame.game.protocal.redPoint.service.RedPointMgtService;
import com.snail.webgame.game.protocal.rolemgt.service.RoleService;
import com.snail.webgame.game.protocal.shizhuang.service.ShizhuangService;
import com.snail.webgame.game.xml.cache.EquipXMLInfoMap;
import com.snail.webgame.game.xml.cache.PropBagXMLMap;
import com.snail.webgame.game.xml.cache.PropXMLInfoMap;
import com.snail.webgame.game.xml.info.DropXMLInfo;
import com.snail.webgame.game.xml.info.EquipXMLInfo;
import com.snail.webgame.game.xml.info.PropXMLInfo;

public class ItemService {

	// 记录获得的英雄转化为灵魂碎片标示
	public static final String RECORD_HERO_TO_SOUL = "getHeroToSoul";
	private static final Logger logger = LoggerFactory.getLogger("logs");

	
	//掉落功能请不要再添加方法,两种就可以支持，一种是解析PropBag,另一种是手动添加奖励类型,如不支持可以扩展参数
	
	/**
	 * 获得奖励,解析配置文件获得掉落方法
	 * @param action
	 * @param roleInfo
	 * @param prizeXmls
	 * @param cardBag 翻牌掉落包
	 * @param dropList 获得的物品提示
	 * @param fpPrizeList 客户端翻牌用
	 * @param mustDrops 必掉物品、必不掉物品
	 * @param chestItemList 抽奖推送给客户端掉了的物品 (方便客户端播武将特效)
	 * @param flag  true-掉落 false-不掉
	 * @return
	 */
	public static int addPrizeForPropBag(int action, RoleInfo roleInfo, List<DropXMLInfo> prizeXmls,String cardBag,
			List<BattlePrize> dropList,List<BattlePrize> fpPrizeList, List<DropInfo> mustDrops, List<ChestItemRe> chestItemList, boolean flag)
	{
		List<DropInfo> addList = new ArrayList<DropInfo>();
		//解析掉落
		getDropXMLInfo(roleInfo, prizeXmls, addList);
		
		// 必掉物品处理
		if (mustDrops != null && mustDrops.size() > 0) {
			Iterator<DropInfo> a = addList.iterator();
			while (a.hasNext()) {
				String itemNo = a.next().getItemNo();
				boolean remove = false;
				for(DropInfo mustDrop :mustDrops){
					if (itemNo.equalsIgnoreCase(mustDrop.getItemNo())) {
						remove = true;
						break;
					}
				}
				if(remove){
					a.remove();
				}
			}

			if (flag) {
				addList.addAll(mustDrops);
			}
		}

		return addPrize(action, roleInfo,addList,cardBag,
				null,null,null,
				null,null, dropList,fpPrizeList, chestItemList, true);
	}

	
	/**
	 * 获得奖励，手动添加奖励方法
	 * @param action
	 * @param roleInfo
	 * @param addList 道具,装备,神兵,资源,武将
	 * @param cardBag 翻牌掉落包
	 * @param getResourceNum 资源变动
	 * @param getItemNum 道具变动
	 * @param addEquipIds 装备变动（掉装备）
	 * @param weapList 神兵变动
	 * @param heroIds 武将变动（掉经验武将等级变化）
	 * @param dropList 获得的所有物品
	 * @param fpPrizeList 客户端翻牌用
	 * @param chestItemList 抽奖推送给客户端掉了的物品
	 * @param needFresh true-刷新,false-不刷新
	 * @return
	 */
	public static int addPrize(int action, RoleInfo roleInfo, List<DropInfo> addList,String cardBag,
			Map<String, Integer> getResourceNum, Map<Integer, Integer> getItemNum, Map<Integer, Integer> addEquipIds,
			List<RoleWeaponInfo> weapList, List<Integer> heroIds, List<BattlePrize> dropList,List<BattlePrize> fpPrizeList,
			List<ChestItemRe> chestItemList, boolean needFresh) 
	{
		
		if((action == ActionType.action66.getType() || 
				action == ActionType.action67.getType() || action == ActionType.action68.getType()
				||action == ActionType.action69.getType() || action == ActionType.action437.getType()
				|| action == ActionType.action438.getType()) && chestItemList == null)
		{
			chestItemList = new ArrayList<ChestItemRe>();
		}
		
		// 相同物品归类,抽奖、扫荡时,物品不归类
		if(addList.size() >= 2 && action != ActionType.action66.getType() 
				&& action != ActionType.action67.getType() && action != ActionType.action68.getType()
				&& action != ActionType.action69.getType() && action != ActionType.action437.getType()
				&& action != ActionType.action438.getType())
		{
			for(int i = 0 ; i < addList.size() ; i++)
			{
				DropInfo dropInfo1 = addList.get(i);
				if(dropInfo1 != null && dropInfo1.getItemNum() == 0)
				{
					addList.remove(i);
					i--;
				}
				
				for(int j = i+1 ; j < addList.size() ; j++)
				{
					DropInfo dropInfo2 = addList.get(j);
					
					if(dropInfo1 != null && dropInfo2!= null 
							&& dropInfo1.getItemNo().equalsIgnoreCase(dropInfo2.getItemNo())
							&& dropInfo1.getSweep() == dropInfo2.getSweep())
					{
						dropInfo1.setItemNum(dropInfo1.getItemNum() + dropInfo2.getItemNum());
						dropInfo2.setItemNum(0);
					}
				}
			}
		}
		// 处理极效活动多倍掉落
		handlerDoubleDrop(action,addList);		
		//vip 双倍收益
		
		handlerDoubleDrop(ActionType.attrParseType(action), roleInfo, addList);
		
		//已拥有武将转化成星石
		checkHeroToSoulItem(roleInfo, addList);
		
		if (dropList != null) 
		{
			// 记录获得的奖励,给客户端结算面板使用
			getPrize(roleInfo,addList, dropList);
		}
		
		//记录翻牌
		if (cardBag != null)
		{
			if(fpPrizeList == null)
			{
				fpPrizeList = new ArrayList<BattlePrize>();
				
				if(logger.isWarnEnabled()){
					logger.warn("ItemService  :  fpPrizeList is null");
				}
			}
			
			if(cardBag != null && cardBag.length() > 0)
			{
				//翻牌操作
				getFpPrizeList(cardBag, fpPrizeList);
			}
			if(fpPrizeList.size() > 0)
			{
				// 增加翻牌奖励
				DropInfo fpDropInfo  = new DropInfo(fpPrizeList.get(0).getNo(), fpPrizeList.get(0).getNum());
				if(fpDropInfo != null)
				{
					boolean newItem = true;
					for(int i = 0 ; i < addList.size() ; i++)
					{
						DropInfo dropInfo1 = addList.get(i);
						if(dropInfo1 != null && dropInfo1.getItemNo().equalsIgnoreCase(fpDropInfo.getItemNo()))
						{
							dropInfo1.setItemNum(dropInfo1.getItemNum() + fpDropInfo.getItemNum());
							newItem = false;
							break;
						}
					}
					
					if(newItem)
					{
						addList.add(fpDropInfo);
					}
				}
			}
		}

		return prizeToRole(action, roleInfo, addList, getResourceNum, getItemNum, addEquipIds, weapList, 
				heroIds, chestItemList, needFresh);
	}
	
	
	
	/**
	 * 你争我夺掉落整理
	 * @param action
	 * @param roleInfo
	 * @param addList 道具,装备,神兵,资源,武将
	 * @param cardBag 翻牌掉落包
	 * @param dropList 获得的所有物品
	 * @param fpPrizeList 客户端翻牌用
	 * @return
	 */
	public static int getLootPrize1(int action, RoleInfo roleInfo, List<DropInfo> addList,String cardBag,
			List<BattlePrize> dropList,List<BattlePrize> fpPrizeList) 
	{
		// 处理极效活动多倍掉落
		handlerDoubleDrop(action,addList);		
		//vip 双倍收益
		
		handlerDoubleDrop(ActionType.attrParseType(action), roleInfo, addList);
		
		//已拥有武将转化成星石
		checkHeroToSoulItem(roleInfo, addList);
		
		if (dropList != null) 
		{
			// 记录获得的奖励,给客户端结算面板使用
			getPrize(roleInfo,addList, dropList);
		}
		
		//记录翻牌
		if (cardBag != null)
		{
			if(fpPrizeList == null)
			{
				fpPrizeList = new ArrayList<BattlePrize>();
				
				if(logger.isWarnEnabled()){
					logger.warn("ItemService  :  fpPrizeList is null");
				}
			}
			
			if(cardBag != null && cardBag.length() > 0)
			{
				//翻牌操作
				getFpPrizeList(cardBag, fpPrizeList);
			}
			if(fpPrizeList.size() > 0)
			{
				// 增加翻牌奖励
				DropInfo fpDropInfo  = new DropInfo(fpPrizeList.get(0).getNo(), fpPrizeList.get(0).getNum());
				if(fpDropInfo != null)
				{
					boolean newItem = true;
					for(int i = 0 ; i < addList.size() ; i++)
					{
						DropInfo dropInfo1 = addList.get(i);
						if(dropInfo1 != null && dropInfo1.getItemNo().equalsIgnoreCase(fpDropInfo.getItemNo()))
						{
							dropInfo1.setItemNum(dropInfo1.getItemNum() + fpDropInfo.getItemNum());
							newItem = false;
							break;
						}
					}
					
					if(newItem)
					{
						addList.add(fpDropInfo);
					}
				}
			}
		}
		
		return 1;
	}
	

	
	/**
	 * 获得奖励(对处理过的资源添加到角色身上)
	 * @param action
	 * @param roleInfo
	 * @param resourceMap 资源
	 * @param getItemNum 道具变动
	 * @param addEquipIds 装备变动
	 * @param weapList 神兵变动
	 * @param heroIds 武将变动（掉经验武将等级变化和新增武将）
	 * @param needFresh true-刷新,false-不刷新
	 * @return
	 */
	public static int prizeToRole(int action, RoleInfo roleInfo, List<DropInfo> addList,
			Map<String, Integer> resourceMap, Map<Integer, Integer> getItemNum, Map<Integer, Integer> addEquipIds,
			List<RoleWeaponInfo> weapList, List<Integer> heroIds, List<ChestItemRe> chestItemList,
			boolean needFresh) 
	{
		
		// 获得的资源
		if(resourceMap == null)
		{
			resourceMap = new HashMap<String, Integer>();
		}
		// 获得的道具
		if(getItemNum == null)
		{
			getItemNum = new HashMap<Integer, Integer>();
		}
		// 获得的装备
		if(addEquipIds == null)
		{
			addEquipIds = new HashMap<Integer, Integer>();
		}
		// 获得的神兵
		if(weapList == null)
		{
			weapList = new ArrayList<RoleWeaponInfo>();
		}
		// 武将变化
		if(heroIds == null)
		{
			heroIds = new ArrayList<Integer>();
		}
		
		int exp = 0;
		List<DropInfo> bagList = new ArrayList<DropInfo>();
		List<Integer> heroNos = new ArrayList<Integer>();
		
		for(DropInfo dropInfo : addList)
		{
			if(dropInfo == null)
			{
				logger.error("#########this chest item error,action="+action);
				continue;
			}
			
			String itemNo = dropInfo.getItemNo();
			int itemNum = dropInfo.getItemNum();
			
			if (AbstractConditionCheck.isResourceType(itemNo))
			{
				if (itemNo.equals(ConditionType.TYPE_EXP.getName()))
				{
					// 给予武将经验
					exp += itemNum;
					continue;
				}
				
				resourceMap.put(itemNo, itemNum);
				
				// 给予资源奖励
				if (!RoleService.addRoleRoleResource(action, roleInfo, ConditionType.attrParseName(itemNo), itemNum,null)) 
				{
					return ErrorCode.BAG_ERROR_5;
				}
			}
			else if (itemNo.startsWith(GameValue.PROP_N0) 
					|| itemNo.startsWith(GameValue.EQUIP_N0)
					|| itemNo.startsWith(GameValue.WEAPAN_NO)) 
			{
				// 给予道具,装备,神兵
				bagList.add(dropInfo);
			}
			else if (itemNo.startsWith(GameValue.HERO_N0)) {
				heroNos.add(Integer.parseInt(itemNo));
			}
		}
		if(heroNos.size() > 0){
			List<HeroInfo> heros = new ArrayList<HeroInfo>();
			for(int heroNo : heroNos){
				HeroXMLInfo heroXMLInfo = HeroXMLInfoMap.getHeroXMLInfo(heroNo);
				if (heroXMLInfo == null) {
					continue;
				}
				heros.add(HeroService.initNewHeroInfo(roleInfo, heroXMLInfo, HeroInfo.DEPLOY_TYPE_COMM));									
			}
			if (HeroDAO.getInstance().insertHeros(heros)) {		
				ChestItemRe re = null;
				for(HeroInfo hero : heros){
					HeroInfoMap.addHeroInfo(hero, false);
					GameLogService.insertHeroUpLog(roleInfo,hero.getHeroNo(), action, 0, 0,1);
					heroIds.add(hero.getId());
					
					if(chestItemList!=null){
						re = new ChestItemRe(ChestItemRe.TYPE_STAR_PLAY);
						re.setId(hero.getId());
						re.setItemNo(hero.getHeroNo());
						re.setItemNum((byte)1);						
						chestItemList.add(re);
					}
				}				
			} else {
				return ErrorCode.BAG_ERROR_3;
			}			
		}
			
		if(bagList.size() > 0)
		{
			int result = itemAdd(action, roleInfo, bagList, getItemNum, addEquipIds, weapList, chestItemList, false);
			if(result != 1)
			{
				return result;
			}
		}
		
		if(exp > 0)
		{
			int addExpResult = expAdd(action, roleInfo, exp, null, heroIds);
			if (addExpResult != 1) 
			{
				return addExpResult;
			}
		}
		
//		// 红点监听
//		RedPointMgtService.check2PopRedPoint(roleInfo.getId(), null, false, GameValue.RED_POINT_TYPE_HERO);
		
		// 刷新
		if (needFresh) {
			prizeRefesh(roleInfo, resourceMap, getItemNum, addEquipIds, heroIds, weapList);
		}
		return 1;
	}
	
	/**
	 * 记录获得的奖励
	 * @param resourceMap
	 * @param itemList
	 * @param addHeroList
	 * @param prizeList
	 */
	public static void getPrize(RoleInfo roleInfo,List<DropInfo> addList,List<BattlePrize> prizeList)
	{
		if (prizeList == null)
		{
			return;
		}

		for(DropInfo dropInfo : addList)
		{
			String itemNo = dropInfo.getItemNo();
			int dropNum = dropInfo.getItemNum();
			byte sweep = dropInfo.getSweep();
			if (AbstractConditionCheck.isResourceType(itemNo))
			{
				if (itemNo.equals(ConditionType.TYPE_EXP.getName())) //经验
				{
					if(roleInfo != null && HeroInfoMap.getMainHeroLv(roleInfo.getId()) >= HeroXMLInfoMap.getMaxMainLv()){
						continue;
					}
					prizeList.add(new BattlePrize(itemNo, dropNum, (byte) 2, sweep));
				} 
				else if(itemNo.equals(ConditionType.TYPE_SP.getName())) //体力
				{
					prizeList.add(new BattlePrize(itemNo, dropNum, (byte) 7, sweep));
				} 
				else if(itemNo.equals(ConditionType.TYPE_MONEY.getName())) //银子
				{
					
					prizeList.add(new BattlePrize(itemNo, dropNum, (byte) 1, sweep));
				} 
				else if(itemNo.equals(ConditionType.TYPE_COIN.getName())) //金子
				{
					prizeList.add(new BattlePrize(itemNo, dropNum, (byte) 8, sweep));
				}
				else if(itemNo.equals(ConditionType.TYPE_KUAFU_MONEY.getName()))//征服令
				{
					prizeList.add(new BattlePrize(itemNo, dropNum, (byte) 9, sweep));
				} 
				else if(itemNo.equals(ConditionType.TYPE_COURAGE.getName())) //勇气令
				{
					prizeList.add(new BattlePrize(itemNo, dropNum, (byte) 10, sweep));
				}
				else if(itemNo.equals(ConditionType.TYPE_EXPLOIT.getName())) //战功
				{
					prizeList.add(new BattlePrize(itemNo, dropNum, (byte) 11, sweep));
				}
				else if(itemNo.equals(ConditionType.TYPE_JUSTICE.getName())) //正义令
				{
					prizeList.add(new BattlePrize(itemNo, dropNum, (byte) 12, sweep));
				}
				else if(itemNo.equals(ConditionType.TYPE_PVP_3_MONEY.getName())) //荣誉点
				{
					prizeList.add(new BattlePrize(itemNo, dropNum, (byte) 13, sweep));
				}
				else if(itemNo.equals(ConditionType.TYPE_TEAM_MONEY.getName()))//斩将令
				{
					prizeList.add(new BattlePrize(itemNo, dropNum, (byte) 14, sweep));
				} 
				else 
				{
					//其他
					prizeList.add(new BattlePrize(itemNo, dropNum, (byte) 0, sweep));
				}
			}
			else if (itemNo.startsWith(GameValue.PROP_N0)) 
			{
				prizeList.add(new BattlePrize(itemNo, dropNum, (byte) 3, sweep));
			} 
			else if(itemNo.startsWith(GameValue.EQUIP_N0))
			{
				prizeList.add(new BattlePrize(itemNo, dropNum, (byte) 4, sweep));
			} 
			else if(itemNo.startsWith(GameValue.HERO_N0))
			{
				prizeList.add(new BattlePrize(itemNo, dropNum, (byte) 5, sweep));
			}
			else if (itemNo.startsWith(GameValue.WEAPAN_NO)) 
			{
				prizeList.add(new BattlePrize(itemNo, dropNum, (byte) 6, sweep));
			}
		}
	}
	
	/**
	 * 处理极效活动多倍掉落
	 * @param action
	 * @param addList
	 */
	public static void handlerDoubleDrop(int action, List<DropInfo> addList) 
	{
		// 绩效多倍掉落行为(1-6)
		boolean isDoubleDrop = ArrayUtils.contains(GameValue.GAME_TOOL_ACTIONS, action);	
		if (isDoubleDrop) 
		{
			for (DropInfo dropInfo : addList)
			{
				if(!AbstractConditionCheck.isResourceType(dropInfo.getItemNo()))
				{
					continue;
				}
				
				String itemNo = dropInfo.getItemNo();
				int itemNum = dropInfo.getItemNum();
				
				if (itemNo.equals(ConditionType.TYPE_EXP.getName()))
				{
					itemNum = (int) (itemNum * GameValue.GAME_TOOL_EXP_RAND);
				} 
				else if (itemNo.equals(ConditionType.TYPE_MONEY.getName())) 
				{
					itemNum = (int) (itemNum * GameValue.GAME_TOOL_MONEY_RAND);
				}
				else if (itemNo.equals(ConditionType.TYPE_COURAGE.getName()))
				{
					itemNum = (int) (itemNum * GameValue.GAME_TOOL_COURAGE_RAND);
				} 
				else if (itemNo.equals(ConditionType.TYPE_JUSTICE.getName()))
				{
					itemNum = (int) (itemNum * GameValue.GAME_TOOL_JUSTICE_RAND);
				}
				else if (itemNo.equals(ConditionType.TYPE_KUAFU_MONEY.getName())) 
				{
					itemNum = (int) (itemNum * GameValue.GAME_TOOL_KUAFUMONEY_RAND);
				} 
				else if (itemNo.equals(ConditionType.TYPE_EXPLOIT.getName()))
				{
					itemNum = (int) (itemNum * GameValue.GAME_TOOL_EXPLOIT_RAND);
				}
				else if (itemNo.equals(ConditionType.TYPE_TEAM_MONEY.getName())) 
				{
					itemNum = (int) (itemNum * GameValue.GAME_TOOL_TEAMMONEY_RAND);
				} 
				
				dropInfo.setItemNum(itemNum);
				dropInfo.setItemType(ItemService.checkItemType(dropInfo.getItemNo()));
			}
		}
		
		// 英雄副本多倍掉落行为(7)
		if(ArrayUtils.contains(GameValue.GAME_TOOL_HERO_BATTLE_ACTIONS, action)){
			for (DropInfo dropInfo : addList) {
				if (!dropInfo.getItemNo().equals(ConditionType.TYPE_EXP.getName())){
					dropInfo.setItemNum((int)(dropInfo.getItemNum() * GameValue.GAME_TOOL_HERO_BATTLE_RAND));
				}
				dropInfo.setItemType(ItemService.checkItemType(dropInfo.getItemNo()));
			}
		}		
	}
	
	/**
	 * vip 双倍收益
	 * @param action
	 * @param roleInfo
	 * @param addList
	 */
	public static void handlerDoubleDrop(ActionType action , RoleInfo roleInfo, List<DropInfo> addList) {
		Integer funop = null;
		switch (action) {
		case action304:
		case action307:
			for(DropInfo dropInfo : addList){
				if(ConditionType.TYPE_MONEY.getName().equals(dropInfo.getItemNo())){
					int plus = (int) (dropInfo.getItemNum() * CampaignMgtService.CampaignVipPlus(roleInfo.getVipLv()));
					dropInfo.setItemNum(dropInfo.getItemNum() + plus);
				}
			}
			break;
		case action321:
			// 练兵
			funop = VipXMLInfoMap.getVipVal(roleInfo.getVipLv(), VipType.LBCDB);
			break;
		case action370:
			// 兵来将挡
			funop = VipXMLInfoMap.getVipVal(roleInfo.getVipLv(), VipType.BLJDB);
			break;
		case action364:
			// 狭路相逢
			funop = VipXMLInfoMap.getVipVal(roleInfo.getVipLv(), VipType.XLXFDB);
			break;
		default:
			break;
		}
		if (funop != null && funop == 1) {
			for (DropInfo drop : addList) {
				drop.setItemNum(drop.getItemNum() * 2);
			}
		}
	}
	
	/**
	 * 获得已经有的英雄转换为灵魂碎片
	 * @param roleInfo
	 * @param addList
	 * @param getOhterObj  武将转化成的星石
	 */
	private static void checkHeroToSoulItem(RoleInfo roleInfo, List<DropInfo> addList)
	{
		int roleId = roleInfo.getId();
		List<Integer> heros = new ArrayList<Integer>();
		List<DropInfo> drops = new ArrayList<DropInfo>();
		Iterator<DropInfo> itemIter = addList.iterator();
		while(itemIter.hasNext()) 
		{
			DropInfo dropInfo = itemIter.next();
			
			if(dropInfo != null) {
				String itemNo = dropInfo.getItemNo();
				//英雄
				if(itemNo.startsWith(GameValue.HERO_N0))
				{
					int heroNo = Integer.parseInt(itemNo);
					if(HeroInfoMap.getHeroInfoByNo(roleId, heroNo) == null && heros.indexOf(heroNo) == -1) 
					{
						heros.add(heroNo);
					}
					else
					{
						//删除该英雄
						itemIter.remove();
						//英雄分解成星石
						DropInfo drop = chgSoul(heroNo);
						drops.add(drop);
					}
				}
			}
		}
		
		if(drops.size() > 0){
			for(DropInfo drop : drops){
				addList.add(drop);
				
			}
		}
		
	}	
	
	
	/**
	 * 解析掉落
	 * @param prizeXmls
	 * @param resourceMap
	 * @param itemList
	 * @param addHeroList
	 */
	public static void getDropXMLInfo(RoleInfo roleInfo, List<DropXMLInfo> prizeXmls, List<DropInfo> addList) 
	{
		getDropXMLInfo(roleInfo, prizeXmls, addList, (byte)0);
	}
	
	/**
	 * 解析掉落-副本专用
	 * @param prizeXmls
	 * @param resourceMap
	 * @param itemList
	 * @param addHeroList
	 */
	public static void getDropXMLInfo(RoleInfo roleInfo, List<DropXMLInfo> prizeXmls, List<DropInfo> addList, byte sweep) 
	{
		if(prizeXmls == null || prizeXmls.size() <= 0)
		{
			return;
		}
		int min = 0;
		int max = 0;
		for (DropXMLInfo dropXMLInfo : prizeXmls) 
		{
			if (min > dropXMLInfo.getMinRand()) {
				min = dropXMLInfo.getMinRand();
			}
			if (max < dropXMLInfo.getMaxRand()) {
				max = dropXMLInfo.getMaxRand();
			}
		}		
		int rand = RandomUtil.getRandom(min, max);
		for (DropXMLInfo dropXMLInfo : prizeXmls)
		{
			if(dropXMLInfo.getItemNo() == null)
			{
				continue;
			}
			
			if(dropXMLInfo.getItemNo().length() <= 0 ||dropXMLInfo.getItemMaxNum() <= 0){
				continue;
			}
			if(dropXMLInfo.getDropType() != 2)
			{
				int minRate = dropXMLInfo.getMinRand();
				int maxRate = dropXMLInfo.getMaxRand();
				if (rand < minRate || rand > maxRate) {
					// 概率没随机到
					continue;
				}
			}
			else
			{
				int rand1 = RandomUtil.getRandom(min, max);
				int minRate = dropXMLInfo.getMinRand();
				int maxRate = dropXMLInfo.getMaxRand();
				if (rand1 < minRate || rand1 > maxRate) {
					// 概率没随机到
					continue;
				}
			}
			

			int itemMinNum = dropXMLInfo.getItemMinNum();
			int itemMaxNum = dropXMLInfo.getItemMaxNum();
			int randNum = 0;
			if (itemMinNum != itemMaxNum) {
				randNum = RandomUtil.getRandom(itemMinNum, itemMaxNum);
			} else {
				randNum = itemMaxNum;
			}
			if(randNum > 0) {
				DropInfo dropInfo = new DropInfo();
				dropInfo.setItemNo(dropXMLInfo.getItemNo());
				dropInfo.setItemNum(randNum);
				
				dropInfo.setItemType(ItemService.checkItemType(dropInfo.getItemNo()));
				dropInfo.setParam(dropXMLInfo.getParam());
				dropInfo.setSweep(sweep);
				
				
				addList.add(dropInfo);
			}
		}
	}
	
	/**
	 * 掉落神兵
	 * @param action
	 * @param roleInfo
	 * @param weaponNos 
	 * @param list  获得的神兵
	 * @return
	 */
	public static int weaponAdd(int action, RoleInfo roleInfo, List<Integer> weaponNos,List<RoleWeaponInfo> list, 
			List<ChestItemRe> chestItemList) 
	{
		RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
		if (roleLoadInfo == null) {
			return ErrorCode.SYSTEM_ERROR;
		}
		for (int weaponNo : weaponNos) {
			list.add(new RoleWeaponInfo(roleInfo.getId(), weaponNo));
		}
		if (WeaponDao.getInstance().insertRoleWeaponList(list)) {
			boolean isChestItem = false; //是否是抽奖物品
			ChestItemRe re = null;
			if(chestItemList != null){
				isChestItem = true;
			}
			
			for (RoleWeaponInfo info : list) {
				roleLoadInfo.getRoleWeaponInfoMap().put(info.getId(), info);
				
				if(isChestItem){
					re = new ChestItemRe(ChestItemRe.TYPE_WEAP);
					re.setId(info.getId());
					re.setItemNo(info.getWeaponNo());
					re.setItemNum((byte)1);
					
					chestItemList.add(re);
				}
				
				// 获得物品日志
				GameLogService.insertItemLog(roleInfo, action, 0, info.getWeaponNo(), 0, 1);
			}
		} else {
			return ErrorCode.SQL_DB_ERROR;
		}
		return 1;
	}

	/**
	 * 掉落经验（不推送）
	 * @param action
	 * @param roleInfo
	 * @param addExp
	 * @param getResourceNum
	 * @param heroIds //等级变化的武将
	 * @return
	 */
	public static int expAdd(int action, RoleInfo roleInfo, int addExp, Map<String, Integer> getResourceNum,
			List<Integer> heroIds) {
		if (getResourceNum == null) {
			getResourceNum = new HashMap<String, Integer>();
		}
		if (heroIds == null) {
			heroIds = new ArrayList<Integer>();
		}
		if (addExp > 0) {
			HeroInfo heroInfo = HeroInfoMap.getMainHeroInfo(roleInfo);
			if (heroInfo == null) {
				return ErrorCode.ADD_ERP_ERROR_1;
			}
			int beforeLevel = heroInfo.getHeroLevel();
			// 掉落经验
			HeroService.heroExpAdd(action, roleInfo, heroInfo, addExp, null);
			if (!heroIds.contains(heroInfo.getId())) {
				heroIds.add(heroInfo.getId());
			}
			if(beforeLevel < heroInfo.getHeroLevel()) {
				//角色（主将） 升级
				if(heroInfo.getDeployStatus() == HeroInfo.DEPLOY_TYPE_MAIN){
					FuncOpenMgtService.checkIsHasFuncOpen(roleInfo, true,null);
					//刷新副本信息
					ChallengeService.refreshBattles(roleInfo, null, true, 1);
					//记录玩家升级日志
					GameLogService.insertRoleUpgradeLog(roleInfo, 1, beforeLevel, heroInfo.getHeroLevel(), 0);
					
					GameLogService.insertHeroUpLog(roleInfo,heroInfo.getHeroNo(), ActionType.action72.getType(), 2, beforeLevel,
							heroInfo.getHeroLevel());
					
				} else {
					// 红点监听武将等级变动
					RedPointMgtService.check2PopRedPoint(roleInfo.getId(), null,  true,
							RedPointMgtService.LISTENING_OTHER_HERO_UPGRADE_TYPES);

				}
			}
			
			Integer reNum = getResourceNum.get(ConditionType.TYPE_EXP.getName());
			if (reNum == null) {
				getResourceNum.put(ConditionType.TYPE_EXP.getName(), addExp);
			} else {
				getResourceNum.put(ConditionType.TYPE_EXP.getName(), reNum + addExp);
			}
		}
		return 1;
	}
	
	/**
	 * 背包添加物品
	 * @param action 行为
	 * @param roleInfo 用户信息
	 * @param dropItems (可能有道具编号重复)
	 * @param chgItemIds 获得的物品
	 * @param addEquipIds 获得的装备 <id，no>
	 * @param weapList 获得的神兵
	 * @param addHeroIds 获得的武将
	 * @param chestItemList 抽卡掉落
	 * @param needFresh 是否需要背包刷新
	 * @return
	 */
	public static int itemAdd(int action, RoleInfo roleInfo, List<DropInfo> dropItems,
			Map<Integer ,Integer> chgItemIds, Map<Integer ,Integer> addEquipIds, 
			List<RoleWeaponInfo> addWeapList, List<ChestItemRe> chestItemList, boolean needFresh)
	{
		
		int roleId = (int) roleInfo.getId();
		if (dropItems == null || dropItems.size() <= 0) 
		{
			return 1;
		}
		if (chgItemIds == null) 
		{
			chgItemIds = new HashMap<Integer, Integer>();
		}
		if (addEquipIds == null) 
		{
			addEquipIds = new HashMap<Integer, Integer>();
		}
		if(addWeapList == null)
		{
			addWeapList = new ArrayList<RoleWeaponInfo>();
		}
		
		List<DropInfo> dropItems2 = new ArrayList<DropInfo>();
		
		// 合并相同道具编号,特殊需求不合并(抽卡道具获得数量不定,保持10个元素让客户端显示)
		if(action == ActionType.action66.getType() || action ==ActionType.action67.getType()
				|| action == ActionType.action68.getType() || action == ActionType.action69.getType()
				|| action == ActionType.action437.getType() || action == ActionType.action438.getType()){
			dropItems2.addAll(dropItems);
		}else{
			for (DropInfo dropInfo : dropItems) {
				String no = dropInfo.getItemNo();
				if (no.startsWith(GameValue.PROP_N0)) {
					boolean newAdd = true;
					for (DropInfo dropInfo2 : dropItems2) {
						if (dropInfo2.getItemNo().equals(no)
								&& StringUtils.equals(dropInfo2.getParam(), dropInfo.getParam())) {
							dropInfo2.setItemNum(dropInfo2.getItemNum() + dropInfo.getItemNum());
							newAdd = false;
						}
					}
					if (newAdd) {
						dropItems2.add(dropInfo);
					}
				} else {
					dropItems2.add(dropInfo);
				}
			}
		}
		

		
		// 记录获得新装备<BagEquipInfo>
		List<EquipInfo> addEquip = new ArrayList<EquipInfo>();
		
		Map<Integer, Integer> updateBagItem = new HashMap<Integer, Integer>();
		Map<Integer, BagItemInfo> addBagItemMap = new HashMap<Integer, BagItemInfo>();
		List<DropInfo> chestList = new ArrayList<DropInfo>();
		
		//神兵
		List<Integer> weaponNos = new ArrayList<Integer>();
		
		for (DropInfo dropInfo : dropItems2) {
			String itemNo = dropInfo.getItemNo();
			int itemNum = dropInfo.getItemNum();
			
			//添加过滤,道具获得不可能为负
			if(itemNum < 1)
			{
				continue;
			}
			
			int level = 0;
			if (itemNo.startsWith(GameValue.EQUIP_N0))
			{
				// 装备
				level = Math.max(EquipInfo.EQUIP_LEVLE, NumberUtils.toInt(dropInfo.getParam()));
				// xml验证
				EquipXMLInfo equipXmlInfo = EquipXMLInfoMap.getEquipXMLInfo(Integer.valueOf(itemNo));
				if (equipXmlInfo == null) {
					continue;
				}
				if (equipXmlInfo.getEquipType() == 9 || equipXmlInfo.getEquipType() == 10) {
					// 时装 只能添加一件
					itemNum = 1;
				}
				int check = ShizhuangService.checkIsShizhuangItemNo(roleInfo, itemNo, itemNum);
				if (check != 1) {
					return check;
				}
				
				EquipInfo equipInfo = null;
				for (int i = 0; i < itemNum; i++) {
					equipInfo = new EquipInfo(equipXmlInfo.getNo(),equipXmlInfo.getEquipType(), (short)level);
					addEquip.add(equipInfo);
				}
			}
			else if (itemNo.startsWith(GameValue.PROP_N0)) 
			{
				// 道具
				PropXMLInfo xmlInfo = PropXMLInfoMap.getPropXMLInfo(Integer.valueOf(itemNo));
				if (xmlInfo == null) {
					continue;
				}
				int itemtype = BagItemInfo.getItemType(itemNo);
				if (itemtype == 0) {
					continue;
				}

				BagItemInfo bagItem = BagItemMap.getBagItembyNo(roleInfo, Integer.parseInt(itemNo));
				
				if (bagItem != null) {
					//背包有
					int realBagItemNum;// 物品的当前数量
					
					if (bagItem.getNum()  > Integer.MAX_VALUE - itemNum) {
						realBagItemNum = Integer.MAX_VALUE;
						logger.error(" ### you prop very big ,please call use,item= "+itemNum);
						continue;
					} else {
						realBagItemNum = bagItem.getNum() + itemNum;
					}
					
					if(updateBagItem.containsKey(bagItem.getId())){
						updateBagItem.put(bagItem.getId(), updateBagItem.get(bagItem.getId()) + itemNum > Integer.MAX_VALUE ? Integer.MAX_VALUE : updateBagItem.get(bagItem.getId()) + itemNum);
					
					}else{
						updateBagItem.put(bagItem.getId(), realBagItemNum);
					}
					chestList.add(dropInfo);
					
				} else {
					//背包无 需要加入
					if(!addBagItemMap.containsKey(Integer.valueOf(itemNo))){
						
						bagItem = new BagItemInfo((int) roleInfo.getId(), itemtype, Integer.valueOf(itemNo), itemNum,
								xmlInfo.getColour(), 0);
						
						bagItem.setIsTransition(dropInfo.getIsHeroTransition());
						
						addBagItemMap.put(bagItem.getItemNo(), bagItem);
					}else{
						
						bagItem = addBagItemMap.get(Integer.valueOf(itemNo));
						bagItem.setNum(bagItem.getNum() + itemNum);
					}
					
					chestList.add(dropInfo);
					
				}
			}
			else if(itemNo.startsWith(GameValue.WEAPAN_NO))
			{
				//神兵
				for(int i = 0 ; i < itemNum ; i++)
				{
					// 给予神兵奖励
					weaponNos.add(Integer.valueOf(itemNo));
				}
			}
		}
				
		// 添加装备
		if (addEquip.size() > 0) {
			if (!EquipDAO.getInstance().addEquip(roleId, 0, addEquip)) {
				return ErrorCode.BAG_ERROR_4;
			}
			
			boolean isChestItem = false; //是否是抽奖物品
			ChestItemRe re = null;
			if(chestItemList != null){
				isChestItem = true;
			}

			for (EquipInfo bagEquipInfo : addEquip) {
				// 添加新纪录
				EquipService.refeshEquipProperty(bagEquipInfo);
				EquipInfoMap.addBagEquipInfo(roleId,bagEquipInfo);
				addEquipIds.put(bagEquipInfo.getId(), bagEquipInfo.getEquipNo());
				
				if(isChestItem){
					re = new ChestItemRe(ChestItemRe.TYPE_EQUIP);
					re.setId(bagEquipInfo.getId());
					re.setItemNo(bagEquipInfo.getEquipNo());
					re.setItemNum((byte)1);
					
					chestItemList.add(re);
				}
				
				
				// 获得物品日志
				if(action != ActionType.action362.getType())
				{
					GameLogService.insertItemLog(roleInfo, action, 0, bagEquipInfo.getEquipNo(), 0, 1);
				}
			}
		}
		
		List<BagItemInfo> addBagItem = new ArrayList<BagItemInfo>(addBagItemMap.values());
		
		// 维护BagItem数据
		if (updateBagItem.size() > 0 || addBagItem.size() > 0) {
			// 更新数据库
			if (!ItemDAO.getInstance().addItemBatch(addBagItem, updateBagItem)) {
				return ErrorCode.BAG_ERROR_3;
			}
			
			boolean isChestItem = false; //是否是抽奖物品
			ChestItemRe re = null;
			if(chestItemList != null){
				isChestItem = true;
			}
			
			// 更新新增的背包物品缓存
			for (BagItemInfo bagItemInfo : addBagItem) {
				// 添加新纪录
				BagItemMap.addBagItem(roleInfo, bagItemInfo);
				if(!chgItemIds.containsKey(bagItemInfo.getId())){
					chgItemIds.put(bagItemInfo.getId(),bagItemInfo.getItemNo());
				}
				
				// 十连抽物品展示不叠加
				for(int i = 0 ; i < chestList.size() ; i++)
				{
					DropInfo info = chestList.get(i);
					if(info.getItemNo().equalsIgnoreCase(bagItemInfo.getItemNo() + ""))
					{
						// 获得物品日志					
						if(isChestItem){
							re = new ChestItemRe(ChestItemRe.TYPE_ITEM);
							re.setId(bagItemInfo.getId());
							re.setItemNo(bagItemInfo.getItemNo());
							int num = info.getItemNum();
							re.setItemNum((byte)num);
							
							if((re.getItemNo() + "").startsWith(GameValue.PROP_STAR_N0) && info.getIsHeroTransition() == 1){
								//是星石 告诉客户端播放动画
								re.setItemType(ChestItemRe.TYPE_STAR_PLAY);
							}
							
							chestItemList.add(re);
						}
						chestList.remove(i);
						i--;
					}
				}
				// 获得物品日志
				if(action != ActionType.action362.getType())
				{
					GameLogService.insertItemLog(roleInfo, action, 0, bagItemInfo.getItemNo(), 0, bagItemInfo.getNum());
				}
			}
			
			// 更新增加的背包物品缓存
			BagItemInfo bagItem;
			int beforeNum;
			
			for (Entry<Integer, Integer> entry : updateBagItem.entrySet()) {
				bagItem = BagItemMap.getBagItemById(roleInfo, entry.getKey());
				
				if(bagItem != null){
					beforeNum = bagItem.getNum();
					bagItem.setNum(entry.getValue());
					
					if(!chgItemIds.containsKey(bagItem.getId())){
						chgItemIds.put(bagItem.getId(),bagItem.getItemNo());
					}
					
					// 十连抽物品展示不叠加
					for(int i = 0 ; i < chestList.size() ; i++)
					{
						DropInfo info = chestList.get(i);
						if(info.getItemNo().equalsIgnoreCase(bagItem.getItemNo()+""))
						{
							// 获得物品日志					
							if(isChestItem){
								re = new ChestItemRe(ChestItemRe.TYPE_ITEM);
								re.setId(bagItem.getId());
								re.setItemNo(bagItem.getItemNo());
								int num = info.getItemNum();
								re.setItemNum((byte)num);
								
								if((re.getItemNo() + "").startsWith(GameValue.PROP_STAR_N0) && info.getIsHeroTransition() == 1){
									//是星石 告诉客户端播放动画
									re.setItemType(ChestItemRe.TYPE_STAR_PLAY);
								}
								
								chestItemList.add(re);
							}
							chestList.remove(i);
							i--;
						}
					}
					
					if(action != ActionType.action362.getType())
					{
						GameLogService.insertItemLog(roleInfo, action, 0, bagItem.getItemNo(), beforeNum, bagItem.getNum());
					}
				}
			}
		}
		
		if(weaponNos.size() > 0)
		{
			int result = weaponAdd(action, roleInfo, weaponNos, addWeapList, chestItemList);
			if(result != 1){
				return result;
			}
		}
		
		// 检测是否有功能开启
		FuncOpenMgtService.checkIsHasFuncOpen(roleInfo, true,null);
		
		// 刷新
		if(needFresh)
		{
			ItemService.prizeRefesh(roleInfo, null, chgItemIds, addEquipIds, null, addWeapList);
		}
		return 1;
	}

	/**
	 * 背包道具物品扣除（推送）
	 * @param action
	 * @param roleInfo
	 * @param map
	 * @return
	 */
	public static int bagItemDel(int action, RoleInfo roleInfo, Map<Integer, Integer> map) {
		return bagItemDel(action, roleInfo, map, null, true);
	}

	/**
	 * 背包道具物品扣除
	 * @param action
	 * @param roleInfo
	 * @param map <物品No，扣除物品数量>
	 * @param getItemNum
	 * @param needFresh
	 * @return
	 */
	public static int bagItemDel(int action, RoleInfo roleInfo, Map<Integer, Integer> map,
			Map<Integer,Integer> chgBagItemMap, boolean needFresh) {
		// <物品ID，物品No> 变动
		Map<Integer,Integer> rshMap = new HashMap<Integer, Integer>();
		if (chgBagItemMap != null) {
			rshMap.putAll(chgBagItemMap);
		}
		// <物品ID，扣除后的数量>
		HashMap<Integer, Integer> updateItemMap = new HashMap<Integer, Integer>();

		// 需要更新的统一保存
		for (int itemNo : map.keySet()) {
			int itemNum = map.get(itemNo);
			BagItemInfo bagItem = BagItemMap.checkBagItem(roleInfo, itemNo);
			if (bagItem == null) {
				return ErrorCode.BAG_ERROR_8;
			}

			if(bagItem.getNum() < itemNum)
			{
				return ErrorCode.BAG_ERROR_7;
			}

			updateItemMap.put(bagItem.getId(), (bagItem.getNum() - itemNum));
		}

		// 库更新
		if (ItemDAO.getInstance().updateBagItem(updateItemMap)) {
			for (int itemId : updateItemMap.keySet()) {
				BagItemInfo bagItem = BagItemMap.getBagItemById(roleInfo, itemId);
				if (bagItem == null) {
					continue;
				}

				int beforeNum = bagItem.getNum();
				int afterNum = updateItemMap.get(itemId);

				if (afterNum > 0) {
					bagItem.setNum(afterNum);
				} else {
					BagItemMap.removeBagItem(roleInfo, bagItem.getId());
				}
				if(!rshMap.containsKey(bagItem.getId())){
					rshMap.put(bagItem.getId(), bagItem.getItemNo());
				}
				// 物品消耗日志
				if(action != ActionType.action41.getType())
				{
					GameLogService.insertItemLog(roleInfo, action, 1, bagItem.getItemNo(), beforeNum, afterNum);
				}
			}
		} else {
			return ErrorCode.BAG_ERROR_2;
		}

		// 推送刷新(物品)
		if(chgBagItemMap != null){
			chgBagItemMap.clear();
			chgBagItemMap.putAll(rshMap);
		}			
		if (needFresh && rshMap.size()>0) {
			SceneService.sendRoleRefreshMsg(roleInfo.getId(), SceneService.REFESH_TYPE_ITEM, rshMap);
		}
		return 1;
	}

	/**
	 * 背包装备物品扣除（推送）
	 * @param action 行为编号
	 * @param roleInfo
	 * @param map <物品No(装备Id)，扣除物品数量>
	 */
	public static int bagEquipDel(int action, RoleInfo roleInfo, List<Integer> equipIds)
	{
		if (equipIds == null || equipIds.size() <= 0) 
		{
			return 1;
		}
		
		// 需要更新的统一保存
		for (int equipId : equipIds)
		{
			EquipInfo equipInfo = EquipInfoMap.getBagEquip(roleInfo.getId(), equipId);
			if (equipInfo == null)
			{
				return ErrorCode.EQUIP_MERGE_ERROR_3;
			}
		}
		
		if (EquipDAO.getInstance().deleteEquip(equipIds)) 
		{
			for (int equipId : equipIds)
			{
				EquipInfoMap.removeBagEquip(roleInfo.getId(), equipId);
			}
		}
		else 
		{
			return ErrorCode.BAG_ERROR_1;
		}
		SceneService.sendRoleRefreshMsg(roleInfo.getId(), SceneService.REFESH_TYPE_EQUIP_BAG, equipIds);

		return 1;
	}

	/**
	 * 推送刷新
	 * @param roleInfo
	 * @param getResourceNum 资源变动
	 * @param bagItems 道具变动
	 * @param addEquipIds 装备变动（掉装备）
	 * @param heroIds 武将变动（掉经验武将等级变化）
	 * @param weapList 神兵
	 */
	public static void prizeRefesh(RoleInfo roleInfo, Map<String, Integer> getResourceNum,
			Map<Integer, Integer> chgItemIds, Map<Integer, Integer> addEquipIds, List<Integer> heroIds,List<RoleWeaponInfo> weapList) 
	{

		if (getResourceNum != null && getResourceNum.size() > 0) 
		{
			for(String sourceType:getResourceNum.keySet())
			{
				if(sourceType.equalsIgnoreCase(ConditionType.TYPE_SP.getName()))
				{
					// 体力刷新
					String spStr = roleInfo.getSp() + "," + roleInfo.getLastRecoverSPTime().getTime() + "," + 
					roleInfo.getRoleLoadInfo().getTodayBuySpNum() + "," + 1;
					SceneService.sendRoleRefreshMsg(roleInfo.getId(), SceneService.REFRESH_TYPE_SP, spStr);
				}
				else if(sourceType.equalsIgnoreCase(ConditionType.TYPE_ENERGY.getName()))
				{
					// 精力刷新
					String energyStr = roleInfo.getEnergy() + "," + roleInfo.getLastRecoverEnergyTime().getTime() + ","
					+ roleInfo.getRoleLoadInfo().getTodayBuyEnergyNum() + "," + 1;
					SceneService.sendRoleRefreshMsg(roleInfo.getId(), SceneService.REFRESH_TYPE_ENERGY, energyStr);
				}
				else if(sourceType.equalsIgnoreCase(ConditionType.TYPE_TECH.getName()))
				{
					// 体力刷新
					String techStr = roleInfo.getTech() + "," + roleInfo.getLastRecoverTechTime().getTime() + "," + 
					roleInfo.getRoleLoadInfo().getTodayBuyTechNum() + "," + 1;
					SceneService.sendRoleRefreshMsg(roleInfo.getId(), SceneService.REFRESH_TYPE_TECH, techStr);
				}
				else if(sourceType.equalsIgnoreCase(ConditionType.TYPE_EXP.getName()))
				{
					//经验....
				}
				/*else if(sourceType.equalsIgnoreCase(ConditionType.TYPE_MONEY.getName())
						|| sourceType.equalsIgnoreCase(ConditionType.TYPE_COIN.getName())
						|| sourceType.equalsIgnoreCase(ConditionType.TYPE_COURAGE.getName())
						|| sourceType.equalsIgnoreCase(ConditionType.TYPE_JUSTICE.getName()))
				{
					SceneService.sendRoleRefreshMsg(roleInfo.getId(), SceneService.REFRESH_TYPE_GOLD_MONEY_COURAGE_JUSTICE, "");
				}*/
				else
				{
					SceneService.sendRoleRefreshMsg(roleInfo.getId(), SceneService.REFESH_TYPE_ROLE, "");
				}
			}
		}

		if (chgItemIds != null && chgItemIds.size() > 0) 
		{
			// 推送背包信息
			SceneService.sendRoleRefreshMsg(roleInfo.getId(), SceneService.REFESH_TYPE_ITEM,chgItemIds);
		}
		
		if (addEquipIds != null && addEquipIds.size() > 0) 
		{
			List<Integer> list = new ArrayList<Integer>(addEquipIds.keySet());
			// 推送装备信息
			SceneService.sendRoleRefreshMsg(roleInfo.getId(), SceneService.REFESH_TYPE_EQUIP_BAG, list);
		}
		
		if (heroIds != null && heroIds.size() > 0)
		{
			String heroIdStr = "";
			for (long heroId : heroIds) 
			{
				heroIdStr += heroId + ",";
			}
			// 推送英雄信息
			SceneService.sendRoleRefreshMsg(roleInfo.getId(), SceneService.REFESH_TYPE_HERO, heroIdStr);
		}
		
		// 神兵
		if(weapList != null && weapList.size() > 0)
		{
			SceneService.sendRoleRefreshMsg(roleInfo.getId(), SceneService.REFRESH_TYPE_WEAPON, weapList);
		}
		
	}
	
	/**
	 * 翻牌操作,目前过滤武将及资源
	 * @param dropBag
	 */
	public static void getFpPrizeList(String dropBag, List<BattlePrize> fpPrizeList)
	{
		List<DropXMLInfo> prizeXmls = PropBagXMLMap.getPropBagXMLList(dropBag);
		
		if (prizeXmls != null) 
		{
			List<BattlePrize> per = null;
			for (int i = 0 ; i < 3 ; i++) 
			{
				List<DropInfo> dropList = new ArrayList<DropInfo>();
				
				per = new ArrayList<BattlePrize>();
				getDropXMLInfo(null, prizeXmls, dropList);
				
				getPrize(null,dropList, per);
				
				for (BattlePrize prize : per) 
				{
					if (AbstractConditionCheck.isResourceType(prize.getNo()))
					{
						continue;
					}
					
					if (!prize.getNo().startsWith(GameValue.HERO_N0)) 
					{
						fpPrizeList.add(prize);
						break;
					}
				}
			}
		}
	}
	
	/**
	 * 添加物品和道具的判断
	 * @param roleInfo
	 * @return
	 */
	public static int addItemAndEquipCheck(RoleInfo roleInfo){
		if(roleInfo == null){
			return ErrorCode.ITEM_CHECK_ERROR_1;
		}
		RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
		
		if(roleLoadInfo == null){
			return ErrorCode.ITEM_CHECK_ERROR_2;
		}
		int bagSize1 = 0;
		int bagSize2 = 0;
		int bagSize3 = 0;
		Map<Integer, BagItemInfo> bagItem = roleLoadInfo.getBagItemMap();
		for(BagItemInfo bagItemInfo : bagItem.values()){
			String itemNo = bagItemInfo.getItemNo()+"";
			if(itemNo.startsWith(GameValue.PROP_PRIZE_N0) || itemNo.startsWith(GameValue.PROP_CHIP_NO)){
				bagSize2++;
			} else if (itemNo.startsWith(GameValue.WEAPAN_CHIP_NO) || itemNo.startsWith(GameValue.PROP_STAR_N0)) {
				
			} else if(itemNo.startsWith(GameValue.PROP_ITEM_N0)) {
				bagSize3++;
			} else {
				bagSize1++;
			}
		}
		
		if(roleLoadInfo.getBagEquipMap().size() + bagSize1 >=  roleInfo.getBagLimit()){
			return ErrorCode.ITEM_CHECK_ERROR_3;
		}
		if(bagSize2 >=  roleInfo.getBagLimit()){
			return ErrorCode.ITEM_CHECK_ERROR_3;
		}
		if(bagSize3 >=  roleInfo.getBagLimit()){
			return ErrorCode.ITEM_CHECK_ERROR_3;
		}
		return 1;
	}
	
	public static byte checkItemType(String itemNo){
		byte type = 0;
		if(itemNo.startsWith(GameValue.PROP_N0)){
			type = 1;
		} else if (itemNo.startsWith(GameValue.EQUIP_N0)){
			type = 2;
		} else if (itemNo.startsWith(GameValue.WEAPAN_NO)){
			type = 3;
		}
		return type;
	}
	
	/**
	 * 武将转化碎片
	 * 
	 * @param heroNo
	 */
	private static DropInfo chgSoul(int heroNo) {

		HeroXMLInfo xmlInfo = HeroXMLInfoMap.getHeroXMLInfo(heroNo);
		if (xmlInfo == null) {
			return null;
		}
		int chgSoulNo = xmlInfo.getChipNo();
		int chgSoulNum = xmlInfo.getChipReNum();//upgrade.getResolveChips();
		// 获得道具
		PropXMLInfo prop = PropXMLInfoMap.getPropXMLInfo(chgSoulNo);
		if (prop == null) {
			return null;
		}
		String itemNo = String.valueOf(prop.getNo());
		if(itemNo.startsWith(GameValue.PROP_N0))
		{
			DropInfo dropInfo = new DropInfo();
			dropInfo.setItemNo(itemNo);
			dropInfo.setItemNum(chgSoulNum);
			dropInfo.setItemType(ItemService.checkItemType(dropInfo.getItemNo()));
			dropInfo.setIsHeroTransition((byte)1);
			return dropInfo;
		}
		return null;
	}
	
}
