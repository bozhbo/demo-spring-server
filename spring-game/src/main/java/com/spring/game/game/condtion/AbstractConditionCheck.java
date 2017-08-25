package com.snail.webgame.game.condtion;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;

import com.snail.webgame.game.condtion.conds.ActiveNumCond;
import com.snail.webgame.game.condtion.conds.AllFinishFightCond;
import com.snail.webgame.game.condtion.conds.ArenaCond;
import com.snail.webgame.game.condtion.conds.AttackNPCCond;
import com.snail.webgame.game.condtion.conds.AttackRoleCond;
import com.snail.webgame.game.condtion.conds.BljdCond;
import com.snail.webgame.game.condtion.conds.BuyMoneyCond;
import com.snail.webgame.game.condtion.conds.BuySpCond;
import com.snail.webgame.game.condtion.conds.CallHeroCond;
import com.snail.webgame.game.condtion.conds.ChallengeChapterCond;
import com.snail.webgame.game.condtion.conds.ChangBpCond;
import com.snail.webgame.game.condtion.conds.ChoukaCond;
import com.snail.webgame.game.condtion.conds.ClubBuildCond;
import com.snail.webgame.game.condtion.conds.CoinCond;
import com.snail.webgame.game.condtion.conds.CompoundHeroCond;
import com.snail.webgame.game.condtion.conds.ContributionCond;
import com.snail.webgame.game.condtion.conds.CourageCond;
import com.snail.webgame.game.condtion.conds.EnergyCond;
import com.snail.webgame.game.condtion.conds.EquipCond;
import com.snail.webgame.game.condtion.conds.EquipRefineCond;
import com.snail.webgame.game.condtion.conds.EquipStrengCond;
import com.snail.webgame.game.condtion.conds.ExploitCond;
import com.snail.webgame.game.condtion.conds.FinishChallengeCond;
import com.snail.webgame.game.condtion.conds.FinishFbNumCond;
import com.snail.webgame.game.condtion.conds.FinishTaskCond;
import com.snail.webgame.game.condtion.conds.FuliCardCond;
import com.snail.webgame.game.condtion.conds.GgzjCond;
import com.snail.webgame.game.condtion.conds.GiveEnergyCond;
import com.snail.webgame.game.condtion.conds.HeroLvCond;
import com.snail.webgame.game.condtion.conds.HeroNumCond;
import com.snail.webgame.game.condtion.conds.HeroQualityCond;
import com.snail.webgame.game.condtion.conds.HeroSkillLvCond;
import com.snail.webgame.game.condtion.conds.HeroWearEquipCond;
import com.snail.webgame.game.condtion.conds.JnupCond;
import com.snail.webgame.game.condtion.conds.JtclCond;
import com.snail.webgame.game.condtion.conds.JusticeCond;
import com.snail.webgame.game.condtion.conds.KejiCond;
import com.snail.webgame.game.condtion.conds.KuafuMoneyCond;
import com.snail.webgame.game.condtion.conds.LdCond;
import com.snail.webgame.game.condtion.conds.MineNumCond;
import com.snail.webgame.game.condtion.conds.MoneyCond;
import com.snail.webgame.game.condtion.conds.NzwdCond;
import com.snail.webgame.game.condtion.conds.OpActConsumeCond;
import com.snail.webgame.game.condtion.conds.OpActFinishFbCond;
import com.snail.webgame.game.condtion.conds.OpActLoginCond;
import com.snail.webgame.game.condtion.conds.OpActPayCond;
import com.snail.webgame.game.condtion.conds.OpActPayDaysCond;
import com.snail.webgame.game.condtion.conds.OpActSdPayCond;
import com.snail.webgame.game.condtion.conds.OpActTreasure1Cond;
import com.snail.webgame.game.condtion.conds.OpActTreasure2Cond;
import com.snail.webgame.game.condtion.conds.OpActTreasure3Cond;
import com.snail.webgame.game.condtion.conds.OpActTreasure4Cond;
import com.snail.webgame.game.condtion.conds.OpActTreasureCond;
import com.snail.webgame.game.condtion.conds.Pvp3MoneyCond;
import com.snail.webgame.game.condtion.conds.PvpCond;
import com.snail.webgame.game.condtion.conds.QiandaoCond;
import com.snail.webgame.game.condtion.conds.ReachCityCond;
import com.snail.webgame.game.condtion.conds.ReachMapNPCCond;
import com.snail.webgame.game.condtion.conds.RoleLvCond;
import com.snail.webgame.game.condtion.conds.RoleLvRangCond;
import com.snail.webgame.game.condtion.conds.RoleQualityCond;
import com.snail.webgame.game.condtion.conds.RoleSkillLvCond;
import com.snail.webgame.game.condtion.conds.RoleWeaponLvCond;
import com.snail.webgame.game.condtion.conds.ShjwCond;
import com.snail.webgame.game.condtion.conds.ShowFuliCardCond;
import com.snail.webgame.game.condtion.conds.ShowTimeCardCond;
import com.snail.webgame.game.condtion.conds.SpCond;
import com.snail.webgame.game.condtion.conds.StarMoneyCond;
import com.snail.webgame.game.condtion.conds.StayMapNPCCond;
import com.snail.webgame.game.condtion.conds.Team3V3FightCond;
import com.snail.webgame.game.condtion.conds.TeamChallengeCond;
import com.snail.webgame.game.condtion.conds.TeamMoneyCond;
import com.snail.webgame.game.condtion.conds.TechCond;
import com.snail.webgame.game.condtion.conds.TimeCardCond;
import com.snail.webgame.game.condtion.conds.TimeCond;
import com.snail.webgame.game.condtion.conds.TotalChargeCond;
import com.snail.webgame.game.condtion.conds.TotalCoinCond;
import com.snail.webgame.game.condtion.conds.TotalCostCond;
import com.snail.webgame.game.condtion.conds.UpStarForHeroCond;
import com.snail.webgame.game.condtion.conds.VipCond;
import com.snail.webgame.game.condtion.conds.VisitCond;
import com.snail.webgame.game.condtion.conds.WeaponUpCond;
import com.snail.webgame.game.condtion.conds.WorldBossCond;
import com.snail.webgame.game.condtion.conds.WorshipCond;
import com.snail.webgame.game.condtion.conds.XlxfCond;
import com.snail.webgame.game.condtion.conds.YaBiaoCond;
import com.snail.webgame.game.condtion.conds.YxupCond;
import com.snail.webgame.game.condtion.conds.ZbupCond;
import com.snail.webgame.game.info.RoleInfo;

