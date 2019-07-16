package org.aztec.autumn.common.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.aztec.autumn.common.utils.annotation.config.Configuration;
import org.aztec.autumn.common.utils.annotation.config.Property;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public abstract class BasePropertiesConfig {

	protected Properties properties;
	private String filePath;
	private static Logger LOG = LoggerFactory.getLogger(BasePropertiesConfig.class);
	private static final String DEFAULT_SEPERATE_CHAR = ",";
	public static final String DEFAUTL_SYSTEM_PROPERTY_FILE = "AUTUMN_SYS_PROPS_FILE";
	
	public BasePropertiesConfig(String propFilePath) {
		// TODO Auto-generated constructor stub
		this.filePath = propFilePath;
		if(filePath.equals("system")) {
			filePath = System.getProperty(DEFAUTL_SYSTEM_PROPERTY_FILE);
			if(filePath == null) {
				filePath = "conf/aztec_base.properties";
			}
		}
		else {
			this.filePath = propFilePath;
		}
		loadFile();
		init();
	}
	
	protected void loadFile(){
		try {
			properties = new Properties();
			if(filePath.startsWith("res:")) {
				String realPath = filePath.substring(4);
				System.out.println(realPath);
				InputStream stream = BasePropertiesConfig.class.getResourceAsStream(realPath);
				if(stream != null){
					properties.load(stream);
				}
				else{
					LOG.error("Resource Not found!Base Properties config path:" +filePath);
				}
			}
			else {
				properties.load(new FileInputStream(new File(filePath)));	
			}		
		} catch (Exception e) {
			LOG.error(e.getMessage(),e);
		}
	}
	
	public BasePropertiesConfig(){
		loadFile();
		init();
	}
	
	protected void reload() {
		loadFile();
		init();
	}
	
	protected void init(){
		try {
			loadProperties();
		} catch (Exception e1) {
			LOG.warn(e1.getMessage(),e1);
			return;
		}
		//String[][] namesMappings = getNamesMapping();
		List<String[]> namesMappings = getNamesMappingByAnnotation();
		
		if(properties == null)
			return;
		if(namesMappings != null){
			for(String[] names : namesMappings){
				try {
					if (!properties.containsKey(names[1])) {
						LOG.debug(names[1] + " is not configured!Using default value!");
						continue;
					}
					Field field = this.getClass().getDeclaredField(names[0]);
					if(field.getType().isArray()){
						String[] readValues = properties.getProperty(names[1]).split(DEFAULT_SEPERATE_CHAR);
						Class fieldType = field.getType();
						Class componentType = fieldType.getComponentType();
						Constructor constructor = componentType.getConstructor(String.class);
						Object valArray = Array.newInstance(componentType, readValues.length);
						for(int i = 0;i < readValues.length;i++){
							Array.set(valArray, i, constructor.newInstance(readValues[i]));
						}
						field.setAccessible(true);
						field.set(this, valArray);
					}
					else{
						Constructor constr = field.getType().getConstructor(String.class);
						field.setAccessible(true);
						field.set(this, constr.newInstance(properties.get(names[1])));
					}
				} catch (Exception e) {
					LOG.debug(e.getMessage());
				}
			}
		}
	}
	
	//public abstract String[][] getNamesMapping();
	
	protected List<String[]> getNamesMappingByAnnotation(){

		Class clazz = this.getClass();
		Field[] fields = clazz.getDeclaredFields();
		List<String[]> mappings = new ArrayList<>();
		for(Field field : fields){
			Property property = field.getAnnotation(Property.class);
			if(property != null){
				String[] mapping = new String[]{field.getName(),property.value()};
				mappings.add(mapping);
			}
		}
		//if(properties == null)
		return mappings;
	}
	
	private void loadProperties() throws FileNotFoundException, IOException, ConfigurationInfoNotFoundException{
		if(properties == null){
			Class clazz = this.getClass();
			Configuration config = (Configuration) clazz.getAnnotation(Configuration.class);
			if(config == null)
				throw new ConfigurationInfoNotFoundException("No information provide for reading configuration!");
			if(config.path() != null){
				properties = new Properties();
				properties.load(new FileInputStream(new File(config.path())));
			}
			else if (config.resourcePath() != null){
				properties = new Properties();
				properties.load(clazz.getResourceAsStream(config.resourcePath()));
			}
			else {
				throw new ConfigurationInfoNotFoundException("No information provide for reading configuration!");
			}
		}
	}
	
	
}
