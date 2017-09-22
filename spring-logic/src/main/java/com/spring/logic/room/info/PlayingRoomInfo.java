package com.spring.logic.room.info;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.spring.logic.role.info.RoomRoleInfo;
import com.spring.logic.room.enums.RoomPlayingEnum;
import com.spring.logic.room.enums.RoomTypeEnum;

/**
 * 用于房间服务器运行对象
 * 
 * @author zhoubo
 *
 */
public class PlayingRoomInfo {

	private int roomId;	// 房间号
	private RoomTypeEnum roomType;	// 房间类型
	private RoomPlayingEnum roomState;// 房间当前状态
	private long lastUpdateTime;	// 最后更新时间
	private long readyTime;	// 准备开始时间
	private long sendCardTime;	// 发牌开始时间
	private int curGoldUnit;// 当前跟注的单位金币数
	private int amountGold;// 当前总金币数
	private int roomRound;// 当前房间回合
	private int roomRoundTemp;// 当前房间回合统计临时变量
	
	private RoomRoleInfo roomRoleInfo; // 当前房间可操作人员
	
	/**
	 * 所有房间人员
	 */
	private List<RoomRoleInfo> list = new ArrayList<>();
	
	/**
	 * 游戏人员
	 */
	private List<RoomRoleInfo> playingList = new ArrayList<>();
	
	/**
	 * 游戏人员Key-Value
	 */
	private Map<Integer, RoomRoleInfo> playingMap = new HashMap<>();
	
	/**
	 * 当前所有人的牌
	 */
	private List<Integer> cardList = new ArrayList<>();
	
	public PlayingRoomInfo(int roomId, RoomTypeEnum roomType) {
		this.roomId = roomId;
		this.roomType = roomType;
		
		this.roomState = RoomPlayingEnum.ROOM_STATE_INIT;
		
		for (int i = 0; i < 52; i++) {
			cardList.add(i + 1);
		}
	}

	public int getRoomId() {
		return roomId;
	}

	public void setRoomId(int roomId) {
		this.roomId = roomId;
	}

	public RoomTypeEnum getRoomType() {
		return roomType;
	}

	public void setRoomType(RoomTypeEnum roomType) {
		this.roomType = roomType;
	}

	public List<RoomRoleInfo> getList() {
		return list;
	}

	public void setList(List<RoomRoleInfo> list) {
		this.list = list;
	}

	public long getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(long lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public RoomPlayingEnum getRoomState() {
		return roomState;
	}

	public void setRoomState(RoomPlayingEnum roomState) {
		this.roomState = roomState;
	}

	public List<RoomRoleInfo> getPlayingList() {
		return playingList;
	}

	public void setPlayingList(List<RoomRoleInfo> playingList) {
		this.playingList = playingList;
	}

	public long getReadyTime() {
		return readyTime;
	}

	public void setReadyTime(long readyTime) {
		this.readyTime = readyTime;
	}

	public RoomRoleInfo getRoomRoleInfo() {
		return roomRoleInfo;
	}

	public void setRoomRoleInfo(RoomRoleInfo roomRoleInfo) {
		this.roomRoleInfo = roomRoleInfo;
	}

	public long getSendCardTime() {
		return sendCardTime;
	}

	public void setSendCardTime(long sendCardTime) {
		this.sendCardTime = sendCardTime;
	}

	public int getCurGoldUnit() {
		return curGoldUnit;
	}

	public void setCurGoldUnit(int curGoldUnit) {
		this.curGoldUnit = curGoldUnit;
	}

	public int getAmountGold() {
		return amountGold;
	}

	public void setAmountGold(int amountGold) {
		this.amountGold = amountGold;
	}

	public List<Integer> getCardList() {
		return cardList;
	}

	public void setCardList(List<Integer> cardList) {
		this.cardList = cardList;
	}

	public Map<Integer, RoomRoleInfo> getPlayingMap() {
		return playingMap;
	}

	public void setPlayingMap(Map<Integer, RoomRoleInfo> playingMap) {
		this.playingMap = playingMap;
	}

	public int getRoomRound() {
		return roomRound;
	}

	public void setRoomRound(int roomRound) {
		this.roomRound = roomRound;
	}

	public int getRoomRoundTemp() {
		return roomRoundTemp;
	}

	public void setRoomRoundTemp(int roomRoundTemp) {
		this.roomRoundTemp = roomRoundTemp;
	}

	
}
