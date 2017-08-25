package com.snail.webgame.game.protocal.fight.checkFight;

import java.util.ArrayList;
import java.util.List;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class CheckFightReq extends MessageBody {

	private int roleId;
	
	private long fightId;// 战斗Id
	private int fightType;
	
	private byte attackType;// 1-普通攻击,2-技能伤害 3-释放技能
	private int skillNo;//释放的技能编号
	
	private int hurt;//本次伤害输出
	
	private int side0Count;//我方单位属性
	private List<CheckPropInfo> list0 = new ArrayList<CheckPropInfo>();
	
	private int side1Count;//BOSS属性
	private List<CheckPropInfo> list1 = new ArrayList<CheckPropInfo>();
	
	private long defendId;//受伤单位

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		
		ps.add("roleId", 0);
		
		ps.add("fightId", 0);
		ps.add("fightType", 0);
		ps.add("attackType", 0);
		ps.add("skillNo", 0);
		ps.add("hurt", 0);
		
		ps.add("side0Count", 0);
		ps.addObjectArray("list0", "com.snail.webgame.game.protocal.fight.checkFight.CheckPropInfo", "side0Count");
		
		ps.add("side1Count", 0);
		ps.addObjectArray("list1", "com.snail.webgame.game.protocal.fight.checkFight.CheckPropInfo", "side1Count");
		
		ps.add("defendId", 0);
		
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public long getFightId() {
		return fightId;
	}

	public void setFightId(long fightId) {
		this.fightId = fightId;
	}

	public int getFightType() {
		return fightType;
	}

	public void setFightType(int fightType) {
		this.fightType = fightType;
	}
	
	public byte getAttackType() {
		return attackType;
	}

	public void setAttackType(byte attackType) {
		this.attackType = attackType;
	}

	public int getSkillNo() {
		return skillNo;
	}

	public void setSkillNo(int skillNo) {
		this.skillNo = skillNo;
	}

	public int getHurt() {
		return hurt;
	}

	public void setHurt(int hurt) {
		this.hurt = hurt;
	}

	public int getSide0Count() {
		return side0Count;
	}

	public void setSide0Count(int side0Count) {
		this.side0Count = side0Count;
	}

	public List<CheckPropInfo> getList0() {
		return list0;
	}

	public void setList0(List<CheckPropInfo> list0) {
		this.list0 = list0;
	}

	public int getSide1Count() {
		return side1Count;
	}

	public void setSide1Count(int side1Count) {
		this.side1Count = side1Count;
	}

	public List<CheckPropInfo> getList1() {
		return list1;
	}

	public void setList1(List<CheckPropInfo> list1) {
		this.list1 = list1;
	}

	public long getDefendId() {
		return defendId;
	}

	public void setDefendId(long defendId) {
		this.defendId = defendId;
	}
	
}
