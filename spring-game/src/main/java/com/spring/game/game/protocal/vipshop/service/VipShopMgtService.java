package com.snail.webgame.game.protocal.vipshop.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.epilot.ccf.config.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.game.cache.RoleInfoMap;
import com.snail.webgame.game.cache.ToolBoxMap;
import com.snail.webgame.game.common.ActionType;
import com.snail.webgame.game.common.ErrorCode;
import com.snail.webgame.game.common.GameValue;
import com.snail.webgame.game.common.fightdata.DropInfo;
import com.snail.webgame.game.common.util.DateUtil;
import com.snail.webgame.game.common.xml.cache.VipXMLInfoMap;
import com.snail.webgame.game.common.xml.info.VipXMLInfo;
import com.snail.webgame.game.condtion.AbstractConditionCheck;
import com.snail.webgame.game.condtion.ConditionType;
import com.snail.webgame.game.condtion.conds.CoinCond;
import com.snail.webgame.game.core.GameLogService;
import com.snail.webgame.game.core.SceneService;
import com.snail.webgame.game.dao.MailDAO;
import com.snail.webgame.game.dao.RoleBoxDAO;
import com.snail.webgame.game.dao.RoleDAO;
import com.snail.webgame.game.info.MailInfo.MailAttachment;
import com.snail.webgame.game.info.RoleBoxRecordInfo;
import com.snail.webgame.game.info.RoleInfo;
import com.snail.webgame.game.info.RoleLoadInfo;
import com.snail.webgame.game.info.ToolBoxInfo;
import com.snail.webgame.game.info.log.RoleChargeLog;
import com.snail.webgame.game.protocal.activity.service.ActivityService;
import com.snail.webgame.game.protocal.appellation.service.TitleService;
import com.snail.webgame.game.protocal.fight.fightend.BattlePrize;
import com.snail.webgame.game.protocal.item.service.ItemService;
import com.snail.webgame.game.protocal.mail.service.MailService;
import com.snail.webgame.game.protocal.opactivity.service.OpActivityService;
import com.snail.webgame.game.protocal.quest.service.QuestService;
import com.snail.webgame.game.protocal.rolemgt.service.RoleService;
import com.snail.webgame.game.protocal.vipshop.buysalebox.BuySaleBoxResp;
import com.snail.webgame.game.protocal.vipshop.buyvipaward.BuyVipLvAwardResp;
import com.snail.webgame.game.protocal.vipshop.checkpay.PayBeforeCheckResp;
import com.snail.webgame.game.protocal.vipshop.query.ChargeBoxRe;
import com.snail.webgame.game.protocal.vipshop.query.QueryVipShopResp;
import com.snail.webgame.game.protocal.vipshop.querysalebox.QuerySaleBoxRe;
import com.snail.webgame.game.protocal.vipshop.querysalebox.QuerySaleBoxResp;
import com.snail.webgame.game.protocal.vipshop.queryvipbuy.QueryVipBuyRe;
import com.snail.webgame.game.protocal.vipshop.queryvipbuy.QueryVipBuyResp;
import com.snail.webgame.game.protocal.vipshop.vipaward.GetVipAwardResp;
import com.snail.webgame.game.xml.cache.PayXMLInfoMap;
import com.snail.webgame.game.xml.cache.PropBagXMLMap;
import com.snail.webgame.game.xml.cache.VipItemBuyXMLInfoMap;
import com.snail.webgame.game.xml.info.DropXMLInfo;
import com.snail.webgame.game.xml.info.PayXMLInfo;
import com.snail.webgame.game.xml.info.VipItemBuyXMLInfo;

public class VipShopMgtService {

	private static final Logger logger = LoggerFactory.getLogger("logs");
	
