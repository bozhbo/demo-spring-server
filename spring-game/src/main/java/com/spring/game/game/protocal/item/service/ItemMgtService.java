package com.snail.webgame.game.protocal.item.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.snail.webgame.game.cache.BagItemMap;
import com.snail.webgame.game.cache.EquipInfoMap;
import com.snail.webgame.game.cache.HeroInfoMap;
import com.snail.webgame.game.cache.RoleInfoMap;
import com.snail.webgame.game.common.ActionType;
import com.snail.webgame.game.common.ErrorCode;
import com.snail.webgame.game.common.GameValue;
import com.snail.webgame.game.common.fightdata.DropInfo;
import com.snail.webgame.game.common.util.DateUtil;
import com.snail.webgame.game.condtion.ConditionType;
import com.snail.webgame.game.core.GameLogService;
import com.snail.webgame.game.core.SceneService;
import com.snail.webgame.game.dao.RoleDAO;
import com.snail.webgame.game.info.BagItemInfo;
import com.snail.webgame.game.info.EquipInfo;
import com.snail.webgame.game.info.HeroInfo;
import com.snail.webgame.game.info.RoleInfo;
import com.snail.webgame.game.info.RoleWeaponInfo;
import com.snail.webgame.game.info.StoreItemInfo;
import com.snail.webgame.game.protocal.appellation.service.TitleService;
import com.snail.webgame.game.protocal.equip.query.EquipDetailRe;
import com.snail.webgame.game.protocal.equip.service.EquipService;
import com.snail.webgame.game.protocal.hero.service.HeroService;
import com.snail.webgame.game.protocal.item.compose.ComposeItemReq;
import com.snail.webgame.game.protocal.item.compose.ComposeItemResp;
import com.snail.webgame.game.protocal.item.propUse.PropUseReq;
import com.snail.webgame.game.protocal.item.propUse.PropUseResp;
import com.snail.webgame.game.protocal.item.query.BagItemRe;
import com.snail.webgame.game.protocal.item.query.QueryEquipResp;
import com.snail.webgame.game.protocal.item.query.QueryItemResp;
import com.snail.webgame.game.protocal.item.sell.SellItemReq;
import com.snail.webgame.game.protocal.item.sell.SellItemResp;
import com.snail.webgame.game.protocal.quest.service.QuestService;
import com.snail.webgame.game.protocal.redPoint.service.RedPointMgtService;
import com.snail.webgame.game.protocal.rolemgt.service.RoleService;
import com.snail.webgame.game.protocal.shizhuang.service.ShizhuangService;
import com.snail.webgame.game.protocal.store.service.StoreService;
import com.snail.webgame.game.protocal.vipshop.service.VipShopMgtService;
import com.snail.webgame.game.xml.cache.EquipXMLInfoMap;
import com.snail.webgame.game.xml.cache.PayXMLInfoMap;
import com.snail.webgame.game.xml.cache.PropBagXMLMap;
import com.snail.webgame.game.xml.cache.PropXMLInfoMap;
import com.snail.webgame.game.xml.info.DropXMLInfo;
import com.snail.webgame.game.xml.info.EquipXMLInfo;
import com.snail.webgame.game.xml.info.PayXMLInfo;
import com.snail.webgame.game.xml.info.PropXMLInfo;

public class ItemMgtService {

	/**
	 * 获取用户背包物品
	 * @param roleId
	 * @return
	 */
	public QueryItemResp queryRoleItem(int roleId) {
		QueryItemResp resp = new QueryItemResp();

		List<BagItemRe> list = null;
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			resp.setResult(ErrorCode.ROLE_NOT_EXIST_ERROR_17);
			return resp;
		}

		synchronized (roleInfo) {
			//获取玩家物品
			list = getRoleItem(roleInfo);
		}

