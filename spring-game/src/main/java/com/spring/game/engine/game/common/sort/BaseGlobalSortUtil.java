package com.snail.webgame.engine.game.common.sort;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * 类介绍:全局排序类，此类支持对Id进行排序，排序规则由各Id相关的SortInfo接口实现{#Comparable.compareTo()}方法
 * 同时提供查询方法
 *
 * @author zhoubo
 * @version 1.0.0
 * @since 2016年1月21日
 */
public class BaseGlobalSortUtil {

	private static Logger logger = LoggerFactory.getLogger("logs");

	private static Map<String, InnerSortInfo> map = new HashMap<String, InnerSortInfo>();
	private static Random r = new Random();

	/**
	 * 初始化排序,设定需要排序类型
	 * 
	 * @param paths		
	 * @param maxRanges
	 */
	public static synchronized void initGlobalSort(Class<SortInfo>[] paths, int[] maxRanges) {
		if (map.size() > 0) {
			return;
		}

		if (paths.length != maxRanges.length) {
			if (logger.isWarnEnabled()) {
				logger.warn("BaseGlobalSortUtil[initGlobalSort] : paths and maxRanges has differently size");
			}

			return;
		}

		for (int i = 0; i < paths.length; i++) {
			if (map.containsKey(paths[i].getName())) {
				if (logger.isWarnEnabled()) {
					logger.warn("BaseGlobalSortUtil[initGlobalSort] : add key is in map");
				}
			} else {
				InnerSortInfo innerSortInfo = new InnerSortInfo();
				innerSortInfo.list = new LinkedList<Integer>();
				innerSortInfo.map = new HashMap<Integer, SortInfo>();
				innerSortInfo.maxRange = maxRanges[i] <= 0 ? 1 : maxRanges[i];

				map.put(paths[i].getName(), innerSortInfo);
			}
		}
	}

	/**
	 * 初始化启动排序,一般用于服务器启动初次排序
	 * 
	 * @param path
	 * @param sortMap
	 */
	public static void initSort(String path, final Map<Integer, SortInfo> sortMap) {
		InnerSortInfo innerSortInfo = map.get(path);

		if (innerSortInfo == null) {
			if (logger.isErrorEnabled()) {
				logger.error("BaseGlobalSortUtil[initSort] : path = " + path + " is not exist");
			}

			return;
		}

		if (sortMap == null || sortMap.size() == 0) {
			return;
		}

		synchronized (innerSortInfo) {
			innerSortInfo.list.addAll(sortMap.keySet());
			Collections.sort(innerSortInfo.list, new Comparator<Integer>() {

				@Override
				public int compare(Integer o1, Integer o2) {
					return sortMap.get(o1).compareTo(sortMap.get(o2));
				}
			});

			if (innerSortInfo.maxRange < innerSortInfo.list.size()) {
				// 排序数量有限制
				List<Integer> useList = innerSortInfo.list.subList(0, innerSortInfo.maxRange + 1);

				for (Integer integer : useList) {
					innerSortInfo.map.put(integer, sortMap.get(integer));
				}

				innerSortInfo.list.subList(innerSortInfo.maxRange + 1, innerSortInfo.list.size()).clear();
			} else {
				for (Integer integer : innerSortInfo.list) {
					innerSortInfo.map.put(integer, sortMap.get(integer));
				}
			}

			sortMap.clear();
		}
	}

	/**
	 * 排序值有变动，重新排序
	 * 
	 * @param id
	 * @param sortInfo
	 */
	public static void addSort(int id, SortInfo sortInfo) {
		InnerSortInfo innerSortInfo = map.get(sortInfo.getClass().getName());

		if (innerSortInfo == null) {
			if (logger.isErrorEnabled()) {
				logger.error("BaseGlobalSortUtil[addSort] : path = " + sortInfo.getClass().getName() + " is not exist");
			}

			return;
		}

		synchronized (innerSortInfo) {
			if (innerSortInfo.list.size() == 0) {
				innerSortInfo.list.add(id);
				innerSortInfo.map.put(id, sortInfo);
			} else {
				if (!innerSortInfo.map.containsKey(id)) {
					// 第一次进入排名列表，先添加
					if (innerSortInfo.list.size() >= innerSortInfo.maxRange) {
						// 排序队列已满
						SortInfo lastSortInfo = innerSortInfo.map.get(innerSortInfo.list.get(innerSortInfo.list.size() - 1));

						if (sortInfo.compareTo(lastSortInfo) >= 0) {
							// 未超过最后一个，不进入排序队列
							return;
						} else {
							innerSortInfo.map.remove(innerSortInfo.list.get(innerSortInfo.list.size() - 1));
							innerSortInfo.map.put(id, sortInfo);
						}
					} else {
						innerSortInfo.map.put(id, sortInfo);
					}
				} else {
					// 已存在于排序队列
					innerSortInfo.list.remove(Integer.valueOf(id));
					innerSortInfo.map.put(id, sortInfo);
				}
			}

			// 排序队列为有序集合，进行二分插入,缩小插入检测范围
			int low = 0;
			int high = innerSortInfo.list.size() - 1;
			int mid = 0;

			while (low <= high) {
				if (high - low < 50) {
					break;
				}

				mid = (low + high) >>> 1;
				int midVal = innerSortInfo.list.get(mid);
				SortInfo tempSortInfo = innerSortInfo.map.get(midVal);

				if (tempSortInfo.compareTo(sortInfo) == -1) {
					low = mid + 1;
				} else if (tempSortInfo.compareTo(sortInfo) == 1) {
					high = mid - 1;
				} else {
					break;
				}
			}

			int index = -1;

			// 取得插入位置
			for (int i = low; i < high + 1; i++) {
				SortInfo tempSortInfo = innerSortInfo.map.get(innerSortInfo.list.get(i));
				
				if (sortInfo.compareTo(tempSortInfo) == -1) {
					index = i;
					break;
				}
			}

			if (index == -1) {
				innerSortInfo.list.add(high + 1, id);
			} else {
				innerSortInfo.list.add(index, id);
			}
		}
	}
	
	/**
	 * 获取位置
	 * 
	 * @param id
	 * @param path
	 * @return
	 */
	public static int getIndex(int id, String path) {
		InnerSortInfo innerSortInfo = map.get(path);

		if (innerSortInfo == null) {
			if (logger.isErrorEnabled()) {
				logger.error("BaseGlobalSortUtil[getIndex] : path = " + path + " is not exist");
			}

			return -1;
		}

		synchronized (innerSortInfo) {
			if (innerSortInfo.list.size() == 0) {
				return -1;
			}
			
			return innerSortInfo.list.indexOf(Integer.valueOf(id));
		}
	}
	
	/**
	 * 指定范围查询
	 * 
	 * @param path
	 * @param startIndex
	 * @param size
	 * @return
	 */
	public static List<Integer> getRank(String path, int startIndex, int size) {
		if (size <= 0) {
			return null;
		}
		
		InnerSortInfo innerSortInfo = map.get(path);

		if (innerSortInfo == null) {
			if (logger.isErrorEnabled()) {
				logger.error("BaseGlobalSortUtil[getRank] : path = " + path + " is not exist");
			}

			return null;
		}
		
		synchronized (innerSortInfo) {
			if (startIndex >= innerSortInfo.list.size()) {
				return null;
			}
			
			if (startIndex < 0) {
				return null;
			}
			
			List<Integer> list = new ArrayList<Integer>();
			
			while (startIndex < innerSortInfo.list.size() && size > 0) {
				list.add(innerSortInfo.list.get(startIndex));
				
				size--;
				startIndex++;
			}
			
			return list;
		}
	}
	
	/**
	 * 随机范围查询
	 * 
	 * @param path
	 * @param size
	 * @param sortRandomInfo
	 * @return
	 */
	public static List<Integer> random(String path, int size, SortRandomInfo sortRandomInfo) {
		if (size <= 0) {
			return null;
		}
		
		InnerSortInfo innerSortInfo = map.get(path);

		if (innerSortInfo == null) {
			if (logger.isErrorEnabled()) {
				logger.error("BaseGlobalSortUtil[random] : path = " + path + " is not exist");
			}

			return null;
		}
		
		List<Integer> result = new ArrayList<Integer>(size);

		synchronized (innerSortInfo) {
			int currentSize = innerSortInfo.list.size();
			int allSize = currentSize * 2; // 鉴于可能随机的Id无效，最多循环2倍大小的所需

			while (allSize > 0) {
				if (result.size() == size) {
					break;
				}
				
				try {
					int id = innerSortInfo.list.get(r.nextInt(currentSize));
					
					if (sortRandomInfo != null) {
						if (sortRandomInfo.canPass(id)) {
							result.add(id);
						} else {
							continue;
						}
					} else {
						result.add(id);
					}
				} finally {
					allSize--;
				}
			}
		}
		
		return result;
	}
	
	/**
	 * 移除排序
	 * 
	 * @param path
	 */
	public static void cleanRank(String path) {
		InnerSortInfo innerSortInfo = map.get(path);

		if (innerSortInfo == null) {
			if (logger.isErrorEnabled()) {
				logger.error("BaseGlobalSortUtil[cleanRank] : path = " + path + " is not exist");
			}

			return;
		}

		synchronized (innerSortInfo) {
			innerSortInfo.list.clear();
			innerSortInfo.map.clear();
		}
	}
}
