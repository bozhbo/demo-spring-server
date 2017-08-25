package com.snail.webgame.game.protocal.club.entity;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class ClubInfoRe extends MessageBody {
	private int clubId; // 公会ID
	private String clubName; // 公会名
	private int level;
	private int imageId; // 图标ID
	private String declaration; // 宣言
	private int memberNum; // 人数
	private int memberNumLimit; // 人数是否满 0 - 不满 1 - 满
	private long createTime; // 公会创建时间
	private int build; // 建设度
	private int levelLimit; // 入会等级限制
	private int isReq; // 已经申请过 0 - 没有申请 1 - 发起申请
	private int isApprove; //0 - 不需要审批 1 - 审批

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("clubId", 0);
		ps.addString("clubName", "flashCode", 0);
		ps.add("level", 0);
		ps.add("imageId", 0);

		ps.addString("declaration", "flashCode", 0);
		ps.add("memberNum", 0);
		ps.add("memberNumLimit", 0);
		ps.add("createTime", 0);
		ps.add("build", 0);
		ps.add("levelLimit", 0);
		ps.add("isReq", 0);
		ps.add("isApprove", 0);
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

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
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

	public int getMemberNumLimit() {
		return memberNumLimit;
	}

	public void setMemberNumLimit(int memberNumLimit) {
		this.memberNumLimit = memberNumLimit;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public int getBuild() {
		return build;
	}

	public void setBuild(int build) {
		this.build = build;
	}

	public int getLevelLimit() {
		return levelLimit;
	}

	public void setLevelLimit(int levelLimit) {
		this.levelLimit = levelLimit;
	}

	public int getIsReq() {
		return isReq;
	}

	public void setIsReq(int isReq) {
		this.isReq = isReq;
	}

	public int getIsApprove() {
		return isApprove;
	}

	public void setIsApprove(int isApprove) {
		this.isApprove = isApprove;
	}

}
