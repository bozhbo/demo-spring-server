package com.snail.webgame.game.protocal.fight.startFight;

import java.util.List;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

import com.snail.webgame.game.common.fightdata.DropBagInfo;
import com.snail.webgame.game.common.fightdata.FightSideData;

public class IntoFightResp extends MessageBody {

	private int result;
	private long id;// 战斗id
	private String mapType;// 地图类型编号
	private int fightType;// 战场类型 1：主线副本 2：掠夺战 3：竞技场
	private String defendStr;	// 副本战斗-"副本类型,副本章节,副本编号"，PVP-防守方ID,
										// 防守玩法-"难度编号,npc编号" ，世界boss 防守方No,总hp,当前hp,系数

	//与副本类非角色战斗时,只传递角色类数据,NPC类数据客户端自己从配置文件解析
	private int sizeNum;
	private List<FightSideData> sizeList;// 对阵方信息
	
	private int dropCount;
	private List<DropBagInfo> dropList;
	
	private byte checkFlag;//是否需要验证

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("result", 0);
		ps.add("id", 0);
		ps.addString("mapType", "flashCode", 0);
		ps.add("fightType", 0);
		ps.addString("defendStr", "flashCode", 0);

		ps.add("sizeNum", 0);
		ps.addObjectArray("sizeList", "com.snail.webgame.game.common.fightdata.FightSideData", "sizeNum");
		
		ps.add("dropCount", 0);
		ps.addObjectArray("dropList", "com.snail.webgame.game.common.fightdata.DropBagInfo", "dropCount");
		
		ps.add("checkFlag", 0);
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public int getFightType() {
		return fightType;
	}

	public void setFightType(int fightType) {
		this.fightType = fightType;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getMapType() {
		return mapType;
	}

	public void setMapType(String mapType) {
		this.mapType = mapType;
	}
	
	public int getSizeNum() {
		return sizeNum;
	}

	public void setSizeNum(int sizeNum) {
		this.sizeNum = sizeNum;
	}

	public List<FightSideData> getSizeList() {
		return sizeList;
	}

	public void setSizeList(List<FightSideData> sizeList) {
		this.sizeList = sizeList;
	}

	public String getDefendStr() {
		return defendStr;
	}

	public void setDefendStr(String defendStr) {
		this.defendStr = defendStr;
	}

	public int getDropCount() {
		return dropCount;
	}

	public void setDropCount(int dropCount) {
		this.dropCount = dropCount;
	}

	public List<DropBagInfo> getDropList() {
		return dropList;
	}

	public void setDropList(List<DropBagInfo> dropList) {
		this.dropList = dropList;
	}

	public byte getCheckFlag() {
		return checkFlag;
	}

	public void setCheckFlag(byte checkFlag) {
		this.checkFlag = checkFlag;
	}
	
}
