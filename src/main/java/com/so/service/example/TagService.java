package com.so.service.example;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.so.mybatis.example.mapper.TagMapper;


@Service
public class TagService {

	@Resource
	private TagMapper tagMapper;

	public List<Map<String,Object>> getTags() {
		return tagMapper.selectTags();
	}

	public Map<String,Object> getTagById(String tagId) {
		return tagMapper.selectTagById(tagId);
	}
}
