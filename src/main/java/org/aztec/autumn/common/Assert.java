package org.aztec.autumn.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Assert {

	private static final Logger Log = LoggerFactory.getLogger("systemAssert");
	
	public Assert() {
		// TODO Auto-generated constructor stub
	}
	
	public static void notNull(Object... targetObject) throws AssertionException{
		if(targetObject != null && targetObject.length > 0){
			for(int i = 0;i < targetObject.length;i++){
				if(targetObject[i] == null){
					log("Object(index=" + i + ") is null");
					throw new AssertionException("Assert Object is null");
				}
			}
		}
	}
	
	private static void log(String message){
		StackTraceElement[] stacks = Thread.currentThread().getStackTrace();
		Log.error(message);
		for(StackTraceElement stack : stacks){
			Log.error("\t at:" + stack.getClassName() + "#" + stack.getMethodName() + "(" + stack.getLineNumber() + ")");
		}
	}

}
