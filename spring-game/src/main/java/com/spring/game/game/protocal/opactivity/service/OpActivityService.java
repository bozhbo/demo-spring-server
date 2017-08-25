package com.snail.webgame.game.protocal.opactivity.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.game.cache.OpActivityProgressMap;
import com.snail.webgame.game.cache.RoleInfoMap;
import com.snail.webgame.game.cache.ToolOpActMap;
import com.snail.webgame.game.common.ActionType;
import com.snail.webgame.game.common.ErrorCode;
import com.snail.webgame.game.common.GameValue;
import com.snail.webgame.game.common.fightdata.DropInfo;
import com.snail.webgame.game.common.util.RandomUtil;
import com.snail.webgame.game.condtion.AbstractConditionCheck;
import com.snail.webgame.game.core.SceneService;
import com.snail.webgame.game.dao.OpActivityProgressDAO;
import com.snail.webgame.game.info.OpActivityProgressInfo;
import com.snail.webgame.game.info.RoleInfo;
import com.snail.webgame.game.info.ToolOpActivityInfo;
import com.snail.webgame.game.info.ToolOpActivityRewardInfo;
import com.snail.webgame.game.protocal.item.service.ItemService;
import com.snail.webgame.game.protocal.recruit.recruit.ChestItemRe;
import com.snail.webgame.game.xml.cache.RecruitDepotXMLInfoMap;
import com.snail.webgame.game.xml.cache.RecruitKindXMLInfoMap;
import com.snail.webgame.game.xml.info.RecruitItemXMLInfo;
import com.snail.webgame.game.xml.info.RecruitKindXMLInfo;

public class OpActivityService {
	private static final Logger logger = LoggerFactory.getLogger("logs");
	
	private static OpActivityProgressDAO opActProDAO = OpActivityProgressDAO.getInstance();
	
	/**
	 * 检测玩家行为对用户的运营时限活动影响
	 * 
	 * @param roleInfo
	 * @param roleAction 玩家行为 （默认为-0）
	 * @param obj	（默认填null）
	 * @param isRefresh 推送客户端
	 */
	public static void dealOpActProInfoCheck(RoleInfo roleInfo, int roleAction, Object obj, boolean isRefresh) {
		checkOpActProInfo(roleInfo, isRefresh, roleAction, obj);
	}
	
