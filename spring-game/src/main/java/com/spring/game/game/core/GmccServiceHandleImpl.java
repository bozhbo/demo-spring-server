package com.snail.webgame.game.core;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.engine.component.gmcc.common.ReceiveGmccMsgReq;
import com.snail.webgame.engine.component.gmcc.message.GmccMessageService;
import com.snail.webgame.game.cache.RoleInfoMap;
import com.snail.webgame.game.common.ErrorCode;
import com.snail.webgame.game.dao.RoleDAO;
import com.snail.webgame.game.info.RoleInfo;
import com.snail.webgame.game.protocal.gmcc.receive.ChatGmccMsgReq;
import com.snail.webgame.game.protocal.gmcc.service.GmccMgtService;
import com.snail.webgame.game.protocal.rolemgt.logout.UserLogoutResp;
import com.snail.webgame.game.protocal.rolemgt.service.RoleCmdSend;

public class GmccServiceHandleImpl implements GmccMessageService {

	private static final Logger logger = LoggerFactory.getLogger("logs");

	private RoleDAO roleDAO = RoleDAO.getInstance();

	/**
	 * GMCC注册消息
	 * @param result 1-注册成功 otherwise-注册失败
	 */
	public void receiveRegister(int result) {
		if (logger.isInfoEnabled()) {
			logger.info("register gmcc :result=" + result);
		}
	}

	/**
	 * GMCC聊天消息
	 * @param req 消息内容，<br>
	 *            ReceiveGmccMsgReq根据flag的不同存在4种情况，<br>
	 *            参考 {@ReceiveGmccMsgReq}， 4种情况下的content为<br>
	 *            flag == 1,content == 游戏问题;充值问题;BUG提交;投诉#好评;中评;差评<br>
	 *            flag == 2,content == 正常聊天内容(如为对话开始会存在其他系统消息，例如：gm2002
	 *            邀请你加入对话./user001 加入了对话等)<br>
	 *            flag == 3,content == GM gm2002 结束了会话，请为gm2002 评价一下吧？<br>
	 *            flag == 4,content == null<br>
	 */
	public void receiveGmMessage(ReceiveGmccMsgReq req) {
		if (req != null) {
			int accountId = req.getAccountId();
			RoleInfo roleInfo = RoleInfoMap.getRoleInfo(accountId);
			if (roleInfo != null) {
				ChatGmccMsgReq gmccreq = new ChatGmccMsgReq();
				gmccreq.setFlag(req.getFlag());
				gmccreq.setGmId(req.getGmId());
				gmccreq.setGmNickname(req.getGmNickname());
				gmccreq.setContent(req.getContent());
				GmccMgtService.sendGmMessage(roleInfo, gmccreq);
			}
		}
	}

	/**
	 * GMCC踢除角色消息
	 * @param type 1-账号 2-角色名
	 * @param value 账号/角色名
	 * @return 1-成功 0-失败
	 */
	public int receiveKickRole(int type, String value) {
		List<RoleInfo> roles = new ArrayList<RoleInfo>();
		switch (type) {
		case 1:
			HashMap<Integer, RoleInfo> roleMap = RoleInfoMap.getRoleMapByAccount(value);
			if (roleMap != null && roleMap.size() > 0) {
				roles.addAll(roleMap.values());
			}
			break;
		case 2:
			RoleInfo roleInfo = RoleInfoMap.getRoleInfoByName(value);
			if (roleInfo != null) {
				roles.add(roleInfo);
			}
			break;
		default:
			return 0;
		}

		if (roles.size() > 0) {
			for (RoleInfo roleInfo : roles) {
				// 玩家在线则通知下线
				if (roleInfo != null && roleInfo.getLoginStatus() == 1) {
					UserLogoutResp resp = new UserLogoutResp();
					resp.setResult(ErrorCode.ROLE_KICK_ERROR_1);
					resp.setRoleId((int) roleInfo.getId());
					RoleCmdSend.sendUserLogoutResp(roleInfo, resp);
					return 1;
				}
			}
		}

		return 0;
	}

	/**
	 * GMCC系统广播
	 * @param message 消息内容
	 */
	public void receiveBroadcast(String message) {
		if (message != null && message.length() > 0) {
			GmccMgtService.sendChatMessage(message);
		}
	}

	/**
	 * GMCC增加聊天监控词汇，此为全量发送，请清空原有词汇集合
	 * @param words 监控词汇(以逗号分割)
	 */
	public void receiveAddWords(String words) {
		GmccMgtService.sendSysWordList(words, 0);
	}

	/**
	 * GMCC增加聊天监控角色，此为全量发送，请清空原有角色集合
	 * @param roles 监控角色(以逗号分割)
	 */
	public void receiveAddRoles(String roles) {
		GmccMgtService.sendRoleCount(roles, 0);
	}

	/**
	 * GMCC增加邮件监控词汇，此为全量发送，请清空原有词汇集合
	 * @param words 监控词汇(以逗号分割)
	 */
	public void receiveMailAddWords(String words) {
		GmccMgtService.sendSysWordList(words, 1);
	}

	/**
	 * GMCC增加邮件监控角色，此为全量发送，请清空原有角色集合
	 * @param roles 监控角色(以逗号分割)
	 */
	public void receiveMailAddRoles(String roles) {
		GmccMgtService.sendRoleCount(roles, 1);
	}

