package com.snail.webgame.game.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.snail.webgame.game.common.GameValue;
import com.snail.webgame.game.info.ChargeAccountInfo;

public class UserAccountMap {

	// <客户端传过来的账号>
	private static Map<String, ChargeAccountInfo> userAccountMap = new ConcurrentHashMap<String, ChargeAccountInfo>();
	
	public static void addForChargeAccount(String key, ChargeAccountInfo account) {
		userAccountMap.put(key.toUpperCase(), account);

	}

	public static void cleanForChargeAccount(String account) {

		if(userAccountMap.containsKey(account.toUpperCase()))
		{
			ChargeAccountInfo info = userAccountMap.get(account.toUpperCase());
			if(info != null && System.currentTimeMillis() - info.getAddTime() > GameValue.USER_LOGOUT_TIME * 1000)
			{
				userAccountMap.remove(account.toUpperCase());
			}
		}
	}
	
	public static ChargeAccountInfo getForChargeAccount(String key) {
		ChargeAccountInfo accountInfo = userAccountMap.get(key.toUpperCase());
		return accountInfo;
	}
}
