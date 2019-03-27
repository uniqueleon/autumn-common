package org.aztec.autumn.common.localcache;

import java.io.File;
import java.util.Random;

import org.aztec.autumn.common.utils.TimeCalculator;
import org.aztec.autumn.common.utils.cache.impl.fast.FileCache;

public class FileCacheTest {

	private static void testPerformance(int testSize) throws Exception{
		Random random = new Random();
		FileCache fileCache = new FileCache(new File("testCache.dat"));
		String prefixKey = "lm_test_redis_";
		TimeCalculator calculator1 = new TimeCalculator();
		for(int i = 0 ;i < testSize;i++){
			UserEntity usrEntity = new UserEntity(new Long(i), "name-"+ i, "account-" +i ,random.nextInt(40) , random.nextInt(1) == 1 ? true : false);
			calculator1.start();
			fileCache.put(new Long(i), usrEntity);
			calculator1.stop(true);
			calculator1.calculate();
		}
		System.out.println(">>>>>>>>>Setting cache performance test<<<<<<<<<<<<<");
		System.out.println(calculator1);

		System.out.println(">>>>>>>>>Setting cache performance test<<<<<<<<<<<<<");

		TimeCalculator calculator2 = new TimeCalculator();
		for(int i = 0;i < testSize;i++){
			calculator2.start();
			Long id = new Long(random.nextInt(testSize));
			while(id >= testSize){
				id = new Long(random.nextInt(testSize)); 
			}
			String key = prefixKey + id;
			Object usrEntity =  fileCache.get(id);
			/*if(usrEntity == null){
				System.out.println("missing index:" + id);
			}*/
			calculator2.stop(usrEntity != null);
			//System.out.println(usrEntity);
			calculator2.calculate();
		}

		System.out.println(">>>>>>>>>Getting cache performance test<<<<<<<<<<<<<");
		System.out.println(calculator2);

		System.out.println(">>>>>>>>>Getting cache performance test<<<<<<<<<<<<<");
		fileCache.close();
	}
	

	
	public static void main(String[] args) {
		try {
			testPerformance(10000000);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
