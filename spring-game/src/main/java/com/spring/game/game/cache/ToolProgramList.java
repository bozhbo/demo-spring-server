package com.snail.webgame.game.cache;

import java.util.ArrayList;
import java.util.List;

import com.snail.webgame.game.common.ToolProgram;

/**
 * 计费活动配置缓存
 */
public class ToolProgramList {

	private static List<ToolProgram> doubleExpList = new ArrayList<ToolProgram>();
	private static List<ToolProgram> doubleMoneyList = new ArrayList<ToolProgram>();
	private static List<ToolProgram> doubleCourageList = new ArrayList<ToolProgram>();
	private static List<ToolProgram> doubleJusticeList = new ArrayList<ToolProgram>();
	private static List<ToolProgram> doubleKuafuMoneyList = new ArrayList<ToolProgram>();
	private static List<ToolProgram> doubleTeamMoneyList = new ArrayList<ToolProgram>();
	private static List<ToolProgram> doubleExploitList = new ArrayList<ToolProgram>();
	private static List<ToolProgram> doubleHeroBattleList = new ArrayList<ToolProgram>();
	public static List<ToolProgram> getDoubleExpList() {
		return doubleExpList;
	}
	public static void setDoubleExpList(List<ToolProgram> doubleExpList) {
		ToolProgramList.doubleExpList = doubleExpList;
	}
	public static List<ToolProgram> getDoubleMoneyList() {
		return doubleMoneyList;
	}
	public static void setDoubleMoneyList(List<ToolProgram> doubleMoneyList) {
		ToolProgramList.doubleMoneyList = doubleMoneyList;
	}
	public static List<ToolProgram> getDoubleCourageList() {
		return doubleCourageList;
	}
	public static void setDoubleCourageList(List<ToolProgram> doubleCourageList) {
		ToolProgramList.doubleCourageList = doubleCourageList;
	}
	public static List<ToolProgram> getDoubleJusticeList() {
		return doubleJusticeList;
	}
	public static void setDoubleJusticeList(List<ToolProgram> doubleJusticeList) {
		ToolProgramList.doubleJusticeList = doubleJusticeList;
	}
	public static List<ToolProgram> getDoubleKuafuMoneyList() {
		return doubleKuafuMoneyList;
	}
	public static void setDoubleKuafuMoneyList(
			List<ToolProgram> doubleKuafuMoneyList) {
		ToolProgramList.doubleKuafuMoneyList = doubleKuafuMoneyList;
	}
	public static List<ToolProgram> getDoubleTeamMoneyList() {
		return doubleTeamMoneyList;
	}
	public static void setDoubleTeamMoneyList(List<ToolProgram> doubleTeamMoneyList) {
		ToolProgramList.doubleTeamMoneyList = doubleTeamMoneyList;
	}
	public static List<ToolProgram> getDoubleExploitList() {
		return doubleExploitList;
	}
	public static void setDoubleExploitList(List<ToolProgram> doubleExploitList) {
		ToolProgramList.doubleExploitList = doubleExploitList;
	}
	public static List<ToolProgram> getDoubleHeroBattleList() {
		return doubleHeroBattleList;
	}
	public static void setDoubleHeroBattleList(List<ToolProgram> doubleHeroBattleList) {
		ToolProgramList.doubleHeroBattleList = doubleHeroBattleList;
	}
}
