package com.snail.webgame.game.protocal.guide.service;

import org.apache.mina.common.IoSession;
import org.epilot.ccf.core.protocol.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.engine.common.ServerName;
import com.snail.webgame.game.cache.RoleInfoMap;
import com.snail.webgame.game.cache.ServerMap;
import com.snail.webgame.game.common.ErrorCode;
import com.snail.webgame.game.common.GameMessageHead;
import com.snail.webgame.game.common.util.Command;
import com.snail.webgame.game.dao.RoleDAO;
import com.snail.webgame.game.info.RoleInfo;
import com.snail.webgame.game.info.RoleLoadInfo;
import com.snail.webgame.game.protocal.guide.query.QueryGuideResp;
import com.snail.webgame.game.protocal.guide.update.UpdateStepReq;
import com.snail.webgame.game.protocal.guide.update.UpdateStepResp;

/**
 * 新手引导
 * @author wanglinhui
 *
 */
public class GuideMgtService {

	private static final Logger logger = LoggerFactory.getLogger("logs");
	
	/**
	 * 查询新手引导信息
	 * @param roleId
	 * @return
	 */
	public static QueryGuideResp queryGuide(int roleId){
		
		QueryGuideResp resp = new QueryGuideResp();
		
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if(roleInfo == null){
			resp.setResult(ErrorCode.ROLE_REF_GUIDE_ERROR_5);
			return resp;
		}
		RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
		if(roleLoadInfo==null){
			resp.setResult(ErrorCode.ROLE_REF_GUIDE_ERROR_5);
			return resp;
		}
		resp.setRate(roleLoadInfo.getGuideInfo());
		resp.setResult(1);
		
		return resp;
	}

