package com.example.dynamicmapperscanner.controller;

import com.example.dynamicmapperscanner.data.SampleMapper;
import com.example.dynamicmapperscanner.data.SampleVo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * RestController
 *
 * @author JI YOONSEONG
 **/
@RestController
@RequestMapping(value = "/api")
public class RestCtrl {

  final SampleMapper mapper;

  public RestCtrl(SampleMapper mapper) {
    this.mapper = mapper;
  }

  @GetMapping(value = "/sample")
  public List<SampleVo> get() {
    return mapper.getNames();
  }

}
