package com.snail.webgame.game.protocal.fight.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.mina.common.IoSession;
import org.epilot.ccf.core.protocol.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.engine.common.ServerName;
import com.snail.webgame.game.cache.FightInfoMap;
import com.snail.webgame.game.cache.HeroInfoMap;
import com.snail.webgame.game.cache.ServerMap;
import com.snail.webgame.game.common.ActionType;
import com.snail.webgame.game.common.FightInfo;
import com.snail.webgame.game.common.FightType;
import com.snail.webgame.game.common.GameMessageHead;
import com.snail.webgame.game.common.GameValue;
import com.snail.webgame.game.common.HeroProType;
import com.snail.webgame.game.common.HeroPropertyInfo;
import com.snail.webgame.game.common.HeroRecord;
import com.snail.webgame.game.common.RideRecord;
import com.snail.webgame.game.common.fightdata.DropBagInfo;
import com.snail.webgame.game.common.fightdata.DropInfo;
import com.snail.webgame.game.common.fightdata.FightArmyDataInfo;
import com.snail.webgame.game.common.fightdata.FightSideData;
import com.snail.webgame.game.common.fightdata.HeroSkillDataInfo;
import com.snail.webgame.game.common.util.Command;
import com.snail.webgame.game.common.xml.cache.HeroXMLInfoMap;
import com.snail.webgame.game.common.xml.cache.SoldierXMLInfoMap;
import com.snail.webgame.game.common.xml.info.HeroXMLInfo;
import com.snail.webgame.game.common.xml.info.HeroXMLSkill;
import com.snail.webgame.game.common.xml.info.SoldierXMLInfo;
import com.snail.webgame.game.info.HeroInfo;
import com.snail.webgame.game.info.RideInfo;
import com.snail.webgame.game.info.RoleInfo;
import com.snail.webgame.game.protocal.arena.service.ArenaMgtService;
import com.snail.webgame.game.protocal.equip.service.EquipService;
import com.snail.webgame.game.protocal.fight.fightend.FightEndResp;
import com.snail.webgame.game.protocal.fight.startFight.IntoFightResp;
import com.snail.webgame.game.protocal.hero.service.HeroProService;
import com.snail.webgame.game.protocal.hero.service.HeroRecordService;
import com.snail.webgame.game.protocal.hero.service.HeroService;
import com.snail.webgame.game.protocal.item.service.ItemService;
import com.snail.webgame.game.protocal.mine.service.MineMgtService;
import com.snail.webgame.game.protocal.scene.sys.SceneService1;
import com.snail.webgame.game.protocal.soldier.service.SoldierService;
import com.snail.webgame.game.protocal.weapon.WeaponService;
import com.snail.webgame.game.pvp.competition.request.SkillInfoVo;
import com.snail.webgame.game.xml.cache.GWXMLInfoMap;
import com.snail.webgame.game.xml.cache.PropBagXMLMap;
import com.snail.webgame.game.xml.cache.RideXMLInfoMap;
import com.snail.webgame.game.xml.info.ChallengeBattleXmlInfo.BattleDetailPoint;
import com.snail.webgame.game.xml.info.DropXMLInfo;
import com.snail.webgame.game.xml.info.GWXMLInfo;
import com.snail.webgame.game.xml.info.RideQuaXMLInfo;
import com.snail.webgame.game.xml.info.RideXMLInfo;
import com.snail.webgame.game.xml.info.RideQuaXMLInfo.QualityInfo;
import com.snail.webgame.game.xml.info.RideXMLInfo.LvUpInfo;

public class FightService {

	private static final Logger logger = LoggerFactory.getLogger("logs");

	/**
	 * 处理超时战斗缓存
	 * @param roleInfo
	 */
	public static void dealFightOutforTimeOut(RoleInfo roleInfo) {
		int roleId = roleInfo.getId();
		FightInfo fightInfo = FightInfoMap.getFightInfoByRoleId(roleId);
		if (fightInfo == null) {
			return;
		}
		FightType fightType = fightInfo.getFightType();// 战场类型
		if (System.currentTimeMillis() - fightInfo.getFightTime() >= FightType.getFightTimeLimit(fightType)) {
			dealFightOut(roleInfo);
		}
	}

	/**
	 * 战斗中途退出，强制退出处理
	 * @param roleInfo
	 */
	public static void dealFightOut(RoleInfo roleInfo) {
		int roleId = roleInfo.getId();
		FightInfo fightInfo = FightInfoMap.getFightInfoByRoleId(roleId);
		if (fightInfo == null) {
			return;
		}
		// 1-胜 2-败
		int fightResult = 2;
		FightType fightType = fightInfo.getFightType();// 战场类型
		switch (fightType) {
		case FIGHT_TYPE_2:// 竞技场
			ArenaMgtService.dealFightEnd(ActionType.action142.getType(), fightResult, roleInfo, fightInfo, null, null);
			break;
		case FIGHT_TYPE_18:// 世界地图开矿
			MineMgtService.dealFightEnd(ActionType.action452.getType(), fightResult, roleInfo, fightInfo, null);
			break;
		case FIGHT_TYPE_19:// 世界地图抢矿
			MineMgtService.dealFightEnd(ActionType.action454.getType(), fightResult, roleInfo, fightInfo, null);
			break;
		default:
			break;
		}
		FightInfoMap.removeFightInfoByRoleId(roleId);
	}

