package org.aztec.autumn.common;

import java.util.List;

import com.google.common.collect.Lists;


public class SqlUtilTest {
	
	private static String TABLE_NAME_LABEL = "{TABLE_NAME}";
	private static String TABLE_ALIAS_PREFIX = "a_";
	

	public static void main(String[] args) {
		String rawSql = "SELECT * FROM " + TABLE_NAME_LABEL + "";
		
		List<String> conditions = Lists.newArrayList();
		conditions.add("out_order_code='OS20181228009627'");
		//System.out.println(getExecutableSql(rawSql, "wmp_sale_order", 32,64,conditions));
		rawSql = "SELECT * FROM " + TABLE_NAME_LABEL + " where out_order_code='OS20181228009627'";
		System.out.println(generateUnionSql(rawSql, "wmp_sale_order_", 32, 64, conditions));
	}
	
	private static String getExecutableSql(String rawSql,String tableNamePrefix,int lowLimit,int upperLimit
			,List<String> conditions){
		String retSql = null;
		if(rawSql != null){
			if(rawSql.contains(TABLE_NAME_LABEL)){
				retSql = rawSql.replace(TABLE_NAME_LABEL, combineTables(tableNamePrefix, lowLimit,upperLimit));
			}
			if(conditions != null && !conditions.isEmpty()){
				retSql = combineConditions(retSql, conditions, lowLimit,upperLimit);
			}
		}
		return retSql;
	}
	
	private static String generateUnionSql(String rawSql,String tableNamePrefix,int lowLimit,int upperLimit
			,List<String> conditions){
		StringBuilder builder = new StringBuilder();
		
		for(int i = lowLimit;i < upperLimit;i++){
			if(builder.length() != 0){
				builder.append(" UNION ALL ");
			}
			String querySql = rawSql.replace(TABLE_NAME_LABEL, tableNamePrefix + SqlStringUtils.integerPadding(i, 4));
			builder.append(querySql);
		}
		return builder.toString();
	}
	
	
	
	private static String combineTables(String tableNamePrefix,int lowerLimit,int upperLimit){
		StringBuilder sb = new StringBuilder();
	
		sb.append(tableNamePrefix + "_" + SqlStringUtils.integerPadding(lowerLimit, 4) + " as " + TABLE_ALIAS_PREFIX + lowerLimit);
		for(int i = (lowerLimit + 1);i < upperLimit;i++){
			sb.append("," + tableNamePrefix + "_" + SqlStringUtils.integerPadding(i, 4) + " as " + TABLE_ALIAS_PREFIX + i);
		}
		return sb.toString();
	}
	
	private static String combineConditions(String rawSql,List<String> conditions,int lowerLimit,int upperLimit){
		StringBuilder sb = new StringBuilder();
		sb.append(rawSql);
		sb.append(" WHERE ");
		for(int i = 0;i < conditions.size();i++){
			String condition = conditions.get(i);
			if(i != 0){
				sb.append("OR");
			}
			for(int j = lowerLimit;j < upperLimit;j++){
				if(j == lowerLimit){
					sb.append("(");
				}
				else{
					sb.append(" OR ");
				}
				sb.append( " " + TABLE_ALIAS_PREFIX + j + "." +  condition );
				if(j == upperLimit - 1){
					sb.append(")");
				}
			}
			
		}
		return sb.toString();
	}
}
