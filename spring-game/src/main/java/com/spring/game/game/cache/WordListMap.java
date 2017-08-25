package com.snail.webgame.game.cache;

import java.util.ArrayList;

public class WordListMap {

	private static ArrayList<String> list = new ArrayList<String>();

	public static void addWordList(String str) {
		list.add(str);
	}

	public static boolean isExistWord(String str, int type) {
		if (type == 1) {
			for (int i = 0; i < list.size(); i++) {
				String word = list.get(i);
				if (str.indexOf(word) != -1) {
					return true;
				}
			}
		} else if (type == 2) {
			String strArray[] = str.split(" ");
			for (int i = 0; i < list.size(); i++) {
				String word = list.get(i);
				for (int j = 0; j < strArray.length; j++) {
					if (strArray[j].equalsIgnoreCase(word)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public static String replaceWord(String content, int type) {
		if (type == 1) {
			for (int i = 0; i < list.size(); i++) {
				String word = list.get(i);
				if (content.indexOf(word) != -1) {
					StringBuilder builder = new StringBuilder();

					for (int k = 0; k < word.length(); k++) {
						builder.append("*");
					}
					content = content.replaceAll(word, builder.toString());
				}
			}
		} else if (type == 2) {
			String str[] = content.split(" ");

			for (int j = 0; j < str.length; j++) {
				if (str[j] != null && str[j].trim().length() > 0) {
					for (int i = 0; i < list.size(); i++) {
						String word = list.get(i);
						if (str[j].equalsIgnoreCase(word)) {
							StringBuilder temp = new StringBuilder();
							for (int k = 0; k < str[j].length(); k++) {
								temp.append("*");
							}
							str[j] = temp.toString();
						}
					}
				}
			}
			content = "";
			StringBuffer buffer = new StringBuffer();
			if (str.length > 0) {
				buffer.append(str[0]);
			}
			for (int j = 1; j < str.length; j++) {
				if (str[j] != null && str[j].trim().length() > 0) {
					buffer.append(" ");
					buffer.append(str[j]);
				}
			}
			content = buffer.toString();
		}
		return content;
	}

}
