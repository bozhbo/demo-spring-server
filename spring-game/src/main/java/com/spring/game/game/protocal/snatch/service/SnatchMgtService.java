package com.snail.webgame.game.protocal.snatch.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.epilot.ccf.config.Resource;

import com.snail.webgame.game.cache.BagItemMap;
import com.snail.webgame.game.cache.HeroInfoMap;
import com.snail.webgame.game.cache.RoleInfoMap;
import com.snail.webgame.game.common.ActionType;
import com.snail.webgame.game.common.ETimeMessageType;
import com.snail.webgame.game.common.ErrorCode;
import com.snail.webgame.game.common.GameValue;
import com.snail.webgame.game.common.MailMessage;
import com.snail.webgame.game.common.fightdata.DropInfo;
import com.snail.webgame.game.common.xml.cache.VipXMLInfoMap;
import com.snail.webgame.game.common.xml.info.VipType;
import com.snail.webgame.game.condtion.ConditionType;
import com.snail.webgame.game.core.GameLogService;
import com.snail.webgame.game.core.SceneService;
import com.snail.webgame.game.dao.ItemDAO;
import com.snail.webgame.game.dao.RoleDAO;
import com.snail.webgame.game.info.BagItemInfo;
import com.snail.webgame.game.info.RoleInfo;
import com.snail.webgame.game.info.RoleLoadInfo;
import com.snail.webgame.game.protocal.fight.fightend.BattlePrize;
import com.snail.webgame.game.protocal.item.service.ItemService;
import com.snail.webgame.game.protocal.quest.service.QuestService;
import com.snail.webgame.game.protocal.redPoint.service.RedPointMgtService;
import com.snail.webgame.game.protocal.rolemgt.service.RoleService;
import com.snail.webgame.game.protocal.snatch.getRivalList.GetRivalListReq;
import com.snail.webgame.game.protocal.snatch.getRivalList.GetRivalListResp;
import com.snail.webgame.game.protocal.snatch.getRivalList.RivalListRe;
import com.snail.webgame.game.protocal.snatch.loot.LootPrizeRe;
import com.snail.webgame.game.protocal.snatch.loot.LootReq;
import com.snail.webgame.game.protocal.snatch.loot.LootResp;
import com.snail.webgame.game.protocal.snatch.mix.SnatchMixReq;
import com.snail.webgame.game.protocal.snatch.mix.SnatchMixResp;
import com.snail.webgame.game.protocal.snatch.query.QuerySnatchInfoResp;
import com.snail.webgame.game.protocal.snatch.safeMode.SafeModeReq;
import com.snail.webgame.game.protocal.snatch.safeMode.SafeModeResp;
import com.snail.webgame.game.thread.SendServerMsgThread;
import com.snail.webgame.game.xml.cache.PropBagXMLMap;
import com.snail.webgame.game.xml.cache.PropXMLInfoMap;
import com.snail.webgame.game.xml.cache.SnatchMap;
import com.snail.webgame.game.xml.info.DropXMLInfo;
import com.snail.webgame.game.xml.info.PropXMLInfo;
import com.snail.webgame.game.xml.info.SnatchInfo;

