package org.aztec.autumn.common.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConcurrentUtils {

	public ConcurrentUtils() {
		// TODO Auto-generated constructor stub
	}

	public static ExecutorService createExecutorService(int threadNum){
		
		return Executors.newFixedThreadPool(threadNum);
		//return Executors.newWorkStealingPool();
	}
}
