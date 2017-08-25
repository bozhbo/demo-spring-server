package com.snail.webgame.engine.game.dao;

import java.util.List;

import com.snail.webgame.engine.game.enums.SqlType;

public class SuperDaoRequest {

	private SqlType sqlType;
	private String sql;
	private Object[] objs;
	private List<Object[]> batch;
	private String key;
	private int[] types;
	private int[] results;
	
	public SuperDaoRequest() {
	}
	
	public SuperDaoRequest(SqlType sqlType, String sql, Object[] objs, int[] types) {
		this.sqlType = sqlType;
		this.sql = sql;
		this.objs = objs;
		this.types = types;
	}
	
	public SuperDaoRequest(SqlType sqlType, String sql, List<Object[]> batch, int[] types) {
		this.sqlType = sqlType;
		this.sql = sql;
		this.batch = batch;
		this.types = types;
	}
	
	public SuperDaoRequest(SqlType sqlType, String sql, Object[] objs, int[] types, String key) {
		this.sqlType = sqlType;
		this.sql = sql;
		this.objs = objs;
		this.types = types;
		this.key = key;
		this.results = new int[1];
	}
	
	public SuperDaoRequest(SqlType sqlType, String sql, List<Object[]> batch, int[] types, String key) {
		this.sqlType = sqlType;
		this.sql = sql;
		this.batch = batch;
		this.types = types;
		this.key = key;
	}

	public SqlType getSqlType() {
		return sqlType;
	}

	public void setSqlType(SqlType sqlType) {
		this.sqlType = sqlType;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public Object[] getObjs() {
		return objs;
	}

	public void setObjs(Object[] objs) {
		this.objs = objs;
	}

	public List<Object[]> getBatch() {
		return batch;
	}

	public void setBatch(List<Object[]> batch) {
		this.batch = batch;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public int[] getTypes() {
		return types;
	}

	public void setTypes(int[] types) {
		this.types = types;
	}

	public int[] getResults() {
		return results;
	}

	public void setResults(int[] results) {
		this.results = results;
	}
	
}
