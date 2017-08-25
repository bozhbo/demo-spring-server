package com.snail.webgame.game.protocal.mine.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.mina.common.IoSession;
import org.epilot.ccf.core.protocol.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.engine.common.ServerName;
import com.snail.webgame.game.cache.HeroInfoMap;
import com.snail.webgame.game.cache.MineInfoMap;
import com.snail.webgame.game.cache.RoleInfoMap;
import com.snail.webgame.game.cache.ServerMap;
import com.snail.webgame.game.common.GameMessageHead;
import com.snail.webgame.game.common.GameValue;
import com.snail.webgame.game.common.HeroRecord;
import com.snail.webgame.game.common.util.Command;
import com.snail.webgame.game.common.util.RandomUtil;
import com.snail.webgame.game.dao.MineDAO;
import com.snail.webgame.game.info.HeroInfo;
import com.snail.webgame.game.info.MineHelpRole;
import com.snail.webgame.game.info.MineInfo;
import com.snail.webgame.game.info.MinePrize;
import com.snail.webgame.game.info.MineRole;
import com.snail.webgame.game.info.RoleInfo;
import com.snail.webgame.game.info.RoleLoadInfo;
import com.snail.webgame.game.info.log.MineFightLog;
import com.snail.webgame.game.protocal.hero.service.HeroRecordService;
import com.snail.webgame.game.protocal.mail.chat.ChatResp;
import com.snail.webgame.game.protocal.mail.chat.PersonalChatResp;
import com.snail.webgame.game.protocal.mine.query.MineDefendLogRe;
import com.snail.webgame.game.protocal.mine.query.MineHelpRoleRe;
import com.snail.webgame.game.protocal.mine.query.MineHeroRe;
import com.snail.webgame.game.protocal.mine.query.MineInfoRe;
import com.snail.webgame.game.protocal.mine.query.MineRoleRe;
import com.snail.webgame.game.protocal.mine.query.QueryMineResp;
import com.snail.webgame.game.protocal.mine.scene.SceneMineRe;
import com.snail.webgame.game.protocal.push.service.PushMgrService;
import com.snail.webgame.game.protocal.scene.cache.MapRoleInfoMap;
import com.snail.webgame.game.protocal.scene.sceneRefre.SceneRefreService;
import com.snail.webgame.game.xml.cache.GoldBuyXMLInfoMap;
import com.snail.webgame.game.xml.cache.MineXMLInfoMap;
import com.snail.webgame.game.xml.info.GoldBuyXMLInfo;
import com.snail.webgame.game.xml.info.GoldBuyXMLPro;
import com.snail.webgame.game.xml.info.MineXMLInfo;
import com.snail.webgame.game.xml.info.PushXMLInfo;

public class MineService {

	private static final Logger logger = LoggerFactory.getLogger("logs");

	/**
	 * 启动服务器初始化数据
	 */
	public static void serverStartInitData() {
		// 随机生成矿
		List<MineInfo> adds = getServerStartInitData();
		if (adds != null) {
			if (MineDAO.getInstance().insertMines(adds)) {
				for (MineInfo info : adds) {
					MineInfoMap.addMineInfo(info);
				}
			}
		}
	}

	/**
	 * 启动服务器初始化数据
	 * @return
	 */
	private static List<MineInfo> getServerStartInitData() {
		int mineNum = MineXMLInfoMap.getMineNum();// 矿固定数量
		List<Integer> poslist = MineXMLInfoMap.getPostions();// 所有位置
		List<Integer> mineNos = new ArrayList<Integer>();
		Map<Integer, MineXMLInfo> xmlMap = MineXMLInfoMap.getMap();
		for (MineXMLInfo xmlInfo : xmlMap.values()) {
			if (xmlInfo.getMaxNum() > 0) {
				for (int i = 0; i < xmlInfo.getMaxNum(); i++) {
					mineNos.add(xmlInfo.getNo());
				}
			}
		}
		// 随机生成矿
		return getAddMineListByRadom(mineNum, mineNos, poslist);
	}

	/**
	 * 0：00 推送抢夺次数
	 * @param roleInfo
	 */
	public static void sendRoleRefreshMineNum(RoleInfo roleInfo) {
		RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
		if (roleLoadInfo == null) {
			return;
		}

		QueryMineResp resp = new QueryMineResp();
		resp.setResult(1);
		resp.setMineNum(roleLoadInfo.getTodayMineNum());
		resp.setMineLimit(MineService.getMineLimit(roleLoadInfo));
		resp.setBuyMine(roleLoadInfo.getTodayBuyMine());
		SceneRefreService.sendRoleRefreshMsg(roleInfo.getId(), SceneRefreService.REFRESH_TYPE_ROLE_MINE, resp);
	}

