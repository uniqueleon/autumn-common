package org.aztec.autumn.common.utils.net.rpc.entity;

import java.io.Serializable;

public class ResultDescriptor implements Serializable{

	private int typeCode;
	private long totalLength = 0;
	private int dataPackagNum = 0;
	private int dataPackageLenght = 0;
	private int dataPackageNo = 0;
	
	private ResultDescriptor(int typeCode, long totalLength, int dataPackagNum,
			int dataPackageLenght, int dataPackageNo) {
		super();
		this.typeCode = typeCode;
		this.totalLength = totalLength;
		this.dataPackagNum = dataPackagNum;
		this.dataPackageLenght = dataPackageLenght;
		this.dataPackageNo = dataPackageNo;
	}

	public int getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(int typeCode) {
		this.typeCode = typeCode;
	}

	public long getTotalLength() {
		return totalLength;
	}

	public void setTotalLength(long totalLength) {
		this.totalLength = totalLength;
	}

	public int getDataPackagNum() {
		return dataPackagNum;
	}

	public void setDataPackagNum(int dataPackagNum) {
		this.dataPackagNum = dataPackagNum;
	}

	public int getDataPackageLenght() {
		return dataPackageLenght;
	}

	public void setDataPackageLenght(int dataPackageLenght) {
		this.dataPackageLenght = dataPackageLenght;
	}

	public int getDataPackageNo() {
		return dataPackageNo;
	}

	public void setDataPackageNo(int dataPackageNo) {
		this.dataPackageNo = dataPackageNo;
	}
	
}
