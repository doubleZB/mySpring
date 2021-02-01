package com.zb.spring;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.StringUtils;

public class ZBAnnotationConfigApplicationContext {

	private Class configClass;
	private Map<String,BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<String,BeanDefinition>();//bd
	private Map<String,Object> singletonObjects = new ConcurrentHashMap<>();//单例池
	
	public ZBAnnotationConfigApplicationContext(Class configClass) {
		this.configClass =configClass;
		//扫描指定包下的类并解析类信息把类信息封装到beanDefinitionMap中
		List<Class> classList = scan(configClass);
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
                            //添加bd
							BeanDefinition beanDefinition = new BeanDefinition();
							beanDefinition.setBeanClass(loadClass);
							if(loadClass.isAnnotationPresent(Scope.class)) {
								if(StringUtils.isNotBlank(loadClass.getAnnotation(Scope.class).value())) {
									beanDefinition.setScope(loadClass.getAnnotation(Scope.class).value());
								}else {
									beanDefinition.setScope("singleton");
								}
							}else {
								//没有默认单例
								beanDefinition.setScope("singleton");
							}
							//判断是否懒加载
							if(loadClass.isAnnotationPresent(Lazy.class)) {
								beanDefinition.setLazy(true);
							}else {
								beanDefinition.setLazy(false);
							}
							beanDefinitionMap.put(loadClass.getAnnotation(Component.class).value(), beanDefinition);
							classList.add(loadClass);
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
		for (String beanName : beanDefinitionMap.keySet()) {
			BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
			String scope = beanDefinition.getScope();
			if(StringUtils.equals("singleton", scope)) {
				if(!singletonObjects.containsKey(beanName)) {
					Object bean = doCreatBean(beanName);
					singletonObjects.put(beanName, bean);
				}
			}	
		}
	}

	private Object doCreatBean(String beanName) {
		Class<? extends BeanDefinition> clazz = beanDefinitionMap.get(beanName).getClass();
		try {
			Object newInstance = clazz.getDeclaredConstructor().newInstance();
			return newInstance;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return null;
	}

	public Object getBean(String beanName) {
		BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
		if(beanDefinition.getScope().equals("singleton")) {
			Object bean = singletonObjects.get(beanName);
			if(bean==null) {
				bean = doCreatBean(beanName);
			}
			return bean;
		}else if("prototype".equals(beanDefinition.getScope())) {
			Object bean = doCreatBean(beanName);
			return bean;
		}
		return null;
	}
}
