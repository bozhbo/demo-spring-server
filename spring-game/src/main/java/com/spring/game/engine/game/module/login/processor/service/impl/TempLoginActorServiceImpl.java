package com.snail.webgame.engine.game.module.login.processor.service.impl;

import java.sql.Types;

import com.snail.webgame.engine.game.dao.BaseSuperDao;
import com.snail.webgame.engine.game.module.login.msg.TempLoginReq;
import com.snail.webgame.engine.game.module.login.msg.TempLoginResp;
import com.snail.webgame.engine.game.module.login.processor.service.TempLoginActorService;
import com.snail.webgame.engine.game.temp.TempRoleInfo;

public class TempLoginActorServiceImpl implements TempLoginActorService {
	
	private BaseSuperDao baseSuperDao;
	
	public TempLoginResp login(TempLoginReq req, TempRoleInfo roleInfo) {
		TempLoginResp resp = new TempLoginResp();
		resp.setMsgType(0xA006);
		
		System.out.println("account = " + req.getAccount());
		System.out.println("md5Pass = " + req.getPass());
		
		baseSuperDao.update(baseSuperDao.getSqlMap().get("role_name_update_name"), baseSuperDao.toObjects(roleInfo.getGold(), roleInfo.getRoleId()), new int[]{Types.INTEGER, Types.BIGINT});
		
		resp.setResult(1);
		
		return resp;
	}

	public void setBaseSuperDao(BaseSuperDao baseSuperDao) {
		this.baseSuperDao = baseSuperDao;
	}
}
