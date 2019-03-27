package org.aztec.autumn.common.utils.sql.impl;

public class GenerationParam {

	public GenerationParam() {
		// TODO Auto-generated constructor stub
	}
	private String rawSql;
	private String dbName;
	private String tableName;
	private String[] generatedDbNames;
	private String[] generatedTableNames;
	private int generatedSize = 0;
	public GenerationParam(String rawSql, String dbName, String tableName, String[] generatedDbNames,
			String[] generatedTableNames) {
		super();
		this.rawSql = rawSql;
		this.dbName = dbName;
		this.tableName = tableName;
		this.generatedDbNames = generatedDbNames;
		this.generatedTableNames = generatedTableNames;
		generatedSize = generatedDbNames.length * generatedTableNames.length;
	}
	public int getGeneratedSize() {
		return generatedSize;
	}
	public void setGeneratedSize(int generatedSize) {
		this.generatedSize = generatedSize;
	}
	public String getRawSql() {
		return rawSql;
	}
	public void setRawSql(String rawSql) {
		this.rawSql = rawSql;
	}
	public String getDbName() {
		return dbName;
	}
	public void setDbName(String dbName) {
		this.dbName = dbName;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String[] getGeneratedDbNames() {
		return generatedDbNames;
	}
	public void setGeneratedDbNames(String[] generatedDbNames) {
		this.generatedDbNames = generatedDbNames;
	}
	public String[] getGeneratedTableNames() {
		return generatedTableNames;
	}
	public void setGeneratedTableNames(String[] generatedTableNames) {
		this.generatedTableNames = generatedTableNames;
	}
	
}
