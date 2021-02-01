package com.zb.spring;

public class BeanDefintion {

	private Class beanClass;
	private String scope;
	private Boolean isLazy;
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
	public Boolean getIsLazy() {
		return isLazy;
	}
	public void setIsLazy(Boolean isLazy) {
		this.isLazy = isLazy;
	}
	
}
