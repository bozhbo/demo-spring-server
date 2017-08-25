package com.snail.webgame.game.xml.load;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.math.NumberUtils;
import org.dom4j.Element;
import org.epilot.ccf.common.BlackProtocolList;

import com.snail.webgame.game.cache.RoleInfoMap;
import com.snail.webgame.game.cache.RoleLoginMap;
import com.snail.webgame.game.cache.RoleLoginQueueInfoMap;
import com.snail.webgame.game.common.GameValue;
import com.snail.webgame.game.common.HeroProType;
import com.snail.webgame.game.common.util.Command;
import com.snail.webgame.game.common.util.DateUtil;
import com.snail.webgame.game.info.RoleInfo;
import com.snail.webgame.game.info.RoleLoadInfo;
import com.snail.webgame.game.protocal.fight.competition.service.CompetitionService;
import com.snail.webgame.game.protocal.fight.mutual.service.MutualService;
import com.snail.webgame.game.xml.cache.RecruitDepotXMLInfoMap;

public class LoadBaseXML {

	private static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	/**
	 * Base.xml
	 * @param rootEle
	 * @return
	 */
	public static void load(Element rootEle,boolean modify) throws Exception {
		if (rootEle != null) {
			String no = rootEle.attributeValue("No").trim();
			String amount = rootEle.attributeValue("Amount").trim();

			// <!-- 克制系数 -->
			if (no.equals("Condition001")) {
				GameValue.BASE_DAO_KE_GONG = Float.valueOf(amount);
			} else if (no.equals("Condition002")) {
				GameValue.BASE_GONG_KE_QIANG = Float.valueOf(amount);
			} else if (no.equals("Condition003")) {
				GameValue.BASE_QIANG_KE_QI = Float.valueOf(amount);
			} else if (no.equals("Condition004")) {
				GameValue.BASE_QI_KE_DAO = Float.valueOf(amount);
			}
			// <!-- 角色-->
			else if (no.equals("Condition005")) {
				GameValue.NEW_BAG_EQUIP = Integer.parseInt(amount);
			} else if (no.equals("Condition006")) {
				GameValue.ROLE_INIT_SP_VAL = Short.parseShort(amount);
			} else if (no.equals("Condition007")) {
				GameValue.ROLE_INIT_MONEY = Integer.parseInt(amount);
			} else if (no.equals("Condition008")) {
				GameValue.ROLE_INIT_COIN = Integer.parseInt(amount);
			} else if (no.equals("Condition009")) {
				GameValue.ROLE_CHANGE_NAME_COST_COIN = Integer.parseInt(amount);
			} else if (no.equals("Condition010")) {
				GameValue.ROLE_ACC_NUM = Integer.parseInt(amount);
			} else if (no.equals("Condition011")) {
				GameValue.MAX_NAME_LENGTH = Integer.parseInt(amount);
			}

			// <!-- 抽卡-->
			else if (no.equals("Condition012")) {
				GameValue.RECRUIT_FREE_lIMIT = Byte.parseByte(amount);
			} else if (no.equals("Condition013")) {
				GameValue.RECRUIT_FREE_MONEY_TIME = Integer.parseInt(amount);
			} else if (no.equals("Condition014")) {
				GameValue.RECRUIT_FREE_COIN_TIME = Integer.parseInt(amount);
			}
			// <!-- 同步竞技场 -->
			else if (no.equals("Condition044")) {
				GameValue.COMPETITION_START_TIME = df.parse("2000-01-01 " + amount);
			} else if (no.equals("Condition045")) {
				GameValue.COMPETITION_END_TIME = df.parse("2000-01-01 " + amount);
			} else if (no.equals("Condition046")) {
				GameValue.COMPETITION_DOUBLE_START_TIME = df.parse("2000-01-01 " + amount);
			} else if (no.equals("Condition047")) {
				GameValue.COMPETITION_DOUBLE_END_TIME = df.parse("2000-01-01 " + amount);
			} else if (no.equals("Condition048")) {
				GameValue.COMPETITION_REWARD_TIME_HOUR = Integer.parseInt(amount);
			} else if (no.equals("Condition049")) {
				GameValue.COMPETITION_WIN_VALUE = Integer.parseInt(amount);
			} else if (no.equals("Condition050")) {
				GameValue.COMPETITION_LOSE_VALUE = Integer.parseInt(amount);
			} else if (no.equals("Condition051")) {
				GameValue.COMPETITION_RISE_VALUE_PERCENT = Double.parseDouble(amount);
			} else if (no.equals("Condition052")) {
				GameValue.COMPETITION_DOWN_VALUE_PERCENT = Double.parseDouble(amount);
			} else if (no.equals("Condition053")) {
				GameValue.COMPETITION_RISE_COMPLETE_TIMES = Integer.parseInt(amount);
			} else if (no.equals("Condition054")) {
				GameValue.COMPETITION_REWARD_TIME_WEEK = Integer.parseInt(amount);
			} else if (no.equals("Condition055")) {
				GameValue.COMPETITION_FIGHT_MAX_TIME = Integer.parseInt(amount) * 60 * 1000;
			} else if (no.equals("Condition056")) {
				GameValue.COMPETITION_RANK_MAX_NUM = Integer.parseInt(amount);
			} else if (no.equals("Condition057")) {
				GameValue.COMPETITION_RANK_PAGE_NUM = Integer.parseInt(amount);
			} else if (no.equals("Condition058")) {
				GameValue.COMPETITION_FIGHT_RECORD_NUM = Integer.parseInt(amount);
			} else if (no.equals("Condition059")) {
				GameValue.XIAO_WEI_1_NO = Byte.parseByte(amount);
			} else if (no.equals("Condition060")) {
				GameValue.COMPETITION_FIGHT_COST_ENERGY_VALUE = Integer.parseInt(amount);
			}

			else if (no.equals("Condition101")) {
				GameValue.HERO_EXP_ZH_MONEY_RATE = Integer.parseInt(amount);
			}
			// <!-- 体力 -->
			else if (no.equals("Condition102")) {
				GameValue.ROLE_SP_ADD_LEVEL = Integer.parseInt(amount);
			} else if (no.equals("Condition103")) {
				GameValue.ROLE_SP_ADD_BUY = Integer.parseInt(amount);
			} else if (no.equals("Condition104")) {
				GameValue.ROLE_INIT_SP_LIMIT = Short.parseShort(amount);
			} else if (no.equals("Condition105")) {
				GameValue.ROLE_SP_ADD_PERIOD = Integer.parseInt(amount);
			} else if (no.equals("Condition106")) {
				GameValue.ROLE_SP_BUY_NUM = Integer.parseInt(amount);
			}

			else if (no.equals("Condition108")) {
				GameValue.VAN_ONLOCK_LEVEL = Integer.parseInt(amount);
			} else if (no.equals("Condition109")) {
				GameValue.ASSISTANT_ONLOCK_LEVEL = Integer.parseInt(amount);
			}

			else if (no.equals("Condition110")) {
				GameValue.ACTIVITY_EXP_MAX_ATTEND_TIMES = Byte.parseByte(amount);
			} else if (no.equals("Condition111")) {
				GameValue.ACTIVITY_MONYEY_MAX_ATTEND_TIMES = Byte.parseByte(amount);
			}
			// <!-- 副将初始装备-->
			else if (no.equals("Condition115")) {
				GameValue.NEW_HERO_WEAPON = Integer.parseInt(amount);
			} else if (no.equals("Condition116")) {
				GameValue.NEW_HERO_CLOTH = Integer.parseInt(amount);
			} else if (no.equals("Condition117")) {
				GameValue.NEW_HERO_HELMET = Integer.parseInt(amount);
			} else if (no.equals("Condition118")) {
				GameValue.NEW_HERO_CUFF = Integer.parseInt(amount);
			} else if (no.equals("Condition119")) {
				GameValue.NEW_HERO_SHOE = Integer.parseInt(amount);
			} else if (no.equals("Condition120")) {
				GameValue.NEW_HERO_CLOAK = Integer.parseInt(amount);
			}

			// <!-- 抽奖-->
			else if (no.equals("Condition122")) {
				GameValue.FIRST_REQUEST_MONEY_HERO_1 = Integer.parseInt(amount);
			} else if (no.equals("Condition123")) {
				GameValue.FIRST_REQUEST_MONEY_HERO_2 = Integer.parseInt(amount);
			} else if (no.equals("Condition124")) {
				GameValue.FIRST_REQUEST_MONEY_HERO_3 = Integer.parseInt(amount);
			}

			else if (no.equals("Condition125")) {
				GameValue.GAME_CHAT_TIME = Integer.parseInt(amount);
			} else if (no.equals("Condition126")) {
				GameValue.SCENE_ROLE_NUM = Integer.parseInt(amount);
			} else if (no.equals("Condition127")) {
				GameValue.MAP_AREA_SEE_NUM = Integer.parseInt(amount);
			} else if (no.equals("Condition128")) {
				GameValue.MAP_AREA_SEE_LV = Integer.parseInt(amount);
			}

			// <!-- 邮件 -->
			else if (no.equals("Condition132")) {
				GameValue.SAVE_MAIL_MAX = Integer.parseInt(amount);
			} else if (no.equals("Condition133")) {
				GameValue.MAIL_PER_PAGE = Integer.parseInt(amount);
			} else if (no.equals("Condition134")) {
				GameValue.MAIL_ATTACHMENT_MAX_NUM = Integer.parseInt(amount);
			} else if (no.equals("Condition135")) {
				GameValue.MAIL_TIMER_DEL_CYCLE = Integer.parseInt(amount) * 24 * 3600;
			} else if (no.equals("Condition136")) {
				GameValue.MAIL_CHECK_DELETE = Integer.parseInt(amount);
			} else if (no.equals("Condition137")) {
				GameValue.CAN_SWEEP_STAR_NUM = Integer.parseInt(amount);
			}

			// <!-- 异步竞技场 -->
			else if (no.equals("Condition141")) {
				GameValue.ARENA_FIGHT_COST_ENERGY = Integer.parseInt(amount);
			} else if (no.equals("Condition142")) {
				GameValue.ARENA_FIGHT_FP_BAG = amount;
			} else if (no.equals("Condition143")) {
				GameValue.ARENA_FIGHT_FP_EXP_RATE = Integer.parseInt(amount);
			} else if (no.equals("Condition144")) {
				GameValue.ARENA_FIGHT_FP_EXP_VAL = Integer.parseInt(amount);
			} else if (no.equals("Condition145")) {
				GameValue.ARENA_RANKING_SHOW_LIMIT = Integer.parseInt(amount);
			} else if (no.equals("Condition146")) {
				if (!DateUtil.verifyHMTime(amount)) {
					throw new RuntimeException("Base.xml Condition146 error!");
				}
				GameValue.ARENA_SEND_PLACE_TIME = amount;
			} else if (no.equals("Condition147")) {
				GameValue.ARENA_NPC_RANDOM_LIMIT = Integer.parseInt(amount);
			} else if (no.equals("Condition148")) {
				GameValue.ARENA_NPC_EQUIP_RATE = Double.parseDouble(amount);
			} else if (no.equals("Condition149")) {
				GameValue.ARENA_LOOK_RANGE = Float.parseFloat(amount);
			}
			// <!-- 技能点 -->
			else if (no.equals("Condition153")) {// 技能点恢复上限
				GameValue.ROLE_INIT_TECH_VAL = Short.parseShort(amount);
				GameValue.ROLE_INIT_TECH_LIMIT = Short.parseShort(amount);
			} else if (no.equals("Condition154")) {// 每点技能点恢复所需要的时间(s)
				GameValue.ROLE_TECH_ADD_PERIOD = Integer.parseInt(amount);
			} else if (no.equals("Condition155")) {// 熔炼开放等级
				GameValue.ROLE_RESOLVE_EQUIP_LIMIT_LEVEL = Integer.parseInt(amount);
			}
			// <!-- 神兵 1.青龙 2.白虎 3.朱雀 4.玄武 5.麒麟 6.经验 -->
			else if (no.equals("Condition158")) {
				GameValue.WEAPON_FIELD_1_LV = Integer.parseInt(amount);
			} else if (no.equals("Condition159")) {
				GameValue.WEAPON_FIELD_2_LV = Integer.parseInt(amount);
			} else if (no.equals("Condition160")) {
				GameValue.WEAPON_FIELD_3_LV = Integer.parseInt(amount);
			} else if (no.equals("Condition161")) {
				GameValue.WEAPON_FIELD_4_LV = Integer.parseInt(amount);
			} else if (no.equals("Condition162")) {
				GameValue.WEAPON_FIELD_5_LV = Integer.parseInt(amount);
			}
			// <!--抽奖公告 -->
			else if (no.equals("Condition163")) {
				GameValue.RECRUIT_NEW_HERO_NOTICE_CONTENT = amount;
			}
			// <!-- 精力 -->
			else if (no.equals("Condition166")) {
				GameValue.ROLE_ENERGY_ADD_LEVEL = Integer.parseInt(amount);
			} else if (no.equals("Condition167")) {
				GameValue.ROLE_ENERGY_ADD_BUY = Integer.parseInt(amount);
			} else if (no.equals("Condition168")) {
				GameValue.ROLE_INIT_ENERGY_LIMIT = Short.parseShort(amount);
			} else if (no.equals("Condition169")) {
				GameValue.ROLE_ENERGY_ADD_PERIOD = Integer.parseInt(amount);
			} else if (no.equals("Condition170")) {
				GameValue.ROLE_ENERGY_BUY_NUM = Integer.parseInt(amount);
			} else if (no.equals("Condition171")) {
				GameValue.ROLE_INIT_ENERGY_VAL = Short.parseShort(amount);
			} else if (no.equals("Condition172")) {// 安全模式开启花费金子
				GameValue.SAFEMODE_COST_GOLD = Integer.parseInt(amount);
			} else if (no.equals("Condition173")) {// 安全模式开启时长（秒）
				GameValue.SAFEMODE_OPEN_TIME = Long.parseLong(amount) * 1000;
			} else if (no.equals("Condition174")) {// 单次夺宝消耗精力值
				GameValue.SNATCH_CHALLENGE_ENERGY = Byte.parseByte(amount);
			}
			// <!-- 夺宝 -->
			else if (no.equals("Condition175")) {
				// 羁绊武将位置开启等级
				String lvs[] = amount.trim().split(",");
				if (lvs != null && lvs.length > 0) {
					for (int i = 6; i < lvs.length + 6; i++) {
						GameValue.FIGHT_DEPLOY_FUN_OPEN_LV.put((byte) i, Integer.parseInt(lvs[i - 6]));
					}
				} else {
					throw new RuntimeException("Base.xml Condition175 error!");
				}
			} else if (no.equals("Condition176")) {
				String lvs[] = amount.trim().split(",");
				if (lvs != null && lvs.length > 0 && lvs.length <= 4) {
					for (int i = 2; i < lvs.length + 2; i++) {
						GameValue.FIGHT_DEPLOY_FUN_OPEN_LV.put((byte) i, Integer.parseInt(lvs[i - 2]));
					}
				} else {
					throw new RuntimeException("Base.xml Condition176 error!");
				}
			} else if (no.equals("Condition177")) {
				GameValue.PER_ARENA_PAGE_NUM = Integer.parseInt(amount);
			} else if (no.equals("Condition178")) {
				GameValue.ATTACK_ANOTHER_EVERY_DAY_TIME = Byte.parseByte(amount);
			}

			// <!--装备强化 -->
			else if (no.equals("Condition164")) {
				GameValue.EQUIP_STRENGEH_RATE = Integer.parseInt(amount);
			} else if (no.equals("Condition165")) {
				GameValue.EQUIP_STRENGEH_ITEM = Integer.parseInt(amount);
			}
			// 经验系数
			else if (no.equals("Condition179")) {
				GameValue.EXP_VALUE = Integer.parseInt(amount);
			} else if (no.equals("Condition180")) {
				GameValue.EXP_ADD = Integer.parseInt(amount);
			} else if (no.equals("Condition181")) {
				GameValue.SNATCH_EXP_VALUE = Integer.parseInt(amount);
			} else if (no.equals("Condition182")) {
				GameValue.SNATCH_EXP_ADD = Integer.parseInt(amount);
			} else if (no.equals("Condition183")) {
				GameValue.MAX_TYPE = Integer.parseInt(amount);
			} else if (no.equals("Condition184")) {
				GameValue.SNATCH_SWITCH = Byte.parseByte(amount);
			} else if (no.equals("Condition185")) {
				GameValue.ATTACK_ANOTHER_SWITCH = Byte.parseByte(amount);
			} else if (no.equals("Condition186")) {
				GameValue.DEFEND_SWITCH = Byte.parseByte(amount);
			} else if (no.equals("Condition187")) {
				GameValue.ATTACK_ANOTHER_MATCH_FIGHT_VALUE_MIN = Double.parseDouble(amount);
			} else if (no.equals("Condition188")) {
				GameValue.ATTACK_ANOTHER_MATCH_FIGHT_VALUE_MAX = Double.parseDouble(amount);
			} else if (no.equals("Condition189")) {
				GameValue.DEFEND_EVERY_DAY_TIME = Byte.parseByte(amount);
			} else if (no.equals("Condition190")) {
				GameValue.WEAPON_POS_1_LV = Byte.parseByte(amount);
			} else if (no.equals("Condition191")) {
				GameValue.WEAPON_POS_2_LV = Byte.parseByte(amount);
			} else if (no.equals("Condition192")) {
				GameValue.WEAPON_POS_3_LV = Byte.parseByte(amount);
			} else if (no.equals("Condition193")) {
				GameValue.WEAPON_POS_4_LV = Byte.parseByte(amount);
			} else if (no.equals("Condition194")) {
				GameValue.WEAPON_POS_5_LV = Byte.parseByte(amount);
			} else if (no.equals("Condition195")) {
				GameValue.WEAPON_POS_6_LV = Byte.parseByte(amount);
			} else if (no.equals("Condition200")) {
				GameValue.SOLDIER_UPGRADE_OPEN_LEVEL = Integer.parseInt(amount);
			} else if (no.equals("Condition201")) {
				GameValue.MONEY_FIX_LOTTERY_TIMES = Integer.parseInt(amount);
			} else if (no.equals("Condition202")) {
				GameValue.HERO_FIX_LOTTERY_TIMES = Integer.parseInt(amount);
			} else if (no.equals("Condition203")) {
				GameValue.BUY_ENY_SP_OPEN = Integer.parseInt(amount);
			} else if (no.equals("Condition204")) {
				GameValue.GAME_TASK_OPEN = Integer.parseInt(amount);
			} else if (no.equals("Condition205")) {
				GameValue.GAME_CHALLENGE_OPEN = Integer.parseInt(amount);
			} else if (no.equals("Condition206")) {
				GameValue.GAME_SOLIDER_OPEN = Integer.parseInt(amount);
			} else if (no.equals("Condition207")) {
				GameValue.GAME_ATTACK_OPEN = Integer.parseInt(amount);
			} else if (no.equals("Condition208")) {
				GameValue.GAME_BLJD_OPEN = Integer.parseInt(amount);
			} else if (no.equals("Condition209")) {
				GameValue.GAME_GCLD_OPEN = Integer.parseInt(amount);
			} else if (no.equals("Condition210")) {
				GameValue.GAME_NIWD_OPEN = Integer.parseInt(amount);
			} else if (no.equals("Condition211")) {
				GameValue.GAME_JJC_OPEN = Integer.parseInt(amount);
			} else if (no.equals("Condition212")) {
				GameValue.GAME_KUJJC_OPEN = Integer.parseInt(amount);
			} else if (no.equals("Condition213")) {
				GameValue.GAME_ROLE_OPEN = Integer.parseInt(amount);
			} else if (no.equals("Condition214")) {
				GameValue.GAME_DZ_OPEN = Integer.parseInt(amount);
			} else if (no.equals("Condition215")) {
				GameValue.GAME_RL_OPEN = Integer.parseInt(amount);
			} else if (no.equals("Condition216")) {
				GameValue.GAME_SB_OPEN = Integer.parseInt(amount);
			} else if (no.equals("Condition217")) {
				GameValue.GAME_BF_OPEN = Integer.parseInt(amount);
			} else if (no.equals("Condition218")) {
				GameValue.GAME_SR_OPEN = Integer.parseInt(amount);
			} else if (no.equals("Condition219")) {
				GameValue.GAME_MAIL_OPEN = Integer.parseInt(amount);
			} else if (no.equals("Condition220")) {
				GameValue.GAME_SYS_OPEN = Integer.parseInt(amount);
			} else if (no.equals("Condition221")) {
				GameValue.GAME_BX_OPEN = Integer.parseInt(amount);
			} else if (no.equals("Condition222")) {
				GameValue.GAME_OUT_CITY_OPEN = Integer.parseInt(amount);
			} else if (no.equals("Condition223")) {
				GameValue.GAME_LB_OPEN = Integer.parseInt(amount);
			} else if (no.equals("Condition224")) {
				GameValue.GAME_HERO_OPEN = Integer.parseInt(amount);
			} else if (no.equals("Condition226")) {
				String[] strs = amount.split(",");
				if (strs != null) {
					List<Integer> list = new ArrayList<Integer>();
					for (String str : strs) {
						int val = NumberUtils.toInt(str);
						if (val != 0) {
							if (!list.contains(val)) {
								list.add(val);
							}
						}
					}
					GameValue.NEW_BAG_PROP = new int[list.size()];
					for (int i = 0; i < list.size(); i++) {
						GameValue.NEW_BAG_PROP[i] = list.get(i);
					}
				}

			} else if (no.equals("Condition227")) {
				GameValue.ARENA_PVE_ACT_MAIN_RATE.put(HeroProType.I_hp, Double.parseDouble(amount));
			} else if (no.equals("Condition228")) {
				GameValue.ARENA_PVE_ACT_MAIN_RATE.put(HeroProType.I_soldierHp, Double.parseDouble(amount));
			} else if (no.equals("Condition229")) {
				GameValue.ARENA_PVE_ACT_MAIN_RATE.put(HeroProType.I_ad, Double.parseDouble(amount));
			} else if (no.equals("Condition230")) {
				GameValue.ARENA_PVE_ACT_MAIN_RATE.put(HeroProType.I_attack, Double.parseDouble(amount));
			} else if (no.equals("Condition231")) {
				GameValue.ARENA_PVE_ACT_MAIN_RATE.put(HeroProType.I_magicAttack, Double.parseDouble(amount));
			}

			else if (no.equals("Condition232")) {
				GameValue.COMPETITION_PVP_ACT_MAIN_RATE.put(HeroProType.I_hp, Double.parseDouble(amount));
			} else if (no.equals("Condition233")) {
				GameValue.COMPETITION_PVP_ACT_MAIN_RATE.put(HeroProType.I_soldierHp, Double.parseDouble(amount));
			} else if (no.equals("Condition234")) {
				GameValue.COMPETITION_PVP_ACT_MAIN_RATE.put(HeroProType.I_ad, Double.parseDouble(amount));
			} else if (no.equals("Condition235")) {
				GameValue.COMPETITION_PVP_ACT_MAIN_RATE.put(HeroProType.I_attack, Double.parseDouble(amount));
			} else if (no.equals("Condition236")) {
				GameValue.COMPETITION_PVP_ACT_MAIN_RATE.put(HeroProType.I_magicAttack, Double.parseDouble(amount));
			}

			else if (no.equals("Condition238")) {
				GameValue.GAME_REFINE_OPEN = Integer.parseInt(amount);
			} else if (no.equals("Condition239")) {
				GameValue.ATTACK_ANOTHER_MATCH_PROTECT_LEVEL = Integer.parseInt(amount);
			} else if (no.equals("Condition240")) {
				GameValue.ATTACK_ANOTHER_MATCHED_LEVEL = Integer.parseInt(amount);
			} else if (no.equals("Condition241")) {
				GameValue.EXP_VALUE_1 = Integer.parseInt(amount);
			} else if (no.equals("Condition242")) {
				GameValue.EXP_ADD_1 = Integer.parseInt(amount);
			} else if (no.equals("Condition243")) {
				GameValue.ARENA_FIGHT_CD_TIME = Integer.parseInt(amount);
			} else if (no.equals("Condition244")) {
				GameValue.ARENA_BUY_CD_COST_GOLD = Integer.parseInt(amount);
			} else if (no.equals("Condition250")) {
				GameValue.GAME_QUEST_RUN_MAX_NUM = Integer.parseInt(amount);
			} else if (no.equals("Condition245")) {
				GameValue.AFTER_DAY_NUM_REMOVE_GET = Integer.parseInt(amount);
			} else if (no.equals("Condition246")) {
				GameValue.GET_ENERGY_MAX_NUM_PER_DAY = Integer.parseInt(amount);
			} else if (no.equals("Condition247")) {
				GameValue.PRESENT_ENERGY_NUM = Integer.parseInt(amount);
			} else if (no.equals("Condition248")) {
				GameValue.FRIEND_LIST_LIMIT = Integer.parseInt(amount);
			} else if (no.equals("Condition249")) {
				GameValue.RECOMMEND_FRIEND_RANGE = Integer.parseInt(amount);
			} else if (no.equals("Condition251")) {
				GameValue.RECOMMEND_FRIEND_NUM = Integer.parseInt(amount);
			}

			else if (no.equals("Condition252")) {
				String[] strs = amount.split(",");
				if (strs == null || strs.length != 2) {
					throw new RuntimeException("Base.xml Condition252 error!");
				}
				GameValue.GAME_DEPLOY_POS_OPEN_GOLD_COST[0] = Integer.parseInt(strs[0]);
				GameValue.GAME_DEPLOY_POS_OPEN_GOLD_COST[1] = Integer.parseInt(strs[1]);
			} else if (no.equals("Condition253")) {
				GameValue.TODAY_FREE_LA_BIAO_NUM = Integer.parseInt(amount);
			} else if (no.equals("Condition254")) {
				GameValue.TODAY_JIE_BIAO_NUM = Integer.parseInt(amount);
			} else if (no.equals("Condition255")) {
				GameValue.BIAO_CHE_TIME = amount;
			} else if (no.equals("Condition256")) {
				GameValue.BIAO_CHE_TIME_PRIZE_RATE = Integer.parseInt(amount);
			} else if (no.equals("Condition257")) {
				GameValue.BIAO_CHE_HELP_PRIZE_NUM = Integer.parseInt(amount);
			} else if (no.equals("Condition258")) {
				GameValue.PER_HELP_HU_BIAO_TIME = Integer.parseInt(amount);
			} else if (no.equals("Condition259")) {
				GameValue.PER_BIAO_CHE_LAN_JIE_NUM = Integer.parseInt(amount);
			} else if (no.equals("Condition260")) {
				GameValue.BIAO_CHE_LAN_JIE_LOST_RATE = Float.valueOf(amount);
			} else if (no.equals("Condition261")) {
				GameValue.BIAO_CHE_HU_SONG_PRIZE_RATE = Float.valueOf(amount);
			} else if (no.equals("Condition262")) {
				GameValue.BIAO_CHE_HU_SONG_PRIZE_LOST_RATE = Float.valueOf(amount);
			} else if (no.equals("Condition263")) {
				GameValue.BIAO_CHE_REF_FREE_NUM = Integer.parseInt(amount);
			} else if (no.equals("Condition264")) {
				GameValue.BIAO_CHE_ATTEND_LEVEL = Integer.parseInt(amount);
			} else if (no.equals("Condition265")) {
				GameValue.BIAO_CHE_BUY_COST_COIN = Integer.parseInt(amount);
			} else if (no.equals("Condition270")) {
				GameValue.REF_BIAO_CHE_BASE_COIN = Integer.parseInt(amount);
			} else if (no.equals("Condition273")) {
				GameValue.FIRST_CHARGE_PRIZE_NO = amount;
			} else if (no.equals("Condition274")) {
				GameValue.PHONE_LINK_PRIZE_NO = amount;
			} else if (no.equals("Condition275")) {
				GameValue.FIRST_WEIXIN_PRIZE_NO = amount;
			} else if (no.equals("Condition276")) {
				GameValue.DAILY_WEIXIN_PRIZE_NO = amount;
			} else if (no.equals("Condition277")) {
				GameValue.CREATE_CLUB_COST_GOLD = Integer.parseInt(amount);
			} else if (no.equals("Condition278")) {
				GameValue.CREATE_CLUB_LEVEL_LIMIT = Integer.parseInt(amount);
			} else if (no.equals("Condition279")) {
				GameValue.CLUB_MEMBER_LIMIT = Integer.parseInt(amount);
			} else if (no.equals("Condition280")) {
				GameValue.CLUB_NAME_LIMIT = Integer.parseInt(amount);
			} else if (no.equals("Condition281")) {
				GameValue.CLUB_DECLARATION_LIMIT = Integer.parseInt(amount);
			} else if (no.equals("Condition282")) {
				GameValue.CLUB_DESCRIPTION_LIMIT = Integer.parseInt(amount);
			} else if (no.equals("Condition283")) {
				GameValue.ARENA_PVE_ACT_OTHER_RATE.put(HeroProType.I_hp, Double.parseDouble(amount));
			} else if (no.equals("Condition284")) {
				GameValue.ARENA_PVE_ACT_OTHER_RATE.put(HeroProType.I_soldierHp, Double.parseDouble(amount));
			} else if (no.equals("Condition285")) {
				GameValue.ARENA_PVE_ACT_OTHER_RATE.put(HeroProType.I_ad, Double.parseDouble(amount));
			} else if (no.equals("Condition286")) {
				GameValue.ARENA_PVE_ACT_OTHER_RATE.put(HeroProType.I_attack, Double.parseDouble(amount));
			} else if (no.equals("Condition287")) {
				GameValue.ARENA_PVE_ACT_OTHER_RATE.put(HeroProType.I_magicAttack, Double.parseDouble(amount));
			} else if (no.equals("Condition288")) {
				GameValue.COMPETITION_PVP_ACT_OTHER_RATE.put(HeroProType.I_hp, Double.parseDouble(amount));
			} else if (no.equals("Condition289")) {
				GameValue.COMPETITION_PVP_ACT_OTHER_RATE.put(HeroProType.I_soldierHp, Double.parseDouble(amount));
			} else if (no.equals("Condition290")) {
				GameValue.COMPETITION_PVP_ACT_OTHER_RATE.put(HeroProType.I_ad, Double.parseDouble(amount));
			} else if (no.equals("Condition291")) {
				GameValue.COMPETITION_PVP_ACT_OTHER_RATE.put(HeroProType.I_attack, Double.parseDouble(amount));
			} else if (no.equals("Condition292")) {
				GameValue.COMPETITION_PVP_ACT_OTHER_RATE.put(HeroProType.I_magicAttack, Double.parseDouble(amount));
			} else if (no.equals("Condition293")) {
				GameValue.CLUB_MAX_LIMIT = Integer.parseInt(amount);
			} else if (no.equals("Condition294")) {
				GameValue.CLUB_EVENT_LIMIT = Integer.parseInt(amount);
			} else if (no.equals("Condition295")) {
				GameValue.BUY_WONDER_PLAN_COST_COIN = Integer.parseInt(amount);
			} else if (no.equals("Condition296")) {
				GameValue.WORLD_BOSS_PRIZE_NUM = amount;
			} else if (no.equals("Condition297")) {
				GameValue.WORLD_BOSS_REFRESH_GOLD = Integer.parseInt(amount);
			} else if (no.equals("Condition298")) {
				GameValue.WORLD_BOSS_NEED_LV = Integer.parseInt(amount);
			} else if (no.equals("Condition299")) {
				GameValue.WORLD_BOSS_END_FIGHT_XY = amount;
			} else if (no.equals("Condition300")) {
				GameValue.BUY_WONDER_PLAN_VIP_LV = Integer.parseInt(amount);
			} else if (no.equals("Condition302")) {
				GameValue.MUTUAL_RANK_MAX_NUM = Integer.parseInt(amount);
			} else if (no.equals("Condition303")) {
				GameValue.MUTUAL_IGHT_MAX_TIME = Integer.parseInt(amount) * 1000;
			} else if (no.equals("Condition304")) {
				GameValue.MUTUAL_FIGHT_COST_ENERGY_VALUE = Integer.parseInt(amount);
			} else if (no.equals("Condition305")) {
				GameValue.MUTUAL_DAILY_FIGHT_COUNTS = Integer.parseInt(amount);
			} else if (no.equals("Condition306")) {
				GameValue.MUTUAL_FIGHT_START_TIME_1 = df.parse("2000-01-01 " + amount);
			} else if (no.equals("Condition307")) {
				GameValue.MUTUAL_FIGHT_END_TIME_1 = df.parse("2000-01-01 " + amount);
			} else if (no.equals("Condition308")) {
				GameValue.MUTUAL_FIGHT_START_TIME_2 = df.parse("2000-01-01 " + amount);
			} else if (no.equals("Condition309")) {
				GameValue.BUY_BLACK_SHOP_PRICE = Integer.parseInt(amount);
			} else if (no.equals("Condition310")) {
				GameValue.BUY_TURK_SHOP_PRICE = Integer.parseInt(amount);
			} else if (no.equals("Condition311")) {
				GameValue.MUTUAL_FIGHT_END_TIME_2 = df.parse("2000-01-01 " + amount);
			} else if (no.equals("Condition312")) {
				GameValue.BLACK_SHOP_OPEN_LV = Integer.parseInt(amount);
			} else if (no.equals("Condition313")) {
				GameValue.TURK_SHOP_OPEN_LV = Integer.parseInt(amount);
			} else if (no.equals("Condition314")) {
				GameValue.GOLE_SHOP_RANDOM = Integer.parseInt(amount);
			} else if (no.equals("Condition315")) {
				GameValue.TURK_SHOP_RANDOM = Integer.parseInt(amount);
			} else if (no.equals("Condition316")) {
				GameValue.FIRST_REQUEST_MONEY_HERO = Integer.parseInt(amount);
			} else if (no.equals("Condition317")) {
				GameValue.FIRST_REQUEST_GOLD_HERO = Integer.parseInt(amount);
			} else if (no.equals("Condition320")) {
				GameValue.PRE_MAP_NPC_FIGHT_EXP_PRIZE_A = Integer.parseInt(amount);
			} else if (no.equals("Condition321")) {
				GameValue.PRE_MAP_NPC_FIGHT_EXP_PRIZE_B = Integer.parseInt(amount);
			} else if (no.equals("Condition322")) {
				GameValue.BUY_SOLDIER_UP_PROBABILITY = Integer.parseInt(amount);
			} else if (no.equals("Condition323")) {
				GameValue.MUTUAL_FIGHT_INIT_SCOREVALUE = Integer.parseInt(amount);
			} else if (no.equals("Condition324")) {
				GameValue.MUTUAL_FIGHT_WON_SCOREVALUE = Integer.parseInt(amount);
			} else if (no.equals("Condition325")) {
				GameValue.MUTUAL_FIGHT_NECK_SCOREVALUE = Integer.parseInt(amount);
			} else if (no.equals("Condition326")) {
				GameValue.MUTUAL_FIGHT_FAILED_SCOREVALUE = Integer.parseInt(amount);
			} else if (no.equals("Condition327")) {
				GameValue.MUTUAL_FIGHT_DECREASE_BOTTOM_SCOREVALUE = Integer.parseInt(amount);
			}

			else if (no.equals("Condition328")) {
				GameValue.PVE_11_MAIN_RATE.put(HeroProType.I_hp, Double.parseDouble(amount));
			} else if (no.equals("Condition329")) {
				GameValue.PVE_11_MAIN_RATE.put(HeroProType.I_soldierHp, Double.parseDouble(amount));
			} else if (no.equals("Condition330")) {
				GameValue.PVE_11_MAIN_RATE.put(HeroProType.I_ad, Double.parseDouble(amount));
			} else if (no.equals("Condition331")) {
				GameValue.PVE_11_MAIN_RATE.put(HeroProType.I_attack, Double.parseDouble(amount));
			} else if (no.equals("Condition332")) {
				GameValue.PVE_11_MAIN_RATE.put(HeroProType.I_magicAttack, Double.parseDouble(amount));
			}

			else if (no.equals("Condition333")) {
				GameValue.PVE_11_OTHER_RATE.put(HeroProType.I_hp, Double.parseDouble(amount));
			} else if (no.equals("Condition334")) {
				GameValue.PVE_11_OTHER_RATE.put(HeroProType.I_soldierHp, Double.parseDouble(amount));
			} else if (no.equals("Condition335")) {
				GameValue.PVE_11_OTHER_RATE.put(HeroProType.I_ad, Double.parseDouble(amount));
			} else if (no.equals("Condition336")) {
				GameValue.PVE_11_OTHER_RATE.put(HeroProType.I_attack, Double.parseDouble(amount));
			} else if (no.equals("Condition337")) {
				GameValue.PVE_11_OTHER_RATE.put(HeroProType.I_magicAttack, Double.parseDouble(amount));
			}

			else if (no.equals("Condition338")) {
				GameValue.PVE_6_MAIN_RATE.put(HeroProType.I_hp, Double.parseDouble(amount));
			} else if (no.equals("Condition339")) {
				GameValue.PVE_6_MAIN_RATE.put(HeroProType.I_soldierHp, Double.parseDouble(amount));
			} else if (no.equals("Condition340")) {
				GameValue.PVE_6_MAIN_RATE.put(HeroProType.I_ad, Double.parseDouble(amount));
			} else if (no.equals("Condition341")) {
				GameValue.PVE_6_MAIN_RATE.put(HeroProType.I_attack, Double.parseDouble(amount));
			} else if (no.equals("Condition342")) {
				GameValue.PVE_6_MAIN_RATE.put(HeroProType.I_magicAttack, Double.parseDouble(amount));
			}

			else if (no.equals("Condition343")) {
				GameValue.PVE_6_OTHER_RATE.put(HeroProType.I_hp, Double.parseDouble(amount));
			} else if (no.equals("Condition344")) {
				GameValue.PVE_6_OTHER_RATE.put(HeroProType.I_soldierHp, Double.parseDouble(amount));
			} else if (no.equals("Condition345")) {
				GameValue.PVE_6_OTHER_RATE.put(HeroProType.I_ad, Double.parseDouble(amount));
			} else if (no.equals("Condition346")) {
				GameValue.PVE_6_OTHER_RATE.put(HeroProType.I_attack, Double.parseDouble(amount));
			} else if (no.equals("Condition347")) {
				GameValue.PVE_6_OTHER_RATE.put(HeroProType.I_magicAttack, Double.parseDouble(amount));
			}

			else if (no.equals("Condition348")) {
				GameValue.PVP_14_MAIN_RATE.put(HeroProType.I_hp, Double.parseDouble(amount));
			} else if (no.equals("Condition349")) {
				GameValue.PVP_14_MAIN_RATE.put(HeroProType.I_soldierHp, Double.parseDouble(amount));
			} else if (no.equals("Condition350")) {
				GameValue.PVP_14_MAIN_RATE.put(HeroProType.I_ad, Double.parseDouble(amount));
			} else if (no.equals("Condition351")) {
				GameValue.PVP_14_MAIN_RATE.put(HeroProType.I_attack, Double.parseDouble(amount));
			} else if (no.equals("Condition352")) {
				GameValue.PVP_14_MAIN_RATE.put(HeroProType.I_magicAttack, Double.parseDouble(amount));
			}

			else if (no.equals("Condition353")) {
				GameValue.PVP_14_OTHER_RATE.put(HeroProType.I_hp, Double.parseDouble(amount));
			} else if (no.equals("Condition354")) {
				GameValue.PVP_14_OTHER_RATE.put(HeroProType.I_soldierHp, Double.parseDouble(amount));
			} else if (no.equals("Condition355")) {
				GameValue.PVP_14_OTHER_RATE.put(HeroProType.I_ad, Double.parseDouble(amount));
			} else if (no.equals("Condition356")) {
				GameValue.PVP_14_OTHER_RATE.put(HeroProType.I_attack, Double.parseDouble(amount));
			} else if (no.equals("Condition357")) {
				GameValue.PVP_14_OTHER_RATE.put(HeroProType.I_magicAttack, Double.parseDouble(amount));
			}

			else if (no.equals("Condition358")) {
				GameValue.PVP_17_MAIN_RATE.put(HeroProType.I_hp, Double.parseDouble(amount));
			} else if (no.equals("Condition359")) {
				GameValue.PVP_17_MAIN_RATE.put(HeroProType.I_soldierHp, Double.parseDouble(amount));
			} else if (no.equals("Condition360")) {
				GameValue.PVP_17_MAIN_RATE.put(HeroProType.I_ad, Double.parseDouble(amount));
			} else if (no.equals("Condition361")) {
				GameValue.PVP_17_MAIN_RATE.put(HeroProType.I_attack, Double.parseDouble(amount));
			} else if (no.equals("Condition362")) {
				GameValue.PVP_17_MAIN_RATE.put(HeroProType.I_magicAttack, Double.parseDouble(amount));
			}

			else if (no.equals("Condition363")) {
				GameValue.PVP_17_OTHER_RATE.put(HeroProType.I_hp, Double.parseDouble(amount));
			} else if (no.equals("Condition364")) {
				GameValue.PVP_17_OTHER_RATE.put(HeroProType.I_soldierHp, Double.parseDouble(amount));
			} else if (no.equals("Condition365")) {
				GameValue.PVP_17_OTHER_RATE.put(HeroProType.I_ad, Double.parseDouble(amount));
			} else if (no.equals("Condition366")) {
				GameValue.PVP_17_OTHER_RATE.put(HeroProType.I_attack, Double.parseDouble(amount));
			} else if (no.equals("Condition367")) {
				GameValue.PVP_17_OTHER_RATE.put(HeroProType.I_magicAttack, Double.parseDouble(amount));
			}

			else if (no.equals("Condition368")) {
				GameValue.PVP_16_MAIN_RATE.put(HeroProType.I_hp, Double.parseDouble(amount));
			} else if (no.equals("Condition369")) {
				GameValue.PVP_16_MAIN_RATE.put(HeroProType.I_soldierHp, Double.parseDouble(amount));
			} else if (no.equals("Condition370")) {
				GameValue.PVP_16_MAIN_RATE.put(HeroProType.I_ad, Double.parseDouble(amount));
			} else if (no.equals("Condition371")) {
				GameValue.PVP_16_MAIN_RATE.put(HeroProType.I_attack, Double.parseDouble(amount));
			} else if (no.equals("Condition372")) {
				GameValue.PVP_16_MAIN_RATE.put(HeroProType.I_magicAttack, Double.parseDouble(amount));
			}

			else if (no.equals("Condition373")) {
				GameValue.PVP_16_OTHER_RATE.put(HeroProType.I_hp, Double.parseDouble(amount));
			} else if (no.equals("Condition374")) {
				GameValue.PVP_16_OTHER_RATE.put(HeroProType.I_soldierHp, Double.parseDouble(amount));
			} else if (no.equals("Condition375")) {
				GameValue.PVP_16_OTHER_RATE.put(HeroProType.I_ad, Double.parseDouble(amount));
			} else if (no.equals("Condition376")) {
				GameValue.PVP_16_OTHER_RATE.put(HeroProType.I_attack, Double.parseDouble(amount));
			} else if (no.equals("Condition377")) {
				GameValue.PVP_16_OTHER_RATE.put(HeroProType.I_magicAttack, Double.parseDouble(amount));
			} else if (no.equals("Condition379")) {
				GameValue.ONE_RECRUIT_FIX_REWARD_TIMES = Integer.parseInt(amount);
			} else if (no.equals("Condition380")) {
				GameValue.FIX_REWARD_NOSTR = amount;
			} else if (no.equals("Condition381")) {
				GameValue.CLUB_INFO_DISPALY_NUM = Integer.parseInt(amount);
			} else if (no.equals("Condition382")) {
				GameValue.CLUB_INFO_REQ_DISPALY_NUM = Integer.parseInt(amount);
			} else if (no.equals("Condition383")) {
				GameValue.WORLD_BOSS_ONE_REWARD = amount;
			} else if (no.equals("Condition384")) {
				GameValue.WORLD_BOSS_TWO_REWARD = amount;
			} else if (no.equals("Condition385")) {
				GameValue.GAME_LOGIN_QUEUE_FLAG = Integer.parseInt(amount);

				if (GameValue.GAME_LOGIN_QUEUE_FLAG == 0) {
					RoleLoginQueueInfoMap.removeAll();
				}
			} else if (no.equals("Condition386")) {
				GameValue.GAME_LOGIN_ONLINE_NUM = Integer.parseInt(amount);
			} else if (no.equals("Condition387")) {
				GameValue.USER_LOGOUT_TIME = Integer.parseInt(amount);
			} else if (no.equals("Condition388")) {
				GameValue.ROLE_RELATION_LIMIT = Integer.parseInt(amount);
			} else if (no.equals("Condition390")) {
				GameValue.COIN_RECRUIT_PRIZE = amount;

				String[] goldRecs = GameValue.COIN_RECRUIT_PRIZE.split(";");
				if (goldRecs != null && goldRecs.length > 0) {
					for (int i = 0; i < goldRecs.length; i++) {
						String[] goldRec = goldRecs[i].split(",");
						if (goldRec != null && goldRec.length > 0) {
							RecruitDepotXMLInfoMap.getGoldRecruitPrizeMap()
									.put(Integer.valueOf(goldRec[0]), goldRec[1]);
						}
					}
				}
			} else if (no.equals("Condition391")) {
				GameValue.COIN_FIX_LOTTERY_TIMES = Integer.parseInt(amount);
			} else if (no.equals("Condition392")) {
				GameValue.HERO_RECRUIT_FIX_REWARD_TIMES = Integer.parseInt(amount);
			} else if (no.equals("Condition393")) {
				GameValue.FIX_HERO_REWARD_NOSTR = amount;
			} else if (no.equals("Condition394")) {
				String[] recs = amount.split(";");
				if (recs != null && recs.length > 0) {
					for (int i = 0; i < recs.length; i++) {
						String[] rec = recs[i].split(",");
						if (rec != null && rec.length > 0) {
							RecruitDepotXMLInfoMap.getHeroRecruitPrizeMap().put(Integer.valueOf(rec[0]), rec[1]);
						}
					}
				}
			} else if (no.equals("Condition397")) {
				GameValue.ACTIVITY_EXP_TYPE1 = amount;
			} else if (no.equals("Condition398")) {
				GameValue.ACTIVITY_EXP_TYPE2 = amount;
			} else if (no.equals("Condition399")) {
				GameValue.ACTIVITY_EXP_TYPE3 = amount;
			} else if (no.equals("Condition400")) {
				GameValue.ACTIVITY_EXP_TYPE4 = amount;
			} else if (no.equals("Condition401")) {
				GameValue.ACTIVITY_EXP_TYPE5 = amount;
			} else if (no.equals("Condition403")) {
				GameValue.ATTACK_ANOTHER_EASY_DOWN = Double.parseDouble(amount);
			} else if (no.equals("Condition404")) {
				GameValue.ATTACK_ANOTHER_EASY_UPP = Double.parseDouble(amount);
			} else if (no.equals("Condition405")) {
				GameValue.ATTACK_ANOTHER_GENERAL_DOWN = Double.parseDouble(amount);
			} else if (no.equals("Condition406")) {
				GameValue.ATTACK_ANOTHER_GENERAL_UPP = Double.parseDouble(amount);
			} else if (no.equals("Condition407")) {
				GameValue.ATTACK_ANOTHER_DIFFICUTL_DOWN = Double.parseDouble(amount);
			} else if (no.equals("Condition408")) {
				GameValue.ATTACK_ANOTHER_DIFFICUTL_UPP = Double.parseDouble(amount);
			} else if (no.equals("Condition395")) {
				GameValue.REPLACE_HERO_REWARD_NOSTR = amount;
			} else if (no.equals("Condition396")) {
				GameValue.REPLACE_EQUIP_REWARD_NOSTR = amount;
			}

			else if (no.equals("Condition411")) {
				GameValue.SCENE_MINE_NUM_LIMIT = Integer.parseInt(amount);
			} else if (no.equals("Condition412")) {
				GameValue.SCENE_MINE_POSTION_LIMIT = Integer.parseInt(amount);
			} else if (no.equals("Condition413")) {
				GameValue.MINE_FIGHT_ENERGY_COST = Integer.parseInt(amount);
			} else if (no.equals("Condition417")) {
				GameValue.QUERY_MINE_COLLECT_SHOW_LIMEIT = Integer.parseInt(amount);
			} else if (no.equals("Condition418")) {
				GameValue.QUERY_MINE_LOOT_SHOW_LIMEIT = Integer.parseInt(amount);
			} else if (no.equals("Condition419")) {
				GameValue.QUERY_MINE_LOG_SHOW_LIMEIT = Integer.parseInt(amount);
			} else if (no.equals("Condition420")) {
				GameValue.MINE_HELP_INVITE_CONTENT = amount.trim();
			} else if (no.equals("Condition423")) {
				GameValue.SERVER_ALL_ROLE_NUM = Integer.parseInt(amount);
			} else if (no.equals("Condition424")) {
				GameValue.ACTIVITY_EXP_TYPE6 = amount;
			} else if (no.equals("Condition425")) {
				GameValue.MINE_NUM_LIMIT = Integer.parseInt(amount);
			} else if (no.equals("Condition426")) {
				GameValue.MINE_RATE_BY_QD = Integer.parseInt(amount);
			} else if (no.equals("Condition427")) {
				GameValue.MINE_RATE_BY_FQ = Integer.parseInt(amount);
			} else if (no.equals("Condition428")) {
				GameValue.ONE_CHEST_REWARD_MONEY = Integer.parseInt(amount);
			} else if (no.equals("Condition429")) {
				GameValue.TEN_CHEST_REWARD_MONEY = Integer.parseInt(amount);
			} else if (no.equals("Condition430")) {
				GameValue.ACTIVITY_EXP_TIMES_1 = Integer.parseInt(amount);
			} else if (no.equals("Condition431")) {
				GameValue.ACTIVITY_EXP_TIMES_2 = Integer.parseInt(amount);
			} else if (no.equals("Condition432")) {
				GameValue.ACTIVITY_EXP_TIMES_3 = Integer.parseInt(amount);
			} else if (no.equals("Condition433")) {
				GameValue.ACTIVITY_EXP_TIMES_4 = Integer.parseInt(amount);
			} else if (no.equals("Condition434")) {
				GameValue.ACTIVITY_EXP_TIMES_5 = Integer.parseInt(amount);
			} else if (no.equals("Condition435")) {
				GameValue.ACTIVITY_EXP_TIMES_6 = Integer.parseInt(amount);
			}

			else if (no.equals("Condition438")) {
				GameValue.PVE_18_19_MAIN_RATE.put(HeroProType.I_hp, Double.parseDouble(amount));
			} else if (no.equals("Condition439")) {
				GameValue.PVE_18_19_MAIN_RATE.put(HeroProType.I_soldierHp, Double.parseDouble(amount));
			} else if (no.equals("Condition440")) {
				GameValue.PVE_18_19_MAIN_RATE.put(HeroProType.I_ad, Double.parseDouble(amount));
			} else if (no.equals("Condition441")) {
				GameValue.PVE_18_19_MAIN_RATE.put(HeroProType.I_attack, Double.parseDouble(amount));
			} else if (no.equals("Condition442")) {
				GameValue.PVE_18_19_MAIN_RATE.put(HeroProType.I_magicAttack, Double.parseDouble(amount));
			}

			else if (no.equals("Condition443")) {
				GameValue.PVE_18_19_OTHER_RATE.put(HeroProType.I_hp, Double.parseDouble(amount));
			} else if (no.equals("Condition444")) {
				GameValue.PVE_18_19_OTHER_RATE.put(HeroProType.I_soldierHp, Double.parseDouble(amount));
			} else if (no.equals("Condition445")) {
				GameValue.PVE_18_19_OTHER_RATE.put(HeroProType.I_ad, Double.parseDouble(amount));
			} else if (no.equals("Condition446")) {
				GameValue.PVE_18_19_OTHER_RATE.put(HeroProType.I_attack, Double.parseDouble(amount));
			} else if (no.equals("Condition447")) {
				GameValue.PVE_18_19_OTHER_RATE.put(HeroProType.I_magicAttack, Double.parseDouble(amount));
			} else if (no.equals("Condition450")) {
				GameValue.WORLD_EVERY_FIGHT_REWARD = amount;
			} else if (no.equals("Condition451")) {
				GameValue.MAX_PROP_USE = Integer.parseInt(amount);
			} else if (no.equals("Condition452")) {
				GameValue.COMPOSE_MAX = Integer.parseInt(amount);
			} else if (no.equals("Condition453")) {
				GameValue.MAX_SELL_PROP = Integer.parseInt(amount);
			} else if (no.equals("Condition455")) {
				GameValue.GAME_ONLINE_MAX_NUM = Integer.parseInt(amount);
			}

			else if (no.equals("Condition456")) {
				GameValue.CAMPAIGN_REVICE_COST_GOLD = Integer.parseInt(amount);
			} else if (no.equals("Condition457")) {
				GameValue.FIGHT_TYPE_11_BASE_ADD_RATE = Double.parseDouble(amount);
			}
			// 大R膜拜模块
			else if (no.equals("Condition459")) {
				GameValue.WORSHIP_SEND_SP = Integer.parseInt(amount);
			} else if (no.equals("Condition460")) {
				GameValue.WORSHIP_MAX_NUM = Integer.parseInt(amount);
			} else if (no.equals("Condition461")) {
				GameValue.WORSHIP_NEED_LEVEL = Integer.parseInt(amount);
			} else if (no.equals("Condition462")) {
				if (!DateUtil.verifyHMTime(amount)) {
					throw new RuntimeException("Base.xml Condition462 error!");
				}
				GameValue.BE_WORSHIP_PRIZE_SEND_TIME = amount;
			} else if (no.equals("Condition463")) {
				GameValue.WORSHIP_SILVER_SEND = Integer.parseInt(amount);
			} else if (no.equals("Condition464")) {
				GameValue.BE_WORSHIP_PER_SILVER = Integer.parseInt(amount);
			} else if (no.equals("Condition469")) {
				GameValue.WORLD_CHAT_TEN_NUM_LEVEL = Integer.parseInt(amount);
			} else if (no.equals("Condition470")) {
				GameValue.WORLD_CHAT_NUM_LEVEL_PLUS = Integer.parseInt(amount);
			} else if (no.equals("Condition471")) {
				GameValue.WORLD_CHAT_MAX_LEVEL = Integer.parseInt(amount);
			} else if (no.equals("Condition472")) {
				GameValue.WORLD_CHAT_MAX_NUM = Integer.parseInt(amount);
			} else if (no.endsWith("Condition473")) {
				if (!DateUtil.verifyHMTime(amount)) {
					throw new RuntimeException("Base.xml Condition473 error!");
				}
				GameValue.COLLECT_START_TIME = amount;
			} else if (no.endsWith("Condition474")) {
				if (!DateUtil.verifyHMTime(amount)) {
					throw new RuntimeException("Base.xml Condition474 error!");
				}
				GameValue.COLLECT_END_TIME = amount;
			} else if (no.endsWith("Condition475")) {
				GameValue.COLLECT_PER_TIME = Integer.parseInt(amount);
			}
			// -----套装配法
			else if (no.endsWith("Condition494")) {
				GameValue.CAMPAIGN_HP_BACK_RATE = Double.parseDouble(amount);
			} else if (no.endsWith("Condition496")) {
				GameValue.SHIZHUANG = amount;
			} else if (no.endsWith("Condition500")) {
				GameValue.REWARD1 = amount;
			} else if (no.endsWith("Condition501")) {
				GameValue.REWARD2 = amount;
			} else if (no.endsWith("Condition502")) {
				GameValue.REWARD3 = amount;
			} else if (no.endsWith("Condition503")) {
				GameValue.REWARD4 = amount;
				// 最后一句把所有套装依次加到map里
				String[] SHIZHUANGstr = GameValue.SHIZHUANG.split(",");
				if (SHIZHUANGstr.length > 0) {
					GameValue.SHIZHUANG_REWARD_MAP.put(NumberUtils.toInt(SHIZHUANGstr[0]), GameValue.REWARD1);
				}
				if (SHIZHUANGstr.length > 1) {
					GameValue.SHIZHUANG_REWARD_MAP.put(NumberUtils.toInt(SHIZHUANGstr[1]), GameValue.REWARD2);
				}
				if (SHIZHUANGstr.length > 2) {
					GameValue.SHIZHUANG_REWARD_MAP.put(NumberUtils.toInt(SHIZHUANGstr[2]), GameValue.REWARD3);
				}
				if (SHIZHUANGstr.length > 3) {
					GameValue.SHIZHUANG_REWARD_MAP.put(NumberUtils.toInt(SHIZHUANGstr[3]), GameValue.REWARD4);
				}			
			} else if (no.endsWith("Condition495")) {
				if (amount != null && amount.length() > 0) {
					String[] gws = amount.split(",");
					for (String gw : gws) {
						GameValue.BOSS_MAP_NPC_NOS.add(gw);
					}
				}
			} else if (no.equals("Condition504")) {
				GameValue.ADD_FRIEND_REQUEST_LIMIT = Integer.parseInt(amount);
			} else if (no.equals("Condition505")) {
				GameValue.TEAM_CHALLENGE_COUNT = Integer.parseInt(amount);
			} else if (no.equals("Condition506")) {
				GameValue.CLUB_EXTEND_UP_RETURN = Integer.parseInt(amount);
			} else if (no.equals("Condition507")) {
				GameValue.CLUB_EXTEND_MIN_GOLD = Integer.parseInt(amount);
			} else if (no.equals("Condition508")) {
				GameValue.TEST_SERVER_FLAG = Integer.parseInt(amount);
			} else if (no.equals("Condition509")) {
				GameValue.TEST_SERVER_ROLE_VIP_LV = Integer.parseInt(amount);
			} else if (no.equals("Condition510")) {
				GameValue.TEAM_3V3_COUNT = Integer.parseInt(amount);
			} else if (no.equals("Condition511")) {
				GameValue.TEAM_FIGHT_START_TIME_1 = df.parse("2000-01-01 " + amount);
			} else if (no.equals("Condition512")) {
				GameValue.TEAM_FIGHT_END_TIME_1 = df.parse("2000-01-01 " + amount);
			} else if (no.equals("Condition513")) {
				GameValue.TEAM_FIGHT_START_TIME_2 = df.parse("2000-01-01 " + amount);
			} else if (no.equals("Condition514")) {
				GameValue.TEAM_FIGHT_END_TIME_2 = df.parse("2000-01-01 " + amount);
			} else if (no.equals("Condition515")) {
				GameValue.TEN_HERO_CHEST_REWARD_REPLACE_TIME = Integer.parseInt(amount);
			}else if(no.equals("Condition518")){
				GameValue.INIT_ROLE_CLUB_TECH_PLUS = amount;
			}else if(no.equals("Condition520")){
				GameValue.HIRE_HERO_CALL_BACK_MIN_TIME = Integer.parseInt(amount);
			}else if(no.equals("Condition521")){
				GameValue.HIRE_HERO_BASE_VALUE = Integer.parseInt(amount);
			}else if(no.equals("Condition522")){
				GameValue.HIRE_HERO_RATE = Double.parseDouble(amount);
			}else if(no.equals("Condition523")){
				GameValue.SEND_HIRE_HERO_BASE_VALUE = Integer.parseInt(amount);
			}else if(no.equals("Condition524")){
				GameValue.SEND_HIRE_HERO_RATE = Double.parseDouble(amount);
			}else if(no.equals("Condition525")){
				GameValue.SEND_HIRE_HERO_TIME_UNIT = Integer.parseInt(amount);
			}else if(no.equals("Condition526")){
				GameValue.SEND_HIRE_HERO_BASE_NUM = Integer.parseInt(amount);
			} else if (no.equals("Condition517")) {
				GameValue.RIDE_PASS_RATE = Integer.parseInt(amount);
			} else if (no.equals("Condition527")) {
				GameValue.APP_COMMENT_AWARD = Integer.parseInt(amount);
			}else if(no.equals("Condition528")){
				GameValue.HIRE_HERO_LEVEL_LIMIT = Integer.parseInt(amount);
			}else if(no.equals("Condition529")){
				GameValue.RIDE_PASS_COIN_COST = Integer.parseInt(amount);
			}else if(no.equals("Condition530")){
				GameValue.CHECK_TIME_FLAG = Integer.parseInt(amount);
			}else if(no.equals("Condition531")){
				GameValue.EQUIP_RETURN_ENCHANT_PROP_NO = Integer.parseInt(amount);
			} else if(no.equals("Condition532")){
				GameValue.MAX_HIRE_HERO_NUM = Integer.parseInt(amount);
			}else if(no.equals("Condition534")){
				GameValue.FIGHT_CHECK_FLAG = Integer.parseInt(amount);
			}else if(no.equals("Condition535")){
				GameValue.TEAM_CHALLENGE_RECONNECTED_TIME = Integer.parseInt(amount);
			}else if(no.equals("Condition537")){
				GameValue.PVE_BOSS_ERROR_FLAG = Integer.parseInt(amount);
			}else if(no.equals("Condition538")){
				GameValue.CHECK_FIGHT_NUM = Integer.parseInt(amount);
			} else if(no.equals("Condition539")){
				GameValue.CHECK_IN_PRIZE_HERO = amount;
			}else if (no.equals("Condition540")) {
				GameValue.SUPERR_START_PLUS_TIME = df.parse("2000-01-01 " + amount);
			}else if (no.equals("Condition541")) {
				GameValue.SUPERR_END_PLUS_TIME = df.parse("2000-01-01 " + amount);
			}else if(no.equals("Condition542")){
				GameValue.BETWEEN_SUPERR_PLUSBASE_TIME = Integer.parseInt(amount);
			}else if(no.equals("Condition543")){
				GameValue.SUPERR_PER_1 = amount;
			}else if(no.equals("Condition544")){
				GameValue.SUPERR_PER_2 = amount;
			}else if(no.equals("Condition545")){
				GameValue.SUPERR_PER_3 = amount;
			}else if(no.equals("Condition546")){
				GameValue.SUPERR_BASE_1 = Integer.parseInt(amount);
			}else if(no.equals("Condition547")){
				GameValue.SUPERR_BASE_2 = Integer.parseInt(amount);
			}else if(no.equals("Condition548")){
				GameValue.SUPERR_BASE_3 = Integer.parseInt(amount);
			}else if(no.equals("Condition551")){
				GameValue.RIDE_OPEN_1 = Integer.parseInt(amount);
			}else if(no.equals("Condition552")){
				GameValue.RIDE_OPEN_2 = Integer.parseInt(amount);
			}else if(no.equals("Condition553")){
				GameValue.RIDE_OPEN_3 = Integer.parseInt(amount);
			}else if(no.equals("Condition554")){
				GameValue.RIDE_OPEN_4 = Integer.parseInt(amount);
			}else if(no.equals("Condition555")){
				GameValue.RIDE_OPEN_5 = Integer.parseInt(amount);
			}else if(no.equals("Condition556")){
				GameValue.RIDE_OPEN_6 = Integer.parseInt(amount);
			}else if(no.equals("Condition560")){
				GameValue.PVE_LI_MA_WIN_FLAG = Integer.parseInt(amount);
			}else if(no.equals("Condition567")){
				GameValue.PVE_FIGHT_FLAG = Integer.parseInt(amount);
				
				if(GameValue.PVE_FIGHT_FLAG == 0)
				{
					Set<Integer> set = RoleInfoMap.getRoleInfoSet();
					for(Integer roleId:set)
					{
						RoleInfo roleInfo =  RoleLoginMap.getRoleInfo(roleId);
						if(roleInfo!=null)
						{
							RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
							if (roleLoadInfo == null)
							{
								continue;
							}
							
							//当极效关闭PVP时,正在报名的玩家强制取消
							if(roleLoadInfo.getInFight() == 1)
							{
								CompetitionService.cancelCompetitionFight(roleId);
							}
							else if(roleLoadInfo.getInFight() == 6 
									|| roleLoadInfo.getInFight() == 8 || roleLoadInfo.getInFight() == 9
									|| roleLoadInfo.getInFight() == 11 || roleLoadInfo.getInFight() == 13
									|| roleLoadInfo.getInFight() == 14 || roleLoadInfo.getInFight() == 15)
							{
								MutualService.getMutualService().cancelMatch(roleId);
							}
						}
					}
				}
			}
		}
		// 检查功能是否关闭
		checkProtocal();
	}

