package com.so.config;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class ApplicationContextHolder implements ApplicationContextAware {

	private static ApplicationContext applicationContext;

	/**
	 * Implement the context injection function of the ApplicationContextAware
	 * interface and store it in a static variable.
	 */
	public void setApplicationContext(ApplicationContext applicationContext) {
		ApplicationContextHolder.applicationContext = applicationContext; // NOSONAR
	}

	/**
	 * Get the ApplicationContext stored in a static variable.
	 */
	public static ApplicationContext getApplicationContext() {
		checkApplicationContext();
		return applicationContext;
	}

	/**
	 * Get the bean from the static variable ApplicationContext, automatically
	 * transform to the type of the assigned object.
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getBean(String name) {
		checkApplicationContext();
		return (T) applicationContext.getBean(name);
	}

	/**
	 * Get the bean from the static variable ApplicationContext, automatically
	 * transform to the type of the assigned object.
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getBean(Class<T> clazz) {
		checkApplicationContext();
		return (T) applicationContext.getBeansOfType(clazz);
	}

	/**
	 * Clear the applicationContext static variable.
	 */
	public static void cleanApplicationContext() {
		applicationContext = null;
	}

	private static void checkApplicationContext() {
		if (applicationContext == null) {
			throw new IllegalStateException("applicaitonContext is not injected, please define SpringContextHolder in applicationContext.xml");
		}
	}
}
