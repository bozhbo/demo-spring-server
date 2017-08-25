package com.snail.webgame.engine.game.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * 类介绍: 数据库操作帮助类
 *
 * @author zhoubo
 * @version 1.0.0
 * @since 2016年1月21日
 */
public class BaseSuperDao {

	private Map<String, String> sqlMap = null;
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * 执行数据库事务操作，保证多条操作在同一事务内
	 * 
	 * @param superDaoRequests	数据库操作
	 * @throws Exception
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor=Exception.class)
	public void execute(SuperDaoRequest ... superDaoRequests) throws Exception {
		if (superDaoRequests != null) {
			for (SuperDaoRequest superDaoRequest : superDaoRequests) {
				switch (superDaoRequest.getSqlType()) {
				case UPDATE:
					this.update(superDaoRequest.getSql(), superDaoRequest.getObjs(), superDaoRequest.getTypes());
					break;
				case UPDATE_BATCH:
					this.updateBatch(superDaoRequest.getSql(), superDaoRequest.getBatch(), superDaoRequest.getTypes());
					break;
				case INSERT_FORKEY:
					int key = this.insertForKey(superDaoRequest.getSql(), superDaoRequest.getKey(), superDaoRequest.getObjs(), superDaoRequest.getTypes());
					superDaoRequest.getResults()[0] = key;
					break;
				case INSERT_FORKEY_BATCH:
					superDaoRequest.setResults(this.insertBatchForKey(superDaoRequest.getSql(), superDaoRequest.getKey(), superDaoRequest.getBatch(), superDaoRequest.getTypes()));
					break;
				default:
					break;
				}
			}
		}
	}
	
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor=Exception.class)
	public void update(String sql, Object[] objs) {
		jdbcTemplate.update(sql, objs);
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor=Exception.class)
	public void update(String sql, Object[] objs, int[] types) {
		jdbcTemplate.update(sql, objs, types);
	}
	
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor=Exception.class)
	public void updateBatch(String sql, List<Object[]> batch) {
		jdbcTemplate.batchUpdate(sql, batch);
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor=Exception.class)
	public void updateBatch(String sql, List<Object[]> batch, int[] types) {
		jdbcTemplate.batchUpdate(sql, batch, types);
	}
	
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor=Exception.class)
	public void delete(String sql, Object[] objs) {
		this.update(sql, objs);
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor=Exception.class)
	public void delete(String sql, Object[] objs, int[] types) {
		this.update(sql, objs, types);
	}
	
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor=Exception.class)
	public void deleteBatch(String sql, List<Object[]> batch) {
		this.updateBatch(sql, batch);
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor=Exception.class)
	public void deleteBatch(String sql, List<Object[]> batch, int[] types) {
		this.updateBatch(sql, batch, types);
	}
	
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor=Exception.class)
	public void insert(final String sql, final Object[] objs) {
		this.update(sql, objs);
	}
	
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor=Exception.class)
	public void insert(final String sql, final Object[] objs, final int[] types) {
		this.update(sql, objs, types);
	}
	
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor=Exception.class)
	public int insertForKey(final String sql, final String keyName, final Object[] objs, final int[] types) {
		KeyHolder keyHolder = new GeneratedKeyHolder();

		jdbcTemplate.update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(sql, new String[] { keyName });
				for (int i = 0; i < types.length; i++) {
					switch (types[i]) {
					case Types.INTEGER:
						ps.setInt(i + 1, (Integer)objs[i]);
						break;
					case Types.TINYINT:
						ps.setByte(i + 1, (Byte)objs[i]);
						break;
					case Types.SMALLINT:
						ps.setShort(i + 1, (Short)objs[i]);
						break;
					case Types.BIGINT:
						ps.setLong(i + 1, (Long)objs[i]);
						break;
					case Types.VARCHAR:
						ps.setString(i + 1, (String)objs[i]);
						break;
					case Types.LONGVARCHAR:
						ps.setString(i + 1, (String)objs[i]);
						break;
					case Types.CHAR:
						ps.setString(i + 1, (String)objs[i]);
						break;
					case Types.REAL:
						ps.setFloat(i + 1, (Float)objs[i]);
						break;
					case Types.FLOAT:
						ps.setDouble(i + 1, (Double)objs[i]);
						break;
					case Types.DOUBLE:
						ps.setDouble(i + 1, (Double)objs[i]);
						break;
					case Types.TIMESTAMP:
						ps.setTimestamp(i + 1, (Timestamp)objs[i]);
						break;
					case Types.DATE:
						ps.setDate(i + 1, (Date)objs[i]);
						break;
					case Types.TIME:
						ps.setTime(i + 1, (Time)objs[i]);
						break;
					case Types.BIT:
						ps.setBoolean(i + 1, (Boolean)objs[i]);
						break;
					case Types.VARBINARY :
						ps.setBytes(i + 1, (byte[])objs[i]);
						break;
					case Types.LONGVARBINARY :
						ps.setBytes(i + 1, (byte[])objs[i]);
						break;

					default:
						break;
					}
				}
				
				return ps;
			}
		}, keyHolder);
		
		return keyHolder.getKey().intValue();
	}
	
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor=Exception.class)
	public int[] insertBatchForKey(final String sql, final String keyName, final List<Object[]> batch, final int[] types) throws Exception {
		ResultSet rest = null;
		PreparedStatement ps = null;
		Connection conn = null;
		int[] res = null;
		
		try {
			conn = DataSourceUtils.getConnection(jdbcTemplate.getDataSource());
			
			ps = conn.prepareStatement(sql, new String[] { keyName });
			
			for (Object[] objs : batch) {
				for (int i = 0; i < types.length; i++) {
					switch (types[i]) {
					case Types.INTEGER:
						ps.setInt(i + 1, (Integer)objs[i]);
						break;
					case Types.TINYINT:
						ps.setByte(i + 1, (Byte)objs[i]);
						break;
					case Types.SMALLINT:
						ps.setShort(i + 1, (Short)objs[i]);
						break;
					case Types.BIGINT:
						ps.setLong(i + 1, (Long)objs[i]);
						break;
					case Types.VARCHAR:
						ps.setString(i + 1, (String)objs[i]);
						break;
					case Types.LONGVARCHAR:
						ps.setString(i + 1, (String)objs[i]);
						break;
					case Types.CHAR:
						ps.setString(i + 1, (String)objs[i]);
						break;
					case Types.REAL:
						ps.setFloat(i + 1, (Float)objs[i]);
						break;
					case Types.FLOAT:
						ps.setDouble(i + 1, (Double)objs[i]);
						break;
					case Types.DOUBLE:
						ps.setDouble(i + 1, (Double)objs[i]);
						break;
					case Types.TIMESTAMP:
						ps.setTimestamp(i + 1, (Timestamp)objs[i]);
						break;
					case Types.DATE:
						ps.setDate(i + 1, (Date)objs[i]);
						break;
					case Types.TIME:
						ps.setTime(i + 1, (Time)objs[i]);
						break;
					case Types.BIT:
						ps.setBoolean(i + 1, (Boolean)objs[i]);
						break;
					case Types.VARBINARY :
						ps.setBytes(i + 1, (byte[])objs[i]);
						break;
					case Types.LONGVARBINARY :
						ps.setBytes(i + 1, (byte[])objs[i]);
						break;

					default:
						break;
					}
				}
				
				ps.addBatch();
			}
			
			ps.executeBatch();
			
			rest = ps.getGeneratedKeys();
			int index = 0;
			res = new int[batch.size()];
			
			while (rest.next()) {
				res[index] = rest.getInt(1);
				index++;
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			try {
				if (rest != null) {
					rest.close();
				}
				
				if (ps != null) {
					ps.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		
		return res;
	}
	
	public Object[] toObjects(Object ... objs) {
		return objs;
	}
	
	public Object[] toTypes(Object ... objs) {
		return objs;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public void setSqlMap(Map<String, String> sqlMap) {
		this.sqlMap = sqlMap;
	}

	public Map<String, String> getSqlMap() {
		return sqlMap;
	}

}
