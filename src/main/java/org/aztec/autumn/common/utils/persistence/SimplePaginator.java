package org.aztec.autumn.common.utils.persistence;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.aztec.autumn.common.GlobalConst;

public class SimplePaginator<T> implements Paginator<T>,Serializable {

	private int pageNo = 0;
	private int pageSize = GlobalConst.DEFAULT_PAGE_SIZE;
	private int lastPage = 1;
	private int totalSize = 0;
	private List<T> data = new ArrayList<T>();
	
	public SimplePaginator() {
		// TODO Auto-generated constructor stub
	}
	
	public SimplePaginator(int pageNo,int pageSize){
		this.pageNo = pageNo;
		this.pageSize = pageSize;
	}

	@Override
	public int getCurrentPage() {
		return pageNo;
	}


	@Override
	public int getPageSize() {
		// TODO Auto-generated method stub
		return pageSize;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}


	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public List<T> getData() {
		return data;
	}

	public void setData(List<T> data) {
		this.data = data;
	}

	@Override
	public int getLastPage() {
		return lastPage;
	}

	@Override
	public void setLastPage(int lastPage) {
		this.lastPage = lastPage;
	}

	public int getTotalSize() {
		return totalSize;
	}

	public void setTotalSize(int totalSize) {
		this.totalSize = totalSize;
	}

	@Override
	public void setCurrentPage(int pageNo) {
		this.pageNo = pageNo;
	}

}
