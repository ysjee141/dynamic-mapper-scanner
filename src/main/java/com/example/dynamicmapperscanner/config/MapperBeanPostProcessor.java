package com.example.dynamicmapperscanner.config;

import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.type.DateTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.SqlTimestampTypeHandler;
import org.apache.ibatis.type.StringTypeHandler;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.*;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

/**
 * 다중 Data Source를 지원하기 위한 MyBatis Mapper Post Processor
 */
public class MapperBeanPostProcessor implements BeanDefinitionRegistryPostProcessor {

	private final RepoConfig repo;
	private final Configuration myBatisConfiguration;

	public MapperBeanPostProcessor(RepoConfig repo) {
		this.repo = repo;

		myBatisConfiguration = new Configuration();
		myBatisConfiguration.setMapUnderscoreToCamelCase(true);
		myBatisConfiguration.setLazyLoadingEnabled(true);
		myBatisConfiguration.setMultipleResultSetsEnabled(true);
		myBatisConfiguration.setUseColumnLabel(true);
		myBatisConfiguration.setDefaultExecutorType(ExecutorType.SIMPLE);
		myBatisConfiguration.setCallSettersOnNulls(true);
		myBatisConfiguration.setCacheEnabled(false);
		myBatisConfiguration.setJdbcTypeForNull(JdbcType.NULL);
		myBatisConfiguration.getTypeHandlerRegistry().register(String.class, StringTypeHandler.class);
		myBatisConfiguration.getTypeHandlerRegistry().register(Timestamp.class, SqlTimestampTypeHandler.class);
		myBatisConfiguration.getTypeHandlerRegistry().register(Time.class, DateTypeHandler.class);
		myBatisConfiguration.getTypeHandlerRegistry().register(Date.class, DateTypeHandler.class);

	}

	@Override
	public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanFactory) throws BeansException {
		repo.forEach((v) -> {
			// DataSource 등록
			registerDataSource(beanFactory, v);

			// SqlSessionFactory
			registerSqlSessionFactory(beanFactory, v);

			// SqlSessionTemplate
			registerSqlSessionTemplate(beanFactory, v);

			// TransactionManager
			registerTransactionManager(beanFactory, v);

			// MapperScannerConfigurer
			registerMapperScanner(beanFactory, v);
		});
	}

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {

	}

	/**
	 * Data Source Bean 등록
	 * @param registry Bean 등록을 위한 BeanRegistry {@link BeanDefinitionRegistry}
	 * @param config Data Source Property {@link RepoConfig.DsConfig}
	 */
	private void registerDataSource(
			BeanDefinitionRegistry registry, RepoConfig.DsConfig config
	) {
		// DataSource 등록
		GenericBeanDefinition dataSourceBeanDefinition = new GenericBeanDefinition();
		dataSourceBeanDefinition.setBeanClass(HikariDataSource.class);
		dataSourceBeanDefinition.setPropertyValues(
				new MutablePropertyValues(config.getDataSource())
		);

		registry.registerBeanDefinition(
				config.getName(RepoConfig.BEAN_TYPE.DATASOURCE),
				dataSourceBeanDefinition
		);
	}

	/**
	 * MyBatis SessionFactory 등록
	 * @param registry Bean 등록을 위한 BeanRegistry {@link BeanDefinitionRegistry}
	 * @param config Data Source Property {@link RepoConfig.DsConfig}
	 */
	private void registerSqlSessionFactory(
			BeanDefinitionRegistry registry, RepoConfig.DsConfig config
	) {
		AbstractBeanDefinition sqlSessionFactory = BeanDefinitionBuilder
				.genericBeanDefinition(SqlSessionFactoryBean.class)
				.addPropertyReference("dataSource", config.getName(RepoConfig.BEAN_TYPE.DATASOURCE))
				.addPropertyValue("configuration", myBatisConfiguration)
				.addPropertyValue("mapperLocations", config.getMapperLocation())
				.getBeanDefinition();

		registry.registerBeanDefinition(
				config.getName(RepoConfig.BEAN_TYPE.SESSION_FACTORY), sqlSessionFactory
		);
	}

	/**
	 * MyBatis SessionTemplate 등록
	 * @param registry Bean 등록을 위한 BeanRegistry {@link BeanDefinitionRegistry}
	 * @param config Data Source Property {@link RepoConfig.DsConfig}
	 */
	private void registerSqlSessionTemplate(
			BeanDefinitionRegistry registry, RepoConfig.DsConfig config
	) {
		AbstractBeanDefinition sqlSessionTemplate = BeanDefinitionBuilder
				.genericBeanDefinition(SqlSessionTemplate.class)
				.addConstructorArgReference(config.getName(RepoConfig.BEAN_TYPE.SESSION_FACTORY))
				.getBeanDefinition();

		registry.registerBeanDefinition(
				config.getName(RepoConfig.BEAN_TYPE.SESSION_TEMPLATE), sqlSessionTemplate
		);
	}

	/**
	 * MyBatis TransactionManager 등록
	 * @param registry Bean 등록을 위한 BeanRegistry {@link BeanDefinitionRegistry}
	 * @param config Data Source Property {@link RepoConfig.DsConfig}
	 */
	private void registerTransactionManager(
			BeanDefinitionRegistry registry, RepoConfig.DsConfig config
	) {
		AbstractBeanDefinition transactionManager = BeanDefinitionBuilder
				.genericBeanDefinition(DataSourceTransactionManager.class)
				.addPropertyReference("dataSource", config.getName(RepoConfig.BEAN_TYPE.DATASOURCE))
				.getBeanDefinition();

		registry.registerBeanDefinition(
				config.getName(RepoConfig.BEAN_TYPE.TRANSACTION_MANAGER), transactionManager
		);
	}

	/**
	 * MyBatis MapperScanner 등록
	 * @param registry Bean 등록을 위한 BeanRegistry {@link BeanDefinitionRegistry}
	 * @param config Data Source Property {@link RepoConfig.DsConfig}
	 */
	private void registerMapperScanner(
			BeanDefinitionRegistry registry, RepoConfig.DsConfig config
	) {
		AbstractBeanDefinition mapperScannerConfigurer = BeanDefinitionBuilder
				.genericBeanDefinition(MapperScannerConfigurer.class)
				.addPropertyValue("sqlSessionFactoryBeanName", config.getName(RepoConfig.BEAN_TYPE.SESSION_FACTORY))
				.addPropertyValue("basePackage", "com.example.dynamicmapperscanner*")
				.addPropertyValue("annotationClass", config.getOrder().get())
				.getBeanDefinition();

		registry.registerBeanDefinition(
				config.getName(RepoConfig.BEAN_TYPE.MAPPER_SCANNER), mapperScannerConfigurer
		);
	}

}
