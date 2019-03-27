package org.aztec.autumn.common.utils.jdbc;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataCache {

	private Map<CacheKey, CacheData> cacheDatas = new ConcurrentHashMap<CacheKey, CacheData>();
	private final static long TTL = 1800000;
	private final static int MAX_CACHE_SIZE = 100000;
	private final static long MAINTAINER_SLEEP_INTERVAL = 600000;
	private final static Logger logger = LoggerFactory
			.getLogger(DataCache.class);

	
	public DataCache(){

    Thread maintainer = new CacheMaintainer();
    maintainer.start();
	}

	public boolean isHit(String querySql,Object[] params) {
	  return cacheDatas.containsKey(new CacheKey(querySql,params));
	}

	public <T> T getFromCache(String querySql,Object[] params) {
		return isHit(querySql,params) ? (T) cacheDatas.get(new CacheKey(querySql,params)).getData() : null;
	}

	public void pushToCache(String querySql, Object[] params,Object queryResult) {
		cacheDatas.put(new CacheKey(querySql,params), new CacheData(queryResult, querySql));
	}

	public void clearCache() {
		cacheDatas.clear();
	}

	private class CacheMaintainer extends Thread {

		@Override
		public void run() {
			while (true) {
				try {
					TreeSet<CacheData> sortSet = new TreeSet<CacheData>();
					for (CacheData cacheData : cacheDatas.values()) {
						if (cacheData.isExpired()) {
							logger.info("Authentication data[sqlString="
									+ cacheData.getQuery()
									+ ", born time="
									+ new SimpleDateFormat(
											"yyyy-MM-dd hh:mm:ss")
											.format(new Date(cacheData
													.getBornTime()))
									+ "] is expired!");
							cacheDatas.remove(cacheData.getQuery());
						}
						sortSet.add(cacheData);
					}
					int removedCount = cacheDatas.size() - MAX_CACHE_SIZE;
					for (int i = 0; i < removedCount; i++) {
						logger.info("Authentication data[sqlString="
								+ sortSet.first().getQuery()
								+ ", born time="
								+ new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
										.format(new Date(sortSet.first()
												.getBornTime()))
								+ "] was removed! Because it is the oldest and the cache size is "
								+ cacheDatas.size());
						cacheDatas.remove(sortSet.first().getQuery());
					}
					Thread.currentThread().sleep(MAINTAINER_SLEEP_INTERVAL);
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
			}
		}

	}

	private static class CacheData implements Comparable<CacheData> {
		private Object data;
		private long bornTime;
		private String query;

		public CacheData(Object data, String query) {
			this.data = data;
			this.bornTime = System.currentTimeMillis();
			this.query = query;
		}

		public boolean isExpired() {
			return System.currentTimeMillis() - bornTime > TTL ? true : false;
		}

		public Object getData() {
			return data;
		}

		public long getBornTime() {
			return bornTime;
		}

		public String getQuery() {
			return query;
		}

		@Override
		public int compareTo(CacheData o) {
			return (int) (this.getBornTime() - o.getBornTime());
		}

		@Override
		public String toString() {
			return "CacheData [data=" + data + ", bornTime=" + bornTime
					+ ",query=" + query + "]";
		}

	}
	
	private static class CacheKey{
	  private String query;
	  private Object[] params;
    public String getQuery() {
      return query;
    }
    public void setQuery(String query) {
      this.query = query;
    }
    public Object[] getParams() {
      return params;
    }
    public void setParams(Object[] params) {
      this.params = params;
    }
    public CacheKey(String query, Object[] params) {
      super();
      this.query = query;
      this.params = params;
    }
    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + Arrays.hashCode(params);
      result = prime * result + ((query == null) ? 0 : query.hashCode());
      return result;
    }
    @Override
    public boolean equals(Object obj) {
      if (this == obj)
        return true;
      if (obj == null)
        return false;
      if (getClass() != obj.getClass())
        return false;
      CacheKey other = (CacheKey) obj;
      if (!Arrays.equals(params, other.params))
        return false;
      if (query == null) {
        if (other.query != null)
          return false;
      } else if (!query.equals(other.query))
        return false;
      return true;
    }
    
    
	}
	
	
	

}
