package com.example.dynamicmapperscanner.a;

import com.example.dynamicmapperscanner.c.PostProcessor;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.swing.*;

/**
 * MapperConfig
 *
 * @author JI YOONSEONG
 **/
@Configuration
public class MapperConfig {

  /*@Bean(name = "datasourceOne")
  @Primary
  @ConfigurationProperties(prefix = "datasource.mappe-one")
  public DataSource dataSourceOne() {
    return DataSourceBuilder.create().build();
  }*/

  @Bean
  @ConfigurationProperties(prefix = "datasource3")
  public RepoConfig getRepoConfig() {
    return new RepoConfig();
  }

  @Bean
  public BeanDefinitionRegistryPostProcessor postProcessor(Environment environment) {
    return new PostProcessor(environment);
  }

  /*@Bean
  public MapperScannerConfigurer mapperScannerConfigurer() {
   *//* MapperScannerConfigurer configurer = new MapperScannerConfigurer();
    configurer.setBasePackage("com.example.dynamicmapperscanner*");
    configurer.setAnnotationClass(MapperOne.class);
    return configurer;*//*
    return null
  }*/

}
