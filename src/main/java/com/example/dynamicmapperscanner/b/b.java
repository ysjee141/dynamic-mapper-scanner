package com.example.dynamicmapperscanner.b;

import com.example.dynamicmapperscanner.a.MapperOne;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * b
 *
 * @author JI YOONSEONG
 **/
@MapperOne
public interface b {

  List<test> getNames();

}
