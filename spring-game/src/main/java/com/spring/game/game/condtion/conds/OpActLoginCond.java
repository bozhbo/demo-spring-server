package com.snail.webgame.game.condtion.conds;

import java.sql.Timestamp;

import com.snail.webgame.game.common.util.DateUtil;
import com.snail.webgame.game.condtion.AbstractConditionCheck;
import com.snail.webgame.game.condtion.CheckDataChg;
import com.snail.webgame.game.dao.OpActivityProgressDAO;
import com.snail.webgame.game.info.OpActivityProgressInfo;
import com.snail.webgame.game.info.RoleInfo;

public class OpActLoginCond extends AbstractConditionCheck {

	private int loginDay;
	
	public OpActLoginCond() {
	}

	public OpActLoginCond(int loginDay) {
		this.loginDay = loginDay;
	}

	
	@Override
	public AbstractConditionCheck generate(String[] agrs) {
		if (agrs.length < 2) {
			return null;
		}
		int loginDay = Integer.parseInt(agrs[1]);
		return new OpActLoginCond(loginDay);
	}

	@Override
	public int checkCond(RoleInfo roleInfo, int action,
			CheckDataChg checkDataChg, int index, Object obj) {
		if (checkDataChg == null || !(checkDataChg instanceof OpActivityProgressInfo)) {
			return 0;
		}
		
		int val = (int) checkDataChg.getSpecValue(index);
		if (val >= loginDay) {
			return 1;
		}
		
		OpActivityProgressInfo info = (OpActivityProgressInfo) checkDataChg;
		// 检测登录天数是否有变
		long now = System.currentTimeMillis();
		if (!DateUtil.isSameDay(info.getCheckTime().getTime(), now)) {
			info.setValue3(info.getValue3() + 1);
			info.setCheckTime(new Timestamp(now));
			
			// 
			OpActivityProgressDAO.getInstance().updateOpActProVal3(info);
		}
		
		int loginVal = (int) info.getValue3();
		if (loginVal >= loginDay) {
			updateValue(index, checkDataChg, loginDay);
			return 1;
		}
		
		if (loginVal > val) {
			updateValue(index, checkDataChg, loginVal);
		}
		
		return 0;
	}

}
