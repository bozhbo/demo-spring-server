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
import com.snail.webgame.game.common.HeroRecord;
import com.snail.webgame.game.common.JsonMapper;

public class HeroRecordTypeHandler implements TypeHandler<Map<Byte, HeroRecord>> {

	private JsonMapper mapper = JsonMapper.nonEmptyMapper();
	private TypeReference<Map<Byte, HeroRecord>> typeRef = new TypeReference<Map<Byte, HeroRecord>>() {
	};

	@Override
	public void setParameter(PreparedStatement ps, int i, Map<Byte, HeroRecord> parameter, JdbcType jdbcType)
			throws SQLException {
		ps.setString(i, mapper.toJson(parameter));
	}

	@Override
	public Map<Byte, HeroRecord> getResult(ResultSet rs, String columnName) throws SQLException {
		String jsonStr = rs.getString(columnName);
		if (StringUtils.isNotBlank(jsonStr)) {
			return mapper.fromJson(jsonStr, typeRef);
		}
		return null;
	}

	@Override
	public Map<Byte, HeroRecord> getResult(ResultSet rs, int columnIndex) throws SQLException {
		String jsonStr = rs.getString(columnIndex);
		if (StringUtils.isNotBlank(jsonStr)) {
			return mapper.fromJson(jsonStr, typeRef);
		}
		return null;
	}

	@Override
	public Map<Byte, HeroRecord> getResult(CallableStatement cs, int columnIndex) throws SQLException {
		String jsonStr = cs.getString(columnIndex);
		if (StringUtils.isNotBlank(jsonStr)) {
			return mapper.fromJson(jsonStr, typeRef);
		}
		return null;
	}

	public static void main(String[] args) {
		// 测试极限长度
		JsonMapper mapper = JsonMapper.nonEmptyMapper();
		Map<Byte, HeroRecord> map = new HashMap<Byte, HeroRecord>();
		HeroRecord record = new HeroRecord();
		record.setId(1000000000);
		record.setHeroNo(1000000000);
		record.setDeployStatus((byte) 128);
		record.setSoldierLevel(1000000000);
		record.setHeroLevel(1000000000);
		record.setQuality(1000000000);
		// 4个技能
		record.getSkillMap().put(1000000000, 1000000000);
		record.getSkillMap().put(1000000001, 1000000000);
		record.getSkillMap().put(1000000002, 1000000000);
		record.getSkillMap().put(1000000003, 1000000000);

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
		record.getEquipMap().put(1000000000, equip);
		record.getEquipMap().put(1000000001, equip);
		record.getEquipMap().put(1000000002, equip);
		record.getEquipMap().put(1000000003, equip);
		record.getEquipMap().put(1000000004, equip);
		record.getEquipMap().put(1000000005, equip);
		record.setHeroStatus((byte) 128);
		record.setCutHp(1000000000);

		// 5个英雄
		map.put((byte) 1, record);
		map.put((byte) 2, record);
		map.put((byte) 3, record);
		map.put((byte) 4, record);
		map.put((byte) 5, record);

		String result = mapper.toJson(map);
		// length:7806
		System.out.print("length:" + result.length());
	}
}