/**
 * 条件检测
 * @author nijp
 */
public abstract class AbstractConditionCheck implements BaseConditionCheck {

	/**
	 * 条件分割
	 */
	public static final String SPLIT_1 = ";";
	public static final String SPLIT_2 = "-";

	/**
	 * 资源类型
	 */
	private static String[] resources = { ConditionType.TYPE_MONEY.getName(), ConditionType.TYPE_COIN.getName(),
			ConditionType.TYPE_SP.getName(),ConditionType.TYPE_TECH.getName(),
			ConditionType.TYPE_EXP.getName(), ConditionType.TYPE_COURAGE.getName(),
			ConditionType.TYPE_JUSTICE.getName(),
//			ConditionType.TYPE_DEVOTE.getName(),
			ConditionType.TYPE_KUAFU_MONEY.getName(), ConditionType.TYPE_EXPLOIT.getName(),
			ConditionType.TYPE_ENERGY.getName(), ConditionType.TYPE_EQUIP.getName(),
			ConditionType.TYPE_PVP_3_MONEY.getName(),ConditionType.TYPE_CLUB_CONTRIBUTION.getName(),
			ConditionType.TYPE_STAR_MONEY.getName(),ConditionType.TYPE_TEAM_MONEY.getName()};

	/**
	 * 商店货币类型
	 */
	private static String[] currency = { ConditionType.TYPE_MONEY.getName(), ConditionType.TYPE_COURAGE.getName(),
			ConditionType.TYPE_JUSTICE.getName(), 
//			ConditionType.TYPE_DEVOTE.getName(),
			ConditionType.TYPE_KUAFU_MONEY.getName(), ConditionType.TYPE_EXPLOIT.getName(),
			ConditionType.TYPE_COIN.getName(), ConditionType.TYPE_EQUIP.getName(),ConditionType.TYPE_PVP_3_MONEY.getName()
			,ConditionType.TYPE_CLUB_CONTRIBUTION.getName(), ConditionType.TYPE_STAR_MONEY.getName(),ConditionType.TYPE_TEAM_MONEY.getName()};

