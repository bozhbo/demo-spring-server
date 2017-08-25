package com.snail.webgame.game.dao.typehandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

public class IntegerMapTypeHandler implements TypeHandler<Map<Integer, Integer>> {

	@Override
	public void setParameter(PreparedStatement ps, int i, Map<Integer, Integer> parameter, JdbcType jdbcType)
			throws SQLException {
		ps.setString(i, getString(parameter));
	}

	@Override
	public Map<Integer, Integer> getResult(ResultSet rs, String columnName) throws SQLException {
		return getString(rs.getString(columnName));
	}

	@Override
	public Map<Integer, Integer> getResult(ResultSet rs, int columnIndex) throws SQLException {
		return getString(rs.getString(columnIndex));
	}

	@Override
	public Map<Integer, Integer> getResult(CallableStatement cs, int columnIndex) throws SQLException {
		return getString(cs.getString(columnIndex));
	}

	public static String getString(Map<Integer, Integer> map) {
		if (map != null && map.size() > 0) {
			StringBuffer buff = new StringBuffer();
			for (int key : map.keySet()) {
				if (buff.length() > 0) {
					buff.append(";");
				}
				buff.append(key).append(",").append(map.get(key));
			}
			return buff.toString();
		}
		return "";
	}

	public static Map<Integer, Integer> getString(String str) throws SQLException {
		Map<Integer, Integer> result = null;
		if (str != null && str.length() > 0) {
			result = new HashMap<Integer, Integer>();
			String[] infos = str.split(";");
			for (String info : infos) {
				String[] keys = info.split(",");
				if (keys.length < 2) {
					continue;
				}
				int key = NumberUtils.toInt(keys[0]);
				int val = NumberUtils.toInt(keys[1]);
				result.put(key, val);
			}
		}
		return result;
	}
}
