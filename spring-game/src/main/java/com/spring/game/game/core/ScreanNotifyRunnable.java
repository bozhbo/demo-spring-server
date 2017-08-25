package com.snail.webgame.game.core;

import org.apache.mina.common.IoSession;
import org.epilot.ccf.config.Resource;
import org.epilot.ccf.core.protocol.MessageBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.engine.common.ServerName;
import com.snail.webgame.game.cache.ServerMap;
import com.snail.webgame.game.info.RoleInfo;

/**
 * 给客户端刷新信息线程类
 * @author wangxf
 * @date 2013-3-20
 */
public class ScreanNotifyRunnable implements Runnable {

	private static final Logger logger = LoggerFactory.getLogger("logs");

	private RoleInfo roleInfo;
	private Object reserve;
	private MessageBody messageBody;
	private int type;
	private int messageType;

	public ScreanNotifyRunnable(RoleInfo roleInfo, int type, Object reserve) {
		this.roleInfo = roleInfo;
		this.reserve = reserve;
		this.type = type;
	}
	
	public ScreanNotifyRunnable(RoleInfo roleInfo, int type, int messageType, MessageBody messageBody) {
		this.roleInfo = roleInfo;
		this.messageBody = messageBody;
		this.type = type;
		this.messageType = messageType;
	}

	public void run() {
		IoSession session = ServerMap.getServerSession(ServerName.GATE_SERVER_NAME + "-" + roleInfo.getGateServerId());
		int roleId = roleInfo.getId();
		if (session != null && session.isConnected()) {
			switch (type) {
			case SceneService.REFESH_TYPE_ROLE:
				session.write(SceneService.notifyType_1(roleId));
				break;
			case SceneService.REFESH_TYPE_HERO:
				session.write(SceneService.notifyType_2(roleId, String.valueOf(reserve)));
				break;
			case SceneService.REFRESH_TYPE_GOLD_BUY:
				session.write(SceneService.notifyType_4(roleId, String.valueOf(reserve)));
				break;
			case SceneService.REFESH_TYPE_RECRUIT:
				session.write(SceneService.notifyType_5(roleId));
				break;
			case SceneService.REFESH_TYPE_EQUIP_BAG:
				session.write(SceneService.notifyType_6(roleId, reserve));
				break;
			case SceneService.REFESH_TYPE_ITEM:
				session.write(SceneService.notifyType_7(roleId, reserve));
				break;
			case SceneService.REFESH_TYPE_FIGHT_DEPLOY:
				session.write(SceneService.notifyType_10(roleId, reserve));
				break;
			case SceneService.REFESH_TYPE_QUEST:
				session.write(SceneService.notifyType_11(roleId, (String) reserve));
				break;
			case SceneService.REFRESH_TYPE_CHALLENGE_BATTLE:
				session.write(SceneService.notifyType_13(roleId, reserve));
				break;
			case SceneService.REFRESH_TYPE_STORE_ITEM:
				session.write(SceneService.notifyType_15(roleId, reserve));
				break;
			case SceneService.REFRESH_TYPE_GEM:
				session.write(SceneService.notifyType_17(roleId));
				break;
			case SceneService.REFRESH_TYPE_CAMPAIGN:
				session.write(SceneService.notifyType_12(roleId));
				break;
			case SceneService.REFRESH_TYPE_CAMPAIGN_HERO:
				session.write(SceneService.notifyType_18(roleId, (String) reserve));
				break;
			case SceneService.REFRESH_TYPE_DEPLOY:
				session.write(SceneService.notifyType_19(roleId));
				break;
			case SceneService.REFRESH_TYPE_SP:
				session.write(SceneService.notifyType_20(roleId, (String) reserve));
				break;
			case SceneService.REFRESH_TYPE_FUNCOPEN:
				session.write(SceneService.notifyType_21(roleId, (String) reserve));
				break;
			case SceneService.REFRESH_TYPE_WEAPON:
				session.write(SceneService.notifyType_22(roleId, reserve));
				break;
			case SceneService.REFRESH_TYPE_ENERGY:
				session.write(SceneService.notifyType_23(roleId,(String) reserve));
				break;
			case SceneService.REFRESH_TYPE_GUIDE:
				session.write(SceneService.notifyType_24(roleId));
				break;
			case SceneService.REFRESH_TYPE_ADD_FRIEND_REQUEST:
				session.write(SceneService.notifyType_26(roleId, reserve));
				break;
			case SceneService.REFRESH_TYPE_FRIEND_LIST:
				session.write(SceneService.notifyType_27(roleId, reserve));
				break;
			case SceneService.REFRESH_TYPE_REMOVE_FRIEND:
				session.write(SceneService.notifyType_28(roleId, reserve));
				break;
			case SceneService.REFRESH_TYPE_PRESENT_ENERGY:
				session.write(SceneService.notifyType_29(roleId, reserve));
				break;
			case SceneService.REFRESH_TYPE_COMMON:
				session.write(SceneService.notifyType_99(roleId, this.messageType, this.messageBody));
				break;
			case SceneService.REFRESH_TYPE_TECH:
				session.write(SceneService.notifyType_30(roleId,(String) reserve));
				break;
			case SceneService.REFRESH_TYPE_CHARGE:
				session.write(SceneService.notifyType_31(roleId));
				break;
			case SceneService.REFRESH_TYPE_COIN_BOX:
				session.write(SceneService.notifyType_32(roleId));
				break;
			case SceneService.REFRESH_TYPE_OPACT:
				session.write(SceneService.notifyType_33(roleId, (String) reserve));
				break;
			case SceneService.REFRESH_TYPE_FIRST:
				session.write(SceneService.notifyType_34(roleId));
				break;
			case SceneService.REFRESH_TYPE_SEVEN:
				session.write(SceneService.notifyType_35(roleId));
				break;
			case SceneService.REFRESH_TYPE_WONDER:
				session.write(SceneService.notifyType_36(roleId));
				break;
			case SceneService.REFRESH_TYPE_VIP_BUY:
				session.write(SceneService.notifyType_37(roleId));
				break;
			case SceneService.REFRESH_TYPE_CHECKIN_7DAY:
				session.write(SceneService.notifyType_38(roleId));
				break;		
			case SceneService.REFRESH_TYPE_CHECKIN:
				session.write(SceneService.notifyType_39(roleId));
				break;
			case SceneService.REFRESH_TYPE_SYSTEM_NOTIFY:
				session.write(SceneService.notifyType_40(roleId));
				break;
			default:
				break;
			}
			if (logger.isInfoEnabled()) {
				logger.info(Resource.getMessage("game", "GAME_COMMON_INFO_1") + ":roleId=" + roleId + ",type=" + type);
			}
		}
	}
}