		resp.setResult(1);
		resp.setCount(list.size());
		resp.setList(list);
		return resp;
	}

	/**
	 * 推送用户背包道具变化
	 * @param roleId
	 * @param req
	 * @return
	 */
	public QueryItemResp refeshItem(int roleId, Map<Integer,Integer> chgBagItemMap) {
		QueryItemResp resp = new QueryItemResp();
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			resp.setResult(ErrorCode.ROLE_NOT_EXIST_ERROR_20);
			return resp;
		}
		synchronized (roleInfo) {
			List<BagItemRe> list = new ArrayList<BagItemRe>();
			BagItemRe itemRe = null;
			for (int itemId : chgBagItemMap.keySet()) {
				BagItemInfo bagItem = BagItemMap.getBagItemById(roleInfo, itemId);
				if (bagItem != null) {
					itemRe = getRoleBagItemRe(bagItem);
				} else{
					itemRe = new BagItemRe();
					itemRe.setItemId(itemId);
					itemRe.setItemNo(chgBagItemMap.get(itemId));
					itemRe.setItemType((byte)BagItemInfo.getItemType(itemRe.getItemNo()+""));
					itemRe.setItemNum(0);// 约定，推送数量为0，表示删除
				}
				if(itemRe != null){
					list.add(itemRe);
				}
			}
			
			resp.setList(list);
			resp.setCount(list.size());
			resp.setResult(1);
			
			return resp;
		}
	}

	/**
	 * 获取背包
	 * @param roleId
	 * @param type
	 * @return
	 */
	public static List<BagItemRe> getRoleItem(RoleInfo roleInfo) {
		List<BagItemRe> reList = new ArrayList<BagItemRe>();
		//玩家的道具物品
		Map<Integer, BagItemInfo> itemMap = BagItemMap.getBagItem(roleInfo);
		if (itemMap != null && itemMap.size() > 0) {
			for (BagItemInfo itemInfo : itemMap.values()) {
				reList.add(getRoleBagItemRe(itemInfo));
			}
		}
		// 排序
		new ItemComparator(reList);
		return reList;

	}

	/**
	 * 处理玩家装备背包信息
	 * @param item
	 * @return
	 */
	public static BagItemRe getRoleBagEquipRe(EquipInfo item) {

		BagItemRe re = new BagItemRe();
		re.setItemId((int) item.getId());
		re.setItemType((byte) 1);
		re.setItemNo(item.getEquipNo());
		re.setItemNum(1);
		re.setLevel((byte) item.getLevel());
		EquipXMLInfo xmlInfo = EquipXMLInfoMap.getEquipXMLInfo(item.getEquipNo());
		if (xmlInfo != null) {
			re.setColour((byte) xmlInfo.getQuality());
		}

		return re;
	}

	/**
	 * 处理玩家道具背包信息
	 * @param item
	 * @return
	 */
	public static BagItemRe getRoleBagItemRe(BagItemInfo item) {

		BagItemRe re = new BagItemRe();
		re.setItemId((int) item.getId());
		re.setItemType((byte) item.getItemType());
		re.setItemNo(item.getItemNo());
		re.setItemNum(item.getNum());
		re.setLevel((byte) item.getLevel());
		re.setColour((byte) item.getColour());
		return re;
	}

	/**
	 * 处理移除装备信息
	 * @param itemId
	 * @return
	 */
	public static EquipDetailRe getDelBagEquipRe(int equipId) {
		EquipDetailRe re = new EquipDetailRe();
		re.setEquipId(equipId);
		re.setEquipNum((short) 0);// 约定，推送数量为0，表示删除
		return re;
	}

	/**
	 * 出售物品
	 * @param roleId
	 * @param req
	 * @return
	 */
	public SellItemResp sellItem(int roleId, SellItemReq req) {
		SellItemResp resp = new SellItemResp();
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			resp.setResult(ErrorCode.ROLE_NOT_EXIST_ERROR_21);
			return resp;
		}
		//参数检测
		int itemId = req.getItemId();
		int num = req.getItemNum();
		int itemType = req.getItemType();
		if (itemId <= 0 || num <= 0) {
			resp.setResult(ErrorCode.SELL_ITEM_ERROR_1);
			return resp;
		}
		if(num > GameValue.MAX_SELL_PROP){
			num = GameValue.MAX_SELL_PROP;
		}
		
		synchronized (roleInfo) {
			long sellMoney = 0;
			if (itemType == EquipInfo.EQUIP_TYPE) {
				// 装备
				List<Integer> equipIds = new ArrayList<Integer>();
				EquipInfo equip = EquipInfoMap.getBagEquip(roleId, itemId);
				if (equip == null) {
					resp.setResult(ErrorCode.SELL_ITEM_ERROR_2);
					return resp;
				}
				if(num > 1){
					resp.setResult(ErrorCode.SELL_ITEM_ERROR_1);
					return resp;
				}
				// 宝石信息<seat,stoneNo>
				Map<Integer, Integer> stoneMap = equip.getStoneMap();				
				if(stoneMap != null && stoneMap.size() > 0) {
					resp.setResult(ErrorCode.SELL_ITEM_ERROR_9);
					return resp;
				}
				
				// 物品出售失败,该时装被锁定属性
				if(roleInfo.getLockShizhuang().values().contains(equip.getId())){
					resp.setResult(ErrorCode.SELL_ITEM_ERROR_10);
					return resp;
				}
				
				resp.setItemNo(equip.getEquipNo());
				//计算money
				EquipXMLInfo equipXmlInfo = EquipXMLInfoMap.getEquipXMLInfo(equip.getEquipNo());
				if (equipXmlInfo != null) {
					sellMoney +=  equipXmlInfo.getSale();
				} else {
					resp.setResult(ErrorCode.SELL_ITEM_ERROR_3);
					return resp;
				}
				equipIds.add(equip.getId());
				//装备扣除
				int result = ItemService.bagEquipDel(ActionType.action41.getType(), roleInfo, equipIds);
				if (result != 1) {
					resp.setResult(result);
					return resp;
				}
				String actValue = equip.getEquipNo() + "-" + 1+":"+ sellMoney;
				GameLogService.insertPlayActionLog(roleInfo, ActionType.action41.getType(), actValue);
			} else {
				HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
				BagItemInfo item = BagItemMap.getBagItemById(roleInfo, itemId);
				if (item == null) {
					resp.setResult(ErrorCode.SELL_ITEM_ERROR_4);
					return resp;
				}
				if (num > item.getNum()) {
					resp.setResult(ErrorCode.SELL_ITEM_ERROR_5);
					return resp;
				}
				//Money计算
				PropXMLInfo xmlInfo = PropXMLInfoMap.getPropXMLInfo(item.getItemNo());
				if (xmlInfo != null) {
					sellMoney += num * xmlInfo.getSell();
				} else {
					resp.setResult(ErrorCode.SELL_ITEM_ERROR_6);
					return resp;
				}
				//删除道具
				map.put(item.getItemNo(), num);
				resp.setItemNo(item.getItemNo());
				int result = ItemService.bagItemDel(ActionType.action41.getType(), roleInfo, map);
				if (result != 1) {
					resp.setResult(result);
					return resp;
				}
				String actValue = item.getItemNo() + "-" + num+":"+ sellMoney;
				GameLogService.insertPlayActionLog(roleInfo, ActionType.action41.getType(), actValue);
			}
			if(sellMoney < 0){
				resp.setResult(ErrorCode.SELL_ITEM_ERROR_5);
				return resp;
			}
			//增加钱
			if (sellMoney > 0) {
				if (RoleService.addRoleRoleResource(ActionType.action41.getType(), roleInfo,
						ConditionType.TYPE_MONEY, sellMoney,null)) {
					
					resp.setSourceType((byte)ConditionType.TYPE_MONEY.getType());
					resp.setSourceChange((int)sellMoney);
					
					//SceneService.sendRoleRefreshMsg(roleId, SceneService.REFESH_TYPE_ROLE, "");
				} else {
					resp.setResult(ErrorCode.SELL_ITEM_ERROR_7);
					return resp;
				}
			} else {
				resp.setResult(ErrorCode.SELL_ITEM_ERROR_8);
				return resp;
			}
			
			// 红点监听
			RedPointMgtService.check2PopRedPoint(roleInfo.getId(), null, true, GameValue.RED_POINT_TYPE_HERO);

			resp.setResult(1);
			resp.setItemId(req.getItemId());
			resp.setNum(req.getItemNum());
			return resp;
		}
	}

	/**
	 * 道具使用
	 * @param roleId
	 * @param req
	 * @return
	 */
	public PropUseResp propUse(int roleId, PropUseReq req) {
		PropUseResp resp = new PropUseResp();
		resp.setResult(1);
		int itemId = req.getItemId();
		int itemNo = req.getItemNo();
		int itemNum = req.getItemNum();

		resp.setItemId(itemId);
		resp.setItemNo(itemNo);
		resp.setItemNum(itemNum);
		
		if(itemNum <= 0){
			resp.setResult(ErrorCode.PROP_USE_ERROR_5);
			return resp;
		}
		if(itemNum > GameValue.MAX_PROP_USE){
			itemNum = GameValue.MAX_PROP_USE;
		}

		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			resp.setResult(ErrorCode.ROLE_NOT_EXIST_ERROR_10);
			return resp;
		}
		synchronized (roleInfo) {
			PropXMLInfo propXMLInfo = PropXMLInfoMap.getPropXMLInfo(itemNo);
			if (propXMLInfo == null) {
				resp.setResult(ErrorCode.USE_ITEM_ERROR_1);
				return resp;
			}
			//检测使用等级
			HeroInfo mainHero = HeroInfoMap.getMainHeroInfo(roleInfo);
			if (mainHero == null) {
				resp.setResult(ErrorCode.HERO_UP_ERROR_8);
				return resp;
			}
			if(propXMLInfo.getLevel() > mainHero.getHeroLevel()){
				resp.setResult(ErrorCode.PROP_USE_ERROR_3);
				return resp;
			}

			// 道具检测
			BagItemInfo bagItem = BagItemMap.checkBagItem(roleInfo, itemNo);
			if (bagItem == null || bagItem.getNum() < itemNum) {
				resp.setResult(ErrorCode.PROP_USE_ERROR_2);
				return resp;
			}
			
			//使用道具消耗
			String key = propXMLInfo.getKey();
			int keyItemNo = 0;
			if(key != null && key.length() > 0 && key.startsWith(GameValue.PROP_N0)) {
				keyItemNo = Integer.parseInt(key);
				if (!BagItemMap.checkBagItemNum(roleInfo, keyItemNo, itemNum)) {
					resp.setResult(ErrorCode.PROP_USE_ERROR_4);
					return resp;
				}
			}

			List<DropInfo> addList = new ArrayList<DropInfo>();
			
			boolean isEquipShopRefresh = false;
			
			switch (propXMLInfo.getSubType()) {
			case 1:// 经验包
				int addExp = Integer.valueOf(propXMLInfo.getUseParam()) * itemNum;
				addList.add(new DropInfo(ConditionType.TYPE_EXP.getName(), addExp));
				// 验证等级是否达上限
				int[] after = HeroService.addHeroExp(roleInfo, mainHero, addExp);
				if (after == null) {
					resp.setResult(ErrorCode.ADD_ERP_ERROR_2);
					return resp;
				}
				break;
			case 2:
				// 银子包
				int addMoney = Integer.valueOf(propXMLInfo.getUseParam()) * itemNum;
				if(addMoney < 0 || addMoney >= Integer.MAX_VALUE){
					resp.setResult(ErrorCode.USE_ITEM_ERROR_1);
					return resp;
				}
				addList.add(new DropInfo(ConditionType.TYPE_MONEY.getName(), addMoney));
				break;
			case 3:
				// 金子包
				int addCoin = Integer.valueOf(propXMLInfo.getUseParam()) * itemNum;
				if(addCoin < 0 || addCoin >= Integer.MAX_VALUE){
					resp.setResult(ErrorCode.USE_ITEM_ERROR_1);
					return resp;
				}
				addList.add(new DropInfo(ConditionType.TYPE_COIN.getName(), addCoin));
				break;
			case 5:
				// 宝箱包
				int result1 = ItemService.addItemAndEquipCheck(roleInfo);
				if(result1 != 1){
					resp.setResult(ErrorCode.USE_ITEM_ERROR_8);
					return resp;
				}
				addList.add(new DropInfo(propXMLInfo.getUseParam(), itemNum));
				break;
			case 6:
				// 体力包
				int addSp = Integer.valueOf(propXMLInfo.getUseParam()) * itemNum;
				if(addSp < 0 || addSp >= Integer.MAX_VALUE){
					resp.setResult(ErrorCode.USE_ITEM_ERROR_1);
					return resp;
				}
				if (roleInfo.getSp()+addSp > GameValue.ROLE_SP_MAX) {
					resp.setResult(ErrorCode.USE_ITEM_ERROR_9);
					return resp;
				}
				addList.add(new DropInfo(ConditionType.TYPE_SP.getName(), addSp));
				break;
			case 9:
				// 精力包
				int addEnergy = Integer.valueOf(propXMLInfo.getUseParam()) * itemNum;
				if(addEnergy < 0 || addEnergy >= Integer.MAX_VALUE){
					resp.setResult(ErrorCode.USE_ITEM_ERROR_1);
					return resp;
				}
				if (roleInfo.getEnergy()+addEnergy > GameValue.ROLE_ENERGY_MAX) {
					resp.setResult(ErrorCode.USE_ITEM_ERROR_9);
					return resp;
				}

				addList.add(new DropInfo(ConditionType.TYPE_ENERGY.getName(), addEnergy));
				break;
			case 31:
			case 32:
				int result2 = ItemService.addItemAndEquipCheck(roleInfo);
				if(result2 != 1){
					resp.setResult(ErrorCode.USE_ITEM_ERROR_8);
					return resp;
				}
				// 一掉多宝箱
				for (int i = 0; i < itemNum; i++) {
					String bag = "bag" + propXMLInfo.getUseParam();
					// 掉落计算
					List<DropXMLInfo> list = PropBagXMLMap.getPropBagXMLListbyStr(bag);
					if (list != null && list.size() > 0) {
						ItemService.getDropXMLInfo(roleInfo, list, addList);
					}
				}

				break;
			case 33:
				// 装备商城刷新
				int shopType = -1;
				
				if(req.getShopRefreshFlag() == 0){
					
					shopType = StoreItemInfo.STORE_TYPE_8;
					
				}else if(req.getShopRefreshFlag() == 1){
					
					shopType = StoreItemInfo.STORE_TYPE_11;
				}
				
				int result = StoreService.refreshStoreItem(roleInfo, shopType);
				if (result == 1) {
					SceneService.sendRoleRefreshMsg(roleInfo.getId(), SceneService.REFRESH_TYPE_STORE_ITEM, shopType);
					GameLogService.insertPlayActionLog(roleInfo, ActionType.action223.getType(), req.getItemId()+","+req.getItemNo()+","+req.getItemNum()+","+req.getShopRefreshFlag());
				}else{
					resp.setResult(result);
					return resp;
				}
				
				isEquipShopRefresh = true;
				
				break;
			case 34:
				//vip经验道具
				int vipExp = Integer.valueOf(propXMLInfo.getUseParam()) * itemNum;
				int totalVipExp = roleInfo.getVipExp() + vipExp;
				if (RoleDAO.getInstance().updateVipExp(roleInfo.getId(), totalVipExp)) {
					roleInfo.setVipExp(totalVipExp);
					// 检测vip等级变化
					VipShopMgtService.checkVipLvChg(roleInfo, totalVipExp);
					
					// 推送vip信息
					SceneService.sendRoleRefreshMsg(roleInfo.getId(), SceneService.REFRESH_TYPE_CHARGE, "");
				}
				
				break;
			case 35:
				// 会员卡使用
				int cardType = Integer.valueOf(propXMLInfo.getUseParam());
				int payNo = 0;
				if (cardType == 1) {
					// 月卡
					payNo = PayXMLInfo.PAY_SMALL_TYPE_1;
				} else if (cardType == 2) {
					// 季卡
					payNo = PayXMLInfo.PAY_SMALL_TYPE_2;
				} else if (cardType == 3) {
					// 年卡
					payNo = PayXMLInfo.PAY_SMALL_TYPE_3;
				} else if (cardType == 4) {
					// 福利卡
					payNo = PayXMLInfo.PAY_SMALL_TYPE_7;
				}
				
				PayXMLInfo payXMLInfo = PayXMLInfoMap.fetchPayXMLInfo(payNo);
				if (payXMLInfo == null) {
					resp.setResult(ErrorCode.USE_ITEM_ERROR_2);
					return resp;
				}
				
				boolean isNotice = false;
				long now = System.currentTimeMillis();
				long cardEndTime = DateUtil.fetchDayStartTime().getTime() + payXMLInfo.getEffectDay() * DateUtil.ONE_DAY_MILLIS * itemNum;
				if (cardType == 4) {
					// 使用福利卡
					if (roleInfo.getFuliCardEndTime() != null && roleInfo.getFuliCardEndTime().getTime() > now) {
						// 当前还未过期 
						cardEndTime = roleInfo.getFuliCardEndTime().getTime() + payXMLInfo.getEffectDay() * DateUtil.ONE_DAY_MILLIS * itemNum;
					} else {
						isNotice = true;
					}
					
					roleInfo.setFuliCardEndTime(new Timestamp(cardEndTime));
				} else {
					// 使用会员卡
					// 判断是否可使用
					int roleCardType = 0;
					if (roleInfo.getCardEndTime() != null && roleInfo.getCardEndTime().getTime() > now) {
						roleCardType = roleInfo.getCardType();
					}
					
					// 玩家已是会员,只允许使用同类型的会员卡
					if (roleCardType > 0 && roleCardType != cardType) {
						resp.setResult(ErrorCode.USE_ITEM_ERROR_3);
						return resp;
					}
					
					if (roleInfo.getCardEndTime() != null && roleInfo.getCardEndTime().getTime() > now) {
						// 当前还未过期 
						cardEndTime = roleInfo.getCardEndTime().getTime() + payXMLInfo.getEffectDay() * DateUtil.ONE_DAY_MILLIS * itemNum;
					}
					roleInfo.setCardType((byte) cardType);
					roleInfo.setCardEndTime(new Timestamp(cardEndTime));
					
					if (roleCardType <= 0) {
						isNotice = true;
					}
				}
				
				// 
				RoleDAO.getInstance().updateRoleTimeCardInfo(roleInfo);
				
				if (isNotice) {
					// 第一次使用
					SceneService.sendRoleRefreshMsg(roleId, SceneService.REFRESH_TYPE_CHARGE, "");
				}
				
				QuestService.checkQuest(roleInfo, ActionType.action42.getType(), null, true, true);
				
				break;
			case 42:
				//称号道具使用
				int useParam = 0;
				try{
					useParam = Integer.valueOf(propXMLInfo.getUseParam());
				}catch (Exception e) {
					e.printStackTrace();
				}
				
				int check = TitleService.achieveTitleCheck(null, useParam, roleInfo);
				
				if(check != 1){
					resp.setResult(check);
					return resp;
				}
				
				break;
			default:
				resp.setResult(ErrorCode.USE_ITEM_ERROR_7);
				return resp;
			}
			
			// 扣除消耗道具
			HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
			map.put(itemNo, itemNum);
			if(key != null && key.startsWith(GameValue.PROP_N0)){
				
				BagItemInfo bagItem1 = BagItemMap.checkBagItem(roleInfo, keyItemNo);
				if (bagItem1 != null) 
				{
					resp.setItemId1(bagItem1.getId());
					resp.setItemNo1(bagItem1.getItemNo());
					resp.setItemNum1(itemNum);
				} 
				else 
				{
					//材料不足
					resp.setResult(ErrorCode.BAG_ERROR_10);
					return resp;
				}
				map.put(keyItemNo, itemNum);
			}
			int usedResult = ItemService.bagItemDel(ActionType.action42.getType(), roleInfo, map);
			if (usedResult != 1) {
				resp.setResult(usedResult);
				return resp;
			}
			
			Map<Integer, Integer> getItemNum =  new HashMap<Integer, Integer>(); //道具
			Map<Integer, Integer> addEquipIds = new HashMap<Integer, Integer>(); //装备
			List<RoleWeaponInfo> weapList = new ArrayList<RoleWeaponInfo>(); //神兵
			
			if(!isEquipShopRefresh){ //不是商店刷新 添加物品
				// 添加物品
				int result = ItemService.addPrize(ActionType.action42.getType(), roleInfo, addList, null,
						null,getItemNum,addEquipIds,
						weapList,null,null,
						null, null, true);
				if (result != 1) {
					resp.setResult(result);
					return resp;
				}
			}

			// 开宝箱获得的道具
			//1,2,3,6,9不需刷新
			String getItem = "";
			if(propXMLInfo.getSubType() == 5 || propXMLInfo.getSubType() == 31 || propXMLInfo.getSubType() == 32){
				//道具
				if(getItemNum != null && getItemNum.size() > 0){
					BagItemInfo bagItemInfo = null;
					for(int id : getItemNum.keySet()){
						bagItemInfo = BagItemMap.getBagItemById(roleInfo, id);
						if(bagItemInfo != null){
							int num = 0;
							for(DropInfo dropInfo : addList)
							{
								if(dropInfo.getItemNo().equalsIgnoreCase(bagItemInfo.getItemNo()+""))
								{
									num = dropInfo.getItemNum();
									break;
								}
							}
							if (getItem.length() <= 0) {
								getItem = bagItemInfo.getItemNo() + ", " + "0" + ", " + num;
							} else {
								getItem = getItem + ", " + bagItemInfo.getItemNo() + ", " + "0" + ", " + num;
							}
						}
					}
				}
				//装备
				if(addEquipIds != null && addEquipIds.size() > 0){
					for(int i : addEquipIds.keySet()){
						int id = i;
						int no = addEquipIds.get(i);
						if (getItem.length() <= 0) {
							getItem = no + "," + id+ "," + 1;
						} else {
							getItem = getItem + "," + no + "," + id+ "," + 1;;
						}
					}
				}
				//神兵
				if(weapList != null && weapList.size() > 0){
					for(RoleWeaponInfo info : weapList){
						if (getItem.length() <= 0) {
							getItem = info.getWeaponNo() + "," + info.getId()+ "," + 1;
						} else {
							getItem = getItem + "," + info.getWeaponNo() + "," + info.getId()+ "," + 1;;
						}
					}
				}
			}
			
			// 红点监听
			RedPointMgtService.check2PopRedPoint(roleInfo.getId(), null, true, RedPointMgtService.LISTENING_MAIN_HERO_UPGRADE_TYPES);

			
			resp.setGetItem(getItem);
			GameLogService.insertPlayActionLog(roleInfo, ActionType.action42.getType(), req.getItemId()+","+req.getItemNo()+","+req.getItemNum());
		}
		
		return resp;
	}

	/**
	 * 推送用户背包装备
	 * @param roleId
	 * @param req
	 * @return
	 */
	public QueryEquipResp refeshEquip(int roleId, List<Integer> chgBagEquipId) {
		QueryEquipResp resp = new QueryEquipResp();
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			resp.setResult(ErrorCode.ROLE_NOT_EXIST_ERROR_19);
			return resp;
		}
		synchronized (roleInfo) {
			List<EquipDetailRe> list = new ArrayList<EquipDetailRe>();
			EquipDetailRe equipRe = null;
			for (int equipId : chgBagEquipId) {
				EquipInfo equipInfo = EquipInfoMap.getBagEquip(roleId, equipId);
				if (equipInfo == null) {
					equipRe = getDelBagEquipRe((int) equipId);
				} else {
					equipRe = EquipService.getEquipDetailRe(0,equipInfo, 1);
				}
				list.add(equipRe);
			}
			resp.setList(list);
			resp.setCount(list.size());
			resp.setResult(1);

			return resp;
		}
	}
	
	/**
	 * 物品合成
	 * @param roleId
	 * @param req
	 * @return
	 */
	public ComposeItemResp compose(int roleId, ComposeItemReq req) {
		ComposeItemResp resp = new ComposeItemResp();
		// 物品Id
		int itemNo = req.getItemNo();
		int flag = req.getFlag();
		if (itemNo <= 0) {
			resp.setResult(ErrorCode.COMPOSE_ITEM_ERROR_1);
			return resp;
		}
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			resp.setResult(ErrorCode.ROLE_NOT_EXIST_ERROR_30);
			return resp;
		}
		synchronized (roleInfo) {
			// 判断背包是否足够
			int checkItem = ItemService.addItemAndEquipCheck(roleInfo);
			if (checkItem != 1) {
				resp.setResult(ErrorCode.COMPOSE_ITEM_ERROR_5);
				return resp;
			}						
			int itemNum = 0;
			if(flag ==  0)
			{
				// 合成一件
				itemNum = 1;
			}
			else 
			{
				// 全部合成
				HashMap<Integer, Integer> chipMap = PropXMLInfoMap.getComposeXMLInfo(itemNo);
				if(chipMap != null && chipMap.size() > 0)
				{
					for (int chipNo : chipMap.keySet())
					{
						int num = 0;
						int chipNum = chipMap.get(chipNo);
						//TODO
						BagItemInfo bagItem = BagItemMap.checkBagItem(roleInfo, chipNo);
						if (bagItem != null) {
							num = bagItem.getNum() / chipNum;
							if(num <= 0){
								break;
							}
							if(itemNum == 0){
								itemNum = num;
							}
							else if(itemNum > 0 && num < itemNum){
								itemNum = num;
							}
						}
					}
				} else {
					resp.setResult(ErrorCode.COMPOSE_ITEM_ERROR_3);
					return resp;
				}
				if (String.valueOf(itemNo).startsWith(GameValue.EQUIP_N0)) {
					EquipXMLInfo xmlInfo = EquipXMLInfoMap.getEquipXMLInfo(itemNo);
					if (xmlInfo != null && (xmlInfo.getEquipType() == 9 || xmlInfo.getEquipType() == 10)) {
						// 时装只能合成一件
						itemNum = 1;
					}
				}			
			}
			if(itemNum == 0){
				resp.setResult(ErrorCode.COMPOSE_ITEM_ERROR_4);
				return resp;
			}
			itemNum = itemNum > GameValue.COMPOSE_MAX ? GameValue.COMPOSE_MAX : itemNum;			
			// 时装判断
			int check = ShizhuangService.checkIsShizhuangItemNo(roleInfo, String.valueOf(itemNo), itemNum);
			if(check != 1) {
				resp.setResult(check);
				return resp;
			}	
			// 获取合成需要的碎片
			HashMap<Integer, Integer> delMap = new HashMap<Integer, Integer>();
			int getDel = getChipMap(roleInfo, itemNo, itemNum, delMap);
			if (getDel != 1) {
				resp.setResult(ErrorCode.COMPOSE_ITEM_ERROR_4);
				return resp;
			}
			BagItemInfo bagItem = null;
			if (String.valueOf(itemNo).startsWith(GameValue.EQUIP_N0)) {
				for (int key : delMap.keySet()) {
					bagItem = BagItemMap.checkBagItem(roleInfo, key);
					if (bagItem != null) {
						resp.setItemId(bagItem.getId());
						resp.setItemNo(key);
						resp.setItemNum(delMap.get(key).shortValue());
						break;
					}
				}
			} else if(String.valueOf(itemNo).startsWith(GameValue.PROP_N0)) {
				for (int key : delMap.keySet()) {
					bagItem = BagItemMap.checkBagItem(roleInfo, key);
					if (bagItem != null) {
						resp.setItemId(bagItem.getId());
						resp.setItemNo(key);
						resp.setItemNum(delMap.get(key).shortValue());
						break;
					}
				}
			}

			// 删除碎片
			Map<Integer, Integer> chgItemIds = new HashMap<Integer, Integer>();
			int result = ItemService.bagItemDel(ActionType.action24.getType(), roleInfo, delMap, chgItemIds, false);
			if (result != 1) {
				resp.setResult(result);
				return resp;
			}
			// 获得道具
			List<DropInfo> dropItems = new ArrayList<DropInfo>();
			dropItems.add(new DropInfo(String.valueOf(itemNo), itemNum));
			// 添加物品
			Map<Integer, Integer> addEquipIds = new HashMap<Integer, Integer>();
			List<RoleWeaponInfo> weapList = new ArrayList<RoleWeaponInfo>();
			Map<Integer, Integer> ItemIds = new HashMap<Integer, Integer>();
			int result1 = ItemService.itemAdd(ActionType.action24.getType(), roleInfo, dropItems, ItemIds,
					addEquipIds, weapList, null, false);
			if (result1 != 1) {
				resp.setResult(result1);
				return resp;
			}
			chgItemIds.putAll(ItemIds);
			ItemService.prizeRefesh(roleInfo, null, chgItemIds, addEquipIds, null, weapList);
						
			// 红点监听
			RedPointMgtService.check2PopRedPoint(roleInfo.getId(), null, true, GameValue.RED_POINT_TYPE_HERO);

			// 装备
			StringBuilder sb = new StringBuilder();
			if (addEquipIds != null && addEquipIds.size() > 0) {
				for (int equipId : addEquipIds.keySet()) {
					int equipNo = addEquipIds.get(equipId);
					if (sb.length() > 0) {
						sb.append(",");
					}
					sb.append(equipNo).append(",").append(equipId).append(",").append(1);
				}
			}
			if(ItemIds != null && ItemIds.size() > 0){
				for (int itemId : ItemIds.keySet()) {
					int itemNo1 = ItemIds.get(itemId);
					if (sb.length() > 0) {
						sb.append(",");
					}
					sb.append(itemNo1).append(",").append(itemId).append(",").append(itemNum);
				}
			}
			resp.setResult(1);
			resp.setGetItem(sb.toString());
			return resp;
		}

	}

	/**
	 * 获取合成需要的碎片
	 * @param roleInfo
	 * @param itemNo
	 * @param itemNum
	 * @param delMap
	 * @return
	 */
	public static int getChipMap(RoleInfo roleInfo, int itemNo, int itemNum, HashMap<Integer, Integer> map) {
		if (itemNum > 0) {
			// 碎片全是道具
			HashMap<Integer, Integer> chipMap = PropXMLInfoMap.getComposeXMLInfo(itemNo);
			if (chipMap != null && chipMap.size() > 0) {
				BagItemInfo bagItem = null;
				for (int chipNo : chipMap.keySet()) {
					int chipNum = chipMap.get(chipNo) * itemNum;
					int bagItemNum = 0;
					bagItem = BagItemMap.checkBagItem(roleInfo, chipNo);
					if (bagItem != null) {
						bagItemNum = bagItem.getNum();
					}
					if (bagItemNum > 0) {
						int currDelNum = Math.min(chipNum, bagItemNum);
						Integer oldval = map.get(chipNo);
						if (oldval == null) {
							map.put(chipNo, currDelNum);
						} else {
							map.put(chipNo, currDelNum + oldval);
						}
					}
					if (chipNum > bagItemNum) {
						int result = getChipMap(roleInfo, chipNo, chipNum - bagItemNum, map);
						if (result != 1) {
							return result;
						}
					}
				}
			} else {
				return 0;
			}
		}
		return 1;
	}
	
}