	/**
	 * 检测条件集合。
	 */
	private static Map<ConditionType, BaseConditionCheck> cdCheckMap = new EnumMap<ConditionType, BaseConditionCheck>(
			ConditionType.class);

	public static void init() {
		// 初始化条件验证类。
		cdCheckMap.clear();

		cdCheckMap.put(ConditionType.TYPE_MONEY, new MoneyCond());
		cdCheckMap.put(ConditionType.TYPE_COIN, new CoinCond());
		cdCheckMap.put(ConditionType.TYPE_SP, new SpCond());
		cdCheckMap.put(ConditionType.TYPE_TECH, new TechCond());
		cdCheckMap.put(ConditionType.TYPE_COURAGE, new CourageCond());
		cdCheckMap.put(ConditionType.TYPE_JUSTICE, new JusticeCond());
//		cdCheckMap.put(ConditionType.TYPE_DEVOTE, new DevoteCond());
		cdCheckMap.put(ConditionType.TYPE_FB, new FinishFbNumCond());
		cdCheckMap.put(ConditionType.TYPE_ROLELV, new RoleLvCond());
		cdCheckMap.put(ConditionType.TYPE_TIME, new TimeCond());
		cdCheckMap.put(ConditionType.TYPE_TASK, new FinishTaskCond());
		cdCheckMap.put(ConditionType.TYPE_ARENA, new ArenaCond());
		cdCheckMap.put(ConditionType.TYPE_JNUP, new JnupCond());
		cdCheckMap.put(ConditionType.TYPE_YXUP, new YxupCond());
		cdCheckMap.put(ConditionType.TYPE_KEJI, new KejiCond());
		cdCheckMap.put(ConditionType.TYPE_ZBUP, new ZbupCond());
		cdCheckMap.put(ConditionType.TYPE_CHOUKA, new ChoukaCond());
		cdCheckMap.put(ConditionType.TYPE_QIANDAO, new QiandaoCond());
		cdCheckMap.put(ConditionType.TYPE_HERONUM, new HeroNumCond());
		cdCheckMap.put(ConditionType.TYPE_HEROQUA, new HeroQualityCond());
		cdCheckMap.put(ConditionType.TYPE_CHALLENGE, new ChallengeChapterCond());
		cdCheckMap.put(ConditionType.TYPE_KUAFU_MONEY, new KuafuMoneyCond());
		cdCheckMap.put(ConditionType.TYPE_TEAM_MONEY, new TeamMoneyCond());
		cdCheckMap.put(ConditionType.TYPE_EXPLOIT, new ExploitCond());
		cdCheckMap.put(ConditionType.TYPE_REACH_MAP_NPC, new ReachMapNPCCond());
		cdCheckMap.put(ConditionType.TYPE_ATTACK_ROLE, new AttackRoleCond());
		cdCheckMap.put(ConditionType.TYPE_ATTACK_NPC, new AttackNPCCond());
		cdCheckMap.put(ConditionType.TYPE_STAY_MAP_NPC, new StayMapNPCCond());
		cdCheckMap.put(ConditionType.TYPE_JTCL, new JtclCond());
		cdCheckMap.put(ConditionType.TYPE_LD, new LdCond());
		cdCheckMap.put(ConditionType.TYPE_SHJW, new ShjwCond());
		cdCheckMap.put(ConditionType.TYPE_GGZJ, new GgzjCond());
		cdCheckMap.put(ConditionType.TYPE_PVP, new PvpCond());
		cdCheckMap.put(ConditionType.TYPE_ROLE_QUA, new RoleQualityCond());
		cdCheckMap.put(ConditionType.TYPE_HERO_LV, new HeroLvCond());
		cdCheckMap.put(ConditionType.TYPE_COMPOSITE_HERO, new CompoundHeroCond());
		cdCheckMap.put(ConditionType.TYPE_ENERGY, new EnergyCond());
		cdCheckMap.put(ConditionType.TYPE_EQUIP, new EquipCond());
		cdCheckMap.put(ConditionType.TYPE_NZWD, new NzwdCond());
		cdCheckMap.put(ConditionType.TYPE_XLXF, new XlxfCond());
		cdCheckMap.put(ConditionType.TYPE_BLJD, new BljdCond());
		cdCheckMap.put(ConditionType.TYPE_ROLE_SKILL_LV, new RoleSkillLvCond());
		cdCheckMap.put(ConditionType.TYPE_HERO_SKILL_LV, new HeroSkillLvCond());
		cdCheckMap.put(ConditionType.TYPE_EQUIP_STRENG, new EquipStrengCond());
		cdCheckMap.put(ConditionType.TYPE_EQUIP_REFINE, new EquipRefineCond());
		cdCheckMap.put(ConditionType.TYPE_MAGIC_LV, new RoleWeaponLvCond());
		cdCheckMap.put(ConditionType.TYPE_FINISH_CHALLENGE, new FinishChallengeCond());
		cdCheckMap.put(ConditionType.TYPE_VIP_LV, new VipCond());
		cdCheckMap.put(ConditionType.TYPE_PVP_3_MONEY, new Pvp3MoneyCond());
		cdCheckMap.put(ConditionType.TYPE_CLUB_CONTRIBUTION, new ContributionCond());
		cdCheckMap.put(ConditionType.TYPE_TOTAL_CHARGE, new TotalChargeCond());
		cdCheckMap.put(ConditionType.TYPE_TOTAL_COIN, new TotalCoinCond());
		cdCheckMap.put(ConditionType.TYPE_TOTAL_COST, new TotalCostCond());
		cdCheckMap.put(ConditionType.TYPE_SHOW_TIME_CARD, new ShowTimeCardCond());
		cdCheckMap.put(ConditionType.TYPE_TIME_CARD, new TimeCardCond());
		cdCheckMap.put(ConditionType.TYPE_SHOW_FULI_CARD, new ShowFuliCardCond());
		cdCheckMap.put(ConditionType.TYPE_FULI_CARD, new FuliCardCond());
		
		cdCheckMap.put(ConditionType.TYPE_CBP, new ChangBpCond());
		cdCheckMap.put(ConditionType.TYPE_WORLD_BOSS, new WorldBossCond());
		cdCheckMap.put(ConditionType.TYPE_YA_BIAO, new YaBiaoCond());
		cdCheckMap.put(ConditionType.TYPE_REACH_CITY, new ReachCityCond());
		
		cdCheckMap.put(ConditionType.TYPE_OPACT_LOGIN, new OpActLoginCond());
		cdCheckMap.put(ConditionType.TYPE_OPACT_PAY, new OpActPayCond());
		cdCheckMap.put(ConditionType.TYPE_OPACT_CONSUME, new OpActConsumeCond());
		cdCheckMap.put(ConditionType.TYPE_OPACT_TREASURE, new OpActTreasureCond());
		cdCheckMap.put(ConditionType.TYPE_OPACT_FINISH_FB, new OpActFinishFbCond());
		cdCheckMap.put(ConditionType.TYPE_OPACT_SDPAY, new OpActSdPayCond());
		cdCheckMap.put(ConditionType.TYPE_OPACT_PAYDAYS, new OpActPayDaysCond());
		
		cdCheckMap.put(ConditionType.TYPE_VISIT, new VisitCond());
		cdCheckMap.put(ConditionType.TYPE_WEAR_HERO_EQUIP, new HeroWearEquipCond());
		cdCheckMap.put(ConditionType.TYPE_BUY_SP, new BuySpCond());
		cdCheckMap.put(ConditionType.TYPE_BUY_MONEY, new BuyMoneyCond());
		cdCheckMap.put(ConditionType.TYPE_UP_WEAPON, new WeaponUpCond());
		cdCheckMap.put(ConditionType.TYPE_MINE_NUM, new MineNumCond());
		
		cdCheckMap.put(ConditionType.TYPE_GIVE_ENERGY, new GiveEnergyCond());
		cdCheckMap.put(ConditionType.TYPE_STAR_MONEY, new StarMoneyCond());
		
		cdCheckMap.put(ConditionType.TYPE_CLUB_BUILD, new ClubBuildCond());
		
		cdCheckMap.put(ConditionType.TYPE_ACTIVE_NUM, new ActiveNumCond());
		cdCheckMap.put(ConditionType.TYPE_WORSHIP, new WorshipCond());
		cdCheckMap.put(ConditionType.TYPE_ROLE_LV_RANG, new RoleLvRangCond());
		cdCheckMap.put(ConditionType.TYPE_ALL_FINISH_FIGHT, new AllFinishFightCond());
		cdCheckMap.put(ConditionType.TYPE_UP_STAR_FOR_HERO, new UpStarForHeroCond());
		cdCheckMap.put(ConditionType.TYPE_CALL_HERO, new CallHeroCond());
		
		cdCheckMap.put(ConditionType.TYPE_OPACT_TREASURE_1, new OpActTreasure1Cond());
		cdCheckMap.put(ConditionType.TYPE_OPACT_TREASURE_2, new OpActTreasure2Cond());
		cdCheckMap.put(ConditionType.TYPE_OPACT_TREASURE_3, new OpActTreasure3Cond());
		cdCheckMap.put(ConditionType.TYPE_OPACT_TREASURE_4, new OpActTreasure4Cond());
		
		cdCheckMap.put(ConditionType.TYPE_TEAM_CHALLENGE, new TeamChallengeCond());
		cdCheckMap.put(ConditionType.TYPE_TEAM_3V3, new Team3V3FightCond());
	}
	
