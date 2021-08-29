package com.so.service.example;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.so.mybatis.example.mapper.CategoryMapper;


@Service
public class CategoryService {

	@Resource
	private CategoryMapper categoryMapper;

	public List<Map<String,Object>> getCategories() {
		return categoryMapper.selectCategories();
	}
}
