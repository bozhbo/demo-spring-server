package com.snail.webgame.game.condtion.conds;

import java.sql.Timestamp;

import com.snail.webgame.game.common.ActionType;
import com.snail.webgame.game.common.util.DateUtil;
import com.snail.webgame.game.condtion.AbstractConditionCheck;
import com.snail.webgame.game.condtion.CheckDataChg;
import com.snail.webgame.game.dao.OpActivityProgressDAO;
import com.snail.webgame.game.info.OpActivityProgressInfo;
import com.snail.webgame.game.info.RoleInfo;

public class OpActPayDaysCond extends AbstractConditionCheck {
	
	private int dailyPay;// 每日充值
	private int continueDays;// 持续天数
	
	public OpActPayDaysCond() {
	}
	
	public OpActPayDaysCond(int dailyPay, int continueDays) {
		this.dailyPay = dailyPay;
		this.continueDays = continueDays;
	}
	
	@Override
	public AbstractConditionCheck generate(String[] agrs) {
		if (agrs.length < 3) {
			return null;
		}
		int totalPay = Integer.parseInt(agrs[1]);
		int continueDays = Integer.parseInt(agrs[2]);
		return new OpActPayDaysCond(totalPay, continueDays);
	}

	@Override
	public int checkCond(RoleInfo roleInfo, int action,
			CheckDataChg checkDataChg, int index, Object obj) {
		if (checkDataChg == null || action != ActionType.action412.getType() || obj == null) {
			return 0;
		}
		
		OpActivityProgressInfo info = (OpActivityProgressInfo) checkDataChg;
		int val = (int) checkDataChg.getSpecValue(index);
		if (val >= dailyPay * continueDays) {
			return 1;
		}
		
		// 检测天数是否有变
		long now = System.currentTimeMillis();
		if (!DateUtil.isSameDay(info.getCheckTime().getTime(), now)) {
			int prePay = (int) (val - dailyPay * info.getValue3());
			// 比对前一天充值
			if (prePay < dailyPay) {
				// 若前一天充值未达标，则活动中断
				return 0;
			}
			
			// 记录当前已连续达标充值天数
			info.setValue3(info.getValue3() + 1);
			info.setCheckTime(new Timestamp(now));
			
			// 
			OpActivityProgressDAO.getInstance().updateOpActProVal3(info);
		}
		
		int totalCharge = val + (Integer) obj;
		
		int daysVal = (int) info.getValue3();
		daysVal++;
		// 最后一天充值达标
		if (daysVal == continueDays && totalCharge >= dailyPay * continueDays) {
			updateValue(index, checkDataChg, totalCharge);
			return 1;
		}
		
		// 每日充值达标后再充值就不做计算
		if (totalCharge >= dailyPay * daysVal) {
			updateValue(index, checkDataChg, dailyPay * daysVal);
			return 0;
		}
		
		if (totalCharge > val) {
			updateValue(index, checkDataChg, totalCharge);
		}
		return 0;
	}

}
