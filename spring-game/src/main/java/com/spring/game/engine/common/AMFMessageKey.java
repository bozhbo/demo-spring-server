package com.snail.webgame.engine.common;

/**
 * 定义AMF消息数据KEY
 * @author zenggy
 *
 */
public interface AMFMessageKey {
	public static final String MSG_BODY = "Body";
	public static final String MSG_HEADER = "Header";
	public static final String MSG_TYPE = "MsgType";
	public static final String MSG_RESULT = "Result";
	public static final String MSG_ROLE_ID = "RoleID";
	
	public static final String DB_NAME = "dbName";
	public static final String SQL_KEY = "sqlKey";
	public static final String SQL_CMD = "sqlCmd";
	public static final String BASE_TO = "baseTO";
	
	public static final String SQL_CMD_QUERY = "sql_cmd_query";
	public static final String SQL_CMD_INSERT = "sql_cmd_insert";
	public static final String SQL_CMD_UPDATE = "sql_cmd_update";
	public static final String SQL_CMD_DELETE = "sql_cmd_delete";
	
	public static final String STORAGE_HANDLE = "storage_handle";
}
