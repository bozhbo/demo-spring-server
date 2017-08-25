package com.snail.webgame.game.protocal.arena.service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.epilot.ccf.config.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.game.cache.FightArenaInfoMap;
import com.snail.webgame.game.cache.HeroInfoMap;
import com.snail.webgame.game.cache.RoleInfoMap;
import com.snail.webgame.game.common.DiffMail;
import com.snail.webgame.game.common.DiffMailMessage;
import com.snail.webgame.game.common.ETimeMessageType;
import com.snail.webgame.game.common.ErrorCode;
import com.snail.webgame.game.common.GameValue;
import com.snail.webgame.game.common.HeroPropertyInfo;
import com.snail.webgame.game.common.HeroRecord;
import com.snail.webgame.game.common.fightdata.DropInfo;
import com.snail.webgame.game.common.util.RandomUtil;
import com.snail.webgame.game.common.xml.cache.HeroXMLInfoMap;
import com.snail.webgame.game.common.xml.info.HeroLevelXMLUpgrade;
import com.snail.webgame.game.common.xml.info.HeroSkillXMLInfo;
import com.snail.webgame.game.common.xml.info.HeroSkillXMLUpgrade;
import com.snail.webgame.game.common.xml.info.HeroXMLInfo;
import com.snail.webgame.game.common.xml.info.HeroXMLSkill;
import com.snail.webgame.game.condtion.ConditionType;
import com.snail.webgame.game.core.GameLogService;
import com.snail.webgame.game.dao.MailDAO;
import com.snail.webgame.game.info.FightArenaInfo;
import com.snail.webgame.game.info.FightDeployInfo;
import com.snail.webgame.game.info.HeroInfo;
import com.snail.webgame.game.info.MailInfo.MailAttachment;
import com.snail.webgame.game.info.RoleInfo;
import com.snail.webgame.game.info.log.RoleArenaLog;
import com.snail.webgame.game.protocal.arena.query.ArenaLog;
import com.snail.webgame.game.protocal.arena.query.ArenaRe;
import com.snail.webgame.game.protocal.fight.fightend.BattlePrize;
import com.snail.webgame.game.protocal.fightdeploy.service.FightDeployService;
import com.snail.webgame.game.protocal.fightdeploy.view.FightDeployDetailRe;
import com.snail.webgame.game.protocal.hero.service.HeroProService;
import com.snail.webgame.game.protocal.hero.service.HeroRecordService;
import com.snail.webgame.game.protocal.hero.service.HeroService;
import com.snail.webgame.game.protocal.item.service.ItemService;
import com.snail.webgame.game.protocal.mail.service.MailService;
import com.snail.webgame.game.thread.SendServerMsgThread;
import com.snail.webgame.game.xml.cache.ArenaXMLInfoMap;
import com.snail.webgame.game.xml.cache.PropBagXMLMap;
import com.snail.webgame.game.xml.cache.RandomNameXMLMap;
import com.snail.webgame.game.xml.info.ArenaXMLHisPrize;
import com.snail.webgame.game.xml.info.ArenaXMLPrize;
import com.snail.webgame.game.xml.info.DropXMLInfo;

public class ArenaService {

	private static Logger logger = LoggerFactory.getLogger("logs");

	/**
	 * 随机生成数据
	 * @return
	 */
	public static List<FightArenaInfo> getRandoms() {
		List<FightArenaInfo> list = new ArrayList<FightArenaInfo>();
		List<Integer> mainHeros = new ArrayList<Integer>();
		List<Integer> otherHeros = new ArrayList<Integer>();
		HashMap<Integer, HeroXMLInfo> heroMap = HeroXMLInfoMap.getHeroMap();
		for (HeroXMLInfo heroXMLInfo : heroMap.values()) {
			if (heroXMLInfo.getInitial() == 1) {
				mainHeros.add(heroXMLInfo.getNo());
			} else {
				otherHeros.add(heroXMLInfo.getNo());
			}
		}

		int limit = GameValue.ARENA_NPC_RANDOM_LIMIT;
		for (int i = 0; i < limit; i++) {
			int place = i + 1;
			Map<Byte, Integer> radomMap = getRadomDeployHeros(mainHeros, otherHeros);
			FightArenaInfo arena = getRandomFightArenaInfo(place, limit, radomMap);
			if (arena != null) {
				list.add(arena);
			}
		}
		return list;
	}

