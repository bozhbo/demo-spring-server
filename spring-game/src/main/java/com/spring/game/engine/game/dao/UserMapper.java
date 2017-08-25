package com.snail.webgame.engine.game.dao;

import com.snail.webgame.engine.game.temp.TempRoleInfo;

/**
 * 
 * 类介绍:用于mybatis配置，暂时忽略此类
 *
 * @author zhoubo
 * @version 1.0.0
 * @since 2016年1月21日
 */
public interface UserMapper {

	TempRoleInfo getRoleInfoById(int roleId);
	
}
