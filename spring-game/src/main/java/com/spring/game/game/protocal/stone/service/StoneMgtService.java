package com.snail.webgame.game.protocal.stone.service;

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
import com.snail.webgame.game.condtion.AbstractConditionCheck;
import com.snail.webgame.game.condtion.conds.MoneyCond;
import com.snail.webgame.game.core.GameLogService;
import com.snail.webgame.game.dao.EquipDAO;
import com.snail.webgame.game.info.EquipInfo;
import com.snail.webgame.game.info.HeroInfo;
import com.snail.webgame.game.info.RoleInfo;
import com.snail.webgame.game.protocal.equip.service.EquipService;
import com.snail.webgame.game.protocal.hero.service.HeroService;
import com.snail.webgame.game.protocal.item.service.ItemService;
import com.snail.webgame.game.protocal.redPoint.service.RedPointMgtService;
import com.snail.webgame.game.protocal.rolemgt.service.RoleService;
import com.snail.webgame.game.protocal.stone.compose.CompStoneReq;
import com.snail.webgame.game.protocal.stone.compose.CompStoneResp;
import com.snail.webgame.game.protocal.stone.inlay.InlayStoneReq;
import com.snail.webgame.game.protocal.stone.inlay.InlayStoneResp;
import com.snail.webgame.game.xml.cache.PropXMLInfoMap;
import com.snail.webgame.game.xml.cache.StoneCompXMLMap;
import com.snail.webgame.game.xml.info.PropXMLInfo;
import com.snail.webgame.game.xml.info.StoneComeXML;

public class StoneMgtService {

	/**
	 * 合成高阶宝石
	 * @param roleId
	 * @param req
	 * @return
	 */
	public CompStoneResp compStone(int roleId, CompStoneReq req) {
		CompStoneResp resp = new CompStoneResp();
		resp.setResult(1);
		int stoneNo1 = req.getStoneNo1();
		int stoneNo2 = req.getStoneNo2();
		int compCount = req.getCompCount();// 合成数量

		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			resp.setResult(ErrorCode.COMP_STONE_ERROR_1);
			return resp;
		}

		if (compCount <= 0) {
			resp.setResult(ErrorCode.COMP_STONE_ERROR_2);
			return resp;
		}

		if (compCount > GameValue.STONE_COMPOSE_MAX_NUM) {
			resp.setResult(ErrorCode.COMP_STONE_ERROR_3);
			return resp;
		}

		synchronized (roleInfo) {
			// 获得该宝石的类别、等级
			PropXMLInfo prop1 = PropXMLInfoMap.getPropXMLInfo(stoneNo1);
			PropXMLInfo prop2 = PropXMLInfoMap.getPropXMLInfo(stoneNo2);
			StoneComeXML stone1 = StoneCompXMLMap.getXml(stoneNo1);
			StoneComeXML stone2 = StoneCompXMLMap.getXml(stoneNo2);
			if (prop1 == null || prop2 == null || stone1 == null || stone2 == null) {
				resp.setResult(ErrorCode.COMP_STONE_ERROR_4);
				return resp;
			}
			if (stone2.getMaterial() != stoneNo1) {
				resp.setResult(ErrorCode.COMP_STONE_ERROR_5);
				return resp;
			}

			int itemNum = stone2.getNum() * compCount;
			if (itemNum <= 0) {
				resp.setResult(ErrorCode.COMP_STONE_ERROR_6);
				return resp;
			}

			// 判断宝石数量
			if (!BagItemMap.checkBagItemNum(roleInfo, stoneNo1, itemNum)) {
				resp.setResult(ErrorCode.COMP_STONE_ERROR_7);
				return resp;
			}

			// 检测购买条件
			List<AbstractConditionCheck> conds = new ArrayList<AbstractConditionCheck>();
			conds.add(new MoneyCond(stone2.getSilver() * compCount));

			int check = AbstractConditionCheck.checkCondition(roleInfo,conds);
			if (check != 1) {
				resp.setResult(check);
				return resp;
			}

			// 增加高等级宝石,为了防止背包叠加超上限
			List<DropInfo> itemList = new ArrayList<DropInfo>();// 道具
			itemList.add(new DropInfo(String.valueOf(stoneNo2), compCount));
			int result = ItemService.itemAdd(ActionType.action82.getType(), roleInfo, itemList, null, null,
					null, null, true);
			if (result != 1) {
				resp.setResult(result);
				return resp;
			}

			// 扣除低等级宝石
			HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
			map.put(stoneNo1, itemNum);
			int result1 = ItemService.bagItemDel(ActionType.action82.getType(), roleInfo, map);
			if (result1 != 1) {
				resp.setResult(result1);
				return resp;
			}

			// 扣去消耗资源
			if (RoleService.subRoleResource(ActionType.action82.getType(), roleInfo, conds , null)) {
				String updateSourceStr = RoleService.returnResourceChange(conds);
				if(updateSourceStr != null)
				{
					String[] sourceStr = updateSourceStr.split(",");
					if(sourceStr != null && sourceStr.length > 1)
					{
						resp.setSourceType(Byte.valueOf(sourceStr[0]));
						resp.setSourceChange(-Integer.valueOf(sourceStr[1]));
					}
				}
				//SceneService.sendRoleRefreshMsg(roleInfo.getId(), SceneService.REFESH_TYPE_ROLE, "");
			} else {
				resp.setResult(ErrorCode.COMP_STONE_ERROR_8);
				return resp;
			}

		}
		
