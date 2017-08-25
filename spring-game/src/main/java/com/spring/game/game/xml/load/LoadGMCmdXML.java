package com.snail.webgame.game.xml.load;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.game.xml.cache.GmCommandMap;
import com.snail.webgame.game.xml.info.GmCommandInfo;

/**
 * 加载GMCmd.xml
 * 
 * @author caowl
 * 
 */
public class LoadGMCmdXML {

	private static final Logger logger = LoggerFactory.getLogger("logs");
	
	public static void loadGmCmdXml(Element e,boolean modify) {
		int no = NumberUtils.toInt(e.attributeValue("No"));
		String cmd = e.attributeValue("Code");
		int num = NumberUtils.toInt(e.attributeValue("ArgNum"), -1);

		if (no > 0 && StringUtils.isNotBlank(cmd)) {
			GmCommandInfo cmdInfo = new GmCommandInfo();
			cmdInfo.setNo(no);
			cmdInfo.setCmd(cmd);
			cmdInfo.setArgNum(num);
			GmCommandMap.addCmdInfo(cmdInfo);
		} else {
			if (logger.isWarnEnabled()) {
				logger.warn("load GMCmdXml error, No:{}, Command:{}", no, cmd);
			}
		}
	}

}
