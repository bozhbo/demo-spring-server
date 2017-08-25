package com.snail.webgame.engine.common.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

/**
 * ErrorCode导出
 * @author zenggy
 *
 */
public class ErrorCodeUtil {

	/**
	 * 导出errorcode
	 */
	public static void exportErrorCode(String errorCodeFilePath, String outPath, String outFileName){
		
		try {
			String s, old = null;
			String value[] = new String[2];
			FileInputStream input = new FileInputStream(errorCodeFilePath);
			InputStreamReader fileReader = new InputStreamReader(input, "UTF-8");
			BufferedReader in = new BufferedReader(fileReader);
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(outPath + "/" + outFileName), "UTF-8"));
			out.write("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>" + "\r");
			out.write("<error>" + "\r");
			while ((s = in.readLine()) != null) {
				if (s.trim().startsWith("*") && !s.trim().startsWith("*/")) {
					old = s.trim().substring(1).trim();
				}

				if (s.trim().startsWith("public static")) {
					value = s.trim().split("=");
					value[0] = old;
					s = "  <item code=\""
							+ value[1].trim().replace("\"", "").replace(";", "")
							+ "\" value=\"" + value[0] + "\" />" + "\r";
					out.write(s);
//				System.out.println(s);
				}
			}
			out.write("</error>");
			out.close();
			fileReader.close();
		}
		catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
