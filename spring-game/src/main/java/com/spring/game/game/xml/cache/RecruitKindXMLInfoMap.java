package com.snail.webgame.game.xml.cache;

import java.util.HashMap;

import com.snail.webgame.game.xml.info.RecruitKindXMLInfo;

public class RecruitKindXMLInfoMap {

	// <no,RecruitKindXMLInfo>
	private static HashMap<Integer, RecruitKindXMLInfo> map = new HashMap<Integer, RecruitKindXMLInfo>();

	public static void addRecruitKindXMLInfo(RecruitKindXMLInfo info,boolean modify) {
		if (map.containsKey(info.getNo()) && !modify) {
			throw new RuntimeException("Load RecruitKind.xml error! no: " + info.getNo() + " repeat");
		}
		map.put(info.getNo(), info);
	}

	public static RecruitKindXMLInfo getRecruitKindXMLInfo(int no) {
		return map.get(no);
	}

	public static HashMap<Integer, RecruitKindXMLInfo> getMap() {
		return map;
	}
}