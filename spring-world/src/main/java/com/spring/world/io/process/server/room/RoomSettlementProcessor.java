package com.spring.world.io.process.server.room;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.snail.mina.protocol.info.IRoomBody;
import com.snail.mina.protocol.info.Message;
import com.snail.mina.protocol.processor.IProcessor;
import com.spring.common.GameMessageType;
import com.spring.logic.message.request.server.RoomSettlementRes;
import com.spring.logic.message.request.server.RoomSettlementResp;
import com.spring.logic.role.cache.RoleCache;
import com.spring.logic.role.info.RoleInfo;
import com.spring.world.dao.RoleDao;

/**
 * 每局结束结算
 * 
 * @author Administrator
 *
 */
public class RoomSettlementProcessor implements IProcessor {
	
	private static final Log logger = LogFactory.getLog(RoomInfoProcessor.class);

	private RoleDao roleDao;
	
	@Override
	public void processor(Message message) {
		RoomSettlementResp resp = (RoomSettlementResp)message.getiRoomBody();
		
		List<RoomSettlementRes> list = resp.getList();
		
		for (RoomSettlementRes roomSettlementRes : list) {
			RoleInfo roleInfo = RoleCache.getRoleInfo(roomSettlementRes.getRoleId());
			
			if (roleInfo != null) {
				synchronized (roleInfo) {
					if (resp.getRoomId() == roleInfo.getRoomId()) {
						roleInfo.setGold(roleInfo.getGold() + roomSettlementRes.getGold());
						roleDao.updateRoleGold(roomSettlementRes.getRoleId(), roomSettlementRes.getGold());
					} else {
						logger.error("role gold set error room = " + resp.getRoomId() + ", actual room = " + roleInfo.getRoomId());
					}
				}
			}
		}
	}

	@Override
	public Class<? extends IRoomBody> getRoomBodyClass() {
		return RoomSettlementResp.class;
	}

	@Override
	public int getMsgType() {
		return GameMessageType.ROOM_2_WORLD_ROOM_SUMMARY;
	}

	@Autowired
	public void setRoleDao(RoleDao roleDao) {
		this.roleDao = roleDao;
	}
}
