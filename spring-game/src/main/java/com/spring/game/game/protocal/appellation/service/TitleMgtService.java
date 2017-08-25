package com.snail.webgame.game.protocal.appellation.service;

import java.util.Map;

import com.snail.webgame.game.cache.HeroInfoMap;
import com.snail.webgame.game.cache.RoleInfoMap;
import com.snail.webgame.game.common.ErrorCode;
import com.snail.webgame.game.common.util.CommonUtil;
import com.snail.webgame.game.dao.RoleDAO;
import com.snail.webgame.game.info.HeroInfo;
import com.snail.webgame.game.info.RoleInfo;
import com.snail.webgame.game.info.RoleLoadInfo;
import com.snail.webgame.game.protocal.appellation.entity.ResetTitleInfosReq;
import com.snail.webgame.game.protocal.appellation.entity.ResetTitleInfosResp;
import com.snail.webgame.game.protocal.appellation.get.GetAppellationReq;
import com.snail.webgame.game.protocal.appellation.get.GetAppellationResp;
import com.snail.webgame.game.protocal.appellation.manage.AppellationManageReq;
import com.snail.webgame.game.protocal.appellation.manage.AppellationManageResp;
import com.snail.webgame.game.protocal.hero.service.HeroService;
import com.snail.webgame.game.xml.cache.ChenghaoXMLInfoMap;
import com.snail.webgame.game.xml.info.ChenghaoXMLInfo;

public class TitleMgtService {

	/**
	 * 目前无用
	 * @param roleId
	 * @param req
	 * @return
	 */
	public GetAppellationResp checkTitle(int roleId, GetAppellationReq req) {
		GetAppellationResp resp = new GetAppellationResp();
		
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if(roleInfo == null){
			
		}
		
		return resp;
	}

	/**
	 * 使用称号
	 * @param roleId
	 * @param req
	 * @return
	 */
	public AppellationManageResp useTitle(int roleId, AppellationManageReq req) {
		AppellationManageResp resp = new AppellationManageResp();
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if(roleInfo == null){
			resp.setResult(ErrorCode.TITLE_ERROR_1);
		}
		
		synchronized(roleInfo){
			RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
			if(roleLoadInfo == null){
				resp.setResult(ErrorCode.TITLE_ERROR_2);
			}
			
			resp.setId(req.getId());
			resp.setChenghaoType(req.getChenghaoType());
			
			if(req.getChenghaoType() == 0){
				//不使用
				removeTitle(req.getId(), roleInfo, resp);
			}else if(req.getChenghaoType() == 1){
				//使用
				uesTitle(req.getId(), roleInfo, resp);
				
			}else{
				resp.setResult(ErrorCode.TITLE_ERROR_3);
			}
			
		}
		
		return resp;
	}

	/**
	 * 使用称号
	 * @param titieId chenghao.xml的No
	 * @param roleLoadInfo
	 * @param resp
	 */
	private void uesTitle(int titleId, RoleInfo roleInfo, AppellationManageResp resp) {
		Map<Integer, Long> map = CommonUtil.String2MapByValueLong(roleInfo.getRoleLoadInfo().getAllAppellation());

		int check = titleCheck(map, titleId, roleInfo);
		if(check != 1){
			resp.setResult(check);
			return;
		}
		
		
		if(!RoleDAO.getInstance().updateNowAppellation(roleInfo.getId(), titleId + ":" + map.get(titleId))){
			resp.setResult(ErrorCode.TITLE_ERROR_8);
			return;
		}
		
		roleInfo.getRoleLoadInfo().setNowAppellation(titleId + ":" + map.get(titleId));
		
		HeroInfo mainHeroInfo = HeroInfoMap.getMainHeroInfo(roleInfo);
		
		if(mainHeroInfo != null){
			HeroService.refeshHeroProperty(roleInfo, mainHeroInfo);
			
			resp.setFightValue(mainHeroInfo.getFightValue());
		}
		
		resp.setResult(1);
		
	}

