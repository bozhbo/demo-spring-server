package com.snail.webgame.engine.game.module.trade.processor.service.impl;

import java.sql.Types;

import com.snail.webgame.engine.game.base.info.msg.DummyReq;
import com.snail.webgame.engine.game.dao.BaseSuperDao;
import com.snail.webgame.engine.game.dao.SuperDaoRequest;
import com.snail.webgame.engine.game.enums.SqlType;
import com.snail.webgame.engine.game.module.trade.msg.TempBuyRoleItemResp;
import com.snail.webgame.engine.game.module.trade.processor.service.TempTradeActorService;
import com.snail.webgame.engine.game.temp.TempRoleInfo;

public class TempTradeActorServiceImpl implements TempTradeActorService {
	
	private BaseSuperDao baseSuperDao;

	public TempBuyRoleItemResp buyRoleItem(DummyReq req, TempRoleInfo buyRoleActor, TempRoleInfo sellRoleActor) {
		TempBuyRoleItemResp resp = new TempBuyRoleItemResp();
		
		SuperDaoRequest superDaoRequest1 = new SuperDaoRequest(SqlType.UPDATE, baseSuperDao.getSqlMap().get("role_step_update_star").toString(), new Object[] {10, 199}, new int[] {Types.TINYINT, Types.INTEGER});
		SuperDaoRequest superDaoRequest2 = new SuperDaoRequest(SqlType.UPDATE, baseSuperDao.getSqlMap().get("role_step_update_star").toString(), new Object[] {10, 199}, new int[] {Types.TINYINT, Types.INTEGER});
		
		try {
			baseSuperDao.execute(superDaoRequest1, superDaoRequest2);
		} catch (Exception e) {
			e.printStackTrace();
			resp.setResult(10000);
		}
		
		resp.setResult(1);
		return resp;
	}

	public void setBaseSuperDao(BaseSuperDao baseSuperDao) {
		this.baseSuperDao = baseSuperDao;
	}
}
