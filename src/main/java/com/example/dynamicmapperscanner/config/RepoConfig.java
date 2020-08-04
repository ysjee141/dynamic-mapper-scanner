package com.example.dynamicmapperscanner.config;

import com.example.dynamicmapperscanner.enums.MapperOrder;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.core.env.Environment;

import java.util.ArrayList;
import java.util.Map;

/**
 * YML 내 Data Source Property Class
 *
 * @author JI YOONSEONG
 **/
public class RepoConfig extends ArrayList<RepoConfig.DsConfig> {

  public static RepoConfig init(Environment environment, String prefix) {
    return Binder.get(environment).bind(prefix, RepoConfig.class).get();
  }

  /**
   * MyBatis Mapper 설정을 위한 Bean Instance Class Type 정의
   */
  public enum BEAN_TYPE {
    DATASOURCE("DataSource"),
    SESSION_FACTORY("SessionFactory"),
    SESSION_TEMPLATE("SessionTemplate"),
    TRANSACTION_MANAGER("TransactionManager"),
    MAPPER_SCANNER("MapperScanner");

    private final String suffix;

    BEAN_TYPE(String suffix) {
      this.suffix = suffix;
    }

    public String getSuffix() {
      return this.suffix;
    }

  }

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

    public String getName(BEAN_TYPE beanType) {
      return this.name.concat(beanType.getSuffix());
    }

    public String getMapperLocation() {
      return "classpath*:/mapper/" + this.name + "/*.xml";
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