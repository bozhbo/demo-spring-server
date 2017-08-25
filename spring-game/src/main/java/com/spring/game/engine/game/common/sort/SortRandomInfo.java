package com.snail.webgame.engine.game.common.sort;

/**
 * 
 * 类介绍:随机取值的验证可用接口
 *
 * @author zhoubo
 * @version 1.0.0
 * @since 2016年1月21日
 */
public interface SortRandomInfo {

	/**
	 * 随机的Id是否符合业务要求
	 * 
	 * @param id	随机结果Id
	 * @return	boolean true-符合要求 false-不符合要求
	 */
	public boolean canPass(int id);
}