	/**
	 * 向客户端发送进入战斗场景
	 * @param roleInfo
	 * @param resp
	 */
	public static void sendIntoFight(RoleInfo roleInfo, IntoFightResp resp) {
		// 向客户端发送
		IoSession session = ServerMap.getServerSession(ServerName.GATE_SERVER_NAME + "-" + roleInfo.getGateServerId());
		if (session != null && session.isConnected()) {
			Message message = new Message();
			GameMessageHead head = new GameMessageHead();
			head.setMsgType(Command.FIGHT_STARE_RESP);
			head.setUserID0((int) roleInfo.getId());
			message.setHeader(head);
			message.setBody(resp);
			session.write(message);

			if (logger.isInfoEnabled()) {
				logger.error("send fight start msg, fightId=" + resp.getId() + ",success!!");
			}
		} else {
			if (logger.isErrorEnabled()) {
				logger.error("send fight start msg, fightId=" + resp.getId() + ",can not find session roleId = "
						+ roleInfo.getId() + "!!");
			}
		}
	}

	/**
	 * 向客户端发送战斗结束处理
	 * @param roleInfo
	 * @param resp
	 */
	public static void sendFightEnd(RoleInfo roleInfo, long fightId, FightEndResp resp) {
		// 向客户端发送
		IoSession session = ServerMap.getServerSession(ServerName.GATE_SERVER_NAME + "-" + roleInfo.getGateServerId());
		if (session != null && session.isConnected()) {
			Message message = new Message();
			GameMessageHead head = new GameMessageHead();
			head.setMsgType(Command.FIGHT_END_RESP);
			head.setUserID0((int) roleInfo.getId());
			message.setHeader(head);
			message.setBody(resp);

			session.write(message);
			if (logger.isInfoEnabled()) {
				logger.error("deal fight end msg,roleId=" + roleInfo.getId() + " fightId=" + fightId + ",result="
						+ resp.getResult());
			}
		} else {
			if (logger.isErrorEnabled()) {
				logger.error("deal fight end msg, fightId=" + fightId + ",can not find session roleId = "
						+ roleInfo.getId() + "!!");
			}
		}
	}

	/**
	 * 获得玩家部队信息
	 * @param roleInfo
	 * @param heroInfo
	 * @param deployPos
	 * @param side
	 * @param rate
	 * @param propType 属性类型 1-正常的全部属性 2-对攻防守战中的裸属性 3-对攻防守中的加成属性
	 * @return
	 */
	public static FightArmyDataInfo getFightArmyDatabyHeroInfo(RoleInfo roleInfo, HeroInfo heroInfo, int deployPos,
			int side, Map<HeroProType, Double> mainRate, Map<HeroProType, Double> otherRate, byte propType) {
		return getFightArmyDatabyHeroInfo(roleInfo, heroInfo, deployPos, side, mainRate, otherRate, propType, 1.0);
	}

