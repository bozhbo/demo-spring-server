package com.snail.webgame.game.protocal.club.create;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class CreateClubReq extends MessageBody {
	private String clubName;// 公会名字
	private int imageId; // 图标ID(旗帜)

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.addString("clubName", "flashCode", 0);
		ps.add("imageId", 0);
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

}
