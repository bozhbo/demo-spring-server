package com.snail.webgame.game.dao.typehandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.snail.webgame.game.common.JsonMapper;
import com.snail.webgame.game.info.MineHelpRole;

public class MineHeroRoleTypeHandler implements TypeHandler<Map<Integer, MineHelpRole>> {

	private JsonMapper mapper = JsonMapper.nonEmptyMapper();
	private TypeReference<Map<Integer, MineHelpRole>> typeRef = new TypeReference<Map<Integer, MineHelpRole>>() {
	};

	@Override
	public void setParameter(PreparedStatement ps, int i, Map<Integer, MineHelpRole> parameter, JdbcType jdbcType)
			throws SQLException {
		ps.setString(i, mapper.toJson(parameter));
	}

	@Override
	public Map<Integer, MineHelpRole> getResult(ResultSet rs, String columnName) throws SQLException {
		String jsonStr = rs.getString(columnName);
		if (StringUtils.isNotBlank(jsonStr)) {
			return mapper.fromJson(jsonStr, typeRef);
		}
		return null;
	}

	@Override
	public Map<Integer, MineHelpRole> getResult(ResultSet rs, int columnIndex) throws SQLException {
		String jsonStr = rs.getString(columnIndex);
		if (StringUtils.isNotBlank(jsonStr)) {
			return mapper.fromJson(jsonStr, typeRef);
		}
		return null;
	}

	@Override
	public Map<Integer, MineHelpRole> getResult(CallableStatement cs, int columnIndex) throws SQLException {
		String jsonStr = cs.getString(columnIndex);
		if (StringUtils.isNotBlank(jsonStr)) {
			return mapper.fromJson(jsonStr, typeRef);
		}
		return null;
	}
}
