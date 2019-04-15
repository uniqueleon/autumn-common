package org.aztec.autumn.common.zk;

import org.aztec.autumn.common.zk.ZkConfig.ConfigFormat;

public class ZkConfigTest {

	public ZkConfigTest() {
		// TODO Auto-generated constructor stub
	}
	
	public static void main(String[] args) {
		try {
			ZkConfig zkConf = new ZkConfig("com.aztec", ConfigFormat.TEXT);
			System.out.println(zkConf.getDataStr());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
