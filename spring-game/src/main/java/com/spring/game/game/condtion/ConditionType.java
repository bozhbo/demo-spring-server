package com.snail.webgame.game.condtion;

import java.util.HashMap;
import java.util.Map;

public enum ConditionType {

	/**
	 * 商店货币类型 1:money银子 2:gold金子 3:sp 体力 4:soul 战魂 5:food 粮草 6:tech 技能点(升级武将技能)
	 * 7:exp 玩家经验 8:courage 竞技场货币 勇气点 9:justice 征战四方货币 正义点 10:guildcurrency 工会币
	 * 11:fbNum 副本-章节编号 12:pre 前置建筑 13:taskTime 时间区间 14:task 前置完成任务 15:roleLv 玩家等级 49:体力值购买次数 50:银子购买次数 51:经验活动剩余次数
	 * 52:金币活动剩余次数 53:用户名修改次数 54:历史最高战斗力  60:vip等级
	 * 93: 【客户端已用请跳过]
	 */
	
	TYPE_MONEY(1, "money", "银子"),
	TYPE_COIN(2, "gold", "金子"),
	TYPE_SP(3, "sp", "体力"),
	TYPE_TECH(6, "tech", "技能点"),
	TYPE_EXP(7, "exp", "玩家经验"),
	TYPE_COURAGE(8, "courage", "竞技场货币 勇气令"),
	TYPE_JUSTICE(9, "justice", "攻城货币 正义令"),
//	TYPE_DEVOTE(10, "guildcurrency", "工会币"),
	TYPE_FB(11, "fbNum", "副本类型-章节编号-关卡编号-完成副本数量 （0-任意）"),
	TYPE_BUILDING(12, "pre", "前置建筑"),
	TYPE_TIME(13, "taskTime", "时间区间"),
	TYPE_TASK(14, "task", "前置完成任务"),
	TYPE_ROLELV(15, "rolelv", "玩家等级"),
	TYPE_NORMAL_SHOP_FRE(16, "normalShop", "普通商店手动刷新"),
	TYPE_ARENA(17, "arena", "完成竞技场"),
	TYPE_JNUP(18, "jnup", "技能升级"),
	TYPE_YXUP(19, "yxup", "副将升级"),
	TYPE_KEJI(20, "keji", "科技升级"),
	TYPE_ZBUP(22, "zbup", "装备强化"),
	TYPE_CHOUKA(23, "chouka", "抽卡"),
	TYPE_QIANDAO(24, "qiandao", "每日签到"),
	TYPE_HERONUM(25, "heroNum", "英雄数量达到多少"),
	TYPE_HEROQUA(27, "heroQuality", "拥有xx颜色xx个以上的英雄"),
	TYPE_KUAFU_MONEY(28, "kuafumoney", "跨服竞技场货币 征服令"),
	TYPE_KUAFU_SHOP_FRE(29, "kuafushop", "跨服商店刷新"),
	TYPE_CHALLENGE(30, "fb", "副本编号"),
	TYPE_EXPLOIT(32, "exploit", "战功"),
	TYPE_EXPLOIT_SHOP_FRE(33, "exploitshop", "战功商店刷新"),
	TYPE_HIS_EXPLOIT(34, "hisExploit", "历史战功"),
	TYPE_REACH_MAP_NPC(35, "reachMapNpc", "到达大地图某NPC"),
	TYPE_ATTACK_ROLE(36, "attackRole", "攻击玩家"),
	TYPE_ATTACK_NPC(37, "attackNPC", "消灭NPC"),
	TYPE_STAY_MAP_NPC(38, "stayMapNpc", "大地图某点驻足一段时间"),
	TYPE_GOLD_SHOP_REF(39, "goldshop", "黑市商店刷新"),
	TYPE_JTCL(40, "jtcl", "军团操练"),
	TYPE_LD(41, "ld", "掠夺粮草"),
	TYPE_SHJW(42, "shjw", "守护君王"),
	TYPE_GGZJ(43, "ggzj", "过关斩将"),
	TYPE_PVP(44, "sport", "同步竞技场"),
	TYPE_ROLE_QUA(46, "roleQuality", "主角品质"),
	TYPE_HERO_LV(47, "herolv", "拥有xx等级xx个以上的武将"),
	TYPE_COMPOSITE_HERO(48, "CompositeHero", "是否有武将可以招募"),
	TYPE_ENERGY(49,"energy","精力"),
	TYPE_EQUIP_STRENG(50, "EquipStrengthen", "几件装备强化到多少级"),
	TYPE_NZWD(51, "nzwd", "你争我夺"),
	TYPE_XLXF(52, "xlxf", "狭路相逢"),
	TYPE_BLJD(53, "bljd", "兵来将挡"),
	TYPE_ROLE_SKILL_LV(54, "roleskillLv", "主角技能等级达到多少级"),
	TYPE_HERO_SKILL_LV(55, "skillLv", "副将技能等级达到多少级"),
	TYPE_EQUIP(56,"equip","装备熔炼后的货币"),
	TYPE_EQUIP_REFINE(57, "EquipRefine", "几件装备精练到多少级"),
	TYPE_MAGIC_LV(58, "MagicLv", "几件神兵升级到多少级"),
	TYPE_FINISH_CHALLENGE(59, "finishChallenge", "完成当前战场"),
	TYPE_VIP_LV(60, "vipLv", "VIP等级"),
	TYPE_USED_COIN(61, "usedGold", "总消耗金子"),
	TYPE_PVP_3_MONEY(62, "pvp3Money", "荣誉点"),
	TYPE_PVP_3_SHOP_REF(63, "pvp3Shop", "pvp3商店手动刷新"),
	TYPE_TURK_SHOP_REF(64, "turkShop", "异域商店刷新"),
	TYPE_CLUB_CONTRIBUTION(65, "clubContribution", "公会贡献值"),
	TYPE_TOTAL_CHARGE(66, "totalCharge", "累计充值的钱"),
	TYPE_TOTAL_COIN(67, "totalCoin", "累计充值获得的金币"),
	TYPE_TOTAL_COST(68, "totalCost", "累计消费的金币"),
	TYPE_SHOW_TIME_CARD(69, "showTimeCard", "指定会员卡显示"),
	TYPE_TIME_CARD(70, "timeCard", "是否达到会员卡类型"),
	TYPE_SHOW_FULI_CARD(71, "showFuliCard", "指定福利卡显示"),
	TYPE_FULI_CARD(72, "fuliCard", "是否购买福利卡"),
	TYPE_CBP(73, "changbanpo", "完成长坂坡活动几次"),
	TYPE_WORLD_BOSS(74, "worldboss", "参与攻击几次世界boss"),
	TYPE_YA_BIAO(75, "yabiao", "完成几次押镖"),
	TYPE_REACH_CITY(76, "reachMapCity", "刺探世界地图襄平城。"),
	
