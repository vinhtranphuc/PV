package com.so.config.database;

import java.util.Properties;
import java.util.Set;
import java.util.regex.Pattern;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.transaction.managed.ManagedTransactionFactory;
import org.hibernate.cfg.Environment;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.type.filter.RegexPatternTypeFilter;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.so.common.FieldMap;
import com.so.config.interceptor.MyBatisIntercept;

/**
 * Created by vinhtp on 2019-4-4.
 */
@PropertySource("classpath:primaryDB.properties")
@Configuration
@ConfigurationProperties
@EnableConfigurationProperties(PrimaryDBConfig.class)
@EnableTransactionManagement
@EnableJpaRepositories(
		entityManagerFactoryRef = "primaryEntityManagerFactory",
		transactionManagerRef = "primaryTransactionManager",
		basePackages = {"${datasource.primary.jpa.repository}" }
)
@MapperScan(
		basePackages = "com.so.mybatis.primary",
		sqlSessionFactoryRef = "primarySqlSessionFactory"
)
public class PrimaryDBConfig {

	private org.apache.ibatis.session.Configuration configuration;

	// TIMEZONE
	@Value("${server.timezone}")
	protected String severTimezone;

	// SEVER
	@Value("${datasource.primary.host}")
	protected String host;
	@Value("${datasource.primary.port}")
	protected String port;
	@Value("${datasource.primary.database}")
	protected String database;
	@Value("${datasource.primary.username}")
	protected String username;
	@Value("${datasource.primary.password}")
	protected String password;

	// ORM
	@Value("${datasource.primary.jpa.entity}")
	protected String jpaEntityPgk;
	@Value("${datasource.primary.mybatis.model}")
	protected String mybatisModelPgk;
	@Value("${datasource.primary.mybatis.resources}")
	protected String myabatisResource;

	// HIBERNATE
	@Value("${datasource.primary.hibernate.hbm2ddl.auto}")
	protected String hbm2ddlAuto;
	@Value("${datasource.primary.hibernate.dialect}")
	protected String dialect;
	@Value("${datasource.primary.hibernate.show_sql}")
	protected String showSql;
	@Value("${datasource.primary.hibernate.format_sql}")
	protected String formatSql;
	@Value("${datasource.primary.hibernate.generate_statistics}")
	protected String generateStatistics;
	@Value("${datasource.primary.hibernate.cache.use_structured_entries}")
	protected String useStructuredEntries;
	@Value("${datasource.primary.hibernate.current_session_context_class}")
	protected String currentSessionContextClass;

	/**
	 * --------------------------------------------------------------------------
	 * DataSource
	 * --------------------------------------------------------------------------
	 */
	@Primary
	@Bean(name = "primaryDataSource")
	public DataSource primaryDataSource() {
		final String URL = "jdbc:mysql://%s:%s/%s?createDatabaseIfNotExist=true&useUnicode=true&characterEncoding=UTF-8&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=%s&zeroDateTimeBehavior=convertToNull";
		String formattedUrl = String.format(URL, host, port, database, severTimezone);
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
		dataSource.setUrl(formattedUrl);
		dataSource.setUsername(username);
		dataSource.setPassword(password);
		return dataSource;
	}

	/**
	 * --------------------------------------------------------------------------
	 * End DataSource
	 * --------------------------------------------------------------------------
	 */

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

	@Primary
	@Bean(name = "primaryEntityManagerFactory")
	public LocalContainerEntityManagerFactoryBean primaryEntityManagerFactory(EntityManagerFactoryBuilder builder,
			@Qualifier("primaryDataSource") DataSource primaryDataSource) {
		LocalContainerEntityManagerFactoryBean factory = builder.dataSource(primaryDataSource).packages(jpaEntityPgk)
				.persistenceUnit("primary").build();
		factory.setJpaProperties(additionalHibernateProperties());
		return factory;
	}

	// config JPA transaction
	@Primary
	@Bean(name = "primaryTransactionManager")
	public PlatformTransactionManager primaryTransactionManager(
			@Qualifier("primaryEntityManagerFactory") EntityManagerFactory primaryEntityManagerFactory) {
		return new JpaTransactionManager(primaryEntityManagerFactory);
	}

	/**
	 * --------------------------------------------------------------------------
	 * End JPA/HIBERNATE Config
	 * --------------------------------------------------------------------------
	 */

	/**
	 * --------------------------------------------------------------------------
	 * MyBatis Config
	 * --------------------------------------------------------------------------
	 */
	@Primary
	@Bean
	public SqlSessionFactory primarySqlSessionFactory(ApplicationContext applicationContext) throws Exception {
		final SqlSessionFactoryBean sqlSessionFactory = new SqlSessionFactoryBean();

		// mybatis config
		configuration = new org.apache.ibatis.session.Configuration();
		configuration.setCacheEnabled(false);
		configuration.setCallSettersOnNulls(true);
		configuration.setUseGeneratedKeys(true);
		configuration.setDefaultExecutorType(ExecutorType.REUSE);
		// configuration.setMapUnderscoreToCamelCase(true);
		registTypeAlias(mybatisModelPgk, FieldMap.class);

		sqlSessionFactory.setConfiguration(configuration);

		// mybatis mapper config
		sqlSessionFactory.setMapperLocations(applicationContext.getResources(myabatisResource));

		// set data source
		sqlSessionFactory.setDataSource(primaryDataSource());

		// ibatis transaction
		sqlSessionFactory.setTransactionFactory(new ManagedTransactionFactory());

		// mybatis interceptor
		MyBatisIntercept myBatisQueryIntercept = new MyBatisIntercept();
		sqlSessionFactory.setPlugins(new Interceptor[] { myBatisQueryIntercept });

		return sqlSessionFactory.getObject();
	}

	// regist type alias
	private void registTypeAlias(final String packageScan, Class<?>... classes) throws ClassNotFoundException {
		final ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(
				false);
		provider.addIncludeFilter(new RegexPatternTypeFilter(Pattern.compile(".*")));
		final Set<BeanDefinition> packageScanClasses = provider.findCandidateComponents(packageScan);

		// model bean
		for (BeanDefinition bean : packageScanClasses) {
			Class<?> clazz = Class.forName(bean.getBeanClassName());
			configuration.getTypeAliasRegistry().registerAlias(clazz);
		}
		for (Class<?> clazz : classes) {
			// register with lowerCase field
			configuration.getTypeAliasRegistry().registerAlias(clazz);
		}
	}

	@Primary
	@Bean
	public SqlSessionTemplate primarySqlSessionTemplate(SqlSessionFactory primarySqlSessionFactory) {
		return new SqlSessionTemplate(primarySqlSessionFactory);
	}
	/**
	 * --------------------------------------------------------------------------
	 * End MyBatis Config
	 * --------------------------------------------------------------------------
	 */
}