	/**
	 * 获得玩家部队信息
	 * @param roleInfo
	 * @param heroInfo
	 * @param deployPos
	 * @param side
	 * @param rate
	 * @param propType 属性类型 1-正常的全部属性 2-对攻防守战中的裸属性 3-对攻防守中的加成属性
	 * @param baseAddRate
	 * @return
	 */
	public static FightArmyDataInfo getFightArmyDatabyHeroInfo(RoleInfo roleInfo, HeroInfo heroInfo, int deployPos,
			int side, Map<HeroProType, Double> mainRate, Map<HeroProType, Double> otherRate, byte propType,
			double baseAddRate) {
		HeroXMLInfo heroXMLInfo = HeroXMLInfoMap.getHeroXMLInfo(heroInfo.getHeroNo());
		if (heroXMLInfo == null) {
			return null;
		}
		if (deployPos == HeroInfo.DEPLOY_TYPE_COMM || deployPos > GameValue.FIGHT_ARMY_LIMIT) {
			return null;
		}
		if (roleInfo.getServerStatus() == 0) {
			// 0-未登录数据未初始化
			// 初始化英雄装备属性
			EquipService.loginInitHeroEquipProperty(heroInfo);
			// 刷新英雄属性 战斗力
			HeroService.refeshHeroProperty(roleInfo, heroInfo);
		}
		FightArmyDataInfo dataInfo = new FightArmyDataInfo();
		dataInfo.setId(heroInfo.getId());
		dataInfo.setSide((byte) side);
		dataInfo.setRace((byte) roleInfo.getRoleRace());
		dataInfo.setHeroNo(heroInfo.getHeroNo());
		// 位置
		dataInfo.setDeployStatus((byte) deployPos);
		// 组
		dataInfo.setGroup(getFightArmyGroup((byte) deployPos));
		if (dataInfo.getGroup() == 0) {
			return null;
		}

		dataInfo.setHeroLv((short) heroInfo.getHeroLevel());
		// dataInfo.setStarNum((byte) heroInfo.getStar());
		dataInfo.seteMiliType((byte) heroXMLInfo.getHeroType());
		// 获取兵种等级
		int soldierLevel = SoldierService.getSoldierLevel(roleInfo, (byte) heroXMLInfo.getHeroType());
		dataInfo.setSoldierLevel((short) soldierLevel);

		dataInfo.setnAdvLevel(heroInfo.getQuality());
		dataInfo.setShowPlanId(roleInfo.getIsShowShizhuang());
		
		// 属性附加
		HeroPropertyInfo heroPropertyInfo = null;
		switch (propType) {
		case 1:// 1-正常的全部属性
			heroPropertyInfo = HeroProService.getHeroTotalProperty(heroInfo, mainRate, otherRate);
			break;
		case 2:// 2-对攻防守战中的裸属性
			heroPropertyInfo = HeroProService.getNudeProperty(roleInfo, heroInfo, heroXMLInfo);
			if (baseAddRate > 0 && baseAddRate < 1) {
				HeroPropertyInfo addPro = HeroProService.getHeroTotalProperty(heroInfo, mainRate, otherRate);
				addPro.setMoveSpeed(0);// 坐骑速度无加成属性，算在基础属性里
				
				if (addPro != null && heroPropertyInfo != null) {
					// 基础属性 + 加成属性*基础属性百分比
					for (HeroProType proType : HeroProType.values()) {
						addPro.subValue(proType, heroPropertyInfo.getValue(proType));
						addPro.mulRate(proType, baseAddRate);
						heroPropertyInfo.addValue(proType, addPro.getValue(proType));
					}
				}
			}
			// 坐骑的速度属性，不受士气影响
			RideInfo rideInfo = roleInfo.getRideInfo();
			
			if(rideInfo != null){
				LvUpInfo lvUpInfo = rideInfo.getLvUpInfo();
				
				if(lvUpInfo != null){
					heroPropertyInfo.setMoveSpeed(heroPropertyInfo.getMoveSpeed() + lvUpInfo.getMoveSpeed());
				}
			}
			break;
		case 3:// 3-对攻防守中的加成属性
			heroPropertyInfo = HeroProService.getHeroTotalProperty(heroInfo, mainRate, otherRate);
			if (heroPropertyInfo != null) {
				HeroPropertyInfo nudeProperty = HeroProService.getNudeProperty(roleInfo, heroInfo, heroXMLInfo);
				if (nudeProperty != null) {
					for (HeroProType proType : HeroProType.values()) {
						heroPropertyInfo.subValue(proType, nudeProperty.getValue(proType));
						if (baseAddRate > 0 && baseAddRate < 1) {
							// 加成属性*（1-基础属性百分比）
							heroPropertyInfo.mulRate(proType, (1 - baseAddRate));
						}
					}
				}
			}
			heroPropertyInfo.setMoveSpeed(0);
			break;
		default:
			break;
		}
		if (heroPropertyInfo == null) {
			return null;
		}
		setFightArmyDataInfo(dataInfo, heroPropertyInfo);
		// 技能
		dataInfo.setHeroSkills(new ArrayList<HeroSkillDataInfo>());
		HeroSkillDataInfo skillData = null;
		Map<Integer, Integer> skillMap = HeroService.getSkillMap(heroInfo);
		if (skillMap != null) {
			// int index = 1;
			for (int skillNo : skillMap.keySet()) {
				int skillLevel = skillMap.get(skillNo);
				HeroXMLSkill xmlSkill = heroXMLInfo.getSkillMap().get(skillNo);
				if (xmlSkill == null) {
					continue;
				}
				skillData = new HeroSkillDataInfo();
				skillData.setIndex(xmlSkill.getSkillPos());
				skillData.setHeroId(heroInfo.getId());
				skillData.setSkillNo(skillNo);
				skillData.setSkillLv((short) skillLevel);
				skillData.setAI(xmlSkill.getAi());
				skillData.setAIOrder(xmlSkill.getAiOrder());
				dataInfo.getHeroSkills().add(skillData);
				// index++;
			}
		}
		dataInfo.setSkillNum(dataInfo.getHeroSkills().size());
		if (deployPos == HeroInfo.DEPLOY_TYPE_MAIN) {
			dataInfo.setEquipNos(SceneService1.getHeroEquipNoforAvater(heroInfo));
			addRideInfo(roleInfo, dataInfo);
		}

		return dataInfo;
	}

	/**
	 * 获取部队信息
	 * @param roleInfo 
	 * @param map
	 * @param record
	 * @param side
	 * @param rate
	 * @return
	 */
	public static FightArmyDataInfo getFightArmyDatabyHeroRecord(Map<Byte, HeroRecord> map, HeroRecord record,
			int side, double rate) {
		return getFightArmyDatabyHeroRecord(map, record, side, HeroProService.getProRate(rate),
				HeroProService.getProRate(rate));
	}