	/**
	 * 运营时限活动的检测
	 * 
	 * @param roleInfo
	 * @param isRefresh
	 * @param roleAction
	 * @param obj
	 */
	private static void checkOpActProInfo(RoleInfo roleInfo, boolean isRefresh, int roleAction, Object obj) {
		OpActivityProgressMap opActProMap = roleInfo.getOpActProMap();
		
		// 状态值发生变化的活动
		List<OpActivityProgressInfo> finishOpAct = new ArrayList<OpActivityProgressInfo>();
		List<OpActivityProgressInfo> addNewOpAct = new ArrayList<OpActivityProgressInfo>();
		List<OpActivityProgressInfo> delOpAct = new ArrayList<OpActivityProgressInfo>();
		
		StringBuilder idSb = new StringBuilder();
				
		// 检测是否有新的活动
		long now = System.currentTimeMillis();
		for (ToolOpActivityInfo toolOpActInfo : ToolOpActMap.getOpActMap().values()) {
			if (toolOpActInfo.getActType() == ToolOpActivityInfo.OP_ACT_TYPE_1
					|| toolOpActInfo.getActType() == ToolOpActivityInfo.OP_ACT_TYPE_2) {
				// 显示类活动
				continue;
			}
			
			if (toolOpActInfo.getStartTime().getTime() > now) {
				// 活动未开始
				continue;
			}
			
			if (now >= toolOpActInfo.getEndTime().getTime()) {
				continue;
			}
			
			List<OpActivityProgressInfo> list = opActProMap.fetchOpActProInfoByActId(toolOpActInfo.getId());
			if (list == null) {
				// 新的领取类活动添加到玩家身上
				Map<Integer, ToolOpActivityRewardInfo> rewardMap = ToolOpActMap.fetchRewardsByActId(toolOpActInfo.getId());
				if (rewardMap == null || rewardMap.isEmpty()) {
					continue;
				}
				
				for (ToolOpActivityRewardInfo toolRewardInfo : rewardMap.values()) {
					addNewOpAct.add(buildOpActPro(roleInfo, toolOpActInfo, toolRewardInfo));
				}
				
				if (idSb.indexOf("" + toolOpActInfo.getId()) == -1) {
					idSb.append(toolOpActInfo.getId()).append(",");
				}
					
			} else {
				// 检测活动版本是否过期
				if (toolOpActInfo.getActVersion() != list.get(0).getActVersion()) {
					// 活动版本号变化处理
					dealRoleOpActVersionChg(roleInfo, toolOpActInfo, addNewOpAct, delOpAct);
						
					if (idSb.indexOf("" + toolOpActInfo.getId()) == -1) {
						idSb.append(toolOpActInfo.getId()).append(",");
					}
				}
			}
			
			if (toolOpActInfo.getActType() == ToolOpActivityInfo.OP_ACT_TYPE_3) {
				// 限时武将不用检测
				continue;
			}
			
			// 检测状态值是否有变化
			Map<Integer, ToolOpActivityRewardInfo> rewardMap = ToolOpActMap.fetchRewardsByActId(toolOpActInfo.getId());
			if (rewardMap == null || rewardMap.isEmpty()) {
				continue;
			}
			
			for (ToolOpActivityRewardInfo toolRewardInfo : rewardMap.values()) {
				OpActivityProgressInfo proInfo = opActProMap.fetchOpActProInfo(toolRewardInfo.getOpActId(), toolRewardInfo.getRewardNo());
				if (proInfo == null) {
					logger.error("#### checkOpActProInfo error ,OpActivityProgressInfo is null, account = " + roleInfo.getAccount() + 
							", roleName = " + roleInfo.getRoleName() + ", OpActId = " + toolRewardInfo.getOpActId() + 
							", RewardNo = " + toolRewardInfo.getRewardNo() + ", toolOpActVersion = " + toolOpActInfo.getActVersion());
					
					// 
					continue;
				}
				if (proInfo.getRewardState() == OpActivityProgressInfo.STATUS_REVEIVE) {
					
					// 检测完成
					AbstractConditionCheck.check(toolRewardInfo.getRewardConds(), roleInfo, proInfo, roleAction, obj);
					// 更新任务中间值
					if (proInfo.isVersionChg()) {
						// 活动完成条件值改变，或活动已经完成
						if (!addNewOpAct.contains(proInfo)) {
							finishOpAct.add(proInfo);
							
							if (idSb.indexOf("" + proInfo.getActId()) == -1) {
								idSb.append(proInfo.getActId()).append(",");
							}
						}
						
						proInfo.setVersionChg(false);
					}
				}
			}
		}
		
		// 新增活动
		if (addNewOpAct.size() > 0 || finishOpAct.size() > 0 || delOpAct.size() > 0) {
			opActProDAO.dealOpActProInfo(addNewOpAct, finishOpAct, delOpAct);
		}
		
		if (isRefresh && idSb.length() > 0) {
			SceneService.sendRoleRefreshMsg(roleInfo.getId(), SceneService.REFRESH_TYPE_OPACT, idSb.deleteCharAt(idSb.length() - 1).toString());
		}
	}
	
	/**
	 * 构建玩家活动对象
	 * 
	 * @param roleInfo
	 * @param toolOpActInfo
	 * @param toolRewardInfo
	 * @return
	 */
	public static OpActivityProgressInfo buildOpActPro(RoleInfo roleInfo, ToolOpActivityInfo toolOpActInfo, ToolOpActivityRewardInfo toolRewardInfo) {
		OpActivityProgressInfo proInfo = new OpActivityProgressInfo();
		
		proInfo.setActId(toolOpActInfo.getId());
		proInfo.setRewardNo(toolRewardInfo.getRewardNo());
		proInfo.setCheckTime(new Timestamp(System.currentTimeMillis()));
		proInfo.setRoleId(roleInfo.getId());
		proInfo.setActVersion(toolOpActInfo.getActVersion());
		
		if (toolRewardInfo.isLoginCond()) {
			proInfo.setValue3(1);
		}
		
		roleInfo.getOpActProMap().addOpActivityProgressInfo(proInfo);
		
		return proInfo;
	}
	
	/**
	 * 检测运营时限活动的开启和关闭
	 */
	public static void checkOpActStartAndEnd() {
		StringBuilder idStr = new StringBuilder();
		long now = System.currentTimeMillis();
		for (ToolOpActivityInfo toolOpActInfo : ToolOpActMap.getOpActMap().values()) {
			if (now >= toolOpActInfo.getStartTime().getTime() && now - toolOpActInfo.getStartTime().getTime() <= 60000) {
				// 活动开始1分钟内推送活动开始
				if (idStr.indexOf("" + toolOpActInfo.getId()) == -1) {
					idStr.append(toolOpActInfo.getId()).append(",");
				}
				
				continue;
			}
			
			if (now >= toolOpActInfo.getEndTime().getTime()) {
				// 活动过期
				ToolOpActMap.opActIsOutTime(toolOpActInfo);
				
				// 推送活动过期
				if (idStr.indexOf("" + toolOpActInfo.getId()) == -1) {
					idStr.append(toolOpActInfo.getId()).append(",");
				}
				
				continue;
			}
			
		}
		
		if (idStr.length() > 0) {
			refreshOpAct(idStr.deleteCharAt(idStr.length() - 1).toString());
		}
	}
	
