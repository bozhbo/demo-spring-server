package com.snail.webgame.game.xml.load;

import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;

import com.snail.webgame.game.xml.cache.RandomNameXMLMap;

public class LoadRandomNameXML {

	private static final String SURNAME = "surname";
	private static final String SURNANMING = "surnanming";
	private static final String SURNVMING = "surnvming";

	private static final char SPLIT = ',';

	/**
	 * 加载RandomName.xml信息
	 * @param xmlName
	 * @param rootEle
	 */
	public static void load(Element rootEle) {
		if (rootEle != null) {
			String type = rootEle.attributeValue("Type");
			String value = rootEle.attributeValue("Text");
			String[] valueArray = StringUtils.split(value, SPLIT);
			if (valueArray == null || valueArray.length == 0) {
				throw new RuntimeException("Load RandomName.Xml error, attribute:text is empty or null");
			}
			if (SURNAME.equalsIgnoreCase(type)) {
				RandomNameXMLMap.addAllSurname(Arrays.asList(valueArray));
			} else if (SURNANMING.equalsIgnoreCase(type)) {
				RandomNameXMLMap.addAllSurnanming(Arrays.asList(valueArray));
			} else if (SURNVMING.equalsIgnoreCase(type)) {
				RandomNameXMLMap.addAllSurnvming(Arrays.asList(valueArray));
			} else {
				throw new RuntimeException("Load RandomName.Xml error, attribute:type not [surname, man, woman]");
			}
		}
	}
}