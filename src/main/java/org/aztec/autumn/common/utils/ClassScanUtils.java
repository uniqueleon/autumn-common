package org.aztec.autumn.common.utils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClassScanUtils {

	private static ClassLoader DEFAULT_CLASS_LOADER = Thread.currentThread().getContextClassLoader();
	private static Logger LOG = LoggerFactory.getLogger(ClassScanUtils.class);

	public ClassScanUtils() {
		// TODO Auto-generated constructor stub
	}
	
	public static void setClassLoader(ClassLoader loader){
		DEFAULT_CLASS_LOADER = loader;
	}

	public static <E> List<Class<E>> scan(String[] paths, Class<E> targetCls, boolean isRecursive,ClassSelectStrategy strategy)
			throws ClassNotFoundException, URISyntaxException, IOException {
		List<Class<E>> classes = new ArrayList<Class<E>>();
		for (String path : paths)
			classes.addAll(scan(path, targetCls, isRecursive,strategy));
		return classes;
	}

	public static <E> List<Class<E>> scan(String path, Class<E> targetCls, boolean isRecursive,ClassSelectStrategy strategy)
			throws URISyntaxException, ClassNotFoundException, IOException {
		List<Class<E>> classes = new ArrayList<Class<E>>();
		URL url = DEFAULT_CLASS_LOADER.getResource(path);
		// url.get
		// System.out.println(url.toURI());
		LOG.info(url.toURI().toString());
		if (!isJarInside(url)) {
			classes.addAll(doScanSimple(url, path, targetCls, isRecursive,strategy));
		} else {
			classes.addAll(doScanJar(url, path, targetCls, isRecursive,strategy));
		}
		return classes;
	}

	public static <E> List<Class<E>> doScanSimple(URL url, String path, Class<E> targetCls, boolean isRecursive,ClassSelectStrategy strategy)
			throws URISyntaxException, ClassNotFoundException, IOException {

		List<Class<E>> classes = new ArrayList<Class<E>>();
		File classesDir = new File(url.toURI());
		if (classesDir.isDirectory()) {
			for (File classesFile : classesDir.listFiles()) {
				if (isRecursive && classesFile.isDirectory()) {
					classes.addAll(
							scan(path.endsWith("/") ? path + classesFile.getName() : path + "/" + classesFile.getName(),
									targetCls, isRecursive,strategy));
				} else if (classesFile.isFile()) {
					String className = path.replace("/", ".") + "." + classesFile.getName().replace(".class", "");
					Class loadedCls = DEFAULT_CLASS_LOADER.loadClass(className);
					if (isSelected(loadedCls, targetCls, strategy)) {
						classes.add(loadedCls);
					}
				}
			}
		}
		return classes;
	}
	
	public static boolean isSelected(Class testCls,Class targetCls,ClassSelectStrategy strategy){
		
		switch(strategy){
		case INTERFACE:
			return targetCls == null || targetCls.isAssignableFrom(testCls);
		case ANNOTAION:
			return targetCls == null || testCls.getAnnotation(targetCls) != null;
		case CONSTRUCTOR_ANNOTATED:
			Constructor[] constructors = testCls.getConstructors();
			for(Constructor constructor : constructors){
				if(constructor.isAnnotationPresent(targetCls)){
					return true;
				}
			}
			return false;
		case METHOD_ANNOTATED :
			Method[] methods = testCls.getDeclaredMethods();
			for(Method method : methods){
				if(method.isAnnotationPresent(targetCls)){
					return true;
				}
			}
			return false;
		case FIELD_ANNOTATED:
			Field[] fields = testCls.getDeclaredFields();
			for(Field field : fields){
				if(field.isAnnotationPresent(targetCls)){
					return true;
				}
			}
			return false;
		default:
			return false;
		}
	}

	public static <E> List<Class<E>> doScanJar(URL url, String path, Class<E> targetCls, boolean isRecursive,ClassSelectStrategy strategy)
			throws URISyntaxException, ClassNotFoundException, IOException {

		List<Class<E>> classes = new ArrayList<Class<E>>();
		// jar:file:/C:/Users/liming1/Desktop/cp6-server/cp26-command-server-0.0.1.jar!/com/baitian/cp26/server/cmd/app/construct
		String jarLocation = url.toURI().toString().replace("jar:file:", "").split("!")[0];
		jarLocation = jarLocation.replaceAll("//", "/");
		JarFile jarFile = new JarFile(new File(jarLocation));
		Enumeration<JarEntry> entries = jarFile.entries();
		JarEntry entry = entries.nextElement();
		while (entry != null) {
			// LOG.info("entry name:" + entry.getName());
			if (entry.getName().startsWith(path) && entry.getName().endsWith(".class")) {
				String className = entry.getName().replace("/", ".").replace(".class", "");
				Class loadedCls = DEFAULT_CLASS_LOADER.loadClass(className);
				if (isSelected(loadedCls, targetCls, strategy)){
					classes.add(loadedCls);
				}
			}
			if (entries.hasMoreElements())
				entry = entries.nextElement();
			else
				entry = null;
		}
		return classes;
	}

	public static boolean isJarInside(URL url) throws URISyntaxException {
		if (url.toURI().toString().contains("jar:file"))
			return true;
		return false;
	}

	public static <E> List<E> newInstancesAsList(String[] paths, Class<E> targetCls, boolean isRecursive,ClassSelectStrategy strategy)
			throws ClassNotFoundException, URISyntaxException, InstantiationException, IllegalAccessException,
			IOException {
		List<Class<E>> classes = scan(paths, targetCls, isRecursive,strategy);
		List<E> entityList = new ArrayList<E>();

		for (int i = 0; i < classes.size(); i++) {
			Class<E> clazz =classes.get(i);
			if(clazz == null || clazz .isInterface()){
				continue;
			}
			try {
				E entity = clazz.newInstance();
				entityList.add(entity);
			} catch (Exception e) {
				LOG.warn(e.getMessage(), e);
			}
		}
		return entityList;
	}
	
	public static <E> E[] newInstancesAsArray(String[] paths, Class<E> targetCls, boolean isRecursive,ClassSelectStrategy strategy)
			throws ClassNotFoundException, URISyntaxException, InstantiationException, IllegalAccessException,
			IOException {
		List<E> entityList = newInstancesAsList(paths, targetCls, isRecursive,strategy);
		E[] entities = (E[]) Array.newInstance(targetCls, entityList.size());
		return entityList.toArray(entities);
	}
	
	public static enum ClassSelectStrategy{
		INTERFACE,ANNOTAION,CONSTRUCTOR_ANNOTATED,METHOD_ANNOTATED,FIELD_ANNOTATED;
	}

}