	/**
	 * 每5分钟检测大地图矿变化
	 * @param refresh
	 */
	public static void checkForAddMine(boolean refresh) {
		long now = System.currentTimeMillis();
		int size = 0;// 正在开采的数量
		Map<Integer, Integer> mineNoNumMap = new HashMap<Integer, Integer>();// 矿类型数量
		List<Integer> poslist = MineXMLInfoMap.getPostions();// 所有位置
		List<Integer> delIds = new ArrayList<Integer>();// 需要删除的矿
		List<MineInfo> addMines = new ArrayList<MineInfo>();// 需要新增的矿

		Set<Integer> changeIds = new HashSet<Integer>();// 变化的矿（矿开采数量变化（开采中））
		Set<Integer> endMineRoleIds = new HashSet<Integer>();// 有结束的矿坑的角色id

		Map<Integer, MineInfo> map = MineInfoMap.getMineMap();
		for (MineInfo mineInfo : map.values()) {
			synchronized (mineInfo) {
				boolean isClosed = mineInfo.isClosed();
				if (isClosed) {
					if (mineInfo.isPostionProtect()) {
						// 30分钟内位置不刷新矿
						poslist.remove((Integer) mineInfo.getPosition());
					} else {
						if (mineInfo.isCanDel()) {
							delIds.add(mineInfo.getId());
						}
					}
				} else {
					if (mineNoNumMap.containsKey(mineInfo.getMineNo())) {
						mineNoNumMap.put(mineInfo.getMineNo(), 1 + mineNoNumMap.get(mineInfo.getMineNo()));
					} else {
						mineNoNumMap.put(mineInfo.getMineNo(), 1);
					}
					poslist.remove((Integer) mineInfo.getPosition());
					size++;
				}
				for (MineRole mineRole : mineInfo.getRoles().values()) {
					if (mineRole.getEndTime() != null) {
						continue;
					}
					long endTime = mineRole.getCurrEndTime();
					// 5分钟内采矿结束时间变化
					if (endTime <= now && endTime > now - 5 * 60 * 1000) {
						if (!isClosed) {
							changeIds.add(mineInfo.getId());
						}
						if (mineRole.getStatus() == MineRole.NOT_GET_PRIZE) {
							endMineRoleIds.add(mineRole.getRoleId());
							endMineRoleIds.addAll(mineRole.getHelpRoles().keySet());
						}
					}
				}
			}
		}
		int mineNum = MineXMLInfoMap.getMineNum();
		if (size < mineNum) {
			List<Integer> mineNos = new ArrayList<Integer>();
			int havingNum = 0;
			for (MineXMLInfo xmlInfo : MineXMLInfoMap.getMap().values()) {
				if (mineNoNumMap.containsKey(xmlInfo.getNo())) {
					havingNum = mineNoNumMap.get(xmlInfo.getNo());
				}
				if (xmlInfo.getMaxNum() > havingNum) {
					for (int i = 0; i < xmlInfo.getMaxNum() - havingNum; i++) {
						mineNos.add(xmlInfo.getNo());
					}
				}
			}
			// 随机生成矿
			addMines = getAddMineListByRadom(mineNum - size, mineNos, poslist);
		}

		if (MineDAO.getInstance().updateMines(delIds, addMines)) {
			for (int mineId : delIds) {
				MineInfoMap.removeMine(mineId);
			}
			for (MineInfo info : addMines) {
				if (changeIds != null) {
					changeIds.add(info.getId());
				}
				MineInfoMap.addMineInfo(info);
			}
		}

		if (refresh) {
			if (changeIds.size() > 0) {
				// 推送场景矿石刷新
				refreshSceneMine(changeIds, null);
			}
			if (endMineRoleIds.size() > 0) {
				// 推送有开采结束的矿坑
				refreshEndMine(endMineRoleIds);
			}
		}
		if (logger.isInfoEnabled()) {
			logger.info("checkMine successful! costTime:" + (System.currentTimeMillis() - now));
		}
	}

