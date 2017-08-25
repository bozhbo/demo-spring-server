package com.snail.webgame.game.protocal.club.scene.inorout;

import java.util.List;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

import com.snail.webgame.game.protocal.club.scene.entity.ClubSceneRoleInfo;

public class InOrOutClubSceneResp extends MessageBody {
	private int result; // 为0 说明不是公会成员 直接发送SearchClubReq请求获取全部公会列表
	private int flag; // 0 - 进入 1 - 退出
	private float x;
	private float y;
	private float z;
	private int clubId; // 公会ID
	private String clubName; // 公会名
	private int imageId; // 图标ID
	private String declaration; // 宣言
	private String description; // 公告
	private int memberNum; // 人数
	private int memberNumLimit; // 人数是否满 0 - 不满 1 - 满
	private int build; // 建设
	private int levelLimit; // 入会等级限制
	private int approve; // 是否需要审批 0-不需要 1-需要
	private int level; // 公会等级
	private int count;
	private List<ClubSceneRoleInfo> list;

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("result", 0);
		ps.add("flag", 0);
		ps.add("x", 0);
		ps.add("y", 0);
		ps.add("z", 0);

		ps.add("clubId", 0);
		ps.addString("clubName", "flashCode", 0);
		ps.add("imageId", 0);
		
		ps.addString("declaration", "flashCode", 0);
		ps.addString("description", "flashCode", 0);
		ps.add("memberNum", 0);

		ps.add("memberNumLimit", 0);
		ps.add("build", 0);
		
		ps.add("levelLimit", 0);
		ps.add("approve", 0);
		ps.add("level", 0);

		ps.add("count", 0);
		ps.addObjectArray("list", "com.snail.webgame.game.protocal.club.scene.entity.ClubSceneRoleInfo", "count");

	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getZ() {
		return z;
	}

	public void setZ(float z) {
		this.z = z;
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

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
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

	public int getBuild() {
		return build;
	}

	public void setBuild(int build) {
		this.build = build;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public List<ClubSceneRoleInfo> getList() {
		return list;
	}

	public void setList(List<ClubSceneRoleInfo> list) {
		this.list = list;
	}

	public String getDeclaration() {
		return declaration;
	}

	public void setDeclaration(String declaration) {
		this.declaration = declaration;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

}
