package com.snail.webgame.game.protocal.quest.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.snail.webgame.game.cache.BagItemMap;
import com.snail.webgame.game.cache.HeroInfoMap;
import com.snail.webgame.game.cache.QuestInfoMap;
import com.snail.webgame.game.cache.RoleInfoMap;
import com.snail.webgame.game.common.ActionType;
import com.snail.webgame.game.common.ErrorCode;
import com.snail.webgame.game.common.GameValue;
import com.snail.webgame.game.common.UserGuideNode;
import com.snail.webgame.game.common.util.DateUtil;
import com.snail.webgame.game.condtion.AbstractConditionCheck;
import com.snail.webgame.game.condtion.ConditionType;
import com.snail.webgame.game.core.GameLogService;
import com.snail.webgame.game.core.SceneService;
import com.snail.webgame.game.dao.QuestDAO;
import com.snail.webgame.game.dao.RoleDAO;
import com.snail.webgame.game.info.HeroInfo;
import com.snail.webgame.game.info.QuestInProgressInfo;
import com.snail.webgame.game.info.RoleInfo;
import com.snail.webgame.game.info.RoleLoadInfo;
import com.snail.webgame.game.protocal.fight.fightend.BattlePrize;
import com.snail.webgame.game.protocal.guide.service.GuideMgtService;
import com.snail.webgame.game.protocal.item.service.ItemService;
import com.snail.webgame.game.protocal.quest.finish.FinishQuestReq;
import com.snail.webgame.game.protocal.quest.finish.FinishQuestResp;
import com.snail.webgame.game.protocal.quest.onekey.OneKeyQuestReq;
import com.snail.webgame.game.protocal.quest.onekey.OneKeyQuestResp;
import com.snail.webgame.game.protocal.quest.query.QueryQuestReq;
import com.snail.webgame.game.protocal.quest.query.QueryQuestResp;
import com.snail.webgame.game.protocal.quest.query.QuestInfoRe;
import com.snail.webgame.game.protocal.quest.talk.NpcTalkOrderReq;
import com.snail.webgame.game.protocal.redPoint.service.RedPointMgtService;
import com.snail.webgame.game.protocal.rolemgt.service.RoleService;
import com.snail.webgame.game.xml.cache.PropBagXMLMap;
import com.snail.webgame.game.xml.cache.PropXMLInfoMap;
import com.snail.webgame.game.xml.cache.QuestProtoXmlInfoMap;
import com.snail.webgame.game.xml.info.DropXMLInfo;
import com.snail.webgame.game.xml.info.PropXMLInfo;
import com.snail.webgame.game.xml.info.QuestProtoXmlInfo;

public class QuestMgrService {
	
	private QuestDAO questDAO = new QuestDAO();

	/**
	 * 获取任务
	 * @param roleInfo
	 * @return
	 */
	public List<QuestInfoRe> getAllShowQuest(RoleInfo roleInfo) {
		List<QuestInfoRe> list = new ArrayList<QuestInfoRe>();
		List<QuestInProgressInfo> quests = roleInfo.getQuestInfoMap().getRoleQuest();
		if (quests != null) {
			for (QuestInProgressInfo info : quests) {
				int questProtoNo = info.getQuestProtoNo();
				QuestProtoXmlInfo questProtoXmlInfo = QuestProtoXmlInfoMap.questXml(questProtoNo);
				if (questProtoXmlInfo == null) {
					continue;
				}
				
				// 判断显示条件
				if (questProtoXmlInfo.isShowCheckCond()) {
					int ShowCondResult = AbstractConditionCheck.checkCondition(roleInfo,questProtoXmlInfo.getQuestConds());
					if (ShowCondResult != 1) {
						continue;
					}
				}

				if (questProtoXmlInfo.getQuestType() == QuestProtoXmlInfo.QUEST_TYPE_CARD || questProtoXmlInfo.getQuestType() == QuestProtoXmlInfo.QUEST_TYPE_ACTIVE
						|| info.getStatus() == QuestInProgressInfo.STATUS_REVEIVE || info.getStatus() == QuestInProgressInfo.STATUS_FINISH) {
					list.add(getQuestInfoRe(roleInfo, info, questProtoXmlInfo.getQuestType()));
				}
			}
		}

		return list;
	}

