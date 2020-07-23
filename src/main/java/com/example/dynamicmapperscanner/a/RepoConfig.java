package com.example.dynamicmapperscanner.a;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * RepoConfig
 *
 * @author JI YOONSEONG
 **/
/*@Component
@ConfigurationProperties(prefix = "datasource2")
public class RepoConfig {

  private List<DsConfig> source = new ArrayList<>();

  public List<DsConfig> getSource() {
    return source;
  }

  public void setSource(List<DsConfig> source) {
    this.source = source;
  }

  public static class DsConfig {
    private String name;
    private Map<String, String> dataSource;

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public Map<String, String> getDataSource() {
      return dataSource;
    }

    public void setDataSource(Map<String, String> dataSource) {
      this.dataSource = dataSource;
    }
  }

}*/
/*@Component
@ConfigurationProperties(prefix = "datasource3")*/
public class RepoConfig extends ArrayList<RepoConfig.DsConfig> {

  public static class DsConfig {
    private String name;
    private Map<String, String> dataSource;

    private MapperOrder order;

    public MapperOrder getOrder() {
      return order;
    }

    public void setOrder(MapperOrder order) {
      this.order = order;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public Map<String, String> getDataSource() {
      return dataSource;
    }

    public void setDataSource(Map<String, String> dataSource) {
      this.dataSource = dataSource;
    }
  }

}