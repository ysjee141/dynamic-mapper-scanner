package com.example.dynamicmapperscanner;

import com.example.dynamicmapperscanner.b.b;
import com.example.dynamicmapperscanner.b.test;
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
@RequestMapping(value = "/a")
public class RestCtrl {

  final b mapper;

  public RestCtrl(b mapper) {
    this.mapper = mapper;
  }

  @GetMapping(value = "/b")
  public List<test> get() {
    return mapper.getNames();
  }

}
