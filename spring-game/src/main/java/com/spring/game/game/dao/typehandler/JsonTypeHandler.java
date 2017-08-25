package com.snail.webgame.game.dao.typehandler;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import com.snail.webgame.game.common.JsonMapper;

/**
 * 英雄技能保存辅助列 typeHandler 用于保存数据库 技能信息保存为json字符串 返回为Map<String, Object>
 * @author caowl
 *
 */
public abstract class JsonTypeHandler<T> extends BaseTypeHandler<T> {

	protected JsonMapper mapper = JsonMapper.nonEmptyMapper();
	
	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, T parameter, JdbcType jdbcType)
			throws SQLException {
		ps.setString(i, mapper.toJson(parameter));
		
	}
	
}
