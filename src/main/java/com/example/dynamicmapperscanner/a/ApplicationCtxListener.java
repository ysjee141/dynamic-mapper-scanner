package com.example.dynamicmapperscanner.a;

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
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

/**
 * ApplicationCtxListener
 *
 * @author JI YOONSEONG
 **/
@Component
public class ApplicationCtxListener {

//  @EventListener
  public void onApplicationEvent(ContextRefreshedEvent event) {

    ApplicationContext context = event.getApplicationContext();
    RepoConfig config = context.getBean(RepoConfig.class);
    BeanDefinitionRegistry beanFactory =
        (BeanDefinitionRegistry) ((GenericApplicationContext) context).getBeanFactory();

    beanFactory.removeBeanDefinition("sqlSessionFactory");
    beanFactory.removeBeanDefinition("sqlSessionTemplate");

   /* // MyBatis Configuration
    AbstractBeanDefinition myBatisConfiguration = BeanDefinitionBuilder
        .genericBeanDefinition(Configuration.class)
        .addPropertyValue("mapUnderscoreToCamelCase", true)
        .getBeanDefinition();*/

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

//    beanFactory.registerBeanDefinition("MyBatisConfiguration", myBatisConfiguration);

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
          .addPropertyReference("sqlSessionFactoryBeanName", sqlSessionFactoryName)
          .addPropertyValue("basePackage", "com.example.dynamicmapperscanner*")
          .addPropertyValue("annotationClass", v.getOrder().get())
          .getBeanDefinition();

      beanFactory.registerBeanDefinition(mapperScannerName, mapperScannerConfigurer);

    });
  }
}