	/**
	 * 随机生成矿
	 * @param addSize
	 * @param mineNos
	 * @param poslist
	 * @param changeIds
	 */
	private static List<MineInfo> getAddMineListByRadom(int addNum, List<Integer> mineNos, List<Integer> poslist) {
		int addSize = addNum;
		addSize = Math.min(addSize, mineNos.size());
		addSize = Math.min(addSize, poslist.size());
		if (addSize <= 0) {
			return null;
		}
		int posIndex[] = RandomUtil.getRandomData(addSize, poslist.size());
		int noIndex[] = RandomUtil.getRandomData(addSize, mineNos.size());
		List<MineInfo> adds = new ArrayList<MineInfo>();
		MineInfo mineInfo = null;
		for (int i = 0; i < addSize; i++) {
			mineInfo = new MineInfo();
			mineInfo.setMineNo(mineNos.get(noIndex[i]));
			mineInfo.setPosition(poslist.get(posIndex[i]));
			mineInfo.setCreateTime(new Timestamp(System.currentTimeMillis()));
			adds.add(mineInfo);
		}
		return adds;
	}

	/**
	 * 推送场景矿石刷新
	 * @param changeIds
	 * @param exRoleId
	 */
	public static void refreshSceneMine(Set<Integer> changeIds, Integer exRoleId) {
		if (changeIds != null && changeIds.size() > 0) {
			StringBuilder sb = new StringBuilder();
			for (int id : changeIds) {
				if (sb.length() > 0) {
					sb.append(",");
				}
				sb.append(id);
			}
			int index = 0;
			for (int roleId : MapRoleInfoMap.getMapRoleId()) {
				if (exRoleId != null && exRoleId == roleId) {
					continue;
				}

				RoleInfo otherInfo = RoleInfoMap.getRoleInfo(roleId);
				// 玩家当前处于断开中
				if (otherInfo == null || otherInfo.getLoginStatus() == 0 || otherInfo.getDisconnectPhase() == 1) {
					continue;
				}

				SceneRefreService.sendRoleRefreshMsg(roleId, SceneRefreService.REFRESH_TYPE_MINE, sb.toString());
				if (index++ > 500) {
					index = 0;
					try {
						TimeUnit.MILLISECONDS.sleep(200);
					} catch (InterruptedException e) {
						logger.error("refreshSceneMine Error", e);
					}
				}
			}
		}
	}

	/**
	 * 推送有开采结束的矿坑
	 * @param endMineRoleIds
	 */
	public static void refreshEndMine(Set<Integer> roleIds) {
		if (roleIds != null && roleIds.size() > 0) {
			int index = 0;
			RoleInfo roleInfo = null;
			for (int roleId : roleIds) {
				roleInfo = RoleInfoMap.getRoleInfo(roleId);
				if (roleInfo != null) {
					// 矿开采完成
					PushMgrService.dealOfflinePush(roleInfo, PushXMLInfo.PUSH_MINE_FINISH);
				}
				if (index++ > 500) {
					index = 0;
					try {
						TimeUnit.MILLISECONDS.sleep(200);
					} catch (InterruptedException e) {
						logger.error("refreshSceneMine Error", e);
					}
				}
			}
		}
	}

	/**
	 * 检测是否有产出产出
	 * @param mineInfo
	 * @param xmlInfo
	 * @param mineRole
	 * @param start
	 * @return
	 */
	public static boolean checkHavingOut(MineInfo mineInfo, MineXMLInfo xmlInfo, MineRole mineRole, Timestamp start,
			Timestamp end) {
		long startTime = mineRole.getCreateTime().getTime();
		if (start != null) {
			startTime = Math.max(startTime, start.getTime());
		}
		long endTime = mineRole.getCurrEndTime();
		if (end != null) {
			endTime = Math.min(endTime, end.getTime());
		}
		return getOut(xmlInfo, startTime, endTime) > 0;
	}

	/**
	 * 检测是否有产出产出
	 * @param mineInfo
	 * @param xmlInfo
	 * @param mineRole
	 * @param start
	 * @return
	 */
	public static boolean checkHavingOut(MinePrize minePrize, MineXMLInfo xmlInfo, Timestamp start, Timestamp end) {
		long startTime = minePrize.getCreateTime().getTime();
		if (start != null) {
			startTime = Math.max(startTime, start.getTime());
		}
		long endTime = minePrize.getCurrEndTime();
		if (end != null) {
			endTime = Math.min(endTime, end.getTime());
		}
		return getOut(xmlInfo, startTime, endTime) > 0;
	}

	/**
	 * 计算产出
	 * @param mineInfo
	 * @param xmlInfo
	 * @param mineRole
	 * @param start
	 * @param end
	 * @return
	 */
	public static int getOut(MineInfo mineInfo, MineXMLInfo xmlInfo, MineRole mineRole, Timestamp start) {
		long startTime = mineRole.getCreateTime().getTime();
		if (start != null) {
			startTime = Math.max(startTime, start.getTime());
		}
		long endTime = mineRole.getCurrEndTime();
		return getOut(xmlInfo, startTime, endTime);
	}

