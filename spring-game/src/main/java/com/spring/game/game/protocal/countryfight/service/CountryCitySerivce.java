package com.snail.webgame.game.protocal.countryfight.service;

import com.snail.webgame.game.protocal.countryfight.office.OfficeUpgradeResp;
import com.snail.webgame.game.protocal.countryfight.queryFightingClub.QueryFightingClubResp;
import com.snail.webgame.game.protocal.countryfight.querycity.QueryCityInfoResp;
import com.snail.webgame.game.protocal.countryfight.xuanzhan.XuanzhanReq;
import com.snail.webgame.game.protocal.countryfight.xuanzhan.XuanzhanResp;

/**
 * 国战业务类
 * 
 * @author xiasd
 *
 */
public class CountryCitySerivce {
	
	/**
	 * 查询城市信息
	 * 
	 * @return
	 */
	public static QueryCityInfoResp queryCityInfo(){
		QueryCityInfoResp resp = new QueryCityInfoResp();
		
		return resp;
	}

	/**
	 * 观战城池战斗信息
	 * 
	 * @return
	 */
	public static QueryFightingClubResp queryFightingClub() {
		QueryFightingClubResp resp = new QueryFightingClubResp();
		
		return resp;
	}

	/**
	 * 宣战
	 * 
	 * @param roleId
	 * @param req
	 * @return
	 */
	public static XuanzhanResp xuanzhan(int roleId, XuanzhanReq req) {
		XuanzhanResp resp = new XuanzhanResp();
		
		return resp;
	}

	/**
	 * 官职升级
	 * 
	 * @param roleId
	 * @param req
	 * @return
	 */
	public static OfficeUpgradeResp officeUpgrade(int roleId, XuanzhanReq req) {
		OfficeUpgradeResp resp = new OfficeUpgradeResp();
		return resp;
	}

	/**
	 * 自己的工会今日战事
	 * 
	 * @return
	 */
	public static QueryFightingClubResp queryMyCityInfo() {
		QueryFightingClubResp resp = new QueryFightingClubResp();
		
		return resp;
	}

}
