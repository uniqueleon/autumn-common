package org.aztec.autumn.common.utils.sql;

import org.aztec.autumn.common.utils.sql.impl.GenerationParam;

public interface ShardingSqlGenerator {

	public static enum SqlType{
		QUERY,INSERT,UPDATE,CREAT_TABLE;
	}
	
	public String generate(GenerationParam param);
	
}
