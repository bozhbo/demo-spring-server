package com.snail.webgame.game.cache;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.snail.webgame.game.common.DiffMail;
import com.snail.webgame.game.common.GameValue;
import com.snail.webgame.game.common.InnerSort;
import com.snail.webgame.game.common.xml.cache.StageXMLInfoMap;
import com.snail.webgame.game.info.RoleInfo;
import com.snail.webgame.game.protocal.fight.competition.service.CompetitionService;

/**
 * 
 * 类介绍:PVP竞技场全服排行集合类
 * 此类保持全局同步处理，可以嵌入synchronized(roleInfo){} 中使用
 * 服务器启动时的首次排序可能需要三秒，之后每次战斗后排序采用二分插入排序性能能到达要求
 *
 * @author zhoubo
 * @2015年4月1日
 */
public class FightCompetitionRankList {

	private static List<Integer> list = new ArrayList<Integer>();
	private static Map<Integer, InnerSort> map = new HashMap<Integer, InnerSort>();
	
	/**
	 * 服务器启动初始化后全局角色排名
	 */
	public static void initSort() {
		Set<Entry<Integer, RoleInfo>> set = RoleInfoMap.getMap().entrySet();
		List<RoleInfo> tempList = new ArrayList<RoleInfo>();
		
		for (Entry<Integer, RoleInfo> entry : set) {
			RoleInfo roleInfo = entry.getValue();
			
			if (roleInfo.getCompetitionStage() == StageXMLInfoMap.minStageXMLInfo.getId() && roleInfo.getCompetitionValue() == 0) {
				continue;
			}
			
			tempList.add(roleInfo);
		}
		
		if (tempList.size() > 0) {
			Collections.sort(tempList, new Comparator<RoleInfo>() {
				@Override
				public int compare(RoleInfo o1, RoleInfo o2) {
					if (o1.getCompetitionStage() > o2.getCompetitionStage()) {
						return -1;
					} else if (o1.getCompetitionStage() < o2.getCompetitionStage()) {
						return 1;
					} else {
						if (o1.getCompetitionValue() > o2.getCompetitionValue()) {
							return -1;
						} else if (o1.getCompetitionValue() < o2.getCompetitionValue()) {
							return 1;
						} else {
							if (o1.getCompetitionTime() < o2.getCompetitionTime()) {
								return -1;
							} else if (o1.getCompetitionTime() > o2.getCompetitionTime()) {
								return 1;
							} else {
								return 0;
							}
						}
					}
				}
			});
			
			for (RoleInfo roleInfo2 : tempList) {
				if (list.size() >= GameValue.COMPETITION_RANK_MAX_NUM) {
					break;
				}
				
				map.put(roleInfo2.getId(), new InnerSort(roleInfo2.getId(), roleInfo2.getCompetitionStage(), roleInfo2.getCompetitionValue(), roleInfo2.getCompetitionTime()));
				list.add(roleInfo2.getId());
			}
		}
	}
	
	/**
	 * 当前角色积分变动，需要重现排名
	 * 
	 * @param roleInfo	角色信息
	 */
	public static void addSort(RoleInfo roleInfo) {
		synchronized (list) {
			if (list.size() == 0) {
				list.add(roleInfo.getId());
				map.put(roleInfo.getId(), new InnerSort(roleInfo.getId(), roleInfo.getCompetitionStage(), roleInfo.getCompetitionValue(), roleInfo.getCompetitionTime()));
				return;
			}
			
			if (!map.containsKey(roleInfo.getId())) {
				// 第一次进入排名列表，先添加
				if (list.size() >= GameValue.COMPETITION_RANK_MAX_NUM) {
					InnerSort innerSort = map.get(list.get(list.size() - 1));
					
					if (innerSort.getStage() > roleInfo.getCompetitionStage()) {
						return;
					} else if (innerSort.getStage() == roleInfo.getCompetitionStage()) {
						if (innerSort.getValue() > roleInfo.getCompetitionValue()) {
							return;
						} else if (innerSort.getValue() == roleInfo.getCompetitionValue()) {
							if (innerSort.getTime() < roleInfo.getCompetitionTime()) {
								return;
							}
						}
					}
					
					map.remove(list.remove(list.size() - 1));
					map.put(roleInfo.getId(), new InnerSort(roleInfo.getId(), roleInfo.getCompetitionStage(), roleInfo.getCompetitionValue(), roleInfo.getCompetitionTime()));
				} else {
					map.put(roleInfo.getId(), new InnerSort(roleInfo.getId(), roleInfo.getCompetitionStage(), roleInfo.getCompetitionValue(), roleInfo.getCompetitionTime()));
				}
			} else {
				// 移除角色排名
				list.remove(Integer.valueOf(roleInfo.getId()));
				map.put(roleInfo.getId(), new InnerSort(roleInfo.getId(), roleInfo.getCompetitionStage(), roleInfo.getCompetitionValue(), roleInfo.getCompetitionTime()));
			}
			
			InnerSort innerSort = map.get(roleInfo.getId());
			
			// 排名集合为有序集合，缩小插入检测范围
			int low = 0;
			int high = list.size() - 1;
			int mid = 0;

			while (low <= high) {
				if (high - low < 2000) {
					break;
				}
				
				mid = (low + high) >>> 1;
				int midVal = list.get(mid);
				InnerSort tempInnerSort = map.get(midVal);
				
				if (compare(tempInnerSort, innerSort) == -1) {
					low = mid + 1;
				} else if (compare(tempInnerSort, innerSort) == 1) {
					high = mid - 1;
				} else {
					break;
				}
			}
			
			int index = -1;
			
			// 取得插入位置
			for (int i = low; i < high + 1; i++) {
				InnerSort o1 = map.get(list.get(i));
				
				if (o1.getStage() < innerSort.getStage()) {
					index = i;
					break;
				} else {
					if (o1.getStage() == innerSort.getStage()) {
						if (o1.getValue() < innerSort.getValue()) {
							index = i;
							break;
						} else {
							if (o1.getValue() == innerSort.getValue()) {
								if (o1.getTime() > innerSort.getTime()) {
									index = i;
									break;
								}
							}
						}
					}
				}
			}
			
			if (index == -1) {
				list.add(high + 1, roleInfo.getId());
			} else {
				list.add(index, roleInfo.getId());
			}
		}
	}
	
