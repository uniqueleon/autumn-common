package org.aztec.autumn.common.utils.net.rpc.entity;

import java.io.Serializable;

public class RpcInvokeResult implements Serializable{

	
	private ResultDescriptor descriptor;
	private Object result;
	private Throwable exception;
	
	public RpcInvokeResult(Object result, Throwable exception) {
		super();
		this.result = result;
		this.exception = exception;
	}
	public Object getResult() {
		return result;
	}
	public void setResult(Object result) {
		this.result = result;
	}
	public Throwable getException() {
		return exception;
	}
	public void setException(Throwable exception) {
		this.exception = exception;
	}
	
	
}
