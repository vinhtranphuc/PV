package com.so.config.database;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.hibernate.cfg.Environment;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@PropertySource("classpath:secondaryDB.properties")
@Configuration
@ConfigurationProperties
@EnableConfigurationProperties(SecondaryDBConfig.class)
@EnableTransactionManagement
@EnableJpaRepositories(
		entityManagerFactoryRef = "secondaryEntityManagerFactory",
		transactionManagerRef = "secondaryTransactionManager",
		basePackages = {"${datasource.secondary.jpa.repository}" }
)
public class SecondaryDBConfig {

	// TIMEZONE
	@Value("${server.timezone}")
	protected String severTimezone;

	// SEVER
	@Value("${datasource.secondary.host}")
	protected String host;
	@Value("${datasource.secondary.port}")
	protected String port;
	@Value("${datasource.secondary.database}")
	protected String database;
	@Value("${datasource.secondary.username}")
	protected String username;
	@Value("${datasource.secondary.password}")
	protected String password;

	// ORM
	@Value("${datasource.secondary.jpa.entity}")
	protected String jpaEntityPgk;

	// HIBERNATE
	@Value("${datasource.secondary.hibernate.hbm2ddl.auto}")
	protected String hbm2ddlAuto;
	@Value("${datasource.secondary.hibernate.dialect}")
	protected String dialect;
	@Value("${datasource.secondary.hibernate.show_sql}")
	protected String showSql;
	@Value("${datasource.secondary.hibernate.format_sql}")
	protected String formatSql;
	@Value("${datasource.secondary.hibernate.generate_statistics}")
	protected String generateStatistics;
	@Value("${datasource.secondary.hibernate.cache.use_structured_entries}")
	protected String useStructuredEntries;
	@Value("${datasource.secondary.hibernate.current_session_context_class}")
	protected String currentSessionContextClass;

	@Bean(name = "secondaryDataSource")
	public DataSource secondaryDataSource() {
		final String URL = "jdbc:mysql://%s:%s/%s?createDatabaseIfNotExist=true&useUnicode=true&characterEncoding=UTF-8&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=%s&zeroDateTimeBehavior=convertToNull";
		String formattedUrl = String.format(URL, host, port, database, severTimezone);
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
		dataSource.setUrl(formattedUrl);
		dataSource.setUsername(username);
		dataSource.setPassword(password);
		return dataSource;
	}

	@Bean(name = "secondaryEntityManagerFactory")
	public LocalContainerEntityManagerFactoryBean secondaryEntityManagerFactory(EntityManagerFactoryBuilder builder,
			@Qualifier("secondaryDataSource") DataSource secondaryDataSource) {
		LocalContainerEntityManagerFactoryBean factory = builder.dataSource(secondaryDataSource).packages(jpaEntityPgk)
				.persistenceUnit("secondary").build();
		factory.setJpaProperties(additionalHibernateProperties());
		return factory;
	}

	/**
	 * --------------------------------------------------------------------------
	 * JPA/HIBERNATE Config
	 * --------------------------------------------------------------------------
	 */
	private Properties additionalHibernateProperties() {
		Properties properties = new Properties();
		properties.setProperty(Environment.HBM2DDL_AUTO, hbm2ddlAuto);
		properties.setProperty(Environment.DIALECT, dialect);
		properties.setProperty(Environment.SHOW_SQL, showSql);
		properties.setProperty(Environment.FORMAT_SQL, formatSql);
		properties.setProperty(Environment.GENERATE_STATISTICS, generateStatistics);
		properties.setProperty(Environment.USE_STRUCTURED_CACHE, useStructuredEntries);
		properties.setProperty(Environment.CURRENT_SESSION_CONTEXT_CLASS, currentSessionContextClass);

		/**
		 * Ehcache properties.setProperty("hibernate.javax.cache.uri",
		 * "classpath:ehcache.xml"); // Enable second level cache (default value is
		 * true) properties.setProperty(Environment.USE_SECOND_LEVEL_CACHE, "true"); //
		 * Specify cache region factory class
		 * properties.setProperty(Environment.CACHE_REGION_FACTORY,
		 * "org.hibernate.cache.jcache.JCacheRegionFactory"); // Specify cache provider
		 * properties.setProperty("hibernate.javax.cache.provider",
		 * "org.ehcache.jsr107.EhcacheCachingProvider");
		 * properties.setProperty(Environment.DEFAULT_CACHE_CONCURRENCY_STRATEGY,
		 * "read-write");
		 */

		return properties;
	}

	// config JPA transaction
	@Bean(name = "secondaryTransactionManager")
	public PlatformTransactionManager secondaryTransactionManager(
			@Qualifier("secondaryEntityManagerFactory") EntityManagerFactory secondaryEntityManagerFactory) {
		return new JpaTransactionManager(secondaryEntityManagerFactory);
	}
	/**
	 * --------------------------------------------------------------------------
	 * End JPA/HIBERNATE Config
	 * --------------------------------------------------------------------------
	 */
}