	/**
	 * 随机生成数据
	 * @param mainHeros
	 * @param otherHeros
	 * @return
	 */
	private static Map<Byte, Integer> getRadomDeployHeros(List<Integer> mainHeros, List<Integer> otherHeros) {
		Map<Byte, Integer> map = new HashMap<Byte, Integer>();
		int rd1 = RandomUtil.getRandom(0, mainHeros.size() - 1);
		map.put((byte) 1, mainHeros.get(rd1));

		int[] rd2 = RandomUtil.getRandomData(4, otherHeros.size() - 1);
		map.put((byte) 2, otherHeros.get(rd2[0]));
		map.put((byte) 3, otherHeros.get(rd2[1]));
		map.put((byte) 4, otherHeros.get(rd2[2]));
		map.put((byte) 5, otherHeros.get(rd2[3]));

		return map;
	}

	/**
	 * 根据排名生成竞技场数据
	 * @param place
	 * @return
	 */
	private static FightArenaInfo getRandomFightArenaInfo(int place, int limit, Map<Byte, Integer> radomMap) {
		FightArenaInfo arenaInfo = new FightArenaInfo();
		arenaInfo.setRoleId(0);

		arenaInfo.setFightDeployMap(getRadomHeroRecord(place, limit, radomMap));
		if (arenaInfo.getFightDeployMap() == null || arenaInfo.getFightDeployMap().size() <= 0) {
			if (logger.isErrorEnabled()) {
				logger.error("Init GAME_FIGHT_ARENA error!!! place=" + place);
			}
			return null;
		}
		HeroRecord hero = arenaInfo.getFightDeployMap().get(HeroInfo.DEPLOY_TYPE_MAIN);
		if (hero == null) {
			if (logger.isErrorEnabled()) {
				logger.error("Init GAME_FIGHT_ARENA error!!! place=" + place);
			}
			return null;
		}
		HeroXMLInfo heroXMLInfo = HeroXMLInfoMap.getHeroXMLInfo(hero.getHeroNo());
		if (heroXMLInfo == null) {
			if (logger.isErrorEnabled()) {
				logger.error("Init GAME_FIGHT_ARENA error!!! place=" + place);
			}
			return null;
		}
		if (heroXMLInfo.getSex() == HeroXMLInfo.SEX_MALE) {
			arenaInfo.setRoleName(RandomNameXMLMap.randomMaleName());
		} else {
			arenaInfo.setRoleName(RandomNameXMLMap.randomFemaleName());
		}

		arenaInfo.setPlace(place);
		arenaInfo.setMaxPlace(place);
		arenaInfo.setInitPlace(place);
		return arenaInfo;
	}