	/**
	 * 更新检测的中间状态。
	 * @param index
	 * @param questInProgressInfo
	 * @param value
	 */
	protected void updateValue(int index, CheckDataChg checkDataChg, int value) {
		if (checkDataChg != null && checkDataChg.getSpecValue(index) != value) {
			checkDataChg.updateInterValue(index, value);

			checkDataChg.setVersionChg(true);
		}
	}

	/**
	 * 条件解析
	 * @param condStr
	 * @return
	 */
	public static List<AbstractConditionCheck> generateConds(String xml, String condStr) {

		if (condStr == null || condStr.trim().length() <= 0 || condStr.trim().equals("0")) {
			return null;
		}

		List<AbstractConditionCheck> conditions = new ArrayList<AbstractConditionCheck>();

		String[] conds = condStr.split(AbstractConditionCheck.SPLIT_1);
		for (String cond : conds) {
			String[] condDetail = cond.split(AbstractConditionCheck.SPLIT_2);

			String name = condDetail[0];
			ConditionType ct = ConditionType.attrParseName(name);
			if (ct == null) {
				throw new RuntimeException("xml:" + xml + ",generate conditoin, typeName =" + name + ",not exist!");
			}
			BaseConditionCheck baseCond = cdCheckMap.get(ct);
			if (baseCond == null) {
				throw new RuntimeException("xml:" + xml + ",generate conditoin, typeName =" + name + ",not exist!");
			}

			AbstractConditionCheck thisBaseCond = baseCond.generate(condDetail);
			if (thisBaseCond == null) {
				throw new RuntimeException("xml:" + xml + ",generate conditoin, typeName =" + name + ",param error!");
			}
			conditions.add(thisBaseCond);
		}

		return conditions;
	}

