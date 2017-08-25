package com.snail.webgame.game.charge.text;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.game.cache.RoleChargeMap;
import com.snail.webgame.game.dao.RoleChargeErrorDao;
import com.snail.webgame.game.info.RoleChargeInfo;
import com.snail.webgame.game.protocal.app.common.EChargeState;

public class AppService implements Runnable {
	private static final Logger logger = LoggerFactory.getLogger("logs");

	private List<Object> list;
	
	public AppService(List<Object> list) {
		this.list = list;
	}
	
	@Override
	public void run() {
		if (this.list != null) {
			for (Object object : list) {
				if (object instanceof String) {
					logger.info("receive = " + object.toString());
				} else if (object instanceof Integer) {
					logger.info("receive = " + (Integer)object);
				}
			}
			
			try {
				if(list.size() >= 12 && list.get(11) instanceof Integer){
					int result = (Integer)list.get(11);
					long seqId = Long.valueOf((String)list.get(10));
					RoleChargeInfo roleChargeInfo = RoleChargeMap.fetchRoleChargeInfoBySeqId(seqId);
					
					if(result != 1){
						if(result == 53029 || result == 53030 || result == -1 || result == 53035){
							if(roleChargeInfo != null){
								roleChargeInfo.setErrorCode(result);
							}
						}
						
						if(!RoleChargeErrorDao.updateRecordStatus(seqId, EChargeState.RECEIVE_FROM_TRANSIT_SERVER_ERRORCODE.getValue(), result)){
							logger.error("receive from transitionServer RoleChargeErrorDao.updateRecordStatus error1");
						} else {
							if(roleChargeInfo != null){
								roleChargeInfo.setState(EChargeState.RECEIVE_FROM_TRANSIT_SERVER_ERRORCODE);
							}
						}
					} else {
						// 改状态
						if(!RoleChargeErrorDao.updateRecordStatus(seqId, EChargeState.RECEIVE_FROM_TRANSIT_SERVER_RESULT_IS_1.getValue(), result)){
							logger.error("receive from transitionServer RoleChargeErrorDao.updateRecordStatus error3");
						} else {
							if(roleChargeInfo != null){
								roleChargeInfo.setState(EChargeState.RECEIVE_FROM_TRANSIT_SERVER_RESULT_IS_1);
							}
						}
					}
				} else {
					logger.error("APP rechare receive list size error");
				}
			} catch (Exception e) {
				logger.error("APP rechare receive error : " + e.getMessage());
			}
		} else {
			
		}
	}
}