	/**
	 * 计算产出
	 * @param xmlInfo
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public static int getOut(MineXMLInfo xmlInfo, long startTime, long endTime) {
		int outPeriod = xmlInfo.getOutPeriod();// 产出频率（秒）
		int outTimeAdd = xmlInfo.getOutTimeAdd();// 单位产出数量（单位时间）
		int per = (int) (endTime - startTime) / (outPeriod * 1000);
		return per * outTimeAdd;
	}

	/**
	 * 检测抢夺次数
	 * @param roleInfo
	 * @return
	 */
	public static boolean checkMineNum(RoleInfo roleInfo) {
		RoleLoadInfo roleLoad = roleInfo.getRoleLoadInfo();
		if (roleLoad == null) {
			return false;
		}
		int mineNum = roleLoad.getTodayMineNum();
		int mineLimit = getMineLimit(roleLoad);
		return mineNum < mineLimit;
	}

	/**
	 * 获取矿抢夺上限
	 * @param roleInfo
	 * @return -1 无上限
	 */
	public static int getMineLimit(RoleLoadInfo roleLoad) {
		// 资源开采每日可抢夺数量
		int mineLimit = GameValue.MINE_NUM_LIMIT;
		int buyNum = 0;
		int buyMine = roleLoad.getTodayBuyMine();
		GoldBuyXMLInfo xmlInfo = GoldBuyXMLInfoMap.getGoldBuyXMLInfo(GoldBuyXMLInfo.TYPE_MINE_BUY);
		if (xmlInfo != null) {
			GoldBuyXMLPro pro = null;
			for (int i = 1; i <= buyMine; i++) {
				pro = xmlInfo.getGoldBuyXMLPro(i);
				if (xmlInfo.getFixed() == 1 && i > xmlInfo.getMaxBuyNum()) {
					// 1:无限次数，超过最后一次后价格不递增，按照最后一次的价格来
					pro = xmlInfo.getMaxBuyXMLPro();
				}
				if (pro != null) {
					buyNum += pro.getGain();
				}
			}
		}
		return mineLimit + buyNum;
	}

	/**
	 * 获取 SceneMineRe
	 * @param mineInfo
	 * @return
	 */
	public static List<SceneMineRe> getSceneMineReList(String mineIdStr) {
		List<SceneMineRe> list = new ArrayList<SceneMineRe>();
		if (mineIdStr != null && mineIdStr.length() > 0) {
			MineInfo mineInfo = null;
			String[] mineIds = mineIdStr.split(",");
			for (String mineId : mineIds) {
				mineInfo = MineInfoMap.getMineInfo(NumberUtils.toInt(mineId));
				if (mineInfo != null && !mineInfo.isClosed()) {
					synchronized (mineInfo) {
						list.add(getSceneMineRe(mineInfo));
					}
				}
			}
		} else {
			// 查询全部
			Map<Integer, MineInfo> map = MineInfoMap.getMineMap();
			for (MineInfo mineInfo : map.values()) {
				if (mineInfo != null && !mineInfo.isClosed()) {
					synchronized (mineInfo) {
						list.add(getSceneMineRe(mineInfo));
					}
				}
			}
		}
		return list;
	}

	/**
	 * 获取 SceneMineRe
	 * @param mineInfo
	 * @return
	 */
	public static SceneMineRe getSceneMineRe(MineInfo mineInfo) {
		SceneMineRe re = new SceneMineRe();
		re.setMineId(mineInfo.getId());
		re.setPosition(mineInfo.getPosition());
		re.setMineNo(mineInfo.getMineNo());
		re.setCreateTime(mineInfo.getCreateTime().getTime());
		re.setZlNum(mineInfo.getZlSize());
		return re;
	}

