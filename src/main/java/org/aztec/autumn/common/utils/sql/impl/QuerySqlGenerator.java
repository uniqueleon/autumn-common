package org.aztec.autumn.common.utils.sql.impl;

import org.aztec.autumn.common.utils.sql.ShardingSqlGenerator;

public class QuerySqlGenerator implements ShardingSqlGenerator {

	public QuerySqlGenerator() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String generate(GenerationParam param) {
		StringBuilder builder = new StringBuilder(param.getRawSql());
		String rawSql = param.getRawSql();
		String newSql = rawSql;
		boolean noDbSelected = (param.getDbName() == null);
		int dbSize = noDbSelected ? 1 : param.getGeneratedDbNames().length;
		for(int i = 0;i < dbSize;i++) {
			for(int j = 0;j < param.getGeneratedTableNames().length;j++){
				if(builder.length() == 0) {
					builder.append(" UNION ALL ");
				}
				String tableName = param.getGeneratedTableNames()[j];
				String dbName = noDbSelected ? "" : param.getGeneratedDbNames()[i];
				if(!noDbSelected) {
					builder.append(rawSql.replace(param.getDbName(), dbName));
				}
				builder.append(rawSql.replace(param.getTableName(), tableName));
			}
		}
		return builder.toString();
	}

}
