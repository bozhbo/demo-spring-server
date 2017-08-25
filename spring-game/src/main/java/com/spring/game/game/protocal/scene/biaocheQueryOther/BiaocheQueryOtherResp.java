package com.snail.webgame.game.protocal.scene.biaocheQueryOther;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;


/**
 * 查看世界地图上的他人镖车
 * @author hongfm
 *
 */
public class BiaocheQueryOtherResp extends MessageBody {

	private int result;
	private int otherRoleId;
	private String otherRoleName;
	private int otherRoleFightValue;
	private byte otherRoleBiaocheType;//当前镖车类型
	private byte otherRoleLeftJiebiaoNum;//剩余可截次数
	private short otherHeroLv;
	private int otherRoleHeroNo;
	private int getSilver;//截镖可获得银子
	
	private int helpRoleId;
	private String helpRoleName;//护送好友
	private int helpRoleFightValue;//护送好友战力
	private short helpRoleLv;//护送好友等级
	
	@Override
	protected void setSequnce(ProtocolSequence ps)
	{
		ps.add("result", 0);
		ps.add("otherRoleId", 0);
		ps.addString("otherRoleName", "flashCode", 0);
		ps.add("otherRoleFightValue", 0);
		ps.add("otherRoleBiaocheType", 0);
		ps.add("otherRoleLeftJiebiaoNum", 0);
		ps.add("otherHeroLv", 0);
		ps.add("otherRoleHeroNo", 0);
		ps.add("getSilver", 0);
		
		ps.add("helpRoleId", 0);
		ps.addString("helpRoleName", "flashCode", 0);
		ps.add("helpRoleFightValue", 0);
		ps.add("helpRoleLv", 0);
		
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public int getOtherRoleId() {
		return otherRoleId;
	}

	public void setOtherRoleId(int otherRoleId) {
		this.otherRoleId = otherRoleId;
	}

	public String getOtherRoleName() {
		return otherRoleName;
	}

	public void setOtherRoleName(String otherRoleName) {
		this.otherRoleName = otherRoleName;
	}

	public int getOtherRoleFightValue() {
		return otherRoleFightValue;
	}

	public void setOtherRoleFightValue(int otherRoleFightValue) {
		this.otherRoleFightValue = otherRoleFightValue;
	}

	public byte getOtherRoleBiaocheType() {
		return otherRoleBiaocheType;
	}

	public void setOtherRoleBiaocheType(byte otherRoleBiaocheType) {
		this.otherRoleBiaocheType = otherRoleBiaocheType;
	}

	public byte getOtherRoleLeftJiebiaoNum() {
		return otherRoleLeftJiebiaoNum;
	}

	public void setOtherRoleLeftJiebiaoNum(byte otherRoleLeftJiebiaoNum) {
		this.otherRoleLeftJiebiaoNum = otherRoleLeftJiebiaoNum;
	}

	public int getGetSilver() {
		return getSilver;
	}

	public void setGetSilver(int getSilver) {
		this.getSilver = getSilver;
	}

	public int getHelpRoleId() {
		return helpRoleId;
	}

	public void setHelpRoleId(int helpRoleId) {
		this.helpRoleId = helpRoleId;
	}

	public String getHelpRoleName() {
		return helpRoleName;
	}

	public void setHelpRoleName(String helpRoleName) {
		this.helpRoleName = helpRoleName;
	}

	public int getHelpRoleFightValue() {
		return helpRoleFightValue;
	}

	public void setHelpRoleFightValue(int helpRoleFightValue) {
		this.helpRoleFightValue = helpRoleFightValue;
	}

	public int getOtherRoleHeroNo() {
		return otherRoleHeroNo;
	}

	public void setOtherRoleHeroNo(int otherRoleHeroNo) {
		this.otherRoleHeroNo = otherRoleHeroNo;
	}

	public short getOtherHeroLv() {
		return otherHeroLv;
	}

	public void setOtherHeroLv(short otherHeroLv) {
		this.otherHeroLv = otherHeroLv;
	}

	public short getHelpRoleLv() {
		return helpRoleLv;
	}

	public void setHelpRoleLv(short helpRoleLv) {
		this.helpRoleLv = helpRoleLv;
	}
	
}