	public static QuestInfoRe getQuestInfoRe(RoleInfo roleInfo, QuestInProgressInfo info, int questType) {
		QuestInfoRe re = new QuestInfoRe();
		re.setQuestProtoNo(info.getQuestProtoNo());
		re.setStatus((byte) info.getStatus());
		re.setValue((int) info.getValue1());
		re.setNpcTalkOrder((byte) info.getTalkOrder());
		
		if (info.isEffect()) {
			re.setIsEffect((byte) 1);
			
			info.setEffect(false);
		}
		
		if (questType == QuestProtoXmlInfo.QUEST_TYPE_RUN) {
			re.setCurRunQuestSeq(roleInfo.getRoleLoadInfo().getTodayRunNum());
			re.setDailyMaxRunNum(GameValue.GAME_QUEST_RUN_MAX_NUM);
		}
		return re;
	}

	/**
	 * 查询任务
	 * @param roleId
	 * @param req
	 * @return
	 */
	public QueryQuestResp queryQuest(int roleId, QueryQuestReq req) {
		QueryQuestResp resp = new QueryQuestResp();
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			resp.setResult(ErrorCode.ROLE_NOT_EXIST_ERROR_15);
			return resp;
		}
		synchronized (roleInfo) {
			if (roleInfo.getRoleLoadInfo() == null || roleInfo.getQuestInfoMap() == null) {
				resp.setResult(ErrorCode.ROLE_NOT_EXIST_ERROR_9);
				return resp;
			}
			
			String noStr = req.getNoStr();
			List<QuestInfoRe> list = new ArrayList<QuestInfoRe>();
			if (noStr == null || noStr.length() <= 0) {
				list = getAllShowQuest(roleInfo);
			} else {
				StringBuilder removeNoStr = new StringBuilder();
				String noList[] = noStr.split(",");
				for (String no : noList) {
					int questProtoNo = Integer.parseInt(no);
					QuestInProgressInfo info = roleInfo.getQuestInfoMap().getQuestInProgressInfo(questProtoNo);
					if (info == null) {
						continue;
					}
					QuestProtoXmlInfo questProtoXmlInfo = QuestProtoXmlInfoMap.questXml(questProtoNo);
					if (questProtoXmlInfo == null) {
						continue;
					}
					// 判断显示条件
					if (questProtoXmlInfo.isShowCheckCond()) {
						int ShowCondResult = AbstractConditionCheck.checkCondition(roleInfo,questProtoXmlInfo.getQuestConds());
						if (ShowCondResult != 1) {
							removeNoStr.append(no).append(",");
							continue;
						}
					}

					if ((questProtoXmlInfo.getQuestType() == QuestProtoXmlInfo.QUEST_TYPE_CARD || questProtoXmlInfo.getQuestType() == QuestProtoXmlInfo.QUEST_TYPE_ACTIVE
							|| info.getStatus() == QuestInProgressInfo.STATUS_REVEIVE || info.getStatus() == QuestInProgressInfo.STATUS_FINISH)) {
						list.add(getQuestInfoRe(roleInfo, info, questProtoXmlInfo.getQuestType()));
					}
				}
				
				noStr = removeNoStr.toString();
			}

			resp.setResult(1);
			resp.setNoStr(noStr);
			resp.setCount(list.size());
			resp.setList(list);
			return resp;
		}
	}

	/**
	 * 完成任务
	 * @param roleId
	 * @param req
	 * @return
	 */
	public FinishQuestResp finishQuest(int roleId, FinishQuestReq req) {
		FinishQuestResp resp = new FinishQuestResp();

		int questProtoNo = req.getQuestProtoNo();
		if (questProtoNo <= 0) {
			resp.setResult(ErrorCode.QUEST_ERROR_1);
			return resp;
		}

		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			resp.setResult(ErrorCode.QUEST_ERROR_2);
			return resp;
		}
		synchronized (roleInfo) {
			RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
			if (roleLoadInfo == null) {
				resp.setResult(ErrorCode.ROLE_NOT_EXIST_ERROR_9);
				return resp;
			}
			HeroInfo heroInfo = HeroInfoMap.getMainHeroInfo(roleInfo);
			if (heroInfo == null) {
				resp.setResult(ErrorCode.QUEST_ERROR_2);
				return resp;
			}
			QuestInfoMap questInfoMap = roleInfo.getQuestInfoMap();
			if (questInfoMap == null) {
				resp.setResult(ErrorCode.SYSTEM_ERROR_CODE38);
				return resp;
			}
			
			// 任务xml判断
			QuestProtoXmlInfo xmlInfo = QuestProtoXmlInfoMap.questXml(questProtoNo);
			if (xmlInfo == null) {
				resp.setResult(ErrorCode.QUEST_ERROR_3);
				return resp;
			}

			QuestInProgressInfo info = questInfoMap.getQuestInProgressInfo(questProtoNo);
			if (info == null) {
				resp.setResult(ErrorCode.QUEST_ERROR_4);
				return resp;
			}

			// 任务状态判断
			if (info.getStatus() != QuestInProgressInfo.STATUS_FINISH) {
				resp.setResult(ErrorCode.QUEST_ERROR_5);
				return resp;
			}
			
			int result = ItemService.addItemAndEquipCheck(roleInfo);
			//背包格子上限判断
			if (result != 1) {
				resp.setResult(ErrorCode.QUEST_ERROR_23);
				return resp;
			}

			boolean isCache = false;
			if (xmlInfo.getQuestType() != QuestProtoXmlInfo.QUEST_TYPE_DAILY 
					&& xmlInfo.getQuestType() != QuestProtoXmlInfo.QUEST_TYPE_RACE
					&& xmlInfo.getQuestType() != QuestProtoXmlInfo.QUEST_TYPE_CARD
					&& xmlInfo.getQuestType() != QuestProtoXmlInfo.QUEST_TYPE_ACTIVE
					&& (xmlInfo.getQuestType() == QuestProtoXmlInfo.QUEST_TYPE_RUN || xmlInfo.isFinishTaskCond())) {
				questDAO.deleteQuest(info);
			} else {
				questDAO.updateQuestInProgressInfo(info.getId(), QuestInProgressInfo.STATUS_CLEAR);
				isCache = true;
			}
			
			// 修改任务状态
			questInfoMap.questClear(info, xmlInfo.getQuestType(), isCache);
			//给予任务奖励
			int oldLv = heroInfo.getHeroLevel();
			String res = "";
						
			int newLv = heroInfo.getHeroLevel();
			if(oldLv < newLv)
			{
				res = "lvUp";
			}
							
			resp.setResult(1);
			resp.setQuestProtoNo(questProtoNo);
			
			if (xmlInfo.getQuestType() == QuestProtoXmlInfo.QUEST_TYPE_RUN) {
				// 如果是跑环,跑环次数加1
				roleLoadInfo.setTodayRunNum(roleLoadInfo.getTodayRunNum() + 1);
				
				// 更新数据库
				RoleDAO.getInstance().updateRoleRunQuestInfo(roleLoadInfo);
				
				// 处理跑环任务缓存
				questInfoMap.setRunQuestInfo(null);
			}
			
			// 处理活跃度
			if (xmlInfo.getActiveVal() > 0) {
				long now = System.currentTimeMillis();
				
				// 当前任务完成后可获取活跃度
				int newActiveVal = xmlInfo.getActiveVal();
				if (roleLoadInfo.getLastActiveChgTime() != null 
						&& DateUtil.isSameDay(now, roleLoadInfo.getLastActiveChgTime().getTime())) {
					newActiveVal += roleLoadInfo.getTodayActive();
				}
				
				roleLoadInfo.setTodayActive(newActiveVal);
				roleLoadInfo.setLastActiveChgTime(new Timestamp(now));
				
				// 更新数据库
				RoleDAO.getInstance().updateRoleActiveVal(roleLoadInfo);
				SceneService.sendRoleRefreshMsg(roleId, SceneService.REFESH_TYPE_ROLE, "");
			}
			
			String prizeNo = xmlInfo.getPrizeNo();
			List<DropXMLInfo> drops = PropBagXMLMap.getPropBagXMLListbyStr(prizeNo);
			List<BattlePrize> getPropList = new ArrayList<BattlePrize>();
			int result1 = ItemService.addPrizeForPropBag(ActionType.action181.getType(), roleInfo, drops,null,
					getPropList,null, null, null, false);
			
			if (result1 != 1) {
				resp.setResult(result1);
				return resp;
			}
			
			// 检查新手引导
			int ck = GuideMgtService.dealGuideIndexNum(roleInfo, UserGuideNode.GUIDE_TASK_NODES);
			if(ck != 1){
				resp.setResult(ck);
				return resp;
			}
			
			// 任务
			boolean isRedPointQuest = QuestService.checkQuest(roleInfo, ActionType.action181.getType(), xmlInfo.getQuestProtoNo(), true, false);
			boolean isRedPointMonth = RedPointMgtService.check2PopRedPoint(roleInfo.getId(), res, false, RedPointMgtService.LISTENING_QUEST_FIGHT_END);
			//红点推送
			if (isRedPointQuest || isRedPointMonth) {
				RedPointMgtService.pop(roleInfo.getId());
			}
						
			// 日志
			GameLogService.insertTaskLog(roleInfo, info, 1);
			return resp;
		}
	}

	/**
	 * 记录当前任务的对话顺序
	 * 
	 * @param roleId
	 * @param req
	 */
	public void dealTalkOrder(int roleId, NpcTalkOrderReq req) {
		int questProtoNo = req.getQuestProtoNo();
		if (questProtoNo <= 0) {
			return;
		}
		
		int talkOrder = req.getTalkOrder();
		if (talkOrder < 0) {
			talkOrder = 0;
		}
		
		if (talkOrder > 2) {
			talkOrder = 2;
		}

		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			return;
		}
		synchronized (roleInfo) {
			if (roleInfo.getRoleLoadInfo() == null || roleInfo.getQuestInfoMap() == null) {
				return;
			}
			
			// 任务xml判断
			QuestProtoXmlInfo xmlInfo = QuestProtoXmlInfoMap.questXml(questProtoNo);
			if (xmlInfo == null) {
				return;
			}

			QuestInProgressInfo info = roleInfo.getQuestInfoMap().getQuestInProgressInfo(questProtoNo);
			if (info == null) {
				return;
			}
			
			// 记录当前任务的对话顺序
			questDAO.updateQuestTalkOrder(info.getId(), talkOrder);
			
			info.setTalkOrder(talkOrder);
			
			// 任务
			QuestService.checkQuest(roleInfo, ActionType.action182.getType(), xmlInfo.getQuestProtoNo(), true, true);
			
		}
	}

	/**
	 * 一键秒任务
	 * 
	 * @param roleId
	 * @param req
	 * @return
	 */
	public OneKeyQuestResp oneKeyQuest(int roleId, OneKeyQuestReq req) {
		OneKeyQuestResp resp = new OneKeyQuestResp();

		if (req.getIsOneKey() != 1 && req.getIsOneKey() != 2) {
			resp.setResult(ErrorCode.ROLE_NOT_EXIST_ERROR_9);
			return resp;
		}
		
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			resp.setResult(ErrorCode.QUEST_ERROR_2);
			return resp;
		}
		synchronized (roleInfo) {
			RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
			if (roleLoadInfo == null) {
				resp.setResult(ErrorCode.ROLE_NOT_EXIST_ERROR_9);
				return resp;
			}
			QuestInfoMap questInfoMap = roleInfo.getQuestInfoMap();
			if (questInfoMap == null) {
				resp.setResult(ErrorCode.SYSTEM_ERROR_CODE38);
				return resp;
			}
			
			StringBuilder noStr = new StringBuilder();
			if (req.getIsOneKey() == 2) {
				// 秒单个
				String questProtoNoStr = req.getQuestProtoNoStr();
				if (questProtoNoStr == null || "".equals(questProtoNoStr)) {
					resp.setResult(ErrorCode.ROLE_NOT_EXIST_ERROR_9);
					return resp;
				}
				
				int questProtoNo = Integer.valueOf(questProtoNoStr);
				
				QuestProtoXmlInfo xmlInfo = QuestProtoXmlInfoMap.questXml(questProtoNo);
				if (xmlInfo == null) {
					resp.setResult(ErrorCode.QUEST_ERROR_3);
					return resp;
				}
				
				// 该任务是否可以秒
				if (xmlInfo.getNeedCost() <= 0) {
					resp.setResult(ErrorCode.QUEST_ERROR_4);
					return resp;
				}

				QuestInProgressInfo info = questInfoMap.getQuestInProgressInfo(questProtoNo);
				if (info == null) {
					resp.setResult(ErrorCode.QUEST_ERROR_5);
					return resp;
				}

				// 任务状态判断
				if (info.getStatus() != QuestInProgressInfo.STATUS_REVEIVE) {
					resp.setResult(ErrorCode.QUEST_ERROR_6);
					return resp;
				}
				
				// 先验证是否有道具抵扣
				int itemNo = xmlInfo.getOneKeyItemNo();
				int itemNum = xmlInfo.getOneKeyItemNum();
				PropXMLInfo propXml = PropXMLInfoMap.getPropXMLInfo(itemNo);
				if (propXml == null) {
					resp.setResult(ErrorCode.ROLE_NOT_EXIST_ERROR_9);
					return resp;
				}
				if (!BagItemMap.checkBagItemNum(roleInfo, itemNo, itemNum)) {
					// 道具不足，验证金币是否足够
					if (roleInfo.getCoin() < xmlInfo.getNeedCost()) {
						resp.setResult(ErrorCode.ROLE_COIN_ERROR_1);
						return resp;
					}
					
					// 扣除金币
					if (!RoleService.subRoleRoleResource(ActionType.action183.getType(), roleInfo, ConditionType.TYPE_COIN, xmlInfo.getNeedCost(), null)) {
						resp.setResult(ErrorCode.ROLE_COIN_ERROR_1);
						return resp;
					}
					
					SceneService.sendRoleRefreshMsg(roleId, SceneService.REFESH_TYPE_ROLE, "");
				} else {
					Map<Integer, Integer> itemMap = new HashMap<Integer, Integer>();
					itemMap.put(itemNo, itemNum);
					int rt = ItemService.bagItemDel(ActionType.action183.getType(), roleInfo, itemMap);
					if (rt != 1) {
						resp.setResult(rt);
						return resp;
					}
				}
				
				noStr.append(xmlInfo.getQuestProtoNo()).append(",");
			} else {
				// 秒所有
				int totalCost = 0;
				for (QuestProtoXmlInfo xmlInfo : QuestProtoXmlInfoMap.getOneKeyMap().values()) {
					QuestInProgressInfo info = questInfoMap.getQuestInProgressInfo(xmlInfo.getQuestProtoNo());
					if (info == null) {
						// 角色没有该任务
						continue;
					}
					
					// 任务状态判断
					if (info.getStatus() != QuestInProgressInfo.STATUS_REVEIVE) {
						continue;
					}
					
					noStr.append(xmlInfo.getQuestProtoNo()).append(",");
					
					totalCost += xmlInfo.getNeedCost();
				}
				
				if (totalCost <= 0) {
					// 没有这样的任务
					resp.setResult(ErrorCode.QUEST_ERROR_6);
					return resp;
				}
				
				// 判断金币是否足够
				if (roleInfo.getCoin() < totalCost) {
					resp.setResult(ErrorCode.ROLE_COIN_ERROR_1);
					return resp;
				}
				
				// 扣除金币
				if (!RoleService.subRoleRoleResource(ActionType.action183.getType(), roleInfo, ConditionType.TYPE_COIN, totalCost, null)) {
					resp.setResult(ErrorCode.ROLE_COIN_ERROR_1);
					return resp;
				}
				
				SceneService.sendRoleRefreshMsg(roleId, SceneService.REFESH_TYPE_ROLE, "");
			}
			
			// 改变任务状态
			List<QuestInProgressInfo> finishQuest = new ArrayList<QuestInProgressInfo>();
			
			String questNoStr = noStr.deleteCharAt(noStr.length() - 1).toString();
			String[] strArr = questNoStr.split(",");
			for (String str : strArr) {
				int questProtoNo = Integer.valueOf(str);
				QuestInProgressInfo info = questInfoMap.getQuestInProgressInfo(questProtoNo);
				// 设置进度值 默认约定10086
				info.setValue1(10086);
				info.setFinishState();
				
				finishQuest.add(info);
			}
			
			if (finishQuest.size() > 0) {
				// 修改任务状态
				questDAO.dealQuestInfo(null, finishQuest);
			}

			SceneService.sendRoleRefreshMsg(roleInfo.getId(), SceneService.REFESH_TYPE_QUEST, questNoStr);
			
			resp.setQuestProtoNoStr(questNoStr);
		}
		
		resp.setResult(1);
		resp.setIsOneKey(req.getIsOneKey());
		return resp;
	}

}