	/**
	 * 获取镜像信息
	 * @param roleInfo 
	 * @param map
	 * @param record
	 * @param side
	 * @param mainRate
	 * @param otherRate
	 * @return
	 */
	public static FightArmyDataInfo getFightArmyDatabyHeroRecord(Map<Byte, HeroRecord> map, HeroRecord record,
			int side, Map<HeroProType, Double> mainRate, Map<HeroProType, Double> otherRate) {
		HeroXMLInfo heroXMLInfo = HeroXMLInfoMap.getHeroXMLInfo(record.getHeroNo());
		if (heroXMLInfo == null) {
			return null;
		}
		if (record.getHeroStatus() == HeroInfo.DEPLOY_TYPE_COMM || record.getHeroStatus() > GameValue.FIGHT_ARMY_LIMIT) {
			return null;
		}
		FightArmyDataInfo dataInfo = new FightArmyDataInfo();
		dataInfo.setId(record.getId());
		dataInfo.setSide((byte) side);
		dataInfo.setHeroNo(record.getHeroNo());
		// 位置
		dataInfo.setDeployStatus(record.getDeployStatus());
		// 组
		dataInfo.setGroup(getFightArmyGroup((byte) record.getDeployStatus()));
		if (dataInfo.getGroup() == 0) {
			return null;
		}

		dataInfo.setHeroLv((short) record.getHeroLevel());
		// dataInfo.setStarNum((byte) record.getStar());
		dataInfo.setnAdvLevel(record.getQuality());
		dataInfo.seteMiliType((byte) heroXMLInfo.getHeroType());
		dataInfo.setSoldierLevel((short) record.getSoldierLevel());

		// 属性附加
		HeroPropertyInfo totalProtperty = HeroRecordService.recalHeroRecordTotalPro(map, record, mainRate, otherRate);
		if (totalProtperty == null) {
			return null;
		}
		setFightArmyDataInfo(dataInfo, totalProtperty);
		dataInfo.setCurrHp(totalProtperty.getHp() - record.getCutHp());
		if(dataInfo.getCurrHp() <= 0){
			dataInfo.setCurrHp(1);
		}
		
		// 技能
		dataInfo.setHeroSkills(new ArrayList<HeroSkillDataInfo>());
		HeroSkillDataInfo skillData = null;
		Map<Integer, Integer> skillMap = record.getSkillMap();

		if (skillMap != null) {
			// int index = 1;
			for (Integer skillNo : skillMap.keySet()) {
				HeroXMLSkill xmlSkill = heroXMLInfo.getSkillMap().get(skillNo);
				if (xmlSkill == null) {
					continue;
				}
				skillData = new HeroSkillDataInfo();
				skillData.setIndex(xmlSkill.getSkillPos());
				skillData.setHeroId(record.getId());
				skillData.setSkillNo(skillNo);
				skillData.setSkillLv(skillMap.get(skillNo).shortValue());
				skillData.setAI(xmlSkill.getAi());
				skillData.setAIOrder(xmlSkill.getAiOrder());
				dataInfo.getHeroSkills().add(skillData);
				// index++;
			}
		}

		dataInfo.setSkillNum(dataInfo.getHeroSkills().size());

		if (record.getDeployStatus() == HeroInfo.DEPLOY_TYPE_MAIN) {
			dataInfo.setEquipNos(SceneService1.getHeroEquipNoforAvater(record));
			addRideInfo(record.getRideRecord(), dataInfo);
		}
		return dataInfo;
	}

	/**
	 * 属性信息
	 * @param dataInfo
	 * @param totalProtperty
	 */
	public static void setFightArmyDataInfo(FightArmyDataInfo dataInfo, HeroPropertyInfo totalProtperty) {
		dataInfo.setForce(totalProtperty.getForce());
		dataInfo.setWit(totalProtperty.getWit());
		dataInfo.setTroops(totalProtperty.getTroops());
		dataInfo.setHp(totalProtperty.getHp());
		dataInfo.setCurrHp(totalProtperty.getHp());

		dataInfo.setAttack(totalProtperty.getAttack());
		dataInfo.setAttackAvo(0);
		dataInfo.setMagicAttack(totalProtperty.getMagicAttack());
		dataInfo.setMagicAvo(0);

		dataInfo.setCrit(totalProtperty.getCrit());
		dataInfo.setCritAvo(totalProtperty.getCritAvo());
		dataInfo.setSkillCrit(totalProtperty.getSkillCrit());
		dataInfo.setSkillCritAvo(totalProtperty.getSkillCritAvo());

		dataInfo.setIgnorAttackAvo(totalProtperty.getIgnorAttackAvo());
		dataInfo.setIgnorMagicAvo(totalProtperty.getIgnorMagicAvo());
		dataInfo.setHit(totalProtperty.getHit());
		dataInfo.setMiss(totalProtperty.getMiss());

		dataInfo.setMoveSpeed(totalProtperty.getMoveSpeed());
		dataInfo.setCutCD(totalProtperty.getCutCD());
		dataInfo.setHpBack(totalProtperty.getHpBack());
		dataInfo.setCureUp(totalProtperty.getCureUp());

		dataInfo.setAttackSpeed(totalProtperty.getAttackSpeed());
		dataInfo.setAttackDef(totalProtperty.getAttackDef());
		dataInfo.setMagicDef(totalProtperty.getMagicDef());
		dataInfo.setAttackRange(totalProtperty.getAttackRange());

		dataInfo.setMinRadius(totalProtperty.getRadius());
		dataInfo.setMaxRadius(totalProtperty.getRadius());
		dataInfo.setLookRange(totalProtperty.getLookRange());
		dataInfo.setBulletSpeed(totalProtperty.getBulletSpeed());

		dataInfo.setAd(totalProtperty.getAd());
		dataInfo.setCritMore((int) totalProtperty.getCritMore());
		dataInfo.setCritLess((int) totalProtperty.getCritLess());
		dataInfo.setSoldierHp(totalProtperty.getSoldierHp());
		dataInfo.setBreakSoldierDef(totalProtperty.getBreakSoldierDef());
		dataInfo.setReduceDamage(totalProtperty.getReduceDamage());
		dataInfo.setImmunityDamage(totalProtperty.getImmunityDamage());
	}

	/**
	 * 获取group
	 * @param deployPos
	 * @return
	 */
	public static byte getFightArmyGroup(byte deployPos) {
		if (deployPos == 1) {
			return 1;
		} else if (deployPos == 2 || deployPos == 3) {
			return 2;
		} else if (deployPos == 4 || deployPos == 5) {
			return 3;
		}
		return 0;
	}