	/**
	 * 获取 MineInfoRe
	 * @param mineInfo
	 * @return
	 */
	public static MineInfoRe getMineInfoRe(RoleInfo roleInfo, MineInfo mineInfo, MineRole mineRole) {
		if (mineInfo == null || mineRole == null) {
			return null;
		}

		Map<Integer, Integer> heroMap = null;
		if (roleInfo != null) {
			long startTime = mineRole.getCreateTime().getTime();
			if (roleInfo.getId() == mineRole.getRoleId()) {
				if (mineRole.getStatus() == MineRole.GET_PRIZE) {
					return null;
				}
				heroMap = mineRole.getHeroMap();
			} else {
				MineHelpRole helpRole = mineRole.getHelpRoles(roleInfo.getId());
				if (helpRole != null) {
					if (helpRole.getStatus() == MineRole.GET_PRIZE) {
						return null;
					}
					heroMap = helpRole.getHeroMap();
					startTime = helpRole.getCreateTime().getTime();
				} else {
					return null;
				}
			}

			MineXMLInfo xmlInfo = MineXMLInfoMap.getMineXMLInfo(mineInfo.getMineNo());
			if (xmlInfo == null) {
				return null;
			}
			long now = System.currentTimeMillis();
			long endTime = mineRole.getCurrEndTime();
			// 过滤 放弃，被抢夺 没收益
			if (mineRole.getEndTime() != null && endTime <= now && getOut(xmlInfo, startTime, endTime) <= 0) {
				return null;
			}
		}
		MineInfoRe re = new MineInfoRe();
		re.setMineId(mineInfo.getId());
		re.setPosition(mineInfo.getPosition());
		re.setMineNo(mineInfo.getMineNo());
		re.setCreateTime(mineInfo.getCreateTime().getTime());

		if (heroMap != null) {
			List<MineHeroRe> heros = getMineHeroReList(heroMap);
			re.setCount(heros.size());
			re.setHeros(heros);
		}

		re.setZlDetail(getMineRoleRe(mineRole));
		if (re.getZlDetail() == null) {
			return null;
		}
		return re;
	}

	public static MineInfoRe getMineInfoRe(RoleInfo roleInfo, MinePrize minePrize) {
		if (minePrize == null) {
			return null;
		}
		MineXMLInfo xmlInfo = MineXMLInfoMap.getMineXMLInfo(minePrize.getMineNo());
		if (xmlInfo == null) {
			return null;
		}
		long startTime = minePrize.getCreateTime().getTime();
		if (roleInfo.getId() == minePrize.getRoleId()) {
			// 自己的矿点
			startTime = minePrize.getCreateTime().getTime();
			if(minePrize.getStatus() == MineRole.GET_PRIZE){
				return null;
			}
		} else {
			MineHelpRole helpRole = minePrize.getHelpRoles(roleInfo.getId());
			if(helpRole == null){
				return null;
			}
			startTime = helpRole.getCreateTime().getTime();
			if(helpRole.getStatus() == MineRole.GET_PRIZE){
				return null;
			}
		}
		long endTime = minePrize.getCurrEndTime();
		int out = MineService.getOut(xmlInfo, startTime, endTime);
		if (out <= 0) {
			// 没产出
			return null;
		}

		MineInfoRe re = new MineInfoRe();
		re.setMineId(minePrize.getMineId());
		re.setPosition(minePrize.getPosition());
		re.setMineNo(minePrize.getMineNo());
		re.setCreateTime(minePrize.getMineCreateTime().getTime());

		RoleInfo role = RoleInfoMap.getRoleInfo(minePrize.getRoleId());
		if (role == null) {
			return null;
		}
		HeroInfo mainHero = HeroInfoMap.getMainHeroInfo(role);
		if (mainHero == null) {
			return null;
		}

		MineRoleRe re1 = new MineRoleRe();
		re1.setMinePointId(minePrize.getMinePointId());
		re1.setRoleId(role.getId());
		re1.setRoleName(role.getRoleName());
		re1.setHeroNo(mainHero.getHeroNo());
		re1.setHeroLevel(mainHero.getHeroLevel());
		re1.setFightValue(0);

		re1.setCreateTime(minePrize.getCreateTime().getTime());
		re1.setEndTime(minePrize.getCurrEndTime());

		List<MineHelpRoleRe> helpRoles = new ArrayList<MineHelpRoleRe>();
		Map<Integer, MineHelpRole> helpRoleMap = minePrize.getHelpRoles();
		if (helpRoleMap != null) {
			for (MineHelpRole helpRole : helpRoleMap.values()) {
				helpRoles.add(getMineHelpRoleRe(helpRole));
			}
		}
		re1.setCount(helpRoles.size());
		re1.setHelpRoles(helpRoles);

		re.setZlDetail(re1);
		if (re.getZlDetail() == null) {
			return null;
		}
		return re;
	}

	/**
	 * 获取MineHeroRe
	 * @param heroMap
	 * @return
	 */
	private static List<MineHeroRe> getMineHeroReList(Map<Integer, Integer> heroMap) {
		List<MineHeroRe> heros = new ArrayList<MineHeroRe>();
		if (heroMap != null) {
			MineHeroRe re = null;
			for (int pos : heroMap.keySet()) {
				re = new MineHeroRe();
				re.setPosition((byte) pos);
				re.setHeroId(heroMap.get(pos));
				heros.add(re);
			}
		}

		return heros;
	}

