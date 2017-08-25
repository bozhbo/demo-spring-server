package com.snail.webgame.game.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.game.cache.BlackWriteAccountMap;

public class LoadWriteAccountList {

	private static final Logger logger = LoggerFactory.getLogger("logs");

	/**
	 * 加载白账号列表
	 */
	public boolean load() {
		String path = LoadWriteAccountList.class.getResource("/config/writeAcount.txt").getPath();

		try {
			File file = new File(path);
			if (!file.isFile()) {
				if (logger.isErrorEnabled()) {
					logger.error("Load [writeAcount.txt]  infomation failure!");
					return true;
				}
			}
			String str = null;
			FileInputStream fis = new FileInputStream(file);
			InputStreamReader fileReader = new InputStreamReader(fis, "UTF-8");
			BufferedReader in = new BufferedReader(fileReader);
			while ((str = in.readLine()) != null) {
				if (str.trim().length() > 0) {
					BlackWriteAccountMap.getWriteAccountList().add(str.toUpperCase());
				}
			}
			in.close();
			fileReader.close();
			fis.close();

			if (logger.isInfoEnabled()) {
				logger.info("Load [writeAcount.txt]  infomation successful!");

			}

		} catch (Exception e) {
			if (logger.isErrorEnabled()) {
				logger.error("Load [writeAcount.txt]  infomation failure!",e);
			}
		}

		return true;
	}
	
	
	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		new LoadWriteAccountList().load();
		System.out.println(System.currentTimeMillis() - start);
	}
}
