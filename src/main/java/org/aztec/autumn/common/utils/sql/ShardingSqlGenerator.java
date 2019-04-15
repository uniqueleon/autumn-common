package org.aztec.autumn.common.utils.sql;

import org.aztec.autumn.common.utils.sql.impl.GenerationParam;

public interface ShardingSqlGenerator {

	public String generate(GenerationParam param);
	
}
