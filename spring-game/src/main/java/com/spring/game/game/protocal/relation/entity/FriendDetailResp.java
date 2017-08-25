package com.snail.webgame.game.protocal.relation.entity;

import java.util.ArrayList;
import java.util.List;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class FriendDetailResp extends MessageBody {
	private int result;
	private int pushType; // 0-好友请求 1-好友 2-馈赠 3-移除好友 4- 删除赠送精力请求 5：更新我的好友，
							// 6：更新推荐好友， 7：更新领取精力， 8：更新好友申请， 9：更新黑名单 10:角色下线通知
							// 11 ：黑名单 12 : 好友请求  

	private int count;
	private List<FriendDetailRe> list = new ArrayList<FriendDetailRe>();

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("result", 0);
		ps.add("pushType", 0);
		ps.add("count", 0);
		ps.addObjectArray("list", "com.snail.webgame.game.protocal.relation.entity.FriendDetailRe", "count");
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public int getPushType() {
		return pushType;
	}

	public void setPushType(int pushType) {
		this.pushType = pushType;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public List<FriendDetailRe> getList() {
		return list;
	}

	public void setList(List<FriendDetailRe> list) {
		this.list = list;
	}

}
