package com.snail.webgame.game.protocal.gm.service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.game.cache.RoleInfoMap;
import com.snail.webgame.game.common.ErrorCode;
import com.snail.webgame.game.info.RoleInfo;
import com.snail.webgame.game.protocal.gm.annotation.CommandMappingHandlerAdapter;
import com.snail.webgame.game.protocal.gm.command.GmReq;
import com.snail.webgame.game.protocal.gm.command.GmResp;
import com.snail.webgame.game.xml.cache.GmCommandMap;
import com.snail.webgame.game.xml.info.GmCommandInfo;

public class GmMgtService {

	private static final Logger logger = LoggerFactory.getLogger("logs");

	public GmResp dealGm(int roleId, GmReq req) {
		GmResp resp = new GmResp();

		String command = req.getCommand();
		if (StringUtils.isBlank(command)) {
			resp.setResult(ErrorCode.REQUEST_PARAM_ERROR);
			return resp;
		}
		resp.setCommand(command);

		RoleInfo gmRoleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (gmRoleInfo == null || gmRoleInfo.getGmRight() == 0) {
			resp.setResult(ErrorCode.GM_CMD_ERROR_15);
			return resp;
		}
		RoleInfo targetRole = null;
		String[] args = null;
		
		if(command != null && command.startsWith("/gm doAppCharge")){
			String argsString = command.substring("/gm doAppCharge ".length());
			argsString = "doAppCharge," + argsString;
			args = StringUtils.split(argsString, ",");
		} else {

			String str[] = StringUtils.split(command, " ");
			if (str == null || str.length < 2 || !"/gm".equals(str[0])) {
				resp.setResult(ErrorCode.GM_CMD_ERROR_16);
				return resp;
			}

			int startIndex = 1;

			if (StringUtils.startsWith(str[1], "<") && StringUtils.endsWith(str[1], ">")) {
				String roleName = StringUtils.substring(str[1], 1, str[1].length() - 1);
				targetRole = RoleInfoMap.getRoleInfoByName(roleName);
				if (targetRole == null) {
					resp.setResult(ErrorCode.GM_CMD_ERROR_17);
					return resp;
				}
				startIndex = 2;
			}
			args = Arrays.copyOfRange(str, startIndex, str.length);
		}

		if (targetRole == null) {
			targetRole = gmRoleInfo;
		}
		int result = executeCmd(targetRole, gmRoleInfo, args);

		if (result == 1) {
			// TODO 记录操作成功的日志
			// GameLogService.insertGMLog(gmRoleInfo.getId(),
			// gmRoleInfo.getGmRight(), command);
		}
		resp.setResult(result);
		return resp;
	}

	/**
	 * 执行gm命令
	 * @param targetRole
	 * @param gmRoleInfo
	 * @param args
	 * @return
	 */
	private int executeCmd(RoleInfo targetRole, RoleInfo gmRoleInfo, String[] args) {
		int result = ErrorCode.GM_CMD_ERROR_1;
		String cmd = args[0];
		if (StringUtils.isBlank(cmd)) {
			return ErrorCode.GM_CMD_ERROR_18;
		}

		GmCommandInfo cmdInfo = GmCommandMap.getCmdInfo(cmd);
		if (cmdInfo == null) {
			return ErrorCode.GM_CMD_ERROR_19;
		}

		int rightSet = gmRoleInfo.getGmRight();

		boolean hasRight = false;
		if (rightSet > 0) {
			hasRight = true;// 拥有这个权限
		}
		if (!hasRight) {
			return ErrorCode.GM_CMD_ERROR_4;
		}
		if (cmdInfo.getArgNum() != -1 && cmdInfo.getArgNum() != args.length) {
			return ErrorCode.GM_CMD_ERROR_2;
		}
		result = invokeCommandMethod(cmd, targetRole, Arrays.copyOfRange(args, 1, args.length));
		return result;
	}

	/**
	 * 执行方法
	 * @param cmd
	 * @param args
	 * @return
	 */
	private static int invokeCommandMethod(String cmd, Object... args) {
		int result = ErrorCode.SYSTEM_ERROR;
		Method method = CommandMappingHandlerAdapter.getCommandMethod(cmd);
		if (method != null) {
			CommandService service = CommandService.getInstance();
			try {
				result = (Integer) method.invoke(service, args);
			} catch (IllegalArgumentException e) {
				logger.error("GmMgtService.invokeCommandMethod exec error: " + e.getMessage(), e);
			} catch (IllegalAccessException e) {
				logger.error("GmMgtService.invokeCommandMethod exec error: " + e.getMessage(), e);
			} catch (InvocationTargetException e) {
				logger.error("GmMgtService.invokeCommandMethod exec error: " + e.getMessage(), e);
			}
		}
		return result;
	}
}
