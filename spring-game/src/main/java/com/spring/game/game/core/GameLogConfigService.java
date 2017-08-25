package com.snail.webgame.game.core;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.math.NumberUtils;

import com.snail.webgame.game.common.ActionType;
import com.snail.webgame.game.common.FightType;
import com.snail.webgame.game.common.xml.cache.HeroXMLInfoMap;
import com.snail.webgame.game.common.xml.info.HeroXMLInfo;
import com.snail.webgame.game.common.xml.info.SkillInfoLoader;
import com.snail.webgame.game.common.xml.info.SkillXmlInfo;
import com.snail.webgame.game.condtion.ConditionType;
import com.snail.webgame.game.dao.GameLogDAO;
import com.snail.webgame.game.info.log.ConfigLog;
import com.snail.webgame.game.info.log.ConfigType;
import com.snail.webgame.game.xml.cache.CampaignXMLInfoMap;
import com.snail.webgame.game.xml.cache.ChallengeBattleXmlInfoMap;
import com.snail.webgame.game.xml.cache.DefendXMLInfoMap;
import com.snail.webgame.game.xml.cache.EquipXMLInfoMap;
import com.snail.webgame.game.xml.cache.ExpActivityMap;
import com.snail.webgame.game.xml.cache.MineXMLInfoMap;
import com.snail.webgame.game.xml.cache.PayXMLInfoMap;
import com.snail.webgame.game.xml.cache.PropXMLInfoMap;
import com.snail.webgame.game.xml.cache.QuestProtoXmlInfoMap;
import com.snail.webgame.game.xml.cache.WeaponXmlInfoMap;
import com.snail.webgame.game.xml.info.CampaignXMLBattle;
import com.snail.webgame.game.xml.info.CampaignXMLInfo;
import com.snail.webgame.game.xml.info.ChallengeBattleXmlInfo;
import com.snail.webgame.game.xml.info.ChallengeBattleXmlInfo.BattleDetail;
import com.snail.webgame.game.xml.info.ChallengeBattleXmlInfo.ChapterInfo;
import com.snail.webgame.game.xml.info.DefendXMLInfo;
import com.snail.webgame.game.xml.info.EquipXMLInfo;
import com.snail.webgame.game.xml.info.ExpActivity;
import com.snail.webgame.game.xml.info.MineXMLInfo;
import com.snail.webgame.game.xml.info.PayXMLInfo;
import com.snail.webgame.game.xml.info.PropXMLInfo;
import com.snail.webgame.game.xml.info.QuestProtoXmlInfo;
import com.snail.webgame.game.xml.info.TeamChallengeXmlInfo;
import com.snail.webgame.game.xml.info.WeaponXmlInfo;
import com.snail.webgame.game.xml.load.TeamChallengeXmlLoader;

/**
 * 日志枚举表赋值
 * @author zenggang
 */
public class GameLogConfigService {

	/**
	 * 插入game_config_type,game_config 数据
	 */
	public static void insertGameConfig() {
		// game_config_type 数据
		List<ConfigType> list = new ArrayList<ConfigType>();
		for (ConfigType configType : ConfigType.values()) {
			list.add(configType);
		}
		GameLogDAO.getInstance().delOldGameConfigType();
		GameLogDAO.getInstance().insertConfigTypeLog(list);
		System.out.println("[SYSTEM] Init game_config successful!");

		List<ConfigLog> configs = getConfigLogList();
		GameLogDAO.getInstance().delOldGameConfig();
		GameLogDAO.getInstance().insertConfigLog(configs);
		System.out.println("[SYSTEM] Init game_config_type successful!");
	}

