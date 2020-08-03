package com.example.dynamicmapperscanner.b;

import com.example.dynamicmapperscanner.a.MapperOne;
import com.example.dynamicmapperscanner.a.MapperTwo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * b
 *
 * @author JI YOONSEONG
 **/
@MapperTwo
public interface b {

  List<test> getNames();

}
