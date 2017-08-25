package com.snail.webgame.game.protocal.rolemgt.voice;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class QueryRoleVoiceUidResp extends MessageBody {

	private int result;	// 处理结果
	private String roleName; // 角色名称
	private String voiceUid;// UID

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("result", 0);
		ps.addString("roleName", "flashCode", 0);
		ps.addString("voiceUid", "flashCode", 0);
		
	}

	public String getVoiceUid() {
		return voiceUid;
	}

	public void setVoiceUid(String voiceUid) {
		this.voiceUid = voiceUid;
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	
	
}
