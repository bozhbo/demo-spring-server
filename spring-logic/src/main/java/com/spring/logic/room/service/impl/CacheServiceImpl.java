package com.spring.logic.room.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.spring.logic.room.RoomConfig;
import com.spring.logic.room.cache.RoomCahce;
import com.spring.logic.room.enums.RoomTypeEnum;
import com.spring.logic.room.info.RoomInfo;
import com.spring.logic.room.service.CacheService;

@Service
public class CacheServiceImpl implements CacheService {
	
	private static final Log logger = LogFactory.getLog(CacheServiceImpl.class);

	public RoomInfo queryRoom(int roomId) {
		return RoomCahce.getAllRoomMap().get(roomId);
	}
	
	public int randomJoinRoom(int roleId, RoomTypeEnum roomTypeEnum) {
		Map<Integer, RoomInfo> map = RoomCahce.getPlayingRoomMap().get(roomTypeEnum);
		
		if (RoomCahce.needCreateRoom(roomTypeEnum)) {
			synchronized (map) {
				createRoom(roomTypeEnum);
			}
		}
		
		synchronized (map) {
			List<RoomInfo> list = RoomCahce.getPlayingRoomListMap().get(roomTypeEnum);
			
			if (list.size() < 100) {
				Collections.shuffle(list);
				
				for (RoomInfo roomInfo : list) {
					synchronized (roomInfo) {
						if (roomInfo.getList().size() < RoomConfig.ROOM_MAX_ROLES) {
							roomInfo.getList().add(roleId);
							return roomInfo.getRoomId();
						}
					}
				}
			} else {
				while (true) {
					int start = RoomConfig.getRandom(list.size() - 50);
					List<RoomInfo> subList =  list.subList(start, start + 50);
					
					Collections.shuffle(subList);
					
					for (RoomInfo roomInfo : subList) {
						synchronized (roomInfo) {
							if (roomInfo.getList().size() < RoomConfig.ROOM_MAX_ROLES) {
								roomInfo.getList().add(roleId);
								return roomInfo.getRoomId();
							}
						}
					}
				}
			}
			
			if (createRoom(roomTypeEnum) != null) {
				randomJoinRoom(roleId, roomTypeEnum);
			}
		}
		
		return 0;
	}

	@Override
	public boolean leaveRoom(int roleId, int roomId, RoomTypeEnum roomTypeEnum) {
		Map<Integer, RoomInfo> map = RoomCahce.getPlayingRoomMap().get(roomTypeEnum);
		
		if (map == null) {
			return false;
		}
		
		RoomInfo roomInfo = map.get(roomId);
		
		if (roomInfo == null) {
			return false;
		}
		
		synchronized (map) {
			synchronized (roomInfo) {
				if (roomInfo.getList().remove(new Integer(roleId))) {
					RoomCahce.decrementRole(roomTypeEnum);
				}
				
				if (roomInfo.getList().isEmpty() && !RoomCahce.needCreateRoom(roomTypeEnum)) {
					closeRoom(roomInfo);
				}
			}
		}
		
		return true;
	}
	
	@Override
	public void closeRoom(int roomId) {
		RoomInfo roomInfo = queryRoom(roomId);
		
		if (roomInfo != null) {
			closeRoom(roomInfo);
			
			
		}
	}
	
	private RoomInfo createRoom(RoomTypeEnum roomTypeEnum) {
		RoomInfo roomInfo = null;
		Map<Integer, RoomInfo> emptyRoomMap = RoomCahce.getEmptyRoomMap();
		
		if (emptyRoomMap.size() > 0) {
			// 优先使用回收的空房间对象
			Set<Entry<Integer, RoomInfo>> set = emptyRoomMap.entrySet();
			
			for (Entry<Integer, RoomInfo> entry : set) {
				roomInfo = entry.getValue();
				emptyRoomMap.remove(entry.getKey());
				break;
			}
		}
		
		if (roomInfo == null) {
			if (RoomCahce.getAllRoomMap().size() > RoomConfig.ROOM_MAX_COUNT) {
				return null;
			}
			
			roomInfo = new RoomInfo(RoomConfig.createRoomId());
			RoomCahce.getAllRoomMap().put(roomInfo.getRoomId(), roomInfo);
		}
		
		roomInfo.setRoomType(roomTypeEnum);
		roomInfo.getList().clear();
		roomInfo.setRoomState(1);
		
		List<RoomInfo> list = RoomCahce.getPlayingRoomListMap().get(roomTypeEnum);
		list.add(roomInfo);
		RoomCahce.getPlayingRoomMap().get(roomTypeEnum).put(roomInfo.getRoomId(), roomInfo);
		
		return roomInfo;
	}
	
	private void closeRoom(RoomInfo roomInfo) {
		if (roomInfo == null) {
			return;
		}
		
		Map<Integer, RoomInfo> map = RoomCahce.getPlayingRoomMap().get(roomInfo.getRoomType());
		
		synchronized (map) {
			synchronized (roomInfo) {
				roomInfo.getList().clear();
				roomInfo.setRoomState(0);
				
				RoomCahce.getPlayingRoomMap().get(roomInfo.getRoomType()).remove(roomInfo.getRoomId());
				RoomCahce.getPlayingRoomListMap().get(roomInfo.getRoomType()).remove(roomInfo);
				RoomCahce.getEmptyRoomMap().put(roomInfo.getRoomId(), roomInfo);
			}
		}
	}
	
	@Override
	public List<Integer> getAllRoles(int roomId) {
		List<Integer> list = null;
		RoomInfo roomInfo = RoomCahce.getAllRoomMap().get(roomId);
		
		if (roomInfo == null) {
			return list;
		}
		
		Map<Integer, RoomInfo> map = RoomCahce.getPlayingRoomMap().get(roomInfo.getRoomType());
		
		synchronized (map) {
			synchronized (roomInfo) {
				list = new ArrayList<>(roomInfo.getList());
			}
		}
		
		return list;
	}

	@Override
	public void printAllRooms() {
		logger.info("start print room info");
		
		Set<Entry<Integer, RoomInfo>> set = RoomCahce.getAllRoomMap().entrySet();
		
		for (Entry<Integer, RoomInfo> entry : set) {
			synchronized (entry.getValue()) {
				logger.info(entry.getValue().toString());
			}
		}
		
		logger.info("end print room info");
	}
	
}
