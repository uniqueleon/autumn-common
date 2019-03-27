package org.aztec.autumn.common.utils.persistence;

import java.io.Serializable;
import java.util.List;

public interface Paginator<T> extends Serializable{

	public int getCurrentPage();
	public void setCurrentPage(int pageNo);
	public void setTotalSize(int totalSize);
	public int getTotalSize();
	public List<T> getData();
	public void setData(List<T> data);
	public int getPageSize();
	public void setPageSize(int pageSize);
	public int getLastPage();
	public void setLastPage(int lastPage);
}