	/**
	 * 获取MineRoleRe
	 * @param info
	 * @return
	 */
	public static MineRoleRe getMineRoleRe(MineRole info) {
		RoleInfo role = RoleInfoMap.getRoleInfo(info.getRoleId());
		if (role == null) {
			return null;
		}
		HeroInfo mainHero = HeroInfoMap.getMainHeroInfo(role);
		if (mainHero == null) {
			return null;
		}

		MineRoleRe re = new MineRoleRe();
		re.setMinePointId(info.getId());
		re.setRoleId(role.getId());
		re.setRoleName(role.getRoleName());
		re.setHeroNo(mainHero.getHeroNo());
		re.setHeroLevel(mainHero.getHeroLevel());
		re.setFightValue(getFightValue(role, info.getHeroMap()));

		re.setCreateTime(info.getCreateTime().getTime());
		re.setEndTime(info.getCurrEndTime());

		List<MineHelpRoleRe> helpRoles = new ArrayList<MineHelpRoleRe>();
		Map<Integer, MineHelpRole> helpRoleMap = info.getHelpRoles();
		if (helpRoleMap != null) {
			for (MineHelpRole helpRole : helpRoleMap.values()) {
				helpRoles.add(getMineHelpRoleRe(helpRole));
			}
		}
		re.setCount(helpRoles.size());
		re.setHelpRoles(helpRoles);
		return re;
	}

	/**
	 * 获取MineHelpRoleRe
	 * @param info
	 * @param helpInfo
	 * @return
	 */
	private static MineHelpRoleRe getMineHelpRoleRe(MineHelpRole helpInfo) {
		RoleInfo role = RoleInfoMap.getRoleInfo(helpInfo.getRoleId());
		if (role == null) {
			return null;
		}
		HeroInfo mainHero = HeroInfoMap.getMainHeroInfo(role);
		if (mainHero == null) {
			return null;
		}

		MineHelpRoleRe re = new MineHelpRoleRe();
		re.setHelpPos(helpInfo.getHelpPos());
		re.setRoleId(role.getId());
		re.setRoleName(role.getRoleName());
		re.setHeroNo(mainHero.getHeroNo());
		re.setHeroLevel(mainHero.getHeroLevel());
		re.setFightValue(getFightValue(role, helpInfo.getHeroMap()));

		re.setCreateTime(helpInfo.getCreateTime().getTime());
		return re;
	}

	/**
	 * 获取MineDefendLogRe
	 * @param log
	 * @return
	 */
	public static MineDefendLogRe getMineDefendLogRe(RoleInfo roleInfo, MineFightLog log) {
		MineDefendLogRe re = new MineDefendLogRe();
		re.setPosition(log.getPosition());
		re.setMineNo(log.getMineNo());
		re.setRoleId(log.getRoleId());
		re.setRoleName(log.getRoleName());
		re.setRoleHeroNo(log.getRoleHeroNo());
		re.setRoleLevel(log.getRoleLevel());
		re.setHelpRoleId(log.getHelpRoleId());

		re.setAttackRoleId(log.getAttackRoleId());
		re.setAttackRoleName(log.getAttackRoleName());
		re.setAttackRoleHeroNo(log.getAttackRoleHeroNo());
		re.setAttackRoleLevel(log.getAttackRoleLevel());

		re.setGetNum(log.getGetNum());
		if (log.getFightResult() == 1 || log.getFightResult() == 2) {
			if (log.getAttackRoleId() == roleInfo.getId()) {
				re.setFightResult(log.getFightResult());
			} else {
				re.setFightResult(log.getFightResult() == 1 ? (byte) 2 : (byte) 1);
			}
		} else {
			re.setFightResult(log.getFightResult());
		}
		re.setTime(log.getTime().getTime());
		return re;
	}

