package com.snail.webgame.game.protocal.snatch.getRivalList;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class RivalListRe extends MessageBody {

	private String roleName; // 角色名称
	private int roleLevel; // 角色等级
	private byte probability; // 抢夺胜利概率（1高 2低 3极高 4极低）
	private int heroNo; // 主武将编号
	private byte isNPC;// 0-不是 1-是

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.addString("roleName", "flashCode", 0);
		ps.add("roleLevel", 0);
		ps.add("probability", 0);
		ps.add("heroNo", 0);
		ps.add("isNPC", 0);
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public int getRoleLevel() {
		return roleLevel;
	}

	public void setRoleLevel(int roleLevel) {
		this.roleLevel = roleLevel;
	}

	public byte getProbability() {
		return probability;
	}

	public void setProbability(byte probability) {
		this.probability = probability;
	}

	public byte getProbabilitybyRate(double successRate) {
		// 0%<“极低概率”<20% 21%<“低概率”<50%
		// 51%<“高概率”<80% 81%<“极高概率”<100%
		if (successRate >= 0.81f) {
			return (byte) 3;// 3：极高概率
		} else if (successRate >= 0.51f && successRate <= 0.8f) {
			return (byte) 1;// 1：高概率
		} else if (successRate >= 0.21f && successRate <= 0.5f) {
			return (byte) 2;// 2：低概率
		} else {
			return (byte) 4;// 4：极低概率
		}
	}

	public void setProbabilitybyRate(double successRate) {
		this.probability = getProbabilitybyRate(successRate);
	}

	public int getHeroNo() {
		return heroNo;
	}

	public void setHeroNo(int heroNo) {
		this.heroNo = heroNo;
	}

	public byte getIsNPC() {
		return isNPC;
	}

	public void setIsNPC(byte isNPC) {
		this.isNPC = isNPC;
	}
}
