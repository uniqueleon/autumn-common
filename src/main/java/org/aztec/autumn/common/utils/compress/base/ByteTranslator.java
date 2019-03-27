package org.aztec.autumn.common.utils.compress.base;

import java.util.Map;

import org.aztec.autumn.common.math.congruence.CongruenceTable;
import org.aztec.autumn.common.math.congruence.CongruenceTable.TableKey;
import org.aztec.autumn.common.utils.compress.Code;
import org.aztec.autumn.common.utils.compress.CodeMapper;

import com.beust.jcommander.internal.Maps;

public class ByteTranslator implements CodeMapper {
	
	private int space = 0;
	private CongruenceTable table;
	private Map<Code,TableKey> mapper = Maps.newHashMap();

	@Override
	public void init(Object[] params) {
		// TODO Auto-generated method stub
		this.table = (CongruenceTable)params[0];
		int product = table.getProduct();
		while(product > 0) {
			product = product / 2;
			space ++;
		}
	}

	@Override
	public byte[] encode(byte[] bite) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public byte[] mappingTable() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public byte[] decode(byte[] table, byte[] datas) {
		// TODO Auto-generated method stub
		return null;
	}

}
