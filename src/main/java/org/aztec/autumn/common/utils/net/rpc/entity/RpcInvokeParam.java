package org.aztec.autumn.common.utils.net.rpc.entity;

import java.io.Serializable;
import java.util.Arrays;

public class RpcInvokeParam implements Serializable{

	private String className;
	private String methodName;
	private String[] methodParamTypes;
	private String retType;
	private Object[] params;
	
	public RpcInvokeParam(String className, String methodName,
			String[] methodParamTypes, String retType, Object[] params) {
		super();
		this.className = className;
		this.methodName = methodName;
		this.methodParamTypes = methodParamTypes;
		this.retType = retType;
		this.params = params;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public String[] getMethodParamTypes() {
		return methodParamTypes;
	}

	public void setMethodParamTypes(String[] methodParamTypes) {
		this.methodParamTypes = methodParamTypes;
	}

	public String getRetType() {
		return retType;
	}

	public void setRetType(String retType) {
		this.retType = retType;
	}

	public Object[] getParams() {
		return params;
	}

	public void setParams(Object[] params) {
		this.params = params;
	}

	@Override
	public String toString() {
		return "RpcInvokeParam [className=" + className + ", methodName="
				+ methodName + ", methodParamTypes="
				+ Arrays.toString(methodParamTypes) + ", retType=" + retType
				+ ", params=" + Arrays.toString(params) + "]";
	}
	
}
