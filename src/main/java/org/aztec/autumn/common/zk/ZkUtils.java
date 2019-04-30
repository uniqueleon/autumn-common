package org.aztec.autumn.common.zk;

import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

public class ZkUtils {

	public ZkUtils() {
		// TODO Auto-generated constructor stub
	}

	public static boolean isNodeExists(String node) {
		String path = "/" + node.replaceAll("\\.", "/");
		ZooKeeper zk = ZkConnector.getKeeper();
		try {
			Stat stat = zk.exists(path, true);
			return stat != null;
		} catch (Exception e) {
			return false;
		}
	}
	
	public static void main(String[] args) {
		System.out.println(isNodeExists("com.aztec.deadsea.meta.auth"));
	}
}