	/**
	 * 推送活动变化
	 * 
	 * @param idStr
	 */
	public static void refreshOpAct(String idStr) {
		// 活动变化
		int index = 0;
		RoleInfo roleInfo = null;
		for (Entry<Integer, RoleInfo> entry : RoleInfoMap.getRoleInfoEntrySet()) {
			roleInfo = entry.getValue();
			if (roleInfo == null || roleInfo.getLoginStatus() != 1) {
				continue;
			}
			
			SceneService.sendRoleRefreshMsg(roleInfo.getId(), SceneService.REFRESH_TYPE_OPACT, idStr);
			
			if (index++ > 500) {
				index = 0;
				try {
					TimeUnit.MILLISECONDS.sleep(100);
				} catch (InterruptedException e) {
					logger.error("refreshOpAct Error", e);
				}
			}
		}
	}
	
	/**
	 * 活动版本变化处理
	 * 
	 * @param toolOpActInfo 版本变化对应的活动对象
	 * @param isVersionChg 活动版本是否改变
	 * @param isRefresh  是否客户端推送
	 */
	public static void refreshOpActVersionChg(ToolOpActivityInfo toolOpActInfo, boolean isVersionChg, boolean isRefresh) {
		List<OpActivityProgressInfo> addOpActs = new ArrayList<OpActivityProgressInfo>(); 
		List<OpActivityProgressInfo> delOpActs = new ArrayList<OpActivityProgressInfo>(); 
		
		// 活动变化
		int index = 0;
		RoleInfo roleInfo = null;
		for (Entry<Integer, RoleInfo> entry : RoleInfoMap.getRoleInfoEntrySet()) {
			roleInfo = entry.getValue();
			if (roleInfo == null || roleInfo.getLoginStatus() != 1) {
				continue;
			}
			
			if (isVersionChg) {
				synchronized (roleInfo) {
					dealRoleOpActVersionChg(roleInfo, toolOpActInfo, addOpActs, delOpActs);
				}
			}
			
			if (isRefresh) {
				SceneService.sendRoleRefreshMsg(roleInfo.getId(), SceneService.REFRESH_TYPE_OPACT, toolOpActInfo.getId() + "");
			}
			
			if (index++ > 500) {
				index = 0;
				try {
					TimeUnit.MILLISECONDS.sleep(100);
				} catch (InterruptedException e) {
					logger.error("refreshOpAct Error", e);
				}
			}
		}
		
		if (addOpActs.size() > 0 || delOpActs.size() > 0) {
			OpActivityProgressDAO.getInstance().dealOpActProInfo(addOpActs, null, delOpActs);
		}
	}
	
	/**
	 * 处理活动版本号变化对角色数据的影响
	 * 
	 * @param roleInfo
	 */
	public static void dealRoleOpActVersionChg(RoleInfo roleInfo, ToolOpActivityInfo toolOpActInfo, List<OpActivityProgressInfo> addOpActs, 
			List<OpActivityProgressInfo> delOpActs) {
		OpActivityProgressMap opActProMap = roleInfo.getOpActProMap();
		// 记录数据库删除数据
		if (opActProMap.fetchOpActProInfoByActId(toolOpActInfo.getId()) != null) {
			delOpActs.addAll(opActProMap.fetchOpActProInfoByActId(toolOpActInfo.getId()));
		}
		
		// 清除旧缓存数据
		opActProMap.clearOpActByActId(toolOpActInfo.getId());
		
		// 新的活动添加到玩家身上
		Map<Integer, ToolOpActivityRewardInfo> rewardMap = ToolOpActMap.fetchRewardsByActId(toolOpActInfo.getId());
		if (rewardMap != null && !rewardMap.isEmpty()) {
			for (ToolOpActivityRewardInfo toolRewardInfo : rewardMap.values()) {
				addOpActs.add(OpActivityService.buildOpActPro(roleInfo, toolOpActInfo, toolRewardInfo));
			}
		}
	}

