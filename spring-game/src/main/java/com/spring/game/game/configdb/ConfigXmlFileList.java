package com.snail.webgame.game.configdb;

/**
 * 如下文件导入数据中时用文件名和No值作为关键字,例如:Army_14001 所以该组文件必须符合2条规则：
 * 1-文件名中最多只能包含一个下划线"_"符号,最好不要包含下划线. 2-文件中的跟节点No值不允许包含下划线"_"符号.
 * @author leiqiang
 * @date Oct 19, 2010
 */
public class ConfigXmlFileList {
	
	// 从GAME_CONFIG里读取的xml
	static final String str[] = new String[] { 
		    "Base.xml",//
			"Hero.xml", //
			"Equip.xml",//
			"Prop.xml",// 道具
			"Challenge.xml",
			"Buff.xml",
			"CSVSkill.xml",
			"ToolItem.xml",
			"Chenghao.xml"
	};
	
	//直接读取包里config/xml目录的xml
	public static final String localXmlStr[] = new String[] {
	    //"Base.xml",
		"RandomName.xml",
		"GMCmd.xml",
		//"Hero.xml", 
		"HeroUp.xml",
		"HeroUpCost.xml",
		"HeroUpColour.xml",
		"HeroStar.xml",
		"HeroSkill.xml",
		"SkillUp.xml",
		"HeroClose.xml",
		"HeroCloseCost.xml",
		"SceneCityNPC.xml",
		//"Equip.xml",
		//"Prop.xml",
		"PropBag.xml",
		"EquipStrengthen.xml",
		"PropAttribute.xml",
		"GemCompose.xml", 
		"Shop.xml",
		"ShopBuy.xml",
		//"Challenge.xml",
		"GoldBuy.xml",
		"GoldBuyRand.xml",
		"RecruitDepot.xml",
		"ChestKind.xml",
		"PropAttribute.xml",
		"LoginActive.xml",//7日登陆签到
		"CheckInPrize.xml",//月签到
		"ArmsDifference.xml",
		"Power.xml",
		"NPC.xml",
		"ArenaPrize.xml",
		"ArenaHisPrize.xml",
		"ArenaBuy.xml",
		"KuafuLevel.xml",
		"MapCity.xml",
		"Task.xml",
		"Play.xml",// 活动玩法
		"Campaign.xml",// 守卫君主（远征）活动玩法
		"MapCityNPC.xml",//
		"Exp.xml",
		"Money.xml",
		"InstanceStar.xml",
		//"ToolItem.xml",
		"FunctionOpen.xml",
		"ChallengeReset.xml",
		"Magic.xml",
		"MagicSuit.xml",
		"MagicExp.xml",
		"Soldier.xml",
		"EquipSuit.xml",
		"EquipEffect.xml",
		"Defend.xml",
		"Duigong.xml",
		"Snatch.xml",
		"GW.xml",
		"RelationShip.xml",
		"LevelGift.xml",
		"ChipCompose.xml",
		"Heroprop.xml",
		"FixLottery.xml",
		"Broadcast.xml",
		"WorldBoss.xml",
		"WorldBossPrize.xml",
		"TeamDuigong.xml",
		"YaBiaoPrize.xml",
		"TeamDuigong.xml",
		"VIP.xml",
		"Wonder.xml",
		"SevenDays.xml",
		"Payment.xml",
		"TimeGift.xml",
		"ItemBuyRule.xml",
		//"Buff.xml",
		//"CSVSkill.xml",
		"KuafuPrize.xml",
		"Mine.xml",
		"GuildUpgrade.xml",
		"GuildShop.xml",
		"GuildConstruction.xml",
		"Notify.xml",
		"GuildTech.xml",
		"Enchant.xml",
//		"Chenghao.xml",
		"TeamChallenge.xml",
		"RideUp.xml",
		"RideColor.xml",
		"RideColorCost.xml",
		"RideUpCost.xml",
		"TeamSports.xml"
};
}