	/**
	 * 1号副将,2号副将的第一技能给主将
	 * @param sideDataList
	 */
	public static void changeArmyFightDataSkill(List<FightSideData> sideDataList) {
		for (FightSideData sideData : sideDataList) {
			List<FightArmyDataInfo> armyList = sideData.getArmyInfos();

			HeroSkillDataInfo skillDataAdd1 = new HeroSkillDataInfo();
			HeroSkillDataInfo skillDataAdd2 = new HeroSkillDataInfo();
			HeroSkillDataInfo skillDataAdd3 = new HeroSkillDataInfo();
			HeroSkillDataInfo skillDataAdd4 = new HeroSkillDataInfo();

			for (FightArmyDataInfo armyData : armyList) {

				if (armyData == null) {
					continue;
				}

				List<HeroSkillDataInfo> heroSkills = armyData.getHeroSkills();
				HeroXMLInfo heroXMLInfo = HeroXMLInfoMap.getHeroXMLInfo(armyData.getHeroNo());
				if (heroXMLInfo == null) {
					continue;
				}
				if (heroSkills == null || heroSkills.size() <= 0) {
					continue;
				}

				// 2-一号副将
				if (armyData.getDeployStatus() == 2) {
					List<HeroSkillDataInfo> newSkillList = new ArrayList<HeroSkillDataInfo>();
					for (HeroSkillDataInfo skillData : heroSkills) {
						HeroXMLSkill xmlSkill = heroXMLInfo.getSkillMap().get(skillData.getSkillNo());

						HeroXMLSkill heroSkillXml = HeroXMLInfoMap.getHeroSkillXML(armyData.getHeroNo(),
								skillData.getSkillNo());

						if (xmlSkill != null && heroSkillXml != null) {
							if (xmlSkill.getSkillPos() == 1) {
								skillDataAdd1 = skillData;
								skillDataAdd1.setHeroId(armyData.getId());
								skillDataAdd1.setAIOrder(heroSkillXml.getAiOrder());
								skillDataAdd1.setAI(heroSkillXml.getAi());
							} else {
								skillData.setAIOrder(heroSkillXml.getAiOrder());
								skillData.setAI(heroSkillXml.getAi());
								newSkillList.add(skillData);
							}
						}
					}

					armyData.setHeroSkills(newSkillList);
					armyData.setSkillNum(armyData.getHeroSkills().size());
				}

				// 3-二号副将 。。
				if (armyData.getDeployStatus() == 3) {
					List<HeroSkillDataInfo> newSkillList = new ArrayList<HeroSkillDataInfo>();
					for (HeroSkillDataInfo skillData : heroSkills) {
						HeroXMLSkill xmlSkill = heroXMLInfo.getSkillMap().get(skillData.getSkillNo());

						HeroXMLSkill heroSkillXml = HeroXMLInfoMap.getHeroSkillXML(armyData.getHeroNo(),
								skillData.getSkillNo());

						if (xmlSkill != null && heroSkillXml != null) {
							if (xmlSkill.getSkillPos() == 1) {
								skillDataAdd2 = skillData;
								skillDataAdd2.setHeroId(armyData.getId());
								skillDataAdd2.setAIOrder(heroSkillXml.getAiOrder());
								skillDataAdd2.setAI(heroSkillXml.getAi());
							} else {
								skillData.setAIOrder((byte) skillData.getIndex());
								skillData.setAIOrder(heroSkillXml.getAiOrder());
								skillData.setAI(heroSkillXml.getAi());
								newSkillList.add(skillData);
							}
						}
					}

					armyData.setHeroSkills(newSkillList);
					armyData.setSkillNum(armyData.getHeroSkills().size());
				}

				// 4-三号副将
				if (armyData.getDeployStatus() == 4) {
					List<HeroSkillDataInfo> newSkillList = new ArrayList<HeroSkillDataInfo>();
					for (HeroSkillDataInfo skillData : heroSkills) {
						HeroXMLSkill xmlSkill = heroXMLInfo.getSkillMap().get(skillData.getSkillNo());

						HeroXMLSkill heroSkillXml = HeroXMLInfoMap.getHeroSkillXML(armyData.getHeroNo(),
								skillData.getSkillNo());

						if (xmlSkill != null && heroSkillXml != null) {
							if (xmlSkill.getSkillPos() == 1) {
								skillDataAdd3 = skillData;
								skillDataAdd3.setHeroId(armyData.getId());
								skillDataAdd3.setAIOrder(heroSkillXml.getAiOrder());
								skillDataAdd3.setAI(heroSkillXml.getAi());
							} else {
								skillData.setAIOrder((byte) skillData.getIndex());
								skillData.setAIOrder(heroSkillXml.getAiOrder());
								skillData.setAI(heroSkillXml.getAi());
								newSkillList.add(skillData);
							}
						}
					}

					armyData.setHeroSkills(newSkillList);
					armyData.setSkillNum(armyData.getHeroSkills().size());
				}
				// 5-四号副将
				if (armyData.getDeployStatus() == 5) {
					List<HeroSkillDataInfo> newSkillList = new ArrayList<HeroSkillDataInfo>();
					for (HeroSkillDataInfo skillData : heroSkills) {
						HeroXMLSkill xmlSkill = heroXMLInfo.getSkillMap().get(skillData.getSkillNo());

						HeroXMLSkill heroSkillXml = HeroXMLInfoMap.getHeroSkillXML(armyData.getHeroNo(),
								skillData.getSkillNo());

						if (xmlSkill != null && heroSkillXml != null) {
							if (xmlSkill.getSkillPos() == 1) {
								skillDataAdd4 = skillData;
								skillDataAdd4.setHeroId(armyData.getId());
								skillDataAdd4.setAI(heroSkillXml.getAi());
								skillDataAdd4.setAIOrder(heroSkillXml.getAiOrder());
							} else {
								skillData.setAIOrder((byte) skillData.getIndex());
								skillData.setAI(heroSkillXml.getAi());
								skillData.setAIOrder(heroSkillXml.getAiOrder());
								newSkillList.add(skillData);
							}
						}
					}

					armyData.setHeroSkills(newSkillList);
					armyData.setSkillNum(armyData.getHeroSkills().size());
				}
			}

			// 主武将
			for (FightArmyDataInfo armyData : armyList) {
				if (armyData != null && armyData.getDeployStatus() == 1) {
					List<HeroSkillDataInfo> newSkillList = new ArrayList<HeroSkillDataInfo>();

					List<HeroSkillDataInfo> heroSkills = armyData.getHeroSkills();
					for (HeroSkillDataInfo info : heroSkills) {

						HeroXMLSkill heroSkillXml = HeroXMLInfoMap.getHeroSkillXML(armyData.getHeroNo(),
								info.getSkillNo());

						info.setHeroId(armyData.getId());
						info.setAIOrder((byte) info.getIndex());
						if (heroSkillXml != null) {
							info.setAI(heroSkillXml.getAi());
							info.setAIOrder(heroSkillXml.getAiOrder());
						}
						newSkillList.add(info);
					}

					if (skillDataAdd1.getSkillNo() != 0 && skillDataAdd1.getSkillLv() > 0) {
						skillDataAdd1.setIndex(5);
						skillDataAdd1.setAIOrder((byte) 5);
						newSkillList.add(skillDataAdd1);
					}

					if (skillDataAdd2.getSkillNo() != 0 && skillDataAdd2.getSkillLv() > 0) {
						skillDataAdd2.setIndex(6);
						skillDataAdd2.setAIOrder((byte) 6);
						newSkillList.add(skillDataAdd2);
					}

					if (skillDataAdd3.getSkillNo() != 0 && skillDataAdd3.getSkillLv() > 0) {
						skillDataAdd3.setIndex(5);
						skillDataAdd3.setAIOrder((byte) 5);
						newSkillList.add(skillDataAdd3);
					}

					if (skillDataAdd4.getSkillNo() != 0 && skillDataAdd4.getSkillLv() > 0) {
						skillDataAdd4.setIndex(6);
						skillDataAdd4.setAIOrder((byte) 6);
						newSkillList.add(skillDataAdd4);
					}

					armyData.setHeroSkills(newSkillList);
					armyData.setSkillNum(armyData.getHeroSkills().size());
				}
			}
		}
	}

