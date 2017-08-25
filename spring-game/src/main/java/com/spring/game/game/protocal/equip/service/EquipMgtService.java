package com.snail.webgame.game.protocal.equip.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;

import com.snail.webgame.game.cache.BagItemMap;
import com.snail.webgame.game.cache.EquipInfoMap;
import com.snail.webgame.game.cache.HeroInfoMap;
import com.snail.webgame.game.cache.RoleInfoMap;
import com.snail.webgame.game.common.ActionType;
import com.snail.webgame.game.common.ErrorCode;
import com.snail.webgame.game.common.GameValue;
import com.snail.webgame.game.common.UserGuideNode;
import com.snail.webgame.game.common.fightdata.DropInfo;
import com.snail.webgame.game.common.xml.cache.HeroXMLInfoMap;
import com.snail.webgame.game.common.xml.cache.VipXMLInfoMap;
import com.snail.webgame.game.common.xml.info.HeroColorXMLUpCost;
import com.snail.webgame.game.common.xml.info.HeroColorXMLUpCostItem;
import com.snail.webgame.game.common.xml.info.HeroXMLInfo;
import com.snail.webgame.game.common.xml.info.VipType;
import com.snail.webgame.game.condtion.AbstractConditionCheck;
import com.snail.webgame.game.condtion.ConditionType;
import com.snail.webgame.game.condtion.conds.CoinCond;
import com.snail.webgame.game.condtion.conds.MoneyCond;
import com.snail.webgame.game.core.GameLogService;
import com.snail.webgame.game.core.SceneService;
import com.snail.webgame.game.dao.EquipDAO;
import com.snail.webgame.game.dao.ItemDAO;
import com.snail.webgame.game.info.BagItemInfo;
import com.snail.webgame.game.info.EquipInfo;
import com.snail.webgame.game.info.HeroInfo;
import com.snail.webgame.game.info.RoleInfo;
import com.snail.webgame.game.protocal.equip.enchant.EquipEnchantReq;
import com.snail.webgame.game.protocal.equip.enchant.EquipEnchantResp;
import com.snail.webgame.game.protocal.equip.heroQuery.HeroEquipDetailRe;
import com.snail.webgame.game.protocal.equip.heroQuery.QueryHeroEquipReq;
import com.snail.webgame.game.protocal.equip.heroQuery.QueryHeroEquipResp;
import com.snail.webgame.game.protocal.equip.merge.MergeEquipReq;
import com.snail.webgame.game.protocal.equip.merge.MergeEquipResp;
import com.snail.webgame.game.protocal.equip.operate.OperateEquipReq;
import com.snail.webgame.game.protocal.equip.operate.OperateEquipResp;
import com.snail.webgame.game.protocal.equip.query.EquipDetailRe;
import com.snail.webgame.game.protocal.equip.query.QueryBagEquipReq;
import com.snail.webgame.game.protocal.equip.query.QueryBagEquipResp;
import com.snail.webgame.game.protocal.equip.refine.EquipRefineReq;
import com.snail.webgame.game.protocal.equip.refine.EquipRefineResp;
import com.snail.webgame.game.protocal.equip.resolve.EquipResolveReq;
import com.snail.webgame.game.protocal.equip.resolve.EquipResolveResp;
import com.snail.webgame.game.protocal.equip.strength.EquipOneKeyStrengthReq;
import com.snail.webgame.game.protocal.equip.strength.EquipOneKeyStrengthResp;
import com.snail.webgame.game.protocal.equip.upgrade.EquipUpReq;
import com.snail.webgame.game.protocal.equip.upgrade.EquipUpResp;
import com.snail.webgame.game.protocal.guide.service.GuideMgtService;
import com.snail.webgame.game.protocal.hero.service.HeroService;
import com.snail.webgame.game.protocal.item.service.ItemService;
import com.snail.webgame.game.protocal.quest.service.QuestService;
import com.snail.webgame.game.protocal.redPoint.service.RedPointMgtService;
import com.snail.webgame.game.protocal.rolemgt.service.RoleService;
import com.snail.webgame.game.protocal.scene.sys.SceneService1;
import com.snail.webgame.game.xml.cache.EquipXMLInfoMap;
import com.snail.webgame.game.xml.cache.PropXMLInfoMap;
import com.snail.webgame.game.xml.info.EnchantXMLInfo;
import com.snail.webgame.game.xml.info.EnchantXMLUpgrade;
import com.snail.webgame.game.xml.info.EquipRefineInfo;
import com.snail.webgame.game.xml.info.EquipStrengthenConfigInfo;
import com.snail.webgame.game.xml.info.EquipXMLInfo;
import com.snail.webgame.game.xml.info.EquipXMLUpgrade;
import com.snail.webgame.game.xml.info.PropXMLInfo;

public class EquipMgtService {

	private EquipDAO equipDAO = EquipDAO.getInstance();

	/**
	 * 查询装备信息
	 * 
	 * @param roleId
	 * @param idStr
	 * @return
	 */
	public QueryBagEquipResp queryBagEquip(int roleId, QueryBagEquipReq req) {
		QueryBagEquipResp resp = new QueryBagEquipResp();
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		
		if (roleInfo == null)
		{
			resp.setResult(ErrorCode.BAG_EQUIP_QUERY_ERROR_1);
			return resp;
		}
		synchronized (roleInfo)
		{
			String idStr = req.getIdStr();
			List<EquipDetailRe> list = EquipService.getBagEquipList(roleInfo, idStr);
			resp.setResult(1);
			resp.setIdStr(idStr);
			resp.setCount(list.size());
			resp.setList(list);
			return resp;
		}
	}

	/**
	 * 查询英雄装备信息
	 * 
	 * @param roleId
	 * @param idStr
	 * @return
	 */
	public QueryHeroEquipResp queryHeroEquip(int roleId, QueryHeroEquipReq req) {
		QueryHeroEquipResp resp = new QueryHeroEquipResp();
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		
		if (roleInfo == null)
		{
			resp.setResult(ErrorCode.HERO_EQUIP_QUERY_ERROR_1);
			return resp;
		}
		synchronized (roleInfo)
		{
			int heroId = req.getHeroId();
			HeroInfo heroInfo = HeroInfoMap.getHeroInfo(roleInfo.getId(), heroId);
			if (heroInfo == null)
			{
				resp.setResult(ErrorCode.HERO_EQUIP_QUERY_ERROR_2);
				return resp;
			}
			Map<Integer, EquipInfo> heroEquipMap = EquipInfoMap.getHeroEquipMap(heroInfo);
			if (heroEquipMap != null && heroEquipMap.size() > 0)
			{
				List<HeroEquipDetailRe> list = EquipService.getHeroEquip(heroInfo, heroEquipMap);
				resp.setCount(list.size());
				resp.setList(list);

			}

			resp.setResult(1);
			resp.setHeroId(heroId);

		}
		return resp;
	}

