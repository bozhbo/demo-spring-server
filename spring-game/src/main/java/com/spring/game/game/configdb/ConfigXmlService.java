package com.snail.webgame.game.configdb;

import java.io.File;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.engine.common.util.XMLUtil4DOM;
import com.snail.webgame.game.common.xml.info.ArmsDifferenceXmlLoader;
import com.snail.webgame.game.common.xml.info.NPCXmlLoader;
import com.snail.webgame.game.common.xml.info.PowerXmlLoader;
import com.snail.webgame.game.common.xml.info.SkillBuffInfoLoader;
import com.snail.webgame.game.common.xml.info.SkillInfoLoader;
import com.snail.webgame.game.common.xml.load.LoadHeroXML;
import com.snail.webgame.game.common.xml.load.LoadSoldierXML;
import com.snail.webgame.game.common.xml.load.LoadStageXML;
import com.snail.webgame.game.common.xml.load.LoadVipXML;
import com.snail.webgame.game.dao.ConfigXmlDAO;
import com.snail.webgame.game.info.xml.ConfigXmlInfo;
import com.snail.webgame.game.thread.CheckConfigThread;
import com.snail.webgame.game.xml.cache.RecruitDepotXMLInfoMap;
import com.snail.webgame.game.xml.cache.RecruitKindXMLInfoMap;
import com.snail.webgame.game.xml.info.RecruitDepotXMLInfo;
import com.snail.webgame.game.xml.info.RecruitKindXMLInfo;
import com.snail.webgame.game.xml.load.LoadArenaXML;
import com.snail.webgame.game.xml.load.LoadAttackAnotherXML;
import com.snail.webgame.game.xml.load.LoadBaseXML;
import com.snail.webgame.game.xml.load.LoadCampaignXML;
import com.snail.webgame.game.xml.load.LoadChallengeBattleXML;
import com.snail.webgame.game.xml.load.LoadChallengeResetXML;
import com.snail.webgame.game.xml.load.LoadCheckInXML;
import com.snail.webgame.game.xml.load.LoadChenghaoXML;
import com.snail.webgame.game.xml.load.LoadChipComposeXML;
import com.snail.webgame.game.xml.load.LoadDefendXML;
import com.snail.webgame.game.xml.load.LoadEquipXML;
import com.snail.webgame.game.xml.load.LoadExpActivityXML;
import com.snail.webgame.game.xml.load.LoadFixLotteryXML;
import com.snail.webgame.game.xml.load.LoadFuncOpenXML;
import com.snail.webgame.game.xml.load.LoadGMCmdXML;
import com.snail.webgame.game.xml.load.LoadGWXML;
import com.snail.webgame.game.xml.load.LoadGoldBuyXML;
import com.snail.webgame.game.xml.load.LoadGuildConstructionXml;
import com.snail.webgame.game.xml.load.LoadGuildShopXml;
import com.snail.webgame.game.xml.load.LoadGuildTechXML;
import com.snail.webgame.game.xml.load.LoadGuildUpgradeXml;
import com.snail.webgame.game.xml.load.LoadHeroRelationShipXML;
import com.snail.webgame.game.xml.load.LoadInstanceStarXML;
import com.snail.webgame.game.xml.load.LoadLevelGiftXML;
import com.snail.webgame.game.xml.load.LoadMineXML;
import com.snail.webgame.game.xml.load.LoadMoneyActivityXML;
import com.snail.webgame.game.xml.load.LoadOnlineGiftXMLInfo;
import com.snail.webgame.game.xml.load.LoadPayXMLInfo;
import com.snail.webgame.game.xml.load.LoadPlayXML;
import com.snail.webgame.game.xml.load.LoadPropBagXML;
import com.snail.webgame.game.xml.load.LoadPropXML;
import com.snail.webgame.game.xml.load.LoadPushXML;
import com.snail.webgame.game.xml.load.LoadQuestProtoXml;
import com.snail.webgame.game.xml.load.LoadRandomNameXML;
import com.snail.webgame.game.xml.load.LoadRecruitDepotXML;
import com.snail.webgame.game.xml.load.LoadRecruitKindXML;
import com.snail.webgame.game.xml.load.LoadRideXML;
import com.snail.webgame.game.xml.load.LoadSceneCityNPCXml;
import com.snail.webgame.game.xml.load.LoadSevenDayXMLInfo;
import com.snail.webgame.game.xml.load.LoadShopXML;
import com.snail.webgame.game.xml.load.LoadSnatchXML;
import com.snail.webgame.game.xml.load.LoadStageMoneyXML;
import com.snail.webgame.game.xml.load.LoadStagePrizeXML;
import com.snail.webgame.game.xml.load.LoadTeam3V3XML;
import com.snail.webgame.game.xml.load.LoadTeamDuiGongXML;
import com.snail.webgame.game.xml.load.LoadToolItemXML;
import com.snail.webgame.game.xml.load.LoadVipItemBuyXMLInfo;
import com.snail.webgame.game.xml.load.LoadWeaponXML;
import com.snail.webgame.game.xml.load.LoadWonderXMLInfo;
import com.snail.webgame.game.xml.load.LoadWorldBossPrizeXML;
import com.snail.webgame.game.xml.load.LoadWorldBossXML;
import com.snail.webgame.game.xml.load.LoadYabiaoPrizeXML;
import com.snail.webgame.game.xml.load.TeamChallengeXmlLoader;