	/**
	 * 条件检测
	 * @param roleInfo
	 * @param conds
	 * @return 1-成功 其他 ErrorCode
	 */
	public static int checkCondition(RoleInfo roleInfo, List<AbstractConditionCheck> conds) {
		if (conds == null || conds.size() <= 0) {
			return 1;
		}

		for (AbstractConditionCheck cond : conds) {
			int result = cond.checkCond(roleInfo, 0, null, 0, null);
			if (result != 1) {
				return result;
			}
		}
		return 1;
	}

	/**
	 * 检测一系列条件。 这里会遍历一系列的检测条件，判断当前检测是否通过。
	 * 需要注意的是，即使其中的一个条件没有通过，后续的检测还是需要继续判断，因为需要记录每一种检测的中间状态。
	 * @param conditions
	 * @param roleInfo
	 * @param checkDataChg
	 * @param action
	 * @param obj
	 * @return
	 */
	public static int check(Collection<AbstractConditionCheck> conditions, RoleInfo roleInfo,
			CheckDataChg checkDataChg, int action, Object obj) {
		boolean isPass = true;

		int checkRt = 0; // 最终检测结果
		int currRt;// 当前条件检测结果
		int index = 0;
		if (conditions != null) {
			for (AbstractConditionCheck cond : conditions) {
				currRt = cond.checkCond(roleInfo, action, checkDataChg, index, obj);
				if (currRt != 1) {
					isPass = false;

					if (currRt != 0) {
						checkRt = currRt;
					}
				}

				index++;
			}
		}

		if (checkDataChg != null && isPass) {
			checkDataChg.setFinishState();
		}

		if (isPass) {
			checkRt = 1;
		}

		return checkRt;
	}

