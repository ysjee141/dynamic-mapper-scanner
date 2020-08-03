package com.example.dynamicmapperscanner.a;

import com.example.dynamicmapperscanner.c.TestAnnotated;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * MapperOne
 *
 * @author JI YOONSEONG
 **/
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(TestAnnotated.class)
@Component
public @interface MapperOne {
}
