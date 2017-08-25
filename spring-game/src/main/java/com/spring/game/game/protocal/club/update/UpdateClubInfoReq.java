package com.snail.webgame.game.protocal.club.update;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class UpdateClubInfoReq extends MessageBody {
	private int clubId;
	private String declaration; // 宣言
	private String description; // 公告
	private int imageId; // 公会头像Id/旗帜
	private int flag;// 是否需要审批 0-不需要 1-需要
	private int levelLimit; //公会加入等级限制

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("clubId", 0);
		ps.addString("declaration", "flashCode", 0);
		ps.addString("description", "flashCode", 0);
		ps.add("imageId", 0);
		ps.add("flag", 0);
		ps.add("levelLimit", 0);
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

	public int getImageId() {
		return imageId;
	}

	public void setImageId(int imageId) {
		this.imageId = imageId;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public int getClubId() {
		return clubId;
	}

	public void setClubId(int clubId) {
		this.clubId = clubId;
	}

	public int getLevelLimit() {
		return levelLimit;
	}

	public void setLevelLimit(int levelLimit) {
		this.levelLimit = levelLimit;
	}

}
