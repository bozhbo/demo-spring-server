package com.snail.webgame.game.condtion.conds;

import java.util.Date;
import java.util.GregorianCalendar;

import com.snail.webgame.game.common.ActionType;
import com.snail.webgame.game.common.util.DateUtil;
import com.snail.webgame.game.condtion.AbstractConditionCheck;
import com.snail.webgame.game.condtion.CheckDataChg;
import com.snail.webgame.game.info.RoleInfo;

public class OpActSdPayCond extends AbstractConditionCheck {
	
	private int totalPay;
	private Date payDate;// 指定日期
	
	public OpActSdPayCond() {
	}
	
	public OpActSdPayCond(int totalPay, Date payDate) {
		this.totalPay = totalPay;
		this.payDate = payDate;
	}

	@Override
	public AbstractConditionCheck generate(String[] agrs) {
		if (agrs.length < 5) {
			return null;
		}
		int totalPay = Integer.parseInt(agrs[1]);
		
		GregorianCalendar cal = new GregorianCalendar();
		int year = Integer.valueOf(agrs[2]);
		int month = Integer.valueOf(agrs[3]);
		--month;
		int day = Integer.valueOf(agrs[4]);
		cal.set(year, month, day, 0, 0, 1);
		return new OpActSdPayCond(totalPay, cal.getTime());
	}

	@Override
	public int checkCond(RoleInfo roleInfo, int action,
			CheckDataChg checkDataChg, int index, Object obj) {
		if (checkDataChg == null || action != ActionType.action412.getType() || obj == null) {
			return 0;
		}
		
		int val = (int) checkDataChg.getSpecValue(index);
		if (val >= totalPay) {
			return 1;
		}

		// 只计算指定日期的充值
		if (!DateUtil.isSameDay(new Date(), payDate)) {
			return 0;
		}
		
		int totalCharge = val + (Integer) obj;
		if (totalCharge >= totalPay) {
			updateValue(index, checkDataChg, totalPay);
			return 1;
		}
		
		if (totalCharge > val) {
			updateValue(index, checkDataChg, totalCharge);
		}
		return 0;
	}

}
