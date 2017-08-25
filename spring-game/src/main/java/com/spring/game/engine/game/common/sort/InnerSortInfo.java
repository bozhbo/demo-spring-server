package com.snail.webgame.engine.game.common.sort;

import java.util.List;
import java.util.Map;

/**
 * 
 * 类介绍:全局排序内部使用类
 *
 * @author zhoubo
 * @version 1.0.0
 * @since 2016年1月21日
 */
class InnerSortInfo {

	int maxRange;	// 排序最大区间
	List<Integer> list = null;	// 排序集合
	Map<Integer, SortInfo> map = null;// 排序集合辅助Map，用于快速取值
}