	TYPE_OPACT_LOGIN(77, "login", "运营时限活动连续登录"),
	TYPE_OPACT_PAY(78, "pay", "运营时限活动累计充值"),
	TYPE_OPACT_CONSUME(79, "consume", "运营时限活动累计消费"),
	TYPE_OPACT_TREASURE(80, "treasure", "运营时限活动累计金币10连开"),
	TYPE_OPACT_FINISH_FB(81, "opFinishFb", "运营时限活动检测关卡"),
	TYPE_OPACT_SDPAY(82, "sdpay", "运营时限活动指定日期充值"),
	TYPE_OPACT_PAYDAYS(83, "paydays", "运营时限活动连续几天每日充值达多少"),
	
	TYPE_VISIT(84, "visit", "拜访任务"),
	TYPE_WEAR_HERO_EQUIP(85, "wearHeroEquip", "副将穿装备次数"),
	TYPE_BUY_SP(86, "buySp", "购买体力次数"),
	TYPE_BUY_MONEY(87, "buyMoney", "购买银子次数"),
	TYPE_UP_WEAPON(88, "upWeapon", "神兵升级"),
	TYPE_CLUB_CONTRIBUTION_SUM(89, "clubContributionSum", "累积获得的公会贡献值"),
	TYPE_MINE_NUM(90, "mineNum", "挖矿几次"),
	TYPE_GIVE_ENERGY(91, "giveEnergy", "赠送精力多少次"),
	TYPE_STAR_MONEY(92, "starMoney", "星石币"),
	TYPE_CLUB_BUILD(93, "clubBuild", "公会建设度"),
	
	TYPE_TEAM_MONEY(94, "teamMoney", "斩将令"),
	TYPE_TEAM_SHOP_FRE(95, "teamShop", "斩将令商店刷新"),
	
	TYPE_ACTIVE_NUM(96, "activeNum", "活跃度累计"),
	TYPE_WORSHIP(97, "worship", "膜拜"),
	TYPE_ROLE_LV_RANG(98, "roleLvRang", "角色等级范围"),
	TYPE_ALL_FINISH_FIGHT(99, "allFinishFight", "战斗检测中间过程及往事"),
	TYPE_UP_STAR_FOR_HERO(100, "upStarForHero", "检测某个副将升星几次"),
	TYPE_CALL_HERO(101, "callHero", "招募某个副将"),
	
	TYPE_OPACT_TREASURE_1(102, "wjtreasure1", "运营时限活动累计武将10连开"),
	TYPE_OPACT_TREASURE_2(103, "zbtreasure1", "运营时限活动累计装备10连开"),
	TYPE_OPACT_TREASURE_3(104, "wjtreasure2", "运营时限活动累计武将单开"),
	TYPE_OPACT_TREASURE_4(105, "zbtreasure2", "运营时限活动累计装备单开"),
	
	TYPE_TEAM_CHALLENGE(106, "Teamfb", "组队副本"),
	TYPE_TEAM_3V3(107, "Lzhd", "龙争虎斗");
	
	private int type;
	private String name;
	private String desc;

	private static Map<Integer, ConditionType> typeMap = new HashMap<Integer, ConditionType>();
	
	private static Map<String, ConditionType> nameMap = new HashMap<String, ConditionType>();

	static {
		ConditionType[] values = ConditionType.values();
		for (ConditionType value : values) {
			typeMap.put(value.getType(), value);
			
			nameMap.put(value.getName(), value);
		}
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	private ConditionType(int type, String name, String desc) {
		this.type = type;
		this.name = name;
		this.desc = desc;
	}

	/**
	 * 解析
	 * 
	 * @param attrTypeValue
	 * @return
	 */
	public static ConditionType attrParseType(int attrTypeValue) {
		return typeMap.get(attrTypeValue);
	}
	
	/**
	 * 解析
	 * 
	 * @param attrNameValue
	 * @return
	 */
	public static ConditionType attrParseName(String attrNameValue) {
		return nameMap.get(attrNameValue);
	}
}
