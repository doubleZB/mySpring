package com.zb.spring;

public class BeanDefinition {

	private Class beanClass;
	private String scope;
	private boolean lazy;
	
	public Class getBeanClass() {
		return beanClass;
	}
	public void setBeanClass(Class beanClass) {
		this.beanClass = beanClass;
	}
	public String getScope() {
		return scope;
	}
	public void setScope(String scope) {
		this.scope = scope;
	}
	public boolean isLazy() {
		return lazy;
	}
	public void setLazy(boolean lazy) {
		this.lazy = lazy;
	}

	
}
