package com.spring.world.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class RoleDao {
	
	private static final Log logger = LogFactory.getLog(RoleDao.class);

	private BaseDao baseDao;

	public void test() {
		JdbcTemplate jdbcTemplate = baseDao.getJdbcTemplate();

		int count = jdbcTemplate.queryForObject("select count(*) from nx_roles ", Integer.class);

		System.out.println(count);
	}

	public void insert1(final String name) {
		JdbcTemplate jdbcTemplate = baseDao.getJdbcTemplate();
		int row = jdbcTemplate.update("insert into nx_roles (name) values (?)", name);
		
		System.out.println(row);
	}

	public void insert2(final String name) {
		JdbcTemplate jdbcTemplate = baseDao.getJdbcTemplate();
		int row = jdbcTemplate.update("insert into nx_sns (name) values (?)", name);
		
		System.out.println(row);
	}
	
	public void updateRoleGold(final int roleId, final int gold) {
		try {
			JdbcTemplate jdbcTemplate = baseDao.getJdbcTemplate();
			jdbcTemplate.update("update nx_roles set gold = ? where id = ? ", gold, roleId);
		} catch (DataAccessException e) {
			logger.error("updateRoleGold failed", e);
		}
	}

	@Autowired
	public void setBaseDao(BaseDao baseDao) {
		this.baseDao = baseDao;
	}
}
