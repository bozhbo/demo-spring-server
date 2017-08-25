package com.snail.webgame.game.protocal.checkIn.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.game.common.ActionType;
import com.snail.webgame.game.common.ErrorCode;
import com.snail.webgame.game.common.GameValue;
import com.snail.webgame.game.common.util.DateUtil;
import com.snail.webgame.game.core.SceneService;
import com.snail.webgame.game.dao.RoleDAO;
import com.snail.webgame.game.info.RoleInfo;
import com.snail.webgame.game.info.RoleLoadInfo;
import com.snail.webgame.game.protocal.checkIn7Day.queryList.QueryCheckIn7DayListItemRe;
import com.snail.webgame.game.protocal.fight.fightend.BattlePrize;
import com.snail.webgame.game.protocal.item.service.ItemService;
import com.snail.webgame.game.protocal.redPoint.service.RedPointMgtService;
import com.snail.webgame.game.xml.cache.LoginActiviryXmlMap;
import com.snail.webgame.game.xml.cache.PropBagXMLMap;
import com.snail.webgame.game.xml.info.DropXMLInfo;
import com.snail.webgame.game.xml.info.LoginActiveXMLInfo;
import com.snail.webgame.game.xml.info.LoginActiveXMLItemInfo;

public class CheckInService {

	private static RoleDAO roleDAO = RoleDAO.getInstance();
	private static final Logger logger = LoggerFactory.getLogger("logs");

	/**
	 * 生成已签过的天数map
	 * 
	 * @param checkInStr
	 * @return
	 */
	public static Map<Integer,Integer> generateCheckedMap(String checkInStr) {
		Map<Integer,Integer> tempMap = new HashMap<Integer, Integer>();
		if (checkInStr == null || "".equals(checkInStr.trim())) {
			return tempMap;
		}
		
		String[] tempArr = checkInStr.split(",");
		for (String str : tempArr) {
			tempMap.put(Integer.valueOf(str), 1);
		}
		
		return tempMap;
	}
	
	/**
	 * 生成已签到字符串
	 * 
	 * @param checkedMap
	 * @return
	 */
	public static String generateNewCheckedStr(Map<Integer,Integer> checkedMap) {
		if (checkedMap == null || checkedMap.isEmpty()) {
			return null;
		}
		
		StringBuilder sb = new StringBuilder();
		for (int checkNo : checkedMap.keySet()) {
			sb.append(checkNo).append(",");
		}
		
		return sb.deleteCharAt(sb.length() - 1).toString();
	}

	/**
	 * 更新30日签到第几日(礼包)
	 * 
	 * @param roleInfo
	 * @param isRefresh 是否刷新推送
	 */
	public static void dealCheckInSync(RoleInfo roleInfo, boolean isRefresh) {
		RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
		// 功能还未开启
		if (roleLoadInfo == null || roleLoadInfo.getLastCheckInTime() == null) {
			return;
		}
		
		// 判断今天是否已计算过了
		long now = System.currentTimeMillis();
		if (DateUtil.isSameDay(roleLoadInfo.getLastCheckInTime().getTime(), now)) {
			return;
		}
		
		// 计算当前签到天数
		// 计算离上次签到过去了几天
		int distDay = DateUtil.dayNumBetweenTowTime(roleLoadInfo.getLastCheckInTime().getTime(), now);
		if (distDay > 0) {
			
			boolean isReset = false;
			int currCheckDay = roleLoadInfo.getCurrCheckDay() + distDay;
			if (currCheckDay > 30) {
				// 超过月末就重置
				currCheckDay = 1;
				roleLoadInfo.setCheckInStr("");
				roleLoadInfo.setVipCheckInStr("");
				
				isReset = true;
			}
			
			roleLoadInfo.setCurrCheckDay(currCheckDay);
			roleLoadInfo.setLastCheckInTime(new Timestamp(now));
			
			if (isReset) {
				// 数据重置更新数据库 (平时天数改变不记库,只变缓存,领奖操作时才改变库)
				RoleDAO.getInstance().updateRoleCheckIn(roleLoadInfo);
			}
			
			if (isRefresh) {
				// 签到礼包
				SceneService.sendRoleRefreshMsg(roleInfo.getId(), SceneService.REFRESH_TYPE_CHECKIN, "");
				
				// 红点触发检测
				RedPointMgtService.check2PopRedPoint(roleInfo.getId(), null, true, GameValue.RED_POINT_TYPE_CHECK_MONTH);
			}
		}
	}