	/**
	 * 判断是否是资源类型
	 * @param type
	 * @return
	 */
	public static boolean isResourceType(String type) {
		return ArrayUtils.contains(resources, type);
	}

	/**
	 * 判断是否是商店货币类型
	 * @param type
	 * @return
	 */
	public static boolean isCurrencyType(String type) {
		return ArrayUtils.contains(currency, type);
	}

	/**
	 * 统计资源类的扣除
	 * @return
	 */
	public BaseSubResource sub() {
		return null;
	}

	/**
	 * 获取资源变动
	 * @param conds
	 * @return
	 */
	public static BaseSubResource subCondition(List<AbstractConditionCheck> conds) {
		if (conds != null && conds.size() > 0) {
			BaseSubResource sub = new BaseSubResource();
			for (AbstractConditionCheck cond : conds) {
				BaseSubResource subCond = cond.sub();
				if (subCond != null) {
					sub.upMoney += subCond.upMoney;
					sub.upCoin += subCond.upCoin;
					sub.upSp += subCond.upSp;
					sub.upCourage += subCond.upCourage;
					sub.upJustice += subCond.upJustice;
					sub.upDevote += subCond.upDevote;
					sub.upKuafuMoney += subCond.upKuafuMoney;
					sub.upTeamMoney += subCond.upTeamMoney;
					sub.upExploit += subCond.upExploit;
					sub.upEnergy += subCond.upEnergy;
					sub.upEquip += subCond.upEquip;
					sub.upTech += subCond.upTech;
					sub.pvp3Money += subCond.pvp3Money;
					sub.clubContribution += subCond.clubContribution;;
					sub.starMoney += subCond.starMoney;
					sub.clubBuild += subCond.clubBuild;
				}
			}
			return sub;
		}
		return null;
	}

	public static class BaseSubResource {
		public long upMoney;// 消耗银子
		public long upCoin;// 消耗金子（充值）
		public int upSp;// 消耗行动值
		public long upCourage;// 消耗勇气点
		public long upJustice;// 消耗正义点
		public long upDevote;// 消耗工会币
		public long upKuafuMoney;// 消耗工会币
		public long upExploit;// 消耗战功
		public long upEnergy;//消耗精力
		public long upEquip; //装备商城货币
		public long upTech;// 消耗行动值
		public long pvp3Money;// 组队PVP奖励
		public long clubContribution;// 公会贡献值
		public long starMoney;// 星石币
		public long clubBuild;// 公会的建设度
		public long upTeamMoney;// 消耗斩将令
	}

}
