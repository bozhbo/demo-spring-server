package com.snail.webgame.game.protocal.club.msg.processor;

import org.epilot.ccf.config.Resource;
import org.epilot.ccf.core.processor.ProtocolProcessor;
import org.epilot.ccf.core.processor.Request;
import org.epilot.ccf.core.processor.Response;
import org.epilot.ccf.core.protocol.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.game.common.GameMessageHead;
import com.snail.webgame.game.common.util.Command;
import com.snail.webgame.game.protocal.club.msg.ClubEveInfoMsgResp;
import com.snail.webgame.game.protocal.club.msg.ClubJoinRequestInfoMsgResp;
import com.snail.webgame.game.protocal.club.msg.ClubRoleMemberInfoMsgResp;
import com.snail.webgame.game.protocal.club.service.ClubMgtService;

public class ClubInfoMsgProcessor extends ProtocolProcessor{
	private static final Logger logger = LoggerFactory.getLogger("logs");
	
	private ClubMgtService clubMgtService;
	
	public void setClubMgtService(ClubMgtService clubMgtService){
		this.clubMgtService = clubMgtService;
	}

	@Override
	public void execute(Request request, Response response) {
		Message msg = request.getMessage();
		GameMessageHead header = (GameMessageHead) msg.getHeader();
		
		int roleId = header.getUserID0();
		
		ClubInfoMsgProcessorReq req = (ClubInfoMsgProcessorReq)msg.getBody();
		
		int result = 0;
		
		if(req.getReqType() == 1){ //  1 - 事件列表 
			header.setMsgType(Command.CLUB_EVENT_LIST_RESP);
			ClubEveInfoMsgResp resp = clubMgtService.getEveList(roleId, req.getClubId());
			result = resp.getResult();
			msg.setBody(resp);
			
		}else if(req.getReqType() == 2){ //  2 - 请求列表 
			header.setMsgType(Command.CLUB_REQUEST_LIST_RESP);
			ClubJoinRequestInfoMsgResp resp = clubMgtService.getJoinRequstList(roleId, req.getClubId());
			result = resp.getResult();
			msg.setBody(resp);
			
		}else if(req.getReqType() == 3){ //  3 - 成员列表
			header.setMsgType(Command.CLUB_MEMBER_LIST_RESP);
			
			ClubRoleMemberInfoMsgResp resp = clubMgtService.getClubMemberInfoList(roleId, req.getClubId());
			result = resp.getResult();
			msg.setBody(resp);
			
		}else{
			return;
		}
		
		
		
		msg.setHeader(header);
		
		response.write(msg);
		
		if(logger.isInfoEnabled()){
			logger.info(Resource.getMessage("game", "GAME_ROLE_CLUB_INFO_7") + ": result=" + result + ",roleId="
					+ roleId);
		}
		
		
		
	}




}
