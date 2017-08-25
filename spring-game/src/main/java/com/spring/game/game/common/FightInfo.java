package com.snail.webgame.game.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.snail.webgame.game.common.fightdata.ArmyFightingInfo;
import com.snail.webgame.game.common.fightdata.DropBagInfo;
import com.snail.webgame.game.common.fightdata.FightArmyDataInfo;
import com.snail.webgame.game.common.fightdata.FightSideData;
import com.snail.webgame.game.info.log.ConfigType;
import com.snail.webgame.game.protocal.fight.checkFight.CheckBuffInfo;
import com.snail.webgame.game.protocal.fight.checkFight.CheckSkillInfo;
import com.snail.webgame.game.protocal.fight.startFight.StartFightPosInfo;

public class FightInfo {

	private long fightId;// 战斗编号(添加缓存时自动分配赋值)
	// ---战斗开始请求信息--
	private int roleId;// 角色Id
	private FightType fightType;// 战场类型
	private String defendStr;// 副本战斗-"副本类型,副本章节,副本编号"，PVP-防守方ID,
								// 防守玩法-"难度编号,npc编号" ，世界boss 防守方No,总hp,当前hp,系数
	private List<StartFightPosInfo> chgPosInfos;// 战斗布阵信息

	// ---战斗开始请求返回信息--
	private int fightRoleId;// 挑战用户编号 0-系统自动的NPC
	private int fightArenaId;// 挑战用户的竞技场编号
	private String startRespDefendStr;// 战斗开始返回参数
	// 双方的阵形信息
	private List<FightSideData> fightDataList = new ArrayList<FightSideData>();

	// ---战斗结束请求信息--
	private String endRespDefendStr;// 战斗结束返回参数
	private int winSide;// 战胜方 0:进攻 1:防守方
	private List<ArmyFightingInfo> armyFightingInfos;// 战斗返回结果
	private int endTimeType = 1;// 1 对方全灭 ,2 我方未全灭

	private int fightServerId;// 战斗服务器id(添加缓存时自动分配赋值)
	private long fightTime;// 缓存添加时间(添加缓存时自动赋值)

	private Map<Integer, DropBagInfo> dropMap = new HashMap<Integer, DropBagInfo>();// 副本打怪掉落
	
	private int checkErrorNum;//PVE中途校验错误次数
	private int checkNum1;//PVE中途校验次数(普通攻击)
	private int checkNum2;//PVE中途校验次数(技能伤害)
	private int checkNum3;//PVE中途校验释放技能次数
	private String str0;//战斗检验串1
	private String str1;//战斗检验串1
	private Map<Integer,CheckSkillInfo> skillMap = new HashMap<Integer,CheckSkillInfo>();//战斗时释放的技能
	private float f1 = 0.2f;
	private float f2 = 0.2f;
	private float f3 = 0.2f;
	private float f4 = 0.2f;
	private String bossHurt="";
	private long dataErrorTime;//异常数据持续时间
	private byte checkFlag;//是否需要验证
	private Map<Long,FightArmyDataInfo> side0ArmyMap = new HashMap<Long,FightArmyDataInfo>();//我方部队
	private Map<Long,FightArmyDataInfo> side1ArmyMap = new HashMap<Long,FightArmyDataInfo>();//敌方部队
	private Map<Long,HashMap<Integer,CheckBuffInfo>> buffMap = new HashMap<Long,HashMap<Integer,CheckBuffInfo>>();
	private FightArmyDataInfo bossArmyDataInfo;//BOSS属性
	private int bossError;//boss检测是否异常 0-未检测 1-数据未篡改 2-数值篡改
	
	public FightInfo(FightType fightType, int roleId) {
		this.roleId = roleId;
		this.fightType = fightType;
	}

	public long getFightId() {
		return fightId;
	}

	public void setFightId(long fightId) {
		this.fightId = fightId;
	}

	public FightType getFightType() {
		return fightType;
	}

