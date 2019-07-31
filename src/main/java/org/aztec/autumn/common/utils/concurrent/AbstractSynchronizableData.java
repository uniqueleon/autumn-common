package org.aztec.autumn.common.utils.concurrent;

import java.util.BitSet;
import java.util.UUID;

import org.aztec.autumn.common.utils.BitSetUtil;
import org.aztec.autumn.common.utils.StringUtils;

public abstract class AbstractSynchronizableData<T> implements Synchronizable<T> {
	
	protected String uuid;
	protected int[] slots;
	protected T data;
	protected T oldData;
	protected String version;
	protected String previousVersion;
	protected int dept;
	protected boolean synchorized = false;
	
	public AbstractSynchronizableData(Cloneable<T> data,int[] slots) {
		this.data = data.cloneThis();
		this.uuid = "" + data.hashCode();
		this.version = generateVersion();
		this.slots = slots;
	}

	public AbstractSynchronizableData(Cloneable<T> data,String uuid,int[] slots) {
		this.data = data.cloneThis();
		this.uuid = uuid;
		this.version = generateVersion();
		this.slots = slots;
	}

	public AbstractSynchronizableData(String uuid, int[] slots, T data, T oldData, String version,
			String previousVersion, int dept, boolean synchorized) {
		super();
		this.uuid = uuid;
		this.slots = slots;
		this.data = data;
		this.oldData = oldData;
		this.version = version;
		this.previousVersion = previousVersion;
		this.dept = dept;
		this.synchorized = synchorized;
	}

	public AbstractSynchronizableData(String uuid, int[] slots, Cloneable<T> data,String version, String nextVersion, String previousVersion,
			int dept) {
		super();
		this.uuid = uuid;
		this.slots = slots;
		this.data = data.cloneThis();
		this.version = version;
		this.previousVersion = previousVersion;
		this.dept = dept;
		this.slots = slots;
	}

	public String generateVersion() {
		return  StringUtils.getRandomCharNumberString(NoLockDataSynchronizer.DEFAULT_VERSION_LENGTH);
	}

	@Override
	public String getUUID() {
		return uuid;
	}

	@Override
	public String getVersion() {
		return version;
	}

	@Override
	public String getPerviousVersion() {
		return previousVersion;
	}

	@Override
	public T getData() {
		return data;
	}

	public int[] getModifySlots() {
		return slots;
	}
	
	

	@Override
	public boolean isMergable(Synchronizable<T> otherNode) {
		return isCompatible(((AbstractSynchronizableData<T>) otherNode).slots);
	}

	public boolean isCompatible(int[] otherSlots) {
		BitSet bs1 = BitSetUtil.array2BitSet(slots);
		BitSet bs2 = BitSetUtil.array2BitSet(otherSlots);
		return bs1.intersects(bs2);
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public int[] getSlots() {
		return slots;
	}

	public void setSlots(int[] slots) {
		this.slots = slots;
	}

	public String getPreviousVersion() {
		return previousVersion;
	}

	public void setPreviousVersion(String previousVersion) {
		this.previousVersion = previousVersion;
	}

	public void setData(T data) {
		this.data = data;
	}

	public void setVersion(String version) {
		this.version = version;
	}


	@Override
	public int getDept() {
		return dept;
	}

	@Override
	public void update(T data) {
		// TODO Auto-generated method stub
		if(synchorized) {
			this.oldData = data;
			this.data = data;
			this.previousVersion = version;
			version = generateVersion();
			synchorized = false;
		}
	}

	public boolean isSynchronized() {
		return synchorized;
	}

	public void setSynchronized(boolean synchorized) {
		this.synchorized = synchorized;
	}

	public T getOldData() {
		return oldData;
	}

	public void setOldData(T oldData) {
		this.oldData = oldData;
	}

	@Override
	public <E extends Synchronizable<T>> E cast() {
		return (E) this;
	}

	
	
}
