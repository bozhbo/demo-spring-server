package com.snail.webgame.game.protocal.fight.team.rank;

import java.util.List;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

/**
 * 查询组队副本首杀速杀
 * 
 * @author xiasd
 *
 */
public class TeamQueryRankResp extends MessageBody {
	private int quickCount;
	private List<TeamRoleRankRes> quickList;
	private int firstCount;
	private List<TeamRoleRankRes> firstList;
	
	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("quickCount", 0);
		ps.addObjectArray("quickList", "com.snail.webgame.game.protocal.fight.team.rank.TeamRoleRankRes", "quickCount");
		ps.add("firstCount", 0);
		ps.addObjectArray("firstList", "com.snail.webgame.game.protocal.fight.team.rank.TeamRoleRankRes", "firstCount");
	}

	public int getQuickCount() {
		return quickCount;
	}

	public void setQuickCount(int quickCount) {
		this.quickCount = quickCount;
	}

	public List<TeamRoleRankRes> getQuickList() {
		return quickList;
	}

	public void setQuickList(List<TeamRoleRankRes> quickList) {
		this.quickList = quickList;
	}

	public int getFirstCount() {
		return firstCount;
	}

	public void setFirstCount(int firstCount) {
		this.firstCount = firstCount;
	}

	public List<TeamRoleRankRes> getFirstList() {
		return firstList;
	}

	public void setFirstList(List<TeamRoleRankRes> firstList) {
		this.firstList = firstList;
	}
}
