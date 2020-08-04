package com.example.dynamicmapperscanner.enums;

import com.example.dynamicmapperscanner.annotation.MapperOne;
import com.example.dynamicmapperscanner.annotation.MapperTwo;

import java.lang.annotation.Annotation;
import java.util.Arrays;

/**
 * Mapper Order를 Property에 지정하기 위한 열거형
 *
 * @author JI YOONSEONG
 **/
public enum MapperOrder {

	NONE(-1), MAPPER_ONE(1), MAPPER_TWO(2);

	private final int order;

	MapperOrder(int order) {
		this.order = order;
	}

	public Class<? extends Annotation> get() {
		Class<? extends Annotation> result = null;
		switch (order) {
			case 1:
				result = MapperOne.class;
				break;
			case 2:
				result = MapperTwo.class;
				break;
		}

		return result;
	}

	public static MapperOrder value(int order) {
		return Arrays.stream(MapperOrder.values())
				.filter(item -> item.order == order)
				.findAny()
				.orElse(null);
	}

}
