package com.snail.webgame.game.protocal.club.info;

import java.util.ArrayList;
import java.util.List;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

import com.snail.webgame.game.protocal.club.entity.ClubEveInfoRe;
import com.snail.webgame.game.protocal.club.entity.ClubRequestInfoRe;
import com.snail.webgame.game.protocal.club.entity.ClubRoleInfoRe;

public class GetClubInfoResp extends MessageBody {
	private int result;
	private int clubId; // 公会ID
	private String clubName; // 公会名
	private int imageId; // 图标ID
	private String declaration; // 宣言
	private String description; // 公告
	private int memberNum; // 人数
	private int memberNumLimit; // 人数是否满 0 - 不满 1 - 满
	private int flag; // 1 - 会长 2 - 副会长 3 - 官员
	private int build; // 建设
	private int levelLimit; // 入会等级限制
	private int approve; // 是否需要审批 0-不需要 1-需要
	private int level; // 公会等级
	private int count;
	private List<ClubRoleInfoRe> list = new ArrayList<ClubRoleInfoRe>();
	private int eveListCount;
	private List<ClubEveInfoRe> eveList = new ArrayList<ClubEveInfoRe>();
	private int requestCount;
	private List<ClubRequestInfoRe> requestList = new ArrayList<ClubRequestInfoRe>();

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("result", 0);
		ps.add("clubId", 0);
		ps.addString("clubName", "flashCode", 0);

		ps.add("imageId", 0);
		ps.addString("declaration", "flashCode", 0);
		ps.addString("description", "flashCode", 0);

		ps.add("memberNum", 0);
		ps.add("memberNumLimit", 0);
		ps.add("flag", 0);

		ps.add("build", 0);
		ps.add("levelLimit", 0);
		ps.add("approve", 0);
		ps.add("level", 0);
		
		ps.add("count", 0);
		ps.addObjectArray("list", "com.snail.webgame.game.protocal.club.entity.ClubRoleInfoRe", "count");

		ps.add("eveListCount", 0);
		ps.addObjectArray("eveList", "com.snail.webgame.game.protocal.club.entity.ClubEveInfoRe", "eveListCount");

		ps.add("requestCount", 0);
		ps.addObjectArray("requestList", "com.snail.webgame.game.protocal.club.entity.ClubRequestInfoRe", "requestCount");

	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public int getClubId() {
		return clubId;
	}

	public void setClubId(int clubId) {
		this.clubId = clubId;
	}

	public String getClubName() {
		return clubName;
	}

	public void setClubName(String clubName) {
		this.clubName = clubName;
	}

	public int getImageId() {
		return imageId;
	}

	public void setImageId(int imageId) {
		this.imageId = imageId;
	}

	public String getDeclaration() {
		return declaration;
	}

	public void setDeclaration(String declaration) {
		this.declaration = declaration;
	}

	public int getMemberNum() {
		return memberNum;
	}

	public void setMemberNum(int memberNum) {
		this.memberNum = memberNum;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getMemberNumLimit() {
		return memberNumLimit;
	}

	public void setMemberNumLimit(int memberNumLimit) {
		this.memberNumLimit = memberNumLimit;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public List<ClubRoleInfoRe> getList() {
		return list;
	}

	public void setList(List<ClubRoleInfoRe> list) {
		this.list = list;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public int getBuild() {
		return build;
	}

	public void setBuild(int build) {
		this.build = build;
	}

	public int getEveListCount() {
		return eveListCount;
	}

	public void setEveListCount(int eveListCount) {
		this.eveListCount = eveListCount;
	}

	public List<ClubEveInfoRe> getEveList() {
		return eveList;
	}

	public void setEveList(List<ClubEveInfoRe> eveList) {
		this.eveList = eveList;
	}

	public int getLevelLimit() {
		return levelLimit;
	}

	public void setLevelLimit(int levelLimit) {
		this.levelLimit = levelLimit;
	}

	public int getApprove() {
		return approve;
	}

	public void setApprove(int approve) {
		this.approve = approve;
	}

	public int getRequestCount() {
		return requestCount;
	}

	public void setRequestCount(int requestCount) {
		this.requestCount = requestCount;
	}

	public List<ClubRequestInfoRe> getRequestList() {
		return requestList;
	}

	public void setRequestList(List<ClubRequestInfoRe> requestList) {
		this.requestList = requestList;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

}
