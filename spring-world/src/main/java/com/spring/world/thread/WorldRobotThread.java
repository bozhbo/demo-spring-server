package com.spring.world.thread;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.spring.logic.business.service.RoleBusinessService;
import com.spring.logic.message.request.server.DeployRoleReq;
import com.spring.logic.role.cache.RoleCache;
import com.spring.logic.role.info.RoomRobotInfo;
import com.spring.logic.role.service.RoleRoomService;
import com.spring.logic.room.enums.RoomTypeEnum;
import com.spring.world.config.WorldConfig;

public class WorldRobotThread extends Thread {

	private static final Log logger = LogFactory.getLog(WorldRobotThread.class);

	/**
	 * 每个房间的机器人集合
	 */
	private Map<RoomTypeEnum, Map<Integer, RoomRobotInfo>> roomRobotMap = new HashMap<>();

	/**
	 * 机器人ID号
	 */
	public static int START_ID = Integer.MAX_VALUE - 10000000;

	/**
	 * 当前房间中的机器人数量
	 */
	private int roomRobotCount = 0;
	
	/**
	 * 最大房间类型
	 */
	private int maxRoomType = 0;

	private Random r = new Random();

	private RoleRoomService roleRoomService;

	private RoleBusinessService roleLogicService;

	@Override
	public void run() {
		RoomTypeEnum[] enums = RoomTypeEnum.values();

		for (RoomTypeEnum roomTypeEnum : enums) {
			roomRobotMap.put(roomTypeEnum, new HashMap<>());

			for (int i = 0; i < WorldConfig.ROOM_ROBOT_MIN_COUNT; i++) {
				RoomRobotInfo roomRobotInfo = createRoomRobotInfo();
				roomRobotMap.get(roomTypeEnum).put(roomRobotInfo.getRoleId(), roomRobotInfo);
			}
			
			maxRoomType = maxRoomType < roomTypeEnum.getValue() ? roomTypeEnum.getValue() : maxRoomType;
		}

		while (true) {
			long curTime = System.currentTimeMillis();
			roomRobotCount = 0;

			try {
				Set<Entry<RoomTypeEnum, Map<Integer, RoomRobotInfo>>> set = roomRobotMap.entrySet();

				for (Entry<RoomTypeEnum, Map<Integer, RoomRobotInfo>> entry : set) {
					Set<Entry<Integer, RoomRobotInfo>> set1 = entry.getValue().entrySet();

					for (Entry<Integer, RoomRobotInfo> entry2 : set1) {
						if (entry2.getValue().getRoomId() == 0) {
							DeployRoleReq deployRoleReq = roleLogicService.getDeployRoleMessage(entry2.getValue());
							roleRoomService.joinRoom(entry2.getValue(), entry.getKey(), deployRoleReq);

							entry2.getValue().setSendToRoomTime(curTime);
						} else {
							resetRobot(curTime, entry2.getValue());

							roomRobotCount++;
						}
					}
				}
				
				addRobot();
			} catch (Exception e) {
				logger.error("", e);
			} finally {
				try {
					TimeUnit.SECONDS.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		}
	}
	
	/**
	 * 重置机器人
	 * 
	 * @param curTime
	 * @param roomRobotInfo
	 */
	private void resetRobot(long curTime, RoomRobotInfo roomRobotInfo) {
		boolean isReset = false;

		if (curTime - roomRobotInfo.getLastUpdateTime() > 600 * 1000) {
			// 十分钟无反馈
			isReset = true;
		}

		if (roomRobotInfo.getGold() < 10000 || roomRobotInfo.getGold() > 1000000) {
			// 金钱过少或者过多
			isReset = true;
		}

		if (curTime - roomRobotInfo.getSendToRoomTime() > 7200 * 1000) {
			// 时间过长
			isReset = true;
		}

		if (isReset) {
			// 退出房间重新开始
			roleRoomService.leaveRoom(roomRobotInfo);
		}
	}

	/**
	 * 增加机器人
	 */
	private void addRobot() {
		int curRoles = RoleCache.getOnlineRole();
		double minRobotCount = WorldConfig.ROOM_ROBOT_RATE_COUNT * curRoles;
		
		if (minRobotCount < roomRobotCount) {
			int cutCount = (int)minRobotCount - roomRobotCount;
			
			for (int i = 0; i < cutCount; i++) {
				RoomRobotInfo roomRobotInfo = createRoomRobotInfo();
				roomRobotMap.get(getRandomRoomTypeEnum(r.nextInt(maxRoomType) + 1)).put(roomRobotInfo.getRoleId(), roomRobotInfo);
			}
		}
	}

	private RoomRobotInfo createRoomRobotInfo() {
		RoomRobotInfo roomRobotInfo = new RoomRobotInfo();

		roomRobotInfo.setAccount("account");
		roomRobotInfo.setGateId(-1);
		roomRobotInfo.setGold(r.nextInt(200000) + 50000);
		roomRobotInfo.setHeader("");
		roomRobotInfo.setLastUpdateTime(System.currentTimeMillis());
		roomRobotInfo.setRoleId(START_ID++);
		roomRobotInfo.setRoleName("admin");
		roomRobotInfo.setVipLevel(1);

		return roomRobotInfo;
	}

	private RoomTypeEnum getRandomRoomTypeEnum(int value) {
		switch (value) {
		case 1:
			return RoomTypeEnum.ROOM_TYPE_NEW;
		case 2:
			return RoomTypeEnum.ROOM_TYPE_LEVEL1;
		case 3:
			return RoomTypeEnum.ROOM_TYPE_LEVEL2;
		case 4:
			return RoomTypeEnum.ROOM_TYPE_LEVEL3;
		case 5:
			return RoomTypeEnum.ROOM_TYPE_LEVEL4;

		default:
			return RoomTypeEnum.ROOM_TYPE_NEW;
		}
	}

	public void setRoleRoomService(RoleRoomService roleRoomService) {
		this.roleRoomService = roleRoomService;
	}

	public void setRoleLogicService(RoleBusinessService roleLogicService) {
		this.roleLogicService = roleLogicService;
	}
}
