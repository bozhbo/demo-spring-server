package com.snail.webgame.game.protocal.scene.sceneRefre;

import org.epilot.ccf.core.protocol.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.game.cache.RoleInfoMap;
import com.snail.webgame.game.common.GameMessageHead;
import com.snail.webgame.game.common.util.Command;
import com.snail.webgame.game.info.RoleInfo;
import com.snail.webgame.game.protocal.mine.query.QueryMineResp;
import com.snail.webgame.game.protocal.mine.scene.QuerySceneMineResp;
import com.snail.webgame.game.protocal.mine.service.MineMgtService;

public class SceneRefreService {

	private static final Logger logger = LoggerFactory.getLogger("logs");

	/**
	 * 刷新类型
	 */
	public static final int REFRESH_TYPE_SCENE = 16;// 场景刷新
	public static final int QUERY_TYPE_MINE = 17;// 查询场景矿石
	public static final int REFRESH_TYPE_MINE = 18;// 场景矿石刷新
	public static final int REFRESH_TYPE_ROLE_MINE = 19;// 角色矿信息刷新

	/**
	 * 通知客户端刷新场景
	 * @param roleId 角色ID
	 * @param type 刷新类型
	 * @param reserve
	 */
	public static void sendRoleRefreshMsg(int roleId, int type, Object reserve) {
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		//  1-在线
		if (roleInfo != null && roleInfo.getLoginStatus() == 1) {
			if(roleInfo.getDisconnectPhase() == 1){
				// 1-临时断开
				return;
			}			
			SceneRefreRunnable r = new SceneRefreRunnable(roleInfo, type, reserve);
			if (SceneRefreThread.getInstance().isCanAdd()) {
				SceneRefreThread.getInstance().run(r);
			} else {
				if (logger.isWarnEnabled()) {
					logger.warn("Notify the client to refresh the thread queue is full......" + "roleId=" + roleId
							+ ",type=" + type);
				}
			}
		}
	}

	/**
	 * 查询场景矿石
	 * @param roleId
	 * @return
	 */
	public static Message notifyType_17(int roleId) {
		MineMgtService service = new MineMgtService();
		QuerySceneMineResp resp = service.querySceneMine(null);
		Message message = new Message();
		GameMessageHead head = new GameMessageHead();
		head.setMsgType(Command.QUERY_SCENE_MINE_RESP);
		head.setUserID0((int) roleId);
		message.setHeader(head);
		message.setBody(resp);
		return message;
	}

	/**
	 * 场景矿石刷新
	 * @param roleId
	 * @return
	 */
	public static Message notifyType_18(int roleId, String reserve) {
		MineMgtService service = new MineMgtService();
		QuerySceneMineResp resp = service.querySceneMine(reserve);
		Message message = new Message();
		GameMessageHead head = new GameMessageHead();
		head.setMsgType(Command.REFRESH_SCENE_MINE_RESP);
		head.setUserID0((int) roleId);
		message.setHeader(head);
		message.setBody(resp);
		return message;
	}
	
	/**
	 * 场景矿石刷新
	 * @param roleId
	 * @return
	 */
	public static Message notifyType_19(int roleId, QueryMineResp resp) {
		Message message = new Message();
		GameMessageHead head = new GameMessageHead();
		head.setMsgType(Command.REFRESH_ROLE_MINE_RESP);
		head.setUserID0((int) roleId);
		message.setHeader(head);
		message.setBody(resp);
		return message;
	}

}