public class ConfigXmlService {

	private static ConfigXmlDAO configXmlDAO = ConfigXmlDAO.getInstance();

	private static final Logger logger = LoggerFactory.getLogger("logs");

	private static final String XML_ROOT_TAG_START = "<XmlRoot>";
	private static final String XML_ROOT_TAG_END = "</XmlRoot>";

	private static final String CONFIG_PARENT_CATALOG = "/config/xml/";

	// 从数据库读取配置数据
	public static final boolean LOAD_GAME_CONIFG_FROMDB = true;

	// 需要装载的xml文件列表
	private static String str[] = ConfigXmlFileList.str;
	private static String localStr[] = ConfigXmlFileList.localXmlStr;

	/**
	 * 初始化装载所有XML配置文件-游戏服务器启动时调用
	 * @return
	 * @throws Exception
	 */
	public static boolean initAllConfigXMLData() throws Exception {
		
		//读取GAME_CONFIG里的xml文件
		for (int i = 0; i < str.length; i++) {
			String xmlName = str[i].replace(".xml", "");
			if (!LOAD_GAME_CONIFG_FROMDB) {
				if (loadGameConfigFromLocal(xmlName,false)) {
					if (logger.isInfoEnabled()) {
						logger.info("Load local [" + str[i] + "] loads successful!");
					}
				} else {
					// 装载数据库中XML失败
					if (logger.isErrorEnabled()) {
						logger.error("Load local [" + str[i] + "] loads failure!");
					}
					return false;
				}
			} else {
				if (initOneConfigXMLData(xmlName,false)) {
					if (logger.isInfoEnabled()) {
						logger.info("Load DAO [" + str[i] + "] loads successful!");
					}
				} else {
					// 装载数据库中XML失败
					if (logger.isErrorEnabled()) {
						logger.error("Load DAO [" + str[i] + "] loads failure!");
					}
					return false;
				}
			}
		}
		
		// 直接读取包里面的xml文件
		for (int i = 0; i < localStr.length; i++) {
			String xmlName = localStr[i].replace(".xml", "");
			
			String filePath =System.getProperty("user.dir") + File.separator + "config" + File.separator +"xml" + File.separator + xmlName + ".xml";
			File file = new File(filePath);
			long modifyTime = file.lastModified();
			
			if (loadGameConfigFromLocal(xmlName,false)) {
				if (logger.isInfoEnabled()) {
					logger.info("Load local [" + localStr[i] + "] loads successful!,modifyTime="+new Timestamp(modifyTime));
				}
				
				CheckConfigThread.getModifyMap().put(xmlName, modifyTime);
			} else {
				// 装载包中的XML失败
				if (logger.isErrorEnabled()) {
					logger.error("Load local [" + localStr[i] + "] loads failure!");
				}
				return false;
			}
		}
		
		return true;
	}

	/**
	 * 从项目中导入配置文件
	 * @param fileName
	 * @return
	 * @throws Exception 
	 */
	public static boolean loadGameConfigFromLocal(String fileName,boolean modify) throws Exception {
		String filePath = CONFIG_PARENT_CATALOG + fileName + ".xml";
		InputStream is = ConfigXmlService.class.getResourceAsStream(filePath);
		if (is == null)
			return false;
		Document doc = XMLUtil4DOM.file2Dom(is);
		if (doc == null) {
			return false;
		}

		return loadConfigFromDb(fileName, doc.asXML(),modify);
	}

