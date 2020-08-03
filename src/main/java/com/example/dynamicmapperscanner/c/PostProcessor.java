package com.example.dynamicmapperscanner.c;

import com.example.dynamicmapperscanner.a.MapperOne;
import com.example.dynamicmapperscanner.a.RepoConfig;
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
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

public class PostProcessor implements BeanDefinitionRegistryPostProcessor {

	private Environment environment;

	public PostProcessor(Environment environment) {
		this.environment = environment;
	}

	@Override
	public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanFactory) throws BeansException {
		RepoConfig config = Binder.get(this.environment).bind("datasource3", RepoConfig.class).get();

		// MyBatis Configuration
		Configuration myBatisConfiguration = new Configuration();
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

		config.forEach((v) -> {
			String dataSourceName = v.getName() + "DataSource";
			String sqlSessionFactoryName = v.getName() + "SqlSessionFactory";
			String sqlSessionTemplateName = v.getName() + "SqlSessionTemplate";
			String transactionManagerName = v.getName() + "TransactionManager";
			String mapperScannerName = v.getName() + "MapperScanner";

			// DataSource 등록
			GenericBeanDefinition dataSourceBeanDefinition = new GenericBeanDefinition();
			dataSourceBeanDefinition.setBeanClass(HikariDataSource.class);
			dataSourceBeanDefinition.setPropertyValues(
					new MutablePropertyValues(v.getDataSource())
			);

			beanFactory.registerBeanDefinition(dataSourceName, dataSourceBeanDefinition);

			// SqlSessionFactory
			AbstractBeanDefinition sqlSessionFactory = BeanDefinitionBuilder
					.genericBeanDefinition(SqlSessionFactoryBean.class)
					.addPropertyReference("dataSource", dataSourceName)
					.addPropertyValue("configuration", myBatisConfiguration)
					.addPropertyValue("mapperLocations", "classpath:/mapper/" + v.getName() + "/*.xml")
//          .addPropertyValue("configLocation", "")
					.getBeanDefinition();

			beanFactory.registerBeanDefinition(sqlSessionFactoryName, sqlSessionFactory);

			// SqlSessionTemplate
			AbstractBeanDefinition sqlSessionTemplate = BeanDefinitionBuilder
					.genericBeanDefinition(SqlSessionTemplate.class)
					.addConstructorArgReference(sqlSessionFactoryName)
					.getBeanDefinition();

			beanFactory.registerBeanDefinition(sqlSessionTemplateName, sqlSessionTemplate);

			// TransactionManager
			AbstractBeanDefinition transactionManager = BeanDefinitionBuilder
					.genericBeanDefinition(DataSourceTransactionManager.class)
					.addPropertyReference("dataSource", dataSourceName)
					.getBeanDefinition();

			beanFactory.registerBeanDefinition(transactionManagerName, transactionManager);

			// MapperScannerConfigurer
			AbstractBeanDefinition mapperScannerConfigurer = BeanDefinitionBuilder
					.genericBeanDefinition(MapperScannerConfigurer.class)
					.addPropertyValue("sqlSessionFactoryBeanName", sqlSessionFactoryName)
					.addPropertyValue("basePackage", "com.example.dynamicmapperscanner*")
					.addPropertyValue("annotationClass", v.getOrder().get())
					.getBeanDefinition();

			beanFactory.registerBeanDefinition(mapperScannerName, mapperScannerConfigurer);

		});
	}

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {

	}
}
