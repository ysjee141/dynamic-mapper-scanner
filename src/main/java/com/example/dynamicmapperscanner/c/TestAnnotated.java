package com.example.dynamicmapperscanner.c;

import com.example.dynamicmapperscanner.a.MapperOne;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import javax.annotation.PostConstruct;
import java.util.Set;

public class TestAnnotated extends ClassPathBeanDefinitionScanner {

	public TestAnnotated(BeanDefinitionRegistry registry) {
		super(registry);
		init();
	}

	public void init() {
		resetFilters(false);
		addIncludeFilter(new AnnotationTypeFilter(MapperOne.class));
	}

	@Override
	protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
		AnnotationMetadata metadata = beanDefinition.getMetadata();
		return metadata.isInterface() && metadata.isIndependent();
	}

	public void scan(BeanDefinitionRegistry registry) {
		Set<BeanDefinitionHolder> result =  super.doScan("com.example.dynamicmapperscanner");

		for(BeanDefinitionHolder holder : result) {
			GenericBeanDefinition definition = (GenericBeanDefinition)holder.getBeanDefinition();
			definition.getConstructorArgumentValues().addGenericArgumentValue(definition.getBeanClassName());
			definition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
			definition.setLazyInit(true);
			registry.registerBeanDefinition(definition.getBeanClassName(), definition);
		}
	}
}
