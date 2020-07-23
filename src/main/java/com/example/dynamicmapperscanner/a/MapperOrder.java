package com.example.dynamicmapperscanner.a;

import java.lang.annotation.Annotation;

/**
 * MapperOrder
 *
 * @author JI YOONSEONG
 **/
public enum MapperOrder {

  MAPPER_ONE(1), MAPPER_TWO(2);

  private final int order;
  MapperOrder(int order) {
    this.order = order;
  }

  public Class<? extends Annotation> get() {
    Class<? extends Annotation> result = null;
    switch(order) {
      case 1:
        result = MapperOne.class;
        break;
      case 2:
        result = MapperTwo.class;
        break;
    }

    return result;
  }

}
