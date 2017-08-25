package com.snail.webgame.game.protocal.scene.queryOtherAI;

import java.util.ArrayList;
import java.util.List;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

import com.snail.webgame.game.protocal.hero.query.HeroDetailRe;


/**
 * 查看场景其它玩家信息
 * @author hongfm
 *
 */
public class QueryOtherAIResp extends MessageBody {

	private int result;
	private int otherRoleId;
	private String otherRolaName;
	private HeroDetailRe heroInfo = new HeroDetailRe();
	
	private int fightNum;
	private int heroCount;
	private List<OtherHeroInfo> heroList = new ArrayList<OtherHeroInfo>();
	
	private int worShipTimes = -1;//被膜拜次数，默认传递-1.如果是-1则是查看玩家的信息，如果大于等于0则做膜拜次数使用
	
	private String clubName; // 公会名
	
	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("result", 0);
		ps.add("otherRoleId", 0);
		ps.addString("otherRolaName", "flashCode", 0);
		ps.addObject("heroInfo");
		ps.add("fightNum", 0);
		ps.add("heroCount", 0);
		ps.addObjectArray("heroList", "com.snail.webgame.game.protocal.scene.queryOtherAI.OtherHeroInfo", "heroCount");
		ps.add("worShipTimes", 0);
		ps.addString("clubName", "flashCode", 0);
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

	public String getOtherRolaName() {
		return otherRolaName;
	}

	public void setOtherRolaName(String otherRolaName) {
		this.otherRolaName = otherRolaName;
	}

	public HeroDetailRe getHeroInfo() {
		return heroInfo;
	}

	public void setHeroInfo(HeroDetailRe heroInfo) {
		this.heroInfo = heroInfo;
	}

	public int getFightNum() {
		return fightNum;
	}

	public void setFightNum(int fightNum) {
		this.fightNum = fightNum;
	}

	public int getHeroCount() {
		return heroCount;
	}

	public void setHeroCount(int heroCount) {
		this.heroCount = heroCount;
	}

	public List<OtherHeroInfo> getHeroList() {
		return heroList;
	}

	public void setHeroList(List<OtherHeroInfo> heroList) {
		this.heroList = heroList;
	}

	public int getWorShipTimes() {
		return worShipTimes;
	}

	public void setWorShipTimes(int worShipTimes) {
		this.worShipTimes = worShipTimes;
	}

	public String getClubName() {
		return clubName;
	}

	public void setClubName(String clubName) {
		this.clubName = clubName;
	}
}
