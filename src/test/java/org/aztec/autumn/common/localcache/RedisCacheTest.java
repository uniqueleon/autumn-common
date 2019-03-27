package org.aztec.autumn.common.localcache;

import java.util.Random;

import org.aztec.autumn.common.utils.SerializationUtils;
import org.aztec.autumn.common.utils.TimeCalculator;

import redis.clients.jedis.Jedis;

public class RedisCacheTest {

	private static void testPerformance(int testSize) throws Exception{
		Random random = new Random();
		Jedis jedis = new Jedis("127.0.0.1", 6379);
		String prefixKey = "lm_test_redis_";
		TimeCalculator calculator1 = new TimeCalculator();
		for(int i = 0 ;i < testSize;i++){
			UserEntity usrEntity = new UserEntity(new Long(i), "name-"+ i, "account-" +i ,random.nextInt(40) , random.nextInt(1) == 1 ? true : false);
			calculator1.start();
			String key = prefixKey + i;
			jedis.set(key.getBytes(), SerializationUtils.serialize(usrEntity));
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
			Object usrEntity =   SerializationUtils.deserialize(jedis.get(key.getBytes()));
			/*if(usrEntity == null){
				System.out.println("missing index:" + id);
			}*/
			calculator2.stop(usrEntity != null);
			calculator2.calculate();
		}

		System.out.println(">>>>>>>>>Getting cache performance test<<<<<<<<<<<<<");
		System.out.println(calculator2);

		System.out.println(">>>>>>>>>Getting cache performance test<<<<<<<<<<<<<");
		jedis.close();
	}
	

	
	public static void main(String[] args) {
		try {
			testPerformance(10000);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