	/**
	 * 获取角色阵形
	 * @param role
	 * @param heroMap
	 * @return
	 */
	public static Map<Byte, HeroRecord> getHeroRecordMap(RoleInfo roleInfo, Map<Integer, Integer> heroMap) {
		HeroInfo heroInfo = null;
		List<Integer> jbHeroNoList = new ArrayList<Integer>();
		for (int pos : heroMap.keySet()) {
			heroInfo = HeroInfoMap.getHeroInfo(roleInfo.getId(), heroMap.get(pos));
			if (heroInfo == null) {
				return null;
			}
			if (pos > GameValue.FIGHT_ARMY_LIMIT) {
				jbHeroNoList.add(heroInfo.getHeroNo());
			}
		}

		HeroRecord heroRecord = null;
		Map<Byte, HeroRecord> recordMap = new HashMap<Byte, HeroRecord>();
		for (int pos : heroMap.keySet()) {
			if (pos != HeroInfo.DEPLOY_TYPE_COMM) {
				heroInfo = HeroInfoMap.getHeroInfo(roleInfo.getId(), heroMap.get(pos));
				if (heroInfo == null) {
					return null;
				}
				heroRecord = HeroRecordService.getDeployHeroRecord(heroInfo, (byte) pos);
				if (heroRecord == null) {
					continue;
				}
				if (pos == HeroInfo.DEPLOY_TYPE_MAIN) {
					heroRecord.setJbHeroNoList(jbHeroNoList);
				}
				recordMap.put((byte) pos, heroRecord);
			}
		}
		if (!heroMap.containsKey((int) HeroInfo.DEPLOY_TYPE_MAIN)) {
			heroInfo = HeroInfoMap.getMainHeroInfo(roleInfo);
			if (heroInfo == null) {
				return null;
			}
			heroRecord = HeroRecordService.getDeployHeroRecord(heroInfo, HeroInfo.DEPLOY_TYPE_MAIN);
			if (heroRecord == null) {
				return null;
			}
			heroRecord.setJbHeroNoList(jbHeroNoList);
			recordMap.put(HeroInfo.DEPLOY_TYPE_MAIN, heroRecord);
		}
		return recordMap;
	}

	/**
	 * 获取角色阵形 战斗力
	 * @param roleInfo
	 * @param heroMap
	 * @return
	 */
	private static int getFightValue(RoleInfo roleInfo, Map<Integer, Integer> heroMap) {
		int fightValue = 0;
		Map<Byte, HeroRecord> recordMap = getHeroRecordMap(roleInfo, heroMap);
		if (recordMap != null) {
			for (HeroRecord record : recordMap.values()) {
				if (record.getFightValue() == 0) {
					int recordFightValue = HeroRecordService.getHeroRecordFightValue(recordMap, record, 1.0);
					record.setFightValue(recordFightValue);
				}
				fightValue += record.getFightValue();
			}
		}
		return fightValue;
	}

	/**
	 * 向工会会友发送防守邀请
	 * @param sendRole
	 * @param recRole
	 * @param mineId
	 */
	public static void sendClubHelpInvite(RoleInfo sendRole, RoleInfo recRole, int mineId, MineXMLInfo xmlInfo) {
		Message message = new Message();
		GameMessageHead head = new GameMessageHead();
		head.setUserID0(recRole.getId());
		head.setMsgType(Command.CHAT_RESP);
		message.setHeader(head);
		ChatResp resp = new ChatResp();
		resp.setResult(1);
		resp.setMsgType(Command.MSG_CAHNNEL_GUILD);
		resp.setSendRoleId(sendRole.getId());
		resp.setSendRace(sendRole.getRoleRace());
		resp.setSendRoleName(sendRole.getRoleName());
		HeroInfo heroInfo = HeroInfoMap.getMainHeroInfo(sendRole);
		if (heroInfo != null) {
			resp.setSendMainHeroNo(heroInfo.getHeroNo());
			String msgContent = String.format(GameValue.MINE_HELP_INVITE_CONTENT, sendRole.getRoleName(),
					heroInfo.getHeroLevel(), xmlInfo.getName());
			resp.setMsgContent(msgContent.toString());
		}
		resp.setRecRoleId(recRole.getId());
		resp.setRecRoleName(recRole.getRoleName());

		resp.setSendTime(System.currentTimeMillis());
		resp.setVipLevel((byte) sendRole.getVipLv());
		String defendStr = "mine," + mineId + "," + sendRole.getId();
		resp.setDefendStr(defendStr);
		message.setBody(resp);

		IoSession session = ServerMap.getServerSession(ServerName.GATE_SERVER_NAME + "-" + recRole.getGateServerId());
		if (session != null && session.isConnected()) {
			session.write(message);
			if (logger.isInfoEnabled()) {
				logger.info("send Club Mine Help Invite recRoleId:" + recRole.getId());
			}
		}
	}

