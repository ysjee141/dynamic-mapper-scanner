package com.example.dynamicmapperscanner.config;

import com.example.dynamicmapperscanner.annotation.ConditionalOnPropertyForList;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.NoSuchElementException;

/**
 * MyBatis Mapper Auto Configuration 설정
 *
 * @author JI YOONSEONG
 **/
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