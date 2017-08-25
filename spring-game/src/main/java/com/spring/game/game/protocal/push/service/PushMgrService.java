package com.snail.webgame.game.protocal.push.service;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.engine.component.push.ClientType;
import com.snail.webgame.engine.component.push.PushObj;
import com.snail.webgame.game.cache.RoleInfoMap;
import com.snail.webgame.game.common.ErrorCode;
import com.snail.webgame.game.dao.RoleDAO;
import com.snail.webgame.game.info.RoleInfo;
import com.snail.webgame.game.info.RoleLoadInfo;
import com.snail.webgame.game.protocal.opactivity.service.OpActivityMgrService;
import com.snail.webgame.game.protocal.push.hit.HitPushInfoResp;
import com.snail.webgame.game.protocal.push.query.QueryPushInfoResp;
import com.snail.webgame.game.thread.PushServerSendMsgThread;
import com.snail.webgame.game.xml.cache.PushXMLInfoMap;
import com.snail.webgame.game.xml.info.PushXMLInfo;

public class PushMgrService {
	private static final Logger logger = LoggerFactory.getLogger("logs");
	
	/**
	 * 查询角色推送信息
	 * 
	 * @param roleId
	 * @return
	 */
	public QueryPushInfoResp queryPushInfo(int roleId) {
		QueryPushInfoResp resp = new QueryPushInfoResp();
		
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
			
			if (roleInfo.getOfflinePushStr() != null) {
				resp.setRolePushStr(roleInfo.getOfflinePushStr());
			}
		}

		resp.setResult(1);
		return resp;
	}

	/**
	 * 设置角色推送状态
	 * 
	 * @param roleId
	 * @param no
	 * @param state
	 * @return
	 */
	public HitPushInfoResp hitPushInfo(int roleId, int no, byte state) {
		HitPushInfoResp resp = new HitPushInfoResp();
		
		if (state != 1 && state != 2) {
			resp.setResult(ErrorCode.ROLE_NOT_EXIST_ERROR_15);
			return resp;
		}
		
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			resp.setResult(ErrorCode.ROLE_NOT_EXIST_ERROR_15);
			return resp;
		}
		
		// 是否是服务器推送类型
		PushXMLInfo pushXMLInfo = PushXMLInfoMap.fetchPushXMLInfo(no);
		if (pushXMLInfo == null || pushXMLInfo.getPushType() != 3) {
			resp.setResult(ErrorCode.ROLE_NOT_EXIST_ERROR_9);
			return resp;
		}
		synchronized (roleInfo) {
			RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
			if (roleLoadInfo == null) {
				resp.setResult(ErrorCode.ROLE_NOT_EXIST_ERROR_9);
				return resp;
			}
			
			resp.setNo(no);
			resp.setState(state);
			resp.setResult(1);
			
			// 服务器记录了哪些不推送的编号
			Map<Integer, Integer> map = OpActivityMgrService.generateRewardRecordMap(roleInfo.getOfflinePushStr());
			if (state == 1) {
				// 推送(勾选)
				if (!map.containsKey(no)) {
					return resp;
				}
				
				map.remove(no);
			} else {
				// 不推送(去勾)
				if (map.containsKey(no)) {
					return resp;
				}
				
				map.put(no, 1);
			}
			
			String rolePushStr = generateStrByMap(map);
			roleInfo.setOfflinePushStr(rolePushStr);
			RoleDAO.getInstance().updateRolePush(roleInfo);
		}
		return resp;
	}
	
	public static String generateStrByMap(Map<Integer, Integer> map) {
		if (map == null || map.isEmpty()) {
			return null;
		}
		
		StringBuilder sb = new StringBuilder();
		for (int key : map.keySet()) {
			sb.append(key).append(",");
		}
		
		if (sb.length() > 0) {
			sb.deleteCharAt(sb.length() - 1);
		}
		
		return sb.toString();
	}
	
	/**
	 * 离线推送处理
	 * 
	 * @param roleInfo
	 * @param pushNo
	 */
	public static void dealOfflinePush(RoleInfo roleInfo, int pushNo) {
		if (logger.isInfoEnabled()) {
			logger.info("[---- server offline push roleId : " + roleInfo.getId() + " ,roleName : " + roleInfo.getRoleName() + 
					" ,loginState : " + roleInfo.getLoginStatus() + " ,clientType : " + roleInfo.getClientType() + " ,packageName : " + 
					roleInfo.getPackageName() + " ,mac : " + roleInfo.getMac() + " ,pushNo : " + pushNo + "------]");
		}
		
		if (roleInfo.getLoginStatus() == 1) {
			// 玩家在线不推送
			return;
		}
		
		if (roleInfo.getPackageName() == null || "".equals(roleInfo.getPackageName().trim())
				|| roleInfo.getMac() == null || "".equals(roleInfo.getMac().trim())) {
			return;
		}
		
		PushXMLInfo pushXMLInfo = PushXMLInfoMap.fetchPushXMLInfo(pushNo);
		if (pushXMLInfo == null) {
			return;
		}
		
		if (isHitPush(roleInfo, pushNo)) {
			sendPushMessage(roleInfo, pushXMLInfo.getTitle(), pushXMLInfo.getContent());
		}
	}
	
	/**
	 * 是否勾选推送
	 * 
	 * @param roleInfo
	 * @param pushNo
	 * @return
	 */
	private static boolean isHitPush(RoleInfo roleInfo, int pushNo) {
		Map<Integer, Integer> map = OpActivityMgrService.generateRewardRecordMap(roleInfo.getOfflinePushStr());
		if (!map.containsKey(pushNo)) {
			// 服务器未记录，则需要推送
			return true;
		}
		
		return false;
	}
	
	/**
	 * 发送消息推送
	 * 
	 * @param roleInfo
	 * @param title
	 * @param message
	 */
	public static void sendPushMessage(RoleInfo roleInfo, String title, String message) {
		
		PushObj obj = new PushObj();
		if (roleInfo.getClientType() == 1) {
			//android
			obj.setClient(ClientType.ANDROID);
		} else if (roleInfo.getClientType() == 2) {
			//ios
			obj.setClient(ClientType.IOS);
		}
		obj.setAppPackage(roleInfo.getPackageName());
		obj.addToken(roleInfo.getMac());
		obj.setMessage(message);
		obj.setTitle(title);
		obj.setBadge(1);
		
		PushServerSendMsgThread.addMessage(obj);
	}
	
	/**
	 * 测试用例：通知推送
	 */
	public static void testPush() {
		RoleInfo roleInfo = null;
		for(int roleId : RoleInfoMap.getMap().keySet()) {
			roleInfo = RoleInfoMap.getRoleInfo(roleId);
			if (roleInfo == null || roleInfo.getLoginStatus() == 1) {
				continue;
			}
			
			if (roleInfo.getClientType() == 0 || roleInfo.getPackageName() == null || roleInfo.getMac() == null) {
				continue;
			}
			
			dealOfflinePush(roleInfo, PushXMLInfo.PUSH_NO_MINE);
			
			System.out.println("[---------------- push sucess , acc : " + roleInfo.getAccount() + " !!!!!!!-----------------------]");
			
			try {
				TimeUnit.MILLISECONDS.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
