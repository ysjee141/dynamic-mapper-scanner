package com.example.dynamicmapperscanner.a;

import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.StaticApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;
import java.util.Objects;
import java.util.Random;

/**
 * FactoryBean
 *
 * @author JI YOONSEONG
 **/
public class FactoryBean {

  private static final Random random = new Random();

  public MapperScannerConfigurer createInstance(
      ApplicationContext context,
      DataSource dataSource, String basePackage
  ) throws Exception {
    MapperScannerConfigurer configurer = new MapperScannerConfigurer();
    configurer.setBasePackage(basePackage);
    configurer.setSqlSessionFactoryBeanName(
        createSessionFactory(dataSource, context)
    );

    return configurer;
  }

  private String createSessionFactory(DataSource dataSource, ApplicationContext context) throws Exception {
    SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
    Resource[] resources = new PathMatchingResourcePatternResolver().getResources("classpath*:mapper/a/*.xml");
    sqlSessionFactoryBean.setMapperLocations(resources);
    sqlSessionFactoryBean.setDataSource(dataSource);

    String factoryBeanName = String.valueOf((char) ((int) (random.nextInt(26)) + 97));
    ((StaticApplicationContext)context).getBeanFactory()
        .registerSingleton(
            factoryBeanName,
            Objects.requireNonNull(sqlSessionFactoryBean.getObject())
        );

    return factoryBeanName;
  }

}