	/**
	 * 获取 game_config 数据
	 * @return
	 */
	public static List<ConfigLog> getConfigLogList() {
		List<ConfigLog> configs = new ArrayList<ConfigLog>();
		for (ConfigType configType : ConfigType.values()) {
			switch (configType) {
			case gameAction:
				// 玩家行为标识
				for (ActionType actionType : ActionType.values()) {
					configs.add(getConfigLog(actionType.getType() + "", actionType.getDesc(), configType,""));
				}
				break;
			case task:
				for (QuestProtoXmlInfo info : QuestProtoXmlInfoMap.getQuestProtoMap().values()) {
					// 同步 GameLogService.insertTaskLog
					String serial = configType.getType() + "-" + info.getQuestProtoNo();
					String name = info.getQuestName();
					configs.add(getConfigLog(serial + "", name, configType,""));
				}
				break;
			case prop:
				for (PropXMLInfo info : PropXMLInfoMap.getMap().values()) {
					configs.add(getConfigLog(info.getNo() + "", info.getName(), configType,""));
				}
				break;
			case hero:
				for (HeroXMLInfo info : HeroXMLInfoMap.getHeroMap().values()) {
					configs.add(getConfigLog(info.getNo() + "", info.getName(), configType,""));
				}
				break;
			case equip:
				for (EquipXMLInfo info : EquipXMLInfoMap.getEquipMap().values()) {
					configs.add(getConfigLog(info.getNo() + "", info.getName(), configType,""));
				}
				break;
			case weapan:
				for (WeaponXmlInfo info : WeaponXmlInfoMap.getWeaponMap().values()) {
					configs.add(getConfigLog(info.getNo() + "", info.getName(), configType,""));
				}
				break;
			case skill:
				for (SkillXmlInfo info : SkillInfoLoader.getSkillList().values()) {
					configs.add(getConfigLog(info.getNo() + "", info.getName(), configType,""));
				}
				break;
			case fightType:
				configs.addAll(getFightTypeConfigs());
				break;
			case mine:
				for (MineXMLInfo info : MineXMLInfoMap.getMap().values()) {
					configs.add(getConfigLog(info.getNo() + "", info.getName(), configType,""));
				}
				break;
			case condition:
				for (ConditionType conditionType : ConditionType.values()) {
					configs.add(getConfigLog(configType.getType()+"-"+conditionType.getType() + "", conditionType.getDesc(), configType,""));
				}
				break;
			case teamChallenge:
				Map<Integer, TeamChallengeXmlInfo> teamChallengeMap = TeamChallengeXmlLoader.getTeamChallengeMap();
				for (TeamChallengeXmlInfo info  : teamChallengeMap.values()) {
					configs.add(getConfigLog(info.getNo() + "", info.getName(), configType,""));
				}
				break;
			case payment:
				HashMap<Integer, PayXMLInfo> map = PayXMLInfoMap.getMap();
				for (PayXMLInfo info  : map.values()) {
					configs.add(getConfigLog(configType.getType()+"-"+info.getNo() + "", info.getName(), configType,String.valueOf(info.getMoneyCost())));
				}
				break;
			default:
				break;
			}
		}
		return configs;
	}

