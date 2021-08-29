package com.so.mybatis.example.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface CategoryMapper {

	List<Map<String, Object>> selectCategories();
}
