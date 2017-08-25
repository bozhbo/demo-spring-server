package com.snail.webgame.engine.gate.config;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.engine.common.util.CodeUtil;

public class CertConfig {

	private static final Logger log = LoggerFactory.getLogger("logs");
	private static CertConfig config;
	private static String filePath = null;

	private static String IP;
	private static int port;
	private long time;
	private boolean flag = false;

	private CertConfig() {
		loadConfig();
	}

	public synchronized static CertConfig getInstance() {
		if (config == null) {
			config = new CertConfig();
		}
		return config;
	}

	public synchronized static CertConfig getInstance(String configPath) {

		if (config == null) {
			filePath = configPath;
			config = new CertConfig();
		}
		return config;
	}

	/**
	 * 载入配置文件
	 * 
	 * @param filePath
	 *            配置文件路径
	 */
	private void loadConfig() {
		InputStream is = CertConfig.class.getResourceAsStream(filePath);
		if (is != null) {
			readConfig(is);
		} else {
			if (log.isErrorEnabled()) {
				log.error("System can not find license ,system exist!");
			}
		}
	}

	/**
	 * 读取配置文件
	 * 
	 * @param file
	 */
	private void readConfig(InputStream is) {

		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(is));

			String str = "";
			str = br.readLine();

			br.close();

			String[] filepaths = filePath.split("/");

			String name = filepaths[filepaths.length - 1];

			if (str != null && str.length() > 0) {
				String strK = WebGameConfig.getInstance().getLocalConfig().getRomateIP() + name;

				String strK1 = WebGameConfig.getInstance().getLocalConfig().getRomateIP() + name;

				String mm = CodeUtil.Md5(strK);

				String m1 = mm.substring(0, 16);
				String m2 = mm.substring(16, 32);

				String nn = CodeUtil.Md5(strK1);
				String n1 = nn.substring(0, 16);
				String n2 = nn.substring(16, 32);

				if (str.startsWith(m1) && str.endsWith(m2)) {
					String str0 = str.substring(16, str.length() - 16);
					String str1 = CodeUtil.decrypt(str0, n2, "blowfish");
					int length1 = Integer.valueOf(str1.substring(0, 4));

					String k1 = str1.substring(4, 4 + length1);
					String k2 = str1.substring(4 + length1, str1.length());

					String pass1 = CodeUtil.decrypt(k1, n1, "blowfish");

					String deStr1 = CodeUtil.decrypt(k2, pass1, "blowfish");

					String p1 = deStr1.substring(4, 4 + length1);
					String p2 = deStr1.substring(4 + length1, deStr1.length());

					String pass2 = CodeUtil.decrypt(p1, n2, "blowfish");

					String deStr2 = CodeUtil.decrypt(p2, pass2, "blowfish");

					String endStr[] = deStr2.split(",");

					if (endStr != null && endStr.length == 2) {

						IP = endStr[0];
						time = Long.valueOf(endStr[1]);
					}

				} else {

				}

			}
		} catch (Exception e) {

			e.printStackTrace();
		}

		if (IP != null && IP.equals(WebGameConfig.getInstance().getLocalConfig().getRomateIP())
				&& time > System.currentTimeMillis()) {

			Date date = new Date(time);

			log.warn("System, through digital signature verification,the certificate expiration time for:"
					+ date.toString());

			flag = true;
		} else {
			log.warn("System does not have the correct digital certificate ! LicIP=" + IP + " RomateIP="
					+ WebGameConfig.getInstance().getLocalConfig().getRomateIP());
			System.exit(0);
		}

	}

	public boolean isPass() {
		return flag;
	}

	public long getTime() {
		return time;
	}

}
