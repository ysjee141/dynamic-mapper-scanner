package com.example.dynamicmapperscanner.a;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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

}