	private static void checkProtocal() {
		if (GameValue.BUY_ENY_SP_OPEN != 1) {
			BlackProtocolList.addBlackProtocol(Command.QUERY_QUEST_REQ);
			BlackProtocolList.addBlackProtocol(Command.FINISH_QUEST_REQ);
		} else {
			BlackProtocolList.removeBlackProtocol(Command.QUERY_QUEST_REQ);
			BlackProtocolList.removeBlackProtocol(Command.FINISH_QUEST_REQ);
		}
		if (GameValue.GAME_TASK_OPEN != 1) {

		} else {

		}
		if (GameValue.GAME_CHALLENGE_OPEN != 1) {
			BlackProtocolList.addBlackProtocol(Command.GET_CHALLENGE_CHAPTER_BATTLE_REQ);
			BlackProtocolList.addBlackProtocol(Command.SWEEP_CHALLENGE_CHAPTER_BATTLE_REQ);
		} else {
			BlackProtocolList.removeBlackProtocol(Command.GET_CHALLENGE_CHAPTER_BATTLE_REQ);
			BlackProtocolList.removeBlackProtocol(Command.SWEEP_CHALLENGE_CHAPTER_BATTLE_REQ);
		}
		if (GameValue.GAME_SOLIDER_OPEN != 1) {
			// 已在功能里处理
		}
		if (GameValue.GAME_ATTACK_OPEN != 1) {

		}
		if (GameValue.GAME_BLJD_OPEN != 1) {

		}
		if (GameValue.GAME_GCLD_OPEN != 1) {

		}
		if (GameValue.GAME_NIWD_OPEN != 1) {

		}
		if (GameValue.GAME_JJC_OPEN != 1) {

		} else {

		}
		if (GameValue.GAME_KUJJC_OPEN != 1) {
			BlackProtocolList.addBlackProtocol(Command.FIGHT_SUBMIT_COMPETITION_REQ);
			BlackProtocolList.addBlackProtocol(Command.FIGHT_PANEL_QUERY_REQ);
			BlackProtocolList.addBlackProtocol(Command.FIGHT_RANK_QUERY_REQ);
			BlackProtocolList.addBlackProtocol(Command.FIGHT_GET_AWARD_REQ);
			BlackProtocolList.addBlackProtocol(Command.FIGHT_USER_RANK_QUERY_REQ);
		} else {
			BlackProtocolList.removeBlackProtocol(Command.FIGHT_SUBMIT_COMPETITION_REQ);
			BlackProtocolList.removeBlackProtocol(Command.FIGHT_PANEL_QUERY_REQ);
			BlackProtocolList.removeBlackProtocol(Command.FIGHT_RANK_QUERY_REQ);
			BlackProtocolList.removeBlackProtocol(Command.FIGHT_GET_AWARD_REQ);
			BlackProtocolList.removeBlackProtocol(Command.FIGHT_USER_RANK_QUERY_REQ);
		}

		if (GameValue.GAME_ROLE_OPEN != 1) {

		}
		if (GameValue.GAME_DZ_OPEN != 1) {
			BlackProtocolList.addBlackProtocol(Command.EQUIP_ONEKEY_STRENGTH_REQ);
			BlackProtocolList.addBlackProtocol(Command.UP_EQUIP_REQ);
		} else {
			BlackProtocolList.removeBlackProtocol(Command.EQUIP_ONEKEY_STRENGTH_REQ);
			BlackProtocolList.removeBlackProtocol(Command.UP_EQUIP_REQ);
		}
		if (GameValue.GAME_RL_OPEN != 1) {
			BlackProtocolList.addBlackProtocol(Command.EQUIP_RESOLVE_REQ);
		} else {
			BlackProtocolList.removeBlackProtocol(Command.EQUIP_RESOLVE_REQ);
		}
		if (GameValue.GAME_SB_OPEN != 1) {
			BlackProtocolList.addBlackProtocol(Command.EQUIP_WEAPON_REQ);
			BlackProtocolList.addBlackProtocol(Command.UPGRADE_WEAPON_REQ);
		} else {
			BlackProtocolList.removeBlackProtocol(Command.EQUIP_WEAPON_REQ);
			BlackProtocolList.removeBlackProtocol(Command.UPGRADE_WEAPON_REQ);
		}
		if (GameValue.GAME_BF_OPEN != 1) {
			BlackProtocolList.addBlackProtocol(Command.QUERY_FIGHT_DEPLOY_REQ);
			BlackProtocolList.addBlackProtocol(Command.MODIFY_FIGHT_DEPLOY_REQ);
			BlackProtocolList.addBlackProtocol(Command.CHANGE_FIGHT_DEPLOY_REQ);
			BlackProtocolList.addBlackProtocol(Command.BUY_FIGHT_DEPLOY_REQ);
		} else {
			BlackProtocolList.removeBlackProtocol(Command.QUERY_FIGHT_DEPLOY_REQ);
			BlackProtocolList.removeBlackProtocol(Command.MODIFY_FIGHT_DEPLOY_REQ);
			BlackProtocolList.removeBlackProtocol(Command.CHANGE_FIGHT_DEPLOY_REQ);
			BlackProtocolList.removeBlackProtocol(Command.BUY_FIGHT_DEPLOY_REQ);
		}
		if (GameValue.GAME_SR_OPEN != 1) {
			BlackProtocolList.addBlackProtocol(Command.QUERY_STORE_ITEM_REQ);
			BlackProtocolList.addBlackProtocol(Command.REFRESH_STORE_ITEM_REQ);
		} else {
			BlackProtocolList.removeBlackProtocol(Command.QUERY_STORE_ITEM_REQ);
			BlackProtocolList.removeBlackProtocol(Command.REFRESH_STORE_ITEM_REQ);
		}
		if (GameValue.GAME_MAIL_OPEN != 1) {
			BlackProtocolList.addBlackProtocol(Command.MAIL_SEND_REQ);
			BlackProtocolList.addBlackProtocol(Command.MAIL_READ_REQ);
			BlackProtocolList.addBlackProtocol(Command.MAIL_DEL_REQ);
			BlackProtocolList.addBlackProtocol(Command.MAIL_LIST_REQ);
			BlackProtocolList.addBlackProtocol(Command.MAIL_GET_NEW_REQ);
			BlackProtocolList.addBlackProtocol(Command.MAIL_GET_ATTACHMENT_REQ);
		} else {
			BlackProtocolList.removeBlackProtocol(Command.MAIL_SEND_REQ);
			BlackProtocolList.removeBlackProtocol(Command.MAIL_READ_REQ);
			BlackProtocolList.removeBlackProtocol(Command.MAIL_DEL_REQ);
			BlackProtocolList.removeBlackProtocol(Command.MAIL_LIST_REQ);
			BlackProtocolList.removeBlackProtocol(Command.MAIL_GET_NEW_REQ);
			BlackProtocolList.removeBlackProtocol(Command.MAIL_GET_ATTACHMENT_REQ);
		}
		if (GameValue.GAME_SYS_OPEN != 1) {

		}
		if (GameValue.GAME_BX_OPEN != 1) {

		}
		if (GameValue.GAME_OUT_CITY_OPEN != 1) {
			BlackProtocolList.addBlackProtocol(Command.OUT_CIYT_REQ);
		}
		if (GameValue.GAME_LB_OPEN != 1) {
			BlackProtocolList.addBlackProtocol(Command.CHECKIN_EXECUTE_REQ);
			BlackProtocolList.addBlackProtocol(Command.CHECKIN_7DAY_QUERY_REQ);
			BlackProtocolList.addBlackProtocol(Command.CHECKIN_7DAY_AWARD_REQ);
			BlackProtocolList.addBlackProtocol(Command.QUERY_LEVEL_GIFT_REQ);
			BlackProtocolList.addBlackProtocol(Command.GET_LEVEL_GIFT_REQ);
		} else {
			BlackProtocolList.removeBlackProtocol(Command.CHECKIN_EXECUTE_REQ);
			BlackProtocolList.removeBlackProtocol(Command.CHECKIN_7DAY_QUERY_REQ);
			BlackProtocolList.removeBlackProtocol(Command.CHECKIN_7DAY_AWARD_REQ);
			BlackProtocolList.removeBlackProtocol(Command.QUERY_LEVEL_GIFT_REQ);
			BlackProtocolList.removeBlackProtocol(Command.GET_LEVEL_GIFT_REQ);
		}
		if (GameValue.GAME_HERO_OPEN != 1) {
			BlackProtocolList.addBlackProtocol(Command.HERO_LEVEL_UP_REQ);
			BlackProtocolList.addBlackProtocol(Command.MERGE_HERO_REQ);
			BlackProtocolList.addBlackProtocol(Command.HELO_COLOR_UP_REQ);
			BlackProtocolList.addBlackProtocol(Command.HELO_STAR_UP_REQ);
			BlackProtocolList.addBlackProtocol(Command.HELO_SKILL_UP_REQ);
		} else {
			BlackProtocolList.removeBlackProtocol(Command.HERO_LEVEL_UP_REQ);
			BlackProtocolList.removeBlackProtocol(Command.MERGE_HERO_REQ);
			BlackProtocolList.removeBlackProtocol(Command.HELO_COLOR_UP_REQ);
			BlackProtocolList.removeBlackProtocol(Command.HELO_STAR_UP_REQ);
			BlackProtocolList.removeBlackProtocol(Command.HELO_SKILL_UP_REQ);
		}
		if (GameValue.GAME_REFINE_OPEN != 1) {
			BlackProtocolList.addBlackProtocol(Command.EQUIP_REFINE_REQ);
		} else {
			BlackProtocolList.removeBlackProtocol(Command.EQUIP_REFINE_REQ);
		}
	}

}