	/**
	 * 查询vip商店界面信息
	 * 
	 * @param roleId
	 * @return
	 */
	public QueryVipShopResp queryVipShopInfo(int roleId) {
		QueryVipShopResp resp = new QueryVipShopResp();
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			resp.setResult(ErrorCode.ROLE_NOT_EXIST_ERROR_15);
			return resp;
		}
		synchronized (roleInfo) {
			RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
			if (roleLoadInfo == null) {
				resp.setResult(ErrorCode.ROLE_NOT_EXIST_ERROR_9);
				return resp;
			}

			resp.setVipLv((byte) roleInfo.getVipLv());
			resp.setFirstChargeSaleNoStr(roleInfo.getFirstChargeSaleNoStr());
			resp.setTotalCharge((int) roleInfo.getTotalCharge());
			resp.setTotalVipExp(roleInfo.getVipExp());
			
			// 当前购买的会员卡
			long now = System.currentTimeMillis();
			if (roleInfo.getCardEndTime() != null && roleInfo.getCardEndTime().getTime() > now) {
				resp.setCardType(roleInfo.getCardType());
			}
			
			// 是否购买了福利卡
			if (roleInfo.getFuliCardEndTime() != null && roleInfo.getFuliCardEndTime().getTime() > now) {
				resp.setIsBuyExtra((byte) 1);
			}
			
			Map<Integer, RoleBoxRecordInfo> roleBoxMap = roleInfo.getRoleBoxMap();
			
			// 已上架的充值宝箱
			List<ChargeBoxRe> list = new ArrayList<ChargeBoxRe>();
			ChargeBoxRe re = null;
			Collection<ToolBoxInfo> chargeBoxs = ToolBoxMap.fetchBoxInfo(ToolBoxInfo.TYPE_BOX_CHARGE);
			if (chargeBoxs != null && !chargeBoxs.isEmpty()) {
				for (ToolBoxInfo chargeBox : chargeBoxs) {
					if (chargeBox.getStartTime().getTime() < now && now < chargeBox.getEndTime().getTime()) {
						
						if (roleBoxMap != null && roleBoxMap.get(chargeBox.getId()) != null) {
							RoleBoxRecordInfo roleBox = roleBoxMap.get(chargeBox.getId());
							if (roleBox.getBuyNum() >= chargeBox.getItemSaleNum()) {
								// 已购买完
								continue;
							}
						}
						
						re = new ChargeBoxRe();
						re.setChargeBoxNo(chargeBox.getChargeNo());
						re.setSellItemStr(chargeBox.getItemStr());
						list.add(re);
					}
				}
			}
			
			if (list.size() > 0) {
				resp.setCount(list.size());
				resp.setBoxList(list);
			}
			
			// vip礼包已领取字符串
			String getVipGiftStr = "";
			if (roleLoadInfo.getDrawVipGiftStr() != null && !"".equals(roleLoadInfo.getDrawVipGiftStr())) {
				getVipGiftStr = roleLoadInfo.getDrawVipGiftStr();
			}
			resp.setGetVipGiftStr(getVipGiftStr);
			
		}
		resp.setResult(1);
		return resp;
	}

	/**
	 * 领取vip礼包奖励
	 * 
	 * @param roleId
	 * @return
	 */
	public GetVipAwardResp getVipAward(int roleId, int vipNo) {
		GetVipAwardResp resp = new GetVipAwardResp();
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			resp.setResult(ErrorCode.ROLE_NOT_EXIST_ERROR_15);
			return resp;
		}
		synchronized (roleInfo) {
			RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
			if (roleLoadInfo == null) {
				resp.setResult(ErrorCode.ROLE_NOT_EXIST_ERROR_9);
				return resp;
			}
			
			VipXMLInfo vipXMLInfo = VipXMLInfoMap.getVipXMLInfo(vipNo);
			if (vipXMLInfo == null) {
				resp.setResult(ErrorCode.SYSTEM_ERROR_CODE30);
				return resp;
			}
			
			// vip礼包是否已领取
			if (roleLoadInfo.getDrawVipGiftStr() != null && !"".equals(roleLoadInfo.getDrawVipGiftStr())) {
				String[] subArr = roleLoadInfo.getDrawVipGiftStr().split(",");
				if (ArrayUtils.contains(subArr, vipNo + "")) {
					resp.setResult(ErrorCode.SYSTEM_ERROR_CODE31);
					return resp;
				}
			}
			
			if (roleInfo.getVipLv() < vipNo) {
				resp.setResult(ErrorCode.SYSTEM_ERROR_CODE32);
				return resp;
			}
			
			int result = ItemService.addItemAndEquipCheck(roleInfo);
			//背包格子上限判断
			if (result != 1) {
				resp.setResult(ErrorCode.QUEST_ERROR_23);
				return resp;
			}
			
			// 更新数据库缓存
			String drawVipGiftStr = "" + vipNo;
			if (roleLoadInfo.getDrawVipGiftStr() != null) {
				drawVipGiftStr = roleLoadInfo.getDrawVipGiftStr() + "," + drawVipGiftStr;
			}
			
			if (RoleDAO.getInstance().updateVipAwardStr(roleId, drawVipGiftStr)) {
				roleLoadInfo.setDrawVipGiftStr(drawVipGiftStr);
			}
			
			String prizeNo = vipXMLInfo.getPrizeNo();
			List<DropXMLInfo> drops = PropBagXMLMap.getPropBagXMLListbyStr(prizeNo);
			
			List<BattlePrize> getPropList = new ArrayList<BattlePrize>();
			
			int result1 = ItemService.addPrizeForPropBag(ActionType.action434.getType(), roleInfo, drops, null,
					getPropList, null, null, null, false);
			
			if (result1 != 1) {
				resp.setResult(result1);
				return resp;
			}
			
			// 日志
			GameLogService.insertPlayActionLog(roleInfo, ActionType.action434.getType(), vipNo + "");
			
			SceneService.sendRoleRefreshMsg(roleId, SceneService.REFRESH_TYPE_CHARGE, "");
			
		}
		resp.setResult(1);
		resp.setVipNo(vipNo);
		return resp;
	}

	/**
	 * 角色充值处理
	 * 
	 * @param roleId
	 * @param orderStr
	 * @param itemId
	 * @param chargeMoney 当笔充值的钱(人民币)
	 * @param chargeBoxItemStr 购买充值礼包时的奖励道具
	 */
	public static boolean dealRoleCharege(int roleId, String orderStr, int itemId, int chargeMoney, String chargeBoxItemStr) {
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			logger.warn("deal role charge , but role is null, roleId = " + roleId + ", orderStr =" + orderStr + ", itemId = " + itemId);
			return false;
		}
		synchronized (roleInfo) {
			// 比对玩家扣的钱和配置内的价格是否匹配
			PayXMLInfo payXMLInfo = PayXMLInfoMap.fetchPayXMLInfo(itemId);
			if (payXMLInfo == null) {
				logger.warn("deal role charge , pay.xml not find itemId , roleId = " + roleId + ", orderStr =" + orderStr + ", itemId = " + itemId);
				return false;
			}
			
			// 这笔充值总共可以得到的金币
			int addTotalCoin = 0;
			boolean isIgnore = false;
						
			// 充值金额比配置价格低直接忽略
			if (chargeMoney < payXMLInfo.getMoneyCost()) {
				logger.warn("deal role charge , charge money is less , roleId = " + roleId + 
						", orderStr =" + orderStr + ", itemId = " + itemId + ", chargeMoney = " + chargeMoney);
				
				addTotalCoin = chargeMoney * GameValue.RMB_TO_COINT_RATE;
				isIgnore = true;
				
				GameLogService.insertRoleChargeLog(roleInfo.getAccount(), roleInfo.getRoleName(), orderStr, itemId, RoleChargeLog.ROLE_CHARGE_EVENT_3);
			}

			int gameAction = ActionType.action431.getType();
			if (!isIgnore) {
				// 充值购买金子
				if (payXMLInfo.getPayType() == PayXMLInfo.PAY_TYPE_COIN) {
					addTotalCoin = dealPayCoin(roleInfo, payXMLInfo, chargeMoney);
					
				} else if (payXMLInfo.getPayType() == PayXMLInfo.PAY_TYPE_CARD) {
					// 充值购买会员卡
					addTotalCoin = dealPayCard(roleInfo, payXMLInfo, chargeMoney);
					
					gameAction = PayXMLInfo.fetchCardAction(payXMLInfo.getNo());
				} else if (payXMLInfo.getPayType() == PayXMLInfo.PAY_TYPE_BOX) {
					// 充值购买礼包
					addTotalCoin = dealPayBox(roleInfo, payXMLInfo, chargeBoxItemStr, orderStr);
					
					gameAction = ActionType.action433.getType();
				}
			}
			
			// 记录真实充值的钱
			long totalChargeMoney = roleInfo.getTotalCharge() + chargeMoney;
			if (RoleDAO.getInstance().updateRoleTotalCharge(roleId, totalChargeMoney)) {
				roleInfo.setTotalCharge(totalChargeMoney);
				
				int totalVipExp = roleInfo.getVipExp() + chargeMoney;
				if (RoleDAO.getInstance().updateVipExp(roleInfo.getId(), totalVipExp)) {
					roleInfo.setVipExp(totalVipExp);
					checkVipLvChg(roleInfo, totalVipExp);
				}
			}
			
			//
			DropInfo drop = new DropInfo(String.valueOf(payXMLInfo.getNo()),1);
			if (addTotalCoin > 0) {
				RoleService.addRoleRoleResource(gameAction, roleInfo, ConditionType.TYPE_COIN, addTotalCoin,drop);
				
				// 推送金币变化
				SceneService.sendRoleRefreshMsg(roleInfo.getId(), SceneService.REFESH_TYPE_ROLE, "");
			}
			
			// 日志
			GameLogService.insertPlayActionLog(roleInfo, gameAction, itemId + "");
			GameLogService.insertRoleChargeLog(roleInfo.getAccount(), roleInfo.getRoleName(), orderStr, itemId, RoleChargeLog.ROLE_CHARGE_EVENT_2);
			
			// 刷新角色信息 推送相关刷新
			if (roleInfo.getLoginStatus() == 1) {
				// 推送充值及vip信息
				SceneService.sendRoleRefreshMsg(roleInfo.getId(), SceneService.REFRESH_TYPE_CHARGE, "");
				
				// 检测任务
				QuestService.checkQuest(roleInfo, gameAction, null, true, true);
				
				// 判断首冲标记是否有变化
				if (totalChargeMoney == chargeMoney) {
					SceneService.sendRoleRefreshMsg(roleId, SceneService.REFRESH_TYPE_FIRST, "");
				}
				
				// 7日活动
				SceneService.sendRoleRefreshMsg(roleInfo.getId(), SceneService.REFRESH_TYPE_SEVEN, "");
				
				// 精彩活动
				SceneService.sendRoleRefreshMsg(roleId, SceneService.REFRESH_TYPE_WONDER, "");
			}
			
			// 累计充值时限活动检测
			OpActivityService.dealOpActProInfoCheck(roleInfo, ActionType.action412.getType(), chargeMoney, true);
			
			return true;
		}
	}
	
	/**
	 * 检测vip等级变化
	 * 
	 * @param roleInfo
	 * @param totalVipExp
	 */
	public static void checkVipLvChg(RoleInfo roleInfo, int totalVipExp) {
		// 检测是否达到下一vip等级的充值要求
		int roleVipLv = roleInfo.getVipLv();
		if (roleVipLv < VipXMLInfoMap.getMaxVipLevel()) {
			
			int newRoleVipLv = 0;
			VipXMLInfo vipXMLInfo = null;
			for (int i = roleVipLv + 1; i <= VipXMLInfoMap.getMaxVipLevel(); i++) {
				vipXMLInfo = VipXMLInfoMap.getVipXMLInfo(i);
				if (vipXMLInfo == null || totalVipExp < vipXMLInfo.getNeedVipMoney()) {
					break;
				}
				
				newRoleVipLv = i;
			}
			
			if (newRoleVipLv > roleVipLv) {
				if (RoleDAO.getInstance().updateRoleVipLv(roleInfo.getId(), newRoleVipLv)) {
					roleInfo.setVipLv(newRoleVipLv);
					
					for(int i = 0; i <= roleInfo.getVipLv(); i++){
						TitleService.achieveTitleCheck(GameValue.APPELLATION_TYPE_VIP, i, roleInfo);
					}
				}
				ActivityService.resNum(roleInfo, roleVipLv, newRoleVipLv);
			}
		}
	}
	
	/**
	 * 金币充值处理
	 * 
	 * @param roleInfo
	 * @param payXMLInfo
	 * @param chargeMoney
	 */
	private static int dealPayCoin(RoleInfo roleInfo, PayXMLInfo payXMLInfo, int chargeMoney) {
		// 是否合服第一次购买买一送一
		boolean isGive = false;
		if (payXMLInfo.getFirstReward() > 0) {
			if (roleInfo.getFirstChargeSaleNoStr() != null) {
				String[] tempArr = roleInfo.getFirstChargeSaleNoStr().split(",");
				if (!ArrayUtils.contains(tempArr, payXMLInfo.getNo() + "")) {
					isGive = true;
				}
			} else {
				isGive = true;
			}
		}
		
		int addTotalCoin = chargeMoney * GameValue.RMB_TO_COINT_RATE + payXMLInfo.getGiftReward();
		if (isGive) {
			addTotalCoin = chargeMoney * GameValue.RMB_TO_COINT_RATE + payXMLInfo.getFirstReward();
		}
		
		// 记录买一送一商品id
		if (isGive) {
			String curItemIdStr = payXMLInfo.getNo() + "";
			if (roleInfo.getFirstChargeSaleNoStr() != null) {
				curItemIdStr = roleInfo.getFirstChargeSaleNoStr() + "," + curItemIdStr;
			}
			
			if (RoleDAO.getInstance().updateRoleFirstChargeRewardStr(roleInfo.getId(), curItemIdStr)) {
				roleInfo.setFirstChargeSaleNoStr(curItemIdStr);
			}
		}
		
		return addTotalCoin;
	}
	
	/**
	 * 会员卡购买处理
	 * 
	 * @param roleInfo
	 * @param payXMLInfo
	 */
	private static int dealPayCard(RoleInfo roleInfo, PayXMLInfo payXMLInfo, int chargeMoney) {
		long now = System.currentTimeMillis();
		
		long cardEndTime = DateUtil.fetchDayStartTime().getTime() + payXMLInfo.getEffectDay() * DateUtil.ONE_DAY_MILLIS;
		if (payXMLInfo.getNo() == PayXMLInfo.PAY_SMALL_TYPE_7) {
			// 福利卡购买
			if (roleInfo.getFuliCardEndTime() != null && roleInfo.getFuliCardEndTime().getTime() > now) {
				// 当前还未过期 // 预防操作  实际情况是不过期不允许购买
				cardEndTime = roleInfo.getFuliCardEndTime().getTime() + payXMLInfo.getEffectDay() * DateUtil.ONE_DAY_MILLIS;
			}
			roleInfo.setFuliCardEndTime(new Timestamp(cardEndTime));
		} else {
			byte buyCardType = 1;// 月卡
			if (payXMLInfo.getNo() == PayXMLInfo.PAY_SMALL_TYPE_2 || payXMLInfo.getNo() == PayXMLInfo.PAY_SMALL_TYPE_4) {
				// 购买季卡
				buyCardType = 2;
				
			} else if (payXMLInfo.getNo() == PayXMLInfo.PAY_SMALL_TYPE_3 
					|| payXMLInfo.getNo() == PayXMLInfo.PAY_SMALL_TYPE_5 || payXMLInfo.getNo() == PayXMLInfo.PAY_SMALL_TYPE_6) {
				// 购买年卡
				buyCardType = 3;
			}
			
			if (roleInfo.getCardEndTime() != null && roleInfo.getCardEndTime().getTime() > now) {
				// 当前还未过期 
				cardEndTime = roleInfo.getCardEndTime().getTime() + payXMLInfo.getEffectDay() * DateUtil.ONE_DAY_MILLIS;
				
				if (buyCardType < roleInfo.getCardType()) {
					buyCardType = roleInfo.getCardType();
				}
			}
			roleInfo.setCardType(buyCardType);
			roleInfo.setCardEndTime(new Timestamp(cardEndTime));
		}
		
		// 
		RoleDAO.getInstance().updateRoleTimeCardInfo(roleInfo);
		
		return chargeMoney * GameValue.RMB_TO_COINT_RATE + payXMLInfo.getGiftReward();
	}
	
	/**
	 * 礼包购买处理
	 * 
	 * @param roleInfo
	 * @param payXMLInfo
	 * @param chargeBoxItemStr
	 */
	private static int dealPayBox(RoleInfo roleInfo, PayXMLInfo payXMLInfo, String chargeBoxItemStr, String orderStr) {
		// 这里不用判断礼包过期,直接添加道具
		if (chargeBoxItemStr == null) {
			logger.warn("deal role charge , charge box item is miss, roleId = " + roleInfo.getId() + 
					", orderStr =" + orderStr + ", itemId = " + payXMLInfo.getNo());
			return 0;
		}
		
		ToolBoxInfo toolBoxInfo = ToolBoxMap.fetchBoxInfoById(ToolBoxInfo.TYPE_BOX_CHARGE, payXMLInfo.getNo());
		if (toolBoxInfo != null) {
			Map<Integer, RoleBoxRecordInfo> roleBoxMap = roleInfo.getRoleBoxMap();
			if (roleBoxMap == null) {
				roleBoxMap = new HashMap<Integer, RoleBoxRecordInfo>();
				roleInfo.setRoleBoxMap(roleBoxMap);
			}
			
			RoleBoxRecordInfo roleBoxInfo = roleBoxMap.get(toolBoxInfo.getId());
			if (roleBoxInfo == null) {
				roleBoxInfo = new RoleBoxRecordInfo();
				roleBoxInfo.setBoxId(toolBoxInfo.getId());
				roleBoxInfo.setBoxVersion(toolBoxInfo.getBoxVersion());
				roleBoxInfo.setBuyNum(1);
				roleBoxInfo.setBuyTime(new Timestamp(System.currentTimeMillis()));
				roleBoxInfo.setRoleId(roleInfo.getId());
				
				roleBoxMap.put(toolBoxInfo.getId(), roleBoxInfo);
				
				RoleBoxDAO.getInstance().insertRoleBoxInfo(roleBoxInfo);
			} else {
				roleBoxInfo.setBuyNum(roleBoxInfo.getBuyNum() + 1);
				roleBoxInfo.setBuyTime(new Timestamp(System.currentTimeMillis()));
				RoleBoxDAO.getInstance().updateRoleBoxInfo(roleBoxInfo);
			}
		}
		
		// 物品掉落
		List<DropInfo> itemList = new ArrayList<DropInfo>();
		
		String[] boxArr = chargeBoxItemStr.split(";");
		for(String subStr : boxArr) {
			String[] itemArr = subStr.split(":");
			
			// 道具
			itemList.add(new DropInfo(itemArr[1], Integer.valueOf(itemArr[0])));
		}
		
		// 添加道具
		if (roleInfo.getLoginStatus() == 1) {
			// 判断背包
			int result = ItemService.addItemAndEquipCheck(roleInfo);
			//背包格子上限判断
			if (result == 1) {
				if (itemList.size() > 0) {
					ItemService.itemAdd(ActionType.action433.getType(), roleInfo, itemList, null, null, null, null, true);
				}
				
				return 0;
			}
		}
		
		// 不在线或背包满了走邮件
		List<MailAttachment> attachments = new ArrayList<MailAttachment>();
		MailAttachment att = null;
		for (DropInfo drop : itemList) {
			att = new MailAttachment(drop.getItemNo(), drop.getItemNum(), NumberUtils.toInt(drop.getParam()), 0);
			attachments.add(att);
		}

		String attachment = MailDAO.encoderAttachment(attachments);
		if (attachment.length() > 0) {
			String title = Resource.getMessage("game", "GAME_CHARGE_TITLE");
			String content = Resource.getMessage("game", "GAME_CHARGE_CONTENT");
			String reserve = DateUtil.getDateStrFromDate(new Date());

			MailService.pushMailPrize(roleInfo.getId() + "", attachment, title, content, reserve);
		}		
		
		return 0;
	}

	/**
	 * 充值前商品检测
	 * 
	 * @param roleId
	 * @param itemId
	 * @return
	 */
	public PayBeforeCheckResp payBeforeCheck(int roleId, int itemId) {
     	PayBeforeCheckResp resp = new PayBeforeCheckResp();
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			resp.setResult(ErrorCode.ROLE_NOT_EXIST_ERROR_15);
			return resp;
		}
		synchronized (roleInfo) {
			RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
			if (roleLoadInfo == null) {
				resp.setResult(ErrorCode.ROLE_NOT_EXIST_ERROR_9);
				return resp;
			}
			
			PayXMLInfo payXMLInfo = PayXMLInfoMap.fetchPayXMLInfo(itemId);
			if (payXMLInfo == null) {
				// 无此商品
				resp.setResult(ErrorCode.SYSTEM_ERROR_CODE33);
				return resp;
			}
			
			long now = System.currentTimeMillis();
			if (payXMLInfo.getPayType() == PayXMLInfo.PAY_TYPE_CARD) {
				// 充值购买会员卡
				// 验证卡是否已存在
				if (itemId == PayXMLInfo.PAY_SMALL_TYPE_7) {
					// 购买福利卡
					if (roleInfo.getFuliCardEndTime() != null && roleInfo.getFuliCardEndTime().getTime() > now) {
						// 福利卡已存在
						resp.setResult(ErrorCode.SYSTEM_ERROR_CODE34);
						return resp;
					}
				} else if (itemId == PayXMLInfo.PAY_SMALL_TYPE_1 || itemId == PayXMLInfo.PAY_SMALL_TYPE_2 || itemId == PayXMLInfo.PAY_SMALL_TYPE_3) {
					// 购买月卡、季卡、年卡
					if (roleInfo.getCardType() != 0 && roleInfo.getCardEndTime().getTime() > now) {
						resp.setResult(ErrorCode.SYSTEM_ERROR_CODE35);
						return resp;
					}
				} else if (itemId == PayXMLInfo.PAY_SMALL_TYPE_4 || itemId == PayXMLInfo.PAY_SMALL_TYPE_5) {
					// 月卡升级
					if (roleInfo.getCardType() != 1) {
						resp.setResult(ErrorCode.SYSTEM_ERROR_CODE36);
						return resp;
					}
				} else if (itemId == PayXMLInfo.PAY_SMALL_TYPE_6) {
					// 季卡升级
					if (roleInfo.getCardType() != 2) {
						resp.setResult(ErrorCode.SYSTEM_ERROR_CODE37);
						return resp;
					}
				} 
				
			} else if (payXMLInfo.getPayType() == PayXMLInfo.PAY_TYPE_BOX) {
				// 充值购买礼包
				// 验证当前礼包是否已过期
				ToolBoxInfo chargeBoxInfo = ToolBoxMap.fetchBoxInfoById(ToolBoxInfo.TYPE_BOX_CHARGE, itemId);
				if (chargeBoxInfo == null) {
					resp.setResult(ErrorCode.CHARGE_BOX_IS_OLD_ERROR);
					return resp;
				}
				
				if (now < chargeBoxInfo.getStartTime().getTime() || now > chargeBoxInfo.getEndTime().getTime()) {
					resp.setResult(ErrorCode.CHARGE_BOX_IS_OLD_ERROR);
					return resp;
				}
				
				Map<Integer, RoleBoxRecordInfo> roleBoxMap = roleInfo.getRoleBoxMap();
				if (roleBoxMap != null && roleBoxMap.get(chargeBoxInfo.getId()) != null) {
					RoleBoxRecordInfo roleBoxInfo = roleBoxMap.get(chargeBoxInfo.getId());
					if (roleBoxInfo.getBuyNum() >= chargeBoxInfo.getItemSaleNum()) {
						resp.setResult(ErrorCode.CHARGE_BOX_LESS_ERROR);
						return resp;
					}
				}
			}
		}
		resp.setResult(1);
		resp.setItemId(itemId);
		return resp;
	}

	/**
	 * 查询促销礼包信息
	 * 
	 * @param roleId
	 * @return
	 */
	public QuerySaleBoxResp querySaleBoxInfo(int roleId) {
		QuerySaleBoxResp resp = new QuerySaleBoxResp();
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			resp.setResult(ErrorCode.ROLE_NOT_EXIST_ERROR_15);
			return resp;
		}
		synchronized (roleInfo) {
			RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
			if (roleLoadInfo == null) {
				resp.setResult(ErrorCode.ROLE_NOT_EXIST_ERROR_9);
				return resp;
			}
			
			Map<Integer, RoleBoxRecordInfo> roleBoxMap = roleInfo.getRoleBoxMap();
			
			long now = System.currentTimeMillis();
			List<QuerySaleBoxRe> queryList = new ArrayList<QuerySaleBoxRe>();
			QuerySaleBoxRe re = null;
			
			RoleBoxRecordInfo roleBoxInfo = null;
			Collection<ToolBoxInfo> saleBoxs = ToolBoxMap.fetchBoxInfo(ToolBoxInfo.TYPE_BOX_GOLD);
			if (saleBoxs != null && !saleBoxs.isEmpty()) {
				for (ToolBoxInfo boxInfo : saleBoxs) {
					if (now < boxInfo.getStartTime().getTime() || now >= boxInfo.getEndTime().getTime()) {
						// 礼包是否上架
						continue;
					}
					
					re = new QuerySaleBoxRe();
					re.setBoxId(boxInfo.getId());
					re.setBoxName(boxInfo.getBoxName());
					re.setCostPrice(boxInfo.getCostPrice());
					re.setItemStr(boxInfo.getItemStr());
					re.setBoxQuality(boxInfo.getBoxQuality());
					
					// 剩余出售个数
					int buyNum = 0;
					if (roleBoxMap != null) {
						roleBoxInfo = roleBoxMap.get(boxInfo.getId());
						if (roleBoxInfo != null) {
							buyNum = roleBoxInfo.getBuyNum();
						}
					}
					int itemSaleNum = boxInfo.getItemSaleNum() - buyNum <= 0 ? 0 : boxInfo.getItemSaleNum() - buyNum;
					re.setItemSaleNum(itemSaleNum);
					
					queryList.add(re);
				}
			}
			
			if (queryList.size() > 0) {
				resp.setCount(queryList.size());
				resp.setQueryList(queryList);
			}
		}
		resp.setResult(1);
		return resp;
	}
	
	/**
	 * 购买促销礼包
	 * 
	 * @param roleId
	 * @param boxId
	 * @return
	 */
	public BuySaleBoxResp buySaleBox(int roleId, int boxId) {
		BuySaleBoxResp resp = new BuySaleBoxResp();
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			resp.setResult(ErrorCode.ROLE_NOT_EXIST_ERROR_15);
			return resp;
		}
		synchronized (roleInfo) {
			RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
			if (roleLoadInfo == null) {
				resp.setResult(ErrorCode.ROLE_NOT_EXIST_ERROR_9);
				return resp;
			}

			long now = System.currentTimeMillis();
			
			// 验证礼包是否有效
			ToolBoxInfo saleBoxInfo = ToolBoxMap.fetchBoxInfoById(ToolBoxInfo.TYPE_BOX_GOLD, boxId);
			if (saleBoxInfo == null || now < saleBoxInfo.getStartTime().getTime() || now > saleBoxInfo.getEndTime().getTime()) {
				resp.setResult(ErrorCode.CHARGE_BOX_IS_OLD_ERROR);
				return resp;
			}
			
			// 是否还有库存
			Map<Integer, RoleBoxRecordInfo> roleBoxMap = roleInfo.getRoleBoxMap();
			if (roleBoxMap == null) {
				roleBoxMap = new HashMap<Integer, RoleBoxRecordInfo>();
				roleInfo.setRoleBoxMap(roleBoxMap);
			}
			
			boolean isInsert = false;
			RoleBoxRecordInfo roleBoxInfo = roleBoxMap.get(boxId);
			if (roleBoxInfo == null) {
				roleBoxInfo = new RoleBoxRecordInfo();
				roleBoxInfo.setBoxId(boxId);
				roleBoxInfo.setBoxVersion(saleBoxInfo.getBoxVersion());
				roleBoxInfo.setBuyNum(0);
				roleBoxInfo.setBuyTime(new Timestamp(System.currentTimeMillis()));
				roleBoxInfo.setRoleId(roleId);
				
				roleBoxMap.put(boxId, roleBoxInfo);
				
				isInsert = true;
			}
			
			int buyNum = roleBoxInfo.getBuyNum();
			// 库存不足
			if (buyNum >= saleBoxInfo.getItemSaleNum()) {
				resp.setResult(ErrorCode.SYSTEM_ERROR_CODE27);
				return resp;
			}
			
			// 金币是否足够
			if (roleInfo.getCoin() < saleBoxInfo.getCostPrice()) {
				resp.setResult(ErrorCode.ROLE_COIN_ERROR_1);
				return resp;
			}
			
			int result = ItemService.addItemAndEquipCheck(roleInfo);
			//背包格子上限判断
			if (result != 1) {
				resp.setResult(ErrorCode.QUEST_ERROR_23);
				return resp;
			}
			
			// 扣除金币
			if (!RoleService.subRoleRoleResource(ActionType.action435.getType(), roleInfo, ConditionType.TYPE_COIN, saleBoxInfo.getCostPrice() , null)) {
				resp.setResult(ErrorCode.SYSTEM_ERROR_CODE28);
				return resp;
			}
			
			// 更新数据库缓存
			roleBoxInfo.setBuyNum(buyNum + 1);
			if (isInsert) {
				// 新增
				RoleBoxDAO.getInstance().insertRoleBoxInfo(roleBoxInfo);
			} else {
				// update
				roleBoxInfo.setBuyTime(new Timestamp(System.currentTimeMillis()));
				RoleBoxDAO.getInstance().updateRoleBoxInfo(roleBoxInfo);
			}
			
			// 物品掉落
			if (saleBoxInfo.getItemStr() != null && saleBoxInfo.getItemStr().length() > 0) {
				List<DropInfo> itemList = new ArrayList<DropInfo>();
				String[] tempArr = saleBoxInfo.getItemStr().split(";");
				for (String itemStr : tempArr) {
					String[] subArr = itemStr.split(":");
					
					// 道具
					itemList.add(new DropInfo(subArr[1], Integer.valueOf(subArr[0])));
				}
				
				int result1 = ItemService.itemAdd(ActionType.action435.getType(), roleInfo, itemList, null, null, null, null, true);
				if (result1 != 1) {
					resp.setResult(result1);
					return resp;
				}
			}
			
			int remainBoxNum = saleBoxInfo.getItemSaleNum() - roleBoxInfo.getBuyNum();
			resp.setRemainBoxNum(remainBoxNum);
			
			// 推送金币变化
			SceneService.sendRoleRefreshMsg(roleInfo.getId(), SceneService.REFESH_TYPE_ROLE, "");
			
			// 日志
			GameLogService.insertPlayActionLog(roleInfo, ActionType.action435.getType(), saleBoxInfo.getBoxName()+"="+saleBoxInfo.getItemStr());
		}
		resp.setResult(1);
		resp.setBoxId(boxId);
		return resp;
	}
	
	/**
	 * 解析生成促销礼包购买记录map
	 * 
	 * @param saleBoxRecord
	 * @return
	 */
	private static Map<Integer,Integer> generateSaleBoxRecordMap(String saleBoxRecord) {
		Map<Integer,Integer> map = new HashMap<Integer, Integer>();
		if (saleBoxRecord != null && !"".equals(saleBoxRecord)) {
			String[] tempArr = saleBoxRecord.split(";");
			for (String subStr : tempArr) {
				String[] subArr = subStr.split(",");
				map.put(Integer.valueOf(subArr[0]), Integer.valueOf(subArr[1]));
			}
		}
		return map;
	}
	
	/**
	 * 根据记录map生成新的记录字符串
	 * 
	 * @param saleBoxMap
	 * @return
	 */
	public static String generateSaleBoxRecordStr(Map<Integer,Integer> saleBoxMap) {
		StringBuilder sb = new StringBuilder();
		for (Entry<Integer,Integer> entry : saleBoxMap.entrySet()) {
			sb.append(entry.getKey()).append(",").append(entry.getValue()).append(";");
		}
		
		if (sb.length() > 0) {
			return sb.deleteCharAt(sb.length() - 1).toString();
		}
		
		return null;
	}

	/**
	 * 查询vip特权礼包购买信息
	 * 
	 * @param roleId
	 * @return
	 */
	public QueryVipBuyResp queryVipBuyInfo(int roleId) {
		QueryVipBuyResp resp = new QueryVipBuyResp();
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			resp.setResult(ErrorCode.ROLE_NOT_EXIST_ERROR_15);
			return resp;
		}
		synchronized (roleInfo) {
			RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
			if (roleLoadInfo == null) {
				resp.setResult(ErrorCode.ROLE_NOT_EXIST_ERROR_9);
				return resp;
			}
			
			Map<Integer, Integer> map = generateSaleBoxRecordMap(roleLoadInfo.getDrawVipLvBuyStr());
			
			List<QueryVipBuyRe> queryList = new ArrayList<QueryVipBuyRe>();
			QueryVipBuyRe re = null;
			for (VipItemBuyXMLInfo xmlInfo : VipItemBuyXMLInfoMap.getMap().values()) {
				re = new QueryVipBuyRe();
				re.setNo(xmlInfo.getNo());
				
				if (map.get(xmlInfo.getNo()) != null) {
					re.setBuyNum(map.get(xmlInfo.getNo()));
				}
				
				queryList.add(re);
			}
			
			if (queryList.size() > 0) {
				resp.setCount(queryList.size());
				resp.setQueryList(queryList);
			}
			
		}
		
		resp.setResult(1);
		return resp;
	}
	
	/**
	 * vip特权礼包登录处理
	 * 
	 * @param roleInfo
	 * @param isRefresh
	 */
	public static void dealVipBuyAward(RoleInfo roleInfo, boolean isRefresh) {
		RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
		if (roleLoadInfo == null) {
			return;
		}
		
		long now = System.currentTimeMillis();
		if (roleLoadInfo.getLastBuyVipLvTime() != null && !DateUtil.isSameDay(now, roleLoadInfo.getLastBuyVipLvTime().getTime())) {
			roleLoadInfo.setDrawVipLvBuyStr(null);
			roleLoadInfo.setLastBuyVipLvTime(new Timestamp(now));
			
			// 数据更新 缓存变动就可以了，数据库玩家操作时更新
			//RoleDAO.getInstance().updateVipBuyAward(roleLoadInfo);
		}
		
		if (isRefresh) {
			SceneService.sendRoleRefreshMsg(roleInfo.getId(), SceneService.REFRESH_TYPE_VIP_BUY, "");
		}
	}

	/**
	 * 购买vip特权礼包
	 * 
	 * @param roleId
	 * @param no
	 * @param num
	 * @return
	 */
	public BuyVipLvAwardResp buyVipLvAward(int roleId, int no, int num) {
		BuyVipLvAwardResp resp = new BuyVipLvAwardResp();
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			resp.setResult(ErrorCode.ROLE_NOT_EXIST_ERROR_15);
			return resp;
		}
		synchronized (roleInfo) {
			RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
			if (roleLoadInfo == null) {
				resp.setResult(ErrorCode.ROLE_NOT_EXIST_ERROR_9);
				return resp;
			}
			
			VipItemBuyXMLInfo xmlInfo = VipItemBuyXMLInfoMap.fetchVipItemBuyXMLInfo(no);
			if (xmlInfo == null) {
				resp.setResult(ErrorCode.SYSTEM_ERROR_CODE29);
				return resp;
			}
			
			int buyNum = 0;
			Map<Integer, Integer> map = generateSaleBoxRecordMap(roleLoadInfo.getDrawVipLvBuyStr());
			if (map.get(no) != null) {
				buyNum = map.get(no);
			}
			
			// 获取当前vip等级对应最多购买次数
			int buyMaxNum = xmlInfo.fetchBuyMaxNumByVipLv(roleInfo.getVipLv());
			
			if(num < 1)
			{
				resp.setResult(ErrorCode.VIP_BUY_ERROR_1);
				return resp;
			}
			
			if (buyNum + num > buyMaxNum) {
				resp.setResult(ErrorCode.VIP_BUY_ERROR_1);
				return resp;
			}
			
			// 计算金币是否足够
			int cost = 0;
			for (int i = buyNum + 1; i <= buyNum + num; i++) {
				if (xmlInfo.fetchBuyPriceByNum(i) == null) {
					continue;
				}
				
				cost += xmlInfo.fetchBuyPriceByNum(i);
			}
			
			// 检测金子是否足够
			List<AbstractConditionCheck> conds = new ArrayList<AbstractConditionCheck>();
			conds.add(new CoinCond(cost));
			int check = AbstractConditionCheck.checkCondition(roleInfo, conds);
			if (check != 1) {
				resp.setResult(check);
				return resp;
			}
			
			// 判断背包
			int result = ItemService.addItemAndEquipCheck(roleInfo);
			//背包格子上限判断
			if (result != 1) {
				resp.setResult(ErrorCode.VIP_BUY_ERROR_2);
				return resp;
			}
			
			// 物品掉落
			List<DropInfo> itemList = new ArrayList<DropInfo>();
			// 道具
			itemList.add(new DropInfo(xmlInfo.getItemId(), num));
			
			// 扣除金币
			if (cost > 0) {
				if (!RoleService.subRoleRoleResource(ActionType.action436.getType(), roleInfo, ConditionType.TYPE_COIN, cost,new DropInfo(xmlInfo.getItemId(), num) )) {
					resp.setResult(ErrorCode.ROLE_COIN_ERROR_1);
					return resp;
				}
				
				SceneService.sendRoleRefreshMsg(roleId, SceneService.REFESH_TYPE_ROLE, "");
			}
			
			
			
			// 修改缓存数据库
			map.put(no, buyNum + num);
			String newVipBuyStr = generateSaleBoxRecordStr(map);
			
			roleLoadInfo.setDrawVipLvBuyStr(newVipBuyStr);
			roleLoadInfo.setLastBuyVipLvTime(new Timestamp(System.currentTimeMillis()));
			RoleDAO.getInstance().updateVipBuyAward(roleLoadInfo);
			
			// 添加奖励
			if (itemList.size() > 0) {
				ItemService.itemAdd(ActionType.action436.getType(), roleInfo, itemList, null, null, null, null, true);
			}
			
			resp.setNo(no);
			resp.setBuyNum(buyNum + num);
		}
		resp.setResult(1);
		return resp;
	}
	
	/**
	 * 检测玩家礼包购买信息是否需要处理版本号
	 * 
	 * @param roleInfo
	 * @param isRefresh
	 */
	public static void checkRoleBoxVersionChg(RoleInfo roleInfo) {
		if (roleInfo.getRoleBoxMap() == null || roleInfo.getRoleBoxMap().isEmpty()) {
			return;
		}
		
		for (RoleBoxRecordInfo roleBox : roleInfo.getRoleBoxMap().values()) {
			ToolBoxInfo toolBoxInfo = ToolBoxMap.fetchToolBoxInfoById(roleBox.getBoxId());
			if (toolBoxInfo == null) {
				continue;
			}
			
			if (roleBox.getBoxVersion() != toolBoxInfo.getBoxVersion()) {
				dealRoleBoxVersionChg(roleInfo, toolBoxInfo);
			}
		}
	}

	/**
	 * 版本变化处理
	 * 
	 * @param roleInfo
	 * @param toolBoxXmlInfo
	 */
	public static void dealRoleBoxVersionChg(RoleInfo roleInfo, ToolBoxInfo toolBoxXmlInfo) {
		if (roleInfo.getRoleBoxMap() != null && roleInfo.getRoleBoxMap().get(toolBoxXmlInfo.getId()) != null) {
			RoleBoxRecordInfo roleBoxRecord = roleInfo.getRoleBoxMap().get(toolBoxXmlInfo.getId());
			roleBoxRecord.setBoxVersion(toolBoxXmlInfo.getBoxVersion());
			roleBoxRecord.setBuyNum(0);
			
			RoleBoxDAO.getInstance().updateRoleBoxInfo(roleBoxRecord);
		}
	}

	/**
	 * 检测礼包是否过期
	 */
	public static void checkToolBoxStartAndEnd() {
		boolean isCharge = false;
		boolean isCoin = false;
		long now = System.currentTimeMillis();
		for (ToolBoxInfo toolBoxInfo : ToolBoxMap.getBoxMapById().values()) {
			
			if (toolBoxInfo.getBoxType() == ToolBoxInfo.TYPE_BOX_CHARGE && isCharge) {
				continue;
			} else if (toolBoxInfo.getBoxType() == ToolBoxInfo.TYPE_BOX_GOLD && isCoin) {
				continue;
			}
			
			if ((now >= toolBoxInfo.getStartTime().getTime() && now - toolBoxInfo.getStartTime().getTime() <= 60000) 
					|| (now >= toolBoxInfo.getEndTime().getTime() && now - toolBoxInfo.getEndTime().getTime() <= 60000)) {
				// 开始1分钟或者过期1分钟内的礼包推送
				if (toolBoxInfo.getBoxType() == ToolBoxInfo.TYPE_BOX_CHARGE) {
					isCharge = true;
				} else {
					isCoin = true;
				}
			}
			
			if (isCharge && isCoin) {
				break;
			}
		}

		if (isCharge || isCoin) {
			int index = 0;
			RoleInfo roleInfo = null;
			for (Entry<Integer, RoleInfo> entry : RoleInfoMap.getRoleInfoEntrySet()) {
				roleInfo = entry.getValue();
				if (roleInfo == null || roleInfo.getLoginStatus() != 1) {
					continue;
				}
				synchronized (roleInfo) {
					if (isCharge) {
						SceneService.sendRoleRefreshMsg(roleInfo.getId(), SceneService.REFRESH_TYPE_CHARGE, "");
					} else if (isCoin) {
						SceneService.sendRoleRefreshMsg(roleInfo.getId(), SceneService.REFRESH_TYPE_COIN_BOX, "");
					}
				}
				
				if (index++ > 500) {
					index = 0;
					try {
						TimeUnit.MILLISECONDS.sleep(100);
					} catch (InterruptedException e) {
						logger.error("ToolBox checkToolBoxEnd Error", e);
					}
				}
			}
		}
	}

}
