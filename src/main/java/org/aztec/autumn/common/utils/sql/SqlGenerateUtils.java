package org.aztec.autumn.common.utils.sql;

import org.aztec.autumn.common.utils.StringUtils;

public class SqlGenerateUtils {

	public SqlGenerateUtils() {
		// TODO Auto-generated constructor stub
	}
	
	
	public static String generateMultiTableSql(String rawSql,String tableName,String seperator,int[] range,int digitNum) {
		StringBuilder sqlBuilder = new StringBuilder();

		int tableSize = range[1] - range[0] + 1;
		String[] tableNames = new String[tableSize];
		for(int i = 0;i < tableSize;i++) {
			int seqNo = range[0] + i;
			tableNames[i] = tableName + seperator + StringUtils.padding(true, "" + seqNo, digitNum, '0');
		}
		for(String tname : tableNames) {
			if(sqlBuilder.length() != 0) {
				sqlBuilder.append(" UNION ALL ");
			}
			sqlBuilder.append(rawSql.replace(tableName, tname));
		}
		return sqlBuilder.toString();
	}
	
	
	public static void main(String[] args) {
		String testSql = generateMultiTableSql("select * from base_item where partner_id = 11377", "base_item", "_", new int[] {0,60}, 4);
		System.out.println(testSql);
	}

}
