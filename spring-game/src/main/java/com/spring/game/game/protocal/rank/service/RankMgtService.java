package com.snail.webgame.game.protocal.rank.service;

import java.util.ArrayList;
import java.util.List;

import com.snail.webgame.game.cache.HeroInfoMap;
import com.snail.webgame.game.cache.RoleInfoMap;
import com.snail.webgame.game.cache.WorldBossMap;
import com.snail.webgame.game.common.ErrorCode;
import com.snail.webgame.game.common.GameValue;
import com.snail.webgame.game.info.HeroInfo;
import com.snail.webgame.game.info.RoleInfo;
import com.snail.webgame.game.info.RoleLoadInfo;
import com.snail.webgame.game.protocal.activity.saodang.ActivitySaodangReq;
import com.snail.webgame.game.protocal.activity.service.ActivityService;
import com.snail.webgame.game.protocal.club.entity.ClubFightInfo;
import com.snail.webgame.game.protocal.rank.rank.RankReq;
import com.snail.webgame.game.protocal.rank.rank.RankResp;

public class RankMgtService {

	public RankResp rank(int roleId, RankReq req) {

		RankResp resp = new RankResp();
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			resp.setResult(ErrorCode.RANK_ERROR_1);
			return resp;
		}
		RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
		if (roleLoadInfo == null) {
			resp.setResult(ErrorCode.RANK_ERROR_1);
			return resp;
		}
		HeroInfo heroInfo = HeroInfoMap.getMainHeroInfo(roleInfo);
		if (heroInfo == null) {
			resp.setResult(ErrorCode.RANK_ERROR_1);
			return resp;
		}
		synchronized (roleInfo) {
			byte type = req.getRankType();
			int page = req.getPage();
			List<RankInfo> list = new ArrayList<RankInfo>();
			List<ClubFightInfo> clubList = new ArrayList<ClubFightInfo>();
			int startPlace = (page - 1) * GameValue.RANK_PAGE_NUM + 1;
			int endPlace = page * GameValue.RANK_PAGE_NUM;

			int myRank = 0;
			long myParam = 0;
			long myPerMax = 0;

			switch (type) {
			case 1: // 等级排行 roleLevelRank
				ActivitySaodangReq s = new ActivitySaodangReq();
				s.setSaodangType(60030);
				ActivityService.saodangExpAction(roleId,s);
				list = RankService.getRoleLevelRank(startPlace, endPlace);
				myRank = roleInfo.getRankLevel();
				if (roleInfo.getLevelRankInfo() != null) {
					myParam = roleInfo.getLevelRankInfo().getParam();
				} else {
					myParam = heroInfo.getHeroLevel();
				}
				break;
			case 2:// 战斗力 RoleInfoMap.getSortFightValuelist();
				list = RankService.getFightValueRank(startPlace, endPlace);
				myRank = roleInfo.getRankFightValue();
				if (roleInfo.getFightValueRankInfo() != null) {
					myParam = roleInfo.getFightValueRankInfo().getParam();
				} else {
					myParam = roleInfo.getFightValue();
				}
				break;
			case 3:// 世界boss伤害 WorldBossMap.bossList
				list = RankService.getAttWorldBossRank(startPlace, endPlace);
				myRank = WorldBossMap.getBossList().contains(roleInfo) ? WorldBossMap.getBossList().indexOf(roleInfo)+1 : 0;
				myParam = roleInfo.getFightWorldBossHp();
				myPerMax = roleInfo.getThisBossBest();
				break;
			case 4:// 武将数量 heroNumRank
				list = RankService.getHeroNumRank(startPlace, endPlace);
				myRank = roleInfo.getRankHeroNum();
				if (roleInfo.getHeroNumRankInfo() != null) {
					myParam = roleInfo.getHeroNumRankInfo().getParam();
				} else {
					myParam = roleInfo.getCommHeroNum();
				}
				break;
			case 5:
				clubList = RankService.getClubFightRank(startPlace, endPlace);
				for(ClubFightInfo clubInfo:clubList){
					if(clubInfo.getClubId() == roleInfo.getClubId()){
						myRank = clubInfo.getRankNum();
						myParam = clubInfo.getTotalFight();
					}
				}
				break;
			default:
				break;

			}
			
			List<RankInfo> tempList = new ArrayList<RankInfo>();
			List<ClubFightInfo> clubTempList = new ArrayList<ClubFightInfo>();
			if (list != null && list.size() > 0) {
				for (RankInfo rankInfo : list) {
					RoleInfo otherInfo = RoleInfoMap.getRoleInfo(rankInfo.getRoleId());
					if (otherInfo == null) {
						continue;
					}
					
					if (type != 3) {
						if (!otherInfo.isShowRank(false)) {
							continue;
						}
					}
					
					tempList.add(rankInfo);
				}
			}else if(clubList != null && clubList.size() > 0){
				for (ClubFightInfo rankInfo : clubList) {
					clubTempList.add(rankInfo);
				}
			}

			resp.setRankType(type);

			resp.setMyRank(myRank);
			resp.setMyParam(myParam);
			resp.setMyPerMax(myPerMax);
			
			if (tempList.size() > 0) {
				resp.setList(tempList);
				resp.setCount(tempList.size());
			}else if(clubTempList.size() > 0){
				resp.setClubList(clubTempList);
				resp.setClubCount(clubTempList.size());
			}

			resp.setResult(1);
		}

		return resp;

	}

}
