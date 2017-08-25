package com.snail.webgame.game.dao.typehandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.snail.webgame.game.common.EquipRecord;
import com.snail.webgame.game.common.JsonMapper;

public class EquipRecordTypeHandle implements TypeHandler<Map<Integer, EquipRecord>> {

	private JsonMapper mapper = JsonMapper.nonEmptyMapper();
	private TypeReference<Map<Integer, EquipRecord>> typeRef = new TypeReference<Map<Integer, EquipRecord>>() {
	};

	@Override
	public void setParameter(PreparedStatement ps, int i, Map<Integer, EquipRecord> parameter, JdbcType jdbcType)
			throws SQLException {
		ps.setString(i, mapper.toJson(parameter));
	}

	@Override
	public Map<Integer, EquipRecord> getResult(ResultSet rs, String columnName) throws SQLException {
		String jsonStr = rs.getString(columnName);
		if (StringUtils.isNotBlank(jsonStr)) {
			return mapper.fromJson(jsonStr, typeRef);
		}
		return null;
	}

	@Override
	public Map<Integer, EquipRecord> getResult(ResultSet rs, int columnIndex) throws SQLException {
		String jsonStr = rs.getString(columnIndex);
		if (StringUtils.isNotBlank(jsonStr)) {
			return mapper.fromJson(jsonStr, typeRef);
		}
		return null;
	}

	@Override
	public Map<Integer, EquipRecord> getResult(CallableStatement cs, int columnIndex) throws SQLException {
		String jsonStr = cs.getString(columnIndex);
		if (StringUtils.isNotBlank(jsonStr)) {
			return mapper.fromJson(jsonStr, typeRef);
		}
		return null;
	}

	public static void main(String[] args) {
		// 测试极限长度
		JsonMapper mapper = JsonMapper.nonEmptyMapper();
		Map<Integer, EquipRecord> map = new HashMap<Integer, EquipRecord>();

		EquipRecord equip = new EquipRecord();
		equip.setId(1000000000);
		equip.setEquipNo(1000000000);
		equip.setEquipLevel(1000000000);
		// 5个宝石
		equip.getStoneMap().put(1000000000, 1000000000);
		equip.getStoneMap().put(1000000001, 1000000000);
		equip.getStoneMap().put(1000000002, 1000000000);
		equip.getStoneMap().put(1000000003, 1000000000);
		equip.getStoneMap().put(1000000004, 1000000000);

		// 6个装备
		map.put(1000000000, equip);
		map.put(1000000001, equip);
		map.put(1000000002, equip);
		map.put(1000000003, equip);
		map.put(1000000004, equip);
		map.put(1000000005, equip);
		
		String result = mapper.toJson(map);
		// length:1255
		System.out.print("length:" + result.length());
	}
}