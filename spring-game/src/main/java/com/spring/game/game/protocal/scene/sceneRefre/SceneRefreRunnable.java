package com.snail.webgame.game.protocal.scene.sceneRefre;

import org.apache.mina.common.IoSession;
import org.epilot.ccf.config.Resource;
import org.epilot.ccf.core.protocol.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.engine.common.ServerName;
import com.snail.webgame.game.cache.HeroInfoMap;
import com.snail.webgame.game.cache.ServerMap;
import com.snail.webgame.game.info.RoleInfo;
import com.snail.webgame.game.protocal.mine.query.QueryMineResp;
import com.snail.webgame.game.protocal.rolemgt.service.RoleService;
import com.snail.webgame.game.xml.cache.FuncOpenXMLInfoMap;
import com.snail.webgame.game.xml.info.FuncOpenXMLInfo;

/**
 * 场景刷新
 * @author hongfm
 * @date 2015-1-29
 */
public class SceneRefreRunnable implements Runnable {

	private static final Logger logger = LoggerFactory.getLogger("logs");

	private RoleInfo roleInfo;
	private Object reserve;
	private int type;

	public SceneRefreRunnable(RoleInfo roleInfo, int type, Object reserve) {
		this.roleInfo = roleInfo;
		this.reserve = reserve;
		this.type = type;
	}

	public void run() {
		IoSession session = ServerMap.getServerSession(ServerName.GATE_SERVER_NAME + "-" + roleInfo.getGateServerId());
		int roleId = roleInfo.getId();
		if (session != null && session.isConnected()) {
			switch (type) {
			case SceneRefreService.REFRESH_TYPE_SCENE:
				session.write((Message) reserve);
				break;
			case SceneRefreService.QUERY_TYPE_MINE:
				if (FuncOpenXMLInfoMap.getFuncOpenHeroLevel(FuncOpenXMLInfo.MINE_NO) <= HeroInfoMap.getMainHeroLv(roleId)) {
					session.write(SceneRefreService.notifyType_17(roleId));
				}
				break;
			case SceneRefreService.REFRESH_TYPE_MINE:
				if (!RoleService.checkRoleInFight(roleInfo)) {
					session.write(SceneRefreService.notifyType_18(roleId, (String) reserve));
				}
				break;
			case SceneRefreService.REFRESH_TYPE_ROLE_MINE:
				if (!RoleService.checkRoleInFight(roleInfo)) {
					session.write(SceneRefreService.notifyType_19(roleId, (QueryMineResp) reserve));
				}
				break;
			default:
				break;
			}
			if (logger.isInfoEnabled() && type != SceneRefreService.REFRESH_TYPE_SCENE) {
				logger.info(Resource.getMessage("game", "GAME_COMMON_INFO_1") + ":roleId=" + roleId + ",type=" + type);
			}
		}
	}
}