	/**
	 * 士兵技能，神兵技能添加 ----PVE构建
	 * @param roleInfo
	 * @param sideDataList
	 */
	@SuppressWarnings("unchecked")
	public static void addSoldierSkillAndMagic(RoleInfo roleInfo, List<FightSideData> sideDataList) {
		for (FightSideData sideData : sideDataList) {
			List<FightArmyDataInfo> armyList = sideData.getArmyInfos();

			for (FightArmyDataInfo armyData : armyList) {
				if (armyData == null) {
					continue;
				}

				List<HeroSkillDataInfo> heroSkills = armyData.getHeroSkills();

				HeroInfo heroInfo = HeroInfoMap.getHeroInfo(roleInfo.getId(), (int) armyData.getId());
				if (heroInfo == null) {
					continue;
				}

				// 士兵科技技能
				SoldierXMLInfo soldierXMLInfo = SoldierXMLInfoMap.getSoldierXMLInfo(armyData.geteMiliType(),
						armyData.getSoldierLevel());
				if (soldierXMLInfo == null) {
					continue;
				}
				HeroSkillDataInfo skillDataAdd1 = new HeroSkillDataInfo();
				skillDataAdd1.setHeroId((int) armyData.getId());
				skillDataAdd1.setIndex(7);
				skillDataAdd1.setSkillNo(soldierXMLInfo.getSkill());
				skillDataAdd1.setSkillLv((short) soldierXMLInfo.getSkillLv());
				skillDataAdd1.setAI(soldierXMLInfo.getAi());
				skillDataAdd1.setAIOrder((byte) 7);

				if (heroSkills == null) {
					List<HeroSkillDataInfo> newHeroSkills = new ArrayList<HeroSkillDataInfo>();
					armyData.setHeroSkills(newHeroSkills);
				}
				armyData.getHeroSkills().add(skillDataAdd1);

				// 神兵技能,只有主武将有
				if (armyData.getDeployStatus() == 1) {
					Object[] propertyInfoArray = WeaponService.getWeaponHeroPropertyInfo(roleInfo);
					if (propertyInfoArray != null && propertyInfoArray.length == 2) {
						ArrayList<String> suitSkillList = (ArrayList<String>) propertyInfoArray[1];
						if (suitSkillList != null && suitSkillList.size() > 0) {
							for (int i = 0; i < suitSkillList.size(); i++) {
								String effect = suitSkillList.get(i);
								if (effect == null) {
									continue;
								}

								String[] effectStr = effect.split(",");
								if (effectStr.length != 3) {
									continue;
								}

								HeroSkillDataInfo skillDataAdd = new HeroSkillDataInfo();
								skillDataAdd.setHeroId((int) armyData.getId());
								skillDataAdd.setIndex(8 + i);
								skillDataAdd.setSkillNo(Integer.valueOf(effectStr[0]));
								skillDataAdd.setSkillLv(Short.valueOf(effectStr[1]));
								skillDataAdd.setAI(Integer.valueOf(effectStr[2]));
								skillDataAdd.setAIOrder((byte) (8 + i));

								armyData.getHeroSkills().add(skillDataAdd);

							}
						}
					}
				}

				armyData.setSkillNum(armyData.getHeroSkills().size());
			}
		}
	}