	/**
	 * GMCC禁言
	 * @param roleName 角色名称
	 * @param account 角色账号
	 * @param publishTime 禁言时间(单位:分钟)
	 * @return int 1-禁言成功 otherwise-禁言失败
	 */
	public int receiveChatLimt(String roleName, String account, int publishTime) {
		if (roleName != null && roleName.length() > 0) {
			RoleInfo roleInfo = RoleInfoMap.getRoleInfoByName(roleName.trim());
			if (roleInfo != null) {
				long now = System.currentTimeMillis();
				long currPunishTime = now + publishTime * 60 * 1000l;
				if (roleInfo.getPunishStatus() == 1 && roleInfo.getPunishTime().getTime() > now) {
					currPunishTime = roleInfo.getPunishTime().getTime() + publishTime * 60 * 1000;
				}
				byte punishStatus = 1; // 0-正常 1-禁言 2-冻结
				if (roleDAO.updateRolePublish(roleInfo.getId(), punishStatus, currPunishTime)) {
					roleInfo.setPunishStatus(punishStatus);
					roleInfo.setPunishTime(new Timestamp(currPunishTime));
					GmccMgtService.sendRolePublish(roleInfo.getId(), punishStatus, currPunishTime);
				} else {
					return ErrorCode.GMCC_ERROR_4;
				}
				// 日志
				GameLogService.insertPunishLog(roleInfo, punishStatus, publishTime, "");
				return 1;
			}
		}
		return ErrorCode.GMCC_ERROR_6;
	}

	/**
	 * GMCC解除禁言
	 * @param roleName 角色名称
	 * @param account 角色账号
	 * @return int 1-解除禁言成功 otherwise-解除禁言失败
	 */
	public int receiveReleaseChatLimt(String roleName, String account) {
		if (roleName != null && roleName.length() > 0) {
			RoleInfo roleInfo = RoleInfoMap.getRoleInfoByName(roleName.trim());
			if (roleInfo != null) {
				long now = System.currentTimeMillis();
				if (roleInfo.getPunishStatus() == 0) {
					return ErrorCode.ROLE_PUBLISH_ERROR_2;
				} else if (roleInfo.getPunishStatus() == 1 && roleInfo.getPunishTime().getTime() <= now) {
					return ErrorCode.ROLE_PUBLISH_ERROR_3;
				}
				long punishTime = 0;
				byte punishStatus = 0; // 0-正常 1-禁言 2-冻结
				if (roleDAO.updateRolePublish(roleInfo.getId(), punishStatus, punishTime)) {
					roleInfo.setPunishStatus(punishStatus);
					roleInfo.setPunishTime(new Timestamp(punishTime));
					GmccMgtService.sendRolePublish(roleInfo.getId(), punishStatus, punishTime);
				} else {
					return ErrorCode.GMCC_ERROR_1;
				}
				// 日志
				GameLogService.insertPunishLog(roleInfo, punishStatus, (int)punishStatus, "");
				return 1;
			}
		}
		return ErrorCode.GMCC_ERROR_5;
	}

	/**
	 * GMCC查询禁言时间
	 * @param roleName 角色名称
	 * @param account 角色账号
	 * @return int[] 长度为2 <br>
	 *         int[0]=聊天状态(0-正常 1-角色被冻结 2-角色永久冻结) <br>
	 *         int[1]=剩余时间(单位:分钟)
	 */
	public int[] receiveQueryChatLimt(String roleName, String account) {
		if (roleName != null && roleName.length() > 0) {
			RoleInfo roleInfo = RoleInfoMap.getRoleInfoByName(roleName.trim());
			if (roleInfo != null) {
				long now = System.currentTimeMillis();
				// 0-正常 1-禁言2-冻结
				int punishStatus = roleInfo.getPunishStatus();
				int leaveTime = 0;// 分钟
				if (roleInfo.getPunishStatus() == 1) {
					if (roleInfo.getPunishTime().getTime() <= now) {
						punishStatus = 0;
					} else {
						leaveTime = (int) ((roleInfo.getPunishTime().getTime() - now) / 60000); // 转换成分钟
					}
				}
				return new int[] { punishStatus, leaveTime };
			}
		}
		return null;
	}

	/**
	 * GMCC踢人且限时登陆
	 * @param roleName 角色名称
	 * @param account 角色账号
	 * @param loginTime 登录状态时间
	 * @return int 1-成功 otherwise-失败
	 */
	public int receiveKickAndRejectRole(String roleName, String account, long loginTime) {
		RoleInfo roleInfo = RoleInfoMap.getRoleInfoByName(roleName.trim());
		if (roleInfo != null) {
			if (loginTime <= System.currentTimeMillis()) {
				return ErrorCode.GMCC_ERROR_2;
			}
			if (roleInfo.getRoleStatus() == 2) {
				return ErrorCode.ROLE_STATUS_ERROR_5;
			}
			int roleStatus = 1;
			long roleStatusTime = loginTime;
			if (roleDAO.updateRoleStatus(roleInfo.getId(), roleStatus, roleStatusTime)) {
				roleInfo.setRoleStatus((byte) roleStatus);
				roleInfo.setRoleStatusTime(new Timestamp(roleStatusTime));
			} else {
				return ErrorCode.GMCC_ERROR_3;
			}

			if (roleInfo.getLoginStatus() == 1) {
				// 玩家在线则通知下线
				UserLogoutResp resp = new UserLogoutResp();
				resp.setResult(ErrorCode.USER_LOGIN_GMCC_KICK);
				resp.setRoleId((int) roleInfo.getId());
				RoleCmdSend.sendUserLogoutResp(roleInfo, resp);
			}
			return 1;
		}
		return 0;
	}

	/**
	 * GMCC连接成功
	 */
	public void receiveGmccReconnected() {
		if (logger.isInfoEnabled()) {
			logger.info("connected gmcc success");
		}
	}

}