	/**
	 * 从数据库装载一个指定XML文件
	 * @param xmlName xml文件名
	 * @return
	 * @throws Exception
	 */
	public static boolean initOneConfigXMLData(String xmlName,boolean modify) throws Exception {
		boolean flag = false;
		// 根据配置文件名称从数据库中查找对应配置文件记录
		List<ConfigXmlInfo> xmlInfoList = configXmlDAO.getXmlByName(xmlName);
		if (xmlInfoList != null && xmlInfoList.size() > 0) {
			ConfigXmlInfo xmlInfo = null;
			StringBuilder xmlBuilder = new StringBuilder(XML_ROOT_TAG_START);
			for (int i = 0; i < xmlInfoList.size(); i++) {
				xmlInfo = xmlInfoList.get(i);
				// String idStr = xmlInfo.getXmlLogId();
				String xmlStr = xmlInfo.getXmlContent();
				String xmlModStr = xmlInfo.getModXmlContent();
				int delFlag = xmlInfo.getDelFlag();
				if (delFlag == 1) {
					// 已经删除的，则跳过
				} else {
					if (StringUtils.isNotBlank(xmlModStr)) {
						// 修改过XML
						xmlBuilder.append(xmlModStr);
					} else {
						xmlBuilder.append(xmlStr);
					}
				}
			}
			xmlBuilder.append(XML_ROOT_TAG_END);
			flag = loadConfigFromDb(xmlName, xmlBuilder.toString(),modify);
		}

		return flag;

	}

	/**
	 * 缓存值即时修改 单条修改
	 * @param isNeedRefresh 是否需要通知客户端刷新
	 * @param idStr
	 * @param xmlStr
	 * @return
	 * @throws Exception 
	 */
	public static boolean modifyConfigCache(boolean isNeedRefresh, String idStr, String xmlStr,boolean modify) throws Exception {

		String s[] = idStr.split("_");
		String xmlName = null;
		if (s.length == 2) {
			xmlName = s[0];
		} else if (s.length == 3) {
			xmlName = s[0] + "_" + s[1];
		} else {
			if (logger.isErrorEnabled()) {
				logger.error("XML filename is wrong. filename:" + idStr);
				return false;
			}
		}
		Document doc = XMLUtil4DOM.string2Document(xmlStr);

		loadXmlElement(xmlName, doc.getRootElement(),modify);

		return true;
	}

	/**
	 * 从数据库加载整个XML
	 * @param xmlName
	 * @param xmlStr
	 * @return
	 * @throws Exception 
	 */
	public static boolean loadConfigFromDb(String xmlName, String xmlStr,boolean modify) throws Exception {

		Document doc = XMLUtil4DOM.string2Document(xmlStr);
		if (doc != null) {
			Element root = doc.getRootElement();
			List<?> childrenElm = root.elements();
			if (childrenElm != null && childrenElm.size() > 0) {
				for (int i = 0; i < childrenElm.size(); i++) {
					Element element = (Element) childrenElm.get(i);
					loadXmlElement(xmlName, element,modify);
				}
			}
		}
		return true;
	}

