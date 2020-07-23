package com.example.dynamicmapperscanner.a;

import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * MapperConfig2
 *
 * @author JI YOONSEONG
 **/
@Configuration
@AutoConfigureAfter(MapperConfig.class)
public class MapperConfig2 {

 /* @Autowired
  DataSource dataSourceOne;

  @Bean
  public MapperScannerConfigurer mapperScannerConfigurer() throws Exception {
    *//*MapperScannerConfigurer configurer = new MapperScannerConfigurer();
    configurer.setBasePackage("com.example.dynamicmapperscanner");
    configurer.setSqlSessionFactoryBeanName("SessionFactoryOne");
    return configurer;*//*
    return FactoryBean.createInstance(dataSourceOne, "com.example.dynamicmapperscanner");
  }*/

}
