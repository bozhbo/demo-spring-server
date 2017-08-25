package com.snail.webgame.engine.gate.config;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.engine.common.util.XMLUtil4DOM;

public class TempTestConfig {
	private static final Logger log = LoggerFactory.getLogger("logs");
	private static String filePath = "/config/temptest.xml";
	private static TempTestConfig instance = new TempTestConfig();
	private boolean isOpen = false;
	private ArrayList<String> ipList = new ArrayList<String>();

	private TempTestConfig() {
		loadConfig();
	}

	public static TempTestConfig instance() {
		return instance;
	}

	private void loadConfig() {
		InputStream in = TempTestConfig.class.getResourceAsStream(filePath);
		if (in != null) {
			Document doc = XMLUtil4DOM.file2Dom(in);
			Element rootEle = null;
			if (doc != null) {
				rootEle = doc.getRootElement();

				List<Element> nodeList = rootEle.elements();

				if (nodeList != null && nodeList.size() > 0) {
					for (Element node : nodeList) {
						if ((node != null) && (node.getName().equals("ip"))) {
							ipList.add(node.getTextTrim());
						}
					}

					if (!isOpen) {
						ipList.add(WebGameConfig.getInstance().getLocalConfig().getLocalIP());
						isOpen = true;
					}
				}
			}
		}

	}

	public boolean isOpen() {
		return isOpen;
	}

	public void setOpen(boolean isOpen) {
		this.isOpen = isOpen;
	}

	/**
	 * 验证IP是否可以访问服务器
	 * 
	 * @param ip
	 * @return
	 */
	public boolean validate(String ip) {
		if (!isOpen) {
			return true;
		}
		if (ipList.contains(ip)) {
			return true;
		}
		return false;
	}
}
