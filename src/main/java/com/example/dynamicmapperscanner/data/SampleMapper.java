package com.example.dynamicmapperscanner.data;

import com.example.dynamicmapperscanner.annotation.MapperOne;

import java.util.List;

/**
 * Sample Mapper
 *
 * @author JI YOONSEONG
 **/
@MapperOne
public interface SampleMapper {

  List<SampleVo> getNames();

}