	/**
	 * 根据排名生成竞技场布阵数据
	 * @param place
	 * @return
	 */
	private static Map<Byte, HeroRecord> getRadomHeroRecord(int place, int limit, Map<Byte, Integer> radomMap) {
		int maxdeployType = radomMap.size();
		ArenaXMLHisPrize prize = ArenaXMLInfoMap.getArenaXMLHisPrizebyPlace(place);
		if (prize != null) {
			maxdeployType = prize.getHeroNum();
		}
		if (maxdeployType > radomMap.size()) {
			maxdeployType = radomMap.size();
		}
		Map<Byte, HeroRecord> fightDeployMap = new HashMap<Byte, HeroRecord>();
		for (byte deployType = 1; deployType <= maxdeployType; deployType++) {
			Integer heroNo = radomMap.get(deployType);
			if (heroNo == null) {
				return null;
			}
			HeroXMLInfo heroXMLInfo = HeroXMLInfoMap.getHeroXMLInfo(heroNo);
			if (heroXMLInfo == null) {
				return null;
			}

			HeroRecord record = new HeroRecord();
			record.setId(place * 10 + deployType);
			record.setHeroNo(heroNo);
			record.setDeployStatus(deployType);

			record.setSoldierLevel(1);

			int lv = 19 - (int) Math.round(Math.pow(place, 0.26));
			HeroLevelXMLUpgrade upgrade = null;
			if (heroXMLInfo.getInitial() == 1) {
				if (lv > HeroXMLInfoMap.getMaxMainLv()) {
					lv = HeroXMLInfoMap.getMaxMainLv();
				}
				// 1:初始武将 0:非初始武将
				upgrade = HeroXMLInfoMap.getMainLvMap().get(lv);
			} else {
				if (lv > HeroXMLInfoMap.getMaxOtherLv()) {
					lv = HeroXMLInfoMap.getMaxOtherLv();
				}
				upgrade = HeroXMLInfoMap.getOtherLvMap().get(lv);
			}
			if (upgrade == null) {
				return null;
			}
			record.setHeroLevel((int) lv);

			if (heroXMLInfo.getInitial() == 0) {				
				// 角色没有觉醒
				int quality = (int) Math.round(Math.pow(limit / (place * 1.0), 0.1));
				if (quality > HeroXMLInfoMap.getMaxColor()) {
					quality = HeroXMLInfoMap.getMaxColor();
				}
				if (heroXMLInfo.getColourMap().get(quality) == null) {
					return null;
				}
				record.setQuality(quality);
				record.setStar(RandomUtil.getRandom(1, 3));
			}
			
			// 技能等级 为英雄, 当前等级能学习的最高等级最大数量的技能
			Map<Integer, HeroXMLSkill> skillMap = heroXMLInfo.getSkillMap();
			for (HeroXMLSkill xmlSkill : skillMap.values()) {
				int skillNo = xmlSkill.getSkillNo();
				int skillPos = xmlSkill.getSkillPos();
				HeroSkillXMLInfo skillXml = HeroXMLInfoMap.getHeroSkillXMLInfo(skillPos);
				if (skillXml == null) {
					return null;
				}
				if (heroXMLInfo.getInitial() == 0) {
					if (record.getQuality() < skillXml.getOtherColorOpen()) {
						continue;
					}

				} else {
					if (record.getHeroLevel() < skillXml.getMainLvOpen()) {
						continue;
					}
				}

				int skillLevel = 0;
				for (HeroSkillXMLUpgrade skillUp : skillXml.getUpMap().values()) {
					if (skillUp.getHeroLevel() <= record.getHeroLevel() && skillUp.getSkillLevel() > skillLevel) {
						skillLevel = skillUp.getSkillLevel();
					}
				}
				if (skillLevel > 0) {
					record.getSkillMap().put(skillNo, skillLevel);
				}
			}
			fightDeployMap.put(deployType, record);
		}
		return fightDeployMap;
	}

	/**
	 * 根据规则获取匹配战斗用户
	 * @param rolePlace
	 * @return
	 */
	public static int recalMatchs(FightArenaInfo arenaInfo) {
		int place = arenaInfo.getPlace();
		int place1 = 0;
		int place2 = 0;
		int place3 = 0;

		switch (place) {
		case 1:
			place1 = 2;
			place2 = 3;
			place3 = 4;
			break;
		case 2:
			place1 = 1;
			place2 = 3;
			place3 = 4;
			break;
		case 3:
			place1 = 1;
			place2 = 2;
			place3 = 4;
			break;
		case 4:
			place1 = 1;
			place2 = 2;
			place3 = 3;
			break;
		default:
			long qj1 = Math.round(place - Math.pow(place, 0.9));
			long qj3 = Math.round(Math.pow(place - 1, 0.996));
			long qj2 = Math.round((qj3 - qj1) * 0.3 + qj1);

			place1 = (int) RandomUtils.nextLong(qj1, qj2);
			place2 = (int) RandomUtils.nextLong(qj2, qj3);
			place3 = (int) RandomUtils.nextLong(qj3, place);
			break;
		}

		List<FightArenaInfo> matchs = new ArrayList<FightArenaInfo>();
		if (place1 > 0) {
			FightArenaInfo info1 = FightArenaInfoMap.getFightArenaInfobyPlace(place1);
			if (info1 != null) {
				matchs.add(info1);
			} else {
				return ErrorCode.ARENA_MATCH_ERROR_3;
			}
		}
		if (place2 > 0) {
			FightArenaInfo info2 = FightArenaInfoMap.getFightArenaInfobyPlace(place2);
			if (info2 != null) {
				matchs.add(info2);
			} else {
				return ErrorCode.ARENA_MATCH_ERROR_4;
			}
		}

		if (place3 > 0) {
			FightArenaInfo info3 = FightArenaInfoMap.getFightArenaInfobyPlace(place3);
			if (info3 != null) {
				matchs.add(info3);
			} else {
				return ErrorCode.ARENA_MATCH_ERROR_4;
			}
		}
		arenaInfo.setMatchs(matchs);
		return 1;
	}

