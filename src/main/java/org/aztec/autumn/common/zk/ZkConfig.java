package org.aztec.autumn.common.zk;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.zookeeper.KeeperException;
import org.aztec.autumn.common.GlobalConst;
import org.aztec.autumn.common.utils.JsonUtils;
import org.aztec.autumn.common.utils.ReflectionUtil;
import org.aztec.autumn.common.utils.UtilsFactory;
import org.fusesource.hawtbuf.ByteArrayInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ZkConfig extends ZkNode {

	private ConfigFormat format;
	private Map<String, String> datas = new ConcurrentHashMap<>();
	private int lastVersion = 0;
	private JsonUtils jsonUtil = UtilsFactory.getInstance().getJsonUtils();
	private ReflectionUtil reflectUtil = UtilsFactory.getInstance().getReflectUtil();
	private static final Logger LOG = LoggerFactory.getLogger(ZkConfig.class);

	public static enum ConfigFormat {
		JSON, PROPERTIES,TEXT;
	}

	public ZkConfig(String dataID, ConfigFormat format) throws IOException, KeeperException, InterruptedException {
		super(dataID);
		this.format = format;
		init();
	}
	
	protected void notifyChanges() throws Exception {
		setFields();
	}

	public void setFields() throws Exception {
		if (format == null) {
			format = ConfigFormat.PROPERTIES;
		}
		if (lastVersion < getCurVersion()) {
			String dataStr = getDataStr();
			switch (format) {
			case JSON:
				datas = jsonUtil.json2Object(getDataStr(), Map.class);
				break;
			case PROPERTIES:
				// String[]
				/*Properties props = new Properties();
				props.load(new ByteArrayInputStream(dataStr.getBytes(GlobalConst.DEFAULT_CHARSET)));
				
				for(Object propKey : props.keySet()) {
					String datakey = (String)propKey;
					System.out.println(datakey + "-" + props.getProperty((String)datakey));
					datas.put(datakey, props.getProperty((String)datakey));
				}*/
				String[] lines = dataStr.split("##");
				for (String line : lines) {
					datas.put(line.split("=")[0], line.split("=")[1]);
				}
				break;
			case TEXT:
				return ;
			}
			Class thisCls = this.getClass();
			Field[] allFields = this.getClass().getDeclaredFields();
			for(Field field : allFields) {
				String fieldName = field.getName();
				if(datas.get(fieldName) == null)
					continue;
				Class fieldType = field.getType();
				Object value = fieldType.getConstructor(String.class).newInstance(datas.get(fieldName));
				reflectUtil.setValue(fieldName, this, value, false);
			}
		}
	}

	public String getData(String key) throws Exception {
		setFields();
		return datas.get(key);
	}
}