	/**
	 * 神兵技能添加 ------PVP构建
	 * @param roleInfo
	 * @param vo
	 * @param heroInfo
	 */
	@SuppressWarnings("unchecked")
	public static void addMagicSkill(RoleInfo roleInfo, List<SkillInfoVo> skillList, HeroInfo heroInfo) {
		Object[] propertyInfoArray = WeaponService.getWeaponHeroPropertyInfo(roleInfo);
		if (propertyInfoArray != null && propertyInfoArray.length == 2) {
			ArrayList<String> suitSkillList = (ArrayList<String>) propertyInfoArray[1];
			if (suitSkillList != null && suitSkillList.size() > 0) {
				for (int i = 0; i < suitSkillList.size(); i++) {
					String effect = suitSkillList.get(i);
					if (effect == null) {
						continue;
					}

					String[] effectStr = effect.split(",");
					if (effectStr.length != 3) {
						continue;
					}

					SkillInfoVo skillInfoVo1 = new SkillInfoVo();
					skillInfoVo1.setHeroId((int) heroInfo.getId());
					skillInfoVo1.setPosition((byte) (8 + i));
					skillInfoVo1.setSkillNo(Integer.valueOf(effectStr[0]));
					skillInfoVo1.setLevel(Short.valueOf(effectStr[1]));
					skillInfoVo1.setAi(Integer.valueOf(effectStr[2]));
					skillInfoVo1.setAiOrder((byte) (8 + i));

					skillList.add(skillInfoVo1);

				}
			}
		}
	}

	/**
	 * PVE添加士兵技能
	 * @param roleInfo
	 * @param heroInfo
	 * @param heroXMLInfo
	 * @param skillList
	 */
	public static void addSoldierSkill(RoleInfo roleInfo, HeroInfo heroInfo, HeroXMLInfo heroXMLInfo,
			List<SkillInfoVo> skillList) {
		// 士兵科技技能
		SoldierXMLInfo soldierXMLInfo = SoldierXMLInfoMap.getSoldierXMLInfo((byte) heroXMLInfo.getHeroType(),
				SoldierService.getSoldierLevel(roleInfo, (byte) heroXMLInfo.getHeroType()));
		if (soldierXMLInfo == null) {
			return;
		}
		SkillInfoVo skillInfoVo = new SkillInfoVo();
		skillInfoVo.setHeroId((int) heroInfo.getId());
		skillInfoVo.setPosition((byte) 7);
		skillInfoVo.setSkillNo(soldierXMLInfo.getSkill());
		skillInfoVo.setLevel((short) soldierXMLInfo.getSkillLv());
		skillInfoVo.setAi(soldierXMLInfo.getAi());
		skillInfoVo.setAiOrder((byte) 7);

		skillList.add(skillInfoVo);
	}

	/**
	 * 获取主将活动系数
	 * @param fightType
	 * @return
	 */
	public static Map<HeroProType, Double> getMainHeroRate(FightType fightType) {
		switch (fightType) {
		case FIGHT_TYPE_2:
			return GameValue.ARENA_PVE_ACT_MAIN_RATE;
		case FIGHT_TYPE_9:
			return GameValue.COMPETITION_PVP_ACT_MAIN_RATE;
		case FIGHT_TYPE_11:
			return GameValue.PVE_11_MAIN_RATE;
		case FIGHT_TYPE_6:
			return GameValue.PVE_6_MAIN_RATE;
		case FIGHT_TYPE_14:
			return GameValue.PVP_14_MAIN_RATE;
		case FIGHT_TYPE_17:
			return GameValue.PVP_17_MAIN_RATE;
		case FIGHT_TYPE_16:
			return GameValue.PVP_16_MAIN_RATE;
		case FIGHT_TYPE_18:
		case FIGHT_TYPE_19:
			return GameValue.PVE_18_19_MAIN_RATE;
		default:
			return null;
		}
	}

	/**
	 * 获取副将活动系数
	 * @param fightType
	 * @return
	 */
	public static Map<HeroProType, Double> getOtherHeroRate(FightType fightType) {
		switch (fightType) {
		case FIGHT_TYPE_2:
			return GameValue.ARENA_PVE_ACT_OTHER_RATE;
		case FIGHT_TYPE_9:
			return GameValue.COMPETITION_PVP_ACT_OTHER_RATE;
		case FIGHT_TYPE_11:
			return GameValue.PVE_11_OTHER_RATE;
		case FIGHT_TYPE_6:
			return GameValue.PVE_6_OTHER_RATE;
		case FIGHT_TYPE_14:
			return GameValue.PVP_14_OTHER_RATE;
		case FIGHT_TYPE_17:
			return GameValue.PVP_17_OTHER_RATE;
		case FIGHT_TYPE_16:
			return GameValue.PVP_16_OTHER_RATE;
		case FIGHT_TYPE_18:
		case FIGHT_TYPE_19:
			return GameValue.PVE_18_19_OTHER_RATE;
		default:
			return null;
		}
	}