	/**
	 * 更新新手引导步骤
	 * @param req
	 * @return
	 */
	public UpdateStepResp updateGuideStep(int roleId, UpdateStepReq req) {
		UpdateStepResp resp = new UpdateStepResp();

		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			logger.error("Not found RoleInfo from RoleInfoMap with roleId={}", roleId);
			resp.setResult(ErrorCode.ROLE_REF_GUIDE_ERROR_2);
			return resp;
		}
		RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
		if(roleLoadInfo==null){
			resp.setResult(ErrorCode.ROLE_REF_GUIDE_ERROR_2);
			return resp;
		}
		synchronized (roleInfo) {
			String rate = req.getRate();
			resp.setResult(updateGuideInfo(roleLoadInfo, rate));

		}
		return resp;
	}
	
	/**
	 * 更新新手引导
	 * @param roleLoadInfo
	 * @param rate
	 * @param sqlMapClient
	 * @return 返回值
	 */
	private int updateGuideInfo(RoleLoadInfo roleLoadInfo, String rate){
		if(rate == null || "".equals(rate)){
			return ErrorCode.ROLE_REF_GUIDE_ERROR_4;
		}
		//校验是否有1被置成0
		String guideInfoStr = roleLoadInfo.getGuideInfo();
		if(guideInfoStr == null){
			guideInfoStr = "";
		}
		String[] guideInfo_server = guideInfoStr.split(",");
		String[] guideInfo_client = rate.split(",");
		String[] guideInfo_base = new String[guideInfo_server.length < guideInfo_client.length ? guideInfo_client.length : guideInfo_server.length];
		for(int i = 0; i < guideInfo_server.length; i++){
			guideInfo_base[i] = guideInfo_server[i];
		}
		for(int i = 0; i < guideInfo_base.length; i++){
			if(guideInfo_client.length <= i){
				break;
			}
			//只将0转成1
			if("1".equals(guideInfo_client[i])){
				guideInfo_base[i] = "1";
			}
		}
		//将数组拼凑成标准格式
		StringBuffer guideInfoUpdate = new StringBuffer();
		for(String oneGuideInfo : guideInfo_base){
			if("1".equals(oneGuideInfo)){
				guideInfoUpdate.append("1,");
			}else{
				guideInfoUpdate.append("0,");
			}
		}
		rate = guideInfoUpdate.substring(0, guideInfoUpdate.length()-1);
		if(!RoleDAO.getInstance().updateGuideData(roleLoadInfo.getId(), rate)){
			return ErrorCode.ROLE_REF_GUIDE_ERROR_3;
		}
		roleLoadInfo.setGuideInfo(rate);
		return 1;
	}
	
	/**
	 * 获取某一位新手引导编号
	 * @param roleInfo
	 * @param index
	 * @return 返回值
	 */
	public static int getGuideIndexNum(RoleLoadInfo roleLoadInfo, int index){
		String guideInfoStr = roleLoadInfo.getGuideInfo();
		if(guideInfoStr == null || "".equals(guideInfoStr.trim())){
			return 0;
		}
		String[] guideInfo = guideInfoStr.split(",");
		if(guideInfo.length < index+1){
			return 0;
		}else{
			String num = guideInfo[index];
			if("".equals(num)){
				return 0;
			}else{
				return Integer.parseInt(num);
			}
		}
	}
	
	/**
	 * 设定某一位新手引导类型
	 * @param roleLoadInfo
	 * @param index
	 * @param status
	 * @return 返回值
	 */
	public static boolean updateGuideIndexNum(RoleInfo roleInfo, int index, int status){
		if(index <= 1){
			return false;
		}
		RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
		if(roleLoadInfo==null){
			return false;
		}
//		index--;
		String guideInfoStr = roleLoadInfo.getGuideInfo();
		if(guideInfoStr == null){
//			if(!GuideService.initGuideInfo(roleInfo.getRoleId(), sqlMapClient)){
//				return false;
//			}
//			guideInfoStr = roleInfo.getGuideInfo();
		}
		String[] guideInfo = new String[]{"0"};
		if(guideInfoStr != null){
			guideInfo = guideInfoStr.split(",");
		}
		for(int i = guideInfo.length; i <= index; i++){
			if("".equals(guideInfoStr)){
				guideInfoStr = "0";
			}else{
				guideInfoStr = guideInfoStr + ",0";
			}
		}
		guideInfo = guideInfoStr.split(",");
		guideInfo[index] = status+"";
		StringBuffer guideInfoUpdate = new StringBuffer();
		for(String oneGuideInfo : guideInfo){
			if("1".equals(oneGuideInfo)){
				guideInfoUpdate.append("1,");
			}else{
				guideInfoUpdate.append("0,");
			}
		}
		guideInfoStr = guideInfoUpdate.substring(0, guideInfoUpdate.length()-1);
		if(!RoleDAO.getInstance().updateGuideData(roleLoadInfo.getId(), guideInfoStr)){
			return false;
		}
		roleLoadInfo.setGuideInfo(guideInfoStr);
//		SceneService.sendRoleRefreshMsg(roleLoadInfo.getId(), SceneService.REFRESH_TYPE_GUIDE, null);
		Message message = new Message();
		GameMessageHead head = new GameMessageHead();
		head.setMsgType(Command.QUERY_ROLE_GUIDE_RESP);
		head.setUserID0((int) roleLoadInfo.getId());
		QueryGuideResp resp = new QueryGuideResp();
		resp.setRate(roleLoadInfo.getGuideInfo());
		resp.setResult(1);
		message.setHeader(head);
		message.setBody(resp);
		
		IoSession ioSession = ServerMap.getServerSession(ServerName.GATE_SERVER_NAME + "-" + roleInfo.getGateServerId());
		if(ioSession!=null&&ioSession.isConnected()){
			ioSession.write(message);
		}
		return true;
	}
	
	/**
	 * 处理新手引导
	 * @param roleInfo
	 * @param indexs
	 * @return
	 */
	public static int dealGuideIndexNum(RoleInfo roleInfo, int indexs[]) {
		// 检查新手引导
		for (int index : indexs) {
//			int result = dealGuideIndexNum(roleInfo, index);
//			// 按照顺序每次检查一个
//			if (result != 1) {
//				return result;
//			}
//			break;
			int guideIndex = getGuideIndexNum(roleInfo.getRoleLoadInfo(), index);
			if(guideIndex==0){
				if (!updateGuideIndexNum(roleInfo, index, 1)) {
					return ErrorCode.ROLE_REF_GUIDE_ERROR_3;
				}
				break;
			}
		}
		return 1;
	}

	/**
	 * 处理新手引导
	 * @param roleInfo
	 * @param indexs
	 * @return
	 */
	public static int dealGuideIndexNum(RoleInfo roleInfo, int index) {
		// 检查新手引导
		int guideIndex = getGuideIndexNum(roleInfo.getRoleLoadInfo(), index);
		// 按照顺序每次检查一个
		if (guideIndex == 0) {
			if (!updateGuideIndexNum(roleInfo, index, 1)) {
				return ErrorCode.ROLE_REF_GUIDE_ERROR_3;
			}
		}
		return 1;
	}

}
