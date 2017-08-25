package com.snail.webgame.game.xml.cache;

import java.util.HashMap;
import java.util.Map;

import com.snail.webgame.game.xml.info.SceneXMLInfo;
import com.snail.webgame.game.xml.info.SceneXMLInfo.MapCityXML;
import com.snail.webgame.game.xml.info.SceneXMLInfo.MapCityXMLNPC;


/**
 * 场景信息缓存
 * @author hongfm
 *
 */
public class SceneXmlInfoMap {

	private static Map<Integer, SceneXMLInfo> SceneCityxmlMap = new HashMap<Integer, SceneXMLInfo>();
	private static Map<Integer, MapCityXML> mapCityMap = new HashMap<Integer, MapCityXML>();
	private static Map<Integer, MapCityXMLNPC> mapCityNPCMap = new HashMap<Integer, MapCityXMLNPC>();

	public static void addXml(SceneXMLInfo info,boolean modify) {
		if (SceneCityxmlMap.containsKey(info.getNo()) && !modify) {
			throw new RuntimeException("Load SceneCityNPC.xml error! no: " + info.getNo() + " repeat");
		}
		SceneCityxmlMap.put(info.getNo(), info);
	}

	
	
	public static SceneXMLInfo getSceneXml(int no) {
		return SceneCityxmlMap.get(no);
	}
	
	public static void addMapCityXml(MapCityXML xmlInfo)
	{
		mapCityMap.put(xmlInfo.getNo(), xmlInfo);
	}
	
	
	public static MapCityXML getMapCityXml(int no)
	{
		return mapCityMap.get(no);
	}
	
	public static int getSceneNo(int race)
	{
		for(int no: mapCityMap.keySet())
		{
			MapCityXML mapCityXML = mapCityMap.get(no);
			if(mapCityXML != null && mapCityXML.getRace() == race)
			{
				return mapCityXML.getNo();
			}
		}
		
		return 0;
	}
	
	public static void addMapCityNpc(MapCityXMLNPC xmlInfo)
	{
		mapCityNPCMap.put(xmlInfo.getNo(), xmlInfo);
	}
	
	public static MapCityXMLNPC getMapCityXMLNPC(int no)
	{
		return mapCityNPCMap.get(no);
	}
	
}