	/**
	 * 获取匹配战斗信息
	 * @param arenaInfo
	 * @return
	 */
	public static List<ArenaRe> getMatchs(FightArenaInfo arenaInfo) {
		List<ArenaRe> list = new ArrayList<ArenaRe>();
		List<FightArenaInfo> matchs = arenaInfo.getMatchs();
		if (matchs != null) {
			for (FightArenaInfo info : matchs) {
				list.add(getArenaRe(info));
			}
		}
		return list;
	}

	/**
	 * 添加竞技场信息
	 * @param roleId
	 * @return
	 */
	public static FightArenaInfo createFightArena(RoleInfo roleInfo) {
		FightArenaInfo arenaInfo = new FightArenaInfo();
		arenaInfo.setRoleId(roleInfo.getId());
		arenaInfo.setRoleName(roleInfo.getRoleName());
		arenaInfo.setFightDeployMap(null);

		arenaInfo.setPlace(FightArenaInfoMap.getMaxPlaceAndIncrement());
		arenaInfo.setMaxPlace(arenaInfo.getPlace());
		arenaInfo.setInitPlace(arenaInfo.getPlace());

		return arenaInfo;
	}

	/**
	 * 获取ArenaRe
	 * @param roleInfo
	 * @param arenaInfo
	 * @return
	 */
	public static ArenaRe getArenaRe(FightArenaInfo arenaInfo) {
		ArenaRe re = new ArenaRe();
		re.setArenaId((int) arenaInfo.getId());
		int roleId = arenaInfo.getRoleId();
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo != null) {
			re.setRoleId((int) roleId);
			re.setRoleName(roleInfo.getRoleName());

			re.setMaxPlace(arenaInfo.getMaxPlace());
			re.setPlace(arenaInfo.getPlace());

			// 布阵信息
			List<FightDeployDetailRe> deployHeros = FightDeployService.getFightDeployDetailList(roleInfo);
			re.getHeros().addAll(deployHeros);
			re.setSize(deployHeros.size());
		} else {
			re.setRoleId(0);
			re.setRoleName(arenaInfo.getRoleName());

			re.setMaxPlace(arenaInfo.getMaxPlace());
			re.setPlace(arenaInfo.getPlace());

			Map<Byte, HeroRecord> fightDeployMap = arenaInfo.getFightDeployMap();
			if (fightDeployMap != null) {
				FightDeployDetailRe detailRe = null;
				for (HeroRecord info : fightDeployMap.values()) {
					detailRe = FightDeployService.getFightDeployDetailRe(null, fightDeployMap, info,
							GameValue.ARENA_NPC_EQUIP_RATE);
					if (detailRe != null) {
						detailRe.setFightValue(getFightValue(null, fightDeployMap, info, arenaInfo, GameValue.ARENA_NPC_EQUIP_RATE));
						re.getHeros().add(detailRe);
					}
				}
			}
			re.setSize(re.getHeros().size());
		}

