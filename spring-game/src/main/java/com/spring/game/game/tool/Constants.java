package com.snail.webgame.game.tool;

import com.snail.webgame.engine.component.tool.config.Constant;

/**
 * 常量类
 * @author caowl
 *
 */
public abstract class Constants extends Constant {

	public static final String GAME_MSG_FIELD_SIZE_ERROR = "GAME TOOL-game tool server request param size error";
	
	public static final String GAME_TOOL_PUNISH_ERROR = "GAME TOOL-update game punish infomation error.";
	public static final String GAME_TOOL_PUNISH_ERROR_GUID = "GAME TOOL-update game punish guid not consistent.";
	public static final String GAME_TOOL_PUNISH_ACCOUNT_ERROR = "GAME TOOL-update game punish account not consistent or roleInfo is null.";
	public static final String GAME_TOOL_PUNISH_DB_ERROR = "GAME TOOL-update game punish update mysql error.";
	public static final String GAME_TOOL_PUNISH_INFO = "GAME TOOL-release game punish infomation";
	public static final String GAME_TOOL_PUNISH_NO_NOT_EXIST = "GAME TOOL-release game punish no is not exist";
	
	//发送邮件
	public static final String GAME_TOOL_SENDMAIL_INFO_01 = "GAME TOOL-update game send mail information error.";
	public static final String GAME_TOOL_SENDMAIL_INFO_02 = "GAME TOOL-update game send mail topic not consistent.";
	public static final String GAME_TOOL_SENDMAIL_INFO_03 = "GAME TOOL-update game send mail receivers is null.";
	public static final String GAME_TOOL_SENDMAIL_INFO_04 = "GAME TOOL-update game send mail information.";
	public static final String GAME_TOOL_SENDMAIL_INFO_05 = "GAME TOOL-update game send mail attachment error.";

	//修改角色
	public static final String GAME_TOOL_ROLE_ERROR_1 = "GAME TOOL-update game update role info error, param error";
	public static final String GAME_TOOL_ROLE_ERROR_2 = "GAME TOOL-update game update role info error, roleName:{0} is not exist";
	public static final String GAME_TOOL_ROLE_ERROR_3 = "GAME TOOL-update game update role info error, update resouece Type:{0} error";
	public static final String GAME_TOOL_ROLE_ERROR_4 = "GAME TOOL-update game update role info error, role is Logout update guide error";
	public static final String GAME_TOOL_ROLE_ERROR_5 = "GAME TOOL-update game update role info error, update roleName5 is error";
	public static final String GAME_TOOL_ROLE_ERROR_6 = "GAME TOOL-update game update role info error, update roleName6 is error";
	public static final String GAME_TOOL_ROLE_ERROR_7 = "GAME TOOL-update game update role info error, update roleName7 is error";
	public static final String GAME_TOOL_ROLE_ERROR_8 = "GAME TOOL-update game update role info error, update roleName8 is error";
	
	//修改惩罚配置
	public static final String GAME_UPDATE_PUNISH_CONFIG_1 = "GAME TOOL-update update punish XML config.";
	public static final String GAME_UPDATE_PUNISH_CONFIG_2 = "GAME TOOL-update update punish XML config error.";
	
	//修改XML config
	public static final String GAME_TOOL_UPDATE_XML_CONFIG_1 = "GAME TOOL-update XML config.";
	public static final String GAME_TOOL_UPDATE_XML_CONFIG_2 = "GAME TOOL-update XML config is error ";
	
	//修改活动配置
	public static final String GAME_TOOL_MODIFY_PROGRAM_1 = "GAME TOOL-update progran XML config error, params error.";
	public static final String GAME_TOOL_MODIFY_PROGRAM_2 = "GAME TOOL-update progran XML config error, db error.";
	
	public static final String GAME_TOOL_RETRANSFER_APPSTORE_CHARGE_1 = "GAME-TOOL retransfer appstore charge success.";
	public static final String GAME_TOOL_RETRANSFER_APPSTORE_CHARGE_2 = "GAME-TOOL retransfer appstore charge error because primary key is null or error";
	public static final String GAME_TOOL_RETRANSFER_APPSTORE_CHARGE_3 = "GAME-TOOL retransfer appstore charge error, because dont hava this record.";
	public static final String GAME_TOOL_RETRANSFER_APPSTORE_CHARGE_4 = "GAME-TOOL retransfer appstore charge error, because this record is not need retransfer.";

	public static final String GAME_TOOL_BOX_ERROR_1 = "GAME TOOL-update box type is error.";
	public static final String GAME_TOOL_BOX_ERROR_2 = "GAME TOOL-update box chargeNo is error.";
	public static final String GAME_TOOL_BOX_ERROR_3 = "GAME TOOL-update box dao insert is error.";
	public static final String GAME_TOOL_BOX_ERROR_4 = "GAME TOOL-update box dao update is error.";
	public static final String GAME_TOOL_BOX_ERROR_5 = "GAME TOOL-update box showid is error.";
	
	public static final String GAME_TOOL_OP_ACT_ERROR_1 = "GAME TOOL-update opAct type is error.";
	public static final String GAME_TOOL_OP_ACT_ERROR_2 = "GAME TOOL-update opAct dao insert is error.";
	public static final String GAME_TOOL_OP_ACT_ERROR_3 = "GAME TOOL-update opAct dao update is error.";
	public static final String GAME_TOOL_OP_ACT_ERROR_4 = "GAME TOOL-update opAct reward size is error.";
	
	public static final String GAME_CHANGE_TITLE_ERROR_1 = "GAME TOOL-change role title is error.";
}