	/**
	 * 向好友发送防守邀请
	 * @param sendRole
	 * @param recRole
	 * @param mineId
	 */
	public static void sendFriendHelpInvite(RoleInfo sendRole, RoleInfo recRole, int mineId, MineXMLInfo xmlInfo) {
		Message message = new Message();
		GameMessageHead head = new GameMessageHead();
		head.setUserID0(recRole.getId());
		head.setMsgType(Command.PERSONAL_CHAT_RESP);
		message.setHeader(head);
		PersonalChatResp resp = new PersonalChatResp();
		resp.setResult(1);
		resp.setRoleId(sendRole.getId());
		resp.setSendRace(sendRole.getRoleRace());
		resp.setRoleName(sendRole.getRoleName());
		HeroInfo heroInfo = HeroInfoMap.getMainHeroInfo(sendRole);
		if (heroInfo == null) {
			return;
		}
		resp.setSendMainHeroNo(heroInfo.getHeroNo());
		resp.setRecRoleId(recRole.getId());
		resp.setRecRoleName(recRole.getRoleName());

		String msgContent = String.format(GameValue.MINE_HELP_INVITE_CONTENT, sendRole.getRoleName(),
				heroInfo.getHeroLevel(), xmlInfo.getName());
		resp.setMsgContent(msgContent.toString());

		resp.setChatTime(System.currentTimeMillis());
		resp.setVipLv((byte) sendRole.getVipLv());
		String defendStr = "mine," + mineId + "," + sendRole.getId();
		resp.setDefendStr(defendStr);
		message.setBody(resp);

		IoSession session = ServerMap.getServerSession(ServerName.GATE_SERVER_NAME + "-" + recRole.getGateServerId());
		if (session != null && session.isConnected()) {
			session.write(message);
			if (logger.isInfoEnabled()) {
				logger.info("send Friend Mine Help Invite recRoleId:" + recRole.getId());
			}
		}
	}

	/**
	 * 初始化数据
	 */
	public static boolean minePrizeInitData() {
		List<Integer> delIds = new ArrayList<Integer>();// 需要删除的矿
		List<MinePrize> addPrizes = new ArrayList<MinePrize>();
		List<MineInfo> addMines = new ArrayList<MineInfo>();// 需要新增的矿
		Map<Integer, MineInfo> map = MineInfoMap.getMineMap();
		if (map != null) {
			boolean is = false;
			MinePrize prize = null;
			for (MineInfo mineInfo : map.values()) {
				delIds.add(mineInfo.getId());
				is = false;
				for (MineRole mineRole : mineInfo.getRoles().values()) {
					if (!is && mineRole.getStatus() == MineRole.NOT_GET_PRIZE) {
						is = true;
					}
					if (!is) {
						for (MineHelpRole helpRole : mineRole.getHelpRoles().values()) {
							if (!is && helpRole.getStatus() == MineRole.NOT_GET_PRIZE) {
								is = true;
								break;
							}
						}
					}
					if (is) {
						prize = getMinePrize(mineInfo, mineRole);
						if (prize != null) {
							addPrizes.add(prize);
						}
					}
				}
			}
		}
		addMines = getServerStartInitData();
		if (MineDAO.getInstance().updateMines(delIds, addPrizes, addMines)) {
			if (delIds != null) {
				for (int mineId : delIds) {
					MineInfoMap.removeMine(mineId);
				}
			}
			if (addPrizes != null) {
				for (MinePrize prize : addPrizes) {
					MineInfoMap.addMinePrize(prize);
				}
			}
			if (addMines != null) {
				for (MineInfo info : addMines) {
					MineInfoMap.addMineInfo(info);
				}
			}
		} else {
			return false;
		}
		return true;
	}

	private static MinePrize getMinePrize(MineInfo mineInfo, MineRole mineRole) {
		MinePrize info = new MinePrize();
		info.setMineId(mineInfo.getId());
		info.setPosition(mineInfo.getPosition());
		info.setMineNo(mineInfo.getMineNo());
		info.setMineCreateTime(mineInfo.getCreateTime());
		info.setMineEndTime(new Timestamp(mineInfo.getOldEndTime()));

		info.setMinePointId(mineRole.getId());
		info.setRoleId(mineRole.getRoleId());
		info.setCreateTime(mineRole.getCreateTime());
		info.setEndTime(mineRole.getEndTime());
		info.setEndStatus(mineRole.getEndStatus());
		info.setStatus(mineRole.getStatus());
		info.setHelpRoles(mineRole.getHelpRoles());

		long startTime = info.getCreateTime().getTime();
		long endTime = info.getCurrEndTime();
		long now = System.currentTimeMillis();
		if (endTime > now) {
			// 矿开采中
			info.setEndTime(new Timestamp(now));
			info.setEndStatus(MineRole.END_BY_FQ);
			endTime = now;
		}
		MineXMLInfo xmlInfo = MineXMLInfoMap.getMineXMLInfo(mineInfo.getMineNo());
		if (xmlInfo == null) {
			return null;
		}
		int out = MineService.getOut(xmlInfo, startTime, endTime);
		if (out <= 0) {
			// 没产出
			return null;
		}
		return info;
	}
}