		// 红点监听
		RedPointMgtService.check2PopRedPoint(roleInfo.getId(), null, true, GameValue.RED_POINT_TYPE_HERO);
		
		resp.setResult(1);
		
		GameLogService.insertPlayActionLog(roleInfo, ActionType.action82.getType(), "");
		return resp;
	}

	/**
	 * 镶嵌宝石到装备
	 */
	public InlayStoneResp inlayStone(int roleId, InlayStoneReq req) {
		InlayStoneResp resp = new InlayStoneResp();
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			resp.setResult(ErrorCode.INLAY_STONE_ERROR_1);
			return resp;
		}
		int heroId = req.getHeroId();
		int equipId = req.getEquipId();
		byte flag = req.getFlag();
		int stoneNo = req.getStoneNo();
		int seat = req.getSeat();// 宝石镶嵌或摘除所在位置
		if (seat <= 0 || seat > GameValue.STONE_INLAY_MAX_NUM) {// 请正确选择所要镶嵌的部位
			resp.setResult(ErrorCode.INLAY_STONE_ERROR_2);
			return resp;
		}
		synchronized (roleInfo) {
			HeroInfo heroInfo = HeroInfoMap.getHeroInfo(roleId, heroId);
			if (heroInfo == null) {
				resp.setResult(ErrorCode.INLAY_STONE_ERROR_3);
				return resp;
			}
			// 英雄装备背包
			EquipInfo equipInfo = null;

			if (heroId > 0) {
				equipInfo = EquipInfoMap.getHeroEquip(heroInfo, equipId);
			} else {
				equipInfo = EquipInfoMap.getBagEquip(roleId, equipId);
			}

			if (equipInfo == null) {
				resp.setResult(ErrorCode.INLAY_STONE_ERROR_4);
				return resp;
			}
			int result = 0;
			switch (flag) {
			case 0:// 镶嵌
				result = inlayStone(roleInfo, heroInfo, equipInfo, seat, stoneNo);
				break;
			case 1:// 摘除
				result = deleteStone(roleInfo, heroInfo, equipInfo, seat,resp);
				break;
			default:
				result = ErrorCode.INLAY_STONE_ERROR_5;
				break;
			}
			if (result != 1) {
				resp.setResult(result);
				return resp;
			}
			resp.setResult(1);
			resp.setHeroId(heroId);
			resp.setEquipId(equipId);
			resp.setFightValue(heroInfo.getFightValue());
			resp.setEquipInfo(EquipService.getEquipInfoRe((int) heroInfo.getId(), equipInfo));
			//resp.setEquipPro(HeroService.getHeroPropertyRe(HeroService.getEquipAddProperty(heroInfo)));
		}

		return resp;
	}

	/**
	 * 镶嵌宝石
	 * @param roleInfo
	 * @param heroInfo
	 * @param equipInfo
	 * @param seat
	 * @param stoneNo
	 * @return
	 */
	private int inlayStone(RoleInfo roleInfo, HeroInfo heroInfo, EquipInfo equipInfo, int seat, int stoneNo) {
		if (stoneNo <= 0) {
			return ErrorCode.INLAY_EQUIP_STONE_ERROR_1;
		}
		PropXMLInfo stoneXMLInfo = PropXMLInfoMap.getPropXMLInfo(stoneNo);
		if (stoneXMLInfo == null) {
			return ErrorCode.INLAY_EQUIP_STONE_ERROR_2;
		}
		// 判断宝石数量是否满足
		if (!BagItemMap.checkBagItemNum(roleInfo, stoneNo, 1)) {
			return ErrorCode.INLAY_EQUIP_STONE_ERROR_3;
		}
		// 同一装备只能镶嵌同一类型宝石
		if (equipInfo.getEquipStoneSeatByStoneNo(stoneNo) != 0) {
			return ErrorCode.INLAY_EQUIP_STONE_ERROR_4;
		}
		// 宝石信息<seat,stoneNo>
		Map<Integer, Integer> stoneMap = equipInfo.getStoneMap();
		if (stoneMap == null) {
			stoneMap = new HashMap<Integer, Integer>();
		}
		// 返还老宝石
		List<DropInfo> dropItems = new ArrayList<DropInfo>(1);
		if (stoneMap.containsKey(seat)) {
			// 该位置已有宝石
			int existStoneNo = stoneMap.get(seat);
			PropXMLInfo existStoneXML = PropXMLInfoMap.getPropXMLInfo(existStoneNo);
			if (existStoneXML != null && existStoneXML.getSubType() == stoneXMLInfo.getSubType()) {
				return ErrorCode.INLAY_EQUIP_STONE_ERROR_5;
			}
			dropItems.add(new DropInfo(existStoneNo + "", 1));
		}
		stoneMap.put(seat, stoneNo);
		// 扣掉宝石
		HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
		map.put(stoneNo, 1);
		int result = ItemService.bagItemDel(ActionType.action81.getType(), roleInfo, map);
		if (result != 1) {
			return result;
		}

		// 更新装备宝石信息
		String stoneStr = equipInfo.getStoneStr(stoneMap);
		if (EquipDAO.getInstance().updateEquipStones(equipInfo.getId(), stoneStr)) {
			EquipService.refeshEquipProperty(equipInfo);
			if (heroInfo != null) {
				HeroService.refeshHeroProperty(roleInfo, heroInfo);
			}
		} else {
			return ErrorCode.INLAY_EQUIP_STONE_ERROR_6;
		}
		// 返还老宝石
		int re = ItemService.itemAdd(ActionType.action81.getType(), roleInfo, dropItems, null, 
				null, null, null , true);
		if (re != 1) {
			return re;
		}

		GameLogService.insertPlayActionLog(roleInfo, ActionType.action81.getType(), "");
		return 1;
	}

	/**
	 * 摘除宝石
	 * @param roleInfo
	 * @param heroInfo
	 * @param equipInfo
	 * @param seat
	 * @return
	 */
	private int deleteStone(RoleInfo roleInfo, HeroInfo heroInfo, EquipInfo equipInfo, int seat,InlayStoneResp resp) {
		// 宝石信息<seat,stoneNo>
		Map<Integer, Integer> stoneMap = equipInfo.getStoneMap();
		if (stoneMap == null) {
			stoneMap = new HashMap<Integer, Integer>();
		}
		if (!stoneMap.containsKey(seat)) {
			return ErrorCode.DELETE_EQUIP_STONE_ERROR_1;
		}
		int exisitStoneNo = stoneMap.get(seat);
		// 宝石摘除消耗
		StoneComeXML stone = StoneCompXMLMap.getXml(exisitStoneNo);
		if (stone == null) {
			return ErrorCode.DELETE_EQUIP_STONE_ERROR_2;
		}
		List<AbstractConditionCheck> conds = new ArrayList<AbstractConditionCheck>();
		conds.add(new MoneyCond(stone.getRemoveSilver()));

		int check = AbstractConditionCheck.checkCondition(roleInfo,conds);
		if (check != 1) {
			return check;
		}

		// 扣去消耗资源
		if (RoleService.subRoleResource(ActionType.action83.getType(), roleInfo, conds ,null)) {
			
			String updateSourceStr = RoleService.returnResourceChange(conds);
			if(updateSourceStr != null)
			{
				String[] sourceStr = updateSourceStr.split(",");
				if(sourceStr != null && sourceStr.length > 1)
				{
					resp.setSourceType(Byte.valueOf(sourceStr[0]));
					resp.setSourceChange(-Integer.valueOf(sourceStr[1]));
				}
			}
			
			//SceneService.sendRoleRefreshMsg(roleInfo.getId(), SceneService.REFESH_TYPE_ROLE, "");
		} else {
			return ErrorCode.DELETE_EQUIP_STONE_ERROR_3;
		}

		// 更新装备宝石信息
		stoneMap.remove(seat);
		String stoneStr = equipInfo.getStoneStr(stoneMap);
		if (EquipDAO.getInstance().updateEquipStones(equipInfo.getId(), stoneStr)) {
			EquipService.refeshEquipProperty(equipInfo);
			if (heroInfo != null) {
				HeroService.refeshHeroProperty(roleInfo, heroInfo);
			}
		} else {
			return ErrorCode.DELETE_EQUIP_STONE_ERROR_4;
		}

		// 返还老宝石
		List<DropInfo> dropItems = new ArrayList<DropInfo>(1);
		dropItems.add(new DropInfo(exisitStoneNo + "", 1));
		int re = ItemService.itemAdd(ActionType.action83.getType(), roleInfo, dropItems, null, null, null, null , true);
		if (re != 1) {
			return re;
		}
		GameLogService.insertPlayActionLog(roleInfo, ActionType.action83.getType(), "");
		return 1;
	}
}
