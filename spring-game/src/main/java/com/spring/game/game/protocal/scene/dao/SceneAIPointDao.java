package com.snail.webgame.game.protocal.scene.dao;

import com.snail.webgame.engine.db.SqlMapDaoSupport;
import com.snail.webgame.game.dao.base.DbConstants;
import com.snail.webgame.game.protocal.scene.info.RolePoint;

public class SceneAIPointDao extends SqlMapDaoSupport{
	
	/**
	 * 更新玩家场景中坐标
	 * @param rolePoint
	 * @return
	 */
	public boolean updateSceneAIPoint(RolePoint rolePoint)
	{
		if(rolePoint.getId() == 0)
		{
			return getSqlMapClient(DbConstants.GAME_DB).insert("insertSceneAIPoint", rolePoint);
		}
		else
		{
			return getSqlMapClient(DbConstants.GAME_DB).update("updateSceneAIPoint", rolePoint);
		}
	}

}