	/**
	 * 不使用称号
	 * @param titieId chenghao.xml的No
	 * @param roleLoadInfo
	 * @param resp
	 */
	private void removeTitle(int titleId, RoleInfo roleInfo, AppellationManageResp resp) {
		Map<Integer, Long> map = CommonUtil.String2MapByValueLong(roleInfo.getRoleLoadInfo().getAllAppellation());

		int check = titleCheck(map, titleId, roleInfo);
		if(check != 1){
			resp.setResult(check);
			return;
		}
		
		
		String titleInfo = roleInfo.getRoleLoadInfo().getNowAppellation();
		if(titleInfo == null || "".equals(titleInfo)){
			resp.setResult(ErrorCode.TITLE_ERROR_9);
			return;
		}
		
		String[] strs = titleInfo.split(":");
		int title = 0;
		//long time = 0;
		
		try{
			title = Integer.parseInt(strs[0]);
			//time = Long.parseLong(strs[1]);
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		if(titleId != title){
			resp.setResult(ErrorCode.TITLE_ERROR_9);
			return;
		}
		
		if(!RoleDAO.getInstance().updateNowAppellation(roleInfo.getId(), "")){
			resp.setResult(ErrorCode.TITLE_ERROR_10);
			return;
		}
		
		roleInfo.getRoleLoadInfo().setNowAppellation("");
		
		
		HeroInfo mainHeroInfo = HeroInfoMap.getMainHeroInfo(roleInfo);
		
		if(mainHeroInfo != null){
			HeroService.refeshHeroProperty(roleInfo, mainHeroInfo);
			
			resp.setFightValue(mainHeroInfo.getFightValue());
		}
		
		resp.setResult(1);
				
	}
	
	/**
	 * 
	 * @param map
	 * @return
	 */
	private int titleCheck(Map<Integer, Long> map, int titleId, RoleInfo roleInfo){
		if(map == null || map.size() <= 0){
			return ErrorCode.TITLE_ERROR_4;
		}
		
		if(!map.containsKey(titleId)){
			return ErrorCode.TITLE_ERROR_6;
		}
		
		ChenghaoXMLInfo chenghaoInfo = ChenghaoXMLInfoMap.getChenghaoXMLInfoByNo(titleId);
		if(chenghaoInfo == null){
			return ErrorCode.TITLE_ERROR_5;
		}
		
		Long time = map.get(titleId);
		if(time == null){
			//避免出现空的情况
			if(chenghaoInfo.getKeepTime() > 0){
				time = (long) (chenghaoInfo.getKeepTime() * 1000);
			}else{
				time = (long) chenghaoInfo.getKeepTime();
			}
			
			map.put(titleId, time);
		}
		
		if(time != null && time > 0 && System.currentTimeMillis() - time > 0){
			//过期的称号清理掉
			map.remove(titleId);
			RoleDAO.getInstance().updateAllAppellation(roleInfo.getId(), CommonUtil.Map2String(map));
			return ErrorCode.TITLE_ERROR_7;
			
		}
		
		return 1;
	}

	/**
	 * 刷新称号
	 * @param roleId
	 * @param req
	 * @return
	 */
	public ResetTitleInfosResp refreshTitle(int roleId, ResetTitleInfosReq req) {
		ResetTitleInfosResp resp = new ResetTitleInfosResp();
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if(roleInfo == null){
			resp.setResult(ErrorCode.TITLE_ERROR_1);
		}
		
		synchronized(roleInfo){
			RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
			if(roleLoadInfo == null){
				resp.setResult(ErrorCode.TITLE_ERROR_2);
			}
		}
		

		Map<Integer, Long> map = CommonUtil.String2MapByValueLong(roleInfo.getRoleLoadInfo().getAllAppellation());
		
		TitleService.titlesReset(roleInfo, map);//刷新称号
		//使用的称号未做检测
		resp.setTitles(CommonUtil.Map2String(map));
		
		resp.setTitle(TitleService.nowTitleCheck(roleInfo, map));
		
		HeroInfo mainHeroInfo = HeroInfoMap.getMainHeroInfo(roleInfo);
		
		if(mainHeroInfo != null){
			HeroService.refeshHeroProperty(roleInfo, mainHeroInfo);
			
			resp.setFightValue(mainHeroInfo.getFightValue());
		}

		
		resp.setResult(1);
		
		return resp;
	}
	
}