	private static int compare(InnerSort o1, InnerSort o2) {
		if (o1.getStage() > o2.getStage()) {
			return -1;
		} else if (o1.getStage() < o2.getStage()) {
			return 1;
		} else {
			if (o1.getValue() > o2.getValue()) {
				return -1;
			} else if (o1.getValue() < o2.getValue()) {
				return 1;
			} else {
				if (o1.getTime() < o2.getTime()) {
					return -1;
				} else if (o1.getTime() > o2.getTime()) {
					return 1;
				} else {
					return 0;
				}
			}
		}
	}
	
	/**
	 * 获取当前角色排名
	 * 
	 * @param roleInfo 角色信息
	 * @return	int-排名 -1-无排名
	 */
	public static int getRoleIndex(RoleInfo roleInfo) {
		if (roleInfo == null) {
			return -1;
		}
		
		if (roleInfo.getCompetitionStage() == StageXMLInfoMap.minStageXMLInfo.getId() && roleInfo.getCompetitionValue() == 0) {
			return -1;
		}
		
		synchronized (list) {
			if (list.size() == 0) {
				return -1;
			}
			
			InnerSort innerSort = map.get(list.get(list.size() - 1));
			
			if (innerSort.getStage() > roleInfo.getCompetitionStage()) {
				return -1;
			} else if (innerSort.getStage() == roleInfo.getCompetitionStage()) {
				if (innerSort.getValue() > roleInfo.getCompetitionValue()) {
					return -1;
				} else if (innerSort.getValue() == roleInfo.getCompetitionValue()) {
					if (innerSort.getTime() < roleInfo.getCompetitionTime()) {
						return -1;
					}
				}
			}
			
			return list.indexOf(Integer.valueOf(roleInfo.getId()));
		}
	}
	
	/**
	 * 获取角色排名子集合
	 * 
	 * @param startIndex	其实下标
	 * @param size			记录条数
	 * @return	子集合
	 */
	public static List<InnerSort> getRank(int startIndex, int size) {
		if (size <= 0) {
			return null;
		}
		
		if (list.size() == 0) {
			return null;
		}
		
		synchronized (list) {
			if (startIndex >= list.size()) {
				return null;
			}
			
			if (startIndex < 0) {
				return null;
			}
			
			List<InnerSort> roleList = new ArrayList<InnerSort>();
			
			while (startIndex < list.size() && size > 0) {
				roleList.add(map.get(list.get(startIndex)));
				
				size--;
				startIndex++;
			}
			
			return roleList;
		}
	}
	
	/**
	 * 取得跨服竞技场邮件信息
	 * 
	 * @return	List<DiffMail> 全员邮件信息集合
	 */
	public static List<DiffMail> getDiffMailList() {
		if (list.size() == 0) {
			return null;
		}
		
		synchronized (list) {
			List<DiffMail> diffMailList = new ArrayList<DiffMail>();
			
			for (int i = 0 ;i<list.size();i++) {
				RoleInfo roleInfo= RoleInfoMap.getRoleInfo(list.get(i));
				
				if (roleInfo != null) {
					DiffMail difMail = CompetitionService.getKuafuPlaceRewardMailInfo(roleInfo,i+1);
					
					if (difMail != null) {
						diffMailList.add(difMail);
					}
				}
			}
			
			return diffMailList;
		}
	}
	
	public static List<Integer> getList() {
		return list;
	}

	/**
	 * 清除排名集合
	 */
	public static void cleanRank() {
		synchronized (list) {
			list.clear();
			map.clear();
		}
	}
}