	/**
	 * 限时武将抽奖
	 * 
	 * @param roleInfo
	 * @param toolOpActivityInfo
	 * @param chestItemList
	 * @return
	 */
	public static int lottOpActHero(RoleInfo roleInfo, ToolOpActivityInfo toolOpActivityInfo, List<ChestItemRe> chestItemList, boolean isMust) {

		RecruitKindXMLInfo kind = RecruitKindXMLInfoMap.getRecruitKindXMLInfo(6);
		if (kind == null) {
			return ErrorCode.ROLE_NOT_EXIST_ERROR_9;
		}
		
		// 记录抽到的奖励
		List<DropInfo> dropInfos = new ArrayList<DropInfo>();
		
		// 记录已抽取过的道具编号
		List<String> lottItemNos = new ArrayList<String>();
		
		List<RecruitItemXMLInfo> items = RecruitDepotXMLInfoMap.getItems(kind.getDepotNoStr());
		if (items != null) {
			int sumRand = 0;
			for (RecruitItemXMLInfo item : items) {
				item.setMinRand(sumRand);
				item.setMaxRand(sumRand + item.getRand());
				sumRand = item.getMaxRand();
			}
			
			String itemNo = null;
			for (int i = 0; i < 50; i++) {
				int rand = RandomUtil.getRandom(0, sumRand);
				
				for (RecruitItemXMLInfo item : items) {
					if (item.getMinRand() <= rand && rand < item.getMaxRand()) {
						itemNo = item.getItemNo();
						if (lottItemNos.contains(itemNo)) {
							break;
						}
						
						if (AbstractConditionCheck.isResourceType(itemNo) || itemNo.startsWith(GameValue.EQUIP_N0)
								|| itemNo.startsWith(GameValue.PROP_N0) || itemNo.startsWith(GameValue.HERO_N0)
								|| itemNo.startsWith(GameValue.WEAPAN_NO)) {
							DropInfo dropInfo = new DropInfo();
							dropInfo.setItemNo(itemNo);
							dropInfo.setItemNum(item.getNum());
							dropInfo.setItemType(ItemService.checkItemType(dropInfo.getItemNo()));
							
							dropInfos.add(dropInfo);
							lottItemNos.add(itemNo);
							
							break;
						}
					}
				}
				
				if (dropInfos.size() >= kind.getPoolNum()) {
					break;
				}
				
			}
		}
		
		// 运营控制一个掉落
		if (!isMust) {
			List<RecruitItemXMLInfo> rewards = toolOpActivityInfo.getList();
			if (rewards != null) {
				int sumRand = 0;
				for (RecruitItemXMLInfo item : rewards) {
					item.setMinRand(sumRand);
					item.setMaxRand(sumRand + item.getRand());
					sumRand = item.getMaxRand();
				}
				
				int rand = RandomUtil.getRandom(0, sumRand);
				for (RecruitItemXMLInfo item : rewards) {
					if (item.getMinRand() <= rand && rand < item.getMaxRand()) {
						String itemNo = item.getItemNo();
						if (AbstractConditionCheck.isResourceType(itemNo) || itemNo.startsWith(GameValue.EQUIP_N0)
								|| itemNo.startsWith(GameValue.PROP_N0) || itemNo.startsWith(GameValue.HERO_N0)
								|| itemNo.startsWith(GameValue.WEAPAN_NO)) {
							DropInfo dropInfo = new DropInfo();
							dropInfo.setItemNo(itemNo);
							dropInfo.setItemNum(item.getNum());
							dropInfo.setItemType(ItemService.checkItemType(dropInfo.getItemNo()));
							
							dropInfos.add(dropInfo);
							
							break;
						}
					}
				}
			}
		} else {
			DropInfo dropInfo = new DropInfo();
			dropInfo.setItemNo(toolOpActivityInfo.getActHeroNo() + "");
			dropInfo.setItemNum(1);
			dropInfo.setItemType(ItemService.checkItemType(dropInfo.getItemNo()));
			
			dropInfos.add(dropInfo);
		}
		
		// 随机打乱list顺序
		Collections.shuffle(dropInfos);

		// 保存物品
		List<Integer> heroIds = new ArrayList<Integer>();
		int rt = ItemService.addPrize(ActionType.action410.getType(), roleInfo, dropInfos, null,
				null, null, null, null, heroIds, null, null, chestItemList, false);
		if (rt != 1) {
			return rt;
		}
		
		if (heroIds != null && heroIds.size() > 0) {
			String heroIdStr = "";
			for (long heroId : heroIds) {
				heroIdStr += heroId + ",";
			}
			// 推送英雄信息
			SceneService.sendRoleRefreshMsg(roleInfo.getId(), SceneService.REFESH_TYPE_HERO, heroIdStr);
		}

		return 1;
	}
}