	/**
	 * 战斗id
	 * @return
	 */
	private static List<ConfigLog> getFightTypeConfigs() {
		List<ConfigLog> configs = new ArrayList<ConfigLog>();
		// 同步 GameLogService.insertInstanceLog FightInfo.getInstanceTypeId()
		for (FightType fightType : FightType.values()) {
			String fightTypeValue = ConfigType.fightType.getType() + "-" + fightType.getValue();
			String fightTypeName = fightType.getName();
			switch (fightType) {
			case FIGHT_TYPE_1:
			//case FIGHT_TYPE_13://(同 FIGHT_TYPE_1)
			//case FIGHT_TYPE_10://(同 FIGHT_TYPE_1)
				// 普通副本 战斗类型-副本类型,副本章节,副本编号
				// 对攻副本 战斗类型-副本类型,副本章节,副本编号
				// 主线副本 战斗类型-副本类型,副本章节,副本编号
				for (ChallengeBattleXmlInfo challenge : ChallengeBattleXmlInfoMap.getMapByNo().values()) {
					for (ChapterInfo chapter : challenge.getChallengeBattlesInfoMap().values()) {
						for (BattleDetail battle : chapter.getBattleDetailMap().values()) {
							String serial = challenge.getNo() + "," + chapter.getChapterNo()
									+ "," + battle.getBattleNo();
							String name = battle.getBattleName();
							configs.add(getConfigLog(serial, name, ConfigType.fightType,""));
						}
					}
				}
				break;
//			case FIGHT_TYPE_3:
//				// 宝石活动 战斗类型-关卡编号
//				PlayXMLInfo xmlInfo = PlayXMLInfoMap.getPlayXMLInfo(PlayXMLInfo.PLAY_TYPE_1);
//				for (PlayXMLBattle battle : xmlInfo.getBattles().values()) {
//					String serial = fightTypeValue + "-" + battle.getNo();
//					// TODO 关卡名称
//					String name = fightTypeName;
//					configs.add(getConfigLog(serial, name, ConfigType.fightType));
//				}
//				break;
			case FIGHT_TYPE_4:
				// 经验活动 战斗类型-活动级别
				for (ExpActivity exp : ExpActivityMap.getMap().values()) {
					String serial = fightTypeValue + "-" + exp.getLevel();
					String name = fightTypeName + "-" + exp.getName();
					configs.add(getConfigLog(serial, name, ConfigType.fightType,""));
				}
				break;
//			case FIGHT_TYPE_5:
//				// 银币活动 战斗类型-活动级别
//				for (MoneyActivity exp : MoneyActivityMap.getMap().values()) {
//					String serial = fightTypeValue + "-" + exp.getLevel();
//					// TODO 活动级别名称
//					String name = fightTypeName;
//					configs.add(getConfigLog(serial, name, ConfigType.fightType));
//				}
//				break;
			case FIGHT_TYPE_6:
				// 宝物活动 战斗类型-关卡编号
				CampaignXMLInfo xmlInfo1 = CampaignXMLInfoMap.getCampaignXMLInfo(CampaignXMLInfo.CAMPAIGN_TYPE_1);
				for (CampaignXMLBattle battle : xmlInfo1.getBattles().values()) {
					String serial = fightTypeValue + "-" + battle.getNo();
					String name = fightTypeName + "-" + battle.getName();
					configs.add(getConfigLog(serial, name, ConfigType.fightType,""));
				}
				break;
			case FIGHT_TYPE_12:
				// 活动防守 战斗类型-关卡编号
				for (DefendXMLInfo exp : DefendXMLInfoMap.getMap().values()) {
					String serial = fightTypeValue + "-" + exp.getNo();
					String name = fightTypeName + "-" + exp.getName();
					configs.add(getConfigLog(serial, name, ConfigType.fightType,""));
				}
				break;
			default:
				configs.add(getConfigLog(fightTypeValue, fightTypeName, ConfigType.fightType,""));
				break;
			}
		}

		return configs;
	}

	/**
	 * 获取ConfigLog
	 * @param serial
	 * @param name
	 * @param configType
	 * @return
	 */
	private static ConfigLog getConfigLog(String serial, String name, ConfigType configType,String comment) {
		ConfigLog log = new ConfigLog();
		log.setSerial(serial);
		log.setName(name);
		log.setType(configType.getType());
		log.setItemType(configType.getName());
		log.setState("1");
		log.setComment(comment);
		log.setCreateTime(new Timestamp(System.currentTimeMillis()));
		return log;
	}

	/**
	 * 获取行为
	 * @return
	 */
	public static Map<Integer, String> getGameAction() {
		String filePath = "F://workspace//SGOnline//RPG-Common//src//main//java//com//snail//webgame//game//common//GameAction.java";
		Map<Integer, String> result = new LinkedHashMap<Integer, String>();
		try {
			FileInputStream input = new FileInputStream(filePath);
			InputStreamReader fileReader = new InputStreamReader(input, "UTF-8");
			@SuppressWarnings("resource")
			BufferedReader in = new BufferedReader(fileReader);
			String s = null;
			int key = 0;
			String val = "";
			while ((s = in.readLine()) != null) {
				if (s.trim().startsWith("//")) {
					val = s.trim().replace("//", "");
				}
				if (s.trim().startsWith("public static final int")) {
					key = NumberUtils.toInt(s.trim().split("=")[1].trim().replace(";", ""));
					if (key != 0) {
						if (!result.containsKey(key)) {
							result.put(key, val);
							val = "";
						} else {
							System.out.println("[SYSTEM] key:" + key + " repeat");
							return null;
						}
					}
				}
			}
			in.close();
			fileReader.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}
}