		return re;
	}

	public static int getFightValue(RoleInfo roleInfo, Map<Byte, HeroRecord> map,HeroRecord record, FightArenaInfo arenaInfo, double rate) {		
		HeroPropertyInfo total = ArenaXMLInfoMap.getHeroPro(record.getHeroNo(), arenaInfo.getInitPlace(),
				HeroProService.getProRate(rate));
		if (total == null) {
			total = HeroRecordService.recalHeroRecordTotalPro(map, record,
					GameValue.ARENA_NPC_EQUIP_RATE);
		}
		if (total != null && total.getLookRange() < GameValue.ARENA_LOOK_RANGE) {
			total.setLookRange(GameValue.ARENA_LOOK_RANGE);
		}
		if (total != null) {
			if(record.getFightValue() == 0) {
				int fightValue = HeroService.recalHeroRecordFightValue(record,total, record.getSkillMap());
				record.setFightValue(fightValue);
			}
			return record.getFightValue();		
		}
		return 0;
	}
	
	/**
	 * 获取竞技场战报
	 * @param arenaInfo
	 * @return
	 */
	public static List<ArenaLog> getArenaLogList(FightArenaInfo arenaInfo, RoleInfo roleInfo) {
		List<ArenaLog> list = new ArrayList<ArenaLog>();
		for (RoleArenaLog info : arenaInfo.getLogs()) {
			ArenaLog log = new ArenaLog();
			Map<Byte, HeroRecord> deployMap = null;
			if (arenaInfo.getRoleId() == info.getRoleId()) {
				// 挑战方
				log.setFightRoleId(info.getDefendRoleId());
				log.setFightRoleName(info.getDefendRoleName());

				log.setBeforePlace(info.getBeforePlace());
				log.setAfterPlace(info.getAfterPlace());

				log.setFightResult(info.getBattleResult());
				deployMap = info.getDefendDeployMap();
			} else if (arenaInfo.getRoleId() == info.getDefendRoleId()) {
				// 遭受挑战方
				log.setFightRoleId(info.getRoleId());
				log.setFightRoleName(info.getRoleName());

				log.setBeforePlace(info.getDefendBeforePlace());
				log.setAfterPlace(info.getDefendAfterPlace());

				log.setFightResult(info.getBattleResult() == 1 ? 2 : 1);
				deployMap = info.getDeployMap();
			}

			if (deployMap != null) {
				FightDeployDetailRe detailRe = null;
				for (HeroRecord record : deployMap.values()) {
					detailRe = FightDeployService.getFightDeployDetailRe(roleInfo, deployMap, record,
							GameValue.ARENA_NPC_EQUIP_RATE);
					if (detailRe != null) {
						log.getList().add(detailRe);
					}
				}
			}

			log.setCount(log.getList().size());
			log.setBeginTime(info.getBeginTime().getTime());
			list.add(log);
		}
		return list;
	}

	/**
	 * 添加战斗日志
	 * @param fightResult
	 * @param fightStartTime
	 * @param role
	 * @param defendRole
	 */
	public static void addFightArenaLog(int fightResult, long fightStartTime, FightArenaInfo role, int roleBeforePlace,
			FightArenaInfo defendRole, int defendBeforePlace, List<BattlePrize> prizeList, List<BattlePrize> fpPrizeList) {
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(role.getRoleId());
		if (roleInfo == null) {
			return;
		}

		RoleArenaLog log = new RoleArenaLog();
		log.setAccount(roleInfo.getAccount());
		log.setArenaId(role.getId());
		log.setRoleId(role.getRoleId());
		log.setRoleName(role.getRoleName());
		log.setBeforePlace(roleBeforePlace);
		log.setAfterPlace(role.getPlace());

		List<HeroInfo> list = HeroInfoMap.getFightDeployHero(roleInfo.getId());
		for (HeroInfo heroInfo : list) {
			HeroRecord record = HeroRecordService.getDeployHeroRecord(heroInfo, heroInfo.getDeployStatus());
			if (record != null) {
				log.getDeployMap().put(record.getDeployStatus(), record);
			}
		}

		log.setDefendArenaId(defendRole.getId());
		log.setDefendRoleId(defendRole.getRoleId());
		log.setDefendRoleName(defendRole.getRoleName());
		log.setDefendBeforePlace(defendBeforePlace);
		log.setDefendAfterPlace(defendRole.getPlace());
		RoleInfo defendRoleInfo = RoleInfoMap.getRoleInfo(defendRole.getRoleId());
		if (defendRoleInfo != null) {
			List<FightDeployInfo> fightDeployList = FightDeployService.getRoleFightDeployBy(defendRole.getRoleId());
			for (FightDeployInfo info : fightDeployList) {
				HeroInfo heroInfo = HeroInfoMap.getHeroInfo(info.getRoleId(), info.getHeroId());
				if (heroInfo == null) {
					continue;
				}
				HeroRecord record = HeroRecordService.getDeployHeroRecord(heroInfo, info.getDeployPos());
				if (record != null) {
					log.getDefendDeployMap().put(record.getDeployStatus(), record);
				}
			}
		} else {
			log.setDefendDeployMap(defendRole.getFightDeployMap());
		}
		log.setGetItem(GameLogService.getItem(prizeList, fpPrizeList));
		log.setUseEnergy(GameValue.ARENA_FIGHT_COST_ENERGY);

		log.setBattleResult(fightResult);
		log.setBeginTime(new Timestamp(fightStartTime));
		log.setCreateTime(new Timestamp(System.currentTimeMillis()));

		if (role.getLogs().size() < GameValue.ARENA_FIGHT_LOG_LIMIT) {
			role.getLogs().add(log);
		} else {
			role.getLogs().add(log);
			role.getLogs().remove(0);
		}

		if (defendRole.getLogs().size() < GameValue.ARENA_FIGHT_LOG_LIMIT) {
			defendRole.getLogs().add(log);
		} else {
			defendRole.getLogs().add(log);
			defendRole.getLogs().remove(0);
		}
		GameLogService.insertArenaLog(log);
	}

	/**
	 * 历史最高排名提升奖励
	 * @param roleInfo
	 * @param hisPlaceGold
	 * @param beforeMaxPlace
	 * @param currMaxPlace
	 */
	public static int sendMaxPlaceUpReward(RoleInfo roleInfo, int hisPlaceGold, int beforeMaxPlace, int currMaxPlace) {
		List<MailAttachment> attachments = new ArrayList<MailAttachment>();
		if (hisPlaceGold > 0) {
			MailAttachment att = new MailAttachment(ConditionType.TYPE_COIN.getName(), hisPlaceGold, 0, 0);
			attachments.add(att);
		}

		String attachment = MailDAO.encoderAttachment(attachments);
		if (attachment.length() > 0) {
			String title = Resource.getMessage("game", "ARENA_PRIZE_TITLE");
			String content = Resource.getMessage("game", "ARENA_PRIZE_CONTENT");
			String reserve = beforeMaxPlace + "," + currMaxPlace;
			MailService.pushMailPrize(roleInfo.getId() + "", attachment, title, content, reserve);
		}
		return 1;
	}

	/**
	 * 每天 21：00 发放竞技场排名奖励
	 */
	public static void sendPlaceReward() {
		Map<Integer, FightArenaInfo> roleMap = FightArenaInfoMap.getRoleMap();
		if (roleMap != null) {
			for (FightArenaInfo info : roleMap.values()) {
				int roleId = info.getRoleId();
				RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
				if (roleInfo != null) {
					synchronized (roleInfo) {
						sendPlaceReward(roleInfo, info);
					}
				}
			}
		}
	}

	/**
	 * 竞技场排名奖励
	 * @param roleInfo
	 * @param beforeMaxPlace
	 * @param currMaxPlace
	 * @return
	 */
	private static int sendPlaceReward(RoleInfo roleInfo, FightArenaInfo arenaInfo) {
		int place = arenaInfo.getPlace();
		ArenaXMLPrize prize = ArenaXMLInfoMap.getArenaXMLbyPlace(place);
		if (prize == null) {
			return ErrorCode.INIT_SKILL_ERROR_3;
		}
		List<DropXMLInfo> list = PropBagXMLMap.getPropBagXMLListbyStr(prize.getPlaceDropNoStr());
		// 奖励
		List<DropInfo> addList = new ArrayList<DropInfo>();

		ItemService.getDropXMLInfo(roleInfo, list, addList);

		List<MailAttachment> attachments = new ArrayList<MailAttachment>();
		MailAttachment att = null;
		for (DropInfo drop : addList) {
			att = new MailAttachment(drop.getItemNo(), drop.getItemNum(), NumberUtils.toInt(drop.getParam()), 0);
			attachments.add(att);
		}

		String attachment = MailDAO.encoderAttachment(attachments);
		if (attachment.length() > 0) {
			String title = Resource.getMessage("game", "ARENA_PRIZE_TITLE1");
			String content = Resource.getMessage("game", "ARENA_PRIZE_CONTENT1");
			SimpleDateFormat time = new SimpleDateFormat("HH:mm");
			String reserve = time.format(System.currentTimeMillis()) + "," + place;

			MailService.pushMailPrize(roleInfo.getId() + "", attachment, title, content, reserve);
		}
		return 1;
	}

	/**
	 * 每天 21：00 发放竞技场排名奖励
	 * @return
	 */
	public static void sendPlaceRewardMailInfo() {
		Map<Integer, FightArenaInfo> roleMap = FightArenaInfoMap.getRoleMap();
		List<DiffMail> diffMailList = new ArrayList<DiffMail>();
		for (FightArenaInfo info : roleMap.values()) {
			int roleId = info.getRoleId();
			RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
			if (roleInfo != null) {
				DiffMail difMail = ArenaService.getArenaPlaceRewardMailInfo(roleInfo, info);
				if (difMail != null) {
					diffMailList.add(difMail);
				}
			}
		}
		DiffMailMessage diffMailMessage = new DiffMailMessage(ETimeMessageType.SEND_BATCH_DIFF_MAIL, diffMailList);
		SendServerMsgThread.addMessage(diffMailMessage);
	}

	/**
	 * 获取竞技场排名奖励邮件信息
	 * @param roleInfo
	 * @param arenaInfo
	 * @return
	 */
	private static DiffMail getArenaPlaceRewardMailInfo(RoleInfo roleInfo, FightArenaInfo arenaInfo) {
		int place = arenaInfo.getPlace();
		ArenaXMLPrize prize = ArenaXMLInfoMap.getArenaXMLbyPlace(place);
		if (prize == null) {
			logger.info("getPlaceReward error palce=" + place + "roleId=" + roleInfo.getId());
			return null;
		}
		List<DropXMLInfo> list = PropBagXMLMap.getPropBagXMLListbyStr(prize.getPlaceDropNoStr());
		// 奖励
		List<DropInfo> addList = new ArrayList<DropInfo>();

		ItemService.getDropXMLInfo(roleInfo, list, addList);

		List<MailAttachment> attachments = new ArrayList<MailAttachment>();
		MailAttachment att = null;
		for (DropInfo drop : addList) {
			att = new MailAttachment(drop.getItemNo(), drop.getItemNum(), NumberUtils.toInt(drop.getParam()), 0);
			attachments.add(att);
		}
		String attachment = MailDAO.encoderAttachment(attachments);
		if (attachment.length() <= 0) {
			logger.info("getPlaceReward error palce=" + place + "roleId=" + roleInfo.getId());
			return null;
		}

		String title = Resource.getMessage("game", "ARENA_PRIZE_TITLE1");
		String content = Resource.getMessage("game", "ARENA_PRIZE_CONTENT1");
		SimpleDateFormat time = new SimpleDateFormat("HH:mm");
		String reserve = time.format(System.currentTimeMillis()) + "," + place;

		DiffMail diffmail = new DiffMail(roleInfo.getId(), attachment, content, title, reserve);

		return diffmail;
	}
	
	/**
	 * 发送排名变化的邮件提醒
	 * @param roleInfo
	 * @param fightRoleName
	 * @param oldPlace
	 * @param place
	 */
	public static void sendDefendPalcechanged(RoleInfo roleInfo,String fightRoleName,int oldPlace,int place){
		if(place <= oldPlace || roleInfo == null){
			return;
		}
		String title = Resource.getMessage("game", "ARENA_PRIZE_TITLE2");
		String content = Resource.getMessage("game", "ARENA_PRIZE_CONTENT2");
		String reserve = fightRoleName + "," + oldPlace+ "," + place;
		List<DiffMail> diffMailList = new ArrayList<DiffMail>();
		diffMailList.add(new DiffMail(roleInfo.getId(), "", content, title, reserve));
		DiffMailMessage diffMailMessage = new DiffMailMessage(ETimeMessageType.SEND_BATCH_DIFF_MAIL, diffMailList);
		SendServerMsgThread.addMessage(diffMailMessage);
	}
}
