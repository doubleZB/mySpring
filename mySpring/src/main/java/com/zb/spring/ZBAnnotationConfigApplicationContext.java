package com.zb.spring;

import java.io.File;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ZBAnnotationConfigApplicationContext {

	private Class configClass;
	
	public ZBAnnotationConfigApplicationContext(Class configClass) {
		this.configClass =configClass;
		//扫描指定包下的类并进行加载
		List<Class> classList = scan(configClass);
		System.out.println(classList);
		
		
		//基于class创建单例bean
		instanceSinglentonBean();
	}
	
	private List<Class> scan(Class configClass2) {
		//扫描包，得到Class 
		List<Class> classList = new ArrayList<Class>();
		if(configClass.isAnnotationPresent(ComponentScan.class)) {
			ComponentScan componentScanAnnotation = (ComponentScan)configClass.getAnnotation(ComponentScan.class);
			//包路径
			String sourcePath = componentScanAnnotation.value();
			//class 路径
			String classPath = sourcePath.replace(".", "/");
			ClassLoader classLoader = ZBAnnotationConfigApplicationContext.class.getClassLoader();
			URL url = classLoader.getResource(classPath);
			File file =new File(url.getFile());//classpath 下的classPath文件夹
			if(file.isDirectory()) {
				File[] listFiles = file.listFiles();
				for (File subFile : listFiles) {
					String absolutePath = subFile.getAbsolutePath();
					absolutePath = absolutePath.substring(absolutePath.lastIndexOf("com"), absolutePath.indexOf(".class"));
					absolutePath = absolutePath.replace("\\", ".");
					try {
						Class<?> loadClass = classLoader.loadClass(absolutePath);
						//操作有注解的class
						if(loadClass.isAnnotationPresent(Component.class)) {
							if(loadClass.isAnnotationPresent(Scope.class)) {
								if(loadClass.getAnnotation(Scope.class).value().equals("singleton")) {
									classList.add(loadClass);
								}
							}else {
								//没有默认单例
								classList.add(loadClass);
							}
						}
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
				}
			}			
		}
		return classList;
	}

	private void instanceSinglentonBean() {
		
	}

	public Object getBean(String beanName) {
		return null;
	}
}