public class SnatchMgtService {
	/**
	 * 操作安全模式
	 * @param roleId
	 * @param safeModeReq
	 * @return
	 */
	public SafeModeResp operatingSafeMode(int roleId, SafeModeReq safeModeReq) {
		SafeModeResp resp = new SafeModeResp();
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			resp.setResult(ErrorCode.SNATCH_SAFEMODE_6);
			return resp;
		}
		synchronized (roleInfo) {
			switch (safeModeReq.getOperatingMode()) {
			case 1:
				// 开启安全模式
				resp.setResult(SnatchService.openSafeMode(roleInfo));
				break;
			case 2:
				// 关闭安全模式
				resp.setResult(SnatchService.closeSafeMode(roleInfo));
				break;
			}
			long safeModeLeftTime = roleInfo.getSafeModeEndTime() - System.currentTimeMillis();
			if (safeModeLeftTime > 0) {
				resp.setSafeModeLeftTimeMillie(safeModeLeftTime);
			} else {
				resp.setSafeModeLeftTimeMillie(0);
			}
			resp.setRemainGold(roleInfo.getCoin());
		}
		return resp;
	}

	/**
	 * 获取挑战列表
	 * @param roleId
	 * @return
	 */
	public GetRivalListResp getRivalList(int roleId, GetRivalListReq req) {
		GetRivalListResp resp = new GetRivalListResp();
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			resp.setResult(ErrorCode.SNATCH_RIVAL_LIST_1);
			return resp;
		}
		synchronized (roleInfo) {
			// 不是该神兵类型的第一个就必须有该类碎片的其中一种才能抢夺
			SnatchInfo snatchInfo = SnatchMap.get(req.getStoneNo());
			if (snatchInfo == null) {
				resp.setResult(ErrorCode.SNATCH_LOOT_2);
				return resp;
			}
			int check = SnatchService.checkCanlootStone(roleInfo, snatchInfo);
			if (check != 1) {
				resp.setResult(check);
				return resp;
			}

			// 获取玩家挑战的人员列表(玩家3个)
			List<RivalListRe> rivalList = SnatchService.getRoleRivalList(roleInfo, req.getStoneNo(), 3);
			// 固定数量为5个 机器人2个，如果玩家数不足3个，则生成机器人补足
			rivalList.addAll(SnatchService.getRivalListbyRandom(roleInfo, snatchInfo, 5 - rivalList.size()));
			Collections.shuffle(rivalList);

			resp.setRivalList(rivalList);
			resp.setRivalListSize(resp.getRivalList().size());
		}
		resp.setResult(1);
		return resp;
	}

	/**
	 * 抢夺石头
	 * @param roleId
	 * @param req
	 * @return
	 */
	public LootResp lootStone(int roleId, LootReq req) {
		LootResp resp = new LootResp();
		resp.setResult(1);
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			resp.setResult(ErrorCode.SNATCH_LOOT_3);
			return resp;
		}
		int lootTimes = req.getLootTimes();// 抢夺次数
		if (lootTimes <= 0 || lootTimes > 5) {
			resp.setResult(ErrorCode.SNATCH_LOOT_2);
			return resp;
		}
		if (lootTimes > 1 && VipXMLInfoMap.getVipVal(roleInfo.getVipLv(), VipType.WLDB) < 1) {
			resp.setResult(ErrorCode.CHALL_SWAPP_ERROR_2);
			return resp;
		}

		int stoneNo = req.getStoneNo();// 石头编号
		SnatchInfo snatchInfo = SnatchMap.get(stoneNo);
		if (snatchInfo == null) {
			resp.setResult(ErrorCode.SNATCH_LOOT_2);
			return resp;
		}
		PropXMLInfo propXml = PropXMLInfoMap.getPropXMLInfo(stoneNo);
		if(propXml == null)
		{
			resp.setResult(ErrorCode.SNATCH_LOOT_12);
			return resp;
		}
		
		RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
		if (roleLoadInfo == null) {
			resp.setResult(ErrorCode.SNATCH_LOOT_8);
			return resp;
		}
		synchronized (roleInfo) 
		{
			// 检测是否可以抢夺宝石
			int check = SnatchService.checkCanlootStone(roleInfo, snatchInfo);
			if (check != 1) {
				resp.setResult(check);
				return resp;
			}
			// 验证自己是否有石头
			if (BagItemMap.checkBagItemNum(roleInfo, stoneNo, 1)) {
				resp.setResult(ErrorCode.SNATCH_LOOT_1);
				return resp;
			}
			// 判断背包
			int itemCheck = ItemService.addItemAndEquipCheck(roleInfo);
			if (itemCheck != 1) {
				resp.setResult(itemCheck);
				return resp;
			}
			
			//精力判断
			RoleService.timerRecoverEnergy(roleInfo);
			if(roleInfo.getEnergy() < GameValue.SNATCH_CHALLENGE_ENERGY)
			{
				resp.setResult(ErrorCode.ROLE_ENERGY_ERROR_1);
				return resp;
			}
		}
		
		byte isNPC = req.getIsNPC();// 0-不是 1-是
		RoleInfo roleInfoTemp = null;
		if (isNPC == 0) {
			roleInfoTemp = RoleInfoMap.getRoleInfoByName(req.getLootRoleName());
			if (roleInfoTemp == null) {
				resp.setResult(ErrorCode.SNATCH_LOOT_7);
				return resp;
			}
		}
		boolean loopResult = false;
		boolean fightResult = false;
		int costEnergy = 0;
		int snatchTimes = roleLoadInfo.getSnatchTimes(snatchInfo.getPatchNo());
		
		List<DropInfo> addList = new ArrayList<DropInfo>();
		
		for (int i = 0; i < lootTimes; i++) {
			costEnergy += GameValue.SNATCH_CHALLENGE_ENERGY;
			// 验证精力值是否有
			if (roleInfo.getEnergy() < costEnergy) {
				break;
			}
			
			synchronized (roleInfo) 
			{
				// 是否夺宝过 0-未夺宝 1-已夺宝
				if (roleLoadInfo.getSnatchFlag() == 0) {
					if (RoleDAO.getInstance().updateSnatchFlag(roleInfo.getId(), (byte) 1)) {
						roleLoadInfo.setSnatchFlag((byte) 1);
					} else {
						resp.setResult(ErrorCode.SNATCH_LOOT_9);
						return resp;
					}
					// 第一次夺宝必成功
					if (!fightResult) {
						fightResult = true;
					}
					if (!loopResult && fightResult) {
						loopResult = true;
					}
				}
			}
			
			if (snatchTimes >= snatchInfo.getLimit()) {
				// 抢夺最大次数,跟碎片绑定抢夺成功后清零，达到此次数，必定成功。
				if (!fightResult) {
					fightResult = true;
				}
				if (!loopResult && fightResult) {
					loopResult = true;
				}
			}
			if (roleInfoTemp == null) {
				// 机器人
				if (!fightResult) {
					fightResult = true;
				}
				if (!loopResult && fightResult) {
					loopResult = SnatchService.calculateResult(snatchInfo.getChance());
				}

			} else {
				synchronized (roleInfoTemp) {
					if (!fightResult) {
						fightResult = SnatchService.calculateFightResult(roleInfo.getFightValue(),
								roleInfoTemp.getFightValue());
					}
					if (!loopResult && fightResult) {
						loopResult = SnatchService.calculateSnatchResult(roleInfo.getFightValue(),
								roleInfoTemp.getFightValue());
					}
					if (loopResult) {
						// 扣除对方宝石
						loopResult = delRoleInfoTempPatchNo(roleInfoTemp, stoneNo,roleInfo.getRoleName());
					}
					
					
				}
			}
			
			synchronized (roleInfo) 
			{
				// 领取奖励
				getLootPrizeRe(resp, roleInfo, snatchInfo, loopResult, fightResult,addList);
				
				if (resp.getResult() != 1) {
					return resp;
				}
			}
			
			if (loopResult) {
				snatchTimes = 0;
				// 成功一次跳出
				break;
			} else {
				snatchTimes++;
			}
		}
				
		synchronized (roleInfo) 
		{
			int energy = roleInfo.getEnergy() - costEnergy;
			if(energy < 0)
			{
				resp.setResult(ErrorCode.ROLE_ENERGY_ERROR_1);
				return resp;
			}
			Timestamp lastRecoverTime = roleInfo.getLastRecoverEnergyTime();		
			if (snatchTimes != roleLoadInfo.getSnatchTimes(stoneNo)) {
				Map<Integer, Integer> snatchTimeMap = new HashMap<Integer, Integer>();
				snatchTimeMap.putAll(roleLoadInfo.getSnatchTimes());
				snatchTimeMap.put(stoneNo, snatchTimes);
				if (RoleDAO.getInstance().updateSnatchTimes(roleId, snatchTimeMap,energy,lastRecoverTime)) {
					roleLoadInfo.setSnatchTimes(snatchTimeMap);
					
					
					roleInfo.setEnergy((short) energy);
					roleInfo.setLastRecoverEnergyTime(lastRecoverTime);
					// 时间重置后需要推送最新的体力和时间
					String energyStr = roleInfo.getEnergy() + "," + roleInfo.getLastRecoverEnergyTime().getTime() + "," + roleLoadInfo.getTodayBuyEnergyNum() + "," + 0;					
					SceneService.sendRoleRefreshMsg(roleInfo.getId(), SceneService.REFRESH_TYPE_ENERGY, energyStr);
				}
			}else{
				if (costEnergy > 0) {
					// 扣除精力值
					RoleService.subRoleRoleResource(ActionType.action362.getType(), roleInfo, ConditionType.TYPE_ENERGY,
							costEnergy , null);
				}
			}
			
			ItemService.prizeToRole(ActionType.action362.getType(),roleInfo,addList, null, null, null, null, 
					null, null, true);
			
			// 任务检测
			QuestService.checkQuest(roleInfo, ActionType.action362.getType(), costEnergy / GameValue.SNATCH_CHALLENGE_ENERGY, true, true);

			// 红点监听
			RedPointMgtService.check2PopRedPoint(roleInfo.getId(), null, true, GameValue.RED_POINT_STONE_COMPOSE);
		}
		

		// 抢夺石头
		resp.setPrizeListSize(resp.getPrizeList().size());

		resp.setResult(1);
		resp.setLootTimes(lootTimes);
		resp.setLootSuccess(loopResult ? (byte) 1 : (byte) 2);
		
		// 日志
		StringBuilder getItem = new StringBuilder();
		for (LootPrizeRe re : resp.getPrizeList()) {
			if (getItem.length() > 0) {
				getItem.append(",");
			}
			getItem.append(GameLogService.getItem(re.getPrize(), re.getCardPrize()));
		}

		GameLogService.insertSnatchLog(roleInfo, roleInfoTemp == null ? 0 : roleInfoTemp.getId(),
				req.getLootRoleName(), stoneNo, resp.getLootSuccess(), lootTimes, getItem.toString(), costEnergy);
		return resp;
	}

	/**
	 * 删除玩家碎片
	 * @param roleInfo
	 * @param itemNo
	 * @return
	 */
	private boolean delRoleInfoTempPatchNo(RoleInfo roleInfo, int patchNo,String snatchRoleName) {
		BagItemInfo itemInfo = null;
		if (roleInfo.getLoginStatus() == 1) {
			itemInfo = BagItemMap.getBagItembyNo(roleInfo, patchNo);
			if (itemInfo != null && itemInfo.getNum() > 0) {
				// 对方只有一个碎片不扣
				if(itemInfo.getNum() > 1){					
					HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
					map.put(patchNo, 1);
					if(ItemService.bagItemDel(ActionType.action362.getType(), roleInfo, map) == 1){
						// 提示对方有人抢夺你的东西了
						noticeBySnatch(roleInfo, patchNo, snatchRoleName);
						return true;
					} else {
						return false;
					}
				}
				return true;
			}
		} else {
			itemInfo = RoleDAO.getInstance().getBagItemInfo(patchNo, roleInfo.getId());
			if (itemInfo != null && itemInfo.getNum() > 0) {
				// 对方只有一个碎片不扣
				if(itemInfo.getNum() > 1){
					HashMap<Integer, Integer> updateItemMap = new HashMap<Integer, Integer>();
					updateItemMap.put(itemInfo.getId(), (itemInfo.getNum() - 1));
					if(ItemDAO.getInstance().updateBagItem(updateItemMap)){	
						// 提示对方有人抢夺你的东西了
						noticeBySnatch(roleInfo, patchNo, snatchRoleName);
						return true;
					} else {
						return false;
					}
				}
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 提示对方有人抢夺你的东西了
	 * @param roleInfoTemp
	 * @param patchNo
	 * @param snatchRoleName
	 */
	private void noticeBySnatch(RoleInfo roleInfo, int patchNo,String snatchRoleName){
		// 提示对方有人抢夺你的东西了
		List<Integer> mailRoleIdList = new ArrayList<Integer>();
		mailRoleIdList.add(roleInfo.getId());
		String title = Resource.getMessage("game", "GAME_SNATCH_TITLE");
		String content = Resource.getMessage("game", "GAME_SNATCH_CONTENT");
		String reserve = patchNo +","+snatchRoleName;
		SendServerMsgThread.addMessage(new MailMessage(ETimeMessageType.SEND_BATCH_SYS_MAIL, mailRoleIdList, null, content, title, reserve));
	}
	
	/**
	 * 领取奖励
	 * @param roleInfo
	 * @param snatchInfo
	 * @param loopResult
	 * @return
	 */
	private void getLootPrizeRe(LootResp resp, RoleInfo roleInfo, SnatchInfo snatchInfo, boolean loopResult,
			boolean fightResult,List<DropInfo> addList) {
		String cardBag = null;
		int action = ActionType.action362.getType();
		if (fightResult) {
			cardBag = snatchInfo.getCardBag();
			List<DropXMLInfo> prizeXmls = new ArrayList<DropXMLInfo>();
			prizeXmls.addAll(PropBagXMLMap.getPropBagXMLList(snatchInfo.getBag()));
			if (prizeXmls != null && prizeXmls.size() > 0) {
				Iterator<DropXMLInfo> a = prizeXmls.iterator();
				while (a.hasNext()) {
					String itemNo = a.next().getItemNo();
					if (itemNo.equalsIgnoreCase(ConditionType.TYPE_EXP.getName())
							|| itemNo.equalsIgnoreCase(snatchInfo.getPatchNo() + "")) {
						a.remove();
					}
				}
			}
			// 解析掉落
			ItemService.getDropXMLInfo(roleInfo, prizeXmls, addList);
		}		
		if (fightResult) {
			int dropExp = HeroInfoMap.getMainHeroLv(roleInfo.getId()) * GameValue.SNATCH_EXP_VALUE
					+ GameValue.SNATCH_EXP_ADD;
			addList.add(new DropInfo(ConditionType.TYPE_EXP.getName(), dropExp));
		}
		if (loopResult) {
			addList.add(new DropInfo(String.valueOf(snatchInfo.getPatchNo()), 1));
		}
		// 获得奖励,解析配置文件获得掉落方法
		List<BattlePrize> fpPrizeList = new ArrayList<BattlePrize>();
		List<BattlePrize> prizeList = new ArrayList<BattlePrize>();
		ItemService.getLootPrize1(action, roleInfo, addList, cardBag, prizeList,
				fpPrizeList);
		
		LootPrizeRe lootPrizeRe = new LootPrizeRe();
		lootPrizeRe.setFightSuccess(fightResult ? (byte) 1 : (byte) 2);
		lootPrizeRe.setPrizeSize(prizeList.size());
		lootPrizeRe.setPrize(prizeList);
		lootPrizeRe.setCardPrizeSize(fpPrizeList.size());
		lootPrizeRe.setCardPrize(fpPrizeList);
		resp.getPrizeList().add(lootPrizeRe);

	}

	/**
	 * 获取夺宝状态信息
	 * @param roleId
	 * @return
	 */
	public QuerySnatchInfoResp querySnatchInfo(int roleId) {
		QuerySnatchInfoResp resp = new QuerySnatchInfoResp();
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			resp.setResult(ErrorCode.SNATCH_QUERY_1);
			return resp;
		}
		synchronized (roleInfo) {
			long safeModeLeftTimeMillis = roleInfo.getSafeModeEndTime() - System.currentTimeMillis();
			if (safeModeLeftTimeMillis > 0) {
				resp.setSafeModeLeftTimeMillis(safeModeLeftTimeMillis);
			}
			// resp.setSnatchTotalTimes(GameValue.SNATCH_CHALLENGE_TIMES);
			// resp.setSnatchleftTimes((byte) (GameValue.SNATCH_CHALLENGE_TIMES
			// - SnatchService.snatchLeftTimes(roleInfo.getRoleLoadInfo())));
		}
		resp.setResult(1);
		return resp;
	}

	/**
	 * 夺宝石头合成
	 * @param roleId
	 * @param snatchMixReq
	 * @return
	 */
	public SnatchMixResp snatchMix(int roleId, SnatchMixReq req) {
		SnatchMixResp resp = new SnatchMixResp();
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			resp.setResult(ErrorCode.SNATCH_MIX_2);
			return resp;
		}
		synchronized (roleInfo) {
			int stoneNo = req.getStoneNo();
			byte mixType = req.getMixType();// 0-普通合成 1-一键合成
			List<SnatchInfo> snatchInfos = SnatchMap.getByPropNo(stoneNo);
			if (snatchInfos == null || snatchInfos.size() == 0) {
				resp.setResult(ErrorCode.SNATCH_MIX_1);
				return resp;
			}
			int getNum = 0;
			switch (mixType) {
			case 0:
				getNum = 1;
				break;
			case 1:
				BagItemInfo item = null;
				for (SnatchInfo snatchInfo : snatchInfos) {
					item = BagItemMap.getBagItembyNo(roleInfo, snatchInfo.getPatchNo());
					if (item == null) {
						resp.setResult(ErrorCode.SNATCH_MIX_3);
						return resp;
					}
					if (getNum == 0) {
						getNum = item.getNum();
					} else {
						getNum = Math.min(getNum, item.getNum());
					}
				}
				break;
			default:
				resp.setResult(ErrorCode.SNATCH_MIX_1);
				return resp;
			}
			HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
			for (SnatchInfo snatchInfo : snatchInfos) {
				map.put(snatchInfo.getPatchNo(), getNum);
			}
			int status = ItemService.bagItemDel(ActionType.action363.getType(), roleInfo, map);
			if (1 != status) {
				resp.setResult(status);
				return resp;
			}
			// 生成新石头
			List<DropInfo> itemList = new ArrayList<DropInfo>();// 道具
			itemList.add(new DropInfo(String.valueOf(snatchInfos.get(0).getPropNo()), getNum));
			status = ItemService.itemAdd(ActionType.action363.getType(), roleInfo, itemList, null, null, null, null,
					true);
			if (status != 1) {
				resp.setResult(status);
				return resp;
			}

			resp.setResult(1);
			resp.setMixNum(getNum);

			// 红点监听
			RedPointMgtService.check2PopRedPoint(roleInfo.getId(), null, true, GameValue.RED_POINT_STONE_COMPOSE);
		}
		return resp;
	}
}