	public void setFightType(FightType fightType) {
		this.fightType = fightType;
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

	public int getFightArenaId() {
		return fightArenaId;
	}

	public void setFightArenaId(int fightArenaId) {
		this.fightArenaId = fightArenaId;
	}

	public String getStartRespDefendStr() {
		return startRespDefendStr;
	}

	public void setStartRespDefendStr(String startRespDefendStr) {
		this.startRespDefendStr = startRespDefendStr;
	}

	public String getEndRespDefendStr() {
		return endRespDefendStr;
	}

	public void setEndRespDefendStr(String endRespDefendStr) {
		this.endRespDefendStr = endRespDefendStr;
	}

	public List<FightSideData> getFightDataList() {
		return fightDataList;
	}

	public void setFightDataList(List<FightSideData> fightDataList) {
		this.fightDataList = fightDataList;
	}

	public int getWinSide() {
		return winSide;
	}

	public void setWinSide(int winSide) {
		this.winSide = winSide;
	}

	public List<ArmyFightingInfo> getArmyFightingInfos() {
		return armyFightingInfos;
	}

	public void setArmyFightingInfos(List<ArmyFightingInfo> armyFightingInfos) {
		this.armyFightingInfos = armyFightingInfos;
	}

	public int getEndTimeType() {
		return endTimeType;
	}

	public void setEndTimeType(int endTimeType) {
		this.endTimeType = endTimeType;
	}

	public int getFightServerId() {
		return fightServerId;
	}

	public void setFightServerId(int fightServerId) {
		this.fightServerId = fightServerId;
	}

	public long getFightTime() {
		return fightTime;
	}

	public void setFightTime(long fightTime) {
		this.fightTime = fightTime;
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public int getFightRoleId() {
		return fightRoleId;
	}

	public void setFightRoleId(int fightRoleId) {
		this.fightRoleId = fightRoleId;
	}

	public Map<Integer, DropBagInfo> getDropMap() {
		return dropMap;
	}

	public void setDropMap(Map<Integer, DropBagInfo> dropMap) {
		this.dropMap = dropMap;
	}
	

	public int getCheckErrorNum() {
		return checkErrorNum;
	}

	public void setCheckErrorNum(int checkErrorNum) {
		this.checkErrorNum = checkErrorNum;
	}
	

	public int getCheckNum1() {
		return checkNum1;
	}

	public void setCheckNum1(int checkNum1) {
		this.checkNum1 = checkNum1;
	}

	public int getCheckNum2() {
		return checkNum2;
	}

	public void setCheckNum2(int checkNum2) {
		this.checkNum2 = checkNum2;
	}

	public String getStr0() {
		return str0;
	}

	public void setStr0(String str0) {
		this.str0 = str0;
	}

	public String getStr1() {
		return str1;
	}

	public void setStr1(String str1) {
		this.str1 = str1;
	}

	public Map<Integer, CheckSkillInfo> getSkillMap() {
		return skillMap;
	}

	public void setSkillMap(Map<Integer, CheckSkillInfo> skillMap) {
		this.skillMap = skillMap;
	}
	
	public float getF1() {
		return f1;
	}

	public void setF1(float f1) {
		this.f1 = f1;
	}

	public float getF2() {
		return f2;
	}

	public void setF2(float f2) {
		this.f2 = f2;
	}

	public float getF3() {
		return f3;
	}

	public void setF3(float f3) {
		this.f3 = f3;
	}

	public float getF4() {
		return f4;
	}

	public void setF4(float f4) {
		this.f4 = f4;
	}

	public String getBossHurt() {
		return bossHurt;
	}

	public void setBossHurt(String bossHurt) {
		this.bossHurt = bossHurt;
	}

	
	public long getDataErrorTime() {
		return dataErrorTime;
	}

	public void setDataErrorTime(long dataErrorTime) {
		this.dataErrorTime = dataErrorTime;
	}

	public byte getCheckFlag() {
		return checkFlag;
	}

	public void setCheckFlag(byte checkFlag) {
		this.checkFlag = checkFlag;
	}

	
	public Map<Long, FightArmyDataInfo> getSide0ArmyMap() {
		return side0ArmyMap;
	}

	public void setSide0ArmyMap(Map<Long, FightArmyDataInfo> side0ArmyMap) {
		this.side0ArmyMap = side0ArmyMap;
	}

	
	public Map<Long, FightArmyDataInfo> getSide1ArmyMap() {
		return side1ArmyMap;
	}

	public void setSide1ArmyMap(Map<Long, FightArmyDataInfo> side1ArmyMap) {
		this.side1ArmyMap = side1ArmyMap;
	}

	public int getCheckNum3() {
		return checkNum3;
	}

	public void setCheckNum3(int checkNum3) {
		this.checkNum3 = checkNum3;
	}

	public Map<Long, HashMap<Integer, CheckBuffInfo>> getBuffMap() {
		return buffMap;
	}

	public void setBuffMap(Map<Long, HashMap<Integer, CheckBuffInfo>> buffMap) {
		this.buffMap = buffMap;
	}

	public FightArmyDataInfo getBossArmyDataInfo() {
		return bossArmyDataInfo;
	}

	public void setBossArmyDataInfo(FightArmyDataInfo bossArmyDataInfo) {
		this.bossArmyDataInfo = bossArmyDataInfo;
	}
	

	public int getBossError() {
		return bossError;
	}

	public void setBossError(int bossError) {
		this.bossError = bossError;
	}

	/**
	 * 副本id 对应日志game_congfig s_id
	 * @return
	 */
	public String getInstanceTypeId() {
		String fightTypeValue = ConfigType.fightType.getType() + "-" + fightType.getValue();
		switch (fightType) {		
		case FIGHT_TYPE_1:// 主线副本
		case FIGHT_TYPE_10://普通副本
		case FIGHT_TYPE_13:// 对攻副本
			//副本类型,副本章节,副本编号
			return defendStr;
//		case FIGHT_TYPE_3:
//			// 宝石活动 战斗类型-关卡编号
//			return fightTypeValue + "-" + defendStr;
		case FIGHT_TYPE_4:
			// 经验活动 战斗类型-活动级别
			return fightTypeValue + "-" + defendStr;
//		case FIGHT_TYPE_5:
//			// 经验活动 战斗类型-活动级别
//			return fightTypeValue + "-" + defendStr;
		case FIGHT_TYPE_6:
			// 宝物活动 战斗类型-关卡编号
			return fightTypeValue + "-" + defendStr;
		case FIGHT_TYPE_12:
			// 活动防守 战斗类型-关卡编号
			return fightTypeValue + "-" + defendStr;
		default:
			return fightTypeValue;
		}
	}

}
