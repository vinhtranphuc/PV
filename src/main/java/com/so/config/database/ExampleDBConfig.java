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
@PropertySource("classpath:exampleDB.properties")
@Configuration
@ConfigurationProperties
@EnableConfigurationProperties(ExampleDBConfig.class)
@EnableTransactionManagement
@EnableJpaRepositories(
		entityManagerFactoryRef = "exampleEntityManagerFactory",
		transactionManagerRef = "exampleTransactionManager",
		basePackages = {"${datasource.example.jpa.repository}" })
@MapperScan(
		basePackages = "com.so.mybatis.example",
		sqlSessionFactoryRef = "exampleSqlSessionFactory"
)
public class ExampleDBConfig {

	private org.apache.ibatis.session.Configuration configuration;

	// TIMEZONE
	@Value("${server.timezone}")
	protected String severTimezone;

	// SEVER
	@Value("${datasource.example.host}")
	protected String host;
	@Value("${datasource.example.port}")
	protected String port;
	@Value("${datasource.example.database}")
	protected String database;
	@Value("${datasource.example.username}")
	protected String username;
	@Value("${datasource.example.password}")
	protected String password;

	// ORM
	@Value("${datasource.example.jpa.entity}")
	protected String jpaEntityPgk;
	@Value("${datasource.example.mybatis.model}")
	protected String mybatisModelPgk;
	@Value("${datasource.example.mybatis.resources}")
	protected String myabatisResource;

	// HIBERNATE
	@Value("${datasource.example.hibernate.hbm2ddl.auto}")
	protected String hbm2ddlAuto;
	@Value("${datasource.example.hibernate.dialect}")
	protected String dialect;
	@Value("${datasource.example.hibernate.show_sql}")
	protected String showSql;
	@Value("${datasource.example.hibernate.format_sql}")
	protected String formatSql;
	@Value("${datasource.example.hibernate.generate_statistics}")
	protected String generateStatistics;
	@Value("${datasource.example.hibernate.cache.use_structured_entries}")
	protected String useStructuredEntries;
	@Value("${datasource.example.hibernate.current_session_context_class}")
	protected String currentSessionContextClass;

	/**
	 * --------------------------------------------------------------------------
	 * DataSource
	 * --------------------------------------------------------------------------
	 */
	@Bean(name = "exampleDataSource")
	public DataSource exampleDataSource() {
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

	@Bean(name = "exampleEntityManagerFactory")
	public LocalContainerEntityManagerFactoryBean exampleEntityManagerFactory(EntityManagerFactoryBuilder builder,
			@Qualifier("exampleDataSource") DataSource exampleDataSource) {
		LocalContainerEntityManagerFactoryBean factory = builder.dataSource(exampleDataSource).packages(jpaEntityPgk)
				.persistenceUnit("example").build();
		factory.setJpaProperties(additionalHibernateProperties());
		return factory;
	}

	// config JPA transaction
	@Bean(name = "exampleTransactionManager")
	public PlatformTransactionManager exampleTransactionManager(
			@Qualifier("exampleEntityManagerFactory") EntityManagerFactory exampleEntityManagerFactory) {
		return new JpaTransactionManager(exampleEntityManagerFactory);
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
	@Bean
	public SqlSessionFactory exampleSqlSessionFactory(ApplicationContext applicationContext) throws Exception {
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
		sqlSessionFactory.setDataSource(exampleDataSource());

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

	@Bean
	public SqlSessionTemplate exampleSqlSessionTemplate(SqlSessionFactory exampleSqlSessionFactory) {
		return new SqlSessionTemplate(exampleSqlSessionFactory);
	}
	/**
	 * --------------------------------------------------------------------------
	 * End MyBatis Config
	 * --------------------------------------------------------------------------
	 */
}
