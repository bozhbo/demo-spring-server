package com.snail.webgame.game.xml.cache;

import java.util.HashMap;
import java.util.Map.Entry;

import com.snail.webgame.game.xml.info.AttackAnotherXMLInfo;

/**
 * 对攻战配置信息
 * @author wanglinhui
 *
 */
public class AttackAnotherXMLInfoMap {
	
	private static HashMap<Integer, AttackAnotherXMLInfo>  map = new HashMap<Integer, AttackAnotherXMLInfo>();
	
	public static void addAttackAnotherXMLInfo(AttackAnotherXMLInfo info){
		
		map.put(info.getNo(), info);
	}
	
	/**
	 * 根据等级取得信息
	 * @param level
	 */
	public static AttackAnotherXMLInfo getAttackAnotherXMLInfoByLevel(int level){
		
		AttackAnotherXMLInfo anotherXMLInfo = null;
		for(Entry<Integer, AttackAnotherXMLInfo> entry: map.entrySet()){
			if(level>=entry.getValue().getMinLevel()&&level<=entry.getValue().getMaxLevel()){
				anotherXMLInfo = entry.getValue();
				break;
			}
		}
		return anotherXMLInfo;
	}

}