	/**
	 * 获取7日连续签到活动信息
	 * @param roleInfo
	 * @return
	 */
	public static List<QueryCheckIn7DayListItemRe> getCheckIn7DayListItem(RoleInfo roleInfo) {
		RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
		if(roleLoadInfo == null){
			return null;
		}
		LoginActiveXMLInfo loginActiveXMLInfo = LoginActiviryXmlMap.getXmlInfo(1);// 写死是1
		if (loginActiveXMLInfo == null) {
			return null;
		}
		long currentTimeMilles = System.currentTimeMillis();
		// 判断开始时间
		if (loginActiveXMLInfo.getStartTime() != 0 && loginActiveXMLInfo.getStartTime() > currentTimeMilles) {
			return null;
		}
		// 判断结束时间
		if (loginActiveXMLInfo.getEndTime() != 0 && loginActiveXMLInfo.getEndTime() < currentTimeMilles) {
			return null;
		}
		if (loginActiveXMLInfo.getActiveXMLItemInfos() == null
				|| loginActiveXMLInfo.getActiveXMLItemInfos().size() == 0) {
			return null;
		}
		// 签到数据为空则初始化
		if (StringUtils.isBlank(roleLoadInfo.getCheckIn7DayNum())) {
			StringBuffer checkIn7DayNumInitData = new StringBuffer("0");
			for (int i = 1; i < loginActiveXMLInfo.getActiveXMLItemInfos().size(); i++) {
				checkIn7DayNumInitData.append(",0");
			}
			if (!roleDAO.role7CheckIn(roleInfo.getId(), checkIn7DayNumInitData.toString(), roleLoadInfo
					.getCheckIn7DayMaxLoginDays(), roleLoadInfo.getCheckIn7DayCurrentLoginDays(), roleLoadInfo
					.getCheckIn7DayTime().getTime())) {
				return null;
			}
			roleLoadInfo.setCheckIn7DayNum(checkIn7DayNumInitData.toString());
		}
		// 判断是否都领取完
		boolean canShow = false; // 能否显示
		String[] checkIn7DaysNum = roleLoadInfo.getCheckIn7DayNum().split(",");
		for (int i = 0; i < loginActiveXMLInfo.getActiveXMLItemInfos().size(); i++) {
			if (checkIn7DaysNum[i].equals("0")) {
				canShow = true;
				break;
			}
		}
		if (!canShow) {
			return null;
		}
		// 返回给客户端的list
		List<QueryCheckIn7DayListItemRe> checkIn7DayListItemRes = new ArrayList<QueryCheckIn7DayListItemRe>();
		QueryCheckIn7DayListItemRe queryCheckIn7DayListItemRe = null;
		String itemInfo = null;
		for (LoginActiveXMLItemInfo loginActiveXMLItemInfo : loginActiveXMLInfo.getActiveXMLItemInfos()) {
			// 奖励物品信息
			if (loginActiveXMLItemInfo.getItems() == null || loginActiveXMLItemInfo.getItems().length == 0) {
				continue;
			}
			itemInfo = loginActiveXMLItemInfo.getItems()[0];// 暂时只取一个
			if (StringUtils.isBlank(itemInfo)) {
				continue;
			}
			String[] itemInfos = itemInfo.split(",");
			if (itemInfos.length != 2) {
				continue;
			}
			
			queryCheckIn7DayListItemRe = new QueryCheckIn7DayListItemRe();
			queryCheckIn7DayListItemRe.setNo(loginActiveXMLItemInfo.getNo());
			queryCheckIn7DayListItemRe.setDay(loginActiveXMLItemInfo.getNeedNum());
			queryCheckIn7DayListItemRe.setItemNo(itemInfos[0]);
			queryCheckIn7DayListItemRe.setItemNum(Integer.parseInt(itemInfos[1]));
			int index = loginActiveXMLInfo.getActiveXMLItemInfos().indexOf(loginActiveXMLItemInfo);
			if(checkIn7DaysNum.length > index){
				queryCheckIn7DayListItemRe.setCheckIn(new Byte(checkIn7DaysNum[index]));
			}
			checkIn7DayListItemRes.add(queryCheckIn7DayListItemRe);
		}
		return checkIn7DayListItemRes;
	}

	/**
	 * 更新登录游戏天数
	 * @param roleInfo
	 * @param isRefresh
	 */
	public static void plusOneDayForCheckIn7Days(RoleInfo roleInfo, boolean isRefresh) {
		RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
		if(roleLoadInfo == null){
			return;
		}
		byte currentLoginDays = roleLoadInfo.getCheckIn7DayCurrentLoginDays();
		byte maxLoginDays = roleLoadInfo.getCheckIn7DayMaxLoginDays();
		Calendar now = Calendar.getInstance();
		Calendar record = Calendar.getInstance();
		if (roleLoadInfo.getCheckIn7DayTime() != null) {
			record.setTimeInMillis(roleLoadInfo.getCheckIn7DayTime().getTime());
		} else {
			record.add(Calendar.DAY_OF_YEAR, -1);
		}

		// 记录与登陆为同一天则不更新
		if (now.get(Calendar.YEAR) == record.get(Calendar.YEAR)
				&& now.get(Calendar.DAY_OF_YEAR) == record.get(Calendar.DAY_OF_YEAR)) {
			return;
		}
		// 增加一天判断是否为同一天
		currentLoginDays++;
		
		if (currentLoginDays > maxLoginDays) {
			maxLoginDays = currentLoginDays;
		}

		// 数据更新
		if (!roleDAO.role7CheckIn(roleInfo.getId(), roleLoadInfo.getCheckIn7DayNum(), maxLoginDays, currentLoginDays,
				now.getTimeInMillis())) {
			logger.error("CheckInService.plusOneDayForCheckIn7Days()中数据库更新异常");
			return;
		}
		roleLoadInfo.setCheckIn7DayCurrentLoginDays(currentLoginDays);
		roleLoadInfo.setCheckIn7DayMaxLoginDays(maxLoginDays);
		roleLoadInfo.setCheckIn7DayTime(new Timestamp(now.getTimeInMillis()));
		
		if (isRefresh) {
			// 开服礼包
			SceneService.sendRoleRefreshMsg(roleInfo.getId(), SceneService.REFRESH_TYPE_CHECKIN_7DAY, "");
			
			RedPointMgtService.check2PopRedPoint(roleInfo.getId(), null, true, GameValue.RED_POINT_TYPE_CHECK_WEEK);
		}
	}

