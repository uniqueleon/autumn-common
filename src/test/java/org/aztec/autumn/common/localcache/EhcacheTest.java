package org.aztec.autumn.common.localcache;

import java.net.URL;
import java.util.Random;

import org.aztec.autumn.common.utils.TimeCalculator;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

public class EhcacheTest {

	
	private static void PerformanceTest(int testSize) throws Exception{
		Random random = new Random();
		
		URL myUrl = EhcacheTest.class.getResource("ehcache-test.xml"); 
		CacheManager myCacheManager = CacheManager.create(myUrl);
		//myCacheManager.addCache("userCache");
		Cache cache = myCacheManager.getCache("userCache");
		TimeCalculator calculator1 = new TimeCalculator();
		for(int i = 0 ;i < testSize;i++){
			UserEntity usrEntity = new UserEntity(new Long(i), "name-"+ i, "account-" +i ,random.nextInt(40) , random.nextInt(1) == 1 ? true : false);
			calculator1.start();
			cache.put(new Element(new Long(i), usrEntity));
			calculator1.stop(true);
			calculator1.calculate();
		}
		System.out.println(">>>>>>>>>Setting cache performance test<<<<<<<<<<<<<");
		System.out.println(calculator1);

		System.out.println(">>>>>>>>>Setting cache performance test<<<<<<<<<<<<<");

		TimeCalculator calculator2 = new TimeCalculator();
		for(int i = 0;i < testSize;i++){
			Long id = new Long(random.nextInt(testSize));
			while(id >= testSize){
				id = new Long(random.nextInt(testSize)); 
			}
			calculator2.start();
			Element usrEntity =  cache.get(id);
			/*if(usrEntity == null){
				System.out.println("missing index:" + id);
			}*/
			calculator2.stop(usrEntity != null);

			calculator2.calculate();
		}

		System.out.println(">>>>>>>>>Getting cache performance test<<<<<<<<<<<<<");
		System.out.println(calculator2);

		System.out.println(">>>>>>>>>Getting cache performance test<<<<<<<<<<<<<");
		//EhCache userCache = 
		//myCacheManager.addCacheIfAbsent(cache);
	}
	
	
	public static void main(String[] args) {
		try {
			PerformanceTest(10000000);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
