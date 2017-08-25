package com.snail.webgame.game.dao.typehandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

public class IntegerListTypeHandler implements TypeHandler<List<Integer>> {

	@Override
	public void setParameter(PreparedStatement ps, int i, List<Integer> parameter, JdbcType jdbcType)
			throws SQLException {
		ps.setString(i, getString(parameter));
	}

	@Override
	public List<Integer> getResult(ResultSet rs, String columnName) throws SQLException {
		return getString(rs.getString(columnName));
	}

	@Override
	public List<Integer> getResult(ResultSet rs, int columnIndex) throws SQLException {
		return getString(rs.getString(columnIndex));
	}

	@Override
	public List<Integer> getResult(CallableStatement cs, int columnIndex) throws SQLException {
		return getString(cs.getString(columnIndex));
	}

	public static String getString(List<Integer> list) {
		if (list != null && list.size() > 0) {
			StringBuffer buff = new StringBuffer();
			for (int key : list) {
				if (buff.length() > 0) {
					buff.append(",");
				}
				buff.append(key);
			}
			return buff.toString();
		}
		return "";
	}

	public static List<Integer> getString(String str) throws SQLException {
		List<Integer> result = null;
		if (str != null && str.length() > 0) {
			result = new ArrayList<Integer>();
			String[] vals = str.split(",");
			for (String val : vals) {
				result.add(Integer.parseInt(val));
			}
		}
		return result;
	}

	public static void main(String[] args) {
		// 测试极限长度
		//极限200
		List<Integer> list=new ArrayList<Integer>();
		for(int i=0;i<200;i++){
			list.add(1000000000);
		}
				
		String result = getString(list);
		// length:2199
		System.out.print("length:" + result.length());
	}
}