	/**
	 * 穿/卸 装备 1:穿上 2:卸下 3:一键
	 * @param roleId
	 * @param req
	 * @return
	 */
	public OperateEquipResp operateEquip(int roleId, OperateEquipReq req) {
		OperateEquipResp resp = new OperateEquipResp();
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			resp.setResult(ErrorCode.EQUIP_OP_ERROR_1);
			return resp;
		}
		synchronized (roleInfo) {
			int heroId = req.getHeroId();
			HeroInfo heroInfo = HeroInfoMap.getHeroInfo(roleId, heroId);
			if (heroInfo == null) {
				resp.setResult(ErrorCode.EQUIP_OP_ERROR_2);
				return resp;
			}

			HeroXMLInfo heroXMLInfo = HeroXMLInfoMap.getHeroXMLInfo(heroInfo.getHeroNo());
			if (heroXMLInfo == null) {
				resp.setResult(ErrorCode.EQUIP_OP_ERROR_3);
				return resp;
			}
			String equipIdStr = req.getEquipId();
			if (equipIdStr == null || equipIdStr.length() <= 0) {
				resp.setResult(ErrorCode.EQUIP_OP_ERROR_4);
				return resp;
			}
			String[] equipIds = equipIdStr.split(",");
			byte action = req.getAction();// 行为 1:穿上 2:卸下 3:一键
			int equipId = Integer.parseInt(equipIds[0]);

			List<Integer> upEquipIds = new ArrayList<Integer>();
			List<Integer> downEquipIds = new ArrayList<Integer>();

			int result = ErrorCode.EQUIP_OP_ERROR_5;
			switch (action) {
			case 1:// 穿上
				if (heroInfo.getDeployStatus() == HeroInfo.DEPLOY_TYPE_MAIN){
					result = wearEquip(roleInfo, heroInfo, new String[]{equipId+""}, upEquipIds, downEquipIds);
				} else{
					result = wearOtherHeroEquip(roleInfo, heroInfo,heroXMLInfo, req.getEquipType(), equipId, upEquipIds);
				}
				break;
			case 2:// 卸下
				result = takeoffEquip(roleInfo, heroInfo,  new String[]{equipId+""}, downEquipIds);
				break;
			case 3:// 一键穿装备
				result = wearEquip(roleInfo, heroInfo, equipIds, upEquipIds, downEquipIds);
				break;
			case 4:// 穿时装
				if (heroInfo.getDeployStatus() == HeroInfo.DEPLOY_TYPE_MAIN){
					result = wearEquip(roleInfo, heroInfo, new String[]{equipId+""}, upEquipIds, downEquipIds);
				} else{
					result = ErrorCode.EQUIP_OP_ERROR_6;
				}
				break;
			case 5:// 卸时装
				if (heroInfo.getDeployStatus() == HeroInfo.DEPLOY_TYPE_MAIN){
					result = takeoffEquip(roleInfo, heroInfo,  new String[]{equipId+""}, downEquipIds);
				} else{
					result = ErrorCode.EQUIP_OP_ERROR_6;
				}
				break;
			default:
				break;
			}
			if (result != 1) {
				resp.setResult(result);
				return resp;
			}
			// 刷新英雄属性 战斗力
			HeroService.refeshHeroProperty(roleInfo, heroInfo);

			// 红点监听武将装备变动
			RedPointMgtService.check2PopRedPoint(roleInfo.getId(), null, true,
					RedPointMgtService.LISTENING_HERO_EQUIPS_CHANGE_TYPES);

			resp.setResult(1);
			resp.setHeroId((int) heroInfo.getId());
			resp.setFightValue(heroInfo.getFightValue());

			StringBuilder upEquipIdstr = new StringBuilder();
			StringBuilder downEquipIdstr = new StringBuilder();

			if (upEquipIds != null && upEquipIds.size() > 0) {
				for (int equipId1 : upEquipIds) {
					if (upEquipIdstr.length() <= 0) {
						upEquipIdstr.append(equipId1);
					} else {
						upEquipIdstr.append("," + equipId1);
					}
				}
			}

			if (downEquipIds != null && downEquipIds.size() > 0) {
				for (int equipId1 : downEquipIds) {
					if (downEquipIdstr.length() <= 0) {
						downEquipIdstr.append(equipId1);
					} else {
						downEquipIdstr.append("," + equipId1);
					}
				}
			}
			resp.setUpEquipId(upEquipIdstr.toString());
			resp.setDownEquipId(downEquipIdstr.toString());
			resp.setAction(action);
			resp.setEquipType(req.getEquipType());
			
			// 日志
			int gameAction = ActionType.action84.getType();
			if (heroInfo.getDeployStatus() != HeroInfo.DEPLOY_TYPE_MAIN) {
				gameAction = ActionType.action79.getType();
				
				// 检测副将穿装备
				QuestService.checkQuest(roleInfo, gameAction, null, true, true);
			}
			GameLogService.insertPlayActionLog(roleInfo, gameAction, action + ","+req.getHeroId()+","+req.getEquipId()+","+req.getEquipType());
			return resp;
		}
	}

	/**
	 * 穿装备
	 * @param heroInfo
	 * @param equipInfo
	 * @return
	 */
	private int wearEquip(RoleInfo roleInfo, HeroInfo heroInfo, String[] equipIds, List<Integer> upEquipIds,
			List<Integer> downEquipIds) {
		if (equipIds.length > 8) {
			return ErrorCode.EQUIP_WEAR_ERROR_1;
		}
		HashMap<Integer, Integer> equipMap = new HashMap<Integer, Integer>();
		EquipInfo equipInfo = null;
		EquipInfo oldHeroEquip = null;
		EquipXMLInfo equipXMLInfo = null;
		boolean change = false;
		for (String equip : equipIds) {
			int equipId = Integer.parseInt(equip);
			equipInfo = EquipInfoMap.getBagEquip(roleInfo.getId(), equipId);
			if (equipInfo == null) {
				return ErrorCode.EQUIP_WEAR_ERROR_2;
			}
			if (!change && SceneService1.checkHeroEquipNoforAvater(equipInfo)) {
				change = true;
			}
			equipXMLInfo = EquipXMLInfoMap.getEquipXMLInfo(equipInfo.getEquipNo());
			if (equipXMLInfo == null) {
				return ErrorCode.EQUIP_WEAR_ERROR_3;
			}
			oldHeroEquip = EquipInfoMap.getHeroEquipbyType(heroInfo, equipXMLInfo.getEquipType());
			if (oldHeroEquip != null) {
				if (heroInfo.getDeployStatus() != HeroInfo.DEPLOY_TYPE_MAIN) {
					return ErrorCode.EQUIP_WEAR_ERROR_4;
				}
				// 穿新装备 卸老装备
				equipMap.put(equipInfo.getId(), heroInfo.getId());
				equipMap.put(oldHeroEquip.getId(), 0);
			} else {
				// 穿新装备
				equipMap.put(equipInfo.getId(), heroInfo.getId());
			}
		}

		if (EquipDAO.getInstance().wearEquips(equipMap)) {
			for (int equipId : equipMap.keySet()) {
				int heroId = equipMap.get(equipId);
				if (heroId == 0) {
					equipInfo = EquipInfoMap.getHeroEquip(heroInfo, equipId);
					if (equipInfo != null) {
						// 卸老装备
						EquipInfoMap.removeHeroEquip(heroInfo, equipId);
						EquipInfoMap.addBagEquipInfo(roleInfo.getId(), equipInfo);

						if (!downEquipIds.contains(equipInfo.getId())) {
							downEquipIds.add(equipInfo.getId());
						}
					}
				} else {
					equipInfo = EquipInfoMap.getBagEquip(roleInfo.getId(), equipId);
					if (equipInfo != null) {
						// 穿新装备
						EquipInfoMap.removeBagEquip(roleInfo.getId(), equipId);
						EquipInfoMap.addHeroEquipInfo(heroInfo, equipInfo);

						if (!upEquipIds.contains(equipInfo.getId())) {
							upEquipIds.add(equipInfo.getId());
						}
					}
				}
			}
		} else {
			return ErrorCode.EQUIP_WEAR_ERROR_5;
		}
		if(change){
			SceneService1.roleEquipUpdate(roleInfo);
		}		
		// 检查新手引导
		int ck = GuideMgtService.dealGuideIndexNum(roleInfo, UserGuideNode.WEAR_MAIN_EQUIPE_NODES);
		if(ck != 1){
			return ck;
		}
		
		return 1;
	}

	/**
	 * 副将穿装备
	 * @param roleInfo
	 * @param heroInfo
	 * @param equipType
	 * @param equipId
	 * @param propNo
	 * @param upEquipIds
	 * @return
	 */
	private int wearOtherHeroEquip(RoleInfo roleInfo, HeroInfo heroInfo,HeroXMLInfo heroXMLInfo, int equipType, int equipId,
			List<Integer> upEquipIds) {
		HeroColorXMLUpCost colorXml =HeroXMLInfoMap.getHeroColorXMLUpCost(heroInfo.getQuality());
		if (colorXml == null) {
			return ErrorCode.EQUIP_WEAR_ERROR_7;
		}
		HeroColorXMLUpCostItem materal = colorXml.getItem(heroXMLInfo.getAwakenType(), equipType);
		if (materal == null) {
			// 副将不能穿上该装备
			return ErrorCode.EQUIP_WEAR_ERROR_8;
		}
		EquipInfo equipInfo = EquipInfoMap.getHeroEquipbyType(heroInfo, equipType);
		if (equipInfo != null) {
			return ErrorCode.EQUIP_WEAR_ERROR_4;
		}
		int itemNo = materal.getItemNo();
		int itemNum = materal.getItemNum();
		if (String.valueOf(itemNo).startsWith(GameValue.EQUIP_N0)) {
			EquipXMLInfo xmlInfo = EquipXMLInfoMap.getEquipXMLInfo(itemNo);
			if (xmlInfo == null) {
				return ErrorCode.EQUIP_WEAR_ERROR_3;
			}
			EquipInfo bagEquipInfo = EquipInfoMap.getBagEquip(roleInfo.getId(), equipId);
			if (bagEquipInfo == null) {
				return ErrorCode.EQUIP_WEAR_ERROR_2;
			}
			if (bagEquipInfo.getEquipNo() != itemNo) {
				return ErrorCode.EQUIP_WEAR_ERROR_9;
			}
			HashMap<Integer, Integer> equipMap = new HashMap<Integer, Integer>();
			equipMap.put(bagEquipInfo.getId(), heroInfo.getId());
			if (equipDAO.wearEquips(equipMap)) {
				EquipInfoMap.removeBagEquip(heroInfo.getRoleId(), bagEquipInfo.getId());
				EquipInfoMap.addHeroEquipInfo(heroInfo, bagEquipInfo);
				
				if (!upEquipIds.contains(bagEquipInfo.getId())) {
					upEquipIds.add(bagEquipInfo.getId());
				}
				
			} else {
				return ErrorCode.EQUIP_WEAR_ERROR_5;
			}
		} else if (String.valueOf(itemNo).startsWith(GameValue.PROP_N0)) {
			if (PropXMLInfoMap.getPropXMLInfo(itemNo) == null) {
				return ErrorCode.EQUIP_WEAR_ERROR_7;
			}
			Map<Integer, Integer> delMap = new HashMap<Integer, Integer>();
			delMap.put(itemNo, itemNum);
			int result = ItemService.bagItemDel(ActionType.action79.getType(), roleInfo, delMap);
			if (result != 1) {
				return result;
			}
			List<EquipInfo> upAddItem = new ArrayList<EquipInfo>();
			upAddItem.add(new EquipInfo(itemNo, equipType, (short) 0));
			if (equipDAO.addEquip(roleInfo.getId(), heroInfo.getId(), upAddItem)) {
				for (EquipInfo heroEquipInfo : upAddItem) {
					EquipInfoMap.addHeroEquipInfo(heroInfo, heroEquipInfo);
					
					if (!upEquipIds.contains(heroEquipInfo.getId())) {
						upEquipIds.add(heroEquipInfo.getId());
					}
				}
			} else {
				return ErrorCode.EQUIP_WEAR_ERROR_5;
			}
		}
		int ck = GuideMgtService.dealGuideIndexNum(roleInfo, UserGuideNode.WEAR_EQUIPE_NODES);
		if(ck != 1){
			return ck;
		}
		return 1;
	}
	
	/**
	 * 主武将卸装备
	 * @param heroInfo
	 * @param equipInfo
	 * @return
	 */
	private int takeoffEquip(RoleInfo roleInfo, HeroInfo heroInfo, String[] equipIds, List<Integer> downEquipIds) {
		if (heroInfo.getDeployStatus() != HeroInfo.DEPLOY_TYPE_MAIN) {
			// 不是主武将不能穿装备
			return ErrorCode.EQUIP_OFF_ERROR_1;
		}

		HashMap<Integer, Integer> equipMap = new HashMap<Integer, Integer>();
		EquipInfo equipInfo = null;
		boolean change = false;
		for (String equip : equipIds) {
			int equipId = Integer.parseInt(equip);
			equipInfo = EquipInfoMap.getHeroEquip(heroInfo, equipId);
			if (equipInfo == null) {
				return ErrorCode.EQUIP_OFF_ERROR_2;
			}
			if (!change && SceneService1.checkHeroEquipNoforAvater(equipInfo)) {
				change = true;
			}
			equipMap.put(equipInfo.getId(), 0);
		}
		// 卸装备
		if (equipDAO.wearEquips(equipMap)) {
			for (int equipId : equipMap.keySet()) {
				int heroId = equipMap.get(equipId);
				if (heroId == 0) {
					equipInfo = EquipInfoMap.getHeroEquip(heroInfo, equipId);
					if (equipInfo != null) {
						// 卸老装备
						EquipInfoMap.removeHeroEquip(heroInfo, equipId);
						EquipInfoMap.addBagEquipInfo(roleInfo.getId(), equipInfo);

						if (!downEquipIds.contains(equipInfo.getId())) {
							downEquipIds.add(equipInfo.getId());
						}
					}
				}
			}
		} else {
			return ErrorCode.EQUIP_OFF_ERROR_3;
		}
		if(change){
			SceneService1.roleEquipUpdate(roleInfo);
		}
		return 1;
	}

	/**
	 * 装备合成
	 * 
	 * @param roleId
	 * @param req
	 * @return
	 */
	public MergeEquipResp mergeEquip(int roleId, MergeEquipReq req) {
		MergeEquipResp resp = new MergeEquipResp();
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		
		if (roleInfo == null)
		{
			resp.setResult(ErrorCode.EQUIP_MERGE_ERROR_1);
			return resp;
		}
		synchronized (roleInfo)
		{
			int heroId = req.getHeroId();
			HeroInfo heroInfo = HeroInfoMap.getHeroInfo(roleId, heroId);
			
			if (heroInfo == null)
			{
				resp.setResult(ErrorCode.EQUIP_MERGE_ERROR_2);
				return resp;
			}
			int equipId = req.getEquipId();
			
			EquipInfo equipInfo = EquipInfoMap.getHeroEquip(heroInfo, equipId);
			
			if (equipInfo == null)
			{
				resp.setResult(ErrorCode.EQUIP_MERGE_ERROR_3);
				return resp;
			}
			
			EquipXMLInfo equipXMLInfo = EquipXMLInfoMap.getEquipXMLInfo(equipInfo.getEquipNo());
			if (equipXMLInfo == null)
			{
				resp.setResult(ErrorCode.EQUIP_MERGE_ERROR_4);
				return resp;
			}
			
			int composeEquipNo = equipXMLInfo.getComposeEquipNo();
			EquipXMLInfo composeEquipXMLInfo = EquipXMLInfoMap.getEquipXMLInfo(composeEquipNo);
			
			if (composeEquipXMLInfo == null)
			{
				resp.setResult(ErrorCode.EQUIP_MERGE_ERROR_29);
				return resp;
			}
			int mainHeroLv = HeroInfoMap.getMainHeroLv(roleId);
			if (composeEquipXMLInfo.getHeroLevel() > mainHeroLv)
			{
				resp.setResult(ErrorCode.EQUIP_MERGE_ERROR_30);
				return resp;
			}
			// 判断资源是否足够
			List<AbstractConditionCheck> conds = new ArrayList<AbstractConditionCheck>();
			conds.add(new MoneyCond(equipXMLInfo.getMoney()));
			int check = AbstractConditionCheck.checkCondition(roleInfo,conds);
			if (check != 1)
			{
				resp.setResult(check);
				return resp;
			}

			EquipXMLInfo equipXmlInfo = EquipXMLInfoMap.getEquipXMLInfo(Integer.valueOf(composeEquipNo));
			if (equipXmlInfo == null)
			{
				resp.setResult(ErrorCode.EQUIP_MERGE_ERROR_5);
				return resp;
			}

			// 扣除相关物品
			int result = ItemService.bagItemDel(ActionType.action78.getType(), roleInfo, equipXMLInfo.getItemMap());
			if (result != 1)
			{
				resp.setResult(result);
				return resp;
			}
			// 扣除资源（钱）
			if (RoleService.subRoleResource(ActionType.action78.getType(), roleInfo, conds , null))
			{
				SceneService.sendRoleRefreshMsg(roleInfo.getId(), SceneService.REFESH_TYPE_ROLE, null);
			}
			else
			{
				resp.setResult(ErrorCode.EQUIP_MERGE_ERROR_6);
				return resp;
			}
			
			// 修改数据库
			if (equipDAO.updateEquipNo(equipId, composeEquipNo))
			{
				equipInfo.setEquipNo(composeEquipNo);

			}
			else
			{
				resp.setResult(ErrorCode.EQUIP_MERGE_ERROR_7);
				return resp;
			}
			EquipService.refeshEquipProperty(equipInfo);
			HeroService.refeshHeroProperty(roleInfo, heroInfo);

			GameLogService.insertPlayActionLog(roleInfo, ActionType.action78.getType(), req.getHeroId()+","+req.getEquipId());
			// 红点监听
			RedPointMgtService.check2PopRedPoint(roleId, null, true, RedPointMgtService.LISTENING_MONEY_CHANGE_TYPES);

			resp.setResult(1);
			resp.setHeroId(heroId);
			resp.setFightValue(heroInfo.getFightValue());
			resp.setEquipId(equipId);
			resp.setEquipNo(equipInfo.getEquipNo());
			resp.setEquipFightValue(equipInfo.getFightValue());

			return resp;
		}
	}

	/**
	 * 装备熔炼
	 * 
	 * @param roleId
	 * @param req
	 * @return
	 */
	public EquipResolveResp resolveEquip(int roleId, EquipResolveReq req) {
		EquipResolveResp resp = new EquipResolveResp();
		if(GameValue.GAME_RL_OPEN != 1)
		{
			resp.setResult(ErrorCode.EQUIP_RESOLVE_ERROR_1);
			return resp;
		}
		
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null)
		{
			resp.setResult(ErrorCode.EQUIP_RESOLVE_ERROR_1);
			return resp;
		}

		synchronized (roleInfo)
		{
			HeroInfo mainHero = HeroInfoMap.getMainHeroInfo(roleInfo);
			if (mainHero == null)
			{
				resp.setResult(ErrorCode.EQUIP_RESOLVE_ERROR_3);
				return resp;
			}

			if (GameValue.ROLE_RESOLVE_EQUIP_LIMIT_LEVEL > mainHero.getHeroLevel())
			{
				resp.setResult(ErrorCode.EQUIP_RESOLVE_ERROR_4);
				return resp;
			}
			
			String[] equipIds = null;
			
			if(req.getResolveType() == 0 || req.getResolveType() == 1){
				if (req.getEquipIds().split(":").length > 5)
				{
					// 超出界面最大上限
					resp.setResult(ErrorCode.EQUIP_RESOLVE_ERROR_2);
					return resp;
				}
				
				equipIds = req.getEquipIds().split(":");
			}
			
			
			if(req.getResolveType() == 2)
			{
				if (req.getStar().split(";").length > 5)
				{
					// 超出界面最大上限
					resp.setResult(ErrorCode.EQUIP_RESOLVE_ERROR_2);
					return resp;
				}
				
			}

			if(req.getResolveType() == 0){
				resp = resolve(roleInfo, equipIds);
			}else if(req.getResolveType() == 1){
				resp = resetEq(roleInfo, equipIds);
			}else if(req.getResolveType() == 2){
				resp = resolveStar(roleInfo, req.getStar());
			}else{
				// 非法操作
				resp.setResult(ErrorCode.EQUIP_RESOLVE_ERROR_12);
				return resp;
			}
		}
		
		//红点检测
		boolean isRed = RedPointMgtService.check2PopRedPoint(roleInfo.getId(), null, false, 
				RedPointMgtService.LISTENING_MONEY_CHANGE_TYPES);
		if(isRed){
			RedPointMgtService.pop(roleInfo.getId());
		}
		
		return resp;

	}

	/**
	 * 装备精炼
	 * 
	 * @param roleId
	 * @param req
	 * @return
	 */
	public EquipRefineResp refineEquip(int roleId, EquipRefineReq req) {
		EquipRefineResp resp = new EquipRefineResp();
		if(GameValue.GAME_REFINE_OPEN != 1)
		{
			resp.setResult(ErrorCode.EQUIP_REFINE_ERROR_15);
			return resp;
		}
		
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null)
		{
			resp.setResult(ErrorCode.EQUIP_REFINE_ERROR_1);
			return resp;
		}

		synchronized (roleInfo)
		{
			int heroId = req.getHeroId();
			int id = req.getId(); // 与GAME_EQUIP_INFO表的主键一致
			EquipInfo refineEquipInfo = null;

			HeroInfo mainHero = HeroInfoMap.getMainHeroInfo(roleInfo);
			if (mainHero == null)
			{
				resp.setResult(ErrorCode.EQUIP_WEAR_ERROR_5);
				return resp;
			}

			if (heroId > 0)
			{
				HeroInfo heroInfo = HeroInfoMap.getHeroInfo(roleId, heroId);
				if (heroInfo == null)
				{
					resp.setResult(ErrorCode.EQUIP_UP_ERROR_2);
					return resp;
				}

				if (heroInfo.getDeployStatus() != mainHero.getDeployStatus() && heroInfo.getId() != mainHero.getId())
				{
					resp.setResult(ErrorCode.EQUIP_REFINE_ERROR_12);
					return resp;
				}

				refineEquipInfo = EquipInfoMap.getHeroEquip(heroInfo, id);

				if (refineEquipInfo == null ||  refineEquipInfo.getEquipType() == 9 || refineEquipInfo.getEquipType() == 10)
				{
					resp.setResult(ErrorCode.EQUIP_REFINE_ERROR_2);
					return resp;
				}

			}
			else
			{
				refineEquipInfo = EquipInfoMap.getBagEquip(roleId, id);

				if (refineEquipInfo == null || refineEquipInfo.getEquipType() == 9 || refineEquipInfo.getEquipType() == 10)
				{
					resp.setResult(ErrorCode.EQUIP_REFINE_ERROR_2);
					return resp;
				}

			}

			int equipNo = refineEquipInfo.getEquipNo(); // 角色背包内的动态对象

			EquipXMLInfo equipXMLInfo = EquipXMLInfoMap.getEquipXMLInfo(equipNo); // 判断Equip.xml是否包含该物品

			if (equipXMLInfo == null)
			{
				resp.setResult(ErrorCode.EQUIP_REFINE_ERROR_3);
				return resp;
			}

			int descLevel = refineEquipInfo.getRefineLv() + 1; // 装备精炼等级

			Map<Integer, EquipRefineInfo> refineInfoXMLMap = equipXMLInfo.getRefineMap();

			if (refineInfoXMLMap.size() == 0)
			{
				// 策划未配置 代表不可精炼
				resp.setResult(ErrorCode.EQUIP_REFINE_ERROR_5);
				return resp;
			}

			if (descLevel > equipXMLInfo.getRefineMaxLevel())
			{
				resp.setResult(ErrorCode.EQUIP_REFINE_ERROR_4);
				return resp;
			}

			EquipRefineInfo equipRefineInfo = refineInfoXMLMap.get(descLevel); // XML配置对象

			if (equipRefineInfo == null)
			{
				resp.setResult(ErrorCode.EQUIP_REFINE_ERROR_7);
				return resp;
			}

			if (equipRefineInfo.getLimitLv() > mainHero.getHeroLevel())
			{
				resp.setResult(ErrorCode.EQUIP_RESOLVE_ERROR_14);
				return resp;
			}
			
			List<AbstractConditionCheck> conds = new ArrayList<AbstractConditionCheck>();
			conds.add(new MoneyCond(equipRefineInfo.getDemoney()));
			int check = AbstractConditionCheck.checkCondition(roleInfo, conds);
			if (check != 1) {
				resp.setResult(check);
				return resp;
			}

			int consumeNum = equipRefineInfo.getConsume(); // 需要消耗的装备个数
			int counter = 0; // 计算消耗同等装备数量是否足够
			boolean isEnough = false; // 判断是否足够的标识
			 // 存放要删除的装备物品ID（ID与数据库主键一致）装备道具不可堆叠
			List<Integer> equipIdList = new ArrayList<Integer>();
																	
			if (consumeNum > 0)
			{
				for (Map.Entry<Integer, EquipInfo> equipInfo : EquipInfoMap.getBagEquipMap(roleId).entrySet())
				{
					if (equipInfo.getValue().getEquipNo() == equipNo && id != equipInfo.getValue().getId())
					{
						equipIdList.add(equipInfo.getKey());
						counter++;

						if (consumeNum == counter)
						{
							// 到达满足的数量后停止循环
							isEnough = true;
							break;
						}
					}
				}

				if (!isEnough)
				{
					resp.setResult(ErrorCode.EQUIP_REFINE_ERROR_6);
					return resp;
				}

			}

			int propId = equipRefineInfo.getProp();
			int itemNeedNum = equipRefineInfo.getNum(); // 需要的道具物品数量

			PropXMLInfo propXMLInfo = PropXMLInfoMap.getPropXMLInfo(propId);
			if (propXMLInfo == null)
			{
				resp.setResult(ErrorCode.EQUIP_REFINE_ERROR_9);
				return resp;
			}

			// 判断道具物品是否存在
			if (BagItemMap.checkBagItem(roleInfo, propId) == null)
			{
				resp.setResult(ErrorCode.EQUIP_REFINE_ERROR_10);
				return resp;
			}

			// 判断道具物品是否存在
			if (!BagItemMap.checkBagItemNum(roleInfo, propId, itemNeedNum))
			{
				resp.setResult(ErrorCode.EQUIP_REFINE_ERROR_8);
				return resp;
			}

			Map<Integer, Integer> bagItemDelMap = new HashMap<Integer, Integer>();
			bagItemDelMap.put(propId, itemNeedNum);

			int result = ItemService.bagItemDel(ActionType.action87.getType(), roleInfo, bagItemDelMap); // 删除背包内道具

			if (result != 1)
			{
				resp.setResult(result);
				return resp;
			}

			result = ItemService.bagEquipDel(ActionType.action87.getType(), roleInfo, equipIdList); // 删除原来

			if (result != 1)
			{
				resp.setResult(result);
				return resp;
			}

			if (!equipDAO.updateEquipRefineLevel(refineEquipInfo.getId(),descLevel))
			{
				resp.setResult(ErrorCode.EQUIP_REFINE_ERROR_11);
				return resp;
			}

			refineEquipInfo.setRefineLv((short) descLevel);
			
			if(RoleService.subRoleResource(ActionType.action87.getType(), roleInfo, conds , null)){
				String updateSourceStr = RoleService.returnResourceChange(conds);
				if (updateSourceStr != null)
				{
					String[] sourceStr = updateSourceStr.split(",");
					if (sourceStr != null && sourceStr.length > 1)
					{
						resp.setSourceType(Byte.valueOf(sourceStr[0]));
						resp.setSourceChange(-Integer.valueOf(sourceStr[1]));
					}
				}
			}
			

			EquipService.refeshEquipProperty(refineEquipInfo);

			if (heroId > 0)
			{
				HeroService.refeshHeroProperty(roleInfo, mainHero);
				resp.setFightValue(mainHero.getFightValue());

			}
			else
			{
				resp.setEquipFightValue(refineEquipInfo.getFightValue());
			}

			// 判断新手引导是否要更新
			int ck = GuideMgtService.dealGuideIndexNum(roleInfo, UserGuideNode.GAME_GUIDE_REFINE_EQUIPE);
			if(ck != 1){
				resp.setResult(ck);
				return resp;
			}

			resp.setResult(1);
			resp.setHeroId(heroId);
			resp.setEquipId(id);
			resp.setEquipFightValue(refineEquipInfo.getFightValue());
			resp.setRefineLevel(refineEquipInfo.getRefineLv());
			
			//任务检测
			boolean isRedQuest = QuestService.checkQuest(roleInfo, ActionType.action87.getType(), null, true, false);
			// 红点监听
			boolean isRed = RedPointMgtService.check2PopRedPoint(roleId, null, false, 
					RedPointMgtService.LISTENING_MONEY_CHANGE_TYPES);
			//刷新红点
			if(isRedQuest || isRed) {
				RedPointMgtService.pop(roleId);
			}

			GameLogService.insertEquipUpLog(roleInfo, ActionType.action87.getType(), heroId, id,
					refineEquipInfo.getEquipNo(), refineEquipInfo.getRefineLv() - 1, refineEquipInfo.getRefineLv());
		}

		return resp;
	}

	/**
	 * 新装备强化规则（2015 - 03 版本）
	 * 
	 * @param roleId
	 * @param req
	 * @return
	 */
	public EquipUpResp newEquipStreng(int roleId, EquipUpReq req) {
		EquipUpResp resp = new EquipUpResp();
		if(GameValue.GAME_DZ_OPEN != 1)
		{
			resp.setResult(ErrorCode.EQUIP_UP_ERROR_1);
			return resp;
		}
		
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null)
		{
			resp.setResult(ErrorCode.EQUIP_UP_ERROR_1);
			return resp;
		}

		synchronized (roleInfo)
		{
			if (StringUtils.isBlank(req.getResolveEquipIds()) && req.getItemNum() == 0)
			{
				resp.setResult(1);
				return resp;
			}

			if (!StringUtils.isBlank(req.getResolveEquipIds()) && req.getItemNum() > 0)
			{
				resp.setResult(ErrorCode.EQUIP_UP_ERROR_13);
				return resp;
			}

			int equipid = req.getEquipId();
			int heroId = req.getHeroId();
			EquipInfo equipInfo = null;

			HeroInfo mainHero = HeroInfoMap.getMainHeroInfo(roleInfo);
			if (mainHero == null)
			{
				resp.setResult(ErrorCode.EQUIP_WEAR_ERROR_5);
				return resp;
			}

			if (heroId > 0)
			{

				HeroInfo heroInfo = HeroInfoMap.getHeroInfo(roleId, heroId);
				if (heroInfo == null)
				{
					resp.setResult(ErrorCode.EQUIP_UP_ERROR_2);
					return resp;
				}

				if (heroInfo.getDeployStatus() != mainHero.getDeployStatus() && heroInfo.getId() != mainHero.getId())
				{
					resp.setResult(ErrorCode.EQUIP_UP_ERROR_8);
					return resp;
				}

				equipInfo = EquipInfoMap.getHeroEquip(heroInfo, equipid);
				
				if (equipInfo == null || equipInfo.getEquipType() == 9 || equipInfo.getEquipType() == 10)
				{
					resp.setResult(ErrorCode.EQUIP_UP_ERROR_3);
					return resp;
				}

			}
			else
			{
				equipInfo = EquipInfoMap.getBagEquip(roleId, equipid);
				if (equipInfo == null || equipInfo.getEquipType() == 9 || equipInfo.getEquipType() == 10)
				{
					resp.setResult(ErrorCode.EQUIP_UP_ERROR_3);
					return resp;
				}
			}
			int beforeEquipLevel = equipInfo.getLevel();
			if (equipInfo.getLevel() >= mainHero.getHeroLevel())
			{
				resp.setResult(ErrorCode.EQUIP_UP_ERROR_4);
				return resp;
			}

			EquipXMLInfo equipXMLInfo = EquipXMLInfoMap.getEquipXMLInfo(equipInfo.getEquipNo()); // 判断Equip.xml是否包含该物品
			if (equipXMLInfo == null)
			{
				resp.setResult(ErrorCode.EQUIP_REFINE_ERROR_3);
				return resp;
			}

			EquipStrengthenConfigInfo upConfigInfo = EquipXMLInfoMap.getEquipStrengthenConfigInfo(equipXMLInfo.getQuality());
			if (upConfigInfo == null)
			{
				resp.setResult(ErrorCode.EQUIP_REFINE_ERROR_3);
				return resp;
			}

			Map<Integer, EquipXMLUpgrade> upgradeMap = upConfigInfo.getEquipXMLUpgradeMap();

			String[] resolveIds = req.getResolveEquipIds().split(":");
			if (resolveIds.length > GameValue.EQUIP_STRENG_NUM)
			{
				resp.setResult(ErrorCode.EQUIP_UP_ERROR_9);
				return resp;
			}

			Map<Integer, EquipInfo> bagEquipMap = EquipInfoMap.getBagEquipMap(roleId);
			Map<Integer, EquipXMLInfo> equipXMLInfoMap = new HashMap<Integer, EquipXMLInfo>(); // key
																								// id(与数据库主键ID一致):value-装备配置XML
			List<Integer> idDelList = new ArrayList<Integer>(); // 存放需要删除的装备ID

			EquipInfo tempEquipInfo = null;
			for (String s_id : resolveIds)
			{
				if (StringUtils.isBlank(s_id))
				{
					continue;
				}

				int n_id = Integer.valueOf(s_id);
				
				// 判断是否含有被强化的装备
				if(n_id == equipid)
				{
					resp.setResult(ErrorCode.EQUIP_UP_ERROR_10);
					return resp;
				}
				
				tempEquipInfo = bagEquipMap.get(n_id);

				if (tempEquipInfo == null)
				{
					resp.setResult(ErrorCode.EQUIP_RESOLVE_ERROR_6);
					return resp;
				}

				equipXMLInfo = EquipXMLInfoMap.getEquipXMLInfo(tempEquipInfo.getEquipNo()); // 判断Equip.xml是否包含该物品

				if (equipXMLInfo == null)
				{
					resp.setResult(ErrorCode.EQUIP_REFINE_ERROR_3);
					return resp;
				}

				equipXMLInfoMap.put(n_id, equipXMLInfo);
			}

			int resetExp = equipInfo.getExp(); // 用于回滚用的
			int exp = equipInfo.getExp();
			int srcLevel = equipInfo.getLevel();
			int addLevel = 0;
			long cost = 0; // 用于计算需要消耗的金币
			int patchItemNum = req.getItemNum();
			int calcExp = 0; //计算退还的经验碎片
			EquipXMLUpgrade upgradeXml = null;

			if (equipXMLInfoMap.size() > 0)
			{
				
out:			for (Map.Entry<Integer, EquipXMLInfo> xmlInfo : equipXMLInfoMap.entrySet()) {
					exp += xmlInfo.getValue().getStrengthenExp();
					cost += xmlInfo.getValue().getStrengthenExp();
					
					upgradeXml = upgradeMap.get(srcLevel + addLevel);

					if (upgradeXml == null)
					{
						resp.setResult(ErrorCode.EQUIP_REFINE_ERROR_3);
						return resp;
					}
					
					if(upgradeXml.getExp() <= 0){
						resp.setResult(ErrorCode.EQUIP_UP_ERROR_14);
						return resp;
					}
					
					if (exp >= upgradeXml.getExp())
					{
						// 升级
						addLevel++;
						exp -= upgradeXml.getExp();
						
						while (true)
						{
							upgradeXml = upgradeMap.get(srcLevel + addLevel);
							if (upgradeXml == null)
							{
								resp.setResult(ErrorCode.EQUIP_REFINE_ERROR_3);
								return resp;
							}

							if (upgradeXml.getExp() <= 0)
							{
								// 满级
								addLevel = upgradeXml.getLevel() - srcLevel;
								calcExp = exp; //剩余未使用的经验退还给玩家
								exp = upgradeXml.getExp();
								
								idDelList.add(xmlInfo.getKey()); 
								
								break out; //直接结束
							}

							if (exp >= upgradeXml.getExp())
							{
								addLevel++;
								exp -= upgradeXml.getExp();
							}
							else
							{
								break;
							}
							

							if ((srcLevel + addLevel) > mainHero.getHeroLevel())
							{
								// 超过主将等级
								addLevel = mainHero.getHeroLevel() - srcLevel;
								upgradeXml = upgradeMap.get(srcLevel + addLevel);
								calcExp = exp + 1; //剩余未使用的经验退还给玩家
								exp = upgradeXml.getExp() - 1;
								
								idDelList.add(xmlInfo.getKey());
								break out; //直接结束
							}
						}
						
					}
					
					if ((srcLevel + addLevel) > mainHero.getHeroLevel())
					{
						// 超过主将等级
						addLevel = mainHero.getHeroLevel() - srcLevel;
						upgradeXml = upgradeMap.get(srcLevel + addLevel);
						calcExp = exp + 1; //剩余未使用的经验退还给玩家
						exp = upgradeXml.getExp() - 1;
						
						idDelList.add(xmlInfo.getKey());
						
						break;
						
					}
					
					idDelList.add(xmlInfo.getKey());

				}

			}
			else if (patchItemNum > 0)
			{
				BagItemInfo bagItemInfo = BagItemMap.getBagItembyNo(roleInfo, GameValue.EQUIP_STRENGEH_ITEM);
				// 碎片数量的判断
				if (bagItemInfo == null || bagItemInfo.getNum() < patchItemNum)
				{
					resp.setResult(ErrorCode.EQUIP_UP_ERROR_11);
					return resp;
				}

				exp += patchItemNum;
				cost += patchItemNum;

				upgradeXml = upgradeMap.get(srcLevel + addLevel);

				if (upgradeXml == null)
				{
					resp.setResult(ErrorCode.EQUIP_REFINE_ERROR_3);
					return resp;
				}
				
				if(upgradeXml.getExp() <= 0){
					resp.setResult(ErrorCode.EQUIP_UP_ERROR_14);
					return resp;
				}

				if (exp >= upgradeXml.getExp())
				{
					// 升级
					exp -= upgradeXml.getExp();
					addLevel++;

					while (true)
					{
						upgradeXml = upgradeMap.get(srcLevel + addLevel);

						if (upgradeXml == null)
						{
							resp.setResult(ErrorCode.EQUIP_REFINE_ERROR_3);
							return resp;
						}

						if (upgradeXml.getExp() <= 0)
						{
							// 满级
							addLevel = upgradeXml.getLevel() - srcLevel;
							calcExp = exp; //剩余未使用的经验退还给玩家
							exp = upgradeXml.getExp();
							break;
						}

						if (exp >= upgradeXml.getExp())
						{
							// 升级
							exp -= upgradeXml.getExp();
							addLevel++;

						}
						else
						{
							break;
						}

						if ((srcLevel + addLevel) > mainHero.getHeroLevel())
						{
							// 超过主将等级
							addLevel = mainHero.getHeroLevel() - srcLevel;
							calcExp = exp + 1; //剩余未使用的经验退还给玩家
							exp = upgradeXml.getExp() - 1;
							break;
						}

					}

				}

			}

			addLevel += srcLevel; // 增加等级

			if(calcExp > 0){
				//多余碎片返还
				cost -= calcExp;
			}
			
			List<AbstractConditionCheck> conds = new ArrayList<AbstractConditionCheck>();
			conds.add(new MoneyCond(cost *= GameValue.EQUIP_STRENGEH_RATE));

			int check = AbstractConditionCheck.checkCondition(roleInfo,conds);

			if (check != 1)
			{
				resp.setResult(check);
				return resp;
			}

			if (RoleService.subRoleResource(ActionType.action77.getType(), roleInfo, conds , null))
			{
				String updateSourceStr = RoleService.returnResourceChange(conds);
				if (updateSourceStr != null)
				{
					String[] sourceStr = updateSourceStr.split(",");
					if (sourceStr != null && sourceStr.length > 1)
					{
						resp.setSourceType(Byte.valueOf(sourceStr[0]));
						resp.setSourceChange(-Integer.valueOf(sourceStr[1]));
					}
				}

			}
			else
			{
				resp.setResult(ErrorCode.EQUIP_UP_ERROR_6);
				return resp;
			}

			if (equipDAO.updateEquipLevel(equipid, (short) addLevel, exp))
			{
				equipInfo.setLevel((short) addLevel);
				equipInfo.setExp(exp);
			}
			else
			{
				resp.setResult(ErrorCode.EQUIP_UP_ERROR_7);
				return resp;
			}

			if (ItemService.bagEquipDel(ActionType.action77.getType(), roleInfo, idDelList) != 1)
			{
				// 删除道具
				// 本次操作失败
				if (equipDAO.updateEquipLevel(equipid, (short) srcLevel, resetExp))
				{
					// 退还到原有状态
					equipInfo.setLevel((short) srcLevel);
					equipInfo.setExp(resetExp);
				}

				resp.setResult(ErrorCode.EQUIP_UP_ERROR_7);
				return resp;
			}

			EquipService.refeshEquipProperty(equipInfo);

			if (heroId > 0)
			{
				HeroService.refeshHeroProperty(roleInfo, mainHero);
				resp.setFightValue(mainHero.getFightValue());
			}

			if (patchItemNum > 0)
			{
				// 删除碎片
				Map<Integer, Integer> delMap = new HashMap<Integer, Integer>();
				delMap.put(GameValue.EQUIP_STRENGEH_ITEM, patchItemNum);

				if (ItemService.bagItemDel(ActionType.action77.getType(), roleInfo, delMap) != 1)
				{
					resp.setResult(ErrorCode.EQUIP_UP_ERROR_7);
					return resp;
				}
			}
			
			if(calcExp > 0){
				int result = addEquipUpItem(roleInfo, calcExp);
				
				if(result != 1){
					resp.setResult(result);
					return resp;
				}
			}


			
			// 判断新手引导是否要更新
			int ck = GuideMgtService.dealGuideIndexNum(roleInfo,  UserGuideNode.GAME_GUIDE_STRENGTH_EQUIPE);
			if(ck != 1){
				resp.setResult(ck);
				return resp;
			}
			
			resp.setResult(1);
			resp.setHeroId(heroId);

			resp.setExp(exp);
			resp.setEquipId(equipid);
			resp.setEquipLevel((short) equipInfo.getLevel());
			resp.setEquipFightValue(equipInfo.getFightValue());
			
			//任务检测
			boolean isRedQuest = QuestService.checkQuest(roleInfo, ActionType.action77.getType(), 1, true, false);
			// 红点监听
			boolean isRed = RedPointMgtService.check2PopRedPoint(roleId, null, false, 
					RedPointMgtService.LISTENING_MONEY_CHANGE_TYPES);
			//刷新红点
			if(isRedQuest || isRed) {
				RedPointMgtService.pop(roleId);
			}
			
			GameLogService.insertEquipUpLog(roleInfo,ActionType.action77.getType(), heroId, equipid,
					equipInfo.getEquipNo(), beforeEquipLevel,equipInfo.getLevel());

		}

		return resp;
	}

	/**
	 * 一键强化
	 * 
	 * @param roleId
	 * @param req
	 * @return
	 */
	public EquipOneKeyStrengthResp equipOneKeyStrength(int roleId, EquipOneKeyStrengthReq req) {
		EquipOneKeyStrengthResp resp = new EquipOneKeyStrengthResp();
		if(GameValue.GAME_DZ_OPEN != 1)
		{
			resp.setResult(ErrorCode.EQUIP_UP_ERROR_1);
			return resp;
		}
		
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null)
		{
			resp.setResult(ErrorCode.EQUIP_UP_ERROR_1);
			return resp;
		}

		synchronized (roleInfo)
		{
			if(VipXMLInfoMap.getVipVal(roleInfo.getVipLv(), VipType.YJQH) < 1)
			{
				resp.setResult(ErrorCode.EQUIP_UP_ERROR_16);
				return resp;
			}
			
			EquipInfo equipInfo = null;
			int equipid = req.getEquipId();
			int heroId = req.getHeroId();

			HeroInfo mainHero = HeroInfoMap.getMainHeroInfo(roleInfo);
			if (mainHero == null)
			{
				resp.setResult(ErrorCode.EQUIP_WEAR_ERROR_5);
				return resp;
			}

			if (heroId > 0)
			{
				// 英雄身上
				HeroInfo heroInfo = HeroInfoMap.getHeroInfo(roleId, heroId);

				if (heroInfo == null)
				{
					resp.setResult(ErrorCode.EQUIP_UP_ERROR_2);
					return resp;
				}

				if (heroInfo.getDeployStatus() != mainHero.getDeployStatus() && heroInfo.getId() != mainHero.getId())
				{
					resp.setResult(ErrorCode.EQUIP_UP_ERROR_8);
					return resp;
				}

				equipInfo = EquipInfoMap.getHeroEquip(heroInfo, equipid);

				if (equipInfo == null ||  equipInfo.getEquipType() == 9 || equipInfo.getEquipType() == 10)
				{
					resp.setResult(ErrorCode.EQUIP_UP_ERROR_3);
					return resp;
				}

			}
			else
			{
				// 背包呢
				equipInfo = EquipInfoMap.getBagEquip(roleId, equipid);
				if (equipInfo == null ||  equipInfo.getEquipType() == 9 || equipInfo.getEquipType() == 10)
				{
					resp.setResult(ErrorCode.EQUIP_UP_ERROR_3);
					return resp;
				}
			}
			int beforeEquipLevel = equipInfo.getLevel();
			if (equipInfo.getLevel() >= mainHero.getHeroLevel())
			{
				resp.setResult(ErrorCode.EQUIP_UP_ERROR_4);
				return resp;
			}

			Map<Integer, EquipInfo> equipMap = EquipInfoMap.getBagEquipMap(roleId);
			List<Integer> delList = new ArrayList<Integer>(); // 把装备颜色 1:白 2:绿
																// 3:蓝 的放入 用于分解
			long total = 0; // 装备碎片(GameValue.EQUIP_STRENGEH_ITEM) 与升级经验1:1
			EquipXMLInfo equipXMLInfo = null;
			
			for (Map.Entry<Integer, EquipInfo> map : equipMap.entrySet())
			{
				equipXMLInfo = EquipXMLInfoMap.getEquipXMLInfo(map.getValue().getEquipNo()); // 判断Equip.xml是否包含该物品
				if (equipXMLInfo == null || equipInfo.getId() == map.getKey())
				{
					// 配置为空或者自身装备
					continue;
				}

				int quality = equipXMLInfo.getQuality();

				if (quality == 1 || quality == 2 || quality == 3)
				{
					delList.add(map.getKey());
					total += equipXMLInfo.getStrengthenExp();
				}

			}

			BagItemInfo bagItemInfo = BagItemMap.getBagItembyNo(roleInfo, GameValue.EQUIP_STRENGEH_ITEM);
			boolean isExist = false; // 物品没有 更新升级碎片时insert 有则update

			if (bagItemInfo != null)
			{
				isExist = true;
				total += bagItemInfo.getNum();
			}

			if (delList.size() <= 0 && (!isExist || bagItemInfo.getNum() <= 0))
			{
				// 品质为1,2,3的装备与合成所需碎片均没有
				resp.setResult(ErrorCode.EQUIP_UP_ERROR_12);
				return resp;
			}

			equipXMLInfo = EquipXMLInfoMap.getEquipXMLInfo(equipInfo.getEquipNo()); // 判断Equip.xml是否包含该物品

			if (equipXMLInfo == null)
			{
				resp.setResult(ErrorCode.EQUIP_REFINE_ERROR_3);
				return resp;
			}

			EquipStrengthenConfigInfo equipStrengthenConfigInfo = EquipXMLInfoMap.getEquipStrengthenConfigInfo(equipXMLInfo.getQuality());

			if (equipStrengthenConfigInfo == null)
			{
				resp.setResult(ErrorCode.EQUIP_REFINE_ERROR_3);
				return resp;
			}

			long itemSurplusNum = 0; // 装备升级碎片剩余数量
			long itemSurplusNumPlus = 0;
			
			long costMoney = total * GameValue.EQUIP_STRENGEH_RATE;
			
			if(roleInfo.getMoney() - GameValue.EQUIP_STRENGEH_RATE < 0){
				resp.setResult(ErrorCode.EQUIP_REFINE_ERROR_14);
				return resp;
			}

			if (costMoney > roleInfo.getMoney())
			{
				// 判断玩家是否有足够的银子升级
				costMoney = (roleInfo.getMoney() - roleInfo.getMoney() % GameValue.EQUIP_STRENGEH_RATE); // 判断剩余多少碎片
				itemSurplusNum = total - (roleInfo.getMoney() / GameValue.EQUIP_STRENGEH_RATE); // 银子不足时 退还碎片
				total = roleInfo.getMoney() / GameValue.EQUIP_STRENGEH_RATE;
			}

			EquipXMLUpgrade upgrade = null;
			int addLevel = 0;
			int srcLevel = equipInfo.getLevel();

			long exp = total + equipInfo.getExp(); //升级用
			upgrade = equipStrengthenConfigInfo.getEquipXMLUpgradeMap().get(srcLevel + addLevel);
			if (upgrade == null)
			{
				resp.setResult(ErrorCode.EQUIP_REFINE_ERROR_3);
				return resp;
			}
			
			if(upgrade.getExp() <= 0){
				resp.setResult(ErrorCode.EQUIP_UP_ERROR_14);
				return resp;
			}
			
			if(exp >= upgrade.getExp()){
				
				addLevel++;
				exp -= upgrade.getExp();
				
				while (true)
				{
					upgrade = equipStrengthenConfigInfo.getEquipXMLUpgradeMap().get(srcLevel + addLevel);
					if (upgrade == null)
					{
						resp.setResult(ErrorCode.EQUIP_REFINE_ERROR_3);
						return resp;
					}
					
					if (upgrade.getExp() <= 0)
					{
						
						// 达到满级
						addLevel = upgrade.getLevel() - srcLevel;
						itemSurplusNumPlus += exp;
						exp = upgrade.getExp();
						break;
					}
					
					if (exp >= upgrade.getExp())
					{
						// 升级
						addLevel++;
						exp -= upgrade.getExp();
						
					}
					else
					{
						break;
					}
					
					if ((srcLevel + addLevel) > mainHero.getHeroLevel())
					{
						addLevel = mainHero.getHeroLevel() - srcLevel; // 主将等级与原来等级的差
						upgrade = equipStrengthenConfigInfo.getEquipXMLUpgradeMap().get(srcLevel + addLevel);
						itemSurplusNumPlus += exp + 1; // 剩余的经验 + 1
						exp = upgrade.getExp() - 1; // 最大经验值少1
						break;
					}
				}
			}
			

			
			addLevel += srcLevel;
			
			if(itemSurplusNumPlus > 0){
				//还有剩余的碎片没消耗
				costMoney -= itemSurplusNumPlus * GameValue.EQUIP_STRENGEH_RATE;
				itemSurplusNum += itemSurplusNumPlus;
			}

			List<AbstractConditionCheck> conds = new ArrayList<AbstractConditionCheck>();
			conds.add(new MoneyCond(costMoney));

			int check = AbstractConditionCheck.checkCondition(roleInfo,conds);

			if (check != 1)
			{
				resp.setResult(check);
				return resp;
			}
			
			if (RoleService.subRoleResource(ActionType.action77.getType(), roleInfo, conds , null))
			{
				// 扣钱

				String updateSourceStr = RoleService.returnResourceChange(conds);
				if (updateSourceStr != null)
				{
					String[] sourceStr = updateSourceStr.split(",");
					if (sourceStr != null && sourceStr.length > 1)
					{
						resp.setSourceType(Byte.valueOf(sourceStr[0]));
						resp.setSourceChange(-Integer.valueOf(sourceStr[1]));
					}
				}

				// SceneService.sendRoleRefreshMsg(roleInfo.getId(),
				// SceneService.REFESH_TYPE_ROLE, null);
			}
			else
			{
				resp.setResult(ErrorCode.EQUIP_UP_ERROR_6);
				return resp;
			}

			if (equipDAO.updateEquipLevel(equipid, (short) addLevel, (int) exp)) {
				// 更新升级后的装备
				equipInfo.setLevel((short) addLevel);
				equipInfo.setExp((int) exp);
			}
			else
			{
				resp.setResult(ErrorCode.EQUIP_UP_ERROR_7);
				return resp;
			}

			if (ItemService.bagEquipDel(ActionType.action77.getType(),roleInfo, delList) != 1)
			{
				// 删除道具
				resp.setResult(ErrorCode.EQUIP_UP_ERROR_7);
				return resp;
			}

			if (isExist)
			{
				// 存在 更新数量
				Map<Integer, Integer> updateItemMap = new HashMap<Integer, Integer>();
				updateItemMap.put(bagItemInfo.getId(), (int) itemSurplusNum);
				Map<Integer,Integer> chgBagItemMap=new HashMap<Integer, Integer>();
				chgBagItemMap.put(bagItemInfo.getId(), bagItemInfo.getItemNo());
				
				if (ItemDAO.getInstance().updateBagItem(updateItemMap)){					
					if (itemSurplusNum > 0){
						bagItemInfo.setNum((int) itemSurplusNum);
					}
					else{
						BagItemMap.removeBagItem(roleInfo, bagItemInfo.getId());
					}				
					SceneService.sendRoleRefreshMsg(roleInfo.getId(), SceneService.REFESH_TYPE_ITEM, chgBagItemMap);
				}

			} 
			else if (!isExist && itemSurplusNum > 0)
			{
				int result = addEquipUpItem(roleInfo, (int)itemSurplusNum);
				if (result != 1) 
				{
					resp.setResult(ErrorCode.EQUIP_RESOLVE_ERROR_7);
					return resp;
				}
			}

			EquipService.refeshEquipProperty(equipInfo);

			if (heroId > 0) {
				HeroService.refeshHeroProperty(roleInfo, mainHero);
				resp.setFightValue(mainHero.getFightValue());
			}
			
			// 判断新手引导是否要更新
			int ck = GuideMgtService.dealGuideIndexNum(roleInfo, UserGuideNode.GAME_GUIDE_STRENGTH_EQUIPE);
			if(ck != 1){
				resp.setResult(ck);
				return resp;
			}

			resp.setResult(1);
			resp.setHeroId(heroId);

			resp.setExp(equipInfo.getExp());
			resp.setEquipId(equipid);
			resp.setEquipLevel((short) equipInfo.getLevel());

			resp.setEquipFightValue(equipInfo.getFightValue());
			resp.setItemSurplusNum((int) itemSurplusNum);
			
			//任务检测
			boolean isRedQuest = QuestService.checkQuest(roleInfo, ActionType.action77.getType(), equipInfo.getLevel() - beforeEquipLevel, true, false);
			// 红点监听
			boolean isRed = RedPointMgtService.check2PopRedPoint(roleId, null, false, 
					RedPointMgtService.LISTENING_MONEY_CHANGE_TYPES);
			//刷新红点
			if(isRedQuest || isRed) {
				RedPointMgtService.pop(roleId);
			}

			GameLogService.insertEquipUpLog(roleInfo,ActionType.action77.getType(), heroId, 
					equipid,equipInfo.getEquipNo(), beforeEquipLevel,
					equipInfo.getLevel());

		}

		return resp;
	}
	
	/**
	 * 增加装备强化碎片 只能添加装备强化碎片
	 * @param itemNum
	 * @return
	 */
	private static int addEquipUpItem(RoleInfo roleInfo, int itemNum){
		// 加入新的物品
		List<DropInfo> addItemList = new ArrayList<DropInfo>();

		DropInfo dropInfo = new DropInfo();
		dropInfo.setItemNo(GameValue.EQUIP_STRENGEH_ITEM + "");
		dropInfo.setItemNum(itemNum);
		dropInfo.setItemType(ItemService.checkItemType(dropInfo.getItemNo()));
		addItemList.add(dropInfo);

		// 添加道具和资源
		return ItemService.addPrize(ActionType.action77.getType(), roleInfo, addItemList,
				null, null, null, null, null, null, null, null, null, true);

	}
	
	/**
	 * 装备熔炼
	 * @param roleInfo
	 * @param equipIds
	 * @return
	 */
	private static EquipResolveResp resolve(RoleInfo roleInfo, String[] equipIds){
		EquipResolveResp resp = new EquipResolveResp();
		
		Map<Integer, EquipInfo> roleEquipMap = EquipInfoMap.getBagEquipMap(roleInfo.getId());
		Map<Integer, Integer> resolveMap = new HashMap<Integer, Integer>(); // 装备熔炼后得到的物品map
		int returnMoney = 0; // 熔炼返还的强化消耗的银子
		int refineReturnMoney = 0; //精炼返回的银子
		int returnItemNum = 0; // 返回的碎片
		int returnEnchant = 0;
		int enchantReturnMoney = 0;
		int id; // 物品Id
		EquipXMLInfo equipXMLInfo = null;
		EquipStrengthenConfigInfo equipStrengthenConfigInfo = null;
		EquipXMLUpgrade equipXmlUpgrade = null;
		EquipInfo equipInfo = null;
		List<Integer> delList = new ArrayList<Integer>(); // 批量删除道具用 数据库主键  缓存的主键
		List<DropInfo> addItemList = new ArrayList<DropInfo>();
		int equipMoney = 0;
		
		StringBuffer logSb = new StringBuffer();
		
		for(String equipId : equipIds){
			if (StringUtils.isBlank(equipId))
			{
				continue;
			}

			id = Integer.valueOf(equipId); // 对应Equip数据库表的主键
			equipInfo = roleEquipMap.get(id);
			
			if (equipInfo == null || equipInfo.getEquipType() == 9 || equipInfo.getEquipType() == 10)
			{
				// 角色不存在该物品
				resp.setResult(ErrorCode.EQUIP_RESOLVE_ERROR_6);
				return resp;
			}
			
			equipXMLInfo = EquipXMLInfoMap.getEquipXMLInfo(equipInfo.getEquipNo());
			if (equipXMLInfo == null)

			{
				resp.setResult(ErrorCode.EQUIP_RESOLVE_ERROR_5);
				return resp;
			}
			
			int result = ItemService.addItemAndEquipCheck(roleInfo);
			// 背包格子上限判断
			if (result != 1)
			{
				resp.setResult(ErrorCode.EQUIP_RESOLVE_ERROR_9);
				return resp;
			}

			// 普通分解

			if (equipXMLInfo.getResolve() > 0)
			{
				if (resolveMap.containsKey(equipXMLInfo.getResolve()))
				{
					// 包含同一种分解物品则增加数量

					resolveMap.put(equipXMLInfo.getResolve(),resolveMap.get(equipXMLInfo.getResolve()) + equipXMLInfo.getResolveNum());
				}
				else
				{

					resolveMap.put(equipXMLInfo.getResolve(), equipXMLInfo.getResolveNum());
				}

			}

			if (equipXMLInfo.getResolmoney() > 0)
			{
				equipMoney += equipXMLInfo.getResolmoney();
				DropInfo dropInfo = new DropInfo();
				dropInfo.setItemNo(ConditionType.attrParseType(equipXMLInfo.getResolmoney()).getName());
				dropInfo.setItemNum(equipXMLInfo.getResolveNum());
				dropInfo.setItemType(ItemService.checkItemType(dropInfo.getItemNo()));
				addItemList.add(dropInfo);
			}

			delList.add(id);
			
			logSb.append(id).append(":").append(equipInfo.getEquipNo()).append(";");

			if (equipInfo.getExp() > 0 || equipInfo.getLevel() > 0)
			{
				// 强化过 需要退还之前所有强化费用

				returnMoney += equipInfo.getExp();
				for (int i = 0; i < equipInfo.getLevel(); i++)
				{
					equipStrengthenConfigInfo = EquipXMLInfoMap.getEquipUpMap().get(equipXMLInfo.getQuality());
					if (equipStrengthenConfigInfo == null)
					{
						resp.setResult(ErrorCode.EQUIP_REFINE_ERROR_3);
						return resp;
					}

					equipXmlUpgrade = equipStrengthenConfigInfo.getEquipXMLUpgradeMap().get(i);

					if (equipXmlUpgrade == null)
					{
						resp.setResult(ErrorCode.EQUIP_REFINE_ERROR_3);
						return resp;
					}

					returnMoney += equipXmlUpgrade.getExp();
					// 返回碎片
					returnItemNum += equipXmlUpgrade.getExp();
				}

				returnItemNum += equipInfo.getExp(); // 返回碎片
			}
			
			if(equipInfo.getRefineLv() > 0)
			{
				//精炼重铸
				Map<Integer, EquipRefineInfo> refineMap = equipXMLInfo.getRefineMap();
				EquipRefineInfo equipRefineInfo = null;
				DropInfo dropInfo = null;
				
				for(int i = equipInfo.getRefineLv(); i > 0; i--)
				{
					equipRefineInfo = refineMap.get(i);
					if(equipRefineInfo == null)
					{
						continue;
					}
					
					refineReturnMoney+= equipRefineInfo.getDemoney(); //精炼消耗的银子
					
					dropInfo = new DropInfo();
					dropInfo.setItemNo(equipRefineInfo.getProp() + "");
					dropInfo.setItemNum(equipRefineInfo.getNum());
					
					addItemList.add(dropInfo);
					
					if(equipRefineInfo.getConsume() > 0)
					{
						dropInfo = new DropInfo();
						dropInfo.setItemNo(equipInfo.getEquipNo() + "");
						dropInfo.setItemNum(equipRefineInfo.getConsume());
						
						addItemList.add(dropInfo);
					}
					
				}
				
			}
			
			if(equipInfo.getEnchantLv() > 0 ){
				returnEnchant += equipInfo.getEnchantExp();
				EnchantXMLInfo xmlInfo = EquipXMLInfoMap.getEnchantXMLInfo(equipXMLInfo.getQuality());
				if (xmlInfo != null && equipInfo.getEnchantLv() > 0) {
					EnchantXMLUpgrade upgrade = null;
					for (int i = 1; i <= equipInfo.getEnchantLv(); i++) {
						upgrade = xmlInfo.getEnchantXMLUpgrade(i);
						if (upgrade != null) {
							returnEnchant += upgrade.getEnchantNum();
							enchantReturnMoney += upgrade.getMoney();
						}
					}
				}
			}
		}
		
		if (delList.size() > 0)
		{
			int result = ItemService.bagEquipDel(ActionType.action86.getType(), roleInfo, delList);
			if (result != 1)
			{
				resp.setResult(result);
				return resp;
			}
		}

		if (resolveMap.size() > 0)
		{
			// 添加物品到背包 普通分解
			DropInfo dropInfo = null;
			for (Map.Entry<Integer, Integer> map : resolveMap.entrySet())
			{
				dropInfo = new DropInfo();
				dropInfo.setItemNo(map.getKey() + "");
				dropInfo.setItemNum(map.getValue());
				dropInfo.setItemType(ItemService.checkItemType(dropInfo.getItemNo()));
				addItemList.add(dropInfo);
			}

			resp.setEquipMoney(equipMoney);
		}
		

		if (returnItemNum > 0)
		{
			// 返回的碎片
			DropInfo dropInfo = new DropInfo();
			dropInfo.setItemNo(GameValue.EQUIP_STRENGEH_ITEM + "");
			dropInfo.setItemNum(returnItemNum);
			dropInfo.setItemType(ItemService.checkItemType(dropInfo.getItemNo()));
			addItemList.add(dropInfo);

			resp.setReturnItemNum(returnItemNum);
		}
		
		if (returnMoney > 0)
		{
			// 有强化返回的银子
			DropInfo dropInfo = new DropInfo();
			dropInfo.setItemNo(ConditionType.TYPE_MONEY.getName());
			dropInfo.setItemNum(returnMoney * GameValue.EQUIP_STRENGEH_RATE);
			dropInfo.setItemType(ItemService.checkItemType(dropInfo.getItemNo()));
			addItemList.add(dropInfo);
		}
		
		if (refineReturnMoney > 0)
		{
			// 有强化返回的银子
			DropInfo dropInfo = new DropInfo();
			dropInfo.setItemNo(ConditionType.TYPE_MONEY.getName());
			dropInfo.setItemNum(refineReturnMoney);
			dropInfo.setItemType(ItemService.checkItemType(dropInfo.getItemNo()));
			addItemList.add(dropInfo);
		}
		
		if (enchantReturnMoney > 0)
		{
			// 有强化返回的银子
			DropInfo dropInfo = new DropInfo();
			dropInfo.setItemNo(ConditionType.TYPE_MONEY.getName());
			dropInfo.setItemNum(enchantReturnMoney);
			dropInfo.setItemType(ItemService.checkItemType(dropInfo.getItemNo()));
			addItemList.add(dropInfo);
		}
		
		if(returnEnchant > 0){
			// 有附魔返回的道具
			int propNo = GameValue.EQUIP_RETURN_ENCHANT_PROP_NO;
			PropXMLInfo propXml = PropXMLInfoMap.getPropXMLInfo(propNo);
			if(propXml!=null){
				int perNum = NumberUtils.toInt(propXml.getUseParam());
				if(perNum!=0){
					int itemNum = returnEnchant / perNum;
					if(itemNum > 0){
						DropInfo dropInfo = new DropInfo();
						dropInfo.setItemNo(propNo+"");
						dropInfo.setItemNum(itemNum);
						dropInfo.setItemType(ItemService.checkItemType(dropInfo.getItemNo()));
						addItemList.add(dropInfo);
					}
				}
			}
		}
		
		
		// 合并相同的物品和资源
		Map<String, Integer> derepeatMap = new HashMap<String, Integer>();
		for (DropInfo di : addItemList)
		{
			if (derepeatMap.containsKey(di.getItemNo()))
			{
				
				derepeatMap.put(di.getItemNo(), derepeatMap.get(di.getItemNo()) + di.getItemNum());
			}
			else
			{

				derepeatMap.put(di.getItemNo(), di.getItemNum());
			}
		}

		if (derepeatMap.size() > 0)
		{
			addItemList.clear();
			DropInfo dropInfo = null;

			for (Map.Entry<String, Integer> derepeat : derepeatMap.entrySet())
			{

				dropInfo = new DropInfo();
				dropInfo.setItemNo(derepeat.getKey());
				dropInfo.setItemNum(derepeat.getValue());
				dropInfo.setItemType(ItemService.checkItemType(dropInfo.getItemNo()));
				addItemList.add(dropInfo);
			}

		}

		// 添加道具和资源
		int result = ItemService.addPrize(ActionType.action86.getType(), roleInfo, addItemList, 
				null, null, null, null, null, null, null, null, null, true);

		if (result != 1)
		{
			resp.setResult(result);
			return resp;
		}

		if (addItemList.size() > 0)
		{
			StringBuffer sb = new StringBuffer();
			for (DropInfo di : addItemList)
			{
				sb.append(di.getItemNo()).append(":").append(di.getItemNum()).append(";");
			}

			resp.setResolveItems(sb.substring(0, sb.length() - 1).toString());

		}
		
		GameLogService.insertEquipResolveLog(roleInfo, logSb.substring(0, logSb.length() - 1).toString(), resp.getResolveItems(), 0, System.currentTimeMillis());
		
		resp.setResult(1);
		
		return resp;
	}
	
	/**
	 * 装备重铸
	 * @param roleInfo
	 * @param equipIds
	 * @return
	 */
	private static EquipResolveResp resetEq(RoleInfo roleInfo, String[] equipIds){
		EquipResolveResp resp = new EquipResolveResp();
		
		Map<Integer, EquipInfo> roleEquipMap = EquipInfoMap.getBagEquipMap(roleInfo.getId());
		int returnMoney = 0; // 熔炼返还的强化消耗的银子
		int returnItemNum = 0; // 返回的碎片
		int refineReturnMoney = 0; //精炼返还的银子
		int returnEnchant = 0;
		int enchantReturnMoney = 0;
		int id; // 物品Id
		EquipXMLInfo equipXMLInfo = null;
		EquipStrengthenConfigInfo equipStrengthenConfigInfo = null;
		EquipXMLUpgrade equipXmlUpgrade = null;
		EquipInfo equipInfo = null;
		List<Integer> updateList = new ArrayList<Integer>(); // 批量更新道具用 数据库主键  缓存的主键
		List<AbstractConditionCheck> conds = new ArrayList<AbstractConditionCheck>();
		List<DropInfo> addItemList = new ArrayList<DropInfo>();
		
		StringBuffer logSb = new StringBuffer();
		
		for(String equipId : equipIds){
			id = Integer.valueOf(equipId); // 对应Equip数据库表的主键
			equipInfo = roleEquipMap.get(id);

			if (equipInfo == null ||  equipInfo.getEquipType() == 9 || equipInfo.getEquipType() == 10)
			{
				// 角色不存在该物品
				resp.setResult(ErrorCode.EQUIP_RESOLVE_ERROR_6);
				return resp;
			}

			equipXMLInfo = EquipXMLInfoMap.getEquipXMLInfo(equipInfo.getEquipNo());
			if (equipXMLInfo == null)

			{
				resp.setResult(ErrorCode.EQUIP_RESOLVE_ERROR_5);
				return resp;
			}
			
			// 重铸

			conds.add(new CoinCond(equipXMLInfo.getGold()));
			int check = AbstractConditionCheck.checkCondition(roleInfo, conds);
			if (check != 1)
			{
				resp.setResult(check);
				return resp;
			}

			updateList.add(id);

			logSb.append(id).append(":").append(equipInfo.getEquipNo()).append(";");
			
			if (equipInfo.getExp() > 0 || equipInfo.getLevel() > 0)
			{
				// 强化过 需要退还之前所有强化费用

				returnMoney += equipInfo.getExp();
				for (int i = 0; i < equipInfo.getLevel(); i++)
				{
					equipStrengthenConfigInfo = EquipXMLInfoMap.getEquipUpMap().get(equipXMLInfo.getQuality());
					if (equipStrengthenConfigInfo == null)
					{
						resp.setResult(ErrorCode.EQUIP_REFINE_ERROR_3);
						return resp;
					}

					equipXmlUpgrade = equipStrengthenConfigInfo.getEquipXMLUpgradeMap().get(i);

					if (equipXmlUpgrade == null)
					{
						resp.setResult(ErrorCode.EQUIP_REFINE_ERROR_3);
						return resp;
					}

					returnMoney += equipXmlUpgrade.getExp();
					// 返回碎片
					returnItemNum += equipXmlUpgrade.getExp();
				}

				returnItemNum += equipInfo.getExp(); // 返回碎片
			}
			
			if(equipInfo.getRefineLv() > 0)
			{
				//精炼重铸
				Map<Integer, EquipRefineInfo> refineMap = equipXMLInfo.getRefineMap();
				EquipRefineInfo equipRefineInfo = null;
				DropInfo dropInfo = null;
				
				for(int i = equipInfo.getRefineLv(); i > 0; i--)
				{
					equipRefineInfo = refineMap.get(i);
					if(equipRefineInfo == null)
					{
						continue;
					}
					
					refineReturnMoney+= equipRefineInfo.getDemoney(); //精炼消耗的银子
					
					dropInfo = new DropInfo();
					dropInfo.setItemNo(equipRefineInfo.getProp() + "");
					dropInfo.setItemNum(equipRefineInfo.getNum());
					
					addItemList.add(dropInfo);
					
					if(equipRefineInfo.getConsume() > 0)
					{
						dropInfo = new DropInfo();
						dropInfo.setItemNo(equipInfo.getEquipNo() + "");
						dropInfo.setItemNum(equipRefineInfo.getConsume());
						
						addItemList.add(dropInfo);
					}
					
				}
			}
			if(equipInfo.getEnchantLv() > 0 ){
				returnEnchant += equipInfo.getEnchantExp();
				EnchantXMLInfo xmlInfo = EquipXMLInfoMap.getEnchantXMLInfo(equipXMLInfo.getQuality());
				if (xmlInfo != null && equipInfo.getEnchantLv() > 0) {
					EnchantXMLUpgrade upgrade = null;
					for (int i = 1; i <= equipInfo.getEnchantLv(); i++) {
						upgrade = xmlInfo.getEnchantXMLUpgrade(i);
						if (upgrade != null) {
							returnEnchant += upgrade.getEnchantNum();
							enchantReturnMoney += upgrade.getMoney();
						}
					}
				}
			}
		}
		
		if (updateList.size() > 0)
		{
			if (!EquipDAO.getInstance().batchUpdateEquipLevel(updateList))
			{
				resp.setResult(ErrorCode.EQUIP_RESOLVE_ERROR_8);
				return resp;
			}

			StringBuffer sb = new StringBuffer();

			for (Integer updateId : updateList)
			{
				equipInfo = roleEquipMap.get(updateId);
				if (equipInfo == null)
				{
					continue;
				}

				equipInfo.setLevel((short) 0);
				equipInfo.setExp(0);
				equipInfo.setRefineLv((short)0);
				equipInfo.setEnchantLv((short)0);
				equipInfo.setEnchantExp(0);
				
				sb.append(updateId).append(":");
			}
			
			resp.setResetEquipInfo(sb.substring(0, sb.length() - 1));
			
		}
		
		if (RoleService.subRoleResource(ActionType.action89.getType(), roleInfo, conds , null))
		{
			// 扣钱

			String updateSourceStr = RoleService.returnResourceChange(conds);
			if (updateSourceStr != null)
			{
				String[] sourceStr = updateSourceStr.split(",");
				if (sourceStr != null && sourceStr.length > 1)
				{
					resp.setSourceType(Byte.valueOf(sourceStr[0]));
					resp.setSourceChange(-Integer.valueOf(sourceStr[1]));
				}
			}
		}
		
		if (returnItemNum > 0)
		{
			// 返回的碎片
			DropInfo dropInfo = new DropInfo();
			dropInfo.setItemNo(GameValue.EQUIP_STRENGEH_ITEM + "");
			dropInfo.setItemNum(returnItemNum);
			dropInfo.setItemType(ItemService.checkItemType(dropInfo.getItemNo()));
			addItemList.add(dropInfo);

			resp.setReturnItemNum(returnItemNum);
		}
		
		if (returnMoney > 0)
		{
			// 有强化返回的银子
			DropInfo dropInfo = new DropInfo();
			dropInfo.setItemNo(ConditionType.TYPE_MONEY.getName());
			dropInfo.setItemNum(returnMoney * GameValue.EQUIP_STRENGEH_RATE);
			dropInfo.setItemType(ItemService.checkItemType(dropInfo.getItemNo()));
			addItemList.add(dropInfo);
		}
		
		if (refineReturnMoney > 0)
		{
			// 有强化返回的银子
			DropInfo dropInfo = new DropInfo();
			dropInfo.setItemNo(ConditionType.TYPE_MONEY.getName());
			dropInfo.setItemNum(refineReturnMoney);
			dropInfo.setItemType(ItemService.checkItemType(dropInfo.getItemNo()));
			addItemList.add(dropInfo);
		}
		
		if (enchantReturnMoney > 0)
		{
			// 有强化返回的银子
			DropInfo dropInfo = new DropInfo();
			dropInfo.setItemNo(ConditionType.TYPE_MONEY.getName());
			dropInfo.setItemNum(enchantReturnMoney);
			dropInfo.setItemType(ItemService.checkItemType(dropInfo.getItemNo()));
			addItemList.add(dropInfo);
		}
		
		if(returnEnchant > 0){
			// 有附魔返回的道具
			int propNo = GameValue.EQUIP_RETURN_ENCHANT_PROP_NO;
			PropXMLInfo propXml = PropXMLInfoMap.getPropXMLInfo(propNo);
			if(propXml!=null){
				int perNum = NumberUtils.toInt(propXml.getUseParam());
				if(perNum!=0){
					int itemNum = returnEnchant / perNum;
					if(itemNum > 0){
						DropInfo dropInfo = new DropInfo();
						dropInfo.setItemNo(propNo+"");
						dropInfo.setItemNum(itemNum);
						dropInfo.setItemType(ItemService.checkItemType(dropInfo.getItemNo()));
						addItemList.add(dropInfo);
					}
				}
			}
		}
		
		// 合并相同的物品和资源
		Map<String, Integer> derepeatMap = new HashMap<String, Integer>();
		for (DropInfo di : addItemList)
		{
			if (derepeatMap.containsKey(di.getItemNo()))
			{
				
				derepeatMap.put(di.getItemNo(), derepeatMap.get(di.getItemNo()) + di.getItemNum());
			}
			else
			{

				derepeatMap.put(di.getItemNo(), di.getItemNum());
			}
		}

		if (derepeatMap.size() > 0)
		{
			addItemList.clear();
			DropInfo dropInfo = null;

			for (Map.Entry<String, Integer> derepeat : derepeatMap.entrySet())
			{

				dropInfo = new DropInfo();
				dropInfo.setItemNo(derepeat.getKey());
				dropInfo.setItemNum(derepeat.getValue());
				dropInfo.setItemType(ItemService.checkItemType(dropInfo.getItemNo()));
				addItemList.add(dropInfo);
			}

		}
		
		// 添加道具和资源
		int result = ItemService.addPrize(ActionType.action89.getType(), roleInfo, addItemList, 
				null, null, null, null, null, null, null, null, null, true);

		if (result != 1)
		{
			resp.setResult(result);
			return resp;
		}

		if (addItemList.size() > 0)
		{
			StringBuffer sb = new StringBuffer();
			for (DropInfo di : addItemList)
			{
				sb.append(di.getItemNo()).append(":").append(di.getItemNum()).append(";");
			}

			resp.setResolveItems(sb.substring(0, sb.length() - 1).toString());

		}
		
		GameLogService.insertEquipResolveLog(roleInfo, logSb.substring(0, logSb.length() - 1).toString(), resp.getResolveItems(), 1, System.currentTimeMillis());
		
		resp.setResult(1);
		return resp;
	}
	
	/**
	 * 星石分解
	 * @param roleInfo
	 * @param stars
	 * @return
	 */
	private EquipResolveResp resolveStar(RoleInfo roleInfo, String stars) {
		EquipResolveResp resp = new EquipResolveResp();
		String[] starInfos = stars.split(";");
		
		Map<Integer, Integer> map = new HashMap<Integer, Integer>();
		
		String[] starInfo = null;
		for(String star : starInfos){
			starInfo = star.split(":");
			if(starInfo == null){
				resp.setResult(ErrorCode.EQUIP_RESOLVE_ERROR_5);
				return resp;
			}
			
			int id = 0, num = 0;
			try{
				id = Integer.parseInt(starInfo[0]);
				num = Integer.parseInt(starInfo[1]);
				
			}catch(Exception e){
				e.printStackTrace();
			}
			
			if(map.containsKey(id)){
				map.put(id, map.get(id) + num);
				
			}else{
				map.put(id, num);
			}
		}
		
		if(map.size() > 0){
			StringBuffer logSb = new StringBuffer();
			BagItemInfo bagItemInfo = null;
			PropXMLInfo propXMLInfo = null;
			Map<Integer, Integer> updateMap = new HashMap<Integer, Integer>();
			int starMoney = 0;
			for(Integer id : map.keySet()){
				bagItemInfo = BagItemMap.getBagItemById(roleInfo, id);
				if(bagItemInfo == null){
					resp.setResult(ErrorCode.EQUIP_RESOLVE_ERROR_6);
					return resp;
				}
				
				propXMLInfo = PropXMLInfoMap.getPropXMLInfo(bagItemInfo.getItemNo());
				if(propXMLInfo == null){
					resp.setResult(ErrorCode.EQUIP_REFINE_ERROR_3);
					return resp;
				}
				
				if(!(propXMLInfo.getNo() + "").startsWith(GameValue.PROP_STAR_N0)){ //不是星石
					resp.setResult(ErrorCode.EQUIP_RESOLVE_ERROR_16);
					return resp;
				}
				
				int num = map.get(id);
				
				logSb.append(bagItemInfo.getItemNo()).append(":").append(num).append(";"); //日志用
				
				starMoney += propXMLInfo.getResolmoney() * num;
				
				int itemSurplusNum = bagItemInfo.getNum() - num;
				
				if(itemSurplusNum < 0){
					resp.setResult(ErrorCode.EQUIP_RESOLVE_ERROR_13);
					return resp;
				}
				
				logSb.append(bagItemInfo.getItemNo()).append(":").append(num).append(";"); //日志用
				
				updateMap.put(id, itemSurplusNum);
				
				map.put(id, bagItemInfo.getNum()); //把使用数量改成消耗前的数量，用于记录日志时候使用
			}
			
			//更新角色星石币
			if(starMoney > 0){
				if(!RoleService.addRoleRoleResource(ActionType.action472.getType(), roleInfo, ConditionType.TYPE_STAR_MONEY, starMoney,null)){
					resp.setResult(ErrorCode.EQUIP_RESOLVE_ERROR_15);
					return resp;
				}
			}
			
			if(ItemDAO.getInstance().updateBagItem(updateMap)){
				for(Map.Entry<Integer, Integer> item : updateMap.entrySet()){
					bagItemInfo = BagItemMap.getBagItemById(roleInfo, item.getKey());
					if(bagItemInfo == null){
						continue;
					}
					
					//清楚缓存
					if(item.getValue() > 0){
						
						bagItemInfo.setNum(item.getValue());
					}else{
						
						BagItemMap.removeBagItem(roleInfo, bagItemInfo.getId());
					}
					
					GameLogService.insertItemLog(roleInfo, ActionType.action472.getType(), 1, bagItemInfo.getItemNo(), map.get(item.getKey()), updateMap.get(item.getKey()));
				}
			}
			
			resp.setResolveItems(GameValue.SATR_MONEY_ITEM + ":" + starMoney);

			GameLogService.insertEquipResolveLog(roleInfo, logSb.substring(0, logSb.length() - 1).toString(), resp.getResolveItems(), 2, System.currentTimeMillis());
			
			resp.setStarMoney(roleInfo.getRoleLoadInfo().getStarMoney());
		}
		
		resp.setResult(1);
		return resp;
	}
	
	/**
	 * 装备附魔
	 * @param roleId
	 * @param req
	 * @return
	 */
	public EquipEnchantResp equipEnchant(int roleId, EquipEnchantReq req) {
		EquipEnchantResp resp = new EquipEnchantResp();
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			resp.setResult(ErrorCode.EQUIP_ENCHANT_ERROR_1);
			return resp;
		}
		String enchantItems = req.getEnchantItems();
		if (StringUtils.isBlank(enchantItems)) {
			resp.setResult(ErrorCode.EQUIP_ENCHANT_ERROR_2);
			return resp;
		}
		synchronized (roleInfo) {
			int mainHeroLv = HeroInfoMap.getMainHeroLv(roleId);
			int heroId = req.getHeroId();
			int equipId = req.getEquipId();
			HeroInfo heroInfo = null;
			EquipInfo equipInfo = null;
			if (heroId != 0) {
				heroInfo = HeroInfoMap.getHeroInfo(roleId, heroId);
				if (heroInfo == null) {
					resp.setResult(ErrorCode.EQUIP_ENCHANT_ERROR_3);
					return resp;
				}
				if (heroInfo.getDeployStatus() != HeroInfo.DEPLOY_TYPE_MAIN) {
					resp.setResult(ErrorCode.EQUIP_ENCHANT_ERROR_4);
					return resp;
				}
				equipInfo = EquipInfoMap.getHeroEquip(heroInfo, equipId);
				if (equipInfo == null) {
					resp.setResult(ErrorCode.EQUIP_ENCHANT_ERROR_5);
					return resp;
				}
			} else {
				equipInfo = EquipInfoMap.getBagEquip(roleId, equipId);
				if (equipInfo == null) {
					resp.setResult(ErrorCode.EQUIP_ENCHANT_ERROR_6);
					return resp;
				}
			}
			Map<Integer, Integer> itemMap = new HashMap<Integer, Integer>();
			String[] infos = enchantItems.split(";");
			PropXMLInfo propXMLInfo = null;
			int addEnchantNum = 0;
			for (String info : infos) {
				String[] keys = info.split(",");
				if (keys.length < 2) {
					resp.setResult(ErrorCode.EQUIP_ENCHANT_ERROR_7);
					return resp;
				}
				int key = NumberUtils.toInt(keys[0]);
				int val = NumberUtils.toInt(keys[1]);
				propXMLInfo = PropXMLInfoMap.getPropXMLInfo(key);
				if (propXMLInfo == null) {
					resp.setResult(ErrorCode.EQUIP_ENCHANT_ERROR_8);
					return resp;
				}
				if (propXMLInfo.getSubType() != 40 && propXMLInfo.getSubType() != 41
						&& NumberUtils.toInt(propXMLInfo.getUseParam()) != 0) {
					resp.setResult(ErrorCode.EQUIP_ENCHANT_ERROR_9);
					return resp;
				}
				if (!BagItemMap.checkBagItemNum(roleInfo, key, val)) {
					resp.setResult(ErrorCode.EQUIP_ENCHANT_ERROR_10);
					return resp;
				}
				if (itemMap.containsKey(key)) {
					resp.setResult(ErrorCode.EQUIP_ENCHANT_ERROR_11);
					return resp;
				} else {
					itemMap.put(key, val);
				}
				addEnchantNum += NumberUtils.toInt(propXMLInfo.getUseParam()) * val;
			}
			if (addEnchantNum > 0) {
				EquipXMLInfo equipXMLInfo = EquipXMLInfoMap.getEquipXMLInfo(equipInfo.getEquipNo());
				if (equipXMLInfo == null) {
					resp.setResult(ErrorCode.EQUIP_ENCHANT_ERROR_12);
					return resp;
				}
				EnchantXMLInfo enchantXMLInfo = EquipXMLInfoMap.getEnchantXMLInfo(equipXMLInfo.getQuality());
				if (enchantXMLInfo == null) {
					resp.setResult(ErrorCode.EQUIP_ENCHANT_ERROR_13);
					return resp;
				}
				int pzMaxLv = enchantXMLInfo.getMaxLv();// 配置最高等级
				int maxLv = Math.min(equipXMLInfo.getMaxEnchantLv(), pzMaxLv);// 当前最高等级
				if (equipInfo.getEnchantLv() >= maxLv) {
					resp.setResult(ErrorCode.EQUIP_ENCHANT_ERROR_14);
					return resp;
				}
				EnchantXMLUpgrade upgrade = null;
				int costMoney = 0;
				int afterLevel = equipInfo.getEnchantLv();
				int beforeLevel = equipInfo.getEnchantLv();
				int afterExp = equipInfo.getEnchantExp() + addEnchantNum;
				while (afterLevel <= maxLv) {
					upgrade = enchantXMLInfo.getEnchantXMLUpgrade(afterLevel + 1);
					if (upgrade != null){
						if(mainHeroLv < upgrade.getLevel()){
							if(afterLevel == equipInfo.getEnchantLv()){
								resp.setResult(ErrorCode.EQUIP_ENCHANT_ERROR_17);
								return resp;
							}
							if(upgrade.getEnchantNum() <= afterExp){
								afterExp = upgrade.getEnchantNum();
							}
							break;
						}
						if(upgrade.getEnchantNum() <= afterExp){
							if (afterLevel == maxLv) {
								if (maxLv < pzMaxLv) {
									afterExp = upgrade.getEnchantNum();
								} else {
									afterExp = 0;
								}
							} else {
								afterExp -= upgrade.getEnchantNum();
							}
							costMoney += upgrade.getMoney();
						} else {
							break;
						}					
					} else {
						break;
					}
					if (afterLevel == maxLv) {
						break;
					}
					afterLevel++;
				}
				List<AbstractConditionCheck> conds = new ArrayList<AbstractConditionCheck>();
				conds.add(new MoneyCond(costMoney));
				if(costMoney > 0){
					int check = AbstractConditionCheck.checkCondition(roleInfo, conds);
					if (check != 1) {
						resp.setResult(check);
						return resp;
					}
				}
				if(itemMap.size() > 0){
					int check = ItemService.bagItemDel(ActionType.action92.getType(), roleInfo, itemMap);
					if (check != 1) {
						resp.setResult(check);
						return resp;
					}
				}
				if(costMoney > 0){
					if (RoleService.subRoleResource(ActionType.action92.getType(), roleInfo, conds, null)) {
						resp.setSourceType((byte)ConditionType.TYPE_MONEY.getType());
						resp.setSourceChange(-costMoney);
					} else {
						resp.setResult(ErrorCode.EQUIP_ENCHANT_ERROR_15);
						return resp;
					}
				}

				if (equipDAO.updateEquipEnchant(equipId, (short) afterLevel, afterExp)) {
					equipInfo.setEnchantLv((short) afterLevel);
					equipInfo.setEnchantExp(afterExp);
				} else {
					resp.setResult(ErrorCode.EQUIP_ENCHANT_ERROR_16);
					return resp;
				}

				// 刷新装备属性 战斗力
				EquipService.refeshEquipProperty(equipInfo);
				if (heroInfo != null) {
					// 刷新英雄属性 战斗力
					HeroService.refeshHeroProperty(roleInfo, heroInfo);
				}
				
				//附魔
				GameLogService.insertEquipUpLog(roleInfo, ActionType.action92.getType(), heroId, equipId,
						equipInfo.getEquipNo(), beforeLevel, equipInfo.getEnchantLv());
			}

			resp.setResult(1);
			resp.setHeroId(heroId);
			if (heroInfo != null) {
				resp.setFightValue(heroInfo.getFightValue());
			}
			resp.setEquipId(equipId);
			resp.setEnchantLv(equipInfo.getEnchantLv());
			resp.setEnchantExp(equipInfo.getEnchantExp());
			resp.setEquipFightValue(equipInfo.getFightValue());
			return resp;
		}
	}
}
