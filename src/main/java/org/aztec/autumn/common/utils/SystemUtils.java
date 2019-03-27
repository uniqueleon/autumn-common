package org.aztec.autumn.common.utils;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;

public class SystemUtils {

	public SystemUtils() {
		// TODO Auto-generated constructor stub
	}
	
	public static long getAvailableMemory(){
		MemoryMXBean memorymbean = ManagementFactory.getMemoryMXBean();
		MemoryUsage usage = memorymbean.getHeapMemoryUsage();
		return usage.getMax() - usage.getUsed();
	}
	
	public static void main(String[] args) {
		System.out.println(getAvailableMemory());
	}

}