	/**
	 * 获取副将活动系数
	 * @param rateMap
	 * @param rate
	 * @return
	 */
	public static Map<HeroProType, Double> mulRate(Map<HeroProType, Double> rateMap, float rate) {
		if (rate == 1) {
			return rateMap;
		}
		Map<HeroProType, Double> proRate = HeroProService.getProRate(rate);
		Double val1 = null;
		Double val2 = null;
		double val = 1.0;
		for (HeroProType proType : HeroProType.values()) {
			val1 = proRate.get(proType);
			if (rateMap != null) {
				val2 = rateMap.get(proType);
			}
			if (val1 != null || val2 != null) {
				val = (val1 == null ? 1.0 : val1) * (val2 == null ? 1.0 : val2);
				if (val != 1.0) {
					proRate.put(proType, val);
				}
			}
		}
		return proRate;
	}

	/**
	 * 物品掉落计算
	 * @param pointMap
	 * @return
	 */
	public static Map<Integer, DropBagInfo> fightDrop(HashMap<Integer, BattleDetailPoint> pointMap) {
		Map<Integer, DropBagInfo> dropBagMap = new HashMap<Integer, DropBagInfo>();
		for (BattleDetailPoint point : pointMap.values()) {
			String NPC = point.getGw();
			if (NPC != null && NPC.length() > 0) {
				GWXMLInfo gwXmlInfo = GWXMLInfoMap.getNPCGWXMLInfo(NPC);
				if (gwXmlInfo != null) {
					HashMap<Integer, String> dropMap = gwXmlInfo.getDropMap();

					for (int no : dropMap.keySet()) {
						DropBagInfo dropBagInfo = new DropBagInfo();
						String bag = dropMap.get(no);

						dropBagInfo.setNo(no);
						List<DropXMLInfo> list = PropBagXMLMap.getPropBagXMLListbyStr(bag);
						List<DropInfo> addList = new ArrayList<DropInfo>();// 奖励
						// 计算
						ItemService.getDropXMLInfo(null, list, addList);
						if (addList.size() > 0) {
							dropBagInfo.setDrop(addList);
							dropBagInfo.setDropNum(addList.size());

							dropBagMap.put(dropBagInfo.getNo(), dropBagInfo);
						}
					}
				}
			}
		}
		return dropBagMap;
	}
	
	/**
	 * 加上坐骑信息
	 * 
	 * @param roleInfo
	 * @param dataInfo
	 */
	private static void addRideInfo(RoleInfo roleInfo, FightArmyDataInfo dataInfo){
		if(roleInfo == null || dataInfo == null){
			return;
		}
		
		// 坐骑
		RideInfo rideInfo = roleInfo.getRideInfo();
		
		if(rideInfo != null){
			LvUpInfo lvUpInfo = rideInfo.getLvUpInfo();
			
			if(lvUpInfo != null){
				dataInfo.setRideHp(rideInfo.getCurrHP());
				dataInfo.setRideLv(lvUpInfo.getLv());
				dataInfo.setRideSpeed(lvUpInfo.getMoveSpeed());
			}
			dataInfo.setRideNo(rideInfo.getRideNo());
			dataInfo.setRideCurrHp(rideInfo.getCurrHP());
		}
	}
	
	/**
	 * 加上坐骑信息
	 * 
	 * @param roleInfo
	 * @param dataInfo
	 */
	private static void addRideInfo(RideRecord rideRecord, FightArmyDataInfo dataInfo){
		if(rideRecord == null || dataInfo == null){
			return;
		}
		
		RideXMLInfo rideXMLInfo = RideXMLInfoMap.fetchRideXMLInfo(rideRecord.getRideNo());
		RideQuaXMLInfo rideQuaXMLInfo = RideXMLInfoMap.fetchRideQuaXMLInfo(rideRecord.getRideNo());
		if (rideXMLInfo == null || rideQuaXMLInfo == null) {
			return;
		}
		// 计算坐骑的速度，血量
		LvUpInfo lvUpInfo = rideXMLInfo.getRideLvUpMap().get(rideRecord.getRideLv());
		QualityInfo qualityInfo = rideQuaXMLInfo.getRideQuaMap().get(rideRecord.getQuality());
		if(lvUpInfo != null){
			if(qualityInfo!=null){
				dataInfo.setRideCurrHp((int) (lvUpInfo.getRideHp() * (1 + qualityInfo.getAddAttrRate())));
			}
			dataInfo.setRideHp(dataInfo.getRideCurrHp());
			dataInfo.setRideLv(lvUpInfo.getLv());
			dataInfo.setRideSpeed(lvUpInfo.getMoveSpeed());
		}
		dataInfo.setRideNo(rideRecord.getRideNo());
	}
	
	public static void main(String[] args) {
		System.out.println(50.1 * (1 + 1.1));
		System.out.println(50.1 * 1 + 50.1 * 1.1);
	}
}