	/**
	 * 获取7日签到奖励
	 * @param roleInfo
	 * @param no
	 * @return
	 */
	public static int checkInReward(RoleInfo roleInfo, int no) {
		/**
		 * 步骤： 1、获取编号对应的配置 2、判断活动是否开始 3、判断天是否<=最大连续登陆天数 4、判断是否已领取 5、修改领取标识
		 * 6、发放奖励
		 */
		RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
		if(roleLoadInfo == null){
			return ErrorCode.QUEST_ERROR_15;
		}
		// -----------------1、获取编号对应的配置----------------
		LoginActiveXMLInfo loginActiveXMLInfo = LoginActiviryXmlMap.getXmlInfo(1);
		if (loginActiveXMLInfo == null) {
			return ErrorCode.QUEST_ERROR_15;
		}
		// -----------------2、判断活动是否开始----------------
		long currentTimeMilles = System.currentTimeMillis();
		// 判断开始时间
		if (loginActiveXMLInfo.getStartTime() != 0 && loginActiveXMLInfo.getStartTime() > currentTimeMilles) {
			logger.error("活动没开始");
			return ErrorCode.QUEST_ERROR_16;
		}
		// 判断结束时间
		if (loginActiveXMLInfo.getEndTime() != 0 && loginActiveXMLInfo.getEndTime() < currentTimeMilles) {
			logger.error("活动没开始");
			return ErrorCode.QUEST_ERROR_17;
		}
		if (loginActiveXMLInfo.getActiveXMLItemInfos() == null
				|| loginActiveXMLInfo.getActiveXMLItemInfos().size() == 0) {
			return ErrorCode.QUEST_ERROR_18;
		}
		
		// ----------------3、判断天是否<=最大登陆天数-------------
		for (LoginActiveXMLItemInfo loginActiveXMLItemInfo : loginActiveXMLInfo.getActiveXMLItemInfos()) {
			if (loginActiveXMLItemInfo.getNo() != no) {
				continue;
			}
			if (loginActiveXMLItemInfo.getNeedNum() > roleLoadInfo.getCheckIn7DayMaxLoginDays()) {
				logger.error("登陆天数不够");
				return ErrorCode.QUEST_ERROR_19;
			}
			// -----------------4、判断是否已领取-----------------
			int index = loginActiveXMLInfo.getActiveXMLItemInfos().indexOf(loginActiveXMLItemInfo);
			String[] checkIn7DayNum = roleLoadInfo.getCheckIn7DayNum().split(",");
			if (checkIn7DayNum[index].equals("1")) {
				logger.error("已领取");
				return ErrorCode.QUEST_ERROR_20;
			}
			// ---------------------5、修改领取标识-------------------
			checkIn7DayNum[index] = "1";
			StringBuffer newCcheckIn7DayNum = new StringBuffer(roleLoadInfo.getCheckIn7DayNum().length());
			for (int i = 0; i < checkIn7DayNum.length; i++) {
				if (i != 0) {
					newCcheckIn7DayNum.append(",");
				}
				newCcheckIn7DayNum.append(checkIn7DayNum[i]);
			}
			if (!roleDAO.role7CheckIn(roleInfo.getId(), newCcheckIn7DayNum.toString(), roleLoadInfo
					.getCheckIn7DayMaxLoginDays(), roleLoadInfo.getCheckIn7DayCurrentLoginDays(), roleLoadInfo
					.getCheckIn7DayTime().getTime())) {
				return ErrorCode.QUEST_ERROR_21;
			}
			roleLoadInfo.setCheckIn7DayNum(newCcheckIn7DayNum.toString());
			// ---------------------6、发放奖励---------------------
			List<DropXMLInfo> list = new ArrayList<DropXMLInfo>();
			for (String itemInfo : loginActiveXMLItemInfo.getItems()) {
				list.addAll(PropBagXMLMap.getPropBagXMLListbyStr(itemInfo));
			}
			List<BattlePrize> dropList = new ArrayList<BattlePrize>();
			ItemService.addPrizeForPropBag(ActionType.action479.getType(),roleInfo,list,null,
					dropList,null,null,null,false);

			return 1;
		}
		return ErrorCode.QUEST_ERROR_22;
	}
}
