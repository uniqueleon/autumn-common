package org.aztec.autumn.common.math;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class HashAlgorithm {
	
	private int modular;
	private int modular2;
	private static AtomicInteger conflictCount = new AtomicInteger(0);
	private static Random random = new Random();

	public HashAlgorithm(int modular) {
		// TODO Auto-generated constructor stub
		this.modular = modular;
		this.modular2 = modular - 2;
	}
	
	public int doubleHash(boolean[] places,int id) {
		int hVal = id % modular;
		int gVal = (id + 1) % modular2;
		if(gVal == 0) {
			return hash(places, id);
		}
		int hashVal = hVal;
		while(isConflict(hashVal, places)) {
			hashVal = (hashVal + gVal) % modular;
		}
		return hashVal;
	}
	
	public boolean isConflict(int hash,boolean[] placeHolders) {
		boolean conflict = placeHolders[hash];
		if(conflict) {
			int cc = conflictCount.incrementAndGet();
			System.out.println("conflict count :" + cc);
			
		}
		return conflict;
	}

	public boolean hasNoPlace(boolean[] placeHolders) {
		boolean hasPlace = true;
		for(boolean flag : placeHolders) {
			if(!flag)
				return hasPlace;
		}
		return false;
	}
	
	public int hash(boolean[] placeHolders,int id) {
		int hashVal = id % modular;
		while(isConflict(hashVal, placeHolders)) {
			hashVal = (hashVal + 1 ) % modular;
		}
		return hashVal;
	}
	
	public static void main(String[] args) {
		int mode = 1;
		int placeNum = 59;
		int idRange = 3000;
		int testNum = 59;
		boolean[] places = new boolean[placeNum];
		Long curTime = System.currentTimeMillis();
		HashAlgorithm algorithm = new HashAlgorithm(placeNum);
		for(int i = 0;i < testNum;i++) {
			int hashVal = -1;
			int randomId = random.nextInt(idRange);
			switch(mode) {
			case 1:
				hashVal = algorithm.hash(places, randomId);
				break;
			case 2:
				hashVal = algorithm.doubleHash(places, randomId);
				break;
			}
			places[hashVal] = true;
		}
		Long usedTime = System.currentTimeMillis() - curTime;
		System.out.println("use time:" + usedTime);
	}
}
