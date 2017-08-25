package com.snail.webgame.game.cache;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.snail.webgame.engine.common.Flag;
import com.snail.webgame.game.common.GameValue;
import com.snail.webgame.game.common.RoleLoginQueueInfo;
import com.snail.webgame.game.protocal.rolemgt.service.RoleService;

public class RoleLoginQueueInfoMap {
	// 所有排队的人，不包含可登陆就绪状态的人
	private static List<RoleLoginQueueInfo> list = new ArrayList<RoleLoginQueueInfo>();

	// 已经排进去了的人，不包含进入角色的（进入角色后，会清除list和loginSet）
	private static Set<String> loginSet = new HashSet<String>();
	
	// 记录账号登出时间,登陆进去之后清空
	private static Map<String, Long> userAccountLogoutTimeMap = new ConcurrentHashMap<String, Long>();

	/**
	 * 添加队列信息
	 * 
	 * @param info
	 */
	public static void addQueueInfo(RoleLoginQueueInfo info) {
		synchronized (Flag.OBJ_LOGIN_QUEUE) {
			
			if(list.contains(info)){
				list.remove(info);
			}
			list.add(info);
		}
	}

	/**
	 * 获取自己的队列位置
	 * 
	 * @param account
	 * @return
	 */
	public static int getIndex(String account) {
		account=account.trim().toUpperCase();
		return list.indexOf(new RoleLoginQueueInfo(account, null)) + 1;
	}

	/**
	 * 移除排队信息
	 * 
	 * @param account
	 */
	public static void removeQueueInfo(String account) {
		if(account != null){
			account=account.trim().toUpperCase();
			synchronized (Flag.OBJ_LOGIN_QUEUE) {
				list.remove(new RoleLoginQueueInfo(account, null));
				loginSet.remove(account);
			}
		}
	}
	
	/**
	 * 移除排队第一个人的信息
	 * 
	 * @param account
	 */
	public static RoleLoginQueueInfo removeFirst() {
		synchronized (Flag.OBJ_LOGIN_QUEUE) {
			if(list.size() > 0){
				return list.remove(0);
			}
		}
		return null;
	}

	public static int getListSize() {
		return list.size();
	}

	public static void removeAll() {
		list.clear();
		loginSet.clear();
	}

	public static void addLoginList(String account) {
		synchronized (Flag.OBJ_LOGIN_QUEUE) {
			account=account.trim().toUpperCase();
			loginSet.add(account);
		}
	}

	public static boolean isMessageLogin(String account) {
		account=account.trim().toUpperCase();
		return loginSet.contains(account);
	}

	public static void removeMessageAccount(String account) {
		synchronized (Flag.OBJ_LOGIN_QUEUE) {
			account=account.trim().toUpperCase();
			loginSet.remove(account);
		}
	}
	
	/**
	 * 已经排进去了的人，不包含进入角色的（进入角色后，会清除list和loginList）
	 * @return
	 */
	public static int getLoginSetNum(){
		return loginSet.size();
	}
	
	public static int getPermitNum(){
		int permitNum = GameValue.GAME_LOGIN_ONLINE_NUM - RoleInfoMap.getOnlineSize() - loginSet.size();
		return permitNum - getAccountNotTimeoutNum();
	}

	public static void changeAccountLogoutTime(String chargeAccount){
		userAccountLogoutTimeMap.put(chargeAccount.toUpperCase(), System.currentTimeMillis());
	}
	
	public static Long getAccountLogoutTime(String chargeAcc){
		if(userAccountLogoutTimeMap.containsKey(chargeAcc.toUpperCase()))
		{
			return userAccountLogoutTimeMap.get(chargeAcc.toUpperCase());
		}
		
		return (long) 0;
	}
	
	public static void removeUserAccountLogout(String chargeAcc){
		if(chargeAcc != null){
			userAccountLogoutTimeMap.remove(chargeAcc);
		}
	}
	
	public static int getAccountNotTimeoutNum(){
		int num = 0;
		for(String account : userAccountLogoutTimeMap.keySet()){
			Long lastLogoutTime = userAccountLogoutTimeMap.get(account);
			
			if(lastLogoutTime > 0){
				boolean isTimeOut = RoleService.isLogoutTimeOut(lastLogoutTime);// 是否下线太久超时  true-太久，超过6分钟
				
				// 短时间下线
				if(!isTimeOut){
					num++;
				}
			}
		}
		return num;
	}
}
