package com.snail.webgame.game.xml.load;

import org.dom4j.Element;

import com.snail.webgame.game.common.GameValue;
import com.snail.webgame.game.common.HeroProType;
import com.snail.webgame.game.xml.cache.PropAttriAddXmlMap;
import com.snail.webgame.game.xml.cache.PropXMLInfoMap;
import com.snail.webgame.game.xml.cache.StoneCompXMLMap;
import com.snail.webgame.game.xml.info.PropAttriAddXml;
import com.snail.webgame.game.xml.info.PropXMLInfo;
import com.snail.webgame.game.xml.info.StoneComeXML;

public class LoadPropXML {

	public static void load(Element rootEle,boolean modify) {
		if (rootEle != null) {

			String itemNo = rootEle.attributeValue("No");
			if (!itemNo.startsWith(GameValue.PROP_N0)) {
				throw new RuntimeException("Load Prop.xml error! No not startsWith" + GameValue.PROP_N0);
			}
			
			int no = Integer.valueOf(itemNo);
			String name = rootEle.attributeValue("Name");
			int type = Integer.valueOf(rootEle.attributeValue("Type").trim());
			int subType = Integer.valueOf(rootEle.attributeValue("SubType").trim());
			int sell = Integer.valueOf(rootEle.attributeValue("Sell").trim());
			int colour = Integer.valueOf(rootEle.attributeValue("Colour").trim());
			
			
			String useParam = rootEle.attributeValue("UseParam").trim();

			int level = 0;
			String levelStr = rootEle.attributeValue("UseLv");
			if (levelStr != null && levelStr.length() > 0) {
				level = Integer.valueOf(levelStr.trim());
			}
			
			String key = rootEle.attributeValue("UseCondition");
			

			PropXMLInfo info = new PropXMLInfo();
			info.setNo(no);
			info.setName(name);
			info.setType(type);
			info.setSubType(subType);
			info.setSell(sell);
			info.setUseParam(useParam);
			info.setColour(colour);
			info.setLevel(level);
			info.setKey(key);
			
			if(itemNo.startsWith(GameValue.PROP_STAR_N0)){
				info.setResolmoney(Integer.valueOf(rootEle.attributeValue("Resolmoney").trim()));
			}


			if (PropXMLInfoMap.getPropXMLInfo(info.getNo()) != null) {
				throw new RuntimeException("Load Prop.xml error! there is no = " + info.getNo() + " repeat!");
			}

			PropXMLInfoMap.addPropXMLInfo(info);
		}
	}

	public static void loadStoneComp(Element rootEle,boolean modify) {
		if (rootEle != null) {
			int no = Integer.valueOf(rootEle.attributeValue("No").trim());
			int level = Integer.valueOf(rootEle.attributeValue("Level").trim());
			int material = Integer.valueOf(rootEle.attributeValue("Material").trim());
			int silver = Integer.valueOf(rootEle.attributeValue("Silver").trim());
			int removeSilver = Integer.valueOf(rootEle.attributeValue("Money").trim());
			int num = Integer.valueOf(rootEle.attributeValue("Num").trim());

			StoneComeXML info = new StoneComeXML();
			info.setNo(no);
			info.setLevel(level);
			info.setMaterial(material);
			info.setSilver(silver);
			info.setRemoveSilver(removeSilver);
			info.setNum(num);
			StoneCompXMLMap.addXmlInfo(info);
		}
	}

	public static void loadPropAttr(Element rootEle,boolean modify) {
		if (rootEle != null) {
			int no = Integer.valueOf(rootEle.attributeValue("No").trim());
			int type = Integer.valueOf(rootEle.attributeValue("Type").trim());
			int num = Integer.valueOf(rootEle.attributeValue("Addnum").trim());

			PropAttriAddXml info = new PropAttriAddXml();
			info.setNo(no);
			info.setProType(HeroProType.getHeroProType(type));
			if (info.getProType() == null) {
				throw new RuntimeException("Load PropAttribute.xml error! type=" + type+" not exit");
			}
			info.setAddNum(num);
			PropAttriAddXmlMap.addXmlInfo(info);
		}
	}
}
