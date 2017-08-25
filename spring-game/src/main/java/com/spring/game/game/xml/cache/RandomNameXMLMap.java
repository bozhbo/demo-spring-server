package com.snail.webgame.game.xml.cache;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class RandomNameXMLMap {

	//性
	private static List<String> surname = new ArrayList<String>();
	//男名
	private static List<String> surnanming = new ArrayList<String>();
	//女民
	private static List<String> surnvming = new ArrayList<String>();
	
	/**
	 * 添加姓氏
	 * @param collection
	 */
	public static void addAllSurname(Collection<String> collection) {
		surname.addAll(collection);
	}
	
	/**
	 * 添加男名
	 * @param collection
	 */
	public static void addAllSurnanming(Collection<String> collection) {
		surnanming.addAll(collection);
	}
	
	/**
	 * 添加女民
	 * @param collection
	 */
	public static void addAllSurnvming(Collection<String> collection) {
		surnvming.addAll(collection);
	}
	
	/**
	 * 随机姓氏
	 * @return
	 */
	public static String randomSurname() {
		if (surname != null && !surname.isEmpty()) {
			Collections.shuffle(surname);
			return surname.get(0);
		}
		return null;
	}
	
	/**
	 * 随机男名字
	 * @return
	 */
	public static String randomSurnanming() {
		if (surnanming != null && !surnanming.isEmpty()) {
			Collections.shuffle(surnanming);
			return surnanming.get(0);
		}
		return null;
	}
	
	/**
	 * 随机女名字
	 * @return
	 */
	public static String randomSurnvming() {
		if (surnvming != null && !surnvming.isEmpty()) {
			Collections.shuffle(surnvming);
			return surnvming.get(0);
		}
		return null;
	}
	
	/**
	 * 随机一个男姓名
	 * @return
	 */
	public static String randomMaleName() {
		StringBuilder builder = new StringBuilder();
		String surname = randomSurname();
		if (surname != null) {
			builder.append(surname);			
			builder.append(randomSurnanming());
		}
		return builder.toString();
	}
	
	/**
	 * 随机一个女姓名
	 * @return
	 */
	public static String randomFemaleName() {
		StringBuilder builder = new StringBuilder();
		String surname = randomSurname();
		if (surname != null) {
			builder.append(surname);			
			builder.append(randomSurnvming());
		}
		return builder.toString();
	}
}

