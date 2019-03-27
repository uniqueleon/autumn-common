package org.aztec.autumn.common.zk;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Stat;
import org.aztec.autumn.common.GlobalConst;
import org.aztec.autumn.common.zk.DataMonitor.DataMonitorListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class ZkNode implements DataMonitorListener{

	private String dataID;
	private String znode;
	private String dataStr = "";
	private DataMonitor monitor;
	private boolean isDeprecated = false;
	private ZooKeeper zk;
	private Stat stat;
	private static final Logger LOG = LoggerFactory.getLogger(ZkNode.class);
	
	public ZkNode(String dataID) throws IOException, KeeperException, InterruptedException {
		zk = ZkConnector.getKeeper();
		this.dataID = dataID;
		znode = dataID.replace(".", "/");
		znode = "/" + znode;
		monitor = new DataMonitor(zk, znode, BaseWatcher.getWatcher(), this);
		stat = zk.exists(znode, true);
	}
	
	protected void init()  {
		try {
			exists(zk.getData(znode, monitor, stat));
		} catch (Exception e) {
			isDeprecated = true;
			dataStr = "";
		}
	}

	@Override
	public void exists(byte[] data) {
		// TODO Auto-generated method stub
		if(data != null) {
			try {
				this.dataStr = new String(data,GlobalConst.DEFAULT_CHARSET);
			} catch (UnsupportedEncodingException e) {
				LOG.error(e.getMessage(),e);
			}
			try {
				notifyChanges();
			} catch (Exception e) {
				LOG.error(e.getMessage(),e);
			}
		}
	}

	public String getDataID() {
		return dataID;
	}

	public void setDataID(String dataID) {
		this.dataID = dataID;
	}

	public String getDataStr() {
		return dataStr;
	}
	
	public String getDataStrWithVersion(int version) throws KeeperException, InterruptedException {
		Stat statQuery = new Stat(stat.getCzxid(),stat.getMzxid(),stat.getCtime(),stat.getMtime(),version,stat.getCversion(),stat.getAversion(),stat.getEphemeralOwner(),stat.getDataLength(),stat.getNumChildren(),stat.getPzxid());
		statQuery.setVersion(version);
		return new String(zk.getData(znode, false, statQuery));
	}

	public void setDataStr(String dataStr) {
		this.dataStr = dataStr;
	}

	@Override
	public void closing(int rc) {
		// TODO Auto-generated method stub
		this.isDeprecated = true;
		synchronized (this) {
			notifyAll();
		}
	}

	public boolean isDeprecated() {
		return isDeprecated;
	}
	
	public void rewrite() throws UnsupportedEncodingException, KeeperException, InterruptedException {
		zk.setData(znode, dataStr.getBytes(GlobalConst.DEFAULT_CHARSET), -1);
	}

	public int getCurVersion() {
		return stat.getVersion();
	}
	
	public void write(String newData) throws UnsupportedEncodingException, KeeperException, InterruptedException {
		if(zk.exists(znode, false) == null){
			List<ACL> aclList = new ArrayList<>();
			aclList.add(new ACL(ZooDefs.Perms.ALL, ZooDefs.Ids.AUTH_IDS));
			zk.create(znode, newData.getBytes(GlobalConst.DEFAULT_CHARSET),aclList,CreateMode.PERSISTENT);
		}
		this.dataStr = newData;
		zk.setData(znode, newData.getBytes(GlobalConst.DEFAULT_CHARSET), -1);
	}
	
	protected abstract void notifyChanges() throws Exception;
	
	
}
