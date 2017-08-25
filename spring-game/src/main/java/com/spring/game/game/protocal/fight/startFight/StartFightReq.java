package com.snail.webgame.game.protocal.fight.startFight;

import java.util.List;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class StartFightReq extends MessageBody {

	private byte fightType;//1-主线副本,2-竞技场,3-宝石活动,4-经验活动 练兵, 5-银币活动 趁火打劫,6-宝物活动 守卫君主 
							//7-大地图攻击NPC(真实的NPC数据) 8-大地图攻击NPC(玩家镜像)  11-对攻战
	private byte isFight;//0-设置防守阵型,1-开始战斗
	private String defendStr;//副本战斗-"副本类型,副本章节,副本编号"，PVP-防守方ID
	private int size;
	private List<StartFightPosInfo> chgPosInfos;
	

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("fightType", 0);
		ps.add("isFight", 0);
		ps.addString("defendStr", "flashCode", 0);
		ps.add("size", 0);
		ps.addObjectArray("chgPosInfos", "com.snail.webgame.game.protocal.fight.startFight.StartFightPosInfo", "size");
	}
	
	public byte getFightType() {
		return fightType;
	}


	public void setFightType(byte fightType) {
		this.fightType = fightType;
	}


	public byte getIsFight() {
		return isFight;
	}

	public void setIsFight(byte isFight) {
		this.isFight = isFight;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public String getDefendStr() {
		return defendStr;
	}

	public void setDefendStr(String defendStr) {
		this.defendStr = defendStr;
	}

	public List<StartFightPosInfo> getChgPosInfos() {
		return chgPosInfos;
	}

	public void setChgPosInfos(List<StartFightPosInfo> chgPosInfos) {
		this.chgPosInfos = chgPosInfos;
	}
}