	/**
	 * 加载入缓存Map
	 * @param xmlName
	 * @param element
	 * @throws Exception 
	 */
	private static void loadXmlElement(String xmlName, Element element,boolean modify) throws Exception {
		if ("Base".equals(xmlName)) {
			LoadBaseXML.load(element,modify);
		} else if ("RandomName".equals(xmlName)) {
			LoadRandomNameXML.load(element);
		} else if ("GMCmd".equals(xmlName)) {
			LoadGMCmdXML.loadGmCmdXml(element,modify);
		} else if ("Hero".equals(xmlName)) {
			LoadHeroXML.loadHero(element,modify);
		} else if ("HeroUp".equals(xmlName)) {
			LoadHeroXML.loadHeroUp(element,modify);
		} else if ("HeroUpCost".equals(xmlName)) {
			LoadHeroXML.loadHeroColorUpCost(element,modify);
		} else if ("HeroUpColour".equals(xmlName)) {
			LoadHeroXML.loadHeroColour(element,modify);
		} else if ("HeroStar".equals(xmlName)) {
			LoadHeroXML.loadHeroStar(element,modify);
		} else if ("HeroSkill".equals(xmlName)) {
			LoadHeroXML.loadHeroUpSkill(element,modify);
		} else if ("SkillUp".equals(xmlName)) {
			LoadHeroXML.loadSkillUp(element,modify);
		} else if ("HeroClose".equals(xmlName)) {
			LoadHeroXML.loadHeroClose(element,modify);
		} else if ("HeroCloseCost".equals(xmlName)) {
			LoadHeroXML.loadHeroCloseCost(element,modify);
		} else if ("SceneCityNPC".equals(xmlName)) {
			LoadSceneCityNPCXml.loadSceneCityNPCXml(element,modify);
		} else if ("Equip".equals(xmlName)) {
			LoadEquipXML.loadEquip(element,modify);
		} else if ("EquipStrengthen".equals(xmlName)) {
			LoadEquipXML.loadEquipUp(element,modify);
		} else if ("Prop".equals(xmlName)) {
			LoadPropXML.load(element,modify);
		} else if ("PropBag".equals(xmlName)) {
			LoadPropBagXML.loadPropBagXMLInfo(element,modify);
		} else if ("PropAttribute".equals(xmlName)) {
			LoadPropXML.loadPropAttr(element,modify);
		} else if ("GemCompose".equals(xmlName)) {
			LoadPropXML.loadStoneComp(element,modify);
		} else if ("Shop".equals(xmlName)) {
			LoadShopXML.loadShop(xmlName, element,modify);
		} else if ("ShopBuy".equals(xmlName)) {
			LoadShopXML.loadShopBuy(xmlName, element,modify);
		} else if ("Challenge".equals(xmlName)) {
			LoadChallengeBattleXML.loadChallengeBattle(xmlName, element,modify);
		} else if ("ChallengeReset".equals(xmlName)) {
			LoadChallengeResetXML.loadXml(element,modify);
		} else if ("ArenaPrize".equals(xmlName)) {
			LoadArenaXML.loadPrize(element,modify);
		} else if ("ArenaHisPrize".equals(xmlName)) {
			LoadArenaXML.loadHisPrize(element,modify);
		} else if ("ArenaBuy".equals(xmlName)) {
			LoadArenaXML.loadBuy(xmlName, element,modify);
		} else if ("GoldBuy".equals(xmlName)) {
			LoadGoldBuyXML.loadXml(xmlName, element,modify);
		} else if ("GoldBuyRand".equals(xmlName)) {
			LoadGoldBuyXML.loadRandXml(element,modify);
		} else if ("ChestKind".equals(xmlName)) {
			RecruitKindXMLInfo xmlInfo = LoadRecruitKindXML.load(xmlName, element);
			if (xmlInfo != null) {
				RecruitKindXMLInfoMap.addRecruitKindXMLInfo(xmlInfo,modify);
			}
		} else if ("RecruitDepot".equals(xmlName)) {
			RecruitDepotXMLInfo xmlInfo = LoadRecruitDepotXML.load(xmlName, element);
			if (xmlInfo != null) {
				RecruitDepotXMLInfoMap.addRecruitDepotXMLInfo(xmlInfo,modify);
			}
		} else if ("Task".equals(xmlName)) {
			LoadQuestProtoXml.load(xmlName, element,modify);
		} else if ("CheckInPrize".equals(xmlName)) {
			LoadCheckInXML.loadCheckInPrize(element,modify);
		} else if ("LoginActive".equals(xmlName)) {
			LoadCheckInXML.loadLoginActive(element,modify);
		} else if ("KuafuLevel".equals(xmlName)) {
			LoadStageXML.load(xmlName, element,modify);
		} else if ("KuafuMoney".equals(xmlName)) {
			LoadStageMoneyXML.load(xmlName, element,modify);
		} else if ("KuafuPrize".equals(xmlName)){
			LoadStagePrizeXML.load(element,modify);
		} else if ("MapCity".equals(xmlName)) {
			LoadSceneCityNPCXml.loadMapCityXml(element,modify);
		} else if ("Play".equals(xmlName)) {
			LoadPlayXML.loadPlay(element,modify);
		} else if ("Campaign".equals(xmlName)) {
			LoadCampaignXML.loadCampaign(element,modify);
		} else if ("MapCityNPC".equals(xmlName)) {
			LoadSceneCityNPCXml.loadMapCityNPCXml(element,modify);
		} else if ("InstanceStar".equals(xmlName)) {
			LoadInstanceStarXML.loadInstanceStarInfo(element,modify);
		} else if ("Exp".equals(xmlName)) {
			LoadExpActivityXML.load(element,modify);
		} else if ("Money".equals(xmlName)) {
			LoadMoneyActivityXML.load(element,modify);
		} else if ("ToolItem".equals(xmlName)) {
			LoadToolItemXML.load(element,modify);
		} else if ("FunctionOpen".equals(xmlName)) {
			LoadFuncOpenXML.load(xmlName, element,modify);
		} else if ("Magic".equals(xmlName)) {
			LoadWeaponXML.load(element,modify);
		} else if ("Soldier".equals(xmlName)) {
			LoadSoldierXML.loadXml(element,modify);
		} else if ("EquipEffect".equals(xmlName)) {
			LoadEquipXML.loadEquipEffect(element,modify);
		} else if ("EquipSuit".equals(xmlName)) {
			LoadEquipXML.loadEquipSuit(element,modify);
		} else if ("MagicSuit".equals(xmlName)) {
			LoadWeaponXML.loadMagicSuit(element,modify);
		} else if ("MagicExp".equals(xmlName)) {
			LoadWeaponXML.loadMagicExp(element,modify);
		} else if ("Snatch".equals(xmlName)) {
			LoadSnatchXML.load(element,modify);
		} else if ("ChipCompose".equals(xmlName)) {
			LoadChipComposeXML.load(element,modify);
		} else if ("LevelGift".equals(xmlName)) {
			LoadLevelGiftXML.load(element,modify);
		} else if ("RelationShip".equals(xmlName)) {
			LoadHeroRelationShipXML.load(element,modify);
		} else if ("GW".equals(xmlName)) {
			LoadGWXML.loadGW(element,modify);
		} else if ("Heroprop".equals(xmlName)) {
			LoadRecruitKindXML.loadRelatedHeropropXml(element,modify);
		} else if ("Defend".equals(xmlName)) {
			LoadDefendXML.load(element,modify);
		} else if ("Duigong".equals(xmlName)) {
			LoadAttackAnotherXML.load(element,modify);
		} else if("FixLottery".equals(xmlName)){
			LoadFixLotteryXML.load(element,modify);
		} else if ("Buff".equals(xmlName)) {
			SkillBuffInfoLoader.loadSkillBuff(element,modify);
		} else if ("CSVSkill".equals(xmlName)) {
			SkillInfoLoader.loadSkill(element,modify);
		} else if("Broadcast".equals(xmlName)){
			LoadRecruitDepotXML.loadBroadcast(element,modify);
		}else if("YaBiaoPrize".equals(xmlName)){
			LoadYabiaoPrizeXML.load(element,modify);
		} else if("WorldBoss".equals(xmlName)){
			LoadWorldBossXML.loadXml(element,modify);
		} else if("WorldBossPrize".equals(xmlName)){
			LoadWorldBossPrizeXML.loadXml(element,modify);
		} else if("TeamDuigong".equals(xmlName)){
			LoadTeamDuiGongXML.load(xmlName, element,modify);
		} else if("VIP".equals(xmlName)){
			LoadVipXML.load(element,modify);
		} else if("Wonder".equals(xmlName)){
			LoadWonderXMLInfo.load(element,modify);
		} else if("SevenDays".equals(xmlName)){
			LoadSevenDayXMLInfo.load(element,modify);
		} else if("Payment".equals(xmlName)){
			LoadPayXMLInfo.load(element,modify);
		} else if("TimeGift".equals(xmlName)){
			LoadOnlineGiftXMLInfo.load(element,modify);
		} else if("ItemBuyRule".equals(xmlName)){
			LoadVipItemBuyXMLInfo.load(element,modify);
		} else if ("ArmsDifference".endsWith(xmlName)) {
			ArmsDifferenceXmlLoader.loadFromEle(element,modify);
		} else if("Power".equals(xmlName)){
			PowerXmlLoader.loadFromEle(element,modify);
		} else if("NPC".equals(xmlName)){
			NPCXmlLoader.loadFromEle(element,modify);
		} else if("GuildShop".equals(xmlName)){
			LoadGuildShopXml.load(element,modify);
		} else if("GuildUpgrade".equals(xmlName)){
			LoadGuildUpgradeXml.load(element,modify);
		} else if("GuildConstruction".equals(xmlName)){
			LoadGuildConstructionXml.load(element,modify);
		} else if("Mine".equals(xmlName)){
			LoadMineXML.load(element,modify);
		} else if("Notify".equals(xmlName)){
			LoadPushXML.load(element,modify);
		} else if("GuildTech".equals(xmlName)){
			LoadGuildTechXML.load(element,modify);
		} else if("Enchant".equals(xmlName)){
			LoadEquipXML.loadEnchant(element,modify);
		} else if("Chenghao".equals(xmlName)){
			LoadChenghaoXML.load(element,modify);
		} else if("TeamChallenge".equals(xmlName)){
			TeamChallengeXmlLoader.loadFromEle(element,modify);
		} else if("RideUp".equals(xmlName)){
			LoadRideXML.loadRide(element,modify);
		} else if("RideColor".equals(xmlName)){
			LoadRideXML.loadRideQua(element,modify);
		} else if("RideColorCost".equals(xmlName)){
			LoadRideXML.loadRideQuaUpCost(element,modify);
		} else if("RideUpCost".equals(xmlName)){
			LoadRideXML.loadRideLvUpCost(element,modify);
		} else if("TeamSports".equals(xmlName)){
			LoadTeam3V3XML.load(element,modify);
		}
	}

}
