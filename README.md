# dynamic-mapper-scanner-sample

다중 데이터 소스를 통한 동적 MyBatis Mapper Scanner를 생성하기 위한 테스트 프로젝트

`BeanDefinitionRegistryPostProcessor`를 통해 어플리케이션 시작시 선언된 Data Source를 통해 동적으로 
MyBatis Mapper를 생성함

## 프로젝트 동작 흐름
1. YML에 DataSource 지정
    ```yaml
    datasource3:
      - name: datasourceOne                                     // Data Source 이름
        order: 1                                                // Data Source 순서 (@MapperOne, @MapperTow... Annotation과 연동)
        data-source:
          jdbcUrl: jdbc:h2:file:d:/test_db;AUTO_SERVER=TRUE     // JDBC URL
          username: test                                        // JDBC Username
          password: test                                        // JDBC Password
          driverClassName: org.h2.Driver                        // JDBC Drive Class
          maximumPoolSize: 20                                   // JDBC Max Pool Size
    ```
2. `MapperBeanPostProcessor` 클래스를 Bean으로 지정
    ```java
    @Configuration
    @ConditionalOnPropertyForList(prefix = MapperConfig.PROPERTY_PREFIX)
    public class MapperConfig {
    
        static final String PROPERTY_PREFIX = "datasource3";
    
        @Bean
        public BeanDefinitionRegistryPostProcessor postProcessor(Environment environment) {
            try {
                /*return new PostProcessor(
                        RepoConfig.init(environment, MapperConfig.PROPERTY_PREFIX)
                );*/
                return new MapperBeanPostProcessor(
                        RepoConfig.init(environment, MapperConfig.PROPERTY_PREFIX)
                );
            } catch (NoSuchElementException e) {
                e.printStackTrace();
            }
            return null;
        }
    
    }
    ```
3. Mapper Interface에 사용할 Data Source에 맞는 Annotation 설정
    ```java
    @MapperOne
    public interface SampleMapper {
    
      List<SampleVo> getNames();
    
    }
    ```
4. Service 또는 Controller 클래스에서 @Autowired 설정하여 사용