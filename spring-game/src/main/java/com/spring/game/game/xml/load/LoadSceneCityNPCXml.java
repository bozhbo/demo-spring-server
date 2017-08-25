package com.snail.webgame.game.xml.load;

import java.util.HashMap;
import java.util.List;

import org.dom4j.Element;

import com.snail.webgame.game.protocal.scene.cache.MapRoleInfoMap;
import com.snail.webgame.game.xml.cache.SceneXmlInfoMap;
import com.snail.webgame.game.xml.info.RecruitItemXMLInfo;
import com.snail.webgame.game.xml.info.SceneXMLInfo;
import com.snail.webgame.game.xml.info.SceneXMLInfo.MapCityXML;
import com.snail.webgame.game.xml.info.SceneXMLInfo.MapCityXMLNPC;
import com.snail.webgame.game.xml.info.SceneXMLInfo.SceneNPCXML;

/**
 * 加载场景
 * @author hongfm
 *
 */
public class LoadSceneCityNPCXml {

	public static void loadSceneCityNPCXml(Element propElement,boolean modify) {

		int no = Integer.valueOf(propElement.attributeValue("No").trim());
		float bornPointX = Float.valueOf(propElement.attributeValue("BornPointX").trim());
		float bornPointY = Float.valueOf(propElement.attributeValue("BornPointY").trim());
		float bornPointZ = Float.valueOf(propElement.attributeValue("BornPointZ").trim());

		float disaperPointX = Float.valueOf(propElement.attributeValue("DisapperX").trim());
		float disaperPointY = Float.valueOf(propElement.attributeValue("DisapperY").trim());
		float disaperPointZ = Float.valueOf(propElement.attributeValue("DisapperZ").trim());

		float jinchengPointX = Float.valueOf(propElement.attributeValue("JinchengX").trim());
		float jinchengPointY = Float.valueOf(propElement.attributeValue("JinchengY").trim());
		float jinchengPointZ = Float.valueOf(propElement.attributeValue("JinchengZ").trim());

		SceneXMLInfo info = new SceneXMLInfo();
		info.setNo(no);
		info.setBornPointX(bornPointX);
		info.setBornPointY(bornPointY);
		info.setBornPointZ(bornPointZ);
		info.setDisaperPointX(disaperPointX);
		info.setDisaperPointY(disaperPointY);
		info.setDisaperPointZ(disaperPointZ);
		info.setJinchengPointX(jinchengPointX);
		info.setJinchengPointY(jinchengPointY);
		info.setJinchengPointZ(jinchengPointZ);

		List<?> npcElements = propElement.elements("NPC");
		if (npcElements != null && npcElements.size() > 0) {
			for (int i = 0; i < npcElements.size(); i++) {
				Element item = (Element) npcElements.get(i);

				SceneNPCXML sceneNPCXML = new SceneXMLInfo().new SceneNPCXML();

				sceneNPCXML.setNpcNo(Integer.valueOf(item.attributeValue("No").trim()));
				sceneNPCXML.setPointX(Float.valueOf(item.attributeValue("PositionX").trim()));
				sceneNPCXML.setPointY(Float.valueOf(item.attributeValue("PositionY").trim()));
				sceneNPCXML.setPointZ(Float.valueOf(item.attributeValue("PositionZ").trim()));
				sceneNPCXML.setNpcFace(Float.valueOf(item.attributeValue("NPCFace").trim()));

				HashMap<Integer, SceneNPCXML> sceneNPCMap = info.getSceneNPCMap();
				if (sceneNPCMap == null) {
					HashMap<Integer, SceneNPCXML> sceneNPCMap1 = new HashMap<Integer, SceneNPCXML>();
					info.setSceneNPCMap(sceneNPCMap1);
				}

				info.getSceneNPCMap().put(sceneNPCXML.getNpcNo(), sceneNPCXML);
			}
		}

		SceneXmlInfoMap.addXml(info,modify);
	}

	public static void loadMapCityXml(Element propElement,boolean modify) {

		int no = Integer.valueOf(propElement.attributeValue("No").trim());
		float pointX = Float.valueOf(propElement.attributeValue("PositionX").trim());
		float pointZ = Float.valueOf(propElement.attributeValue("PositionZ").trim());
		int race = Integer.valueOf(propElement.attributeValue("Race").trim());

		MapCityXML info = new SceneXMLInfo().new MapCityXML();
		info.setNo(no);
		info.setPointX(pointX);
		info.setPointZ(pointZ);
		info.setRace(race);

		SceneXmlInfoMap.addMapCityXml(info);
	}

	public static void loadMapCityNPCXml(Element propElement,boolean modify) {
		MapCityXMLNPC info = new SceneXMLInfo().new MapCityXMLNPC();
		info.setNo(Integer.valueOf(propElement.attributeValue("No").trim()));
		info.setType(Integer.valueOf(propElement.attributeValue("Type").trim()));
		info.setBattleType(Integer.valueOf(propElement.attributeValue("BattleType").trim()));
		info.setTimeDown(Integer.valueOf(propElement.attributeValue("TimeDown").trim()));
		info.setGwName(propElement.attributeValue("GwName").trim());
		String usedEng = propElement.attributeValue("UsedEnger").trim();
		if (usedEng != null && !"".equals(usedEng)) {
			info.setCostEng(Integer.valueOf(usedEng));
		}
		String Prize = propElement.attributeValue("Prize").trim();
		if (Prize != null && !"".endsWith(Prize)) {
			info.setPrize(Prize);
		}
		info.setLevel(Integer.valueOf(propElement.attributeValue("Level").trim()));
		String gw = propElement.attributeValue("GW").trim();
		RecruitItemXMLInfo item = null;
		if (gw != null && gw.length() > 0) {
			String gw1[] = gw.split(";");
			if (gw1 != null && gw1.length > 0) {
				for (String items : gw1) {
					String gw2[] = items.split(",");
					item = new RecruitItemXMLInfo();
					item.setItemNo(gw2[0]);
					item.setNum(1);
					item.setRand(Integer.valueOf(gw2[1]));
					info.getGwItems().add(item);
				}
			}
		}

		SceneXmlInfoMap.addMapCityNpc(info);
		if (info.getType() == 2) {
			MapRoleInfoMap.addMapNpc(info);
		}
	}
}
